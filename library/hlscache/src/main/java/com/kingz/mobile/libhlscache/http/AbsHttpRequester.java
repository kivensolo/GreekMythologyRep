package com.kingz.mobile.libhlscache.http;

import com.kingz.mobile.libhlscache.EncryptInputStream;
import com.kingz.mobile.libhlscache.utils.ActionCallBack;
import com.kingz.mobile.libhlscache.utils.IOUtils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created 2017/11/8.
 */
public abstract class AbsHttpRequester {

    public abstract void enqueue(final String url, final ActionCallBack<IResponse> parser,
                                 final ActionCallBack<IOException> failCallback);

    public abstract IResponse execute(String url) throws IOException;

    public void downloadFile(String url, String filePath, boolean encrypt, ProgressCallback progressCallback) throws IOException {
        IResponse response = null;
        try {
            response = execute(url);
            InputStream in = response.getInputStream();
            if (encrypt) {
                in = new EncryptInputStream(in);
            }
            IOUtils.save2FileWithTmpFile(in, new File(filePath), progressCallback);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ignore) {
                }
            }
        }
    }

    public void asyncDownloadFile(
            String url, final String filePath, final ActionCallBack<String> success,
            final ActionCallBack<IOException> failCallback, ProgressCallback progressCallback) {
        asyncDownloadFile(url, filePath, success, failCallback, false, progressCallback);
    }

    public void asyncDownloadFile(
            String url, final String filePath, final ActionCallBack<String> success,
            final ActionCallBack<IOException> failCallback,
            final boolean encrypt, final ProgressCallback progressCallback) {
        enqueue(url, new ActionCallBack<IResponse>() {
            @Override
            public void call(IResponse response) {
                try {
                    InputStream in = response.getInputStream();
                    if (encrypt) {
                        in = new EncryptInputStream(in);
                    }
                    IOUtils.save2FileWithTmpFile(in, new File(filePath), progressCallback);
                    if (success != null) {
                        success.call(filePath);
                    }
                } catch (IOException e) {
                    if (failCallback != null) {
                        failCallback.call(e);
                    }
                } finally {
                    try {
                        response.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }, failCallback);
    }

    public interface IResponse extends Closeable {
        InputStream getInputStream();
    }

    public interface ProgressCallback {
        void onRead(int bytes);
    }
}
