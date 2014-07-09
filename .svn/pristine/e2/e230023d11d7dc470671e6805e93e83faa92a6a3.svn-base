package com.renpin.location;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import android.content.Context;

public class MyLocation {
	public static LocationClient mLocationClient;
	// public GeofenceClient mGeofenceClient;
	private static MyLocationListener mMyLocationListener;
	// public Vibrator mVibrator;//手机振动器
	// public NotifyLister mNotifyLister;
	// 存储当前经纬度
	public static double mLatitude = -1;
	public static double mLongitude = -1;
	// 当前的地址
	public static String mStrAddress = null;
	// 当前用户所在的区名
	public static String mDistrictName = null;
	private static LocationMode[] mMode = { LocationMode.Hight_Accuracy,// 高精度,gps,wifi,基站定位
			LocationMode.Battery_Saving,// 仅wifi和基站定位
			LocationMode.Device_Sensors };// 仅gps定位
	private static String[] strCoordType = { "gcj02", "bd09ll", "bd09" };// 经纬度格式

	public MyLocation(Context context) {

		mLocationClient = new LocationClient(context);
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		// 设置相关参数
		setLocationOption();
		// 开始定位
		mLocationClient.start();
	}

	// 设置Option
	private void setLocationOption() {
		try {
			LocationClientOption option = new LocationClientOption();
			// 设置定位模式
			option.setLocationMode(mMode[0]);
			// 设置经纬度格式
			option.setCoorType(strCoordType[1]);
			// 设置获取经纬度信息的周期,这里设置每300000毫秒获取一次
			option.setScanSpan(600000);
			// 设置是否需要根据手机指定方向
			option.setNeedDeviceDirect(false);
			// 设置是否需要将当前的经纬度转换成地址信息
			option.setIsNeedAddress(true);
			// 设置定位参数
			mLocationClient.setLocOption(option);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获取
	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			mLatitude = location.getLatitude();
			mLongitude = location.getLongitude();
			mStrAddress = location.getAddrStr();
			mDistrictName = location.getDistrict();
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {

		}
	}

	/**
	 * 高精度地理围栏回调
	 * 
	 * @author jpren
	 * 
	 */
	public class NotifyLister extends BDNotifyListener {
		public void onNotify(BDLocation mlocation, float distance) {
			// mVibrator.vibrate(1000);
		}
	}

}
