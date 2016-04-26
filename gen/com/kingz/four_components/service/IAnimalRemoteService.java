/*___Generated_by_IDEA___*/

/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\admin\\Documents\\GitHub\\UiUsingListView\\src\\com\\kingz\\four_components\\service\\IAnimalRemoteService.aidl
 */
package com.kingz.four_components.service;
//AIDL Basic Types
//1. Java 鐨勫師鐢熺被鍨�
//2. String and CharSequence
//3. List and Map ,List鍜孧ap 瀵硅薄鐨勫厓绱犲繀椤绘槸AIDL鏀寔鐨勬暟鎹被鍨嬶紱  浠ヤ笂涓夌绫诲瀷閮戒笉闇�瑕佸鍏�(import)
//4. AIDL 鑷姩鐢熸垚鐨勬帴鍙�  闇�瑕佸鍏�(import)
//5. 瀹炵幇android.os.Parcelable 鎺ュ彛鐨勭被.  闇�瑕佸鍏�(import)銆�

public interface IAnimalRemoteService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.kingz.four_components.service.IAnimalRemoteService
{
private static final java.lang.String DESCRIPTOR = "com.kingz.four_components.service.IAnimalRemoteService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.kingz.four_components.service.IAnimalRemoteService interface,
 * generating a proxy if needed.
 */
public static com.kingz.four_components.service.IAnimalRemoteService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.kingz.four_components.service.IAnimalRemoteService))) {
return ((com.kingz.four_components.service.IAnimalRemoteService)iin);
}
return new com.kingz.four_components.service.IAnimalRemoteService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_setName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setName(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_desc:
{
data.enforceInterface(DESCRIPTOR);
this.desc();
reply.writeNoException();
return true;
}
case TRANSACTION_getValue:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getValue();
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.kingz.four_components.service.IAnimalRemoteService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void setName(java.lang.String name) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
mRemote.transact(Stub.TRANSACTION_setName, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void desc() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_desc, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.lang.String getValue() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getValue, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_setName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_desc = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getValue = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public void setName(java.lang.String name) throws android.os.RemoteException;
public void desc() throws android.os.RemoteException;
public java.lang.String getValue() throws android.os.RemoteException;
}
