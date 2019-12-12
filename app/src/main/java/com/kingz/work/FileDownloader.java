package com.kingz.work;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kingz.net.OkHttpClientManager;
import com.kingz.utils.ExecutorServiceHelper;
import com.kingz.utils.UriUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 文件下载器，具备断点下载功能。
 *
 * <p>
 * |---RandomAccessFile介绍：
 *      java的RandomAccessFile提供对文件的读写功能，
 *  与普通的输入输出流不一样的是RamdomAccessFile可以任意的访问文件的任何地方。
 *
 *  RandomAccessFile的对象包含一个记录指针，用于标识当前流的读写位置，
 *  这个位置可以向前移动，也可以向后移动。
 *      --- long getFilePoint():记录文件指针的当前位置。
 *      --- void seek(long pos):将文件记录指针定位到pos位置。
 *
 *  RandomAccessFile包含InputStream的三个read方法，也包含OutputStream的三个write方法。
 *  同时RandomAccessFile还包含一系列的readXXX和writeXXX方法完成输入输出
 */
@SuppressWarnings("WeakerAccess")
public class FileDownloader implements Runnable {
    private static final String TAG = "FileDownloader";
    private volatile Handler _handler;
    private volatile OkHttpClientManager okClient = null;
    private String download_url;
    private RandomAccessFile raFile;
    private File _file_info;
    private long _resume_pos;
    private byte[] _resume_sign;
    private volatile long _file_size;
    private volatile long _file_write_pos;

    /**
     * 启动下载(重头开始)
     *
     * @param url        文件url
     * @param local_file 本地保存file
     * @param handler    Handler
     * @return 是否下载成功
     */
    public boolean start(String url, File local_file, Handler handler) {
        return start(url, local_file, false, handler);
    }

