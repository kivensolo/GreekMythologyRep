package com.zeke.assets;

import android.content.Context;
import android.util.Log;

import com.nioserver.R;

/**
 * author：ZekeWang
 * date：2021/2/1
 * description：
 */
public class DebugXul {
    private static final String TAG = DebugXul.class.getSimpleName();
    private static final int DEFAULT_PORT = 8085;
    private static AssetServer clientServer;

    private DebugXul() {
        // This class in not publicly instantiable
    }

    public static void initialize(Context context) {
        int portNumber;
        try {
            portNumber = Integer.parseInt(context.getString(R.string.PORT_NUMBER));
        } catch (NumberFormatException ex) {
            Log.e(TAG, "PORT_NUMBER should be integer", ex);
            portNumber = DEFAULT_PORT;
            Log.i(TAG, "Using Default port : " + DEFAULT_PORT);
        }

        clientServer = new AssetServer(context, portNumber);
        Log.d(TAG, NetworkUtils.getAddressLog(context, portNumber));
    }
}
