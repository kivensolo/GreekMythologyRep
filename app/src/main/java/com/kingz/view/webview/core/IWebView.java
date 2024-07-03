package com.kingz.view.webview.core;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WebView能力增强接口
 **/
public interface IWebView {
    AtomicInteger evalJsIndex = new AtomicInteger(0);
    Map<Integer, String> jsReturnValues = new HashMap<Integer, String>();

    void bringToFront();

    void setBackgroundColor(int color);

    String _waitForJsReturnValue(int index, int i);

    void loadUrl(String jsCallbackUrl);

    Context getViewContext();

    /**
     * Android侧回调Js的CallBack
     */
    class JSCallback {
        public static final String JS_EXPOSE_NAME = "JsExtApollo"; //starcorExt
        private static final String TAG = "IWebView.JSCallback";
        private IWebView extWebView;

        public JSCallback(IWebView extWebView) {
            this.extWebView = extWebView;
        }

        public String jsMethodName;

        public String run(Object... args) {
            if (Looper.getMainLooper().getThread().getId() != Thread.currentThread().getId()) {
                throw new IllegalThreadStateException("JSCallback must invoked in UI Thread!!!");
            }
            ArrayList<Pair<String, Object>> argsList = new ArrayList<Pair<String, Object>>();
            for (Object arg : args) {
                if (arg instanceof Integer
                        || arg instanceof Double
                        || arg instanceof Long
                        || arg instanceof Float
                        || arg instanceof Short
                        || arg instanceof Byte) {
                    argsList.add(Pair.create("number", arg));
                } else if (arg instanceof Boolean) {
                    argsList.add(Pair.create("boolean", arg));
                } else if (arg instanceof CharSequence
                        || arg instanceof Character) {
                    argsList.add(Pair.create("string", arg));
                } else if (arg instanceof JSONObject) {
                    argsList.add(Pair.create("object", arg));
                }
            }
            String argsJson = buildJson(argsList.toArray(new Pair[argsList.size()]));
            Log.d(TAG, "invokeCallback argsJson= " + argsJson);
            return invokeJSCallback(jsMethodName, argsJson);
        }

        private String invokeJSCallback(final String methodName, final String args) {
            final int index = extWebView.evalJsIndex.incrementAndGet();
            synchronized (extWebView.jsReturnValues) {
                extWebView.jsReturnValues.put(index, null);
            }
            String jsCallback = String.format("javascript:%s._invokeCallback(\"%s\", %s, %d)", JS_EXPOSE_NAME, methodName, args, index);
            Log.d(TAG, "jsCallback = " + jsCallback);
            extWebView.loadUrl(jsCallback);
            return extWebView._waitForJsReturnValue(index, 1000);
        }

        private String buildJson(Pair<String, Object>... jsonArgs) {
            JSONStringer jsonStringer = new JSONStringer();
            try {
                jsonStringer.array();
                for (Pair<String, Object> arg : jsonArgs) {
                    jsonStringer.object()
                            .key("type").value(arg.first)
                            .key("value").value(arg.second)
                            .endObject();
                }
                jsonStringer.endArray();
            } catch (JSONException e) {

            }
            return jsonStringer.toString();
        }
    }
}
