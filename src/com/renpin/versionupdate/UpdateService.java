package com.renpin.versionupdate;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.renpin.renpincustomer.R;
import com.renpin.renpincustomer.RenPinMainActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

/***
 * 更新版本
 * 
 */
public class UpdateService extends Service {
	private static final int TIMEOUT = 10 * 1000;// 超时
	private static final String down_url = 
			"http://59.60.9.202:8000/Apk/download?fileName=RenPinCustomer";
			//"http://192.168.2.2:8080/Apk/download?fileName=RenPinCustomer";
	private static final int DOWN_OK = 1;
	private static final int DOWN_ERROR = 0;

	private String app_name;

	private NotificationManager notificationManager;
	private Notification notification;

	private Intent updateIntent;
	private PendingIntent pendingIntent;

	private int notification_id = 1;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		app_name = intent.getStringExtra("app_name");
		// 创建文件
		FileUtil.createFile(app_name);

		createNotification();

		createThread();

		return super.onStartCommand(intent, flags, startId);

	}

	/***
	 * 开线程下载
	 */
	public void createThread() {
		/***
		 * 更新UI
		 */
		final Handler handler = new Handler() {
			@SuppressWarnings("deprecation")
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case DOWN_OK:
					
					// 下载完成，点击安装
					Uri uri = Uri.fromFile(FileUtil.updateFile);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(uri,
							"application/vnd.android.package-archive");

					pendingIntent = PendingIntent.getActivity(
							UpdateService.this, 0, intent, 0);

					notification.setLatestEventInfo(UpdateService.this,
							app_name, "下载成功，点击安装", pendingIntent);

					notificationManager.notify(notification_id, notification);
					
					stopService(updateIntent);
					break;
				case DOWN_ERROR:
					notification.setLatestEventInfo(UpdateService.this,
							app_name, "下载失败", pendingIntent);
					break;

				default:
					stopService(updateIntent);
					break;
				}

			}

		};

		final Message message = new Message();

		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					long downloadSize = downloadUpdateFile(down_url,
							FileUtil.updateFile.toString());
					if (downloadSize > 0) {
						// 下载成功
						message.what = DOWN_OK;
						handler.sendMessage(message);
					}

				} catch (Exception e) {
					e.printStackTrace();
					message.what = DOWN_ERROR;
					handler.sendMessage(message);
				}

			}
		}).start();
	}

	/***
	 * 创建通知栏
	 */
	RemoteViews contentView;

	public void createNotification() {
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		updateIntent = new Intent(this, RenPinMainActivity.class);
		updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);
		notification = new Notification();
		// 设置通知在状态栏显示的图标
		notification.icon = R.drawable.zan;

		// 通知时在状态栏显示的内容
		//notification.tickerText = "你有新通知!";

		// 通知的默认参数 DEFAULT_SOUND, DEFAULT_VIBRATE, DEFAULT_LIGHTS.
		// 如果要全部采用默认值, 用 DEFAULT_ALL.
		// 此处采用默认声音
		//notification.defaults |= Notification.DEFAULT_SOUND;
		//notification.defaults |= Notification.DEFAULT_VIBRATE;
		//notification.defaults |= Notification.DEFAULT_LIGHTS;

		// 让声音、振动无限循环，直到用户响应
		// mNotification.flags |= Notification.FLAG_INSISTENT;

		// 通知被点击后，自动消失
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		/***
		 * 在这里我们用自定的view来显示Notification
		 */
		contentView = new RemoteViews(getPackageName(),
				R.layout.notification_item);
		contentView.setTextViewText(R.id.notificationTitle, "正在下载");
		contentView.setTextViewText(R.id.notificationPercent, "0%");
		contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

		notification.contentView = contentView;

		notification.contentIntent = pendingIntent;

		notificationManager.notify(notification_id, notification);

	}

	/***
	 * 下载文件
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	public long downloadUpdateFile(String down_url, String file)
			throws Exception {
		int down_step = 5;// 提示step
		int totalSize;// 文件总大小
		int downloadCount = 0;// 已经下载好的大小
		int updateCount = 0;// 已经上传的文件大小
		InputStream inputStream;
		OutputStream outputStream;

		URL url = new URL(down_url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		httpURLConnection.setConnectTimeout(TIMEOUT);
		httpURLConnection.setReadTimeout(TIMEOUT);
		httpURLConnection.setRequestProperty("Accept-Encoding", "identity"); 
		// 获取下载文件的size
		totalSize = httpURLConnection.getContentLength();
		if (httpURLConnection.getResponseCode() == 404) {
			throw new Exception("fail!");
		}
		int nCode = httpURLConnection.getHeaderFieldInt("versioncode", -1);
		inputStream = httpURLConnection.getInputStream();
		outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉
		byte buffer[] = new byte[1024];
		int readsize = 0;
		while ((readsize = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, readsize);
			downloadCount += readsize;// 时时获取下载到的大小
			/**
			 * 每次增张5%
			 */
			if (updateCount == 0
					|| (downloadCount * 100 / totalSize - down_step) >= updateCount) {
				updateCount += down_step;
				// 改变通知栏
				// notification.setLatestEventInfo(this, "正在下载...", updateCount
				// + "%" + "", pendingIntent);
				contentView.setTextViewText(R.id.notificationPercent,
						updateCount + "%");
				contentView.setProgressBar(R.id.notificationProgress, 100,
						updateCount, false);
				// show_view
				notificationManager.notify(notification_id, notification);

			}

		}
		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}
		inputStream.close();
		outputStream.close();

		return downloadCount;

	}

}
