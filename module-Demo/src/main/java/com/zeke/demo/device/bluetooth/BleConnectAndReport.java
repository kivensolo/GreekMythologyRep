//package com.zeke.demo.device.bluetooth;
//
//import com.cmhi.agedcare.common.ble.message.BleDisconnectMessage;
//import com.cmhi.agedcare.common.constant.AppConstant;
//import com.cmhi.agedcare.common.model.base.AgedCareDevice;
//import com.cmhi.agedcare.common.provider.AppContextProvider;
//import com.cmhi.agedcare.common.push.DeviceMessage;
//import com.cmhi.agedcare.util.ByteUtil;
//import com.cmhi.agedcare.util.DateUtil;
//import com.cmhi.agedcare.util.ErrorUtils;
//import com.cmhi.agedcare.util.ProtocalUtil;
//import com.cmhi.agedcare.util.listener.ICommonListener;
//import com.cmhi.common.RxBus;
//import com.cmhi.common.util.SharePreferencesUtil;
//import com.inuker.bluetooth.library.BluetoothClient;
//import com.inuker.bluetooth.library.Constants;
//import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
//import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
//import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
//import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
//import com.inuker.bluetooth.library.model.BleGattProfile;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import android.bluetooth.BluetoothAdapter;
//import android.os.Handler;
//import android.os.Looper;
//import android.text.TextUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
//
//public class BleConnectAndReport {
//    private static final Logger logger = LoggerFactory.getLogger(BleConnectAndReport.class);
//
//    protected final BluetoothClient mBleClient;
//    protected final List<byte[]> byteLists = new ArrayList<>();
//    protected int count = 0;
//    protected BleConnectStatusListener mBleConnectStatusListener;
//
//    public BleConnectAndReport(BluetoothClient client) {
//        mBleClient = client;
//        mBleConnectStatusListener = new BleConnectStatusListener() {
//
//            @Override
//            public void onConnectStatusChanged(String mac, int status) {
//                logger.info("onConnectStatusChanged mac:" + mac + " status:" + status + " mBleConnectStatusListener:" + mBleConnectStatusListener);
//                if (status == Constants.STATUS_DISCONNECTED) {
//                    RxBus.getDefault().post(new DeviceMessage());
//                    RxBus.getDefault().post(new BleDisconnectMessage(mac));
//
//                    disconnect(mac);
//                }
//            }
//        };
//    }
//
//    public void connectAndNotify(AgedCareDevice device, IBleDeviceReporter reporter, ICommonListener<Object> listener, boolean needBind) {
//        if (listener == null) {
//            logger.error("connectAndNotify error listener empty");
//            return;
//        }
//
//        if (mBleClient == null
//                || device == null
//                || TextUtils.isEmpty(device.getMac())
//                || !BluetoothAdapter.checkBluetoothAddress(device.getMac())) {
//            logger.error("connectAndNotify params client or device empty error client: " + mBleClient + " device:" + device);
//            listener.onFail(ErrorUtils.ERROR_CODE_COMMON, ErrorUtils.ERROR_MSG_COMMON);
//            return;
//        }
//
//        mBleClient.registerConnectStatusListener(device.getMac(), mBleConnectStatusListener);
//
//        mBleClient.connect(device.getMac(), new BleConnectResponse() {
//            @Override
//            public void onResponse(int code, BleGattProfile profile) {
//                /*
//                 * 乐心血压计需要调用indicate和notify，两者都使能了才可以绑定
//                 * 鱼跃血压计只需要notify即可
//                 * 数据交互的规则是同一套，为了确保逻辑统一，统一都先notify
//                 */
//                requestBind(code, device, reporter, listener, needBind);
//            }
//        });
//    }
//
//    private void requestBind(int code, AgedCareDevice device, IBleDeviceReporter reporter, ICommonListener<Object> listener,
//                             boolean needBind) {
//        logger.info("bluetoothz connectAndNotify onResponse code: " + code);
//        if (code == REQUEST_SUCCESS) {
//            mBleClient.notify(device.getMac(), UUID.fromString(AppConstant.SERVICE_UUID), UUID.fromString(AppConstant.CHARACTERISTIC_UUID_NOTIFY), new BleNotifyResponse() {
//                @Override
//                public void onNotify(UUID service, UUID character, byte[] value) {
//                    /*
//                         鱼跃血压计回调这里
//                     */
//                    logger.info("bluetoothz notify received.");
//                    realBind(value, reporter, device, listener, code);
//                }
//
//                @Override
//                public void onResponse(int code) {
//                    if (code == REQUEST_SUCCESS) {
//                        mBleClient.indicate(device.getMac(), UUID.fromString(AppConstant.SERVICE_UUID), UUID.fromString(AppConstant.CHARACTERISTIC_UUID_INDICATE), new BleNotifyResponse() {
//                            @Override
//                            public void onNotify(UUID service, UUID character, byte[] value) {
//                                /*
//                                 * 乐心血压计走这里，会回调在indicate，鱼跃的会回调在notify
//                                 */
//                                logger.info("bluetoothz indicate received.");
//                                realBind(value, reporter, device, listener, code);
//                            }
//
//                            @Override
//                            public void onResponse(int code) {
//                                int delayMillis = 0;
//                                if (device.getDeviceType() == 501957) {
//                                    delayMillis = 2000;
//                                }
//                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        logger.info("bluetoothz is needBind: " + needBind);
//                                        if (needBind) {
//                                            //乐心血压计登陆命令
//                                            writeDataToDevices(device, listener, 3);
//                                        } else {
//                                            //不需要绑定，直接登陆
//                                            writeDataToDevices(device, listener, 2);
//                                        }
//                                    }
//                                }, delayMillis);
//                            }
//                        });
//                    } else {
//                        listener.onFail(code, "bluetoothz notify response failed");
//                    }
//                }
//            });
//        } else {
//            disconnect(device);
//            listener.onFail(code, "bluetoothz connect error");
//        }
//    }
//
//    //正式开始绑定，先开始发送write的数据只是验证，回传拿到数据之后才开始真正的绑定
//    private void realBind(byte[] value, IBleDeviceReporter reporter, AgedCareDevice device,
//                          ICommonListener<Object> listener, int code) {
//        logger.info("bluetoothz received value:" + ByteUtil.byteToString(value));
//        if (value != null) {
//            if (count > 0) {
//                byteLists.add(value);
//                count--;
//                if (count == 0) {
//                    if (reporter != null) {
//                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                logger.info("bluetoothz run report.");
//                                for (byte[] byteList : byteLists) {
//                                    String res = "";
//                                    for (byte b : byteList) {
//                                        res = res + b + ",";
//                                    }
//                                    logger.info("bluetoothz received data : " + res);
//                                }
//                                reporter.onReport(device.getDeviceId(), byteLists);
//                                byteLists.clear();
//                            }
//                        }, 1 * 1000);
//                    }
//                }
//            }
//
//            //write之后的响应码
//            if (value.length == 10) {
//                logger.info("bluetoothz response : " + Integer.toHexString(value[2]) + " "
//                        + Integer.toHexString(value[3]));
//                logger.info("bluetoothz response is successful: " + Integer.toHexString(value[9]));
//                //返回码，aa 01 绑定指令
//                if (value[2] == (byte) 0xaa && (value[3] == 0x01 || value[3] == 0x03)) {
//                    logger.info("get bind response");
//                    //可以绑定
//                    if (value[9] == 0x01) {
//                        writeDataToDevices(device, listener, 2);
//                        //如果已经绑定了,那就发超级绑定（覆盖绑定）
//                    } else if (value[9] == 0x02) {
//                        writeDataToDevices(device, listener, 3);
//                    } else {
//                        listener.onFail(code, "notify bind error");
//                        logger.info("bluetoothz notify bind error: result " + value[9]);
//                    }
//                    //返回的是登陆指令，那就同步数据
//                } else if (value[2] == (byte) 0xaa && value[3] == 0x02) {
//                    if (value[9] == 0x01) {
//                        logger.info("bluetoothz login success response start sync");
//                        listener.onSuccess(null);
////                        long utcTime = DateUtil.getUtcTime();
////                        byte[] timeStampRequest = ProtocalUtil.getTimeStampRequest(utcTime);
//                        //登陆成功之后需要写入时间戳,乐心血压计来不及处理登陆数据，登陆成功延迟一秒发送时间数据
////                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
////                            @Override
////                            public void run() {
////                                mBleClient.write(device.getMac(), UUID.fromString(AppConstant.SERVICE_UUID), UUID.fromString(AppConstant.CHARACTERISTIC_UUID_WRITE), timeStampRequest, new BleWriteResponse() {
////                                    @Override
////                                    public void onResponse(int code) {
////                                        if (code == REQUEST_SUCCESS) {
////                                            logger.info("bluetoothz write time success");
////                                        }
////                                    }
////                                });
////                            }
////                        }, 1 * 1000);
//
//                        //同步数据
//                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (reporter != null) {
//                                    byte[] syncRequest = reporter.getSyncRequest();
//                                    logger.info("bluetoothz begin write syncRequest bytes:" + ByteUtil.byteToString(syncRequest));
//                                    mBleClient.write(device.getMac(), UUID.fromString(AppConstant.SERVICE_UUID), UUID.fromString(AppConstant.CHARACTERISTIC_UUID_WRITE), syncRequest, new BleWriteResponse() {
//                                        @Override
//                                        public void onResponse(int code) {
//                                            if (code == REQUEST_SUCCESS) {
//                                                logger.info("bluetoothz write sync success");
//                                            } else {
//                                                logger.info("bluetoothz write sync failed code:" + code);
//                                                listener.onFail(code, "write sync fail");
//                                            }
//                                        }
//                                    });
//                                }
//                            }
//                        }, 1 * 1000);
//                    } else {
//                        listener.onFail(code, "notify login error");
//                        logger.info("bluetoothz notify login failed: " + value[9]);
//                    }
//                }
//
//            } else if (reporter != null && reporter.isNeedReport(value)) {
//                count = value[1] - 1;
//                logger.info("bluetoothz get sync data,count = " + count);
//                byteLists.clear();
//                byteLists.add(value);
//            }
//        }
//
//    }
//
//    /**
//     * @param type 1.绑定； 2.登陆
//     */
//    private void writeDataToDevices(AgedCareDevice device, ICommonListener<Object> listener, int type) {
//        List<byte[]> bytesList;
//        if (type == 1) {
//            bytesList = ProtocalUtil.getBindRequest(device.getDeviceType(), DateUtil.getUtcTime(), SharePreferencesUtil.getPassId(AppContextProvider.getFullContext()));
//        } else if (type == 2) {
//            bytesList = ProtocalUtil.getLoginRequest(device.getDeviceType(), DateUtil.getUtcTime(), SharePreferencesUtil.getPassId(AppContextProvider.getFullContext()), device.getDeviceId());
//        } else {
//            bytesList = ProtocalUtil.getSuperBindRequest(device.getDeviceType(), DateUtil.getUtcTime(), SharePreferencesUtil.getPassId(AppContextProvider.getFullContext()));
//        }
//        for (byte[] bytes : bytesList) {
//            mBleClient.write(device.getMac(), UUID.fromString(AppConstant.SERVICE_UUID),
//                    UUID.fromString(AppConstant.CHARACTERISTIC_UUID_WRITE), bytes, new BleWriteResponse() {
//                        @Override
//                        public void onResponse(int code) {
//                            if (code == REQUEST_SUCCESS) {
//                                logger.info("bluetoothz aa " + "0" + type + "write success");
//                            } else {
//                                logger.info("bluetoothz write bind or login failed");
//                                listener.onFail(code, "write bind fail");
//                            }
//                        }
//                    });
//        }
//    }
//
//    public void disconnect(AgedCareDevice device) {
//        String mac = device.getMac();
//        if (TextUtils.isEmpty(mac)) {
//            mac = ProtocalUtil.deviceIdToMAC(device.getDeviceId());
//        }
//
//        disconnect(mac);
//    }
//
//    protected void disconnect(String mac) {
//        if (TextUtils.isEmpty(mac) || !BluetoothAdapter.checkBluetoothAddress(mac)) {
//            logger.error("disconnect mac cant't get or checkBluetoothAddress false mac:" + mac);
//            return;
//        }
//        logger.info("disconnect: " + mac + " status: " + mBleClient.getConnectStatus(mac));
//        mBleClient.unregisterConnectStatusListener(mac, mBleConnectStatusListener);
//        if (mBleClient.getConnectStatus(mac) != Constants.STATUS_DEVICE_CONNECTED) {
//            mBleClient.disconnect(mac);
//        }
//    }
//
//}
