package com.renpin.location;

import com.baidu.location.BDLocation;
import com.baidu.location.BDNotifyListener;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.renpin.renpincustomer.TaskAnnounceActivity;
import com.renpin.utils.CommonUtils;

import android.content.Context;
import android.widget.Toast;

public class MySearch {
	public static MySearchListener mysearch = null;
	// public Vibrator mVibrator;//手机振动器
	// public NotifyLister mNotifyLister;
	// 存储当前经纬度
	public static double mLatitude = -1;
	public static double mLongitude = -1;
	// 当前的地址
	public String mStrAddress = null;
	// 环境上下文
	private static Context mcontext = null;
	// 主界面类
	private static TaskAnnounceActivity mTaskAnnounceActivity = null;

	public static BMapManager mBMapManager = null;

	public MySearch(Context context) {

		if (context != null) {
			mcontext = context;
			mTaskAnnounceActivity = (TaskAnnounceActivity) context;
		}

		// if (null == mMKSearch) {
		// 用来实现经纬度与地址之间的转换
		//mMKSearch = new MKSearch();
		// }
	}

	public void GetLatiLong(String strAddress, String strCity) {
		boolean bSucceed = true;
		if (null == mBMapManager) {
			// 初始化加载地图引擎
			mBMapManager = new BMapManager(
					mTaskAnnounceActivity.getApplicationContext());
			if (!mBMapManager.init(new MyGeneralListener())) {
				CommonUtils.ShowToastCenter(mcontext, "BMapManager初始化错误!",
						Toast.LENGTH_LONG);
				bSucceed = false;
			}
			if (!mBMapManager.start()) {
				CommonUtils.ShowToastCenter(mcontext, "BMapManager启动失败!",
						Toast.LENGTH_LONG);
				bSucceed = false;
			}
		}
		mysearch = new MySearchListener();
		MKSearch mMKSearch = new MKSearch();
		
		if (!mMKSearch.init(mBMapManager, mysearch)) {
			CommonUtils.ShowToastCenter(mcontext, "初始化失败!", Toast.LENGTH_LONG);
			bSucceed = false;
		}
		if (bSucceed) {
			mMKSearch.geocode(strAddress, strCity);// 地址解析
		} else {
			// 地理编码：通过地址检索坐标点
			if (mTaskAnnounceActivity != null) {
				mTaskAnnounceActivity.DealAddressPositionInfo(0, 0);
			} else {
				CommonUtils.ShowToastCenter(mcontext,
						"mTaskAnnounceActivity为null", Toast.LENGTH_LONG);
			}
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

	// 以下是用来实现经纬度与地址之间互相转换的代码
	public class MySearchListener implements MKSearchListener {
		@Override
		public void onGetAddrResult(MKAddrInfo result, int error) {
			// 返回地址信息搜索结果
			if (error != 0) {
				String str = String.format("错误号：%d", error);
				CommonUtils.ShowToastCenter(mcontext, str, Toast.LENGTH_LONG);
				// 地理编码：通过地址检索坐标点
				if (mTaskAnnounceActivity != null) {
					mTaskAnnounceActivity.DealAddressPositionInfo(0, 0);
				} else {
					CommonUtils.ShowToastCenter(mcontext,
							"mTaskAnnounceActivity为null", Toast.LENGTH_LONG);
				}
				return;
			}
			if (result.type == MKAddrInfo.MK_GEOCODE) {
				// 地理编码：通过地址检索坐标点
				if (mTaskAnnounceActivity != null) {
					mTaskAnnounceActivity.DealAddressPositionInfo(
							(double) result.geoPt.getLongitudeE6() / 1e6,
							(double) result.geoPt.getLatitudeE6() / 1e6);
				} else {
					CommonUtils.ShowToastCenter(mcontext,
							"mTaskAnnounceActivity为null", Toast.LENGTH_LONG);
				}
				return;
			}
			CommonUtils.ShowToastCenter(mcontext, "获取经纬度失败", Toast.LENGTH_LONG);
			// 地理编码：通过地址检索坐标点
			if (mTaskAnnounceActivity != null) {
				mTaskAnnounceActivity.DealAddressPositionInfo(0, 0);
			} else {
				CommonUtils.ShowToastCenter(mcontext,
						"mTaskAnnounceActivity为null", Toast.LENGTH_LONG);
			}
			/*
			 * if (result.type == MKAddrInfo.MK_REVERSEGEOCODE) { //
			 * 反地理编码：通过坐标点检索详细地址及周边poi String strInfo = result.strAddr;
			 * CommonUtils.ShowToastCenter(mcontext, strInfo,
			 * Toast.LENGTH_LONG); }
			 */

		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result,
				int iError) {
			// 返回驾乘路线搜索结果
			CommonUtils.ShowToastCenter(mcontext, "返回驾乘路线搜索结果",
					Toast.LENGTH_LONG);
		}

		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int iError) {
			// 返回poi搜索结果
			CommonUtils.ShowToastCenter(mcontext, "返回poi搜索结果",
					Toast.LENGTH_LONG);
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result,
				int iError) {
			// 返回公交搜索结果
			CommonUtils
					.ShowToastCenter(mcontext, "返回公交搜索结果", Toast.LENGTH_LONG);
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result,
				int iError) {
			// 返回步行路线搜索结果
			CommonUtils.ShowToastCenter(mcontext, "返回步行路线搜索结果",
					Toast.LENGTH_LONG);
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			// 返回公交车详情信息搜索结果
			CommonUtils.ShowToastCenter(mcontext, "返回公交车详情信息搜索结果",
					Toast.LENGTH_LONG);
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult result, int iError) {
			// 返回联想词信息搜索结果
			CommonUtils.ShowToastCenter(mcontext, "返回联想词信息搜索结果",
					Toast.LENGTH_LONG);
		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult result, int type,
				int error) {
			// 在此处理短串请求返回结果.
			CommonUtils.ShowToastCenter(mcontext, "在此处理短串请求返回结果.",
					Toast.LENGTH_LONG);
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
		}
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				CommonUtils.ShowToastCenter(mcontext, "您的网络出错啦！",
						Toast.LENGTH_LONG);
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				CommonUtils.ShowToastCenter(mcontext, "输入正确的检索条件！",
						Toast.LENGTH_LONG);
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			// 非零值表示key验证未通过
			if (iError != 0) {
				// 授权Key错误：
				CommonUtils.ShowToastCenter(mcontext,
						"请在 MyApplication.java文件输入正确的授权Key,并检查您的网络连接是否正常！error: "
								+ iError, Toast.LENGTH_LONG);
			} else {
				CommonUtils.ShowToastCenter(mcontext, "key认证成功",
						Toast.LENGTH_LONG);
			}
		}
	}
}
