package com.zeke.demo.device.bluetooth;

import android.text.TextUtils;

import androidx.collection.LruCache;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.cmhi.agedcare.common.ble.IBleDeviceReporter;
import com.cmhi.agedcare.common.ble.cache.ModelDiskCacheFactory;
import com.cmhi.agedcare.common.ble.cache.ModelMemoryCacheFactory;
import com.cmhi.agedcare.common.ble.model.Property;
import com.cmhi.agedcare.common.manager.AgedCareRequestManager;
import com.cmhi.agedcare.common.model.base.BluetoothInfo;
import com.cmhi.agedcare.common.thread.ThreadExecutorInstance;
import com.cmhi.agedcare.util.ByteUtil;
import com.cmhi.agedcare.util.DateUtil;
import com.cmhi.agedcare.util.MyLogger;
import com.cmhi.agedcare.util.listener.ICommonListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelReporter implements IBleDeviceReporter {
    private final static String TAG = "ModelReporter";
    private MyLogger Log = MyLogger.getLogger(TAG);
    protected final static int REPORT_COMMAND_ID_INDEX = 2;
    protected final static int REPORT_KEY_INDEX = 3;
    protected final static int REPORT_MIN_INDEX = REPORT_KEY_INDEX;

    protected BluetoothInfo mBluetoothInfo;
    protected String mDeviceType;
    protected DiskLruCache mDiskLruCache;
    protected LruCache<String, BluetoothInfo> mMemoryLruCache;
    protected ICommonListener mListener;

    public ModelReporter(String deviceType, ICommonListener listener) {
        mDeviceType = deviceType;
        mListener = listener;

        ThreadExecutorInstance.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                mDiskLruCache = new ModelDiskCacheFactory().createDiskLruCache();
                mMemoryLruCache = new ModelMemoryCacheFactory().createMemoryLruCache();

                //通过缓存获取
                boolean isNeedUpdate = false;
                mBluetoothInfo = mMemoryLruCache.get(deviceType);
                if (mBluetoothInfo == null) {
                    try {
                        DiskLruCache.Value value = mDiskLruCache.get(deviceType);
                        if (value != null) {
                            String bluetoothInfo = value.getString(0);
                            mBluetoothInfo = JSON.parseObject(bluetoothInfo, BluetoothInfo.class);
                            isNeedUpdate = true;
                        }
                    } catch (Exception e) {
                        Log.e("cache error:" + e.toString());
                    }
                }

                //通过网络获取
                if (mBluetoothInfo == null || isNeedUpdate) {
                    AgedCareRequestManager.getBluetoothInfo(deviceType, new ICommonListener<BluetoothInfo>() {
                        @Override
                        public void onSuccess(BluetoothInfo bluetoothInfo) {
                            mBluetoothInfo = bluetoothInfo;
                            if (mBluetoothInfo == null) {
                                Log.e("getBluetoothInfo error bluetoothInfo empty");
                                return;
                            }

                            //写入缓存
                            mMemoryLruCache.put(deviceType, mBluetoothInfo);
                            try {
                                DiskLruCache.Editor editor = mDiskLruCache.edit(deviceType);
                                editor.set(0, JSON.toJSONString(mBluetoothInfo));
                            } catch (Exception e) {
                                Log.e("save error:" + e.toString());
                            }
                        }

                        @Override
                        public void onFail(int errorCode, String errorMsg) {
                            Log.e("onFail errorCode" + +errorCode + " errorMsg:" + errorMsg);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onReport(String deviceId, List<byte[]> byteLists) {
        try {
            Map<String, String> values = getData(byteLists);
            AgedCareRequestManager.reportDevice(deviceId, values, mListener);
        } catch (Exception e) {
            Log.e("onReport e:" + e.toString());
        }
    }

    @Override
    public boolean isNeedReport(byte[] value) {
        if (mBluetoothInfo == null || mBluetoothInfo.command == null || TextUtils.isEmpty(mBluetoothInfo.command.uploadCommandId)) {
            Log.e("isNeedReport false mBluetoothInfo:" + mBluetoothInfo);
            return false;
        }
        try {
            int commandId = Integer.parseInt(mBluetoothInfo.command.uploadCommandId);
            return value != null
                    && value.length > REPORT_MIN_INDEX
                    && commandId == (value[REPORT_COMMAND_ID_INDEX] & 0xFF)
                    && mBluetoothInfo.command.uploadKey == (value[REPORT_KEY_INDEX] & 0xFF);
        } catch (Exception e) {
            Log.e("isNeedReport e:" + e.toString());
        }
        return false;
    }

    @Override
    public byte[] getSyncRequest() {
        if (mBluetoothInfo == null || mBluetoothInfo.command == null || TextUtils.isEmpty(mBluetoothInfo.command.uploadCommandId)) {
            Log.e("getSyncRequest error mBluetoothInfo:" + mBluetoothInfo);
            return null;
        }

        byte[] bytes = null;
        try {
            int length = 11 + (mBluetoothInfo.command.timeBegin ? 4 : 0);
            bytes = new byte[length];
            bytes[0] = 0x00;
            bytes[1] = (byte) (0x01);
            bytes[2] = (byte) Integer.parseInt(mBluetoothInfo.command.uploadCommandId);
            bytes[3] = (byte) mBluetoothInfo.command.uploadKey;
            bytes[4] = 0x00;
            byte[] byteTime = ProtocalUtil.getBytesTimeForSeconds(DateUtil.getUtcTime());
            bytes[5] = byteTime[0];
            bytes[6] = byteTime[1];
            bytes[7] = byteTime[2];
            bytes[8] = byteTime[3];
            bytes[9] = 0x00;
            bytes[10] = 0x01;
            if (mBluetoothInfo.command.timeBegin) {
                bytes[11] = byteTime[0];
                bytes[12] = byteTime[1];
                bytes[13] = byteTime[2];
                bytes[14] = byteTime[3];
                //16adbceea
            }
        } catch (Exception e) {
            Log.e("getSyncRequest e:" + e.toString());
        }

        return bytes;
    }

    public Map<String, String> getData(List<byte[]> byteLists) {
        if (mBluetoothInfo == null || mBluetoothInfo.command == null || mBluetoothInfo.model == null || mBluetoothInfo.model.properties == null) {
            Log.e("getData error mBluetoothInfo:" + mBluetoothInfo);
            return null;
        }
        Map<String, String> data = new HashMap<>();

        int size = 0;
        for (byte[] bytes : byteLists) {
            size += bytes.length;
        }

        size -= byteLists.size();
        byte[] bytes = new byte[size];
        int i = 0;
        for (byte[] bTmp : byteLists) {
            System.arraycopy(bTmp, 1, bytes, i, bTmp.length - 1);
            i += (bTmp.length - 1);
        }
        MyLogger.getLogger(TAG).d("sync combine data:" + ByteUtil.byteToString(bytes));

        i = 8;
        i += mBluetoothInfo.command.remainAmountLength;
        while (i < bytes.length) {
            int count = bytes[i + 2];
            short type = ByteUtil.getShort(bytes, i);
            Property property = mBluetoothInfo.model.getProperty(type);
            if (property != null) {
                Object value = property.getValue(bytes, i + 3, count);
                if (value != null) {
                    data.put(property.name, String.valueOf(value));
                }
            }
            i += count + 3;
        }

        return data;
    }
}
