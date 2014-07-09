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
	// public Vibrator mVibrator;//�ֻ�����
	// public NotifyLister mNotifyLister;
	// �洢��ǰ��γ��
	public static double mLatitude = -1;
	public static double mLongitude = -1;
	// ��ǰ�ĵ�ַ
	public String mStrAddress = null;
	// ����������
	private static Context mcontext = null;
	// ��������
	private static TaskAnnounceActivity mTaskAnnounceActivity = null;

	public static BMapManager mBMapManager = null;

	public MySearch(Context context) {

		if (context != null) {
			mcontext = context;
			mTaskAnnounceActivity = (TaskAnnounceActivity) context;
		}

		// if (null == mMKSearch) {
		// ����ʵ�־�γ�����ַ֮���ת��
		//mMKSearch = new MKSearch();
		// }
	}

	public void GetLatiLong(String strAddress, String strCity) {
		boolean bSucceed = true;
		if (null == mBMapManager) {
			// ��ʼ�����ص�ͼ����
			mBMapManager = new BMapManager(
					mTaskAnnounceActivity.getApplicationContext());
			if (!mBMapManager.init(new MyGeneralListener())) {
				CommonUtils.ShowToastCenter(mcontext, "BMapManager��ʼ������!",
						Toast.LENGTH_LONG);
				bSucceed = false;
			}
			if (!mBMapManager.start()) {
				CommonUtils.ShowToastCenter(mcontext, "BMapManager����ʧ��!",
						Toast.LENGTH_LONG);
				bSucceed = false;
			}
		}
		mysearch = new MySearchListener();
		MKSearch mMKSearch = new MKSearch();
		
		if (!mMKSearch.init(mBMapManager, mysearch)) {
			CommonUtils.ShowToastCenter(mcontext, "��ʼ��ʧ��!", Toast.LENGTH_LONG);
			bSucceed = false;
		}
		if (bSucceed) {
			mMKSearch.geocode(strAddress, strCity);// ��ַ����
		} else {
			// ������룺ͨ����ַ���������
			if (mTaskAnnounceActivity != null) {
				mTaskAnnounceActivity.DealAddressPositionInfo(0, 0);
			} else {
				CommonUtils.ShowToastCenter(mcontext,
						"mTaskAnnounceActivityΪnull", Toast.LENGTH_LONG);
			}
		}
	}

	/**
	 * �߾��ȵ���Χ���ص�
	 * 
	 * @author jpren
	 * 
	 */
	public class NotifyLister extends BDNotifyListener {
		public void onNotify(BDLocation mlocation, float distance) {
			// mVibrator.vibrate(1000);
		}
	}

	// ����������ʵ�־�γ�����ַ֮�以��ת���Ĵ���
	public class MySearchListener implements MKSearchListener {
		@Override
		public void onGetAddrResult(MKAddrInfo result, int error) {
			// ���ص�ַ��Ϣ�������
			if (error != 0) {
				String str = String.format("����ţ�%d", error);
				CommonUtils.ShowToastCenter(mcontext, str, Toast.LENGTH_LONG);
				// ������룺ͨ����ַ���������
				if (mTaskAnnounceActivity != null) {
					mTaskAnnounceActivity.DealAddressPositionInfo(0, 0);
				} else {
					CommonUtils.ShowToastCenter(mcontext,
							"mTaskAnnounceActivityΪnull", Toast.LENGTH_LONG);
				}
				return;
			}
			if (result.type == MKAddrInfo.MK_GEOCODE) {
				// ������룺ͨ����ַ���������
				if (mTaskAnnounceActivity != null) {
					mTaskAnnounceActivity.DealAddressPositionInfo(
							(double) result.geoPt.getLongitudeE6() / 1e6,
							(double) result.geoPt.getLatitudeE6() / 1e6);
				} else {
					CommonUtils.ShowToastCenter(mcontext,
							"mTaskAnnounceActivityΪnull", Toast.LENGTH_LONG);
				}
				return;
			}
			CommonUtils.ShowToastCenter(mcontext, "��ȡ��γ��ʧ��", Toast.LENGTH_LONG);
			// ������룺ͨ����ַ���������
			if (mTaskAnnounceActivity != null) {
				mTaskAnnounceActivity.DealAddressPositionInfo(0, 0);
			} else {
				CommonUtils.ShowToastCenter(mcontext,
						"mTaskAnnounceActivityΪnull", Toast.LENGTH_LONG);
			}
			/*
			 * if (result.type == MKAddrInfo.MK_REVERSEGEOCODE) { //
			 * ��������룺ͨ������������ϸ��ַ���ܱ�poi String strInfo = result.strAddr;
			 * CommonUtils.ShowToastCenter(mcontext, strInfo,
			 * Toast.LENGTH_LONG); }
			 */

		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result,
				int iError) {
			// ���ؼݳ�·���������
			CommonUtils.ShowToastCenter(mcontext, "���ؼݳ�·���������",
					Toast.LENGTH_LONG);
		}

		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int iError) {
			// ����poi�������
			CommonUtils.ShowToastCenter(mcontext, "����poi�������",
					Toast.LENGTH_LONG);
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result,
				int iError) {
			// ���ع����������
			CommonUtils
					.ShowToastCenter(mcontext, "���ع����������", Toast.LENGTH_LONG);
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result,
				int iError) {
			// ���ز���·���������
			CommonUtils.ShowToastCenter(mcontext, "���ز���·���������",
					Toast.LENGTH_LONG);
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			// ���ع�����������Ϣ�������
			CommonUtils.ShowToastCenter(mcontext, "���ع�����������Ϣ�������",
					Toast.LENGTH_LONG);
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult result, int iError) {
			// �����������Ϣ�������
			CommonUtils.ShowToastCenter(mcontext, "�����������Ϣ�������",
					Toast.LENGTH_LONG);
		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult result, int type,
				int error) {
			// �ڴ˴���̴����󷵻ؽ��.
			CommonUtils.ShowToastCenter(mcontext, "�ڴ˴���̴����󷵻ؽ��.",
					Toast.LENGTH_LONG);
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
		}
	}

	// �����¼���������������ͨ�������������Ȩ��֤�����
	class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				CommonUtils.ShowToastCenter(mcontext, "���������������",
						Toast.LENGTH_LONG);
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				CommonUtils.ShowToastCenter(mcontext, "������ȷ�ļ���������",
						Toast.LENGTH_LONG);
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			// ����ֵ��ʾkey��֤δͨ��
			if (iError != 0) {
				// ��ȨKey����
				CommonUtils.ShowToastCenter(mcontext,
						"���� MyApplication.java�ļ�������ȷ����ȨKey,������������������Ƿ�������error: "
								+ iError, Toast.LENGTH_LONG);
			} else {
				CommonUtils.ShowToastCenter(mcontext, "key��֤�ɹ�",
						Toast.LENGTH_LONG);
			}
		}
	}
}
