package com.renpin.location;

import com.baidu.mapapi.BMapManager;

import android.app.Application;

public class MyApplication extends Application {
	public static BMapManager mBMapManager = null;// 加载地图的引擎
	public boolean m_bKeyRight = true;

	// public static int localVersion = 1;// 本地安装版本

	// 缩小图片的宽度(上传到服务器的缩小图片,包括发布图片以及评论中发送的图片)
	public static final int nImageWidth = 400;
	// 缩小图片的高度(上传到服务器的缩小图片,包括发布图片以及评论中发送的图片)
	public static final int nImageHeight = 400;
	// 放大图片的宽度(上传到服务器的放大图片,包括发布图片以及评论中发送的图片)
	public static final int nLargeImageWidth = 1024;
	// 放大图片的高度(上传到服务器的放大图片,包括发布图片以及评论中发送的图片)
	public static final int nLargeImageHeight = 1024;

	public static int serverVersion = 0;// 服务器版本

	public static String strServerVersionName = "";// 服务器版本名称

	public static String downloadDir = "renpin/";// 安装目录
	public static String imagefile = "image/";// 存储图片的父目录
	public static String sharefile = "screenshot";// 分享图片的名称

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
		// 到服务器中下载update.xml文件,获取里面的信息
		// checkVersion();
	}
}
