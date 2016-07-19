package com.utils;

import android.net.http.AndroidHttpClient;
import android.os.Handler;
import android.util.Log;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件下载器
 *
 *
 * |---RandomAccessFile介绍：
 *      java的RandomAccessFile提供对文件的读写功能，与普通的输入输出流不一样的
 *   是RamdomAccessFile可以任意的访问文件的任何地方。
 *   RandomAccessFile的对象包含一个记录指针，用于标识当前流的读写位置，
 *   这个位置可以向前移动，也可以向后移动。
 *   long getFilePoint():记录文件指针的当前位置。
 *   void seek(long pos):将文件记录指针定位到pos位置。
 *   RandomAccessFile包含InputStream的三个read方法，也包含OutputStream的三个write方法。
 *   同时RandomAccessFile还包含一系列的readXxx和writeXxx方法完成输入输出
 */
public class FileDownloader implements Runnable {
	private Thread downloadThread = null;
	private volatile Handler _handler;
	private volatile AndroidHttpClient _client = null;
	private String download_url;
	private RandomAccessFile raFile;
	private File _file_info;
	private long _resume_pos;
	private byte[] _resume_sign;
	private StatusLine _status;
	private volatile long _file_size;
	private volatile long _file_write_pos;
	private int _error_status;

	// -----------------------------
	public boolean start(String url, File local_file, Handler handler) {
		return start(url, local_file, false, handler);
	}

	public boolean start(String url, File local_file, boolean resume, Handler handler) {
		if (_client != null) {
			return false;
		}
		_error_status = ERR_NO_ERROR;
//		_client = AndroidHttpClient.newInstance("Starcor upgrade service");
		_client = AndroidHttpClient.newInstance("default");
		downloadThread = new Thread(this);
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
		} catch (FileNotFoundException e) {
			_error_status = ERR_CANNOT_OPEN_LOCALFILE;
			Log.e("file_downloader.start", "file not found exception");
			return false;
		} catch (IOException e) {
			_error_status = ERR_IO_ERROR;
			Log.e("file_downloader.start", "io exception");
			return false;
		}