    public boolean start(String url, File local_file, boolean resume, Handler handler) {
        if (okClient != null) {
            return false;
        }
        okClient = OkHttpClientManager.getInstance();
        try {
            _file_info = local_file;
            raFile = new RandomAccessFile(_file_info, "rw"); //已读写方式打开文件，不存在就创建新文件
            long file_size = raFile.length();
            if (resume && file_size > 512) {
                raFile.seek(file_size - 512);
                byte[] buffer = new byte[512];
                raFile.read(buffer);
                _resume_sign = buffer;
                _resume_pos = file_size - 512;
            } else {
                _resume_sign = null;
                _resume_pos = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        _handler = handler;
        download_url = url;
        ExecutorServiceHelper.getInstance().execute(this);
        return true;
    }

    public boolean resume() {
        if (okClient == null) {
            return false;
        }
        Handler handler = _handler;
        File local_file = _file_info;
        String url = download_url;
        stop();
        return start(url, local_file, true, handler);
    }

    public boolean stop() {
        if (okClient == null) {
            return false;
        }
        _handler = null;
        if (raFile != null) {
            try {
                raFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        okClient = null;
        return true;
    }

    public float getProgress() {
        if (_file_size > 0) {
            return ((float) _file_write_pos) / _file_size;
        }
        return 0.0f;
    }

    // -----------------------------
    public static final int STARTING = 0x01;
    public static final int RECIVING = 0x02;
    public static final int PROGRESSING = 0x03;
    public static final int ERROR = 0x04;
    public static final int FINISHED = 0x05;
    static final String MSG_NORMAL = "normal";
    static final String MSG_URL_INVALID = "url-invalid";
    static final String MSG_INTERNAL_ERROR = "network-error";
    static final String MSG_RESUME_CHECK_FAILED = "resume-check-failed";
    static final String MSG_CANNOT_OPEN_LOCALFILE = "open-localfile-failed";
    static final String MSG_IO_ERROR = "io-error";
    static final String MSG_HTTP_RESPONSE_ERROR = "http-code-error";

    private void sendMessage(int state) {
        sendMessage(state, MSG_NORMAL);
    }

    private void sendMessage(int state, Object obj) {
        if (_handler != null) {
            Message msg = Message.obtain();
            msg.what = state;
            msg.obj = obj;
            _handler.removeMessages(state);
            _handler.sendMessage(msg);
        }
    }

    // -----------------------------
    @Override
    public void run() {
        try {
            Log.d(TAG, "start downloading... " + download_url);
            if (UriUtils.isValid(new URI(download_url))) {
                return;
            }

            this.sendMessage(STARTING);
            Response resp = sendAsynRequest();
            long code = resp.code();
            if (code != 200 && code != 206) {
                Log.e(TAG, "HTTP error!! code=" + code);
                this.sendMessage(ERROR);
                return;
            }
            this.sendMessage(RECIVING);

            ResponseBody body = resp.body();
            if (body == null) {
                Log.e(TAG, "Response body is empty!! ");
                this.sendMessage(ERROR, MSG_HTTP_RESPONSE_ERROR);
                return;
            }

            InputStream is = body.byteStream();
            long total_size = body.contentLength();
            this._file_size = total_size;
            long read_bytes = 0;

            if (_resume_pos > 0 && code == 206) {
                Log.d(TAG, "check content-range...");
                // resume downloading
                Headers headers = resp.headers();
                if (headers.size() < 1) {
                    Log.e(TAG, "resume: HTTP error, no content-range header!");
                    this.sendMessage(ERROR, MSG_INTERNAL_ERROR);
                    return;
                }
                {

                    String content_range_val = headers.get("content-range");
                    Pattern pattern = Pattern.compile("^bytes\\s+(\\d+)-(\\d*)/(\\d+)$", Pattern.CASE_INSENSITIVE);
                    Matcher m = pattern.matcher(content_range_val);
                    if (!m.matches()) {
                        Log.e(TAG, "resume: unsupported content-range:" + content_range_val);
                        this.sendMessage(ERROR, MSG_INTERNAL_ERROR);
                        return;
                    }
                    long begin_pos = Long.parseLong(m.group(1));
                    long end_pos = Long.parseLong(m.group(2));
                    long file_length = Long.parseLong(m.group(3));
                    if (begin_pos != _resume_pos) {
                        Log.e(TAG, "resume: file range invalid");
                        this.sendMessage(ERROR, MSG_INTERNAL_ERROR);
                        return;
                    }
                    _file_size = file_length;
                    byte[] sign_buffer = new byte[_resume_sign.length];
                    int size = 0;
                    while (size < _resume_sign.length) {
                        if (Thread.interrupted()) {
                            Log.w(TAG, "download interrupted!!");
                            return;
                        }
                        size += is.read(sign_buffer, size, _resume_sign.length - size);
                    }
                    read_bytes += size;
                    if (size != _resume_sign.length || !Arrays.equals(sign_buffer, _resume_sign)) {
                        Log.e(TAG, "resume data check failed!!");
                        this.sendMessage(ERROR, MSG_RESUME_CHECK_FAILED);
                        return;
                    }
                    Log.d(TAG, "continue download from " + _resume_pos);
                }
            } else {
                Log.d(TAG, "download from begining...");
                raFile.seek(0);
            }
            byte[] read_cache = new byte[2048];
            float percentage = 0f;
            while (read_bytes < total_size) {
                long max_len = Math.min(read_cache.length, total_size - read_bytes);
                if (Thread.interrupted()) {
                    Log.w(TAG, "download interrupted!!");
                    return;
                }
                int size = is.read(read_cache, 0, (int) max_len);
                raFile.write(read_cache, 0, size);
                read_bytes += size;
                _file_write_pos = raFile.getFilePointer();
                if (getProgress() - percentage > 0.10f) {
                    percentage = getProgress();
                    this.sendMessage(PROGRESSING, percentage);
                }

            }
            _file_write_pos = raFile.getFilePointer();
            raFile.setLength(_file_write_pos);
            raFile.close();
            raFile = null;
            Log.d(TAG, "download finished!");
            this.sendMessage(FINISHED);
            okClient = null;
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            this.sendMessage(ERROR);
        }
    }

    /**
     * 发送同步请求
     *
     * @return Response
     */
    private Response sendAsynRequest() throws IOException {
        Request.Builder request = okClient.getRequestBuilder();
        request.url(download_url);
        if (_resume_pos > 0) {
            Log.d(TAG, "resuming... bytes=" + _resume_pos + "-");
            request.header("Range", "bytes=" + Long.toString(_resume_pos) + "-");
        }
        okClient.getAsyn(request.build());
        return okClient.getAsyn(request.build());
    }


}