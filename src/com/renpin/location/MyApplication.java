package com.renpin.location;

import com.baidu.mapapi.BMapManager;

import android.app.Application;

public class MyApplication extends Application {
	public static BMapManager mBMapManager = null;// ���ص�ͼ������
	public boolean m_bKeyRight = true;

	// public static int localVersion = 1;// ���ذ�װ�汾

	// ��СͼƬ�Ŀ���(�ϴ�������������СͼƬ,��������ͼƬ�Լ������з��͵�ͼƬ)
	public static final int nImageWidth = 400;
	// ��СͼƬ�ĸ߶�(�ϴ�������������СͼƬ,��������ͼƬ�Լ������з��͵�ͼƬ)
	public static final int nImageHeight = 400;
	// �Ŵ�ͼƬ�Ŀ���(�ϴ����������ķŴ�ͼƬ,��������ͼƬ�Լ������з��͵�ͼƬ)
	public static final int nLargeImageWidth = 1024;
	// �Ŵ�ͼƬ�ĸ߶�(�ϴ����������ķŴ�ͼƬ,��������ͼƬ�Լ������з��͵�ͼƬ)
	public static final int nLargeImageHeight = 1024;

	public static int serverVersion = 0;// �������汾

	public static String strServerVersionName = "";// �������汾����

	public static String downloadDir = "renpin/";// ��װĿ¼
	public static String imagefile = "image/";// �洢ͼƬ�ĸ�Ŀ¼
	public static String sharefile = "screenshot";// ����ͼƬ������

	// "http://192.168.2.2:8080/Apk/download?fileName=update.xml";
	@Override
	public void onCreate() {
		super.onCreate();
		// initEngineManager(this);
		/*
		 * try { //PackageInfo packageInfo = getApplicationContext() //
		 * .getPackageManager().getPackageInfo(getPackageName(), 0);
		 * //localVersion = packageInfo.versionCode;
		 * //CommonUtils.ShowToastCenter(getApplicationContext(),
		 * ""+localVersion, Toast.LENGTH_LONG); } catch (NameNotFoundException
		 * e) { e.printStackTrace(); }
		 */
		// ��������������update.xml�ļ�,��ȡ�������Ϣ
		// checkVersion();
	}
}