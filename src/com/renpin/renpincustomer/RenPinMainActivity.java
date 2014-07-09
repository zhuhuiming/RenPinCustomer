package com.renpin.renpincustomer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import com.renpin.domin.DistanceDetail;
import com.renpin.domin.TaskInfoDetail;
import com.renpin.location.MyLocation;
import com.renpin.location.MySearch;
import com.renpin.myservice.UpdateDataService;
import com.renpin.renpincustomer.PullToRefreshView.OnFooterRefreshListener;
import com.renpin.renpincustomer.PullToRefreshView.OnHeaderRefreshListener;
import com.renpin.service.GoodService;
import com.renpin.service.Impl.GoodServiceImpl;
import com.renpin.utils.CommonUtils;
import com.renpin.utils.PollingUtils;
import com.renpin.versionupdate.FileUtil;
import com.renpin.versionupdate.UpdateInfo;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class RenPinMainActivity extends Activity implements
		OnHeaderRefreshListener, OnFooterRefreshListener {

	private final static String strSpecialOldTime = "2012年12月12日12:12:12";
	// 显示动态列表框对象
	private ListView mListView2;
	//mListView2为空时显示的文字
	private TextView emptytextview;
	// 分享
	PullToRefreshView mPullToRefreshView1;
	/** 地球半径（单位：公里） */
	public final static double EARTH_RADIUS_KM = 6378.137;
	// 存储动态信息
	public static List<TaskInfoDetail> mDynamicNew = null;
	// 公共操作类
	private static CommonUtils mUtils = null;
	// 用来存储从数据库中获取到的数据
	// 我的
	private List<HashMap<String, Object>> mListData1 = null;
	// 求助
	private List<HashMap<String, Object>> mListData = null;
	// 分享
	private List<HashMap<String, Object>> mListData2 = null;
	// 求助
	private static TaskInfoListAdpater madapter = null;
	// 我的
	private static DynamicNewsListAdpater madapter1 = null;
	// 分享
	private static TaskInfoListAdpater madapter2 = null;
	// 判断任务数据是否应该刷新
	boolean mbIsFinished = true;
	// 当网络断开了,那么就将这个窗口隐藏
	LinearLayout nonetworklayout = null;
	// 更多操作
	LinearLayout morelayout;
	// 发布操作
	ImageView mAnnounceTaskImageView;
	// 定时更新任务数据的线程对象
	private static Thread mthre = null;
	// 判断更新任务线程是否应该停止
	private boolean mbIsRunning = true;
	// 接收消息对象
	MyHandler myhandler = null;
	// 用来获取服务器上数据的对象
	private GoodService goodService = new GoodServiceImpl();
	// 窗口显示1表示我的,2表示求助,3表示分享,4表示更多
	public static int mIsShowingWindow = 3;
	// 用来存储用户信息
	SharedPreferences msettings = null;
	// 当前用户名称
	private String mCurrentPersonName = null;
	// 当前用户昵称
	private String mCurrentPersonNameTemp = null;

	// private TextView tasktextview;
	// 发布人图片
	public static Bitmap mAnnounceImage;
	// 获取当前经纬度的类
	private MyLocation mMyLocation = null;
	// 显示动态信息条数的控件对象
	private TextView textview6;
	// 动态控件对象
	private LinearLayout linearlayout1;
	// 分享控件对象
	private LinearLayout linearlayout7;
	// 附近顶端控件对象
	private LinearLayout linearlayout2;
	// 动态顶端控件对象
	private LinearLayout linearlayout3;
	// 求助控件对象
	private LinearLayout linearlayout4;
	// 状态提示信息
	private String strStatusInfo = "";
	// 任务进度状态
	private String strTaskStatus = "";
	// 更多界面
	LinearLayout linearlayout5;
	// 更多顶部界面
	LinearLayout linearlayout6;
	// 用户名称控件
	TextView my_textview1;
	// 用户图标控件
	ImageView my_imageview1;
	// 赞图标控件
	ImageView my_imageview2;
	// 人品值控件
	TextView my_textview2;
	// 赞值控件
	TextView my_textview3;
	// 计数器，保证程序刚刚启动时刷新数据
	public static boolean bIsFirstStart = true;
	// 判断网络连接状态
	private static boolean bNetWorkLinkStatus = true;
	// listview容量值
	private final static int mnVolume = 60;
	// 一次加载
	private final static int nMaxDataLine = 10;
	// 求助任务当前加载最旧的任务数据id号
	private static int mnHelpTaskMaxOldIndex = 0;
	// 求助任务当前刷新到最新任务数据的id号
	private static int mnHelpTaskMaxNewIndex = 0;
	// 存储求助任务数据(广场)
	public static List<TaskInfoDetail> mHelpTaskData = null;
	// 存储分享任务数据(广场)
	public static List<TaskInfoDetail> mShareTaskData = null;
	// 存储求助任务数据(按距离排序)
	public static List<DistanceDetail> mHelpTaskDataDistance = null;
	// 存储分享任务数据(按距离排序)
	public static List<DistanceDetail> mShareTaskDataDistance = null;
	// 求助任务中距离用户最大的数值(周边)
	// private static int mnMaxDistanceForHelpNear = 0;
	// 分享中距离用户最大的数值(周边)
	// private static int mnMaxDistanceForShareNear = 0;
	// 求助任务中最老的时间(周边)
	private static String mnOldTimeForHelp = strSpecialOldTime;
	// 分享中最老的时间(周边)
	private static String mnOldTimeForShareNear = strSpecialOldTime;
	// 分享中最老的时间(广场)
	private static String mnOldTimeForShareAll = strSpecialOldTime;
	// 分享中最新的时间(广场)
	private static String mnNewTimeForShareAll = strSpecialOldTime;

	private long mlCurTime = 0;
	// 意见反馈控件
	private RelativeLayout advicetextview;
	// 关于我们控件
	private RelativeLayout aboutustextview;
	// 判断是否已经提示更新版本号
	private boolean bIsReminderUpdateApk = false;
	// 版本更新
	private RelativeLayout versionupdaterelative;
	// 版本更新提示信息
	private TextView versionupdateinfotextview;
	public UpdateInfo mupdateinfo = null;
	private static final int TIMEOUT = 10 * 1000;// 超时59.60.9.202:8000
	private static final String strUpdateXmlPath = // "http://192.168.2.2:8080/Apk/download?fileName=update.xml";
	"http://www.meiliangshare.cn:8000/Apk/download?fileName=update.xml";
	public static int localVersion = 1;// 本地安装版本
	// 点击该控件进入用户详细界面
	LinearLayout myinfolinearlayout;
	// 最大距离(米)
	private static final double mnMaxDistance = 5000;
	// 进度条对象
	public ProgressDialog m_ProgressDialog = null;
	// 周边按钮
	private Button nearbutton;
	// 广场按钮
	private Button spacebutton;
	// 周边或广场标志,1为周边,2为广场,默认为周边
	private int mnDataType = 2;
	// 当前所在区域名称
	private String strDistrictName;

	/* 下载更新有关的变量 (start) */
	private NotificationManager notificationManager;
	private Notification notification;
	private Intent updateIntent;
	private PendingIntent pendingIntent;
	private int notification_id = 1;
	RemoteViews contentView;
	private static final int DOWN_OK = 1;
	private static final int DOWN_ERROR = 0;
	private static final String down_url = "http://www.meiliangshare.cn:8000/Apk/download?fileName=RenPinCustomer";
	/* (end) */
	// 判断数据是否加载完成,加载完了之后才会弹出版本更新对话框
	private boolean bDataFinishLoad = false;
	// gridview
	private GridView mgridview;
	// 包含发布与更多的linearlayout
	private LinearLayout moreoperalinearlayout;
	// 发布textview控件
	private TextView mAnnounceTextView;
	// 更多textview控件
	private TextView mMoreTextView;
	// 更多界面中的返回图标
	private ImageView mMoreReturnImageView;
	// 我的顶端界面中的返回imagview
	private ImageView mMyTopReturnImageView;
	// 用户信息linearlayout控件
	private LinearLayout mPersonInfoLinearLayout;
	// 用户头像控件
	private ImageView mPersonImageView;
	// 用户昵称控件
	private TextView mPersonNickTextView;
	// 显示登录的控件
	private TextView mLoginTextView;
	//保存赞值
	public static HashMap<String,Integer> StoreZanMap = new HashMap<String,Integer>();
	//StoreZanMap容器数目最大的个数
	public static int MaxStoreMap = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 程序启动的时候避免光标处在editview中而弹出输入法窗口
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_ren_pin_main);
		// 启动轮询服务器
		PollingUtils.startPollingService(this, 20, UpdateDataService.class,
				UpdateDataService.ACTION);
		// 获取版本号
		PackageInfo packageInfo = null;
		try {
			packageInfo = getApplicationContext().getPackageManager()
					.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (packageInfo != null) {
			localVersion = packageInfo.versionCode;
		}
        
		if(MySearch.mBMapManager != null){
			MySearch.mBMapManager.start();
		}
		
		UpdateDataService.bMainProcessExit = false;
		// 初始为没有更新提示，程序一旦监测到新版本,那么就提示，提示完了之后只要用户没有退出就不再提示
		bIsReminderUpdateApk = false;
		// 确保动态中显示数据
		madapter1 = null;
		if (null == mHelpTaskData) {
			mnHelpTaskMaxOldIndex = 0;
			mnHelpTaskMaxNewIndex = 0;
		} else if (mHelpTaskData.size() <= 0) {
			mnHelpTaskMaxOldIndex = 0;
			mnHelpTaskMaxNewIndex = 0;
		}

		if (null == mShareTaskData) {
			mnOldTimeForShareAll = strSpecialOldTime;
			mnNewTimeForShareAll = strSpecialOldTime;
		} else if (mShareTaskData.size() <= 0) {
			mnOldTimeForShareAll = strSpecialOldTime;
			mnNewTimeForShareAll = strSpecialOldTime;
		}

		if (null == mHelpTaskDataDistance) {
			// mnMaxDistanceForHelpNear = 0;
			mnOldTimeForHelp = strSpecialOldTime;
		} else if (mHelpTaskDataDistance.size() <= 0) {
			mnOldTimeForHelp = strSpecialOldTime;
		}

		if (null == mShareTaskDataDistance) {
			// mnMaxDistanceForShareNear = 0;
			mnOldTimeForShareNear = strSpecialOldTime;
		} else if (mShareTaskDataDistance.size() <= 0) {
			// mnMaxDistanceForShareNear = 0;
			mnOldTimeForShareNear = strSpecialOldTime;
			mnOldTimeForShareAll = strSpecialOldTime;
		}

		mPullToRefreshView1 = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		mPullToRefreshView1.setOnHeaderRefreshListener(this);
		mPullToRefreshView1.setOnFooterRefreshListener(this);
		mPullToRefreshView1.setVisibility(View.GONE);
		// 动态信息中没有数据那么就到服务器中申请数据
		if (mDynamicNew != null) {
			if (mDynamicNew.size() <= 0) {
				bIsFirstStart = true;
			}
		} else {
			bIsFirstStart = true;
		}

		msettings = getSharedPreferences("MekeSharedPreferences", 0);
		mCurrentPersonNameTemp = msettings.getString("PersonName", "");
		mCurrentPersonName = msettings.getString("TruePersonName", "");
		if (null == mUtils) {
			// 创建公共操作类对象
			mUtils = new CommonUtils(this);
		}
		// 初始化界面控件
		InitActivities();
		// 到服务器中下载update.xml文件,获取里面的信息
		checkVersion();
		// 创建接收消息对象
		myhandler = new MyHandler();

		if (null == mMyLocation) {
			// 开始获取当前经纬度
			mMyLocation = new MyLocation(this);
		}
		SharedPreferences.Editor editor = msettings.edit();
		// 将base64类型的图标保存起来
		editor.putString("CurrentLocationAddress", MyLocation.mStrAddress);
		editor.commit();

		// 启动线程,获取求助和分享数据
		GetHelpOrShareData();
		// 如果此时线程对象还没有创建,那么就创建
		if (null == mthre) {
			mbIsRunning = true;
			mthre = new Thread(run);
			mthre.start();
		}
		// 如果当前为2G/3G环境就显示提示框
		ShowPromptWindow();
	}

	private void ShowPromptWindow() {
		// 手机网络类型
		ConnectivityManager connectMgr = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectMgr.getActiveNetworkInfo();
		if (info != null) {
			// 如果手机处于2g或3g环境
			if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						RenPinMainActivity.this);
				dialog.setMessage("当前网络为2G/3G环境,使用完后记得退出程序哦!");
				dialog.setTitle("温馨提示");
				dialog.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				dialog.show();
			}
		}
	}

	public void onDestroy() {
		mbIsRunning = false;
		mthre = null;
		if (MyLocation.mLocationClient != null) {
			MyLocation.mLocationClient.stop();
			MyLocation.mLocationClient = null;
		}
		if(MySearch.mBMapManager != null){
			MySearch.mBMapManager.destroy();
			MySearch.mBMapManager = null;
		}
		super.onDestroy();

	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		if(MySearch.mBMapManager != null){
			MySearch.mBMapManager.stop();
		}
	}

	@Override
	public void onBackPressed() {
		// 如果此时处在我的界面或者更多界面,那么就返回到分享界面
		if (1 == mIsShowingWindow || 4 == mIsShowingWindow) {
			linearlayout2.setVisibility(View.VISIBLE);
			// tasktextview.setText("分享");
			linearlayout3.setVisibility(View.GONE);
			mPullToRefreshView1.setVisibility(View.VISIBLE);
			// 如果是从另外一个窗口切换过来的,那么就要重新设置数据
			if (mIsShowingWindow != 3) {
				if (1 == mnDataType) {
					ShowNearShareDataForClick(1);
				} else if (2 == mnDataType) {
					ShowSpaceShareDataForClick(1);
				}
			} else {
				if (1 == mnDataType) {
					ShowNearShareDataForClick(0);
				} else if (2 == mnDataType) {
					ShowSpaceShareDataForClick(0);
				}
			}
			mListView2.setVisibility(View.GONE);
			emptytextview.setVisibility(View.GONE);
			linearlayout5.setVisibility(View.GONE);
			linearlayout6.setVisibility(View.GONE);
			mIsShowingWindow = 3;
			return;
		}
		if (System.currentTimeMillis() - mlCurTime < 3000) {
			mbIsRunning = false;
			mthre = null;
			if (MyLocation.mLocationClient != null) {
				MyLocation.mLocationClient.stop();
				MyLocation.mLocationClient = null;
			}
			UpdateDataService.bMainProcessExit = true;
			finish();
		} else {
			CommonUtils.ShowToastCenter(this, "再按一次退出程序", Toast.LENGTH_LONG);
		}
		mlCurTime = System.currentTimeMillis();
	}

	private void ReadHelpDistanceData() {
		if (mHelpTaskDataDistance != null) {
			// 如果此时求助中没有数据,那么就加载
			if (mHelpTaskDataDistance.size() <= 0) {
				// 一次性更新30条数据
				mHelpTaskDataDistance = goodService.getHelpNearData(
						MyLocation.mLongitude, MyLocation.mLatitude,
						(double) mnMaxDistance, strDistrictName);
				if (mHelpTaskDataDistance != null) {
					int nSize = mHelpTaskDataDistance.size();
					if (nSize > 0) {
						int ndistance = 0;
						String strTime;
						ndistance = mHelpTaskDataDistance.get(nSize - 1)
								.getnDistance();
						strTime = mHelpTaskDataDistance.get(nSize - 1)
								.getmTaskAnnounceTime();
						// 保存最大距离
						// mnMaxDistanceForHelpNear = ndistance;
						mnOldTimeForHelp = strTime;
					}
				}
			}
		} else {// 此时也更新数据
			mHelpTaskDataDistance = goodService.getHelpNearData(
					MyLocation.mLongitude, MyLocation.mLatitude,
					(double) mnMaxDistance, strDistrictName);
			if (mHelpTaskDataDistance != null) {
				int nSize = mHelpTaskDataDistance.size();
				if (nSize > 0) {
					int ndistance = 0;
					String strTime;
					ndistance = mHelpTaskDataDistance.get(nSize - 1)
							.getnDistance();
					strTime = mHelpTaskDataDistance.get(nSize - 1)
							.getmTaskAnnounceTime();
					// 保存最大距离
					// mnMaxDistanceForHelpNear = ndistance;
					mnOldTimeForHelp = strTime;
				}
			}
		}
	}

	private void ReadShareDistanceData() {
		if (mShareTaskDataDistance != null) {
			// 如果此时求助中没有数据,那么就加载
			if (mShareTaskDataDistance.size() <= 0) {
				// 一次性更新mnVolume条数据
				mShareTaskDataDistance = goodService.getShareNearData(
						MyLocation.mLongitude, MyLocation.mLatitude,
						(double) mnMaxDistance, strDistrictName);
				if (mShareTaskDataDistance != null) {
					int nSize = mShareTaskDataDistance.size();
					if (nSize > 0) {
						int nDistance = 0;
						String strTime;
						nDistance = mShareTaskDataDistance.get(nSize - 1)
								.getnDistance();
						strTime = mShareTaskDataDistance.get(nSize - 1)
								.getmTaskAnnounceTime();
						// 保存最旧的任务id
						// mnMaxDistanceForShareNear = nDistance;
						mnOldTimeForShareNear = strTime;
					}
				}
			}
		} else {// 此时也更新数据
			mShareTaskDataDistance = goodService.getShareNearData(
					MyLocation.mLongitude, MyLocation.mLatitude,
					(double) mnMaxDistance, strDistrictName);
			if (mShareTaskDataDistance != null) {
				int nSize = mShareTaskDataDistance.size();
				if (nSize > 0) {
					int nDistance = 0;
					String strTime;
					nDistance = mShareTaskDataDistance.get(nSize - 1)
							.getnDistance();
					strTime = mShareTaskDataDistance.get(nSize - 1)
							.getmTaskAnnounceTime();
					// 保存最旧的任务id
					// mnMaxDistanceForShareNear = nDistance;
					mnOldTimeForShareNear = strTime;
				}
			}
		}
	}

	private void ReadHelpData() {
		if (mHelpTaskData != null) {
			// 如果此时求助中没有数据,那么就加载
			if (mHelpTaskData.size() <= 0) {
				// 一次性更新mnVolume条数据
				mHelpTaskData = goodService.UpdateTaskDataForLimit(
						nMaxDataLine, 1, 0);
				if (mHelpTaskData != null) {
					int nSize = mHelpTaskData.size();
					if (nSize > 0) {
						int nIndex = 0;
						nIndex = Integer.parseInt(mHelpTaskData.get(nSize - 1)
								.getmstrId().toString());
						// 保存最旧的任务id
						mnHelpTaskMaxOldIndex = nIndex;
						// 保存最新的任务id
						nIndex = Integer.parseInt(mHelpTaskData.get(0)
								.getmstrId().toString());
						mnHelpTaskMaxNewIndex = nIndex;
					}
				}
			}
		} else {// 此时也更新数据
			mHelpTaskData = goodService.UpdateTaskDataForLimit(nMaxDataLine, 1,
					0);
			if (mHelpTaskData != null) {
				int nSize = mHelpTaskData.size();
				if (nSize > 0) {
					int nIndex = 0;
					nIndex = Integer.parseInt(mHelpTaskData.get(nSize - 1)
							.getmstrId().toString());
					// 保存最旧的任务id
					mnHelpTaskMaxOldIndex = nIndex;
					// 保存最新的任务id
					nIndex = Integer.parseInt(mHelpTaskData.get(0).getmstrId()
							.toString());
					mnHelpTaskMaxNewIndex = nIndex;
				}
			}
		}
	}

	private void ReadShareData() {
		if (mShareTaskData != null) {
			// 如果此时求助中没有数据,那么就加载
			if (mShareTaskData.size() <= 0) {
				// 一次性更新mnVolume条数据
				mShareTaskData = goodService.UpdateTaskDataForLimitNew(
						nMaxDataLine, 2, mnNewTimeForShareAll);
				if (mShareTaskData != null) {
					int nSize = mShareTaskData.size();
					if (nSize > 0) {
						String strTime = mShareTaskData.get(nSize - 1)
								.getmTaskAnnounceTime().toString();
						// 保存最旧的任务id
						mnOldTimeForShareAll = strTime;
						// 保存最新的任务id
						strTime = mShareTaskData.get(0).getmTaskAnnounceTime()
								.toString();
						mnNewTimeForShareAll = strTime;
					}
				}
			}
		} else {// 此时也更新数据
			mShareTaskData = goodService.UpdateTaskDataForLimitNew(
					nMaxDataLine, 2, mnNewTimeForShareAll);
			if (mShareTaskData != null) {
				int nSize = mShareTaskData.size();
				if (nSize > 0) {
					String strTime = mShareTaskData.get(nSize - 1)
							.getmTaskAnnounceTime().toString();
					// 保存最旧的任务id
					mnOldTimeForShareAll = strTime;
					// 保存最新的任务id
					strTime = mShareTaskData.get(0).getmTaskAnnounceTime()
							.toString();
					mnNewTimeForShareAll = strTime;
				}
			}
		}
	}

	private void GetHelpOrShareData() {
		Runnable rundata = new Runnable() {
			public void run() {
				// 如果是周边
				if (1 == mnDataType) {
					// 等待程序获取到当前用户经纬度
					while (-1 == MyLocation.mLatitude) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					strDistrictName = "'" + MyLocation.mDistrictName + "'";
					// 读取区中按时间排序的分享数据
					ReadShareDistanceData();

					Message msg = myhandler.obtainMessage();
					msg.what = 4;
					myhandler.sendMessage(msg);

				} else if (2 == mnDataType) {// 如果是广场
					// 读取按时间排序的分享数据
					ReadShareData();
					Message msg = myhandler.obtainMessage();
					msg.what = 4;
					myhandler.sendMessage(msg);
				}
			}
		};
		bDataFinishLoad = false;
		m_ProgressDialog = ProgressDialog.show(RenPinMainActivity.this, "提示",
				"正在加载数据,请等待...", true);
		// 可以使得按返回键关闭等待框
		m_ProgressDialog.setCancelable(true);
		// 启动获取评论数据线程
		Thread thread = new Thread(rundata);
		thread.start();
	}

	// 根据动态信息中的分享id号生成一个id号字符串
	private String GetIdString() {
		String strID = "";
		if (mDynamicNew != null) {
			int nSize = mDynamicNew.size();
			for (int i = 0; i < nSize; i++) {
				String strid = mDynamicNew.get(i).getmstrId();
				if (strid.equals("")) {
					strID += "0";
				} else {
					strID += strid;
				}
				if (i < nSize - 1) {
					strID += ",";
				}
			}
		}
		return strID;
	}

	private void CombineDynamicData(List<TaskInfoDetail> mdatas) {
		// 判断是否存在系统消息
		boolean bIsExist = false;
		if (mdatas != null) {
			if (null == mDynamicNew) {
				mDynamicNew = mdatas;
			} else {
				int nSize = mdatas.size();
				int nCount = mDynamicNew.size();
				// 获取系统消息是否存在
				if (nCount > 0) {
					if (mDynamicNew.get(0).getmstrId().equals("")) {
						bIsExist = true;
					}
				}
				if (nSize > 0) {
					// 如果第一个不是系统消息,而客户端有系统消息,那么就将通知消息个数赋值给系统消息
					if (!mdatas.get(0).getmstrId().equals("") && nCount > 0) {
						if (mDynamicNew.get(0).getmstrId().equals("")) {
							mDynamicNew.get(0).setnDynamicNewsNum(
									mdatas.get(0).getnDynamicNewsNum());
						}
					}
				}
				for (int i = nSize - 1; i >= 0; i--) {
					boolean bIsFind = false;
					nCount = mDynamicNew.size();
					for (int j = 0; j < nCount; j++) {
						// 如果找到了相同的分享id号,说明这是数据的更新
						if (mdatas.get(i).getmstrId()
								.equals(mDynamicNew.get(j).getmstrId())) {
							// 替换掉mDynamicNew中相应的数据
							mDynamicNew.set(j, mdatas.get(i));
							bIsFind = true;
							break;
						}
					}
					// 如果没有找到,说明是新数据,将新数据添加到mDynamicNew中
					if (!bIsFind) {
						// 如果是系统消息
						if (mdatas.get(i).getmstrId().equals("")) {
							// 将系统消息数据添加到mDynamicNew的最前端
							mDynamicNew.add(0, mdatas.get(i));
						} else {// 如果不是系统消息
								// 如果存在系统消息
							if (bIsExist) {
								// 将该消息数据插入到系统消息数据的下面
								mDynamicNew.add(1, mdatas.get(i));
							} else {
								mDynamicNew.add(0, mdatas.get(i));
							}
						}
					}
				}
			}
		}
	}

	Runnable run = new Runnable() {
		public void run() {
			while (mbIsRunning) {
				if (mbIsFinished) {
					// 将刷新标志设为不刷新状态
					mbIsFinished = false;
					// 如果需要更新数据
					if ((1 == UpdateDataService.mShouldUpdate
							.getnUpdateSignal() || bIsFirstStart)
							&& !mCurrentPersonName.equals("")) {
						UpdateDataService.mShouldUpdate.setnUpdateSignal(0);

						// 获取动态信息中的id字符串
						String strID = GetIdString();// mDynamicNew
						// 获取我的显示数据
						List<TaskInfoDetail> mdatas = goodService
								.GetMsgInfoNumNewDetail2(mCurrentPersonName,
										strID);
						// 将新获取到的数据合并到原数据中
						CombineDynamicData(mdatas);
						bIsFirstStart = false;
					}
					Message msg = myhandler.obtainMessage();
					msg.what = 1;
					myhandler.sendMessage(msg);
					// 每隔1秒刷新一次界面
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	};

	private void ShowWindow() {
		// 我的
		if (1 == mIsShowingWindow) {
			linearlayout2.setVisibility(View.GONE);
			linearlayout3.setVisibility(View.VISIBLE);
			mPullToRefreshView1.setVisibility(View.GONE);
			mListView2.setVisibility(View.VISIBLE);
			emptytextview.setVisibility(View.VISIBLE);
			linearlayout5.setVisibility(View.GONE);
			linearlayout6.setVisibility(View.GONE);

		} else if (2 == mIsShowingWindow) {// 求助
			linearlayout2.setVisibility(View.VISIBLE);
			linearlayout3.setVisibility(View.GONE);
			mPullToRefreshView1.setVisibility(View.VISIBLE);
			linearlayout5.setVisibility(View.GONE);
			linearlayout6.setVisibility(View.GONE);
		} else if (3 == mIsShowingWindow) {// 分享
			linearlayout2.setVisibility(View.VISIBLE);
			linearlayout3.setVisibility(View.GONE);

			mPullToRefreshView1.setVisibility(View.VISIBLE);
			linearlayout5.setVisibility(View.GONE);
			linearlayout6.setVisibility(View.GONE);
		} else if (4 == mIsShowingWindow) {// 更多
			linearlayout2.setVisibility(View.GONE);
			linearlayout3.setVisibility(View.GONE);
			mPullToRefreshView1.setVisibility(View.GONE);
			mListView2.setVisibility(View.GONE);
			emptytextview.setVisibility(View.GONE);
			linearlayout5.setVisibility(View.VISIBLE);
			linearlayout6.setVisibility(View.VISIBLE);
		}
	}

	private void InitActivities() {

		mListView2 = (ListView) findViewById(R.id.ren_pin_listView2);
		emptytextview = (TextView)findViewById(R.id.ren_pin_listviewemptytextview);
		mListView2.setEmptyView(emptytextview);
		emptytextview.setVisibility(View.GONE);
		nonetworklayout = (LinearLayout) findViewById(R.id.upshowlayout_nonetwork);
		morelayout = (LinearLayout) findViewById(R.id.more_linearlayout);
		mAnnounceTaskImageView = (ImageView) findViewById(R.id.renpin_announceimage);
		textview6 = (TextView) findViewById(R.id.renpin_textiview6);
		linearlayout1 = (LinearLayout) findViewById(R.id.dynamic_state_layoutnew);
		linearlayout2 = (LinearLayout) findViewById(R.id.ren_pin_layout1);
		linearlayout3 = (LinearLayout) findViewById(R.id.ren_pin_linearlayout2);
		linearlayout4 = (LinearLayout) findViewById(R.id.near_linear_layout);
		linearlayout5 = (LinearLayout) findViewById(R.id.ren_pin_linearlayout3);
		linearlayout6 = (LinearLayout) findViewById(R.id.top_linearlayout2);
		linearlayout7 = (LinearLayout) findViewById(R.id.share_linear_layout);
		advicetextview = (RelativeLayout) findViewById(R.id.renpinmain_advice);
		aboutustextview = (RelativeLayout) findViewById(R.id.renpinmain_aboutus);
		moreoperalinearlayout = (LinearLayout) findViewById(R.id.renpinmain_moreoperalinear);
		moreoperalinearlayout.setVisibility(View.GONE);
		versionupdaterelative = (RelativeLayout) findViewById(R.id.renpinmain_relativelayout);
		versionupdateinfotextview = (TextView) findViewById(R.id.renpinmain_prompttextview);
		versionupdateinfotextview.setText("");
		nearbutton = (Button) findViewById(R.id.renpin_button1);
		spacebutton = (Button) findViewById(R.id.renpin_button2);
		mgridview = (GridView) findViewById(R.id.main_gridview);
		mAnnounceTextView = (TextView) findViewById(R.id.renpinmain_annoucetextview);
		mMoreTextView = (TextView) findViewById(R.id.renpinmain_moretextview);
		mMoreReturnImageView = (ImageView) findViewById(R.id.renpinmain_morereturnimageview);
		mMyTopReturnImageView = (ImageView) findViewById(R.id.customer_login_image1);
		mPersonInfoLinearLayout = (LinearLayout) findViewById(R.id.renpinmain_personinfolinearlayout);
		mPersonImageView = (ImageView) findViewById(R.id.renpinmain_personimage);
		mPersonNickTextView = (TextView) findViewById(R.id.renpinmain_personname);
		mLoginTextView = (TextView) findViewById(R.id.renpinmain_logintextview);

		// 如果用户没有登录
		if (mCurrentPersonName.equals("")) {
			mPersonInfoLinearLayout.setVisibility(View.GONE);
			mLoginTextView.setVisibility(View.VISIBLE);
			mLoginTextView.setText("登录");
		} else {
			mPersonInfoLinearLayout.setVisibility(View.VISIBLE);
			mLoginTextView.setVisibility(View.GONE);
			mPersonNickTextView.setText(mCurrentPersonNameTemp);
			String strImage = msettings.getString("Base64Image", "");
			mPersonImageView.setImageBitmap(mUtils.base64ToBitmap(strImage));
		}

		if (1 == mnDataType) {
			nearbutton.setBackgroundColor(Color.rgb(251, 188, 64));
			nearbutton.setTextColor(Color.rgb(255, 255, 255));
			spacebutton.setBackgroundColor(Color.rgb(240, 240, 240));
			spacebutton.setTextColor(Color.rgb(128, 128, 128));
		} else {
			spacebutton.setBackgroundColor(Color.rgb(251, 188, 64));
			spacebutton.setTextColor(Color.rgb(255, 255, 255));
			nearbutton.setBackgroundColor(Color.rgb(240, 240, 240));
			nearbutton.setTextColor(Color.rgb(128, 128, 128));
		}

		if (1 == mIsShowingWindow) {
			// 设置背景色
			morelayout.setBackgroundColor(Color.rgb(41, 36, 33));
		} else if (2 == mIsShowingWindow) {
			morelayout.setBackgroundColor(Color.rgb(41, 36, 33));
		} else if (3 == mIsShowingWindow) {
			morelayout.setBackgroundColor(Color.rgb(41, 36, 33));
		} else if (4 == mIsShowingWindow) {
			morelayout.setBackgroundColor(Color.rgb(82, 82, 82));
		}

		// 更多界面控件
		my_textview1 = (TextView) findViewById(R.id.more_opera_Accounttextview1);
		my_imageview1 = (ImageView) findViewById(R.id.more_opera_imageview1);
		my_textview2 = (TextView) findViewById(R.id.more_opera_credittextview2);
		my_textview3 = (TextView) findViewById(R.id.more_opera_charmtextview3);
		my_imageview2 = (ImageView) findViewById(R.id.more_opera_imageview2);

		if (mCurrentPersonName.equals("")) {
			my_textview1.setText("点击登录");
			my_textview1.setTextColor(Color.rgb(0, 135, 255));
			my_imageview2.setImageBitmap(null);
		} else {
			my_textview1.setTextColor(Color.rgb(0, 0, 0));
			my_textview1.setText(mCurrentPersonNameTemp);
			my_textview2.setText("人品:");
			my_textview3.setText("");
			Resources res = getResources();
			Bitmap zanbmp = BitmapFactory.decodeResource(res, R.drawable.zan1);
			my_imageview2.setImageBitmap(zanbmp);

			String strBase64Image = msettings.getString("Base64Image", "");
			if (!strBase64Image.equals("")) {
				CommonUtils utils = new CommonUtils(this);
				Bitmap bmp = utils.base64ToBitmap(strBase64Image);
				my_imageview1.setImageBitmap(bmp);
			} else {
				res = getResources();
				Bitmap bmp = BitmapFactory.decodeResource(res,
						R.drawable.noperson);
				my_imageview1.setImageBitmap(bmp);
			}
		}

		my_textview1.setOnClickListener(new TextView.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RenPinMainActivity.this,
						LoginActivity.class);
				startActivityForResult(intent, 20);
			}

		});
		// 更多界面结尾
		ShowWindow();
		myinfolinearlayout = (LinearLayout) findViewById(R.id.activity_renpin_myinfolinearlayout);

		morelayout.setOnClickListener(new RelativeLayout.OnClickListener() {

			Runnable run1 = new Runnable() {
				public void run() {
					// 获取用户赞个数
					int nCharm = goodService.GetCharmValue(mCurrentPersonName);
					int nCredit = goodService
							.GetCreditValue(mCurrentPersonName);
					Message msg = myhandler.obtainMessage();
					Bundle b = new Bundle();// 存放数据
					b.putInt("nCharm", nCharm);
					b.putInt("nCredit", nCredit);
					msg.setData(b);
					msg.what = 0;
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View arg0) {
				linearlayout2.setVisibility(View.GONE);
				linearlayout3.setVisibility(View.GONE);
				mPullToRefreshView1.setVisibility(View.GONE);
				mListView2.setVisibility(View.GONE);
				emptytextview.setVisibility(View.GONE);
				linearlayout5.setVisibility(View.VISIBLE);
				linearlayout6.setVisibility(View.VISIBLE);
				mIsShowingWindow = 4;
				// 设置背景色
				// linearlayout1.setBackgroundColor(Color.rgb(41, 36, 33));
				linearlayout4.setBackgroundColor(Color.rgb(41, 36, 33));
				linearlayout7.setBackgroundColor(Color.rgb(41, 36, 33));
				morelayout.setBackgroundColor(Color.rgb(82, 82, 82));
				Thread thread = new Thread(run1);
				thread.start();
			}
		});

		mAnnounceTaskImageView
				.setOnClickListener(new TextView.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (View.VISIBLE == moreoperalinearlayout
								.getVisibility()) {
							moreoperalinearlayout.setVisibility(View.GONE);
						} else {
							moreoperalinearlayout.setVisibility(View.VISIBLE);
						}
					}

				});

		mAnnounceTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断是否登录
				String strPersonName = msettings
						.getString("TruePersonName", "");
				if (strPersonName.equals("")) {
					Intent intent = new Intent(RenPinMainActivity.this,
							LoginActivity.class);
					startActivityForResult(intent, 20);
					return;
				}
				Intent intent = new Intent(RenPinMainActivity.this,
						TaskAnnounceActivity.class);
				// 显示我的界面
				startActivity(intent);
			}

		});

		mMoreTextView.setOnClickListener(new OnClickListener() {

			Runnable run1 = new Runnable() {
				public void run() {
					// 获取用户赞个数
					int nCharm = goodService.GetCharmValue(mCurrentPersonName);
					int nCredit = goodService
							.GetCreditValue(mCurrentPersonName);
					Message msg = myhandler.obtainMessage();
					Bundle b = new Bundle();// 存放数据
					b.putInt("nCharm", nCharm);
					b.putInt("nCredit", nCredit);
					msg.setData(b);
					msg.what = 0;
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View arg0) {
				linearlayout2.setVisibility(View.GONE);
				linearlayout3.setVisibility(View.GONE);
				mPullToRefreshView1.setVisibility(View.GONE);
				moreoperalinearlayout.setVisibility(View.GONE);
				mListView2.setVisibility(View.GONE);
				emptytextview.setVisibility(View.GONE);
				linearlayout5.setVisibility(View.VISIBLE);
				linearlayout6.setVisibility(View.VISIBLE);
				mIsShowingWindow = 4;
				// 设置背景色
				// linearlayout1.setBackgroundColor(Color.rgb(41, 36, 33));
				linearlayout4.setBackgroundColor(Color.rgb(41, 36, 33));
				linearlayout7.setBackgroundColor(Color.rgb(41, 36, 33));
				morelayout.setBackgroundColor(Color.rgb(82, 82, 82));
				Thread thread = new Thread(run1);
				thread.start();
			}

		});

		mMoreReturnImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				linearlayout2.setVisibility(View.VISIBLE);
				// tasktextview.setText("分享");
				linearlayout3.setVisibility(View.GONE);
				mPullToRefreshView1.setVisibility(View.VISIBLE);
				// 如果是从另外一个窗口切换过来的,那么就要重新设置数据
				if (mIsShowingWindow != 3) {
					if (1 == mnDataType) {
						ShowNearShareDataForClick(1);
					} else if (2 == mnDataType) {
						ShowSpaceShareDataForClick(1);
					}
				} else {
					if (1 == mnDataType) {
						ShowNearShareDataForClick(0);
					} else if (2 == mnDataType) {
						ShowSpaceShareDataForClick(0);
					}
				}
				mListView2.setVisibility(View.GONE);
				emptytextview.setVisibility(View.GONE);
				linearlayout5.setVisibility(View.GONE);
				linearlayout6.setVisibility(View.GONE);
				mIsShowingWindow = 3;
			}

		});

		mMyTopReturnImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				linearlayout2.setVisibility(View.VISIBLE);
				// tasktextview.setText("分享");
				linearlayout3.setVisibility(View.GONE);
				mPullToRefreshView1.setVisibility(View.VISIBLE);
				// 如果是从另外一个窗口切换过来的,那么就要重新设置数据
				if (mIsShowingWindow != 3) {
					if (1 == mnDataType) {
						ShowNearShareDataForClick(1);
					} else if (2 == mnDataType) {
						ShowSpaceShareDataForClick(1);
					}
				} else {
					if (1 == mnDataType) {
						ShowNearShareDataForClick(0);
					} else if (2 == mnDataType) {
						ShowSpaceShareDataForClick(0);
					}
				}
				mListView2.setVisibility(View.GONE);
				emptytextview.setVisibility(View.GONE);
				linearlayout5.setVisibility(View.GONE);
				linearlayout6.setVisibility(View.GONE);
				mIsShowingWindow = 3;
			}

		});

		// "我的"listview中选项长按删除
		mListView2.setOnItemLongClickListener(new OnItemLongClickListener() {

			// 删除对话框
			private void dialog(AdapterView<?> l, final View view,
					final int position) {

				ListView listView1 = (ListView) l;
				@SuppressWarnings("unchecked")
				final HashMap<String, Object> map = (HashMap<String, Object>) listView1
						.getItemAtPosition(position);

				AlertDialog.Builder builder = new Builder(
						RenPinMainActivity.this);
				builder.setMessage("确定要删除该项吗?");
				builder.setTitle("提示");
				builder.setPositiveButton("确认",
						new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Runnable DeleteDynameicrun = new Runnable() {
									public void run() {
										String strTaskId = map.get("taskid")
												.toString();
										String strType = map.get("tasktype")
												.toString();
										String strPersonName = msettings
												.getString("TruePersonName", "");
										// 删除数据库中的数据
										int nRet = goodService
												.DeleteMineDynamicInfo(
														strTaskId, strType,
														strPersonName);
										Message msg = myhandler.obtainMessage();
										Bundle b = new Bundle();// 存放数据
										b.putInt("nRet", nRet);
										b.putString("taskid", strTaskId);
										b.putString("tasktype", strType);
										msg.setData(b);
										msg.what = 8;
										myhandler.sendMessage(msg);
									}
								};
								Thread thre = new Thread(DeleteDynameicrun);
								thre.start();
							}
						});
				builder.setNegativeButton("取消",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();
			}

			@Override
			public boolean onItemLongClick(AdapterView<?> l, final View v,
					final int position, long arg3) {
				dialog(l, v, position);
				return true;
			}

		});
		// 点击列表中的某一项
		mListView2.setOnItemClickListener(new OnItemClickListener() {
			String strCustomerName;// 发布用户名称
			String strTaskAnnounceTime;
			String strTimeLimit;
			String strTaskTitle;
			String strDetail;
			String strTaskRunSeconds;
			String strTaskId;
			String strImplementName;// 接收者名称
			String strCustomerNameTemp;// 发布用户昵称
			String strImplementNameTemp;// 接收者昵称
			int nTimeStatus = 1;
			int nImpleStatus = 1;
			int nTaskType = 0;

			int nTaskSelectType;
			int nTaskFinishType;
			int nTaskVerifiType;
			int nTaskAnnounceCommentType;
			int nTaskImplementCommentType;
			// 发布者对执行者的评论
			String strAnnounceComment;
			// 发布者给执行者的图片
			String strAnnounceBase64Image;
			// 执行者对发布者的评论
			String strImpleComment;
			// 执行者给发布者的图片
			String strImpleBase64Image;
			int nCharmValue = 0;
			int nCommentNum = 0;
			int nBrowseNum = 0;
			// 发布地址
			String strAddressRegion = "";

			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				ListView listView1 = (ListView) adapterView;
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) listView1
						.getItemAtPosition(position);

				// 如果该项被点击了,那么就说明这个新状态被用户看到了,需要取消这个状态
				strCustomerName = map.get("customername").toString();
				strTaskAnnounceTime = map.get("TaskAnnounceTime").toString();
				strTimeLimit = map.get("TaskTimeLimit").toString();
				strTaskTitle = map.get("tasktitle").toString();
				strDetail = map.get("TaskDetail").toString();
				mAnnounceImage = (Bitmap) map.get("customericon");
				strTaskRunSeconds = map.get("taskruntime").toString();
				strTaskId = map.get("taskid").toString();
				strImplementName = map.get("taskimplementname").toString();
				strCustomerNameTemp = map.get("customernametemp").toString();
				strImplementNameTemp = map.get("taskimplementnametemp")
						.toString();
				nTimeStatus = Integer.parseInt(map.get("tasktimestatus")
						.toString());
				nImpleStatus = Integer.parseInt(map.get("taskimplestatue")
						.toString());
				nTaskType = Integer.parseInt(map.get("tasktype").toString());

				nTaskSelectType = Integer.parseInt(map.get("nTaskSelectType")
						.toString());
				nTaskFinishType = Integer.parseInt(map.get("nTaskFinishType")
						.toString());
				nTaskVerifiType = Integer.parseInt(map.get("nTaskVerifiType")
						.toString());
				nTaskAnnounceCommentType = Integer.parseInt(map.get(
						"nTaskAnnounceCommentType").toString());
				nTaskImplementCommentType = Integer.parseInt(map.get(
						"nTaskImplementCommentType").toString());
				strAnnounceComment = map.get("strAnnounceComment").toString();
				strAnnounceBase64Image = map.get("strAnnounceBase64Image")
						.toString();
				strImpleComment = map.get("strImpleComment").toString();
				strImpleBase64Image = map.get("strImpleBase64Image").toString();
				nCharmValue = Integer.parseInt(map.get("nCharmValue")
						.toString());
				nCommentNum = Integer.parseInt(map.get("nCommentNum")
						.toString());
				nBrowseNum = Integer.parseInt(map.get("nBrowseNum").toString());
				strAddressRegion = map.get("strAddressRegion").toString();

				String strPersonName = msettings
						.getString("TruePersonName", "");
				if (strPersonName.equals("")) {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"请先登录", Toast.LENGTH_LONG);
					return;
				}

				// 如果该任务有新提示则将该任务或分享中的红点去掉
				CancleRedHot(strTaskId, nTaskType);

				Intent it = null;
				if (strTaskId.equals("")) {
					it = new Intent(RenPinMainActivity.this,
							ShareKingActivity.class);
					String strBaseImage = mUtils
							.BitmapToBase64BySize(mAnnounceImage);
					// 达人名称
					it.putExtra("com.renpin.RenPinMainActivity.customername",
							strCustomerName);
					// 达人昵称
					it.putExtra(
							"com.renpin.RenPinMainActivity.strCustomerNameTemp",
							strCustomerNameTemp);
					// 达人头像
					it.putExtra("com.renpin.RenPinMainActivity.mAnnounceImage",
							strBaseImage);
					// 时间
					it.putExtra(
							"com.renpin.RenPinMainActivity.strAddressRegion",
							strAddressRegion);
					// 分享次数
					it.putExtra("com.renpin.RenPinMainActivity.nCommentNum",
							nCommentNum);

				} else {
					if (1 == nTaskType) {
						it = new Intent(RenPinMainActivity.this,
								DynamicDetailActivity.class);
					} else if (2 == nTaskType) {
						it = new Intent(RenPinMainActivity.this,
								ShareDynamicDetailActivity.class);
					}

					it.putExtra("com.renpin.RenPinMainActivity.customername",
							strCustomerName);
					it.putExtra(
							"com.renpin.RenPinMainActivity.TaskAnnounceTime",
							strTaskAnnounceTime);

					it.putExtra("com.renpin.RenPinMainActivity.TimeLimit",
							strTimeLimit);
					it.putExtra("com.renpin.RenPinMainActivity.TaskTitle",
							strTaskTitle);
					it.putExtra("com.renpin.RenPinMainActivity.Detail",
							strDetail);
					it.putExtra("com.renpin.RenPinMainActivity.RunSeconds",
							strTaskRunSeconds);
					it.putExtra("com.renpin.RenPinMainActivity.TaskId",
							strTaskId);
					it.putExtra(
							"com.renpin.RenPinMainActivity.TaskImplementName",
							strImplementName);
					it.putExtra(
							"com.renpin.RenPinMainActivity.strCustomerNameTemp",
							strCustomerNameTemp);
					it.putExtra(
							"com.renpin.RenPinMainActivity.strImplementNameTemp",
							strImplementNameTemp);
					it.putExtra("com.renpin.RenPinMainActivity.nTimeStatus",
							nTimeStatus);
					it.putExtra(
							"com.renpin.RenPinMainActivity.nTaskImplemeStatus",
							nImpleStatus);

					it.putExtra(
							"com.renpin.RenPinMainActivity.strAnnounceComment",
							strAnnounceComment);
					it.putExtra(
							"com.renpin.RenPinMainActivity.strAnnounceBase64Image",
							strAnnounceBase64Image);
					it.putExtra(
							"com.renpin.RenPinMainActivity.strImpleComment",
							strImpleComment);
					it.putExtra(
							"com.renpin.RenPinMainActivity.strImpleBase64Image",
							strImpleBase64Image);
					it.putExtra("com.renpin.RenPinMainActivity.nTaskType",
							nTaskType);

					// 动态内容
					it.putExtra(
							"com.renpin.RenPinMainActivity.nTaskSelectType",
							nTaskSelectType);
					it.putExtra(
							"com.renpin.RenPinMainActivity.nTaskFinishType",
							nTaskFinishType);
					it.putExtra(
							"com.renpin.RenPinMainActivity.nTaskVerifiType",
							nTaskVerifiType);
					it.putExtra(
							"com.renpin.RenPinMainActivity.nTaskAnnounceCommentType",
							nTaskAnnounceCommentType);
					it.putExtra(
							"com.renpin.RenPinMainActivity.nTaskImplementCommentType",
							nTaskImplementCommentType);

					it.putExtra("com.renpin.RenPinMainActivity.nCharmValue",
							nCharmValue);
					it.putExtra("com.renpin.RenPinMainActivity.nCommentNum",
							nCommentNum);
					it.putExtra("com.renpin.RenPinMainActivity.nBrowseNum",
							nBrowseNum);
					it.putExtra(
							"com.renpin.RenPinMainActivity.strAddressRegion",
							strAddressRegion);
				}
				startActivity(it);
			}
		});

		linearlayout1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//判断当前是否由用户登录
				String strPersonName = msettings
						.getString("TruePersonName", "");
				if(strPersonName.equals("")){
					emptytextview.setText("请先登录哦!");
				}else{
					emptytextview.setText("还没有内容,去发布一个分享或评论别人吧!");
				}
				mPullToRefreshView1.setVisibility(View.GONE);
				mListView2.setVisibility(View.VISIBLE);
				emptytextview.setVisibility(View.VISIBLE);
				mIsShowingWindow = 1;
				moreoperalinearlayout.setVisibility(View.GONE);
				linearlayout2.setVisibility(View.GONE);
				linearlayout3.setVisibility(View.VISIBLE);
				linearlayout5.setVisibility(View.GONE);
				linearlayout6.setVisibility(View.GONE);
			}

		});

		linearlayout7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				linearlayout2.setVisibility(View.VISIBLE);
				// tasktextview.setText("分享");
				linearlayout3.setVisibility(View.GONE);
				mPullToRefreshView1.setVisibility(View.VISIBLE);
				// 如果是从另外一个窗口切换过来的,那么就要重新设置数据
				if (mIsShowingWindow != 3) {
					if (1 == mnDataType) {
						ShowNearShareDataForClick(1);
					} else if (2 == mnDataType) {
						ShowSpaceShareDataForClick(1);
					}
				} else {
					if (1 == mnDataType) {
						ShowNearShareDataForClick(0);
					} else if (2 == mnDataType) {
						ShowSpaceShareDataForClick(0);
					}
				}
				mListView2.setVisibility(View.GONE);
				emptytextview.setVisibility(View.GONE);
				linearlayout5.setVisibility(View.GONE);
				linearlayout6.setVisibility(View.GONE);
				// 设置背景色
				// linearlayout1.setBackgroundColor(Color.rgb(41, 36, 33));
				linearlayout4.setBackgroundColor(Color.rgb(41, 36, 33));
				linearlayout7.setBackgroundColor(Color.rgb(82, 82, 82));
				morelayout.setBackgroundColor(Color.rgb(41, 36, 33));
				mIsShowingWindow = 3;
			}

		});

		linearlayout4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				linearlayout2.setVisibility(View.VISIBLE);
				// tasktextview.setText("求助");
				linearlayout3.setVisibility(View.GONE);
				mPullToRefreshView1.setVisibility(View.VISIBLE);
				// 如果窗口中没有数据,那么就不能进行上拉加载,下拉更新的操作,所以这里在每次点击
				// 的时候都要去自动读数据
				if (mIsShowingWindow != 2) {
					if (1 == mnDataType) {
						ShowNearHelpDataForClick(1);
					} else if (2 == mnDataType) {
						ShowSpaceHelpDataForClick(1);
					}
				} else {
					if (1 == mnDataType) {
						ShowNearHelpDataForClick(0);
					} else if (2 == mnDataType) {
						ShowSpaceHelpDataForClick(0);
					}
				}
				mListView2.setVisibility(View.GONE);
				emptytextview.setVisibility(View.GONE);
				linearlayout5.setVisibility(View.GONE);
				linearlayout6.setVisibility(View.GONE);
				// 设置背景色
				// linearlayout1.setBackgroundColor(Color.rgb(41, 36, 33));
				linearlayout4.setBackgroundColor(Color.rgb(82, 82, 82));
				linearlayout7.setBackgroundColor(Color.rgb(41, 36, 33));
				morelayout.setBackgroundColor(Color.rgb(41, 36, 33));
				mIsShowingWindow = 2;
			}

		});

		advicetextview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 打开意见反馈界面
				Intent it = new Intent(RenPinMainActivity.this,
						AdviceActivity.class);
				startActivity(it);
			}
		});

		aboutustextview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 打开意见反馈界面
				Intent it = new Intent(RenPinMainActivity.this,
						AboutUsActivity.class);
				startActivity(it);
			}
		});

		versionupdaterelative.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mupdateinfo != null) {

					String strDescript = mupdateinfo.getDescription();
					// 如果当前版本比服务器版本低
					if (localVersion < Integer.parseInt(mupdateinfo
							.getVersion())) {
						// 发现新版本，提示用户更新
						AlertDialog.Builder alert = new AlertDialog.Builder(
								RenPinMainActivity.this);
						alert.setTitle("有新版本可用")
								.setMessage(strDescript)
								.setPositiveButton("现在升级",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												// 创建文件
												FileUtil.createFile(getResources()
														.getString(
																R.string.app_name1));
												createNotification();
												createThread();
											}
										})
								.setNegativeButton("以后再说",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
											}
										});
						alert.create().show();
					} else {
						CommonUtils.ShowToastCenter(RenPinMainActivity.this,
								"您当前的版本为最新版本,不需要更新", Toast.LENGTH_LONG);
					}
				}
			}
		});

		myinfolinearlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String mCurrentPersonName = msettings.getString(
						"TruePersonName", "");
				if (!mCurrentPersonName.equals("")) {
					Intent it = new Intent(RenPinMainActivity.this,
							MyInfoActivity.class);
					startActivityForResult(it, 0);
				} else {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"请先登录", Toast.LENGTH_LONG);
				}
			}

		});

		nearbutton.setOnClickListener(new OnClickListener() {

			Runnable nearclickrun = new Runnable() {
				public void run() {
					// 如果为求助
					if (2 == mIsShowingWindow) {
						// 读取区中按时间排序的求助数据
						ReadHelpDistanceData();
					} else if (3 == mIsShowingWindow) {// 为分享
						// 读取区中按时间排序的分享数据
						ReadShareDistanceData();
					}
					Message msg = myhandler.obtainMessage();
					msg.what = 7;
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {
				mnDataType = 1;
				nearbutton.setBackgroundColor(Color.rgb(251, 188, 64));
				nearbutton.setTextColor(Color.rgb(255, 255, 255));
				spacebutton.setBackgroundColor(Color.rgb(240, 240, 240));
				spacebutton.setTextColor(Color.rgb(128, 128, 128));

				Thread thre = new Thread(nearclickrun);
				thre.start();
			}

		});

		spacebutton.setOnClickListener(new OnClickListener() {

			Runnable spaceclickrun = new Runnable() {
				public void run() {
					// 如果为求助
					if (2 == mIsShowingWindow) {
						// 读取按时间排序的求助数据
						ReadHelpData();
					} else if (3 == mIsShowingWindow) {// 为分享
						// 读取按时间排序的分享数据
						ReadShareData();
					}
					Message msg = myhandler.obtainMessage();
					msg.what = 7;
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {
				mnDataType = 2;
				spacebutton.setBackgroundColor(Color.rgb(251, 188, 64));
				spacebutton.setTextColor(Color.rgb(255, 255, 255));
				nearbutton.setBackgroundColor(Color.rgb(240, 240, 240));
				nearbutton.setTextColor(Color.rgb(128, 128, 128));
				Thread thre = new Thread(spaceclickrun);
				thre.start();
			}

		});

		mLoginTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RenPinMainActivity.this,
						LoginActivity.class);
				startActivityForResult(intent, 20);
			}
		});

		mPersonInfoLinearLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		mgridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String strCustomerNameTemp;// 发布人名称
				String strTaskAnnounceTime;// 发布时间
				String strTimeLimit;// 时间限制
				String strTaskTitle;// 任务标题
				String strDetail;// 任务详细内容
				String strTaskRunSeconds;// 任务执行时间(秒)
				String strTaskId;// 任务id
				String strImplementName;// 任务执行人名称
				String strCustomerNameNick;// 发布人昵称
				String strImplementNick;// 任务执行人昵称
				int nTimeStatus = 1;// 时间状态
				int nImpleStatus = 1;// 完成状态
				String strTaskType;// 任务类型
				// int nCreditValue = 1;// 限制的人品值
				// int nCharmValue = 0;// 限制的赞值

				int nTaskType = 0;
				int nTaskAnnounceCommentType;
				int nTaskImplementCommentType;

				int nCharmValue = 0;
				int nCommentNum = 0;
				int nBrowseNum = 0;
				// 发布区域地址
				String strAddressRegion = "";

				String strPersonName = msettings
						.getString("TruePersonName", "");
				// 如果没有登录,那么就切换到登录界面
				if (strPersonName.equals("")) {
					Intent intent = new Intent(RenPinMainActivity.this,
							LoginActivity.class);
					startActivityForResult(intent, 20);
					return;
				}

				if (1 == mnDataType) {
					DistanceDetail task = null;
					if (2 == mIsShowingWindow) {// 如果是在求助窗口
						task = mHelpTaskDataDistance.get(position);
					} else if (3 == mIsShowingWindow) {// 如果是在分享窗口
						task = mShareTaskDataDistance.get(position);
					}
					if (task != null) {
						strCustomerNameTemp = task.getstrTaskPersonTrueName();
						strTaskAnnounceTime = task.getmTaskAnnounceTime();
						strTimeLimit = task.getmTimeLimit();
						strTaskTitle = task.getmTaskTitle();
						strDetail = task.getmTaskDetail();
						mAnnounceImage = mUtils.base64ToBitmap(task
								.getmTaskAskPersonIcon());
						strTaskRunSeconds = task.getmRunSeconds();
						strTaskId = task.getmstrId();
						strImplementName = task.getstrTaskImplementTrueName();
						strCustomerNameNick = task.getmPersonName();
						strImplementNick = task.getmTaskImplementName();
						nTimeStatus = task.getmnValiableStatus();
						nImpleStatus = task.getmnImplementStatus();
						strTaskType = task.getmnTaskType() + "";

						nTaskType = task.getmnTaskType();
						nTaskAnnounceCommentType = task
								.getnTaskAnnounceCommentType();
						nTaskImplementCommentType = task
								.getnTaskImplementCommentType();

						nCharmValue = task.getnTaskCharmValue();
						nCommentNum = task.getnCommentRecordNum();
						nBrowseNum = task.getnBrowseTimes();
						strAddressRegion = task.getmstrTaskRegion();

						Intent it = null;
						if (1 == nTaskType) {
							it = new Intent(RenPinMainActivity.this,
									DynamicDetailActivity.class);
						} else if (2 == nTaskType) {
							it = new Intent(RenPinMainActivity.this,
									ShareDynamicDetailActivity.class);
						}

						it.putExtra(
								"com.renpin.RenPinMainActivity.customername",
								strCustomerNameTemp);
						it.putExtra(
								"com.renpin.RenPinMainActivity.TaskAnnounceTime",
								strTaskAnnounceTime);

						it.putExtra("com.renpin.RenPinMainActivity.TimeLimit",
								strTimeLimit);
						it.putExtra("com.renpin.RenPinMainActivity.TaskTitle",
								strTaskTitle);
						it.putExtra("com.renpin.RenPinMainActivity.Detail",
								strDetail);
						it.putExtra("com.renpin.RenPinMainActivity.RunSeconds",
								strTaskRunSeconds);
						it.putExtra("com.renpin.RenPinMainActivity.TaskId",
								strTaskId);
						it.putExtra(
								"com.renpin.RenPinMainActivity.TaskImplementName",
								strImplementName);
						it.putExtra(
								"com.renpin.RenPinMainActivity.strCustomerNameTemp",
								strCustomerNameNick);
						it.putExtra(
								"com.renpin.RenPinMainActivity.strImplementNameTemp",
								strImplementNick);
						it.putExtra(
								"com.renpin.RenPinMainActivity.nTimeStatus",
								nTimeStatus);
						it.putExtra(
								"com.renpin.RenPinMainActivity.nTaskImplemeStatus",
								nImpleStatus);

						it.putExtra(
								"com.renpin.RenPinMainActivity.strAnnounceComment",
								"");
						it.putExtra(
								"com.renpin.RenPinMainActivity.strAnnounceBase64Image",
								"");
						it.putExtra(
								"com.renpin.RenPinMainActivity.strImpleComment",
								"");
						it.putExtra(
								"com.renpin.RenPinMainActivity.strImpleBase64Image",
								"");
						it.putExtra("com.renpin.RenPinMainActivity.nTaskType",
								nTaskType);

						// 动态内容
						it.putExtra(
								"com.renpin.RenPinMainActivity.nTaskSelectType",
								1);
						it.putExtra(
								"com.renpin.RenPinMainActivity.nTaskFinishType",
								1);
						it.putExtra(
								"com.renpin.RenPinMainActivity.nTaskVerifiType",
								1);
						it.putExtra(
								"com.renpin.RenPinMainActivity.nTaskAnnounceCommentType",
								nTaskAnnounceCommentType);
						it.putExtra(
								"com.renpin.RenPinMainActivity.nTaskImplementCommentType",
								nTaskImplementCommentType);

						it.putExtra(
								"com.renpin.RenPinMainActivity.nCharmValue",
								nCharmValue);
						it.putExtra(
								"com.renpin.RenPinMainActivity.nCommentNum",
								nCommentNum);
						it.putExtra("com.renpin.RenPinMainActivity.nBrowseNum",
								nBrowseNum);
						it.putExtra(
								"com.renpin.RenPinMainActivity.strAddressRegion",
								strAddressRegion);

						startActivityForResult(it, 1);
					}
				} else if (2 == mnDataType) {
					TaskInfoDetail task = null;
					if (2 == mIsShowingWindow) {// 如果是在求助窗口
						task = mHelpTaskData.get(position);
					} else if (3 == mIsShowingWindow) {// 如果是在分享窗口
						task = mShareTaskData.get(position);
					}
					if (task != null) {
						strCustomerNameTemp = task.getstrTaskPersonTrueName();
						strTaskAnnounceTime = task.getmTaskAnnounceTime();
						strTimeLimit = task.getmTimeLimit();
						strTaskTitle = task.getmTaskTitle();
						strDetail = task.getmTaskDetail();
						mAnnounceImage = mUtils.base64ToBitmap(task
								.getmTaskAskPersonIcon());
						strTaskRunSeconds = task.getmRunSeconds();
						strTaskId = task.getmstrId();
						strImplementName = task.getstrTaskImplementTrueName();
						strCustomerNameNick = task.getmPersonName();
						strImplementNick = task.getmTaskImplementName();
						nTimeStatus = task.getmnValiableStatus();
						nImpleStatus = task.getmnImplementStatus();
						strTaskType = task.getmnTaskType() + "";

						nTaskType = task.getmnTaskType();

						nTaskAnnounceCommentType = task
								.getnTaskAnnounceCommentType();
						nTaskImplementCommentType = task
								.getnTaskImplementCommentType();

						nCharmValue = task.getnTaskCharmValue();
						nCommentNum = task.getnCommentRecordNum();
						nBrowseNum = task.getnBrowseTimes();
						strAddressRegion = task.getmstrTaskRegion();

						Intent it = null;
						if (1 == nTaskType) {
							it = new Intent(RenPinMainActivity.this,
									DynamicDetailActivity.class);
						} else if (2 == nTaskType) {
							it = new Intent(RenPinMainActivity.this,
									ShareDynamicDetailActivity.class);
						}
						it.putExtra(
								"com.renpin.RenPinMainActivity.customername",
								strCustomerNameTemp);
						it.putExtra(
								"com.renpin.RenPinMainActivity.TaskAnnounceTime",
								strTaskAnnounceTime);

						it.putExtra("com.renpin.RenPinMainActivity.TimeLimit",
								strTimeLimit);
						it.putExtra("com.renpin.RenPinMainActivity.TaskTitle",
								strTaskTitle);
						it.putExtra("com.renpin.RenPinMainActivity.Detail",
								strDetail);
						it.putExtra("com.renpin.RenPinMainActivity.RunSeconds",
								strTaskRunSeconds);
						it.putExtra("com.renpin.RenPinMainActivity.TaskId",
								strTaskId);
						it.putExtra(
								"com.renpin.RenPinMainActivity.TaskImplementName",
								strImplementName);
						it.putExtra(
								"com.renpin.RenPinMainActivity.strCustomerNameTemp",
								strCustomerNameNick);
						it.putExtra(
								"com.renpin.RenPinMainActivity.strImplementNameTemp",
								strImplementNick);
						it.putExtra(
								"com.renpin.RenPinMainActivity.nTimeStatus",
								nTimeStatus);
						it.putExtra(
								"com.renpin.RenPinMainActivity.nTaskImplemeStatus",
								nImpleStatus);

						it.putExtra(
								"com.renpin.RenPinMainActivity.strAnnounceComment",
								"");
						it.putExtra(
								"com.renpin.RenPinMainActivity.strAnnounceBase64Image",
								"");
						it.putExtra(
								"com.renpin.RenPinMainActivity.strImpleComment",
								"");
						it.putExtra(
								"com.renpin.RenPinMainActivity.strImpleBase64Image",
								"");
						it.putExtra("com.renpin.RenPinMainActivity.nTaskType",
								nTaskType);

						// 动态内容
						it.putExtra(
								"com.renpin.RenPinMainActivity.nTaskSelectType",
								1);
						it.putExtra(
								"com.renpin.RenPinMainActivity.nTaskFinishType",
								1);
						it.putExtra(
								"com.renpin.RenPinMainActivity.nTaskVerifiType",
								1);
						it.putExtra(
								"com.renpin.RenPinMainActivity.nTaskAnnounceCommentType",
								nTaskAnnounceCommentType);
						it.putExtra(
								"com.renpin.RenPinMainActivity.nTaskImplementCommentType",
								nTaskImplementCommentType);

						it.putExtra(
								"com.renpin.RenPinMainActivity.nCharmValue",
								nCharmValue);
						it.putExtra(
								"com.renpin.RenPinMainActivity.nCommentNum",
								nCommentNum);
						it.putExtra("com.renpin.RenPinMainActivity.nBrowseNum",
								nBrowseNum);
						it.putExtra(
								"com.renpin.RenPinMainActivity.strAddressRegion",
								strAddressRegion);

						startActivityForResult(it, 1);
					}
				}
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void CancleRedHot(String strTaskId, int nTaskType) {

		if (mDynamicNew != null) {
			// 如果为系统消息
			if (strTaskId.equals("")) {

				int nAnnounceCommentType = mDynamicNew.get(0)
						.getnTaskAnnounceCommentType();
				if (1 == nAnnounceCommentType) {
					// 清除通知栏消息
					NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					if (nm != null) {
						nm.cancel(0);
					}

					int nNum = mDynamicNew.get(0).getnDynamicNewsNum();
					if (nNum > 0) {
						mDynamicNew.get(0).setnDynamicNewsNum(nNum - 1);
					}

					if (mDynamicNew.get(0).getnDynamicNewsNum() > 0) {
						textview6.setText(mDynamicNew.get(0)
								.getnDynamicNewsNum() + "");
						textview6.setBackgroundDrawable(getResources()
								.getDrawable(R.drawable.red_oval));
					} else {
						textview6.setText("");
						textview6.setBackgroundDrawable(null);
					}
					mDynamicNew.get(0).setnTaskAnnounceCommentType(2);
				}
			} else {
				int nCount = mDynamicNew.size();
				for (int i = 0; i < nCount; i++) {
					String strId = mDynamicNew.get(i).getmstrId();
					int nTaskTypeTemp = mDynamicNew.get(i).getmnTaskType();
					boolean b1 = strTaskId.equals(strId);
					if (nTaskTypeTemp == nTaskType) {
						if (b1) {
							int nSelectType = mDynamicNew.get(i)
									.getnTaskSelectType();
							int nFinishType = mDynamicNew.get(i)
									.getnTaskFinishType();
							int nVerifiType = mDynamicNew.get(i)
									.getnTaskVerifiType();
							int nAnnounceCommentType = mDynamicNew.get(i)
									.getnTaskAnnounceCommentType();
							int nImpleCommentType = mDynamicNew.get(i)
									.getnTaskImplementCommentType();
							// 判断是否有更新
							if (2 == nSelectType || 2 == nFinishType
									|| 2 == nVerifiType
									|| 2 == nAnnounceCommentType
									|| 2 == nImpleCommentType) {

								// 清除通知栏消息
								NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
								if (nm != null) {
									nm.cancel(0);
								}

								int nNum = mDynamicNew.get(0)
										.getnDynamicNewsNum();
								if (nNum > 0) {
									mDynamicNew.get(0).setnDynamicNewsNum(
											nNum - 1);
								}

								if (mDynamicNew.get(0).getnDynamicNewsNum() > 0) {
									textview6.setText(mDynamicNew.get(0)
											.getnDynamicNewsNum() + "");
									textview6
											.setBackgroundDrawable(getResources()
													.getDrawable(
															R.drawable.red_oval));
								} else {
									textview6.setText("");
									textview6.setBackgroundDrawable(null);
								}

								if (2 == nSelectType) {
									mDynamicNew.get(i).setnTaskSelectType(3);
								}
								if (2 == nFinishType) {
									mDynamicNew.get(i).setnTaskFinishType(3);
								}
								if (2 == nVerifiType) {
									mDynamicNew.get(i).setnTaskVerifiType(3);
								}
								if (2 == nAnnounceCommentType) {
									mDynamicNew.get(i)
											.setnTaskAnnounceCommentType(3);
								}
								if (2 == nImpleCommentType) {
									mDynamicNew.get(i)
											.setnTaskImplementCommentType(3);
								}
							}
							break;

						}
					}
				}
			}
		}
	}

	// 如果nType为0表示不需要重置列表框数据,为1表示需要
	private void ShowNearHelpDataForClick(final int nType) {
		Runnable rundata = new Runnable() {
			public void run() {
				if (mHelpTaskDataDistance != null) {
					// 如果此时求助中没有数据,那么就加载
					if (mHelpTaskDataDistance.size() <= 0) {
						// 一次性更新mnVolume条数据
						mHelpTaskDataDistance = goodService.getHelpNearData(
								MyLocation.mLongitude, MyLocation.mLatitude,
								(double) mnMaxDistance, strDistrictName);
						if (mHelpTaskDataDistance != null) {
							int nSize = mHelpTaskDataDistance.size();
							if (nSize > 0) {
								int ndistance = 0;
								String strTime;
								ndistance = mHelpTaskDataDistance
										.get(nSize - 1).getnDistance();
								strTime = mHelpTaskDataDistance.get(nSize - 1)
										.getmTaskAnnounceTime();
								// 保存最大距离
								// mnMaxDistanceForHelpNear = ndistance;
								mnOldTimeForHelp = strTime;
							}
						}
					}
				} else {// 此时也更新数据
					mHelpTaskDataDistance = goodService.getHelpNearData(
							MyLocation.mLongitude, MyLocation.mLatitude,
							(double) mnMaxDistance, strDistrictName);
					if (mHelpTaskDataDistance != null) {
						int nSize = mHelpTaskDataDistance.size();
						if (nSize > 0) {
							int ndistance = 0;
							String strTime;
							ndistance = mHelpTaskDataDistance.get(nSize - 1)
									.getnDistance();
							strTime = mHelpTaskDataDistance.get(nSize - 1)
									.getmTaskAnnounceTime();
							// 保存最大距离
							// mnMaxDistanceForHelpNear = ndistance;
							mnOldTimeForHelp = strTime;
						}
					}
				}

				Message msg = myhandler.obtainMessage();
				Bundle b = new Bundle();// 存放数据
				b.putInt("nType", nType);
				msg.setData(b);
				msg.what = 5;
				myhandler.sendMessage(msg);
			}
		};
		// 启动获取评论数据线程
		// Thread thread = new Thread(rundata);
		// thread.start();
	}

	private void ShowNearShareDataForClick(final int nType) {
		Runnable rundata = new Runnable() {
			public void run() {
				if (mShareTaskDataDistance != null) {
					// 如果此时求助中没有数据,那么就加载
					if (mShareTaskDataDistance.size() <= 0) {
						// 一次性更新mnVolume条数据
						mShareTaskDataDistance = goodService.getShareNearData(
								MyLocation.mLongitude, MyLocation.mLatitude,
								(double) mnMaxDistance, strDistrictName);
						if (mShareTaskDataDistance != null) {
							int nSize = mShareTaskDataDistance.size();
							if (nSize > 0) {
								int nDistance = 0;
								String strTime;
								nDistance = mShareTaskDataDistance.get(
										nSize - 1).getnDistance();
								strTime = mShareTaskDataDistance.get(nSize - 1)
										.getmTaskAnnounceTime();
								// 保存最旧的任务id
								// mnMaxDistanceForShareNear = nDistance;
								mnOldTimeForShareNear = strTime;
							}
						}
					}
				} else {// 此时也更新数据
					mShareTaskDataDistance = goodService.getShareNearData(
							MyLocation.mLongitude, MyLocation.mLatitude,
							(double) mnMaxDistance, strDistrictName);
					if (mShareTaskDataDistance != null) {
						int nSize = mShareTaskDataDistance.size();
						if (nSize > 0) {
							int nDistance = 0;
							String strTime;
							nDistance = mShareTaskDataDistance.get(nSize - 1)
									.getnDistance();
							strTime = mShareTaskDataDistance.get(nSize - 1)
									.getmTaskAnnounceTime();
							// 保存最旧的任务id
							// mnMaxDistanceForShareNear = nDistance;
							mnOldTimeForShareNear = strTime;
						}
					}
				}
				Message msg = myhandler.obtainMessage();
				Bundle b = new Bundle();// 存放数据
				b.putInt("nType", nType);
				msg.setData(b);
				msg.what = 5;
				myhandler.sendMessage(msg);
			}
		};

		// 启动获取评论数据线程
		Thread thread = new Thread(rundata);
		thread.start();
	}

	// 如果nType为0表示不需要重置列表框数据,为1表示需要
	private void ShowSpaceHelpDataForClick(final int nType) {
		Runnable rundata = new Runnable() {
			public void run() {
				if (mHelpTaskData != null) {
					// 如果此时求助中没有数据,那么就加载
					if (mHelpTaskData.size() <= 0) {
						// 一次性更新mnVolume条数据
						mHelpTaskData = goodService.UpdateTaskDataForLimit(
								nMaxDataLine, 1, 0);
						if (mHelpTaskData != null) {
							int nSize = mHelpTaskData.size();
							if (nSize > 0) {
								int nIndex = 0;
								nIndex = Integer.parseInt(mHelpTaskData
										.get(nSize - 1).getmstrId().toString());
								// 保存最旧的任务id
								mnHelpTaskMaxOldIndex = nIndex;
								// 保存最新的任务id
								nIndex = Integer.parseInt(mHelpTaskData.get(0)
										.getmstrId().toString());
								mnHelpTaskMaxNewIndex = nIndex;
							}
						}
					}
				} else {// 此时也更新数据
					mHelpTaskData = goodService.UpdateTaskDataForLimit(
							nMaxDataLine, 1, 0);
					if (mHelpTaskData != null) {
						int nSize = mHelpTaskData.size();
						if (nSize > 0) {
							int nIndex = 0;
							nIndex = Integer.parseInt(mHelpTaskData
									.get(nSize - 1).getmstrId().toString());
							// 保存最旧的任务id
							mnHelpTaskMaxOldIndex = nIndex;
							// 保存最新的任务id
							nIndex = Integer.parseInt(mHelpTaskData.get(0)
									.getmstrId().toString());
							mnHelpTaskMaxNewIndex = nIndex;
						}
					}
				}

				Message msg = myhandler.obtainMessage();
				Bundle b = new Bundle();// 存放数据
				b.putInt("nType", nType);
				msg.setData(b);
				msg.what = 5;
				myhandler.sendMessage(msg);
			}
		};
		// 启动获取评论数据线程
		// Thread thread = new Thread(rundata);
		// thread.start();
	}

	private void ShowSpaceShareDataForClick(final int nType) {
		Runnable rundata = new Runnable() {
			public void run() {
				if (mShareTaskData != null) {
					// 如果此时求助中没有数据,那么就加载
					if (mShareTaskData.size() <= 0) {
						// 一次性更新mnVolume条数据
						mShareTaskData = goodService.UpdateTaskDataForLimitNew(
								nMaxDataLine, 2, mnNewTimeForShareAll);
						if (mShareTaskData != null) {
							int nSize = mShareTaskData.size();
							if (nSize > 0) {
								String strTime = mShareTaskData.get(nSize - 1)
										.getmTaskAnnounceTime().toString();
								// 保存最旧的任务id
								mnOldTimeForShareAll = strTime;
								// 保存最新的任务id
								strTime = mShareTaskData.get(0)
										.getmTaskAnnounceTime().toString();
								mnNewTimeForShareAll = strTime;
							}
						}
					}
				} else {// 此时也更新数据
					mShareTaskData = goodService.UpdateTaskDataForLimitNew(
							nMaxDataLine, 2, mnNewTimeForShareAll);
					if (mShareTaskData != null) {
						int nSize = mShareTaskData.size();
						if (nSize > 0) {
							String strTime = mShareTaskData.get(nSize - 1)
									.getmTaskAnnounceTime().toString();
							// 保存最旧的任务id
							mnOldTimeForShareAll = strTime;
							// 保存最新的任务id
							strTime = mShareTaskData.get(0)
									.getmTaskAnnounceTime().toString();
							mnNewTimeForShareAll = strTime;
						}
					}
				}
				Message msg = myhandler.obtainMessage();
				Bundle b = new Bundle();// 存放数据
				b.putInt("nType", nType);
				msg.setData(b);
				msg.what = 5;
				myhandler.sendMessage(msg);
			}
		};

		// 启动获取评论数据线程
		Thread thread = new Thread(rundata);
		thread.start();
	}

	public boolean isConnectInternet() {

		ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

		if (networkInfo != null) { // 这个判断一定要加上，要不然会出错
			return networkInfo.isAvailable();
		}
		return false;
	}

	class MyHandler extends Handler {
		// 子类必须重写此方法,接受数据
		@SuppressWarnings("deprecation")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Bundle b = msg.getData();
				int nCharmValue = b.getInt("nCharm");
				int nCreditValue = b.getInt("nCredit");
				mCurrentPersonName = msettings.getString("TruePersonName", "");
				mCurrentPersonNameTemp = msettings.getString("PersonName", "");
				if (mCurrentPersonName.equals("")) {
					my_textview1.setText("点击登录");
					my_textview1.setTextColor(Color.rgb(0, 135, 255));
					my_textview2.setText("");
					my_textview3.setText("");
					my_imageview2.setImageBitmap(null);
				} else {
					my_textview1.setTextColor(Color.rgb(0, 0, 0));
					my_textview1.setText(mCurrentPersonNameTemp);

					Resources res = getResources();
					Bitmap zanbmp = BitmapFactory.decodeResource(res,
							R.drawable.zan1);
					my_imageview2.setImageBitmap(zanbmp);

					String strBase64Image = msettings.getString("Base64Image",
							"");
					if (!strBase64Image.equals("")) {
						CommonUtils utils = new CommonUtils(
								RenPinMainActivity.this);
						Bitmap bmp = utils.base64ToBitmap(strBase64Image);
						my_imageview1.setImageBitmap(bmp);
					} else {
						res = getResources();
						Bitmap bmp = BitmapFactory.decodeResource(res,
								R.drawable.noperson);
						my_imageview1.setImageBitmap(bmp);
					}
					Editor editor = msettings.edit();
					editor.putInt("CreditValue", nCreditValue);
					editor.putInt("CharmValue", nCharmValue);
					editor.commit();
					my_textview2.setText("人品:" + nCreditValue);

					my_textview3.setText(nCharmValue + "");
				}
				break;
			case 1:
				if (mDynamicNew != null) {
					int nSize = mDynamicNew.size();
					if (nSize > 0) {
						if (mDynamicNew.get(0).getnDynamicNewsNum() > 0) {
							textview6.setText(mDynamicNew.get(0)
									.getnDynamicNewsNum() + "");
							textview6.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.red_oval));
						} else {
							textview6.setText("");
							textview6.setBackgroundDrawable(null);
						}
					} else {
						textview6.setText("");
						textview6.setBackgroundDrawable(null);
					}

					nonetworklayout.setVisibility(View.GONE);
					// 动态
					int nCount1 = mDynamicNew.size();

					mListData1 = getListData1(nCount1);
					if (1 == mIsShowingWindow) {
						if (null == madapter1) {
							madapter1 = new DynamicNewsListAdpater(
									RenPinMainActivity.this, mListData1,
									R.layout.dynamic_item, new String[] {
											"customericon", "customername",
											"tasktitle", "TaskAnnounceTime" },
									new int[] { R.id.dynamic_item_imageview1,
											R.id.dynamic_item_textview1,
											R.id.dynamic_item_textview2,
											R.id.dynamic_item_textview3 });
							mListView2.setAdapter(madapter1);
						} else {
							madapter1.mItemList = mListData1;
							madapter1.notifyDataSetChanged();
						}
					}
				}
				if (!isConnectInternet()) {
					nonetworklayout.setVisibility(View.VISIBLE);
					bNetWorkLinkStatus = false;
				} else {
					nonetworklayout.setVisibility(View.GONE);
					// 如果上一次测到是处在断网状态下,那么一旦有网那么默认去刷新数据
					if (!bNetWorkLinkStatus) {
						// 更新相关求助与分享中的数据
						GetHelpOrShareData();
						bIsFirstStart = true;
					}
					bNetWorkLinkStatus = true;
				}
				// 将刷新标志设为可以刷新
				mbIsFinished = true;
				// 新版本更新提示
				if (!bIsReminderUpdateApk && bDataFinishLoad) {
					if (mupdateinfo != null) {
						int nNewVersion = Integer.parseInt(mupdateinfo
								.getVersion());
						String strDescript = mupdateinfo.getDescription();
						// 如果有新版本,那么就提示更新最新版本
						if (localVersion < nNewVersion) {
							// 发现新版本，提示用户更新
							AlertDialog.Builder alert = new AlertDialog.Builder(
									RenPinMainActivity.this);
							alert.setTitle("有新版本可用")
									.setMessage(strDescript)
									.setPositiveButton(
											"现在升级",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													// 创建文件
													FileUtil.createFile(getResources()
															.getString(
																	R.string.app_name1));
													createNotification();
													createThread();
												}
											})
									.setNegativeButton(
											"以后再说",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													dialog.dismiss();
												}
											});
							alert.create().show();
							bIsReminderUpdateApk = true;
							versionupdateinfotextview.setText("有新版本");
						}
					}
				}
				if (mupdateinfo != null) {
					int nNewVersion = Integer
							.parseInt(mupdateinfo.getVersion());
					// 如果在更多种有版本更新提示的文字,同时版本已经更新,那么就将这个提示文字去掉
					if (!versionupdateinfotextview.getText().toString()
							.equals("")
							&& localVersion >= nNewVersion) {
						versionupdateinfotextview.setText("");
					}
				}
				break;
			case 2:// 向上滑动加载
				if (2 == mIsShowingWindow) {// 求助
					if (1 == mnDataType) {
						if (mHelpTaskDataDistance != null) {
							// 求助
							int nCount = mHelpTaskDataDistance.size();
							mListData = getListData(1, nCount);
							SetHelpAdapter(false);
						}
					} else if (2 == mnDataType) {
						if (mHelpTaskData != null) {
							// 求助
							int nCount = mHelpTaskData.size();
							mListData = getListData(1, nCount);
							SetHelpAdapter(false);
						}
					}

				} else if (3 == mIsShowingWindow) {// 分享
					if (1 == mnDataType) {
						if (mShareTaskDataDistance != null) {
							// 分享
							int nCount2 = mShareTaskDataDistance.size();
							mListData2 = getListData(2, nCount2);
							SetShareAdapter(false);

						}
					} else if (2 == mnDataType) {
						if (mShareTaskData != null) {
							// 分享
							int nCount2 = mShareTaskData.size();
							mListData2 = getListData(2, nCount2);
							SetShareAdapter(false);
						}
					}
				}
				mPullToRefreshView1.onFooterRefreshComplete();
				break;
			case 3:// 向下滑动刷新
				if (2 == mIsShowingWindow) {// 求助
					if (1 == mnDataType) {
						if (mHelpTaskDataDistance != null) {
							// 求助
							int nCount = mHelpTaskDataDistance.size();
							mListData = getListData(1, nCount);
							SetHelpAdapter(false);
						}
					} else if (2 == mnDataType) {
						if (mHelpTaskData != null) {
							// 求助
							int nCount = mHelpTaskData.size();
							mListData = getListData(1, nCount);
							SetHelpAdapter(false);
						}
					}
				} else if (3 == mIsShowingWindow) {// 分享
					if (1 == mnDataType) {
						if (mShareTaskDataDistance != null) {
							// 分享
							int nCount2 = mShareTaskDataDistance.size();
							mListData2 = getListData(2, nCount2);
							SetShareAdapter(false);
						}
					} else if (2 == mnDataType) {
						if (mShareTaskData != null) {
							// 分享
							int nCount2 = mShareTaskData.size();
							mListData2 = getListData(2, nCount2);
							SetShareAdapter(false);
						}
					}
				}
				mPullToRefreshView1.onHeaderRefreshComplete();
				break;
			case 4:// 程序刚启动时刷新
				if (1 == mnDataType) {
					if (mHelpTaskDataDistance != null) {
						// 求助
						int nCount = mHelpTaskDataDistance.size();
						mListData = getListData(1, nCount);
						if (2 == mIsShowingWindow) {
							SetHelpAdapter(true);
						} else {
							SetHelpAdapter(false);
						}
					}
					if (mShareTaskDataDistance != null) {
						// 分享
						int nCount2 = mShareTaskDataDistance.size();
						mListData2 = getListData(2, nCount2);
						if (3 == mIsShowingWindow) {
							SetShareAdapter(true);
						} else {
							SetShareAdapter(false);
						}
					}
				} else if (2 == mnDataType) {
					if (mHelpTaskData != null) {
						// 求助
						int nCount = mHelpTaskData.size();
						mListData = getListData(1, nCount);
						if (2 == mIsShowingWindow) {
							SetHelpAdapter(true);
						} else {
							SetHelpAdapter(false);
						}
					}
					if (mShareTaskData != null) {
						// 分享
						int nCount2 = mShareTaskData.size();
						mListData2 = getListData(2, nCount2);
						if (3 == mIsShowingWindow) {
							SetShareAdapter(true);
						} else {
							SetShareAdapter(false);
						}
					}
				}
				if (m_ProgressDialog != null) {
					m_ProgressDialog.dismiss();
					m_ProgressDialog = null;
				}
				bDataFinishLoad = true;
				break;
			case 5:// 窗口切换刷新
				Bundle WindowChangeMsg = msg.getData();
				int nType = WindowChangeMsg.getInt("nType");
				if (2 == mIsShowingWindow) {// 求助
					if (1 == mnDataType) {
						if (mHelpTaskDataDistance != null) {
							if (0 == nType) {// 如果不是从另外一个窗口切换到求助窗口
								// 求助
								int nCount = mHelpTaskDataDistance.size();
								mListData = getListData(1, nCount);
								SetHelpAdapter(false);
							} else {
								// 求助
								int nCount = mHelpTaskDataDistance.size();
								mListData = getListData(1, nCount);
								SetHelpAdapter(true);
							}
						} else {// 如果没有获取到数据,在窗口切换的时候也应该做相应的切换
							if (0 == nType) {// 如果不是从另外一个窗口切换到求助窗口
								// 求助
								SetHelpAdapter(false);
							} else {
								// 求助
								SetHelpAdapter(true);
							}
						}
					} else if (2 == mnDataType) {
						if (mHelpTaskData != null) {
							if (0 == nType) {// 如果不是从另外一个窗口切换到求助窗口
								// 求助
								int nCount = mHelpTaskData.size();
								mListData = getListData(1, nCount);
								SetHelpAdapter(false);
							} else {
								// 求助
								int nCount = mHelpTaskData.size();
								mListData = getListData(1, nCount);
								SetHelpAdapter(true);
							}
						} else {// 如果没有获取到数据,在窗口切换的时候也应该做相应的切换
							if (0 == nType) {// 如果不是从另外一个窗口切换到求助窗口
								// 求助
								SetHelpAdapter(false);
							} else {
								// 求助
								SetHelpAdapter(true);
							}
						}
					}
				} else if (3 == mIsShowingWindow) {// 分享
					if (1 == mnDataType) {
						if (mShareTaskDataDistance != null) {
							if (0 == nType) {// 如果不是从另外一个窗口切换到分享窗口
								// 分享
								int nCount2 = mShareTaskDataDistance.size();
								mListData2 = getListData(2, nCount2);
								SetShareAdapter(false);
							} else {
								if (mShareTaskDataDistance != null) {
									// 分享
									int nCount2 = mShareTaskDataDistance.size();
									mListData2 = getListData(2, nCount2);
									SetShareAdapter(true);
								}
							}
						} else {
							if (0 == nType) {// 如果不是从另外一个窗口切换到分享窗口
								// 分享
								SetShareAdapter(false);
							} else {
								// 分享
								SetShareAdapter(true);
							}
						}
					} else if (2 == mnDataType) {
						if (mShareTaskData != null) {
							if (0 == nType) {// 如果不是从另外一个窗口切换到分享窗口
								// 分享
								int nCount2 = mShareTaskData.size();
								mListData2 = getListData(2, nCount2);
								SetShareAdapter(false);
							} else {
								if (mShareTaskData != null) {
									// 分享
									int nCount2 = mShareTaskData.size();
									mListData2 = getListData(2, nCount2);
									SetShareAdapter(true);
								}
							}
						} else {
							if (0 == nType) {// 如果不是从另外一个窗口切换到分享窗口
								// 分享
								SetShareAdapter(false);
							} else {
								// 分享
								SetShareAdapter(true);
							}
						}
					}
				}
				break;
			case 6:
				Bundle b6 = msg.getData();
				int nRet = b6.getInt("nRet");
				if (4 == nRet) {
					Intent intent = new Intent(RenPinMainActivity.this,
							LoginActivity.class);
					startActivityForResult(intent, 20);
				} else {
					String strId = b6.getString("id");
					String strType = b6.getString("type");
					String strMsg1 = "";
					if (0 == nRet) {
						strMsg1 = "操作失败";
					} else if (1 == nRet) {
						strMsg1 = "操作成功";
						// 更新赞的数值
						UpdateTaskOrShareCharmValue(strId, strType);
					} else if (2 == nRet) {
						strMsg1 = "已经赞过";
					} else if (3 == nRet) {
						strMsg1 = "不能给自己赞哦";
					}
					CommonUtils.ShowToastCenter(getBaseContext(), strMsg1,
							Toast.LENGTH_LONG);
				}

				break;
			case 7:
				if (2 == mIsShowingWindow) {// 求助
					if (1 == mnDataType) {
						if (mHelpTaskDataDistance != null) {
							// 求助
							int nCount = mHelpTaskDataDistance.size();
							mListData = getListData(1, nCount);
							SetHelpAdapter(false);
						} else {
							// 求助
							SetHelpAdapter(false);
						}
					} else if (2 == mnDataType) {
						if (mHelpTaskData != null) {
							// 求助
							int nCount = mHelpTaskData.size();
							mListData = getListData(1, nCount);
							SetHelpAdapter(false);
						} else {
							// 求助
							SetHelpAdapter(false);
						}
					}
				} else if (3 == mIsShowingWindow) {// 分享
					if (1 == mnDataType) {
						if (mShareTaskDataDistance != null) {
							// 分享
							int nCount2 = mShareTaskDataDistance.size();
							mListData2 = getListData(2, nCount2);
							SetShareAdapter(false);
						} else {
							// 分享
							SetShareAdapter(false);
						}
					} else if (2 == mnDataType) {
						if (mShareTaskData != null) {
							// 分享
							int nCount2 = mShareTaskData.size();
							mListData2 = getListData(2, nCount2);
							SetShareAdapter(false);

						} else {
							// 分享
							SetShareAdapter(false);
						}
					}
				}
				break;
			case 8:
				Bundle b8 = msg.getData();
				int nRet8 = b8.getInt("nRet");
				String strTaskid = b8.getString("taskid", "");
				String strType = b8.getString("tasktype", "");
				nType = Integer.parseInt(strType);
				if (nRet8 > 0) {
					// 删除动态容器中的数据
					DeleteDynamicData(strTaskid, nType);
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"删除成功", Toast.LENGTH_LONG);
				} else {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"删除失败", Toast.LENGTH_LONG);
				}
				break;
			}
		}
	}

	private void DeleteDynamicData(String strTaskId, int nType) {
		if (mDynamicNew != null) {
			int nSize = mDynamicNew.size();
			for (int i = 0; i < nSize; i++) {
				if (mDynamicNew.get(i).getmnTaskType() == nType
						&& mDynamicNew.get(i).getmstrId().equals(strTaskId)) {
					// 删除该项
					mDynamicNew.remove(i);
					break;
				}
			}
		}
	}

	// 更新赞值
	private void UpdateTaskOrShareCharmValue(String strId, String strType) {
		int nSize = 0;
		if (1 == mnDataType) {
			if (strType.equals("1") && mHelpTaskDataDistance != null) {
				nSize = mHelpTaskDataDistance.size();
				for (int i = 0; i < nSize; i++) {
					if (mHelpTaskDataDistance.get(i).getmstrId().equals(strId)) {
						int nCharmValue = mHelpTaskDataDistance.get(i)
								.getnTaskCharmValue();
						mHelpTaskDataDistance.get(i).setnTaskCharmValue(
								nCharmValue + 1);
						// 求助
						int nCount = mHelpTaskDataDistance.size();
						mListData = getListData(1, nCount);
						SetHelpAdapter(false);
						break;
					}
				}
			} else if (strType.equals("2") && mShareTaskDataDistance != null) {
				nSize = mShareTaskDataDistance.size();
				for (int i = 0; i < nSize; i++) {
					if (mShareTaskDataDistance.get(i).getmstrId().equals(strId)) {
						int nCharmValue = mShareTaskDataDistance.get(i)
								.getnTaskCharmValue();
						mShareTaskDataDistance.get(i).setnTaskCharmValue(
								nCharmValue + 1);
						// 分享
						int nCount2 = mShareTaskDataDistance.size();
						mListData2 = getListData(2, nCount2);
						SetShareAdapter(false);
						break;
					}
				}
			}
		} else if (2 == mnDataType) {
			if (strType.equals("1") && mHelpTaskData != null) {
				nSize = mHelpTaskData.size();
				for (int i = 0; i < nSize; i++) {
					if (mHelpTaskData.get(i).getmstrId().equals(strId)) {
						int nCharmValue = mHelpTaskData.get(i)
								.getnTaskCharmValue();
						mHelpTaskData.get(i)
								.setnTaskCharmValue(nCharmValue + 1);
						// 求助
						int nCount = mHelpTaskData.size();
						mListData = getListData(1, nCount);
						SetHelpAdapter(false);
						break;
					}
				}
			} else if (strType.equals("2") && mShareTaskData != null) {
				nSize = mShareTaskData.size();
				for (int i = 0; i < nSize; i++) {
					if (mShareTaskData.get(i).getmstrId().equals(strId)) {
						int nCharmValue = mShareTaskData.get(i)
								.getnTaskCharmValue();
						mShareTaskData.get(i).setnTaskCharmValue(
								nCharmValue + 1);
						// 分享
						int nCount2 = mShareTaskData.size();
						mListData2 = getListData(2, nCount2);
						SetShareAdapter(false);
						break;
					}
				}
			}
		}
	}

	// 更新浏览量
	public void UpdateTaskOrShareInfo(String strId, String strType, int nZan,
			int nTalk, int nLook) {
		int nSize = 0;
		if (1 == mnDataType) {
			if (strType.equals("1") && mHelpTaskDataDistance != null) {
				nSize = mHelpTaskDataDistance.size();
				for (int i = 0; i < nSize; i++) {
					if (mHelpTaskDataDistance.get(i).getmstrId().equals(strId)) {
						mHelpTaskDataDistance.get(i).setnBrowseTimes(nLook);
						mHelpTaskDataDistance.get(i).setnTaskCharmValue(nZan);
						mHelpTaskDataDistance.get(i)
								.setnCommentRecordNum(nTalk);
						// 求助
						int nCount = mHelpTaskDataDistance.size();
						mListData = getListData(1, nCount);
						SetHelpAdapter(false);
						break;
					}
				}
			} else if (strType.equals("2") && mShareTaskDataDistance != null) {
				nSize = mShareTaskDataDistance.size();
				for (int i = 0; i < nSize; i++) {
					if (mShareTaskDataDistance.get(i).getmstrId().equals(strId)) {
						mShareTaskDataDistance.get(i).setnBrowseTimes(nLook);
						mShareTaskDataDistance.get(i).setnTaskCharmValue(nZan);
						mShareTaskDataDistance.get(i).setnCommentRecordNum(
								nTalk);
						// 求助
						int nCount = mShareTaskDataDistance.size();
						mListData2 = getListData(2, nCount);
						SetShareAdapter(false);
						break;
					}
				}
			}
		} else if (2 == mnDataType) {
			if (strType.equals("1") && mHelpTaskData != null) {
				nSize = mHelpTaskData.size();
				for (int i = 0; i < nSize; i++) {
					if (mHelpTaskData.get(i).getmstrId().equals(strId)) {
						mHelpTaskData.get(i).setnBrowseTimes(nLook);
						mHelpTaskData.get(i).setnTaskCharmValue(nZan);
						mHelpTaskData.get(i).setnCommentRecordNum(nTalk);
						// 求助
						int nCount = mHelpTaskData.size();
						mListData = getListData(1, nCount);
						SetHelpAdapter(false);
						break;
					}
				}
			} else if (strType.equals("2") && mShareTaskData != null) {
				nSize = mShareTaskData.size();
				for (int i = 0; i < nSize; i++) {
					if (mShareTaskData.get(i).getmstrId().equals(strId)) {
						mShareTaskData.get(i).setnBrowseTimes(nLook);
						mShareTaskData.get(i).setnTaskCharmValue(nZan);
						mShareTaskData.get(i).setnCommentRecordNum(nTalk);
						// 求助
						int nCount = mShareTaskData.size();
						mListData2 = getListData(2, nCount);
						SetShareAdapter(false);
						break;
					}
				}
			}
		}
	}

	private List<HashMap<String, Object>> getListData(int nType, int nCount) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = null;

		for (int i = 0; i < nCount; i++) {
			String strRegionName = "";
			String strCustomerIcon = "";
			Bitmap bitmap = null;
			String strCustomerName = "";
			String strTaskTile = "";
			String strTaskId = "";
			double dLongitude = 0;
			double Latidude = 0;
			double distance = 0;
			String strTaskAnnounceTime = "";
			String strTaskTimeLimit = "";
			String strTaskDetail = "";
			String strRunSeconds = "";
			String strTaskImplementName = "";
			String strCustomerNameNick = "";
			String strTaskImplementNick = "";
			int nTaskType = 0;
			int nTaskVerifiType = 0;
			int nTaskSelectType = 0;
			int nCharmValue = 0;// 赞值
			int nCommentNum = 0;// 评论条数
			int nBrowseNum = 0;// 浏览条数

			if (1 == nType) {
				if (1 == mnDataType) {
					nTaskType = mHelpTaskDataDistance.get(i).getmnTaskType();
					strRegionName = mHelpTaskDataDistance.get(i)
							.getmstrTaskRegion();
					strCustomerIcon = mHelpTaskDataDistance.get(i)
							.getmTaskAskPersonIcon();
					bitmap = mUtils.base64ToBitmap(strCustomerIcon);
					strCustomerName = mHelpTaskDataDistance.get(i)
							.getstrTaskPersonTrueName();
					strTaskTile = mHelpTaskDataDistance.get(i).getmTaskTitle();
					strTaskId = mHelpTaskDataDistance.get(i).getmstrId();
					dLongitude = mHelpTaskDataDistance.get(i).getmdLongitude();
					Latidude = mHelpTaskDataDistance.get(i).getmdLatidude();
					strTaskAnnounceTime = mHelpTaskDataDistance.get(i)
							.getmTaskAnnounceTime();
					strTaskTimeLimit = mHelpTaskDataDistance.get(i)
							.getmTimeLimit();
					strTaskDetail = mHelpTaskDataDistance.get(i)
							.getmTaskDetail();
					strRunSeconds = mHelpTaskDataDistance.get(i)
							.getmRunSeconds();
					strTaskImplementName = mHelpTaskDataDistance.get(i)
							.getstrTaskImplementTrueName();

					strCustomerNameNick = mHelpTaskDataDistance.get(i)
							.getmPersonName();
					strTaskImplementNick = mHelpTaskDataDistance.get(i)
							.getmTaskImplementName();
					nTaskVerifiType = mHelpTaskDataDistance.get(i)
							.getnTaskVerifiType();
					nTaskSelectType = mHelpTaskDataDistance.get(i)
							.getnTaskSelectType();
					nCharmValue = mHelpTaskDataDistance.get(i)
							.getnTaskCharmValue();
					nCommentNum = mHelpTaskDataDistance.get(i)
							.getnCommentRecordNum();
					nBrowseNum = mHelpTaskDataDistance.get(i).getnBrowseTimes();
				} else if (2 == mnDataType) {
					nTaskType = mHelpTaskData.get(i).getmnTaskType();
					strRegionName = mHelpTaskData.get(i).getmstrTaskRegion();
					strCustomerIcon = mHelpTaskData.get(i)
							.getmTaskAskPersonIcon();
					bitmap = mUtils.base64ToBitmap(strCustomerIcon);
					strCustomerName = mHelpTaskData.get(i)
							.getstrTaskPersonTrueName();
					strTaskTile = mHelpTaskData.get(i).getmTaskTitle();
					strTaskId = mHelpTaskData.get(i).getmstrId();
					dLongitude = mHelpTaskData.get(i).getmdLongitude();
					Latidude = mHelpTaskData.get(i).getmdLatidude();
					strTaskAnnounceTime = mHelpTaskData.get(i)
							.getmTaskAnnounceTime();
					strTaskTimeLimit = mHelpTaskData.get(i).getmTimeLimit();
					strTaskDetail = mHelpTaskData.get(i).getmTaskDetail();
					strRunSeconds = mHelpTaskData.get(i).getmRunSeconds();
					strTaskImplementName = mHelpTaskData.get(i)
							.getstrTaskImplementTrueName();
					strCustomerNameNick = mHelpTaskData.get(i).getmPersonName();
					strTaskImplementNick = mHelpTaskData.get(i)
							.getmTaskImplementName();
					nTaskVerifiType = mHelpTaskData.get(i).getnTaskVerifiType();
					nTaskSelectType = mHelpTaskData.get(i).getnTaskSelectType();
					nCharmValue = mHelpTaskData.get(i).getnTaskCharmValue();
					nCommentNum = mHelpTaskData.get(i).getnCommentRecordNum();
					nBrowseNum = mHelpTaskData.get(i).getnBrowseTimes();
				}
			} else if (2 == nType) {
				if (1 == mnDataType) {
					nTaskType = mShareTaskDataDistance.get(i).getmnTaskType();
					strRegionName = mShareTaskDataDistance.get(i)
							.getmstrTaskRegion();
					strCustomerIcon = mShareTaskDataDistance.get(i)
							.getmTaskAskPersonIcon();
					bitmap = mUtils.base64ToBitmap(strCustomerIcon);
					strCustomerName = mShareTaskDataDistance.get(i)
							.getstrTaskPersonTrueName();
					strTaskTile = mShareTaskDataDistance.get(i).getmTaskTitle();
					strTaskId = mShareTaskDataDistance.get(i).getmstrId();
					dLongitude = mShareTaskDataDistance.get(i).getmdLongitude();
					Latidude = mShareTaskDataDistance.get(i).getmdLatidude();
					strTaskAnnounceTime = mShareTaskDataDistance.get(i)
							.getmTaskAnnounceTime();
					strTaskTimeLimit = mShareTaskDataDistance.get(i)
							.getmTimeLimit();
					strTaskDetail = mShareTaskDataDistance.get(i)
							.getmTaskDetail();
					strRunSeconds = mShareTaskDataDistance.get(i)
							.getmRunSeconds();
					strTaskImplementName = mShareTaskDataDistance.get(i)
							.getstrTaskImplementTrueName();
					strCustomerNameNick = mShareTaskDataDistance.get(i)
							.getmPersonName();
					strTaskImplementNick = mShareTaskDataDistance.get(i)
							.getmTaskImplementName();
					nTaskVerifiType = mShareTaskDataDistance.get(i)
							.getnTaskVerifiType();
					nTaskSelectType = mShareTaskDataDistance.get(i)
							.getnTaskSelectType();
					nCharmValue = mShareTaskDataDistance.get(i)
							.getnTaskCharmValue();
					nCommentNum = mShareTaskDataDistance.get(i)
							.getnCommentRecordNum();
					nBrowseNum = mShareTaskDataDistance.get(i)
							.getnBrowseTimes();
				} else if (2 == mnDataType) {
					nTaskType = mShareTaskData.get(i).getmnTaskType();
					strRegionName = mShareTaskData.get(i).getmstrTaskRegion();
					strCustomerIcon = mShareTaskData.get(i)
							.getmTaskAskPersonIcon();
					bitmap = mUtils.base64ToBitmap(strCustomerIcon);
					strCustomerName = mShareTaskData.get(i)
							.getstrTaskPersonTrueName();
					strTaskTile = mShareTaskData.get(i).getmTaskTitle();
					strTaskId = mShareTaskData.get(i).getmstrId();
					dLongitude = mShareTaskData.get(i).getmdLongitude();
					Latidude = mShareTaskData.get(i).getmdLatidude();
					strTaskAnnounceTime = mShareTaskData.get(i)
							.getmTaskAnnounceTime();
					strTaskTimeLimit = mShareTaskData.get(i).getmTimeLimit();
					strTaskDetail = mShareTaskData.get(i).getmTaskDetail();
					strRunSeconds = mShareTaskData.get(i).getmRunSeconds();
					strTaskImplementName = mShareTaskData.get(i)
							.getstrTaskImplementTrueName();
					strCustomerNameNick = mShareTaskData.get(i)
							.getmPersonName();
					strTaskImplementNick = mShareTaskData.get(i)
							.getmTaskImplementName();
					nTaskVerifiType = mShareTaskData.get(i)
							.getnTaskVerifiType();
					nTaskSelectType = mShareTaskData.get(i)
							.getnTaskSelectType();
					nCharmValue = mShareTaskData.get(i).getnTaskCharmValue();
					nCommentNum = mShareTaskData.get(i).getnCommentRecordNum();
					nBrowseNum = mShareTaskData.get(i).getnBrowseTimes();
				}
			}

			String strDistance = ""; // 计算人离任务发布区域的距离
			if (MyLocation.mLatitude != -1) {
				distance = getDistance(dLongitude, Latidude,
						MyLocation.mLongitude, MyLocation.mLatitude); // 如果不足1km
				if (distance < 1) { // 如果不足100m
					if (distance * 1000 < 100) {
						strDistance = "< 100 m";
					} else {
						strDistance = (int) (distance * 1000) + "m";
					}

				} else {
					strDistance = (int) distance + "km";
				}
			}
			// 将获取到的数据存储到容器中
			map = new HashMap<String, Object>();
			map.put("regionname", strRegionName);
			map.put("customericon", bitmap);
			map.put("customername", strCustomerName);
			map.put("tasktitle", strTaskTile);
			map.put("taskid", strTaskId);
			map.put("distance", strDistance);
			map.put("TaskAnnounceTime", strTaskAnnounceTime);
			map.put("TaskTimeLimit", strTaskTimeLimit);
			map.put("TaskDetail", strTaskDetail);
			map.put("taskruntime", strRunSeconds);
			map.put("taskimplementname", strTaskImplementName);
			map.put("customernick", strCustomerNameNick);
			map.put("taskimplementnick", strTaskImplementNick);
			map.put("tasktype", nTaskType + "");
			map.put("nTaskVerifiType", nTaskVerifiType + "");
			map.put("nTaskSelectType", nTaskSelectType + "");
			map.put("nCharmValue", nCharmValue + "");
			map.put("nCommentNum", nCommentNum + "");
			map.put("nBrowseNum", nBrowseNum + "");
			list.add(map);
		}
		return list;
	}

	private List<HashMap<String, Object>> getListData1(int nCount) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = null;

		for (int i = 0; i < nCount; i++) {
			String strRegionName;
			String strCustomerIcon;
			Bitmap bitmap;
			String strCustomerName;
			String strTaskTile;
			String strTaskId;
			double dLongitude = 0;
			double Latidude = 0;
			double distance = 0;
			String strTaskAnnounceTime;
			String strTaskTimeLimit;
			String strTaskDetail;
			String strRunSeconds;
			String strTaskImplementName;
			String strCustomerNameTemp;
			String strImplementNameTemp;
			int nTimeStatus = 1;
			int nImpleStatus = 1;
			int nTaskType = 0;

			int nTaskSelectType;
			int nTaskFinishType;
			int nTaskVerifiType;
			int nTaskAnnounceCommentType;
			int nTaskImplementCommentType;
			// 发布者对执行者的评论
			String strAnnounceComment;
			// 发布者给执行者的图片
			String strAnnounceBase64Image;
			// 执行者对发布者的评论
			String strImpleComment;
			// 执行者给发布者的图片
			String strImpleBase64Image;
			// 赞值
			int nCharmValue = 0;
			// 评论数
			int nCommentNum = 0;
			// 浏览数
			int nBrowseNum = 0;
			// 发布地址
			String strAddressRegion = "";

			strTaskId = mDynamicNew.get(i).getmstrId();
			// 判断这条发布信息是否与当前用户有关系
			strRegionName = mDynamicNew.get(i).getmstrTaskRegion();
			strCustomerIcon = mDynamicNew.get(i).getmTaskAskPersonIcon();
			bitmap = mUtils.base64ToBitmap(strCustomerIcon);
			strCustomerName = mDynamicNew.get(i).getstrTaskPersonTrueName();
			strTaskTile = mDynamicNew.get(i).getmTaskTitle();
			dLongitude = mDynamicNew.get(i).getmdLongitude();
			Latidude = mDynamicNew.get(i).getmdLatidude();
			strTaskAnnounceTime = mDynamicNew.get(i).getmTaskAnnounceTime();
			strTaskTimeLimit = mDynamicNew.get(i).getmTimeLimit();
			strTaskDetail = mDynamicNew.get(i).getmTaskDetail();
			strRunSeconds = mDynamicNew.get(i).getmRunSeconds();
			strTaskImplementName = mDynamicNew.get(i)
					.getstrTaskImplementTrueName();
			strCustomerNameTemp = mDynamicNew.get(i).getmPersonName();
			strImplementNameTemp = mDynamicNew.get(i).getmTaskImplementName();
			nTimeStatus = mDynamicNew.get(i).getmnValiableStatus();
			nImpleStatus = mDynamicNew.get(i).getmnImplementStatus();
			nTaskType = mDynamicNew.get(i).getmnTaskType();
			nTaskSelectType = mDynamicNew.get(i).getnTaskSelectType();
			nTaskFinishType = mDynamicNew.get(i).getnTaskFinishType();
			nTaskVerifiType = mDynamicNew.get(i).getnTaskVerifiType();
			nTaskAnnounceCommentType = mDynamicNew.get(i)
					.getnTaskAnnounceCommentType();
			nTaskImplementCommentType = mDynamicNew.get(i)
					.getnTaskImplementCommentType();
			strAnnounceComment = mDynamicNew.get(i)
					.getstrTaskAccountCommentContent();
			strAnnounceBase64Image = mDynamicNew.get(i)
					.getstrTaskAccountImage();
			strImpleComment = mDynamicNew.get(i)
					.getstrTaskImplementCommentContent();
			strImpleBase64Image = mDynamicNew.get(i).getstrTaskImplementImage();
			nCharmValue = mDynamicNew.get(i).getnTaskCharmValue();
			nCommentNum = mDynamicNew.get(i).getnCommentRecordNum();
			nBrowseNum = mDynamicNew.get(i).getnBrowseTimes();
			strAddressRegion = mDynamicNew.get(i).getmstrTaskRegion();

			String strDistance = "";
			// 计算人离任务发布区域的距离
			if (MyLocation.mLatitude != -1) {
				// 如果不足1km
				if (distance <= 1) {
					// 如果不足100m
					if (distance * 1000 < 100) {
						strDistance = "< 100 m";
					} else {
						strDistance = (int) (distance * 1000) + "m";
					}

				} else {
					strDistance = (int) distance + "km";
				}
			}
			// 将获取到的数据存储到容器中
			map = new HashMap<String, Object>();
			map.put("regionname", strRegionName);
			map.put("customericon", bitmap);
			map.put("customername", strCustomerName);
			map.put("tasktitle", strTaskTile);
			map.put("taskid", strTaskId);
			map.put("distance", strDistance);
			map.put("TaskAnnounceTime", strTaskAnnounceTime);
			map.put("TaskTimeLimit", strTaskTimeLimit);
			map.put("TaskDetail", strTaskDetail);
			map.put("taskruntime", strRunSeconds);
			map.put("taskimplementname", strTaskImplementName);
			map.put("customernametemp", strCustomerNameTemp);
			map.put("taskimplementnametemp", strImplementNameTemp);
			map.put("tasktimestatus", nTimeStatus + "");
			map.put("taskimplestatue", nImpleStatus + "");
			map.put("tasktype", nTaskType + "");
			map.put("nTaskSelectType", nTaskSelectType + "");
			map.put("nTaskFinishType", nTaskFinishType + "");
			map.put("nTaskVerifiType", nTaskVerifiType + "");
			map.put("nTaskAnnounceCommentType", nTaskAnnounceCommentType + "");
			map.put("nTaskImplementCommentType", nTaskImplementCommentType + "");
			map.put("strAnnounceComment", strAnnounceComment);
			map.put("strAnnounceBase64Image", strAnnounceBase64Image);
			map.put("strImpleComment", strImpleComment);
			map.put("strImpleBase64Image", strImpleBase64Image);
			map.put("nCharmValue", nCharmValue + "");
			map.put("nCommentNum", nCommentNum + "");
			map.put("nBrowseNum", nBrowseNum + "");
			map.put("strAddressRegion", strAddressRegion);
			list.add(map);
		}
		return list;
	}

	class TaskInfoListAdpater extends SimpleAdapter {

		private LayoutInflater mInflater;
		Context context;
		int count = 0;
		private List<HashMap<String, Object>> mItemList;

		@SuppressWarnings("unchecked")
		public TaskInfoListAdpater(Context context,
				List<? extends HashMap<String, Object>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.context = context;
			mItemList = (List<HashMap<String, Object>>) data;
			if (data == null) {
				count = 0;
			} else {
				count = data.size();
			}
		}

		public int getCount() {
			return mItemList.size();
		}

		public Object getItem(int pos) {

			return mItemList.get(pos);
		}

		public long getItemId(int pos) {
			return pos;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) getItem(position);

			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.help_eachother, null);
			}
			// 发布区域名称
			TextView regionname = (TextView) convertView
					.findViewById(R.id.help_eachother_textview1);
			// 发布区域距离
			TextView regiondistance = (TextView) convertView
					.findViewById(R.id.help_eachother_textview2new);
			// 发布图片
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.help_eachother_imageview1);
			// 发布图片
			// ImageView imageView1 = (ImageView) convertView
			// .findViewById(R.id.help_eachother_imageview3);
			// 发布人名称
			TextView customername = (TextView) convertView
					.findViewById(R.id.help_eachother_textview3);

			// 发布标题内容
			TextView detailtext = (TextView) convertView
					.findViewById(R.id.help_eachother_textview4);
			// 任务状态信息
			ImageView imageviewstatus = (ImageView) convertView
					.findViewById(R.id.help_eachother_imageview2);
			// 发布时间
			TextView accounttimetextview = (TextView) convertView
					.findViewById(R.id.help_eachother_timetextview);

			// 赞值
			LinearLayout praiselayout = (LinearLayout) convertView
					.findViewById(R.id.help_eachother_zanlayout);
			// 赞值图标
			final ImageView zanimageview = (ImageView) convertView
					.findViewById(R.id.help_eachother_zanimageview);

			TextView praisetextview = (TextView) convertView
					.findViewById(R.id.help_eachother_zantextview);
			// 评论条数
			TextView commenttextview = (TextView) convertView
					.findViewById(R.id.help_eachother_commenttextview);
			// 浏览次数
			TextView browsetextview = (TextView) convertView
					.findViewById(R.id.help_eachother_looktextview);

			/*
			 * Bitmap image = (Bitmap) map.get("customericon"); if (image !=
			 * null) { imageView.setImageBitmap(image); } else { Resources res =
			 * getResources(); Bitmap bmp = BitmapFactory.decodeResource(res,
			 * R.drawable.noperson); imageView.setImageBitmap(bmp); }
			 */
			
			regionname.setText(map.get("regionname").toString());
			regiondistance.setText(map.get("distance").toString());
			customername.setText(" " + map.get("customernick").toString()
					+ ": ");
			detailtext.setText(map.get("tasktitle").toString());
			accounttimetextview.setText(map.get("TaskAnnounceTime").toString());

			// 任务发布人名称
			final String strTaskPersonName = map.get("customername").toString();
			final String strId = map.get("taskid").toString();
			final String strType = map.get("tasktype").toString();
			
			Resources res = getResources();
			//判断是否点过赞
			if(StoreZanMap.containsKey(strId)){
				// 判断该任务的状态
				Bitmap zanbmp = BitmapFactory.decodeResource(res, R.drawable.zan1);
				zanimageview.setImageBitmap(zanbmp);	
			}else{
				Bitmap zanbmp = BitmapFactory.decodeResource(res, R.drawable.zan01);
				zanimageview.setImageBitmap(zanbmp);
			}

			// 生成图片文件名称(任务号_任务类型_图片索引)
			String strFileName = strId + "_" + strType + "_" + "1";
			new ImageDownloadTask().execute(strFileName, imageView, strId,
					strType);

			// 添加赞点击响应
			praiselayout.setOnClickListener(new OnClickListener() {
				Runnable zanrun = new Runnable() {
					public void run() {
						int nRet = 0;
						// 先获取当前用户名称
						String strCurName = msettings.getString(
								"TruePersonName", "");
						if (strCurName.equals("")) {
							nRet = 4;// 表示没有登录
						} else {
							// 避免自己给自己赞
							if (!strTaskPersonName.equals(strCurName)) {

								nRet = goodService.PraiseToTaskOrShare(strId,
										strCurName, strTaskPersonName, strType);
								if(1 == nRet){
									//判断容器是否超过了最大的容量,如果超过了就清空
									if(StoreZanMap.size() >= MaxStoreMap){
										StoreZanMap.clear();
									}
									StoreZanMap.put(strId, 1);
								}
							} else {
								nRet = 3;// 表示不能给自己赞
							}
						}
						Message msg = myhandler.obtainMessage();
						Bundle b = new Bundle();// 存放数据
						b.putInt("nRet", nRet);
						b.putString("id", strId);
						b.putString("type", strType);
						msg.setData(b);
						msg.what = 6;
						myhandler.sendMessage(msg);
					}
				};

				@Override
				public void onClick(View v) {
					 Thread thre = new Thread(zanrun);
					 thre.start();
				}
			});

			int nTaskTimeLimit = Integer.parseInt(map.get("TaskTimeLimit")
					.toString());
			int nTaskRunTime = Integer.parseInt(map.get("taskruntime")
					.toString());
			int nTaskVerifiType = Integer.parseInt(map.get("nTaskVerifiType")
					.toString());
			int nTaskSelectType = Integer.parseInt(map.get("nTaskSelectType")
					.toString());
			String strCharmValue = map.get("nCharmValue").toString();
			String strCommentNum = map.get("nCommentNum").toString();
			String strBrowseNum = map.get("nBrowseNum").toString();

			praisetextview.setText(strCharmValue);
			commenttextview.setText(strCommentNum);
			browsetextview.setText(strBrowseNum);
			// 如果完成了
			if (2 == nTaskVerifiType || 3 == nTaskVerifiType) {
				Bitmap bmp = BitmapFactory.decodeResource(res,
						R.drawable.sign_finish);
				imageviewstatus.setImageBitmap(bmp);
			} else if (2 == nTaskSelectType || 3 == nTaskSelectType) {// 任务拿下
				Bitmap bmp = BitmapFactory.decodeResource(res,
						R.drawable.sign_ondoing);
				imageviewstatus.setImageBitmap(bmp);
			} else if (nTaskTimeLimit - nTaskRunTime <= 0) {// 过期
				Bitmap bmp = BitmapFactory.decodeResource(res,
						R.drawable.sign_overdue);
				imageviewstatus.setImageBitmap(bmp);
			} else {
				imageviewstatus.setImageBitmap(null);
			}
			return convertView;
		}

	}

	class DynamicNewsListAdpater extends SimpleAdapter {

		private LayoutInflater mInflater;
		Context context;
		int count = 0;
		private List<HashMap<String, Object>> mItemList;

		@SuppressWarnings("unchecked")
		public DynamicNewsListAdpater(Context context,
				List<? extends HashMap<String, Object>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.context = context;
			mItemList = (List<HashMap<String, Object>>) data;
			if (data == null) {
				count = 0;
			} else {
				count = data.size();
			}
		}

		public int getCount() {
			return mItemList.size();
		}

		public Object getItem(int pos) {

			return mItemList.get(pos);
		}

		public long getItemId(int pos) {
			return pos;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) getItem(position);

			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.dynamic_item, null);
			}
			// 发布人头像
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.dynamic_item_imageview1);

			// 状态图标
			ImageView imageView1 = (ImageView) convertView
					.findViewById(R.id.dynamic_item_imageview2);

			// 发布人名称
			TextView customername = (TextView) convertView
					.findViewById(R.id.dynamic_item_textview1);

			// 发布任务标题
			TextView detailtext = (TextView) convertView
					.findViewById(R.id.dynamic_item_textview2);

			// 任务发布时间
			TextView tasktimetext = (TextView) convertView
					.findViewById(R.id.dynamic_item_textview3);

			// 状态信息
			TextView statustextview = (TextView) convertView
					.findViewById(R.id.dynamic_item_textview4);
			// 任务类型
			TextView strtypetextview = (TextView) convertView
					.findViewById(R.id.dynamic_item_textview5);

			// 如果为系统消息项
			if (map.get("taskid").toString().equals("")) {
				Resources res = getResources();
				Bitmap bmp = BitmapFactory.decodeResource(res,
						R.drawable.zan512);
				imageView.setImageBitmap(bmp);

				if (1 == Integer.parseInt(map.get("nTaskAnnounceCommentType")
						.toString())) {
					res = getResources();
					bmp = BitmapFactory
							.decodeResource(res, R.drawable.red_oval);
					imageView1.setImageBitmap(bmp);
				} else {
					imageView1.setImageBitmap(null);
				}

				customername.setText("系统消息");
				detailtext.setText("今天分享达人");
				strtypetextview.setText("");
				statustextview.setText("");
			} else {// 为其他项
				Bitmap image = (Bitmap) map.get("customericon");
				if (image != null) {
					imageView.setImageBitmap(image);
				} else {
					Resources res = getResources();
					Bitmap bmp = BitmapFactory.decodeResource(res,
							R.drawable.noperson);
					imageView.setImageBitmap(bmp);
				}

				mCurrentPersonName = msettings.getString("TruePersonName", "");
				// 判断是否有更新
				if (IsUpdatedNew(Integer.parseInt(map.get("tasktype")
						.toString()), map.get("taskid").toString())) {
					Resources res = getResources();
					Bitmap bmp = BitmapFactory.decodeResource(res,
							R.drawable.red_oval);
					imageView1.setImageBitmap(bmp);
					statustextview.setText(strStatusInfo);
				} else {
					statustextview.setText("");
					imageView1.setImageBitmap(null);
				}

				int nTimeLimit = Integer.parseInt(map.get("TaskTimeLimit")
						.toString());
				int nRunTime = Integer.parseInt(map.get("taskruntime")
						.toString());
				String strTaskType = map.get("tasktype").toString();
				int nTaskType = Integer.parseInt(strTaskType);
				// 判断任务是否过期
				if (!IsShowTimeOut(nTaskType, map.get("taskid").toString(),
						mCurrentPersonName, map.get("taskimplementname")
								.toString())
						&& nTimeLimit - nRunTime <= 0) {
					strTaskStatus = "已过期";
					if (2 == Integer.parseInt(map.get("tasktype").toString())) {
						strTaskStatus = "已过期";
					}
				}

				customername.setText(" "
						+ map.get("customernametemp").toString() + ": ");
				detailtext.setText(map.get("tasktitle").toString());

				tasktimetext.setText(strTaskStatus);
				if (1 == nTaskType) {
					strtypetextview.setText("【求助】");
				} else if (2 == nTaskType) {
					strtypetextview.setText("【分享】");
				}
			}
			return convertView;
		}

	}

	private boolean IsShowTimeOut(int nTaskType, String strTaskId,
			String CurrentPersonName, String strImplePersonName) {
		boolean bIsTure = false;
		if (mDynamicNew != null) {
			int nCount = mDynamicNew.size();
			strStatusInfo = "";
			for (int i = 0; i < nCount; i++) {
				String strId = mDynamicNew.get(i).getmstrId();
				String strImpleAccouceName = mDynamicNew.get(i)
						.getstrTaskImplementTrueName();
				int nType = mDynamicNew.get(i).getmnTaskType();

				boolean b1 = strTaskId.equals(strId);
				boolean b3 = !strImpleAccouceName.equals("");
				if (b1 && nType == nTaskType) {
					if (!b3) {
						strTaskStatus = "等待被拿下...";
						if (2 == nTaskType) {
							strTaskStatus = "等待被抢下...";
						}
					} else {
						strTaskStatus = "任务正在执行中";
						if (2 == nTaskType) {
							strTaskStatus = "分享正在执行中";
						}
					}
					if (2 == mDynamicNew.get(i).getnTaskVerifiType()
							|| 3 == mDynamicNew.get(i).getnTaskVerifiType()) {
						strTaskStatus = "已评价，任务结束";
						if (2 == nTaskType) {
							strTaskStatus = "已评价，分享结束";
						}
						bIsTure = true;
					}
					break;
				}
			}
		}
		return bIsTure;
	}

	private boolean IsUpdatedNew(int nTaskType, String strTaskId) {
		boolean bIsTure = false;
		if (mDynamicNew != null) {
			int nCount = mDynamicNew.size();
			strStatusInfo = "";
			for (int i = 0; i < nCount; i++) {
				String strId = mDynamicNew.get(i).getmstrId();
				int nTaskTypeTemp = mDynamicNew.get(i).getmnTaskType();

				boolean b1 = strTaskId.equals(strId);
				if (nTaskTypeTemp == nTaskType) {
					if (b1) {
						if (2 == mDynamicNew.get(i)
								.getnTaskAnnounceCommentType()) {
							strStatusInfo = "有新消息";
							bIsTure = true;
						}
						break;
					}
				}
			}
		}
		return bIsTure;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 可以根据多个请求代码来作相应的操作
		if (20 == resultCode) {
			String PersonName = data.getExtras().getString("personname");
			int nCreditValue = data.getExtras().getInt("CreditValue");
			int nCharmValue = data.getExtras().getInt("CharmValue");
			my_textview1.setText(PersonName);
			my_textview1.setTextColor(Color.rgb(0, 0, 0));
			my_textview2.setText("人品:" + nCreditValue);
			my_textview3.setText("" + nCharmValue);

			Resources res = getResources();
			Bitmap zanbmp = BitmapFactory.decodeResource(res, R.drawable.zan1);
			my_imageview2.setImageBitmap(zanbmp);

			String strBase64Image = msettings.getString("Base64Image", "");
			if (!strBase64Image.equals("")) {
				CommonUtils utils = new CommonUtils(this);
				Bitmap bmp = utils.base64ToBitmap(strBase64Image);
				my_imageview1.setImageBitmap(bmp);
			} else {
				res = getResources();
				Bitmap bmp = BitmapFactory.decodeResource(res,
						R.drawable.noperson);
				my_imageview1.setImageBitmap(bmp);
			}
			// 启动轮询服务器
			PollingUtils.startPollingService(this, 20, UpdateDataService.class,
					UpdateDataService.ACTION);

			String strTemp = msettings.getString("TruePersonName", "");
			// 如果切换的用户是同一个,那么就没必要更新数据
			if (!strTemp.equals(mCurrentPersonName)) {
				// 修改当前用户名称
				mCurrentPersonNameTemp = msettings.getString("PersonName", "");
				mCurrentPersonName = msettings.getString("TruePersonName", "");
				if (mDynamicNew != null) {
					// 将动态信息清空
					mDynamicNew.clear();
				}
				bIsFirstStart = true;
			}

			mPersonInfoLinearLayout.setVisibility(View.VISIBLE);
			mLoginTextView.setVisibility(View.GONE);
			mPersonNickTextView.setText(mCurrentPersonNameTemp);
			mPersonImageView.setImageBitmap(mUtils
					.base64ToBitmap(strBase64Image));
		}
		if (0 == requestCode) {
			if (0 == resultCode) {
				my_textview1.setTextColor(Color.rgb(0, 135, 255));
				my_textview1.setText("点击登录");
				my_textview2.setText("人品:");
				my_textview3.setText("");

				Resources res = getResources();
				res = getResources();
				Bitmap bmp = BitmapFactory.decodeResource(res,
						R.drawable.noperson);
				my_imageview1.setImageBitmap(bmp);

				mPersonInfoLinearLayout.setVisibility(View.GONE);
				mLoginTextView.setVisibility(View.VISIBLE);
				mLoginTextView.setText("登录");
			} else if (1 == resultCode) {
				// 此时有可能修改了头像,这时应该更新头像
				String strBase64Image = msettings.getString("Base64Image", "");
				if (!strBase64Image.equals("")) {
					CommonUtils utils = new CommonUtils(RenPinMainActivity.this);
					Bitmap bmp = utils.base64ToBitmap(strBase64Image);
					my_imageview1.setImageBitmap(bmp);
				} else {
					Resources res = getResources();
					Bitmap bmp = BitmapFactory.decodeResource(res,
							R.drawable.noperson);
					my_imageview1.setImageBitmap(bmp);
				}
			}
		}
		if (1 == requestCode) {
			if (1 == resultCode) {
				String strId = data.getExtras().getString("strTaskId");
				String strtype = data.getExtras().getString("strTaskType");
				String strZan = data.getExtras().getString("strZan");
				String strTalk = data.getExtras().getString("strTalk");
				String strLook = data.getExtras().getString("strLook");
				if (!strZan.equals("") && !strTalk.equals("")
						&& !strLook.equals("")) {
					int nZan = Integer.parseInt(strZan);
					int nTalk = Integer.parseInt(strTalk);
					int nLook = Integer.parseInt(strLook);
					UpdateTaskOrShareInfo(strId, strtype, nZan, nTalk, nLook);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private double getDistance(double lng1, double lat1, double lng2,
			double lat2) {

		double radLat1 = Math.toRadians(lat1);

		double radLat2 = Math.toRadians(lat2);

		double radLng1 = Math.toRadians(lng1);

		double radLng2 = Math.toRadians(lng2);

		double deltaLat = radLat1 - radLat2;

		double deltaLng = radLng1 - radLng2;

		double distance = 2 * Math.asin(Math.sqrt(Math.pow(

		Math.sin(deltaLat / 2), 2)

		+ Math.cos(radLat1)

		* Math.cos(radLat2)

		* Math.pow(Math.sin(deltaLng / 2), 2)));

		distance = distance * EARTH_RADIUS_KM;

		long nvalue = Math.round(distance * 10000);
		distance = (double) nvalue / 10000;

		return distance;

	}

	// 处理刷新到的求助数据
	private void DealUpdateHelpTaskData(List<TaskInfoDetail> data) {
		if (data != null) {
			int nCount = data.size();
			int nSize = mHelpTaskData.size();
			// 判断新加入的数据与老数据总条数是否超过了mnVolume,如果超过了那么就需要清除一些老数据
			if (nCount + nSize > mnVolume) {
				// 组合新的数据
				List<TaskInfoDetail> newdata = new ArrayList<TaskInfoDetail>();
				// 先将最新的数据加入到该容器中
				newdata.addAll(0, data);
				// 判断容器中还能插入几条数据
				int nRemainDatas = mnVolume - nCount;
				int nOldTaskId = 0;
				// 如果还能插入
				if (nRemainDatas > 0) {
					for (int i = 0; i < nRemainDatas; i++) {
						newdata.add(mHelpTaskData.get(i));
					}
					nOldTaskId = Integer.parseInt(mHelpTaskData
							.get(nRemainDatas - 1).getmstrId().toString());
				} else {
					nOldTaskId = Integer.parseInt(data.get(mnVolume - 1)
							.getmstrId().toString());
				}
				// 将mHelpTaskData清空
				mHelpTaskData.clear();
				// 重新将数据赋值到该容器中
				mHelpTaskData.addAll(0, newdata);
				// 修改最旧任务id号
				mnHelpTaskMaxOldIndex = nOldTaskId;
				newdata.removeAll(newdata);
			} else {
				// 组合新的数据
				List<TaskInfoDetail> newdata = new ArrayList<TaskInfoDetail>();
				// 先将最新的数据加入到该容器中
				newdata.addAll(0, data);
				// 将老数据追加到容器后面
				newdata.addAll(data.size(), mHelpTaskData);
				// 将mHelpTaskData清空
				mHelpTaskData.clear();
				// 重新将数据赋值到该容器中
				mHelpTaskData.addAll(0, newdata);
				newdata.removeAll(newdata);
			}
		}
	}

	// 处理刷新到的分享数据
	private void DealUpdateShareTaskData(List<TaskInfoDetail> data) {
		if (data != null) {
			int nCount = data.size();
			int nSize = mShareTaskData.size();
			// 判断新加入的数据与老数据总条数是否超过了mnVolume,如果超过了那么就需要清除一些老数据
			if (nCount + nSize > nMaxDataLine) {
				// 组合新的数据
				List<TaskInfoDetail> newdata = new ArrayList<TaskInfoDetail>();
				// 先将最新的数据加入到该容器中
				newdata.addAll(0, data);
				// 判断容器中还能插入几条数据
				int nRemainDatas = nMaxDataLine - nCount;
				String strOldTaskTime = "";
				// 如果还能插入
				if (nRemainDatas > 0) {
					for (int i = 0; i < nRemainDatas; i++) {
						newdata.add(mShareTaskData.get(i));
					}
					int nSumSize = newdata.size();
					strOldTaskTime = newdata.get(nSumSize - 1)
							.getmTaskAnnounceTime().toString();
				} else {
					strOldTaskTime = data.get(nMaxDataLine - 1)
							.getmTaskAnnounceTime().toString();
				}
				// 将mHelpTaskData清空
				mShareTaskData.clear();
				// 重新将数据赋值到该容器中
				mShareTaskData.addAll(0, newdata);
				// 修改最旧任务id号
				mnOldTimeForShareAll = strOldTaskTime;
				newdata.removeAll(newdata);
			} else {
				// 组合新的数据
				List<TaskInfoDetail> newdata = new ArrayList<TaskInfoDetail>();
				// 先将最新的数据加入到该容器中
				newdata.addAll(0, data);
				// 将老数据追加到容器后面
				newdata.addAll(data.size(), mShareTaskData);
				// 将mHelpTaskData清空
				mShareTaskData.clear();
				// 重新将数据赋值到该容器中
				mShareTaskData.addAll(0, newdata);
				newdata.removeAll(newdata);
			}
		}
	}

	// 处理加载到的求助数据
	private void DealLoadNearHelpTaskData(List<DistanceDetail> data) {
		int nCount = data.size();
		int nSize = mHelpTaskDataDistance.size();
		int nRemainCount = nCount + nSize - mnVolume;
		// 如果数据条数超过了容器最大容量,那么就要去掉几条最近的数据
		if (nRemainCount > 0) {
			// 去掉nRemainCount条数据
			for (int i = 0; i < nRemainCount; i++) {
				mHelpTaskDataDistance.remove(0);
			}
			mHelpTaskDataDistance.addAll(nSize - nRemainCount, data);
		} else {// 如果数据没有超过最大值
				// 将加载数据添加到容器中
			mHelpTaskDataDistance.addAll(nSize, data);
		}
	}

	// 处理加载到的分享数据
	private void DealLoadNearShareTaskData(List<DistanceDetail> data) {

		int nCount = data.size();
		int nSize = mShareTaskDataDistance.size();
		int nRemainCount = nCount + nSize - mnVolume;
		// 如果数据条数超过了容器最大容量,那么就要去掉几条最新数据
		if (nRemainCount > 0) {
			// 去掉nRemainCount条数据
			for (int i = 0; i < nRemainCount; i++) {
				mShareTaskDataDistance.remove(0);
			}
			// 将加载数据添加到容器中
			mShareTaskDataDistance.addAll(nSize - nRemainCount, data);
		} else {// 如果数据没有超过最大值
			// 将加载数据添加到容器中
			mShareTaskDataDistance.addAll(nSize, data);
		}
	}

	// 处理加载到的求助数据
	private void DealLoadSpaceHelpTaskData(List<TaskInfoDetail> data) {
		int nCount = data.size();
		int nSize = mHelpTaskData.size();
		int nRemainCount = nCount + nSize - mnVolume;
		// 如果数据条数超过了容器最大容量,那么就要去掉几条最新数据
		if (nRemainCount > 0) {
			// 去掉nRemainCount条数据
			for (int i = 0; i < nRemainCount; i++) {
				mHelpTaskData.remove(0);
			}
			// 将加载数据添加到容器中
			mHelpTaskData.addAll(nSize - nRemainCount, data);
			// 修改最新的任务id
			mnHelpTaskMaxNewIndex = Integer.parseInt(mHelpTaskData.get(0)
					.getmstrId());
		} else {// 如果数据没有超过最大值
				// 将加载数据添加到容器中
			mHelpTaskData.addAll(nSize, data);
		}
	}

	// 处理加载到的分享数据
	private void DealLoadSpaceShareTaskData(List<TaskInfoDetail> data) {

		int nCount = data.size();
		int nSize = mShareTaskData.size();
		int nRemainCount = nCount + nSize - mnVolume;
		// 如果数据条数超过了容器最大容量,那么就要去掉几条最新数据
		if (nRemainCount > 0) {
			// 去掉nRemainCount条数据
			for (int i = 0; i < nRemainCount; i++) {
				mShareTaskData.remove(0);
			}
			// 将加载数据添加到容器中
			mShareTaskData.addAll(nSize - nRemainCount, data);
			// 修改最新的任务id
			mnNewTimeForShareAll = mShareTaskData.get(0).getmTaskAnnounceTime();
		} else {// 如果数据没有超过最大值
			// 将加载数据添加到容器中
			mShareTaskData.addAll(nSize, data);
		}
	}

	private List<DistanceDetail> DeleteSameDataForHelp(List<DistanceDetail> data) {
		if (data != null) {
			int nSize = data.size();
			for (int i = 0; i < nSize; i++) {
				if (mHelpTaskDataDistance != null) {
					int nSize1 = mHelpTaskDataDistance.size();
					for (int j = 0; j < nSize1; j++) {
						// 如果有相同的数据
						if (data.get(i)
								.getmstrId()
								.equals(mHelpTaskDataDistance.get(j)
										.getmstrId())) {
							// 将相同的数据删除
							data.remove(i);
							i--;
							nSize = data.size();
							break;
						}
					}
				}

			}
		}
		return data;
	}

	private List<DistanceDetail> DeleteSameDataForShare(
			List<DistanceDetail> data) {
		if (data != null) {
			int nSize = data.size();
			for (int i = 0; i < nSize; i++) {
				if (mShareTaskDataDistance != null) {
					int nSize1 = mShareTaskDataDistance.size();
					for (int j = 0; j < nSize1; j++) {
						// 如果有相同的数据
						if (data.get(i)
								.getmstrId()
								.equals(mShareTaskDataDistance.get(j)
										.getmstrId())) {
							// 将相同的数据删除
							data.remove(i);
							i--;
							nSize = data.size();
							break;
						}
					}
				}

			}
		}
		return data;
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// 加载
		Runnable rundata = new Runnable() {
			public void run() {
				if (2 == mIsShowingWindow) {
					if (1 == mnDataType) {
						// 更新mnVolume条数据
						List<DistanceDetail> HelpTaskDataTemp = goodService
								.LoadHelpNearData(MyLocation.mLongitude,
										MyLocation.mLatitude,
										(double) mnMaxDistance,
										strDistrictName, mnOldTimeForHelp);
						if (HelpTaskDataTemp != null) {
							int nSize = HelpTaskDataTemp.size();
							// 如果查询到了数据
							if (nSize > 0) {
								int nDistance = 0;
								String strTime;
								nDistance = HelpTaskDataTemp.get(nSize - 1)
										.getnDistance();
								strTime = HelpTaskDataTemp.get(nSize - 1)
										.getmTaskAnnounceTime();
								// 保存最新旧的任务id
								// mnMaxDistanceForHelpNear = nDistance;
								mnOldTimeForHelp = strTime;
								// 对HelpTaskDataTemp进行去重处理
								HelpTaskDataTemp = DeleteSameDataForHelp(HelpTaskDataTemp);
								// 排序
								DealLoadNearHelpTaskData(HelpTaskDataTemp);
							}
						}
					} else if (2 == mnDataType) {
						// 更新mnVolume条数据
						List<TaskInfoDetail> HelpTaskDataTemp = goodService
								.LoadTaskDataForLimitNew(nMaxDataLine, 1,
										mnOldTimeForShareAll);
						if (HelpTaskDataTemp != null) {
							int nSize = HelpTaskDataTemp.size();
							// 如果查询到了数据
							if (nSize > 0) {
								String strTime = "";
								strTime = HelpTaskDataTemp.get(nSize - 1)
										.getmTaskAnnounceTime().toString();
								// 保存最新旧的任务id
								mnOldTimeForShareAll = strTime;
								// 更新listview中的数据,修改相应的数据
								DealLoadSpaceHelpTaskData(HelpTaskDataTemp);
							}
						}
					}
				} else if (3 == mIsShowingWindow) {// 此时也更新数据
					if (1 == mnDataType) {
						// 更新mnVolume条数据
						List<DistanceDetail> ShareTaskDataTemp = goodService
								.LoadShareNearData(MyLocation.mLongitude,
										MyLocation.mLatitude,
										(double) mnMaxDistance,
										strDistrictName, mnOldTimeForShareNear);
						if (ShareTaskDataTemp != null) {
							int nSize = ShareTaskDataTemp.size();
							// 如果查询到了数据
							if (nSize > 0) {
								int nDistance = 0;
								String strTime;
								nDistance = ShareTaskDataTemp.get(nSize - 1)
										.getnDistance();
								strTime = ShareTaskDataTemp.get(nSize - 1)
										.getmTaskAnnounceTime();
								// 保存最新的任务id
								// mnMaxDistanceForShareNear = nDistance;
								mnOldTimeForShareNear = strTime;
								ShareTaskDataTemp = DeleteSameDataForShare(ShareTaskDataTemp);
								// 排序
								DealLoadNearShareTaskData(ShareTaskDataTemp);
							}
						}
					} else if (2 == mnDataType) {
						// 更新mnVolume条数据
						List<TaskInfoDetail> ShareTaskDataTemp = goodService
								.LoadTaskDataForLimitNew(nMaxDataLine, 2,
										mnOldTimeForShareAll);
						if (ShareTaskDataTemp != null) {
							int nSize = ShareTaskDataTemp.size();
							// 如果查询到了数据
							if (nSize > 0) {
								String strTime = "";
								strTime = ShareTaskDataTemp.get(nSize - 1)
										.getmTaskAnnounceTime().toString();
								// 保存最新的任务id
								mnOldTimeForShareAll = strTime;
								// 更新listview中的数据,修改相应的数据
								DealLoadSpaceShareTaskData(ShareTaskDataTemp);
							}
						}
					}
				}
				// 发送消息,将数据更新
				Message msg = myhandler.obtainMessage();
				msg.what = 2;
				myhandler.sendMessage(msg);
			}
		};
		// 启动获取评论数据线程
		Thread thread = new Thread(rundata);
		thread.start();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// 刷新
		Runnable rundata = new Runnable() {
			public void run() {
				if (2 == mIsShowingWindow) {
					if (1 == mnDataType) {
						// 更新mnVolume条数据
						mHelpTaskDataDistance = goodService.getHelpNearData(
								MyLocation.mLongitude, MyLocation.mLatitude,
								(double) mnMaxDistance, strDistrictName);
						if (mHelpTaskDataDistance != null) {
							int nSize = mHelpTaskDataDistance.size();
							// 如果查询到了数据
							if (nSize > 0) {
								int ndistance = 0;
								String strTime;
								ndistance = mHelpTaskDataDistance
										.get(nSize - 1).getnDistance();
								strTime = mHelpTaskDataDistance.get(nSize - 1)
										.getmTaskAnnounceTime();
								// 保存最大距离
								// mnMaxDistanceForHelpNear = ndistance;
								mnOldTimeForHelp = strTime;
								// 更新listview中的数据,修改相应的数据
								// DealUpdateHelpTaskData(HelpTaskDataTemp);
							}
						}
					} else if (2 == mnDataType) {
						// 更新mnVolume条数据
						List<TaskInfoDetail> HelpTaskDataTemp = goodService
								.UpdateTaskDataForLimit(nMaxDataLine, 1,
										mnHelpTaskMaxNewIndex);
						if (HelpTaskDataTemp != null) {
							int nSize = HelpTaskDataTemp.size();
							// 如果查询到了数据
							if (nSize > 0) {
								int nIndex = 0;
								nIndex = Integer.parseInt(HelpTaskDataTemp
										.get(0).getmstrId().toString());
								// 保存最新的任务id
								mnHelpTaskMaxNewIndex = nIndex;
								// 更新listview中的数据,修改相应的数据
								DealUpdateHelpTaskData(HelpTaskDataTemp);
							}
						}
					}
				} else if (3 == mIsShowingWindow) {// 此时也更新数据
					if (1 == mnDataType) {
						// 更新mnVolume条数据
						mShareTaskDataDistance = goodService.getShareNearData(
								MyLocation.mLongitude, MyLocation.mLatitude,
								(double) mnMaxDistance, strDistrictName);
						if (mShareTaskDataDistance != null) {
							int nSize = mShareTaskDataDistance.size();
							// 如果查询到了数据
							if (nSize > 0) {
								int nDistance = 0;
								String strTime;
								nDistance = mShareTaskDataDistance.get(0)
										.getnDistance();
								strTime = mShareTaskDataDistance.get(0)
										.getmTaskAnnounceTime();
								// 保存最新的任务id
								// mnMaxDistanceForShareNear = nDistance;
								mnOldTimeForShareNear = strTime;
								// 更新listview中的数据,修改相应的数据
								// DealUpdateShareTaskData(mShareTaskDataDistance);
							}
						}
					} else if (2 == mnDataType) {
						// 更新mnVolume条数据
						List<TaskInfoDetail> ShareTaskDataTemp = goodService
								.UpdateTaskDataForLimitNew(nMaxDataLine, 2,
										mnNewTimeForShareAll);
						if (ShareTaskDataTemp != null) {
							int nSize = ShareTaskDataTemp.size();
							// 如果查询到了数据
							if (nSize > 0) {
								String strTime = "";
								strTime = ShareTaskDataTemp.get(0)
										.getmTaskAnnounceTime().toString();
								// 保存最新的任务id
								mnNewTimeForShareAll = strTime;
								// 更新listview中的数据,修改相应的数据
								DealUpdateShareTaskData(ShareTaskDataTemp);
							}
						}
					}
				}
				// 发送消息,将数据更新
				Message msg = myhandler.obtainMessage();
				msg.what = 3;
				myhandler.sendMessage(msg);
			}
		};
		// 启动获取评论数据线程
		Thread thread = new Thread(rundata);
		thread.start();
	}

	// 设置求助adapter
	public void SetHelpAdapter(boolean bIsReSet) {
		if (null == madapter) {
			if (mListData != null) {
				madapter = new TaskInfoListAdpater(RenPinMainActivity.this,
						mListData, R.layout.help_eachother, new String[] {
								"regionname", "distance", "customericon",
								"customername", "tasktitle" }, new int[] {
								R.id.help_eachother_textview1,
								R.id.help_eachother_textview2,
								R.id.help_eachother_imageview1,
								R.id.help_eachother_textview3,
								R.id.help_eachother_textview4 });
				mgridview.setAdapter(madapter);
			}
		} else {
			if (bIsReSet) {
				// setListAdapter(madapter);
				mgridview.setAdapter(madapter);
			}
			madapter.mItemList = mListData;
			madapter.notifyDataSetChanged();
		}
	}

	// 设置分享adapter
	public void SetShareAdapter(boolean bIsReset) {
		if (null == madapter2) {
			if (mListData2 != null) {
				madapter2 = new TaskInfoListAdpater(RenPinMainActivity.this,
						mListData2, R.layout.help_eachother, new String[] {
								"regionname", "distance", "customericon",
								"customername", "tasktitle" }, new int[] {
								R.id.help_eachother_textview1,
								R.id.help_eachother_textview2,
								R.id.help_eachother_imageview1,
								R.id.help_eachother_textview3,
								R.id.help_eachother_textview4 });
				mgridview.setAdapter(madapter2);
			}

		} else {
			if (bIsReset) {
				// setListAdapter(madapter2);
				mgridview.setAdapter(madapter2);
			}
			madapter2.mItemList = mListData2;
			madapter2.notifyDataSetChanged();
		}
	}

	/***
	 * 检查是否更新版本
	 */
	public void checkVersion() {
		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					downloadUpdateXMLFile(strUpdateXmlPath);
					/*
					 * if (downloadSize > 0) { // 获取更新xml文件 Message message =
					 * handler.obtainMessage(); message.what = 1;
					 * handler.sendMessage(message); }
					 */

				} catch (Exception e) {
					/*
					 * Message message = handler.obtainMessage(); message.what =
					 * 2; handler.sendMessage(message);
					 */
				}

			}
		}).start();
	}

	/***
	 * 下载文件
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	public long downloadUpdateXMLFile(String down_url) throws Exception {
		int totalSize;// 文件总大小
		InputStream inputStream;
		int downloadCount = 0;// 已经下载好的大小
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
		inputStream = httpURLConnection.getInputStream();
		mupdateinfo = getUpdataInfo(inputStream);
		/*
		 * byte buffer[] = new byte[1024]; int readsize = 0; while ((readsize =
		 * inputStream.read(buffer)) != -1) { downloadCount += readsize;//
		 * 时时获取下载到的大小 }
		 */
		if (!mupdateinfo.getVersion().equals("")) {
			downloadCount = 1;
		}

		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}
		inputStream.close();

		return downloadCount;
	}

	/*
	 * 用pull解析器解析服务器返回的xml文件 (xml封装了版本号)
	 */
	public static UpdateInfo getUpdataInfo(InputStream is) throws Exception {
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "utf-8");// 设置解析的数据源
		int type = parser.getEventType();
		UpdateInfo info = new UpdateInfo();// 实体
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				if ("version".equals(parser.getName())) {
					info.setVersion(parser.nextText()); // 获取版本号
				} else if ("url".equals(parser.getName())) {
					info.setUrl(parser.nextText()); // 获取要升级的APK文件
				} else if ("description".equals(parser.getName())) {
					String strTemp = parser.nextText().replace("\\n", "\n");
					info.setDescription(strTemp); // 获取该文件的信息
				}
				break;
			}
			type = parser.next();
		}
		return info;
	}

	// 更新用户头像
	public static void UpdateAccountPersonImage(String strPersonName,
			String strImage) {
		int nSize = 0;
		// 更新我的中的头像
		if (mDynamicNew != null) {
			nSize = mDynamicNew.size();
			for (int i = 0; i < nSize; i++) {
				// 如果找到了当前用户发布的任务或分享,那么就将他的头像更新
				if (mDynamicNew.get(i).getstrTaskPersonTrueName()
						.equals(strPersonName)) {
					mDynamicNew.get(i).setmTaskAskPersonIcon(strImage);
				}
			}
		}
		// 更新求助中的头像
		if (mHelpTaskDataDistance != null) {
			nSize = mHelpTaskDataDistance.size();
			for (int i = 0; i < nSize; i++) {
				if (mHelpTaskDataDistance.get(i).getstrTaskPersonTrueName()
						.equals(strPersonName)) {
					mHelpTaskDataDistance.get(i)
							.setmTaskAskPersonIcon(strImage);
				}
			}

		}
		// 更新分享中的头像
		if (mShareTaskDataDistance != null) {
			nSize = mShareTaskDataDistance.size();
			for (int i = 0; i < nSize; i++) {
				if (mShareTaskDataDistance.get(i).getstrTaskPersonTrueName()
						.equals(strPersonName)) {
					mShareTaskDataDistance.get(i).setmTaskAskPersonIcon(
							strImage);
				}
			}
		}

		// 更新求助中的头像
		if (mHelpTaskData != null) {
			nSize = mHelpTaskData.size();
			for (int i = 0; i < nSize; i++) {
				if (mHelpTaskData.get(i).getstrTaskPersonTrueName()
						.equals(strPersonName)) {
					mHelpTaskData.get(i).setmTaskAskPersonIcon(strImage);
				}
			}

		}
		// 更新分享中的头像
		if (mShareTaskData != null) {
			nSize = mShareTaskData.size();
			for (int i = 0; i < nSize; i++) {
				if (mShareTaskData.get(i).getstrTaskPersonTrueName()
						.equals(strPersonName)) {
					mShareTaskData.get(i).setmTaskAskPersonIcon(strImage);
				}
			}
		}
	}

	// 更新项目信息,nType值为1表示求助,2表示分享,nValue为1表示修改标题,2表示详细信息
	public static void UpdateAccountPersonInfo(String strId, int nType,
			int nValue, String strInfo) {
		int nSize = 0;
		// 更新我的中的信息
		if (mDynamicNew != null) {
			nSize = mDynamicNew.size();
			for (int i = 0; i < nSize; i++) {
				// 如果找到了当前用户发布的任务或分享,那么就将他的头像更新
				if (mDynamicNew.get(i).getmstrId().equals(strId)
						&& nType == mDynamicNew.get(i).getmnTaskType()) {
					if (1 == nValue) {
						mDynamicNew.get(i).setmTaskTitle(strInfo);
					} else if (2 == nValue) {
						mDynamicNew.get(i).setmTaskDetail(strInfo);
					}
				}
			}
		}
		// 更新求助中的信息
		if (mHelpTaskDataDistance != null) {
			nSize = mHelpTaskDataDistance.size();
			for (int i = 0; i < nSize; i++) {
				if (mHelpTaskDataDistance.get(i).getmstrId().equals(strId)
						&& nType == mHelpTaskDataDistance.get(i)
								.getmnTaskType()) {
					if (1 == nValue) {
						mHelpTaskDataDistance.get(i).setmTaskTitle(strInfo);
					} else if (2 == nValue) {
						mHelpTaskDataDistance.get(i).setmTaskDetail(strInfo);
					}
				}
			}

		}
		// 更新分享中的信息
		if (mShareTaskDataDistance != null) {
			nSize = mShareTaskDataDistance.size();
			for (int i = 0; i < nSize; i++) {
				if (mShareTaskDataDistance.get(i).getmstrId().equals(strId)
						&& nType == mShareTaskDataDistance.get(i)
								.getmnTaskType()) {
					if (1 == nValue) {
						mShareTaskDataDistance.get(i).setmTaskTitle(strInfo);
					} else if (2 == nValue) {
						mShareTaskDataDistance.get(i).setmTaskDetail(strInfo);
					}
				}
			}
		}

		// 更新求助中的信息
		if (mHelpTaskData != null) {
			nSize = mHelpTaskData.size();
			for (int i = 0; i < nSize; i++) {
				if (mHelpTaskData.get(i).getmstrId().equals(strId)
						&& nType == mHelpTaskData.get(i).getmnTaskType()) {
					if (1 == nValue) {
						mHelpTaskData.get(i).setmTaskTitle(strInfo);
					} else if (2 == nValue) {
						mHelpTaskData.get(i).setmTaskDetail(strInfo);
					}
				}
			}

		}
		// 更新分享中的信息
		if (mShareTaskData != null) {
			nSize = mShareTaskData.size();
			for (int i = 0; i < nSize; i++) {
				if (mShareTaskData.get(i).getmstrId().equals(strId)
						&& nType == mShareTaskData.get(i).getmnTaskType()) {
					if (1 == nValue) {
						mShareTaskData.get(i).setmTaskTitle(strInfo);
					} else if (2 == nValue) {
						mShareTaskData.get(i).setmTaskDetail(strInfo);
					}
				}
			}
		}
	}

	public void createNotification() {

		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		updateIntent = new Intent(this, RenPinMainActivity.class);
		updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);
		notification = new Notification();
		// 设置通知在状态栏显示的图标
		notification.icon = R.drawable.zan;

		// 通知时在状态栏显示的内容
		notification.tickerText = "开始下载";
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
	 * 开线程下载
	 */
	public void createThread() {
		/***
		 * 更新UI
		 */
		final Handler handlerDownFile = new Handler() {
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

					RenPinMainActivity.this.startActivity(intent);
					// notificationManager.notify(notification_id,
					// notification);
					if (notificationManager != null) {
						notificationManager.cancel(notification_id);
					}
					RenPinMainActivity.this.finish();
					break;
				case DOWN_ERROR:
					notification.setLatestEventInfo(RenPinMainActivity.this,
							getResources().getString(R.string.app_name1),
							"下载失败", pendingIntent);
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
						handlerDownFile.sendMessage(message);
					}

				} catch (Exception e) {
					e.printStackTrace();
					message.what = DOWN_ERROR;
					handlerDownFile.sendMessage(message);
				}

			}
		}).start();
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

	// 异步加载图片
	private class ImageDownloadTask extends AsyncTask<Object, Object, Bitmap> {
		private ImageView imageView = null;

		@Override
		protected Bitmap doInBackground(Object... params) {
			Bitmap bmp = null;
			imageView = (ImageView) params[1];
			String strTaskId = (String) params[2];
			String strType = (String) params[3];
			String strFileName = (String) params[0];
			String strRootPath = mUtils.GetFileRootPath();
			String strImagePath = strRootPath + strFileName;
			// 判断本地中是否保存了该图片
			boolean bIsExist = mUtils.fileIsExists(strImagePath);
			// 如果本地存在该图片数据
			if (bIsExist) {
				// 将图片从本地中读出
				bmp = mUtils.GetBitMapFromPathNew(strImagePath);
			} else {// 如果不存在
					// 从服务器中读取
				String strImage = goodService.GetFirstSmallIcon(strTaskId,
						strType);
				// 将base64格式的图片转换成bmp
				bmp = mUtils.base64ToBitmap(strImage);
				// 将图片数据保存到本地文件中
				try {
					mUtils.saveBitmapToFileNew(bmp, strImagePath);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return bmp;
		}

		protected void onPostExecute(Bitmap result) {
			imageView.setImageBitmap(result);
		}
	}

	// 判断某任务或分享是否在mDynamicNew中,如果不存在,那么就到服务器中取
	public static void UpdateDynamicData(String strTaskId, int nType) {
		boolean bFind = false;
		if (mDynamicNew != null) {
			int nSize = mDynamicNew.size();
			for (int i = 0; i < nSize; i++) {
				if (mDynamicNew.get(i).getmnTaskType() == nType
						&& mDynamicNew.get(i).getmstrId().equals(strTaskId)) {
					bFind = true;
					break;
				}
			}
			// 如果找到了,那么说明已经有了该数据,不用到服务器上取,否则需要到服务器上获取新数据
			if (!bFind) {
				// 设置更新"我的"数据的标志
				bIsFirstStart = true;
			}
		}
	}
}