		_handler = handler;
		download_url = url;
		downloadThread.start();
		_status = null;
		return true;
	}

	public boolean resume() {
		if (_client == null) {
			return false;
		}
		Handler handler = _handler;
		File local_file = _file_info;
		String url = download_url;
		stop();
		return start(url, local_file, true, handler);
	}

	public boolean stop() {
		if (_client == null) {
			return false;
		}
		_handler = null;
		try {
			downloadThread.interrupt();
			downloadThread.join();
			downloadThread = null;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (raFile != null) {
			try {
				raFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (_client != null) {
			_client.close();
		}
		_client = null;
		return true;
	}

	public float getProgress() {
		if (_file_size > 0) {
			return ((float) _file_write_pos) / _file_size;
		}
		return 0.0f;
	}

	public int getError() {
		return _error_status;
	}
	// -----------------------------
	public static final int MSG_STARTING = 1;
	public static final int MSG_ERROR = 2;
	public static final int MSG_FINISHED = 3;
	public static final int MSG_RECIVING = 4;
	public static final int MSG_PROGRESSING = 5;

	// ---------------状态机--------------
	static final int ERR_NO_ERROR = 0x00;   					//正常
	static final int ERR_INTERNAL_ERROR = 0x01;				//网络异常
	static final int ERR_RESUME_CHECK_FAILED = 0x010;  		//重试检查失败
	static final int ERR_CANNOT_OPEN_LOCALFILE = 0x011;		//无法打开本地文件
	static final int ERR_IO_ERROR = 0x012;						//IO异常
	static final int ERR_HTTP_RESPONSE_ERROR = 0x013;			//HTTP返回错误

	private void sendMessage(int msg) {
		if (_handler != null) {
			_handler.removeMessages(msg);
			_handler.sendEmptyMessage(msg);
		}
	}

	// -----------------------------
	@Override
	public void run() {
		try {
			Log.d("file_downloader.run", "start downloading... " + download_url);
			URI uri = new URI(download_url);
			if ( uri.getHost() == null || uri.getPath() == null ) {
				Log.e("file_downloader.run", "URI no host/path error!! " + download_url);
				this._error_status = ERR_INTERNAL_ERROR;
				this.sendMessage(MSG_ERROR);
				return;
			}
			HttpGet get_url = new HttpGet(uri);
			if (_resume_pos > 0) {
				Log.d("file_downloader.run", "resuming... bytes=" + _resume_pos + "-");
				get_url.setHeader("Range", "bytes=" + Long.toString(_resume_pos) + "-");
			}
			HttpResponse resp;
			this.sendMessage(MSG_STARTING);
			resp = _client.execute(get_url);
			_status = resp.getStatusLine();
			long code = _status.getStatusCode();
			Log.d("file_downloader.run", "response " + code + " " + _status.getReasonPhrase());
			if (code != 200 && code != 206) {
				Log.e("file_downloader.run", "HTTP error!! ");
				this._error_status = ERR_HTTP_RESPONSE_ERROR;
				this.sendMessage(MSG_ERROR);
				return;
			}
			this.sendMessage(MSG_RECIVING);
			HttpEntity entity = resp.getEntity();
			InputStream is = entity.getContent();
			long total_size = entity.getContentLength();
			this._file_size = total_size;
			long read_bytes = 0;

			if (_resume_pos > 0 && code == 206) {
				Log.d("file_downloader.run", "check content-range...");
				// resume downloading
				Header[] header = resp.getHeaders("Content-Range");
				if (header == null || header.length != 1) {
					Log.e("file_downloader.run", "resume: HTTP error, no content-range header!");
					this._error_status = ERR_INTERNAL_ERROR;
					this.sendMessage(MSG_ERROR);
					return;
				}
				{
					String content_range_val = header[0].getValue();
					Pattern pattern = Pattern.compile("^bytes\\s+(\\d+)-(\\d*)/(\\d+)$", Pattern.CASE_INSENSITIVE);
					Matcher m = pattern.matcher(content_range_val);
					if (!m.matches()) {
						Log.e("file_downloader.run", "resume: unsupported content-range:" + content_range_val);
						this._error_status = ERR_INTERNAL_ERROR;
						this.sendMessage(MSG_ERROR);
						return;
					}
					long begin_pos = Long.parseLong(m.group(1));
					long end_pos = Long.parseLong(m.group(2));
					long file_length = Long.parseLong(m.group(3));
					if (begin_pos != _resume_pos) {
						Log.e("file_downloader.run", "resume: file range invalid");
						this._error_status = ERR_RESUME_CHECK_FAILED;
						this.sendMessage(MSG_ERROR);
						return;
					}
					_file_size = file_length;
					byte[] sign_buffer = new byte[_resume_sign.length];
					int size = 0;
					while (size < _resume_sign.length) {
						if (Thread.interrupted()) {
							Log.w("file_downloader.run", "download interrupted!!");
							return;
						}
						size += is.read(sign_buffer, size, _resume_sign.length - size);
					}
					read_bytes += size;
					if (size != _resume_sign.length || !Arrays.equals(sign_buffer, _resume_sign)) {
						Log.e("file_downloader.run", "resume data check failed!!");
						this._error_status = ERR_RESUME_CHECK_FAILED;
						this.sendMessage(MSG_ERROR);
						return;
					}
					Log.d("file_downloader.run", "continue download from " + _resume_pos);
				}
			} else {
				Log.d("file_downloader.run", "download from begining...");
				raFile.seek(0);
			}
			byte[] read_cache = new byte[2048];
			float percentage=0f;
			while (read_bytes < total_size) {
				long max_len = Math.min(read_cache.length, total_size - read_bytes);
				if (Thread.interrupted()) {
					Log.w("file_downloader.run", "download interrupted!!");
					return;
				}
				int size = is.read(read_cache, 0, (int) max_len);
				raFile.write(read_cache, 0, size);
				read_bytes += size;
				_file_write_pos = raFile.getFilePointer();
				if(getProgress()-percentage>0.10f){
					percentage=getProgress();
					this.sendMessage(MSG_PROGRESSING);
				}
			}
			_file_write_pos = raFile.getFilePointer();
			raFile.setLength(_file_write_pos);
			raFile.close();
			raFile = null;
			_client.close();
			Log.d("file_downloader.run", "download finished!");
			this.sendMessage(MSG_FINISHED);
			_client = null;
		} catch (IOException e) {
			Log.e("file_downloader.run", "io exception");
			this._error_status = ERR_IO_ERROR;
			this.sendMessage(MSG_ERROR);
		} catch (NumberFormatException e) {
			Log.e("file_downloader.run", "number format exception");
			this._error_status = ERR_INTERNAL_ERROR;
			this.sendMessage(MSG_ERROR);
		} catch (URISyntaxException e) {
			Log.e("file_downloader.run", "URI syntax error!! " + download_url);
			this._error_status = ERR_INTERNAL_ERROR;
			this.sendMessage(MSG_ERROR);
		} catch (Exception e) {
			Log.e("file_downloader.run", "unknown exception!! " + e.getMessage());
			this._error_status = ERR_INTERNAL_ERROR;
			this.sendMessage(MSG_ERROR);
		}
	}
}