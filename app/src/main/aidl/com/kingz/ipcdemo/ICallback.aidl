// SiKCallback.aidl
package com.kingz.ipcdemo;

// Declare any non-default types here with import statements
interface ICallback {
     void onSuccess(String result);
     void onError(int errorCode,String reason);
}
