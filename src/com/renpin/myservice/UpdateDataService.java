package com.renpin.myservice;

import java.util.List;

import com.renpin.domin.TaskDynamicNew;
import com.renpin.domin.TaskInfo;
import com.renpin.domin.UpdateData;
import com.renpin.renpincustomer.R;
import com.renpin.renpincustomer.RenPinMainActivity;
import com.renpin.service.GoodService;
import com.renpin.service.Impl.GoodServiceImpl;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;

public class UpdateDataService extends Service {

	public static final String ACTION = "com.renpin.myservice.UpdateDataService";
	// 服务中的线程是否继续运行
	// boolean bIsRun = true;
	// 用来获取服务器上数据的对象
	private GoodService goodService = new GoodServiceImpl();
	// 用来存储用户信息
	SharedPreferences msettings = null;
	// 存储所有发布信息数据
	public static List<TaskInfo> mTaskInfos = null;
	// 存储未拿下的互助数据
	public static List<TaskInfo> mNoSelectTaskInfos = null;
	// 存储未拿下的分享数据
	public static List<TaskInfo> mNoSelectShareTaskInfos = null;
	// 存储动态信息
	public static List<TaskDynamicNew> mDynamicNew = null;
	// 存储数据更新标志
	public static UpdateData mShouldUpdate = new UpdateData(0, "");
	// 计数器，保证程序刚刚启动时刷新数据
	public static boolean bIsFirstStart = true;
	// 当前用户名称
	String mCurrentPersonName;
	// 用于手机震动
	private Vibrator vibrator = null;
	private Notification mNotification;
	// 通知显示内容
	PendingIntent contentIntent;
	// 状态栏提示信息
	private String strStatusInfo = "";
	PowerManager.WakeLock mLock = null;
	// 同步锁
	public static final Object lock1 = new Object();
	/** 地球半径（单位：公里） */
	public final static double EARTH_RADIUS_KM = 6378.137;

	NotificationManager nm;
	//判断主程序是否退出
    public static boolean bMainProcessExit = false;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		// bIsRun = true;
		msettings = getSharedPreferences("MekeSharedPreferences", 0);
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Intent intent = new Intent(this.getApplicationContext(),
				RenPinMainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 关键的一步，设置启动模式
		contentIntent = PendingIntent.getActivity(UpdateDataService.this, 0,
				intent, 0);
	}

	@SuppressWarnings("deprecation")
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		// 手机网络类型
		ConnectivityManager connectMgr = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectMgr.getActiveNetworkInfo();
		if(info != null){
			// 如果手机没有处于2g或3g环境下或者主程序没有退出,那么就启动后天服务
			if (info.getType() != ConnectivityManager.TYPE_MOBILE || !bMainProcessExit) {
				new PollingThread().start();
			}else{
			}
		}
	}

	class PollingThread extends Thread {
		@Override
		public void run() {
			
			if (mLock != null) {
				// 释放
				mLock.release();
				mLock = null;
			}
			// 获取当前的账户名称
			mCurrentPersonName = msettings.getString("TruePersonName", "");
			// 先判断数据是否有更新
			mShouldUpdate = goodService.GetUpdateSignalNew1(mCurrentPersonName);
			// 如果数据有更新
			if (1 == mShouldUpdate.getnUpdateSignal()) {
				// 将更新标志设为不更新
				goodService.SetUpdateSignal(mCurrentPersonName, 0);
				strStatusInfo = mShouldUpdate.getstrUpdateDescribe();
				// 如果有内容就让手机亮屏并响动
				if (!strStatusInfo.equals("")) {
					// PlaySound(UpdateDataService.this);
					// 是屏幕亮起来
					wakeUpAndUnlock(UpdateDataService.this);
					ShowStatusUpdateInfo();
				}

			}
		}
	}

	// 显示状态更新信息
	@SuppressWarnings("deprecation")
	private void ShowStatusUpdateInfo() {
		// 新建状态栏通知
		mNotification = new Notification();

		// 设置通知在状态栏显示的图标
		mNotification.icon = R.drawable.zan;

		// 通知时在状态栏显示的内容
		mNotification.tickerText = "你有新通知!";

		// 通知的默认参数 DEFAULT_SOUND, DEFAULT_VIBRATE, DEFAULT_LIGHTS.
		// 如果要全部采用默认值, 用 DEFAULT_ALL.
		// 此处采用默认声音
		mNotification.defaults |= Notification.DEFAULT_SOUND;
		mNotification.defaults |= Notification.DEFAULT_VIBRATE;
		mNotification.defaults |= Notification.DEFAULT_LIGHTS;

		// 让声音、振动无限循环，直到用户响应
		// mNotification.flags |= Notification.FLAG_INSISTENT;

		// 通知被点击后，自动消失
		mNotification.flags |= Notification.FLAG_AUTO_CANCEL;

		// 点击'Clear'时，不清楚该通知(QQ的通知无法清除，就是用的这个)
		// mNotification.flags |= Notification.FLAG_NO_CLEAR;
		/*
		 * final RemoteViews remoteView = new
		 * RemoteViews(getApplicationContext() .getPackageName(),
		 * R.layout.activity_ren_pin_main); mNotification.contentView =
		 * remoteView;
		 */
		// 第二个参数 ：下拉状态栏时显示的消息标题 expanded message title
		// 第三个参数：下拉状态栏时显示的消息内容 expanded message text
		// 第四个参数：点击该通知时执行页面跳转
		mNotification.setLatestEventInfo(UpdateDataService.this, "攒人品",
				strStatusInfo, contentIntent);

		// 发出状态栏通知
		// The first parameter is the unique ID for the Notification
		// and the second is the Notification object.
		nm.notify(0, mNotification);

	}

	@SuppressWarnings("deprecation")
	public void wakeUpAndUnlock(Context context) {
		KeyguardManager km = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
		@SuppressWarnings("deprecation")
		KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
		// 解锁
		kl.disableKeyguard();
		// 获取电源管理器对象
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		// 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
		PowerManager.WakeLock mLock = pm.newWakeLock(
				PowerManager.ACQUIRE_CAUSES_WAKEUP
						| PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
		// 点亮屏幕
		mLock.acquire();
	}

	public void onDestroy() {
		super.onDestroy();
		// bIsRun = false;
		if (mLock != null) {
			// 释放
			mLock.release();
			mLock = null;
		}
	}
}
