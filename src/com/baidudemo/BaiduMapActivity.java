package com.baidudemo;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.kingz.uiusingListViews.R;
import com.utils.net.NetTools;

/**
 * Created by KingZ on 2016/1/7.
 * Discription: 百度地图Test
 */
public class BaiduMapActivity extends Activity {

	public static final String TAG = "BaiduMapActivity";
	MapView mMapView = null;
	private BaiduMap mBaiduMap;
	private Context context;

	/*******************定位参数********************/
	private LocationClient mLocationClient;  		//定位服务的客户端 宿主程序在客户端声明此类，并调用
	private MyLocationListener mLocationListener;	//位置监听者
	private boolean isFirshIn = true;

	/******************** UI参数 *************************/
	private BitmapDescriptor bitmapDescriptor;		//自定义方向图标

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//SDK各功能组件使用之前都需要调用,该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_baidumap);
		 //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        this.context = this;
		initView();
		initNetWork();
        initLocation();//初始化定位
	}


	private void initView() {
		mMapView = (MapView) findViewById(R.id.bmapView);
		//修改默认显示尺寸
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);//大约五百米
		mBaiduMap.setMapStatus(msu);
	}

	/**
	 * 初始化判断网络
	 */
	private void initNetWork() {
		//获取ConnectivityManager对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE); //获取系统网络服务
		//获取NetworkInfo对象
		NetworkInfo[] networkInfo = cm.getAllNetworkInfo();	 //获取设备支持的所有网络类型的链接状态信息
//		NetworkInfo networkInfo = cm.getActiveNetworkInfo(); //获取当前连接可用的网络
		//判断当前网络状态是否为连接状态
		 if (networkInfo != null && networkInfo.length > 0)
		{
			for (int i = 0; i < networkInfo.length; i++)
			{
				System.out.println(i + "===状态===" + networkInfo[i].getState());
				System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
				// 判断当前网络状态是否为连接状态
				if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED){
					Toast.makeText(this,"当前网络正常",Toast.LENGTH_SHORT).show();
					if(networkInfo[i].getType() == cm.TYPE_WIFI){
						String wifiName = NetTools.getWifiName(context);
						Toast.makeText(this,"当前为wifi网络,所连接的网络为：" + wifiName,Toast.LENGTH_SHORT).show();
					}else if(networkInfo[i].getType() == cm.TYPE_MOBILE){
						Toast.makeText(this,"当前为移动网络",Toast.LENGTH_SHORT).show();
					}
				}else if(networkInfo[i].getState() == NetworkInfo.State.DISCONNECTED){
					Toast.makeText(this,"当前未连接网络",Toast.LENGTH_SHORT).show();
				}else if(networkInfo[i].getState() == NetworkInfo.State.UNKNOWN){

				}
			}
		}
	}

	/**
	 * 地图位置配置初始化
	 */
	private void initLocation() {
		mLocationClient = new LocationClient(this);
		mLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);//进行注册
		//registerLocationListener(BDLocationListener listener) 注册定位监听函数

		LocationClientOption option =new LocationClientOption();	//客户端定位参数设置

		//设置坐标类型
		option.setCoorType("bd09ll");//必须为09ll
		//setCoorType(java.lang.String coorType)
		//参数：coorType - 取值有3个： 返回国测局经纬度坐标系：gcj02
		//返回百度墨卡托坐标系 ：bd09
		//返回百度经纬度坐标系 ：bd09ll
		option.setIsNeedAddress(true);	//当前位置  此项设置必须设置为true才能够使用getAddr
		option.setOpenGps(true);		//打开gps进行定位
		option.setScanSpan(200);		//获取 设置的扫描间隔，单位是毫秒
		//当所需要的方式设置好了之后  用LocationClient的setLocOption()方法设置LocationClientOption
		mLocationClient.setLocOption(option);	//设置 LocationClientOption

		bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);

	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume(); //重写地图生命周期
	}
	@Override
	protected void onStart() {
		super.onStart();
		mBaiduMap.setMyLocationEnabled(true);//开启定位
		if(!mLocationClient.isStarted()){//若没启动便开启
			mLocationClient.start();
		}

	}
	 @Override
	protected void onStop() {
		super.onStop();
		mBaiduMap.setMyLocationEnabled(false);//关闭定位
		mLocationClient.stop();
	}
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.baidu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
            case R.id.id_map_common:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);		//普通地图
                break;
            case R.id.id_map_site:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);		//卫星地图
                break;
            case R.id.id_map_traffic:									//交通地图
                if (mBaiduMap.isTrafficEnabled()) {
                    mBaiduMap.setTrafficEnabled(false);
                    item.setTitle("实时交通Off");
                }
                else{
                    mBaiduMap.setTrafficEnabled(true);
                    item.setTitle("实时交通On");
                }
                break;
            default:
                break;
		}
		return super.onOptionsItemSelected(item);
	}


	/**
	 * 定位成功回调
	 * */
	private class MyLocationListener implements BDLocationListener {
		//父类是BDLocationListener  只有onReceiveLocation这个方法

		@Override	//定位请求回调函数
		public void onReceiveLocation(BDLocation location) {
			//参数 BDLocation：定位结果 ——
			MyLocationData data = new MyLocationData.Builder()
										.accuracy(location.getRadius())		//精度
										.latitude(location.getLatitude())	// 纬度
										.longitude(location.getLongitude())	// 经度
										.build();

			MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL
																,true,bitmapDescriptor);
			mBaiduMap.setMyLocationData(data);
			mBaiduMap.setMyLocationConfigeration(config);


			//第一次定到位时  更新地图
			if(isFirshIn){
				LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude()); //经度
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.animateMapStatus(msu);//使用动画的效果传过去
				isFirshIn = false;
				String currentLocation = location.getAddrStr();
				Log.i(TAG,"获取到的位置为：" + currentLocation);
				if(!TextUtils.isEmpty(currentLocation)){
					Toast.makeText(context, location.getAddrStr(), Toast.LENGTH_SHORT).show();
				}
				//getAddStr()是BDLocation获取详细地址信息的一个方法
			}
		}
	}
}
