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

	private final static String strSpecialOldTime = "2012��12��12��12:12:12";
	// ��ʾ��̬�б������
	private ListView mListView2;
	//mListView2Ϊ��ʱ��ʾ������
	private TextView emptytextview;
	// ����
	PullToRefreshView mPullToRefreshView1;
	/** ����뾶����λ����� */
	public final static double EARTH_RADIUS_KM = 6378.137;
	// �洢��̬��Ϣ
	public static List<TaskInfoDetail> mDynamicNew = null;
	// ����������
	private static CommonUtils mUtils = null;
	// �����洢�����ݿ��л�ȡ��������
	// �ҵ�
	private List<HashMap<String, Object>> mListData1 = null;
	// ����
	private List<HashMap<String, Object>> mListData = null;
	// ����
	private List<HashMap<String, Object>> mListData2 = null;
	// ����
	private static TaskInfoListAdpater madapter = null;
	// �ҵ�
	private static DynamicNewsListAdpater madapter1 = null;
	// ����
	private static TaskInfoListAdpater madapter2 = null;
	// �ж����������Ƿ�Ӧ��ˢ��
	boolean mbIsFinished = true;
	// ������Ͽ���,��ô�ͽ������������
	LinearLayout nonetworklayout = null;
	// �������
	LinearLayout morelayout;
	// ��������
	ImageView mAnnounceTaskImageView;
	// ��ʱ�����������ݵ��̶߳���
	private static Thread mthre = null;
	// �жϸ��������߳��Ƿ�Ӧ��ֹͣ
	private boolean mbIsRunning = true;
	// ������Ϣ����
	MyHandler myhandler = null;
	// ������ȡ�����������ݵĶ���
	private GoodService goodService = new GoodServiceImpl();
	// ������ʾ1��ʾ�ҵ�,2��ʾ����,3��ʾ����,4��ʾ����
	public static int mIsShowingWindow = 3;
	// �����洢�û���Ϣ
	SharedPreferences msettings = null;
	// ��ǰ�û�����
	private String mCurrentPersonName = null;
	// ��ǰ�û��ǳ�
	private String mCurrentPersonNameTemp = null;

	// private TextView tasktextview;
	// ������ͼƬ
	public static Bitmap mAnnounceImage;
	// ��ȡ��ǰ��γ�ȵ���
	private MyLocation mMyLocation = null;
	// ��ʾ��̬��Ϣ�����Ŀؼ�����
	private TextView textview6;
	// ��̬�ؼ�����
	private LinearLayout linearlayout1;
	// �����ؼ�����
	private LinearLayout linearlayout7;
	// �������˿ؼ�����
	private LinearLayout linearlayout2;
	// ��̬���˿ؼ�����
	private LinearLayout linearlayout3;
	// �����ؼ�����
	private LinearLayout linearlayout4;
	// ״̬��ʾ��Ϣ
	private String strStatusInfo = "";
	// �������״̬
	private String strTaskStatus = "";
	// �������
	LinearLayout linearlayout5;
	// ���ඥ������
	LinearLayout linearlayout6;
	// �û����ƿؼ�
	TextView my_textview1;
	// �û�ͼ��ؼ�
	ImageView my_imageview1;
	// ��ͼ��ؼ�
	ImageView my_imageview2;
	// ��Ʒֵ�ؼ�
	TextView my_textview2;
	// ��ֵ�ؼ�
	TextView my_textview3;
	// ����������֤����ո�����ʱˢ������
	public static boolean bIsFirstStart = true;
	// �ж���������״̬
	private static boolean bNetWorkLinkStatus = true;
	// listview����ֵ
	private final static int mnVolume = 60;
	// һ�μ���
	private final static int nMaxDataLine = 10;
	// ��������ǰ������ɵ���������id��
	private static int mnHelpTaskMaxOldIndex = 0;
	// ��������ǰˢ�µ������������ݵ�id��
	private static int mnHelpTaskMaxNewIndex = 0;
	// �洢������������(�㳡)
	public static List<TaskInfoDetail> mHelpTaskData = null;
	// �洢������������(�㳡)
	public static List<TaskInfoDetail> mShareTaskData = null;
	// �洢������������(����������)
	public static List<DistanceDetail> mHelpTaskDataDistance = null;
	// �洢������������(����������)
	public static List<DistanceDetail> mShareTaskDataDistance = null;
	// ���������о����û�������ֵ(�ܱ�)
	// private static int mnMaxDistanceForHelpNear = 0;
	// �����о����û�������ֵ(�ܱ�)
	// private static int mnMaxDistanceForShareNear = 0;
	// �������������ϵ�ʱ��(�ܱ�)
	private static String mnOldTimeForHelp = strSpecialOldTime;
	// ���������ϵ�ʱ��(�ܱ�)
	private static String mnOldTimeForShareNear = strSpecialOldTime;
	// ���������ϵ�ʱ��(�㳡)
	private static String mnOldTimeForShareAll = strSpecialOldTime;
	// ���������µ�ʱ��(�㳡)
	private static String mnNewTimeForShareAll = strSpecialOldTime;

	private long mlCurTime = 0;
	// ��������ؼ�
	private RelativeLayout advicetextview;
	// �������ǿؼ�
	private RelativeLayout aboutustextview;
	// �ж��Ƿ��Ѿ���ʾ���°汾��
	private boolean bIsReminderUpdateApk = false;
	// �汾����
	private RelativeLayout versionupdaterelative;
	// �汾������ʾ��Ϣ
	private TextView versionupdateinfotextview;
	public UpdateInfo mupdateinfo = null;
	private static final int TIMEOUT = 10 * 1000;// ��ʱ59.60.9.202:8000
	private static final String strUpdateXmlPath = // "http://192.168.2.2:8080/Apk/download?fileName=update.xml";
	"http://www.meiliangshare.cn:8000/Apk/download?fileName=update.xml";
	public static int localVersion = 1;// ���ذ�װ�汾
	// ����ÿؼ������û���ϸ����
	LinearLayout myinfolinearlayout;
	// ������(��)
	private static final double mnMaxDistance = 5000;
	// ����������
	public ProgressDialog m_ProgressDialog = null;
	// �ܱ߰�ť
	private Button nearbutton;
	// �㳡��ť
	private Button spacebutton;
	// �ܱ߻�㳡��־,1Ϊ�ܱ�,2Ϊ�㳡,Ĭ��Ϊ�ܱ�
	private int mnDataType = 2;
	// ��ǰ������������
	private String strDistrictName;

	/* ���ظ����йصı��� (start) */
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
	// �ж������Ƿ�������,��������֮��Żᵯ���汾���¶Ի���
	private boolean bDataFinishLoad = false;
	// gridview
	private GridView mgridview;
	// ��������������linearlayout
	private LinearLayout moreoperalinearlayout;
	// ����textview�ؼ�
	private TextView mAnnounceTextView;
	// ����textview�ؼ�
	private TextView mMoreTextView;
	// ��������еķ���ͼ��
	private ImageView mMoreReturnImageView;
	// �ҵĶ��˽����еķ���imagview
	private ImageView mMyTopReturnImageView;
	// �û���Ϣlinearlayout�ؼ�
	private LinearLayout mPersonInfoLinearLayout;
	// �û�ͷ��ؼ�
	private ImageView mPersonImageView;
	// �û��ǳƿؼ�
	private TextView mPersonNickTextView;
	// ��ʾ��¼�Ŀؼ�
	private TextView mLoginTextView;
	//������ֵ
	public static HashMap<String,Integer> StoreZanMap = new HashMap<String,Integer>();
	//StoreZanMap������Ŀ���ĸ���
	public static int MaxStoreMap = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// ����������ʱ������괦��editview�ж��������뷨����
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_ren_pin_main);
		// ������ѯ������
		PollingUtils.startPollingService(this, 20, UpdateDataService.class,
				UpdateDataService.ACTION);
		// ��ȡ�汾��
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
		// ��ʼΪû�и�����ʾ������һ����⵽�°汾,��ô����ʾ����ʾ����֮��ֻҪ�û�û���˳��Ͳ�����ʾ
		bIsReminderUpdateApk = false;
		// ȷ����̬����ʾ����
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
		// ��̬��Ϣ��û��������ô�͵�����������������
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
			// �����������������
			mUtils = new CommonUtils(this);
		}
		// ��ʼ������ؼ�
		InitActivities();
		// ��������������update.xml�ļ�,��ȡ�������Ϣ
		checkVersion();
		// ����������Ϣ����
		myhandler = new MyHandler();

		if (null == mMyLocation) {
			// ��ʼ��ȡ��ǰ��γ��
			mMyLocation = new MyLocation(this);
		}
		SharedPreferences.Editor editor = msettings.edit();
		// ��base64���͵�ͼ�걣������
		editor.putString("CurrentLocationAddress", MyLocation.mStrAddress);
		editor.commit();

		// �����߳�,��ȡ�����ͷ�������
		GetHelpOrShareData();
		// �����ʱ�̶߳���û�д���,��ô�ʹ���
		if (null == mthre) {
			mbIsRunning = true;
			mthre = new Thread(run);
			mthre.start();
		}
		// �����ǰΪ2G/3G��������ʾ��ʾ��
		ShowPromptWindow();
	}

	private void ShowPromptWindow() {
		// �ֻ���������
		ConnectivityManager connectMgr = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectMgr.getActiveNetworkInfo();
		if (info != null) {
			// ����ֻ�����2g��3g����
			if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						RenPinMainActivity.this);
				dialog.setMessage("��ǰ����Ϊ2G/3G����,ʹ�����ǵ��˳�����Ŷ!");
				dialog.setTitle("��ܰ��ʾ");
				dialog.setPositiveButton("ȷ��",
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
		// �����ʱ�����ҵĽ�����߸������,��ô�ͷ��ص���������
		if (1 == mIsShowingWindow || 4 == mIsShowingWindow) {
			linearlayout2.setVisibility(View.VISIBLE);
			// tasktextview.setText("����");
			linearlayout3.setVisibility(View.GONE);
			mPullToRefreshView1.setVisibility(View.VISIBLE);
			// ����Ǵ�����һ�������л�������,��ô��Ҫ������������
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
			CommonUtils.ShowToastCenter(this, "�ٰ�һ���˳�����", Toast.LENGTH_LONG);
		}
		mlCurTime = System.currentTimeMillis();
	}

	private void ReadHelpDistanceData() {
		if (mHelpTaskDataDistance != null) {
			// �����ʱ������û������,��ô�ͼ���
			if (mHelpTaskDataDistance.size() <= 0) {
				// һ���Ը���30������
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
						// ����������
						// mnMaxDistanceForHelpNear = ndistance;
						mnOldTimeForHelp = strTime;
					}
				}
			}
		} else {// ��ʱҲ��������
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
					// ����������
					// mnMaxDistanceForHelpNear = ndistance;
					mnOldTimeForHelp = strTime;
				}
			}
		}
	}

	private void ReadShareDistanceData() {
		if (mShareTaskDataDistance != null) {
			// �����ʱ������û������,��ô�ͼ���
			if (mShareTaskDataDistance.size() <= 0) {
				// һ���Ը���mnVolume������
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
						// ������ɵ�����id
						// mnMaxDistanceForShareNear = nDistance;
						mnOldTimeForShareNear = strTime;
					}
				}
			}
		} else {// ��ʱҲ��������
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
					// ������ɵ�����id
					// mnMaxDistanceForShareNear = nDistance;
					mnOldTimeForShareNear = strTime;
				}
			}
		}
	}

	private void ReadHelpData() {
		if (mHelpTaskData != null) {
			// �����ʱ������û������,��ô�ͼ���
			if (mHelpTaskData.size() <= 0) {
				// һ���Ը���mnVolume������
				mHelpTaskData = goodService.UpdateTaskDataForLimit(
						nMaxDataLine, 1, 0);
				if (mHelpTaskData != null) {
					int nSize = mHelpTaskData.size();
					if (nSize > 0) {
						int nIndex = 0;
						nIndex = Integer.parseInt(mHelpTaskData.get(nSize - 1)
								.getmstrId().toString());
						// ������ɵ�����id
						mnHelpTaskMaxOldIndex = nIndex;
						// �������µ�����id
						nIndex = Integer.parseInt(mHelpTaskData.get(0)
								.getmstrId().toString());
						mnHelpTaskMaxNewIndex = nIndex;
					}
				}
			}
		} else {// ��ʱҲ��������
			mHelpTaskData = goodService.UpdateTaskDataForLimit(nMaxDataLine, 1,
					0);
			if (mHelpTaskData != null) {
				int nSize = mHelpTaskData.size();
				if (nSize > 0) {
					int nIndex = 0;
					nIndex = Integer.parseInt(mHelpTaskData.get(nSize - 1)
							.getmstrId().toString());
					// ������ɵ�����id
					mnHelpTaskMaxOldIndex = nIndex;
					// �������µ�����id
					nIndex = Integer.parseInt(mHelpTaskData.get(0).getmstrId()
							.toString());
					mnHelpTaskMaxNewIndex = nIndex;
				}
			}
		}
	}

	private void ReadShareData() {
		if (mShareTaskData != null) {
			// �����ʱ������û������,��ô�ͼ���
			if (mShareTaskData.size() <= 0) {
				// һ���Ը���mnVolume������
				mShareTaskData = goodService.UpdateTaskDataForLimitNew(
						nMaxDataLine, 2, mnNewTimeForShareAll);
				if (mShareTaskData != null) {
					int nSize = mShareTaskData.size();
					if (nSize > 0) {
						String strTime = mShareTaskData.get(nSize - 1)
								.getmTaskAnnounceTime().toString();
						// ������ɵ�����id
						mnOldTimeForShareAll = strTime;
						// �������µ�����id
						strTime = mShareTaskData.get(0).getmTaskAnnounceTime()
								.toString();
						mnNewTimeForShareAll = strTime;
					}
				}
			}
		} else {// ��ʱҲ��������
			mShareTaskData = goodService.UpdateTaskDataForLimitNew(
					nMaxDataLine, 2, mnNewTimeForShareAll);
			if (mShareTaskData != null) {
				int nSize = mShareTaskData.size();
				if (nSize > 0) {
					String strTime = mShareTaskData.get(nSize - 1)
							.getmTaskAnnounceTime().toString();
					// ������ɵ�����id
					mnOldTimeForShareAll = strTime;
					// �������µ�����id
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
				// ������ܱ�
				if (1 == mnDataType) {
					// �ȴ������ȡ����ǰ�û���γ��
					while (-1 == MyLocation.mLatitude) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					strDistrictName = "'" + MyLocation.mDistrictName + "'";
					// ��ȡ���а�ʱ������ķ�������
					ReadShareDistanceData();

					Message msg = myhandler.obtainMessage();
					msg.what = 4;
					myhandler.sendMessage(msg);

				} else if (2 == mnDataType) {// ����ǹ㳡
					// ��ȡ��ʱ������ķ�������
					ReadShareData();
					Message msg = myhandler.obtainMessage();
					msg.what = 4;
					myhandler.sendMessage(msg);
				}
			}
		};
		bDataFinishLoad = false;
		m_ProgressDialog = ProgressDialog.show(RenPinMainActivity.this, "��ʾ",
				"���ڼ�������,��ȴ�...", true);
		// ����ʹ�ð����ؼ��رյȴ���
		m_ProgressDialog.setCancelable(true);
		// ������ȡ���������߳�
		Thread thread = new Thread(rundata);
		thread.start();
	}

	// ���ݶ�̬��Ϣ�еķ���id������һ��id���ַ���
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
		// �ж��Ƿ����ϵͳ��Ϣ
		boolean bIsExist = false;
		if (mdatas != null) {
			if (null == mDynamicNew) {
				mDynamicNew = mdatas;
			} else {
				int nSize = mdatas.size();
				int nCount = mDynamicNew.size();
				// ��ȡϵͳ��Ϣ�Ƿ����
				if (nCount > 0) {
					if (mDynamicNew.get(0).getmstrId().equals("")) {
						bIsExist = true;
					}
				}
				if (nSize > 0) {
					// �����һ������ϵͳ��Ϣ,���ͻ�����ϵͳ��Ϣ,��ô�ͽ�֪ͨ��Ϣ������ֵ��ϵͳ��Ϣ
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
						// ����ҵ�����ͬ�ķ���id��,˵���������ݵĸ���
						if (mdatas.get(i).getmstrId()
								.equals(mDynamicNew.get(j).getmstrId())) {
							// �滻��mDynamicNew����Ӧ������
							mDynamicNew.set(j, mdatas.get(i));
							bIsFind = true;
							break;
						}
					}
					// ���û���ҵ�,˵����������,�����������ӵ�mDynamicNew��
					if (!bIsFind) {
						// �����ϵͳ��Ϣ
						if (mdatas.get(i).getmstrId().equals("")) {
							// ��ϵͳ��Ϣ�������ӵ�mDynamicNew����ǰ��
							mDynamicNew.add(0, mdatas.get(i));
						} else {// �������ϵͳ��Ϣ
								// �������ϵͳ��Ϣ
							if (bIsExist) {
								// ������Ϣ���ݲ��뵽ϵͳ��Ϣ���ݵ�����
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
					// ��ˢ�±�־��Ϊ��ˢ��״̬
					mbIsFinished = false;
					// �����Ҫ��������
					if ((1 == UpdateDataService.mShouldUpdate
							.getnUpdateSignal() || bIsFirstStart)
							&& !mCurrentPersonName.equals("")) {
						UpdateDataService.mShouldUpdate.setnUpdateSignal(0);

						// ��ȡ��̬��Ϣ�е�id�ַ���
						String strID = GetIdString();// mDynamicNew
						// ��ȡ�ҵ���ʾ����
						List<TaskInfoDetail> mdatas = goodService
								.GetMsgInfoNumNewDetail2(mCurrentPersonName,
										strID);
						// ���»�ȡ�������ݺϲ���ԭ������
						CombineDynamicData(mdatas);
						bIsFirstStart = false;
					}
					Message msg = myhandler.obtainMessage();
					msg.what = 1;
					myhandler.sendMessage(msg);
					// ÿ��1��ˢ��һ�ν���
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
		// �ҵ�
		if (1 == mIsShowingWindow) {
			linearlayout2.setVisibility(View.GONE);
			linearlayout3.setVisibility(View.VISIBLE);
			mPullToRefreshView1.setVisibility(View.GONE);
			mListView2.setVisibility(View.VISIBLE);
			emptytextview.setVisibility(View.VISIBLE);
			linearlayout5.setVisibility(View.GONE);
			linearlayout6.setVisibility(View.GONE);

		} else if (2 == mIsShowingWindow) {// ����
			linearlayout2.setVisibility(View.VISIBLE);
			linearlayout3.setVisibility(View.GONE);
			mPullToRefreshView1.setVisibility(View.VISIBLE);
			linearlayout5.setVisibility(View.GONE);
			linearlayout6.setVisibility(View.GONE);
		} else if (3 == mIsShowingWindow) {// ����
			linearlayout2.setVisibility(View.VISIBLE);
			linearlayout3.setVisibility(View.GONE);

			mPullToRefreshView1.setVisibility(View.VISIBLE);
			linearlayout5.setVisibility(View.GONE);
			linearlayout6.setVisibility(View.GONE);
		} else if (4 == mIsShowingWindow) {// ����
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

		// ����û�û�е�¼
		if (mCurrentPersonName.equals("")) {
			mPersonInfoLinearLayout.setVisibility(View.GONE);
			mLoginTextView.setVisibility(View.VISIBLE);
			mLoginTextView.setText("��¼");
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
			// ���ñ���ɫ
			morelayout.setBackgroundColor(Color.rgb(41, 36, 33));
		} else if (2 == mIsShowingWindow) {
			morelayout.setBackgroundColor(Color.rgb(41, 36, 33));
		} else if (3 == mIsShowingWindow) {
			morelayout.setBackgroundColor(Color.rgb(41, 36, 33));
		} else if (4 == mIsShowingWindow) {
			morelayout.setBackgroundColor(Color.rgb(82, 82, 82));
		}

		// �������ؼ�
		my_textview1 = (TextView) findViewById(R.id.more_opera_Accounttextview1);
		my_imageview1 = (ImageView) findViewById(R.id.more_opera_imageview1);
		my_textview2 = (TextView) findViewById(R.id.more_opera_credittextview2);
		my_textview3 = (TextView) findViewById(R.id.more_opera_charmtextview3);
		my_imageview2 = (ImageView) findViewById(R.id.more_opera_imageview2);

		if (mCurrentPersonName.equals("")) {
			my_textview1.setText("�����¼");
			my_textview1.setTextColor(Color.rgb(0, 135, 255));
			my_imageview2.setImageBitmap(null);
		} else {
			my_textview1.setTextColor(Color.rgb(0, 0, 0));
			my_textview1.setText(mCurrentPersonNameTemp);
			my_textview2.setText("��Ʒ:");
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
		// ��������β
		ShowWindow();
		myinfolinearlayout = (LinearLayout) findViewById(R.id.activity_renpin_myinfolinearlayout);

		morelayout.setOnClickListener(new RelativeLayout.OnClickListener() {

			Runnable run1 = new Runnable() {
				public void run() {
					// ��ȡ�û��޸���
					int nCharm = goodService.GetCharmValue(mCurrentPersonName);
					int nCredit = goodService
							.GetCreditValue(mCurrentPersonName);
					Message msg = myhandler.obtainMessage();
					Bundle b = new Bundle();// �������
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
				// ���ñ���ɫ
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
				// �ж��Ƿ��¼
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
				// ��ʾ�ҵĽ���
				startActivity(intent);
			}

		});

		mMoreTextView.setOnClickListener(new OnClickListener() {

			Runnable run1 = new Runnable() {
				public void run() {
					// ��ȡ�û��޸���
					int nCharm = goodService.GetCharmValue(mCurrentPersonName);
					int nCredit = goodService
							.GetCreditValue(mCurrentPersonName);
					Message msg = myhandler.obtainMessage();
					Bundle b = new Bundle();// �������
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
				// ���ñ���ɫ
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
				// tasktextview.setText("����");
				linearlayout3.setVisibility(View.GONE);
				mPullToRefreshView1.setVisibility(View.VISIBLE);
				// ����Ǵ�����һ�������л�������,��ô��Ҫ������������
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
				// tasktextview.setText("����");
				linearlayout3.setVisibility(View.GONE);
				mPullToRefreshView1.setVisibility(View.VISIBLE);
				// ����Ǵ�����һ�������л�������,��ô��Ҫ������������
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

		// "�ҵ�"listview��ѡ���ɾ��
		mListView2.setOnItemLongClickListener(new OnItemLongClickListener() {

			// ɾ���Ի���
			private void dialog(AdapterView<?> l, final View view,
					final int position) {

				ListView listView1 = (ListView) l;
				@SuppressWarnings("unchecked")
				final HashMap<String, Object> map = (HashMap<String, Object>) listView1
						.getItemAtPosition(position);

				AlertDialog.Builder builder = new Builder(
						RenPinMainActivity.this);
				builder.setMessage("ȷ��Ҫɾ��������?");
				builder.setTitle("��ʾ");
				builder.setPositiveButton("ȷ��",
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
										// ɾ�����ݿ��е�����
										int nRet = goodService
												.DeleteMineDynamicInfo(
														strTaskId, strType,
														strPersonName);
										Message msg = myhandler.obtainMessage();
										Bundle b = new Bundle();// �������
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
				builder.setNegativeButton("ȡ��",
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
		// ����б��е�ĳһ��
		mListView2.setOnItemClickListener(new OnItemClickListener() {
			String strCustomerName;// �����û�����
			String strTaskAnnounceTime;
			String strTimeLimit;
			String strTaskTitle;
			String strDetail;
			String strTaskRunSeconds;
			String strTaskId;
			String strImplementName;// ����������
			String strCustomerNameTemp;// �����û��ǳ�
			String strImplementNameTemp;// �������ǳ�
			int nTimeStatus = 1;
			int nImpleStatus = 1;
			int nTaskType = 0;

			int nTaskSelectType;
			int nTaskFinishType;
			int nTaskVerifiType;
			int nTaskAnnounceCommentType;
			int nTaskImplementCommentType;
			// �����߶�ִ���ߵ�����
			String strAnnounceComment;
			// �����߸�ִ���ߵ�ͼƬ
			String strAnnounceBase64Image;
			// ִ���߶Է����ߵ�����
			String strImpleComment;
			// ִ���߸������ߵ�ͼƬ
			String strImpleBase64Image;
			int nCharmValue = 0;
			int nCommentNum = 0;
			int nBrowseNum = 0;
			// ������ַ
			String strAddressRegion = "";

			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				ListView listView1 = (ListView) adapterView;
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) listView1
						.getItemAtPosition(position);

				// �����������,��ô��˵�������״̬���û�������,��Ҫȡ�����״̬
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
							"���ȵ�¼", Toast.LENGTH_LONG);
					return;
				}

				// ���������������ʾ�򽫸����������еĺ��ȥ��
				CancleRedHot(strTaskId, nTaskType);

				Intent it = null;
				if (strTaskId.equals("")) {
					it = new Intent(RenPinMainActivity.this,
							ShareKingActivity.class);
					String strBaseImage = mUtils
							.BitmapToBase64BySize(mAnnounceImage);
					// ��������
					it.putExtra("com.renpin.RenPinMainActivity.customername",
							strCustomerName);
					// �����ǳ�
					it.putExtra(
							"com.renpin.RenPinMainActivity.strCustomerNameTemp",
							strCustomerNameTemp);
					// ����ͷ��
					it.putExtra("com.renpin.RenPinMainActivity.mAnnounceImage",
							strBaseImage);
					// ʱ��
					it.putExtra(
							"com.renpin.RenPinMainActivity.strAddressRegion",
							strAddressRegion);
					// ��������
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

					// ��̬����
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
				//�жϵ�ǰ�Ƿ����û���¼
				String strPersonName = msettings
						.getString("TruePersonName", "");
				if(strPersonName.equals("")){
					emptytextview.setText("���ȵ�¼Ŷ!");
				}else{
					emptytextview.setText("��û������,ȥ����һ�����������۱��˰�!");
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
				// tasktextview.setText("����");
				linearlayout3.setVisibility(View.GONE);
				mPullToRefreshView1.setVisibility(View.VISIBLE);
				// ����Ǵ�����һ�������л�������,��ô��Ҫ������������
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
				// ���ñ���ɫ
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
				// tasktextview.setText("����");
				linearlayout3.setVisibility(View.GONE);
				mPullToRefreshView1.setVisibility(View.VISIBLE);
				// ���������û������,��ô�Ͳ��ܽ�����������,�������µĲ���,����������ÿ�ε��
				// ��ʱ��Ҫȥ�Զ�������
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
				// ���ñ���ɫ
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
				// �������������
				Intent it = new Intent(RenPinMainActivity.this,
						AdviceActivity.class);
				startActivity(it);
			}
		});

		aboutustextview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// �������������
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
					// �����ǰ�汾�ȷ������汾��
					if (localVersion < Integer.parseInt(mupdateinfo
							.getVersion())) {
						// �����°汾����ʾ�û�����
						AlertDialog.Builder alert = new AlertDialog.Builder(
								RenPinMainActivity.this);
						alert.setTitle("���°汾����")
								.setMessage(strDescript)
								.setPositiveButton("��������",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												// �����ļ�
												FileUtil.createFile(getResources()
														.getString(
																R.string.app_name1));
												createNotification();
												createThread();
											}
										})
								.setNegativeButton("�Ժ���˵",
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
								"����ǰ�İ汾Ϊ���°汾,����Ҫ����", Toast.LENGTH_LONG);
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
							"���ȵ�¼", Toast.LENGTH_LONG);
				}
			}

		});

		nearbutton.setOnClickListener(new OnClickListener() {

			Runnable nearclickrun = new Runnable() {
				public void run() {
					// ���Ϊ����
					if (2 == mIsShowingWindow) {
						// ��ȡ���а�ʱ���������������
						ReadHelpDistanceData();
					} else if (3 == mIsShowingWindow) {// Ϊ����
						// ��ȡ���а�ʱ������ķ�������
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
					// ���Ϊ����
					if (2 == mIsShowingWindow) {
						// ��ȡ��ʱ���������������
						ReadHelpData();
					} else if (3 == mIsShowingWindow) {// Ϊ����
						// ��ȡ��ʱ������ķ�������
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
				String strCustomerNameTemp;// ����������
				String strTaskAnnounceTime;// ����ʱ��
				String strTimeLimit;// ʱ������
				String strTaskTitle;// �������
				String strDetail;// ������ϸ����
				String strTaskRunSeconds;// ����ִ��ʱ��(��)
				String strTaskId;// ����id
				String strImplementName;// ����ִ��������
				String strCustomerNameNick;// �������ǳ�
				String strImplementNick;// ����ִ�����ǳ�
				int nTimeStatus = 1;// ʱ��״̬
				int nImpleStatus = 1;// ���״̬
				String strTaskType;// ��������
				// int nCreditValue = 1;// ���Ƶ���Ʒֵ
				// int nCharmValue = 0;// ���Ƶ���ֵ

				int nTaskType = 0;
				int nTaskAnnounceCommentType;
				int nTaskImplementCommentType;

				int nCharmValue = 0;
				int nCommentNum = 0;
				int nBrowseNum = 0;
				// ���������ַ
				String strAddressRegion = "";

				String strPersonName = msettings
						.getString("TruePersonName", "");
				// ���û�е�¼,��ô���л�����¼����
				if (strPersonName.equals("")) {
					Intent intent = new Intent(RenPinMainActivity.this,
							LoginActivity.class);
					startActivityForResult(intent, 20);
					return;
				}

				if (1 == mnDataType) {
					DistanceDetail task = null;
					if (2 == mIsShowingWindow) {// ���������������
						task = mHelpTaskDataDistance.get(position);
					} else if (3 == mIsShowingWindow) {// ������ڷ�������
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

						// ��̬����
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
					if (2 == mIsShowingWindow) {// ���������������
						task = mHelpTaskData.get(position);
					} else if (3 == mIsShowingWindow) {// ������ڷ�������
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

						// ��̬����
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
			// ���Ϊϵͳ��Ϣ
			if (strTaskId.equals("")) {

				int nAnnounceCommentType = mDynamicNew.get(0)
						.getnTaskAnnounceCommentType();
				if (1 == nAnnounceCommentType) {
					// ���֪ͨ����Ϣ
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
							// �ж��Ƿ��и���
							if (2 == nSelectType || 2 == nFinishType
									|| 2 == nVerifiType
									|| 2 == nAnnounceCommentType
									|| 2 == nImpleCommentType) {

								// ���֪ͨ����Ϣ
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

	// ���nTypeΪ0��ʾ����Ҫ�����б�������,Ϊ1��ʾ��Ҫ
	private void ShowNearHelpDataForClick(final int nType) {
		Runnable rundata = new Runnable() {
			public void run() {
				if (mHelpTaskDataDistance != null) {
					// �����ʱ������û������,��ô�ͼ���
					if (mHelpTaskDataDistance.size() <= 0) {
						// һ���Ը���mnVolume������
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
								// ����������
								// mnMaxDistanceForHelpNear = ndistance;
								mnOldTimeForHelp = strTime;
							}
						}
					}
				} else {// ��ʱҲ��������
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
							// ����������
							// mnMaxDistanceForHelpNear = ndistance;
							mnOldTimeForHelp = strTime;
						}
					}
				}

				Message msg = myhandler.obtainMessage();
				Bundle b = new Bundle();// �������
				b.putInt("nType", nType);
				msg.setData(b);
				msg.what = 5;
				myhandler.sendMessage(msg);
			}
		};
		// ������ȡ���������߳�
		// Thread thread = new Thread(rundata);
		// thread.start();
	}

	private void ShowNearShareDataForClick(final int nType) {
		Runnable rundata = new Runnable() {
			public void run() {
				if (mShareTaskDataDistance != null) {
					// �����ʱ������û������,��ô�ͼ���
					if (mShareTaskDataDistance.size() <= 0) {
						// һ���Ը���mnVolume������
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
								// ������ɵ�����id
								// mnMaxDistanceForShareNear = nDistance;
								mnOldTimeForShareNear = strTime;
							}
						}
					}
				} else {// ��ʱҲ��������
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
							// ������ɵ�����id
							// mnMaxDistanceForShareNear = nDistance;
							mnOldTimeForShareNear = strTime;
						}
					}
				}
				Message msg = myhandler.obtainMessage();
				Bundle b = new Bundle();// �������
				b.putInt("nType", nType);
				msg.setData(b);
				msg.what = 5;
				myhandler.sendMessage(msg);
			}
		};

		// ������ȡ���������߳�
		Thread thread = new Thread(rundata);
		thread.start();
	}

	// ���nTypeΪ0��ʾ����Ҫ�����б�������,Ϊ1��ʾ��Ҫ
	private void ShowSpaceHelpDataForClick(final int nType) {
		Runnable rundata = new Runnable() {
			public void run() {
				if (mHelpTaskData != null) {
					// �����ʱ������û������,��ô�ͼ���
					if (mHelpTaskData.size() <= 0) {
						// һ���Ը���mnVolume������
						mHelpTaskData = goodService.UpdateTaskDataForLimit(
								nMaxDataLine, 1, 0);
						if (mHelpTaskData != null) {
							int nSize = mHelpTaskData.size();
							if (nSize > 0) {
								int nIndex = 0;
								nIndex = Integer.parseInt(mHelpTaskData
										.get(nSize - 1).getmstrId().toString());
								// ������ɵ�����id
								mnHelpTaskMaxOldIndex = nIndex;
								// �������µ�����id
								nIndex = Integer.parseInt(mHelpTaskData.get(0)
										.getmstrId().toString());
								mnHelpTaskMaxNewIndex = nIndex;
							}
						}
					}
				} else {// ��ʱҲ��������
					mHelpTaskData = goodService.UpdateTaskDataForLimit(
							nMaxDataLine, 1, 0);
					if (mHelpTaskData != null) {
						int nSize = mHelpTaskData.size();
						if (nSize > 0) {
							int nIndex = 0;
							nIndex = Integer.parseInt(mHelpTaskData
									.get(nSize - 1).getmstrId().toString());
							// ������ɵ�����id
							mnHelpTaskMaxOldIndex = nIndex;
							// �������µ�����id
							nIndex = Integer.parseInt(mHelpTaskData.get(0)
									.getmstrId().toString());
							mnHelpTaskMaxNewIndex = nIndex;
						}
					}
				}

				Message msg = myhandler.obtainMessage();
				Bundle b = new Bundle();// �������
				b.putInt("nType", nType);
				msg.setData(b);
				msg.what = 5;
				myhandler.sendMessage(msg);
			}
		};
		// ������ȡ���������߳�
		// Thread thread = new Thread(rundata);
		// thread.start();
	}

	private void ShowSpaceShareDataForClick(final int nType) {
		Runnable rundata = new Runnable() {
			public void run() {
				if (mShareTaskData != null) {
					// �����ʱ������û������,��ô�ͼ���
					if (mShareTaskData.size() <= 0) {
						// һ���Ը���mnVolume������
						mShareTaskData = goodService.UpdateTaskDataForLimitNew(
								nMaxDataLine, 2, mnNewTimeForShareAll);
						if (mShareTaskData != null) {
							int nSize = mShareTaskData.size();
							if (nSize > 0) {
								String strTime = mShareTaskData.get(nSize - 1)
										.getmTaskAnnounceTime().toString();
								// ������ɵ�����id
								mnOldTimeForShareAll = strTime;
								// �������µ�����id
								strTime = mShareTaskData.get(0)
										.getmTaskAnnounceTime().toString();
								mnNewTimeForShareAll = strTime;
							}
						}
					}
				} else {// ��ʱҲ��������
					mShareTaskData = goodService.UpdateTaskDataForLimitNew(
							nMaxDataLine, 2, mnNewTimeForShareAll);
					if (mShareTaskData != null) {
						int nSize = mShareTaskData.size();
						if (nSize > 0) {
							String strTime = mShareTaskData.get(nSize - 1)
									.getmTaskAnnounceTime().toString();
							// ������ɵ�����id
							mnOldTimeForShareAll = strTime;
							// �������µ�����id
							strTime = mShareTaskData.get(0)
									.getmTaskAnnounceTime().toString();
							mnNewTimeForShareAll = strTime;
						}
					}
				}
				Message msg = myhandler.obtainMessage();
				Bundle b = new Bundle();// �������
				b.putInt("nType", nType);
				msg.setData(b);
				msg.what = 5;
				myhandler.sendMessage(msg);
			}
		};

		// ������ȡ���������߳�
		Thread thread = new Thread(rundata);
		thread.start();
	}

	public boolean isConnectInternet() {

		ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

		if (networkInfo != null) { // ����ж�һ��Ҫ���ϣ�Ҫ��Ȼ�����
			return networkInfo.isAvailable();
		}
		return false;
	}

	class MyHandler extends Handler {
		// ���������д�˷���,��������
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
					my_textview1.setText("�����¼");
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
					my_textview2.setText("��Ʒ:" + nCreditValue);

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
					// ��̬
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
					// �����һ�β⵽�Ǵ��ڶ���״̬��,��ôһ��������ôĬ��ȥˢ������
					if (!bNetWorkLinkStatus) {
						// �����������������е�����
						GetHelpOrShareData();
						bIsFirstStart = true;
					}
					bNetWorkLinkStatus = true;
				}
				// ��ˢ�±�־��Ϊ����ˢ��
				mbIsFinished = true;
				// �°汾������ʾ
				if (!bIsReminderUpdateApk && bDataFinishLoad) {
					if (mupdateinfo != null) {
						int nNewVersion = Integer.parseInt(mupdateinfo
								.getVersion());
						String strDescript = mupdateinfo.getDescription();
						// ������°汾,��ô����ʾ�������°汾
						if (localVersion < nNewVersion) {
							// �����°汾����ʾ�û�����
							AlertDialog.Builder alert = new AlertDialog.Builder(
									RenPinMainActivity.this);
							alert.setTitle("���°汾����")
									.setMessage(strDescript)
									.setPositiveButton(
											"��������",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													// �����ļ�
													FileUtil.createFile(getResources()
															.getString(
																	R.string.app_name1));
													createNotification();
													createThread();
												}
											})
									.setNegativeButton(
											"�Ժ���˵",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													dialog.dismiss();
												}
											});
							alert.create().show();
							bIsReminderUpdateApk = true;
							versionupdateinfotextview.setText("���°汾");
						}
					}
				}
				if (mupdateinfo != null) {
					int nNewVersion = Integer
							.parseInt(mupdateinfo.getVersion());
					// ����ڸ������а汾������ʾ������,ͬʱ�汾�Ѿ�����,��ô�ͽ������ʾ����ȥ��
					if (!versionupdateinfotextview.getText().toString()
							.equals("")
							&& localVersion >= nNewVersion) {
						versionupdateinfotextview.setText("");
					}
				}
				break;
			case 2:// ���ϻ�������
				if (2 == mIsShowingWindow) {// ����
					if (1 == mnDataType) {
						if (mHelpTaskDataDistance != null) {
							// ����
							int nCount = mHelpTaskDataDistance.size();
							mListData = getListData(1, nCount);
							SetHelpAdapter(false);
						}
					} else if (2 == mnDataType) {
						if (mHelpTaskData != null) {
							// ����
							int nCount = mHelpTaskData.size();
							mListData = getListData(1, nCount);
							SetHelpAdapter(false);
						}
					}

				} else if (3 == mIsShowingWindow) {// ����
					if (1 == mnDataType) {
						if (mShareTaskDataDistance != null) {
							// ����
							int nCount2 = mShareTaskDataDistance.size();
							mListData2 = getListData(2, nCount2);
							SetShareAdapter(false);

						}
					} else if (2 == mnDataType) {
						if (mShareTaskData != null) {
							// ����
							int nCount2 = mShareTaskData.size();
							mListData2 = getListData(2, nCount2);
							SetShareAdapter(false);
						}
					}
				}
				mPullToRefreshView1.onFooterRefreshComplete();
				break;
			case 3:// ���»���ˢ��
				if (2 == mIsShowingWindow) {// ����
					if (1 == mnDataType) {
						if (mHelpTaskDataDistance != null) {
							// ����
							int nCount = mHelpTaskDataDistance.size();
							mListData = getListData(1, nCount);
							SetHelpAdapter(false);
						}
					} else if (2 == mnDataType) {
						if (mHelpTaskData != null) {
							// ����
							int nCount = mHelpTaskData.size();
							mListData = getListData(1, nCount);
							SetHelpAdapter(false);
						}
					}
				} else if (3 == mIsShowingWindow) {// ����
					if (1 == mnDataType) {
						if (mShareTaskDataDistance != null) {
							// ����
							int nCount2 = mShareTaskDataDistance.size();
							mListData2 = getListData(2, nCount2);
							SetShareAdapter(false);
						}
					} else if (2 == mnDataType) {
						if (mShareTaskData != null) {
							// ����
							int nCount2 = mShareTaskData.size();
							mListData2 = getListData(2, nCount2);
							SetShareAdapter(false);
						}
					}
				}
				mPullToRefreshView1.onHeaderRefreshComplete();
				break;
			case 4:// ���������ʱˢ��
				if (1 == mnDataType) {
					if (mHelpTaskDataDistance != null) {
						// ����
						int nCount = mHelpTaskDataDistance.size();
						mListData = getListData(1, nCount);
						if (2 == mIsShowingWindow) {
							SetHelpAdapter(true);
						} else {
							SetHelpAdapter(false);
						}
					}
					if (mShareTaskDataDistance != null) {
						// ����
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
						// ����
						int nCount = mHelpTaskData.size();
						mListData = getListData(1, nCount);
						if (2 == mIsShowingWindow) {
							SetHelpAdapter(true);
						} else {
							SetHelpAdapter(false);
						}
					}
					if (mShareTaskData != null) {
						// ����
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
			case 5:// �����л�ˢ��
				Bundle WindowChangeMsg = msg.getData();
				int nType = WindowChangeMsg.getInt("nType");
				if (2 == mIsShowingWindow) {// ����
					if (1 == mnDataType) {
						if (mHelpTaskDataDistance != null) {
							if (0 == nType) {// ������Ǵ�����һ�������л�����������
								// ����
								int nCount = mHelpTaskDataDistance.size();
								mListData = getListData(1, nCount);
								SetHelpAdapter(false);
							} else {
								// ����
								int nCount = mHelpTaskDataDistance.size();
								mListData = getListData(1, nCount);
								SetHelpAdapter(true);
							}
						} else {// ���û�л�ȡ������,�ڴ����л���ʱ��ҲӦ������Ӧ���л�
							if (0 == nType) {// ������Ǵ�����һ�������л�����������
								// ����
								SetHelpAdapter(false);
							} else {
								// ����
								SetHelpAdapter(true);
							}
						}
					} else if (2 == mnDataType) {
						if (mHelpTaskData != null) {
							if (0 == nType) {// ������Ǵ�����һ�������л�����������
								// ����
								int nCount = mHelpTaskData.size();
								mListData = getListData(1, nCount);
								SetHelpAdapter(false);
							} else {
								// ����
								int nCount = mHelpTaskData.size();
								mListData = getListData(1, nCount);
								SetHelpAdapter(true);
							}
						} else {// ���û�л�ȡ������,�ڴ����л���ʱ��ҲӦ������Ӧ���л�
							if (0 == nType) {// ������Ǵ�����һ�������л�����������
								// ����
								SetHelpAdapter(false);
							} else {
								// ����
								SetHelpAdapter(true);
							}
						}
					}
				} else if (3 == mIsShowingWindow) {// ����
					if (1 == mnDataType) {
						if (mShareTaskDataDistance != null) {
							if (0 == nType) {// ������Ǵ�����һ�������л�����������
								// ����
								int nCount2 = mShareTaskDataDistance.size();
								mListData2 = getListData(2, nCount2);
								SetShareAdapter(false);
							} else {
								if (mShareTaskDataDistance != null) {
									// ����
									int nCount2 = mShareTaskDataDistance.size();
									mListData2 = getListData(2, nCount2);
									SetShareAdapter(true);
								}
							}
						} else {
							if (0 == nType) {// ������Ǵ�����һ�������л�����������
								// ����
								SetShareAdapter(false);
							} else {
								// ����
								SetShareAdapter(true);
							}
						}
					} else if (2 == mnDataType) {
						if (mShareTaskData != null) {
							if (0 == nType) {// ������Ǵ�����һ�������л�����������
								// ����
								int nCount2 = mShareTaskData.size();
								mListData2 = getListData(2, nCount2);
								SetShareAdapter(false);
							} else {
								if (mShareTaskData != null) {
									// ����
									int nCount2 = mShareTaskData.size();
									mListData2 = getListData(2, nCount2);
									SetShareAdapter(true);
								}
							}
						} else {
							if (0 == nType) {// ������Ǵ�����һ�������л�����������
								// ����
								SetShareAdapter(false);
							} else {
								// ����
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
						strMsg1 = "����ʧ��";
					} else if (1 == nRet) {
						strMsg1 = "�����ɹ�";
						// �����޵���ֵ
						UpdateTaskOrShareCharmValue(strId, strType);
					} else if (2 == nRet) {
						strMsg1 = "�Ѿ��޹�";
					} else if (3 == nRet) {
						strMsg1 = "���ܸ��Լ���Ŷ";
					}
					CommonUtils.ShowToastCenter(getBaseContext(), strMsg1,
							Toast.LENGTH_LONG);
				}

				break;
			case 7:
				if (2 == mIsShowingWindow) {// ����
					if (1 == mnDataType) {
						if (mHelpTaskDataDistance != null) {
							// ����
							int nCount = mHelpTaskDataDistance.size();
							mListData = getListData(1, nCount);
							SetHelpAdapter(false);
						} else {
							// ����
							SetHelpAdapter(false);
						}
					} else if (2 == mnDataType) {
						if (mHelpTaskData != null) {
							// ����
							int nCount = mHelpTaskData.size();
							mListData = getListData(1, nCount);
							SetHelpAdapter(false);
						} else {
							// ����
							SetHelpAdapter(false);
						}
					}
				} else if (3 == mIsShowingWindow) {// ����
					if (1 == mnDataType) {
						if (mShareTaskDataDistance != null) {
							// ����
							int nCount2 = mShareTaskDataDistance.size();
							mListData2 = getListData(2, nCount2);
							SetShareAdapter(false);
						} else {
							// ����
							SetShareAdapter(false);
						}
					} else if (2 == mnDataType) {
						if (mShareTaskData != null) {
							// ����
							int nCount2 = mShareTaskData.size();
							mListData2 = getListData(2, nCount2);
							SetShareAdapter(false);

						} else {
							// ����
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
					// ɾ����̬�����е�����
					DeleteDynamicData(strTaskid, nType);
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"ɾ���ɹ�", Toast.LENGTH_LONG);
				} else {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"ɾ��ʧ��", Toast.LENGTH_LONG);
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
					// ɾ������
					mDynamicNew.remove(i);
					break;
				}
			}
		}
	}

	// ������ֵ
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
						// ����
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
						// ����
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
						// ����
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
						// ����
						int nCount2 = mShareTaskData.size();
						mListData2 = getListData(2, nCount2);
						SetShareAdapter(false);
						break;
					}
				}
			}
		}
	}

	// ���������
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
						// ����
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
						// ����
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
						// ����
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
						// ����
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
			int nCharmValue = 0;// ��ֵ
			int nCommentNum = 0;// ��������
			int nBrowseNum = 0;// �������

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

			String strDistance = ""; // �����������񷢲�����ľ���
			if (MyLocation.mLatitude != -1) {
				distance = getDistance(dLongitude, Latidude,
						MyLocation.mLongitude, MyLocation.mLatitude); // �������1km
				if (distance < 1) { // �������100m
					if (distance * 1000 < 100) {
						strDistance = "< 100 m";
					} else {
						strDistance = (int) (distance * 1000) + "m";
					}

				} else {
					strDistance = (int) distance + "km";
				}
			}
			// ����ȡ�������ݴ洢��������
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
			// �����߶�ִ���ߵ�����
			String strAnnounceComment;
			// �����߸�ִ���ߵ�ͼƬ
			String strAnnounceBase64Image;
			// ִ���߶Է����ߵ�����
			String strImpleComment;
			// ִ���߸������ߵ�ͼƬ
			String strImpleBase64Image;
			// ��ֵ
			int nCharmValue = 0;
			// ������
			int nCommentNum = 0;
			// �����
			int nBrowseNum = 0;
			// ������ַ
			String strAddressRegion = "";

			strTaskId = mDynamicNew.get(i).getmstrId();
			// �ж�����������Ϣ�Ƿ��뵱ǰ�û��й�ϵ
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
			// �����������񷢲�����ľ���
			if (MyLocation.mLatitude != -1) {
				// �������1km
				if (distance <= 1) {
					// �������100m
					if (distance * 1000 < 100) {
						strDistance = "< 100 m";
					} else {
						strDistance = (int) (distance * 1000) + "m";
					}

				} else {
					strDistance = (int) distance + "km";
				}
			}
			// ����ȡ�������ݴ洢��������
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
			// ������������
			TextView regionname = (TextView) convertView
					.findViewById(R.id.help_eachother_textview1);
			// �����������
			TextView regiondistance = (TextView) convertView
					.findViewById(R.id.help_eachother_textview2new);
			// ����ͼƬ
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.help_eachother_imageview1);
			// ����ͼƬ
			// ImageView imageView1 = (ImageView) convertView
			// .findViewById(R.id.help_eachother_imageview3);
			// ����������
			TextView customername = (TextView) convertView
					.findViewById(R.id.help_eachother_textview3);

			// ������������
			TextView detailtext = (TextView) convertView
					.findViewById(R.id.help_eachother_textview4);
			// ����״̬��Ϣ
			ImageView imageviewstatus = (ImageView) convertView
					.findViewById(R.id.help_eachother_imageview2);
			// ����ʱ��
			TextView accounttimetextview = (TextView) convertView
					.findViewById(R.id.help_eachother_timetextview);

			// ��ֵ
			LinearLayout praiselayout = (LinearLayout) convertView
					.findViewById(R.id.help_eachother_zanlayout);
			// ��ֵͼ��
			final ImageView zanimageview = (ImageView) convertView
					.findViewById(R.id.help_eachother_zanimageview);

			TextView praisetextview = (TextView) convertView
					.findViewById(R.id.help_eachother_zantextview);
			// ��������
			TextView commenttextview = (TextView) convertView
					.findViewById(R.id.help_eachother_commenttextview);
			// �������
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

			// ���񷢲�������
			final String strTaskPersonName = map.get("customername").toString();
			final String strId = map.get("taskid").toString();
			final String strType = map.get("tasktype").toString();
			
			Resources res = getResources();
			//�ж��Ƿ�����
			if(StoreZanMap.containsKey(strId)){
				// �жϸ������״̬
				Bitmap zanbmp = BitmapFactory.decodeResource(res, R.drawable.zan1);
				zanimageview.setImageBitmap(zanbmp);	
			}else{
				Bitmap zanbmp = BitmapFactory.decodeResource(res, R.drawable.zan01);
				zanimageview.setImageBitmap(zanbmp);
			}

			// ����ͼƬ�ļ�����(�����_��������_ͼƬ����)
			String strFileName = strId + "_" + strType + "_" + "1";
			new ImageDownloadTask().execute(strFileName, imageView, strId,
					strType);

			// �����޵����Ӧ
			praiselayout.setOnClickListener(new OnClickListener() {
				Runnable zanrun = new Runnable() {
					public void run() {
						int nRet = 0;
						// �Ȼ�ȡ��ǰ�û�����
						String strCurName = msettings.getString(
								"TruePersonName", "");
						if (strCurName.equals("")) {
							nRet = 4;// ��ʾû�е�¼
						} else {
							// �����Լ����Լ���
							if (!strTaskPersonName.equals(strCurName)) {

								nRet = goodService.PraiseToTaskOrShare(strId,
										strCurName, strTaskPersonName, strType);
								if(1 == nRet){
									//�ж������Ƿ񳬹�����������,��������˾����
									if(StoreZanMap.size() >= MaxStoreMap){
										StoreZanMap.clear();
									}
									StoreZanMap.put(strId, 1);
								}
							} else {
								nRet = 3;// ��ʾ���ܸ��Լ���
							}
						}
						Message msg = myhandler.obtainMessage();
						Bundle b = new Bundle();// �������
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
			// ��������
			if (2 == nTaskVerifiType || 3 == nTaskVerifiType) {
				Bitmap bmp = BitmapFactory.decodeResource(res,
						R.drawable.sign_finish);
				imageviewstatus.setImageBitmap(bmp);
			} else if (2 == nTaskSelectType || 3 == nTaskSelectType) {// ��������
				Bitmap bmp = BitmapFactory.decodeResource(res,
						R.drawable.sign_ondoing);
				imageviewstatus.setImageBitmap(bmp);
			} else if (nTaskTimeLimit - nTaskRunTime <= 0) {// ����
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
			// ������ͷ��
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.dynamic_item_imageview1);

			// ״̬ͼ��
			ImageView imageView1 = (ImageView) convertView
					.findViewById(R.id.dynamic_item_imageview2);

			// ����������
			TextView customername = (TextView) convertView
					.findViewById(R.id.dynamic_item_textview1);

			// �����������
			TextView detailtext = (TextView) convertView
					.findViewById(R.id.dynamic_item_textview2);

			// ���񷢲�ʱ��
			TextView tasktimetext = (TextView) convertView
					.findViewById(R.id.dynamic_item_textview3);

			// ״̬��Ϣ
			TextView statustextview = (TextView) convertView
					.findViewById(R.id.dynamic_item_textview4);
			// ��������
			TextView strtypetextview = (TextView) convertView
					.findViewById(R.id.dynamic_item_textview5);

			// ���Ϊϵͳ��Ϣ��
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

				customername.setText("ϵͳ��Ϣ");
				detailtext.setText("�����������");
				strtypetextview.setText("");
				statustextview.setText("");
			} else {// Ϊ������
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
				// �ж��Ƿ��и���
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
				// �ж������Ƿ����
				if (!IsShowTimeOut(nTaskType, map.get("taskid").toString(),
						mCurrentPersonName, map.get("taskimplementname")
								.toString())
						&& nTimeLimit - nRunTime <= 0) {
					strTaskStatus = "�ѹ���";
					if (2 == Integer.parseInt(map.get("tasktype").toString())) {
						strTaskStatus = "�ѹ���";
					}
				}

				customername.setText(" "
						+ map.get("customernametemp").toString() + ": ");
				detailtext.setText(map.get("tasktitle").toString());

				tasktimetext.setText(strTaskStatus);
				if (1 == nTaskType) {
					strtypetextview.setText("��������");
				} else if (2 == nTaskType) {
					strtypetextview.setText("��������");
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
						strTaskStatus = "�ȴ�������...";
						if (2 == nTaskType) {
							strTaskStatus = "�ȴ�������...";
						}
					} else {
						strTaskStatus = "��������ִ����";
						if (2 == nTaskType) {
							strTaskStatus = "��������ִ����";
						}
					}
					if (2 == mDynamicNew.get(i).getnTaskVerifiType()
							|| 3 == mDynamicNew.get(i).getnTaskVerifiType()) {
						strTaskStatus = "�����ۣ��������";
						if (2 == nTaskType) {
							strTaskStatus = "�����ۣ���������";
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
							strStatusInfo = "������Ϣ";
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
		// ���Ը��ݶ���������������Ӧ�Ĳ���
		if (20 == resultCode) {
			String PersonName = data.getExtras().getString("personname");
			int nCreditValue = data.getExtras().getInt("CreditValue");
			int nCharmValue = data.getExtras().getInt("CharmValue");
			my_textview1.setText(PersonName);
			my_textview1.setTextColor(Color.rgb(0, 0, 0));
			my_textview2.setText("��Ʒ:" + nCreditValue);
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
			// ������ѯ������
			PollingUtils.startPollingService(this, 20, UpdateDataService.class,
					UpdateDataService.ACTION);

			String strTemp = msettings.getString("TruePersonName", "");
			// ����л����û���ͬһ��,��ô��û��Ҫ��������
			if (!strTemp.equals(mCurrentPersonName)) {
				// �޸ĵ�ǰ�û�����
				mCurrentPersonNameTemp = msettings.getString("PersonName", "");
				mCurrentPersonName = msettings.getString("TruePersonName", "");
				if (mDynamicNew != null) {
					// ����̬��Ϣ���
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
				my_textview1.setText("�����¼");
				my_textview2.setText("��Ʒ:");
				my_textview3.setText("");

				Resources res = getResources();
				res = getResources();
				Bitmap bmp = BitmapFactory.decodeResource(res,
						R.drawable.noperson);
				my_imageview1.setImageBitmap(bmp);

				mPersonInfoLinearLayout.setVisibility(View.GONE);
				mLoginTextView.setVisibility(View.VISIBLE);
				mLoginTextView.setText("��¼");
			} else if (1 == resultCode) {
				// ��ʱ�п����޸���ͷ��,��ʱӦ�ø���ͷ��
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

	// ����ˢ�µ�����������
	private void DealUpdateHelpTaskData(List<TaskInfoDetail> data) {
		if (data != null) {
			int nCount = data.size();
			int nSize = mHelpTaskData.size();
			// �ж��¼�����������������������Ƿ񳬹���mnVolume,�����������ô����Ҫ���һЩ������
			if (nCount + nSize > mnVolume) {
				// ����µ�����
				List<TaskInfoDetail> newdata = new ArrayList<TaskInfoDetail>();
				// �Ƚ����µ����ݼ��뵽��������
				newdata.addAll(0, data);
				// �ж������л��ܲ��뼸������
				int nRemainDatas = mnVolume - nCount;
				int nOldTaskId = 0;
				// ������ܲ���
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
				// ��mHelpTaskData���
				mHelpTaskData.clear();
				// ���½����ݸ�ֵ����������
				mHelpTaskData.addAll(0, newdata);
				// �޸��������id��
				mnHelpTaskMaxOldIndex = nOldTaskId;
				newdata.removeAll(newdata);
			} else {
				// ����µ�����
				List<TaskInfoDetail> newdata = new ArrayList<TaskInfoDetail>();
				// �Ƚ����µ����ݼ��뵽��������
				newdata.addAll(0, data);
				// ��������׷�ӵ���������
				newdata.addAll(data.size(), mHelpTaskData);
				// ��mHelpTaskData���
				mHelpTaskData.clear();
				// ���½����ݸ�ֵ����������
				mHelpTaskData.addAll(0, newdata);
				newdata.removeAll(newdata);
			}
		}
	}

	// ����ˢ�µ��ķ�������
	private void DealUpdateShareTaskData(List<TaskInfoDetail> data) {
		if (data != null) {
			int nCount = data.size();
			int nSize = mShareTaskData.size();
			// �ж��¼�����������������������Ƿ񳬹���mnVolume,�����������ô����Ҫ���һЩ������
			if (nCount + nSize > nMaxDataLine) {
				// ����µ�����
				List<TaskInfoDetail> newdata = new ArrayList<TaskInfoDetail>();
				// �Ƚ����µ����ݼ��뵽��������
				newdata.addAll(0, data);
				// �ж������л��ܲ��뼸������
				int nRemainDatas = nMaxDataLine - nCount;
				String strOldTaskTime = "";
				// ������ܲ���
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
				// ��mHelpTaskData���
				mShareTaskData.clear();
				// ���½����ݸ�ֵ����������
				mShareTaskData.addAll(0, newdata);
				// �޸��������id��
				mnOldTimeForShareAll = strOldTaskTime;
				newdata.removeAll(newdata);
			} else {
				// ����µ�����
				List<TaskInfoDetail> newdata = new ArrayList<TaskInfoDetail>();
				// �Ƚ����µ����ݼ��뵽��������
				newdata.addAll(0, data);
				// ��������׷�ӵ���������
				newdata.addAll(data.size(), mShareTaskData);
				// ��mHelpTaskData���
				mShareTaskData.clear();
				// ���½����ݸ�ֵ����������
				mShareTaskData.addAll(0, newdata);
				newdata.removeAll(newdata);
			}
		}
	}

	// �������ص�����������
	private void DealLoadNearHelpTaskData(List<DistanceDetail> data) {
		int nCount = data.size();
		int nSize = mHelpTaskDataDistance.size();
		int nRemainCount = nCount + nSize - mnVolume;
		// ����������������������������,��ô��Ҫȥ���������������
		if (nRemainCount > 0) {
			// ȥ��nRemainCount������
			for (int i = 0; i < nRemainCount; i++) {
				mHelpTaskDataDistance.remove(0);
			}
			mHelpTaskDataDistance.addAll(nSize - nRemainCount, data);
		} else {// �������û�г������ֵ
				// �������������ӵ�������
			mHelpTaskDataDistance.addAll(nSize, data);
		}
	}

	// �������ص��ķ�������
	private void DealLoadNearShareTaskData(List<DistanceDetail> data) {

		int nCount = data.size();
		int nSize = mShareTaskDataDistance.size();
		int nRemainCount = nCount + nSize - mnVolume;
		// ����������������������������,��ô��Ҫȥ��������������
		if (nRemainCount > 0) {
			// ȥ��nRemainCount������
			for (int i = 0; i < nRemainCount; i++) {
				mShareTaskDataDistance.remove(0);
			}
			// �������������ӵ�������
			mShareTaskDataDistance.addAll(nSize - nRemainCount, data);
		} else {// �������û�г������ֵ
			// �������������ӵ�������
			mShareTaskDataDistance.addAll(nSize, data);
		}
	}

	// �������ص�����������
	private void DealLoadSpaceHelpTaskData(List<TaskInfoDetail> data) {
		int nCount = data.size();
		int nSize = mHelpTaskData.size();
		int nRemainCount = nCount + nSize - mnVolume;
		// ����������������������������,��ô��Ҫȥ��������������
		if (nRemainCount > 0) {
			// ȥ��nRemainCount������
			for (int i = 0; i < nRemainCount; i++) {
				mHelpTaskData.remove(0);
			}
			// �������������ӵ�������
			mHelpTaskData.addAll(nSize - nRemainCount, data);
			// �޸����µ�����id
			mnHelpTaskMaxNewIndex = Integer.parseInt(mHelpTaskData.get(0)
					.getmstrId());
		} else {// �������û�г������ֵ
				// �������������ӵ�������
			mHelpTaskData.addAll(nSize, data);
		}
	}

	// �������ص��ķ�������
	private void DealLoadSpaceShareTaskData(List<TaskInfoDetail> data) {

		int nCount = data.size();
		int nSize = mShareTaskData.size();
		int nRemainCount = nCount + nSize - mnVolume;
		// ����������������������������,��ô��Ҫȥ��������������
		if (nRemainCount > 0) {
			// ȥ��nRemainCount������
			for (int i = 0; i < nRemainCount; i++) {
				mShareTaskData.remove(0);
			}
			// �������������ӵ�������
			mShareTaskData.addAll(nSize - nRemainCount, data);
			// �޸����µ�����id
			mnNewTimeForShareAll = mShareTaskData.get(0).getmTaskAnnounceTime();
		} else {// �������û�г������ֵ
			// �������������ӵ�������
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
						// �������ͬ������
						if (data.get(i)
								.getmstrId()
								.equals(mHelpTaskDataDistance.get(j)
										.getmstrId())) {
							// ����ͬ������ɾ��
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
						// �������ͬ������
						if (data.get(i)
								.getmstrId()
								.equals(mShareTaskDataDistance.get(j)
										.getmstrId())) {
							// ����ͬ������ɾ��
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
		// ����
		Runnable rundata = new Runnable() {
			public void run() {
				if (2 == mIsShowingWindow) {
					if (1 == mnDataType) {
						// ����mnVolume������
						List<DistanceDetail> HelpTaskDataTemp = goodService
								.LoadHelpNearData(MyLocation.mLongitude,
										MyLocation.mLatitude,
										(double) mnMaxDistance,
										strDistrictName, mnOldTimeForHelp);
						if (HelpTaskDataTemp != null) {
							int nSize = HelpTaskDataTemp.size();
							// �����ѯ��������
							if (nSize > 0) {
								int nDistance = 0;
								String strTime;
								nDistance = HelpTaskDataTemp.get(nSize - 1)
										.getnDistance();
								strTime = HelpTaskDataTemp.get(nSize - 1)
										.getmTaskAnnounceTime();
								// �������¾ɵ�����id
								// mnMaxDistanceForHelpNear = nDistance;
								mnOldTimeForHelp = strTime;
								// ��HelpTaskDataTemp����ȥ�ش���
								HelpTaskDataTemp = DeleteSameDataForHelp(HelpTaskDataTemp);
								// ����
								DealLoadNearHelpTaskData(HelpTaskDataTemp);
							}
						}
					} else if (2 == mnDataType) {
						// ����mnVolume������
						List<TaskInfoDetail> HelpTaskDataTemp = goodService
								.LoadTaskDataForLimitNew(nMaxDataLine, 1,
										mnOldTimeForShareAll);
						if (HelpTaskDataTemp != null) {
							int nSize = HelpTaskDataTemp.size();
							// �����ѯ��������
							if (nSize > 0) {
								String strTime = "";
								strTime = HelpTaskDataTemp.get(nSize - 1)
										.getmTaskAnnounceTime().toString();
								// �������¾ɵ�����id
								mnOldTimeForShareAll = strTime;
								// ����listview�е�����,�޸���Ӧ������
								DealLoadSpaceHelpTaskData(HelpTaskDataTemp);
							}
						}
					}
				} else if (3 == mIsShowingWindow) {// ��ʱҲ��������
					if (1 == mnDataType) {
						// ����mnVolume������
						List<DistanceDetail> ShareTaskDataTemp = goodService
								.LoadShareNearData(MyLocation.mLongitude,
										MyLocation.mLatitude,
										(double) mnMaxDistance,
										strDistrictName, mnOldTimeForShareNear);
						if (ShareTaskDataTemp != null) {
							int nSize = ShareTaskDataTemp.size();
							// �����ѯ��������
							if (nSize > 0) {
								int nDistance = 0;
								String strTime;
								nDistance = ShareTaskDataTemp.get(nSize - 1)
										.getnDistance();
								strTime = ShareTaskDataTemp.get(nSize - 1)
										.getmTaskAnnounceTime();
								// �������µ�����id
								// mnMaxDistanceForShareNear = nDistance;
								mnOldTimeForShareNear = strTime;
								ShareTaskDataTemp = DeleteSameDataForShare(ShareTaskDataTemp);
								// ����
								DealLoadNearShareTaskData(ShareTaskDataTemp);
							}
						}
					} else if (2 == mnDataType) {
						// ����mnVolume������
						List<TaskInfoDetail> ShareTaskDataTemp = goodService
								.LoadTaskDataForLimitNew(nMaxDataLine, 2,
										mnOldTimeForShareAll);
						if (ShareTaskDataTemp != null) {
							int nSize = ShareTaskDataTemp.size();
							// �����ѯ��������
							if (nSize > 0) {
								String strTime = "";
								strTime = ShareTaskDataTemp.get(nSize - 1)
										.getmTaskAnnounceTime().toString();
								// �������µ�����id
								mnOldTimeForShareAll = strTime;
								// ����listview�е�����,�޸���Ӧ������
								DealLoadSpaceShareTaskData(ShareTaskDataTemp);
							}
						}
					}
				}
				// ������Ϣ,�����ݸ���
				Message msg = myhandler.obtainMessage();
				msg.what = 2;
				myhandler.sendMessage(msg);
			}
		};
		// ������ȡ���������߳�
		Thread thread = new Thread(rundata);
		thread.start();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// ˢ��
		Runnable rundata = new Runnable() {
			public void run() {
				if (2 == mIsShowingWindow) {
					if (1 == mnDataType) {
						// ����mnVolume������
						mHelpTaskDataDistance = goodService.getHelpNearData(
								MyLocation.mLongitude, MyLocation.mLatitude,
								(double) mnMaxDistance, strDistrictName);
						if (mHelpTaskDataDistance != null) {
							int nSize = mHelpTaskDataDistance.size();
							// �����ѯ��������
							if (nSize > 0) {
								int ndistance = 0;
								String strTime;
								ndistance = mHelpTaskDataDistance
										.get(nSize - 1).getnDistance();
								strTime = mHelpTaskDataDistance.get(nSize - 1)
										.getmTaskAnnounceTime();
								// ����������
								// mnMaxDistanceForHelpNear = ndistance;
								mnOldTimeForHelp = strTime;
								// ����listview�е�����,�޸���Ӧ������
								// DealUpdateHelpTaskData(HelpTaskDataTemp);
							}
						}
					} else if (2 == mnDataType) {
						// ����mnVolume������
						List<TaskInfoDetail> HelpTaskDataTemp = goodService
								.UpdateTaskDataForLimit(nMaxDataLine, 1,
										mnHelpTaskMaxNewIndex);
						if (HelpTaskDataTemp != null) {
							int nSize = HelpTaskDataTemp.size();
							// �����ѯ��������
							if (nSize > 0) {
								int nIndex = 0;
								nIndex = Integer.parseInt(HelpTaskDataTemp
										.get(0).getmstrId().toString());
								// �������µ�����id
								mnHelpTaskMaxNewIndex = nIndex;
								// ����listview�е�����,�޸���Ӧ������
								DealUpdateHelpTaskData(HelpTaskDataTemp);
							}
						}
					}
				} else if (3 == mIsShowingWindow) {// ��ʱҲ��������
					if (1 == mnDataType) {
						// ����mnVolume������
						mShareTaskDataDistance = goodService.getShareNearData(
								MyLocation.mLongitude, MyLocation.mLatitude,
								(double) mnMaxDistance, strDistrictName);
						if (mShareTaskDataDistance != null) {
							int nSize = mShareTaskDataDistance.size();
							// �����ѯ��������
							if (nSize > 0) {
								int nDistance = 0;
								String strTime;
								nDistance = mShareTaskDataDistance.get(0)
										.getnDistance();
								strTime = mShareTaskDataDistance.get(0)
										.getmTaskAnnounceTime();
								// �������µ�����id
								// mnMaxDistanceForShareNear = nDistance;
								mnOldTimeForShareNear = strTime;
								// ����listview�е�����,�޸���Ӧ������
								// DealUpdateShareTaskData(mShareTaskDataDistance);
							}
						}
					} else if (2 == mnDataType) {
						// ����mnVolume������
						List<TaskInfoDetail> ShareTaskDataTemp = goodService
								.UpdateTaskDataForLimitNew(nMaxDataLine, 2,
										mnNewTimeForShareAll);
						if (ShareTaskDataTemp != null) {
							int nSize = ShareTaskDataTemp.size();
							// �����ѯ��������
							if (nSize > 0) {
								String strTime = "";
								strTime = ShareTaskDataTemp.get(0)
										.getmTaskAnnounceTime().toString();
								// �������µ�����id
								mnNewTimeForShareAll = strTime;
								// ����listview�е�����,�޸���Ӧ������
								DealUpdateShareTaskData(ShareTaskDataTemp);
							}
						}
					}
				}
				// ������Ϣ,�����ݸ���
				Message msg = myhandler.obtainMessage();
				msg.what = 3;
				myhandler.sendMessage(msg);
			}
		};
		// ������ȡ���������߳�
		Thread thread = new Thread(rundata);
		thread.start();
	}

	// ��������adapter
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

	// ���÷���adapter
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
	 * ����Ƿ���°汾
	 */
	public void checkVersion() {
		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					downloadUpdateXMLFile(strUpdateXmlPath);
					/*
					 * if (downloadSize > 0) { // ��ȡ����xml�ļ� Message message =
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
	 * �����ļ�
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	public long downloadUpdateXMLFile(String down_url) throws Exception {
		int totalSize;// �ļ��ܴ�С
		InputStream inputStream;
		int downloadCount = 0;// �Ѿ����غõĴ�С
		URL url = new URL(down_url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		httpURLConnection.setConnectTimeout(TIMEOUT);
		httpURLConnection.setReadTimeout(TIMEOUT);
		httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
		// ��ȡ�����ļ���size
		totalSize = httpURLConnection.getContentLength();
		if (httpURLConnection.getResponseCode() == 404) {
			throw new Exception("fail!");
		}
		inputStream = httpURLConnection.getInputStream();
		mupdateinfo = getUpdataInfo(inputStream);
		/*
		 * byte buffer[] = new byte[1024]; int readsize = 0; while ((readsize =
		 * inputStream.read(buffer)) != -1) { downloadCount += readsize;//
		 * ʱʱ��ȡ���ص��Ĵ�С }
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
	 * ��pull�������������������ص�xml�ļ� (xml��װ�˰汾��)
	 */
	public static UpdateInfo getUpdataInfo(InputStream is) throws Exception {
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "utf-8");// ���ý���������Դ
		int type = parser.getEventType();
		UpdateInfo info = new UpdateInfo();// ʵ��
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				if ("version".equals(parser.getName())) {
					info.setVersion(parser.nextText()); // ��ȡ�汾��
				} else if ("url".equals(parser.getName())) {
					info.setUrl(parser.nextText()); // ��ȡҪ������APK�ļ�
				} else if ("description".equals(parser.getName())) {
					String strTemp = parser.nextText().replace("\\n", "\n");
					info.setDescription(strTemp); // ��ȡ���ļ�����Ϣ
				}
				break;
			}
			type = parser.next();
		}
		return info;
	}

	// �����û�ͷ��
	public static void UpdateAccountPersonImage(String strPersonName,
			String strImage) {
		int nSize = 0;
		// �����ҵ��е�ͷ��
		if (mDynamicNew != null) {
			nSize = mDynamicNew.size();
			for (int i = 0; i < nSize; i++) {
				// ����ҵ��˵�ǰ�û���������������,��ô�ͽ�����ͷ�����
				if (mDynamicNew.get(i).getstrTaskPersonTrueName()
						.equals(strPersonName)) {
					mDynamicNew.get(i).setmTaskAskPersonIcon(strImage);
				}
			}
		}
		// ���������е�ͷ��
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
		// ���·����е�ͷ��
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

		// ���������е�ͷ��
		if (mHelpTaskData != null) {
			nSize = mHelpTaskData.size();
			for (int i = 0; i < nSize; i++) {
				if (mHelpTaskData.get(i).getstrTaskPersonTrueName()
						.equals(strPersonName)) {
					mHelpTaskData.get(i).setmTaskAskPersonIcon(strImage);
				}
			}

		}
		// ���·����е�ͷ��
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

	// ������Ŀ��Ϣ,nTypeֵΪ1��ʾ����,2��ʾ����,nValueΪ1��ʾ�޸ı���,2��ʾ��ϸ��Ϣ
	public static void UpdateAccountPersonInfo(String strId, int nType,
			int nValue, String strInfo) {
		int nSize = 0;
		// �����ҵ��е���Ϣ
		if (mDynamicNew != null) {
			nSize = mDynamicNew.size();
			for (int i = 0; i < nSize; i++) {
				// ����ҵ��˵�ǰ�û���������������,��ô�ͽ�����ͷ�����
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
		// ���������е���Ϣ
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
		// ���·����е���Ϣ
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

		// ���������е���Ϣ
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
		// ���·����е���Ϣ
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
		// ����֪ͨ��״̬����ʾ��ͼ��
		notification.icon = R.drawable.zan;

		// ֪ͨʱ��״̬����ʾ������
		notification.tickerText = "��ʼ����";
		// ֪ͨ��������Զ���ʧ
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		/***
		 * �������������Զ���view����ʾNotification
		 */
		contentView = new RemoteViews(getPackageName(),
				R.layout.notification_item);
		contentView.setTextViewText(R.id.notificationTitle, "��������");
		contentView.setTextViewText(R.id.notificationPercent, "0%");
		contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

		notification.contentView = contentView;

		notification.contentIntent = pendingIntent;

		notificationManager.notify(notification_id, notification);

	}

	/***
	 * ���߳�����
	 */
	public void createThread() {
		/***
		 * ����UI
		 */
		final Handler handlerDownFile = new Handler() {
			@SuppressWarnings("deprecation")
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case DOWN_OK:

					// ������ɣ������װ
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
							"����ʧ��", pendingIntent);
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
						// ���سɹ�
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
	 * �����ļ�
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	public long downloadUpdateFile(String down_url, String file)
			throws Exception {
		int down_step = 5;// ��ʾstep
		int totalSize;// �ļ��ܴ�С
		int downloadCount = 0;// �Ѿ����غõĴ�С
		int updateCount = 0;// �Ѿ��ϴ����ļ���С
		InputStream inputStream;
		OutputStream outputStream;

		URL url = new URL(down_url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		httpURLConnection.setConnectTimeout(TIMEOUT);
		httpURLConnection.setReadTimeout(TIMEOUT);
		httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
		// ��ȡ�����ļ���size
		totalSize = httpURLConnection.getContentLength();
		if (httpURLConnection.getResponseCode() == 404) {
			throw new Exception("fail!");
		}
		int nCode = httpURLConnection.getHeaderFieldInt("versioncode", -1);
		inputStream = httpURLConnection.getInputStream();
		outputStream = new FileOutputStream(file, false);// �ļ������򸲸ǵ�
		byte buffer[] = new byte[1024];
		int readsize = 0;
		while ((readsize = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, readsize);
			downloadCount += readsize;// ʱʱ��ȡ���ص��Ĵ�С
			/**
			 * ÿ������5%
			 */
			if (updateCount == 0
					|| (downloadCount * 100 / totalSize - down_step) >= updateCount) {
				updateCount += down_step;
				// �ı�֪ͨ��
				// notification.setLatestEventInfo(this, "��������...", updateCount
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

	// �첽����ͼƬ
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
			// �жϱ������Ƿ񱣴��˸�ͼƬ
			boolean bIsExist = mUtils.fileIsExists(strImagePath);
			// ������ش��ڸ�ͼƬ����
			if (bIsExist) {
				// ��ͼƬ�ӱ����ж���
				bmp = mUtils.GetBitMapFromPathNew(strImagePath);
			} else {// ���������
					// �ӷ������ж�ȡ
				String strImage = goodService.GetFirstSmallIcon(strTaskId,
						strType);
				// ��base64��ʽ��ͼƬת����bmp
				bmp = mUtils.base64ToBitmap(strImage);
				// ��ͼƬ���ݱ��浽�����ļ���
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

	// �ж�ĳ���������Ƿ���mDynamicNew��,���������,��ô�͵���������ȡ
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
			// ����ҵ���,��ô˵���Ѿ����˸�����,���õ���������ȡ,������Ҫ���������ϻ�ȡ������
			if (!bFind) {
				// ���ø���"�ҵ�"���ݵı�־
				bIsFirstStart = true;
			}
		}
	}
}