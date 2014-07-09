package com.renpin.utils;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class ScreenShot {
	
	public static String rootpath = "renpin/";// 安装目录
	
	private Context mcontext = null;

	public ScreenShot(Context context) {
		mcontext = context;
	}

	// 截取屏幕图片
	public Bitmap GetCurrenScreenBitmap() {
		Bitmap bmp = null;
		Activity activity = (Activity) mcontext;
		if (activity != null) {
			View view = activity.getWindow().getDecorView();
			view.setDrawingCacheEnabled(true);
			view.buildDrawingCache();
			Bitmap bitmap = view.getDrawingCache();
			Rect rect = new Rect();
			activity.getWindow().getDecorView()
					.getWindowVisibleDisplayFrame(rect);
			int statusBarHeight = rect.top;

			int width = activity.getWindowManager().getDefaultDisplay()
					.getWidth();
			int height = activity.getWindowManager().getDefaultDisplay()
					.getHeight();

			bmp = Bitmap.createBitmap(bitmap, 0, statusBarHeight, width, height
					- statusBarHeight);
			view.destroyDrawingCache();
		}
		return bmp;
	}

	// 获取分享图片保存路径
	public String GetImageFilePath(String name) {

		String strPath = "";
		// 判断是否存在sd卡
		boolean sdExist = android.os.Environment.MEDIA_MOUNTED
				.equals(android.os.Environment.getExternalStorageState());
		if (!sdExist) {// 如果不存在,
			Log.e("SD卡管理：", "SD卡不存在，请加载SD卡");
		} else {// 如果存在
				// 获取sd卡路径
			strPath = android.os.Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			strPath += "/";
			strPath += rootpath;// 数据库所在目录
			String dbPath = strPath + name + ".jpg";// 数据库路径
			// 判断目录是否存在，不存在则创建该目录
			File dirFile = new File(strPath);
			if (!dirFile.exists())
				dirFile.mkdirs();
			// 判断文件是否存在，不存在则创建该文件
			File dbFile = new File(dbPath);
			if (!dbFile.exists()) {
				try {
					dbFile.createNewFile();// 创建文件
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				// isFileCreateSuccess = true;
				strPath = dbPath;
			}
		}
		return strPath;
	}
}
