package com.renpin.renpincustomer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.renpin.domin.CommentInfo;
import com.renpin.domin.CreditAndCharmForTask;
import com.renpin.domin.TaskIcon;
import com.renpin.faceimage.ExpressionUtil;
import com.renpin.location.MyApplication;
import com.renpin.mysqlite.OperaDatabase;
import com.renpin.renpincustomer.AboutDialog.PriorityListener;
import com.renpin.service.GoodService;
import com.renpin.service.Impl.GoodServiceImpl;
import com.renpin.utils.CommonUtils;
import com.renpin.utils.ScreenShot;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DynamicDetailActivity extends Activity {
	// ����id��
	private String strTaskId;
	// ������ͼ��
	ImageView imageview1;
	// ����������
	TextView textview1;
	// ���񷢲�ʱ��
	TextView textview2;
	// ����ʣ��ʱ��
	TextView textview4;
	// �������
	TextView textview5;
	TextView textview5_1;
	// ������ϸ��Ϣ
	TextView textview6;
	// ִ�������߸������ߵ��ܽ�
	// TextView textview8;
	// ���񷢲��߸�ִ���ߵ��ܽ�
	TextView textview16;
	// ִ�������߸������߷��͵�ͼƬ
	// ImageView imageview2;
	// �����߸�ִ���߷��͵�ͼƬ
	ImageView imageview10;
	// ����ͼ��
	ImageView imageview3;
	// ����ͼ��
	ImageView imageview4;
	// ��������
	TextView textview9;
	// ��ʾ��������
	TextView textview18;
	// �洢��������
	private List<CommentInfo> mCommentInfos = null;
	// �洢���»�õ���������
	private List<CommentInfo> mNewCommentInfos = null;
	// ��ǰ���۵�����ʱ��
	private String strCommentTime = "2012��06��06��06:06:08";
	private// �����洢�����ݿ��л�ȡ��������
	List<HashMap<String, Object>> mListData = null;
	// ��ʱ��ʣ��ʱ��(��)
	private static long mnRemainTime;
	private static CommonUtils mUtils = null;
	// �����洢�û���Ϣ
	SharedPreferences msettings = null;
	// �ж��Ƿ��������ʱ
	boolean mbIsContinue = true;
	MyHandler myhandler = null;
	// ��ʱ����ʱ����̶߳���
	private static Thread mthre = null;
	// ������ȡ�����������ݵĶ���
	private GoodService goodService = new GoodServiceImpl();
	// �������۱༭��
	ClearEditText edittext1;
	// �������۰�ť
	Button button5;
	// �ж������Ƿ����ָ������
	boolean bIsTalkToPerson = false;

	// ���񷢲�������
	private String strAccountName1;
	// ����ִ��������
	private String strImpleName2;
	// ���񷢲����ǳ�
	private String strCustomerNameNick;
	// ����ִ�����ǳ�
	private String strImplementNameNick;

	// ���񷢲�������״̬
	private int nAccountComment1;
	// ����ִ��������״̬
	private int nImpleComment1;

	// �������۵�������
	private String strReceiveName;
	// �������۵�������
	private String strReceiveNickName;
	// ��ʾ���ؼ�
	private ImageView imageview5;
	// �ж�����ִ�е���λ��
	private int nTaskPosition = 1;
	// ״̬ͼƬ������
	private int nStatusImageIndex = 0;
	// ��ʾ���۵�LinearLayout
	private LinearLayout linearlayout1 = null;
	private LinearLayout linearlayout2 = null;
	// ������ɻ����ͼƬ�ؼ�
	private ImageView imagetask1;
	private ImageView imagetask2;
	private ImageView imagetask3;
	private ImageView imagetask4;
	private ImageView imagetask5;
	private ImageView imagetask6;

	// ������������еķŴ�ͼƬ
	public static String[] strIcons = null;
	// �����������ͼƬ
	TaskIcon taskicon = null;
	// �洢�������ݿ��еĴ�ͼƬ����
	TaskIcon largeicon = null;
	// �����߷��͸������ߵ�ͼƬ
	static String strAccountPersonToRecImage = "";
	// �����߷��͸������ߵ�ͼƬ
	static String strRecToAccountPersonImage = "";

	// ��л��ť
	private Button thanksbutton;
	// ����ͼƬ�ؼ�
	private ImageView sendpictureimageview;
	// ��������,1��ʾ����,2��ʾ����
	// private int mnTaskType;
	// ������ѡͼƬ��·��
	private String mImgPaths = "";
	public static final String[] addPhoto = new String[] { "��������", "�����ѡ��",
			"ȡ��" };
	// �洢�����еĴ�ͼƬ
	//public static HashMap<String, String> CommentMaps;
	// ����ո�ѡ�������ѹ��ͼƬ
	private String strCommentSmallImage;
	// ����ո�ѡ������۴�ͼƬ
	private String strCommentLargeImage;
	// ��Ʒֵ
	int mnCredit = 0;
	// ��ֵ
	int mnCharmValue = 0;
	// ��ʾ��Ʒֵ�ؼ�
	TextView credittextview;
	// ��ʾ��ֵ�ؼ�
	TextView charmvaluetextview;
	// �����ط���ť
	Button resendbutton;
	// �������֮����Ʒֵ����ֵ�ļ�������ؼ�
	TextView taskendtextview;
	// �������ؼ�
	ScrollView scrollview;
	// �洢��ǰ�����û���ȡ������Ʒֵ����ֵ
	CreditAndCharmForTask taskvalue;
	// �ж������Ƿ��Ѿ������ı��
	int nVerfiValue = 1;
	// ��л����Ϣ�ؼ�
	TextView textviewthanksfrom;
	// ��ʾ��һ����СͼƬ��linearlayout
	private LinearLayout smalliconslinearlayout1;
	// ��ʾ�ڶ�����СͼƬ��linearlayout
	private LinearLayout smalliconslinearlayout2;
	// ����������
	public static ProgressDialog m_ProgressDialog = null;
	// �޿ؼ�
	private LinearLayout zanimageviewlayout;
	// ��ֵ�����ؼ�
	private TextView zantextview;
	// ��ʾ���������Ŀؼ�
	private TextView talktextview;
	// ��ʾ��������Ŀؼ�
	private TextView looktimetextview;
	// ��ֵ
	private int nTaskCharmValue;
	// ��������
	private int nCommentNum;
	// �����
	private int nBrowseNum;
	// ��������Ƿ�ɹ���һ
	private boolean bIsAddBrowseTime = false;
	// ��ʾ��������ַ�Ŀؼ�
	private TextView addresstextview;
	// ���������ַ
	private String strAddressRegion;
	// ��ʾͼ��Ŀؼ�
	public static LinearLayout gridviewlinearlayout;
	// ��ʾͼ��Ŀؼ�
	GridView gridView;
	// private Dialog builder;
	private int[] imageIds = new int[107];
	// �������ݿ����
	private OperaDatabase mDatabase = null;
	// ���ڷ���������Ӧ��ƽ̨��ͼƬ��ť�ؼ�
	private ImageView ShareImageView;
	// �������
	public String strItemTitleText;
	// ������ϸ��Ϣ
	public String strItemDetailText;
	// �жϷ���ͼƬ�Ƿ��ȡ����
	// private boolean bIsAccountImageGet = false;
	// ����ͼƬ������
	private static final int mnImageNum = 6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// ����������ʱ������괦��editview�ж��������뷨����
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setContentView(R.layout.my_task);
		// ��ʼ������ͼƬ����
		//CommentMaps = new HashMap<String, String>();

		strIcons = new String[6];

		strAccountPersonToRecImage = "";
		strRecToAccountPersonImage = "";

		String strCustomerNameTemp;// ����������
		String strTaskAnnounceTime;// ����ʱ��
		String strTimeLimit;// ʱ������
		String strTaskTitle;// �������
		String strDetail;// ������ϸ����
		String strRunSeconds;// ������е�����
		String strImplementName;// ����ִ����
		int nTaskTimeStatus;// ������Ч��״̬
		int nTaskImpleStatus;// ����ִ��״̬
		// ��̬��Ϣ
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

		strCustomerNameTemp = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.customername");
		strTaskAnnounceTime = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.TaskAnnounceTime");
		strTimeLimit = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.TimeLimit");
		strTaskTitle = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.TaskTitle");
		strItemTitleText = strTaskTitle;
		strDetail = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.Detail");
		strItemDetailText = strDetail;
		strRunSeconds = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.RunSeconds");
		strTaskId = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.TaskId");
		strImplementName = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.TaskImplementName");
		strCustomerNameNick = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.strCustomerNameTemp");
		strImplementNameNick = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.strImplementNameTemp");
		nTaskTimeStatus = getIntent().getIntExtra(
				"com.renpin.RenPinMainActivity.nTimeStatus", -1);
		nTaskImpleStatus = getIntent().getIntExtra(
				"com.renpin.RenPinMainActivity.nTaskImplemeStatus", -1);
		nTaskSelectType = getIntent().getIntExtra(
				"com.renpin.RenPinMainActivity.nTaskSelectType", -1);
		nTaskFinishType = getIntent().getIntExtra(
				"com.renpin.RenPinMainActivity.nTaskFinishType", -1);
		nTaskVerifiType = getIntent().getIntExtra(
				"com.renpin.RenPinMainActivity.nTaskVerifiType", -1);
		nTaskAnnounceCommentType = getIntent().getIntExtra(
				"com.renpin.RenPinMainActivity.nTaskAnnounceCommentType", -1);
		nTaskImplementCommentType = getIntent().getIntExtra(
				"com.renpin.RenPinMainActivity.nTaskImplementCommentType", -1);

		strAnnounceComment = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.strAnnounceComment");
		strAnnounceBase64Image = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.strAnnounceBase64Image");
		strImpleComment = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.strImpleComment");
		strImpleBase64Image = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.strImpleBase64Image");

		nTaskCharmValue = getIntent().getIntExtra(
				"com.renpin.RenPinMainActivity.nCharmValue", -1);
		nCommentNum = getIntent().getIntExtra(
				"com.renpin.RenPinMainActivity.nCommentNum", -1);
		nBrowseNum = getIntent().getIntExtra(
				"com.renpin.RenPinMainActivity.nBrowseNum", -1);

		strAddressRegion = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.strAddressRegion");

		// �����������ݿ����
		mDatabase = new OperaDatabase(this);
		if (!mDatabase.IsTableExist(Integer.parseInt(strTaskId), 1)) {
			mDatabase.CloseDatabase();
			mDatabase = null;
			mDatabase = new OperaDatabase(this);
		}

		msettings = getSharedPreferences("MekeSharedPreferences", 0);
		// ����������Ϣ����
		myhandler = new MyHandler();
		if (null == mUtils) {
			// �����������������
			mUtils = new CommonUtils(this);
		}
		strAccountName1 = strCustomerNameTemp;
		strImpleName2 = strImplementName;
		nAccountComment1 = nTaskAnnounceCommentType;
		nImpleComment1 = nTaskImplementCommentType;
		// ���������Ƿ���ɵı��
		nVerfiValue = nTaskVerifiType;
		// ��������������Ĭ��Ϊ���񷢲�������
		strReceiveName = strCustomerNameTemp;
		strReceiveNickName = strCustomerNameNick;
		// ��ʼ������
		InitActivities(strCustomerNameTemp, strTaskAnnounceTime, strTimeLimit,
				strTaskTitle, strDetail, strRunSeconds, strImplementName,
				nTaskTimeStatus, nTaskImpleStatus, nTaskSelectType,
				nTaskFinishType, nTaskVerifiType, nTaskAnnounceCommentType,
				nTaskImplementCommentType, strAnnounceComment,
				strAnnounceBase64Image, strImpleComment, strImpleBase64Image);
		// ��ʼ������ͼƬ
		InitGridView();
		// ���ؽ���
		// init();
		// ���º��״̬
		UpdateDynamicStatus(strCustomerNameTemp, strImplementName, 3,
				strTaskId, nTaskSelectType, nTaskFinishType, nTaskVerifiType,
				nTaskAnnounceCommentType, nTaskImplementCommentType);

		// ��ʾ��������
		ShowComments();
		// ��ʾ����
		ShowOtherData();
		// ��ʾͼƬ
		ShowPictures();
		// ��ʾ�������
		ShowBrowseTime();
	}

	private boolean GetOtherSmallImageData() {
		boolean bIsFind = true;
		taskicon = new TaskIcon("", "", "", "", "", "");
		// �洢ͼƬ�ļ���·��
		String strImageFilePath = "";
		// ��Ŀ¼·��
		String strRootPath = mUtils.GetFileRootPath();
		boolean bIsExist = false;
		// ͼƬ
		Bitmap bmp = null;
		// ���ж�����ͼƬ�Ƿ����
		for (int i = 2; i <= mnImageNum; i++) {

			strImageFilePath = strRootPath + strTaskId + "_" + 1 + "_" + i;
			// �жϱ������Ƿ񱣴��˸�ͼƬ
			bIsExist = mUtils.fileIsExists(strImageFilePath);
			// ������ش��ڸ�ͼƬ����
			if (bIsExist) {
				// ��ͼƬ�ӱ����ж���
				bmp = mUtils.GetBitMapFromPathNew(strImageFilePath);
				// ��bmpת��base64
				String strBase64Image = mUtils.BitmapToBase64BySize(bmp);
				if (2 == i) {
					if (strBase64Image != null) {
						taskicon.setstrIcon2(strBase64Image);
					}
				} else if (3 == i) {
					if (strBase64Image != null) {
						taskicon.setstrIcon3(strBase64Image);
					}
				} else if (4 == i) {
					if (strBase64Image != null) {
						taskicon.setstrIcon4(strBase64Image);
					}
				} else if (5 == i) {
					if (strBase64Image != null) {
						taskicon.setstrIcon5(strBase64Image);
					}
				} else if (mnImageNum == i) {
					if (strBase64Image != null) {
						taskicon.setstrIcon6(strBase64Image);
					}
				}

			} else {// ֻҪ����һ��ͼƬ�����ھͷ���false
				bIsFind = false;
				break;
			}
		}
		return bIsFind;
	}

	// ��ȡ��ͼ
	private boolean GetLargeImageData() {
		largeicon = new TaskIcon("", "", "", "", "", "");
		boolean bIsFind = true;
		// �洢ͼƬ�ļ���·��
		String strImageFilePath = "";
		// ��Ŀ¼·��
		String strRootPath = mUtils.GetFileRootPath();
		boolean bIsExist = false;
		// ͼƬ
		Bitmap bmp = null;
		// ���ж�����ͼƬ�Ƿ����
		for (int i = 1; i <= mnImageNum; i++) {

			strImageFilePath = strRootPath + strTaskId + "_" + 1 + "_" + i
					+ "_" + "l";
			// �жϱ������Ƿ񱣴��˸�ͼƬ
			bIsExist = mUtils.fileIsExists(strImageFilePath);
			// ������ش��ڸ�ͼƬ����
			if (bIsExist) {
				// ��ͼƬ�ӱ����ж���
				bmp = mUtils.GetBitMapFromPathNew(strImageFilePath);
				// ��bmpת��base64
				String strBase64Image = mUtils.BitmapToBase64BySize(bmp);
				if (1 == i) {
					if (strBase64Image != null) {
						largeicon.setstrIcon1(strBase64Image);
					}
				} else if (2 == i) {
					if (strBase64Image != null) {
						largeicon.setstrIcon2(strBase64Image);
					}
				} else if (3 == i) {
					if (strBase64Image != null) {
						largeicon.setstrIcon3(strBase64Image);
					}
				} else if (4 == i) {
					if (strBase64Image != null) {
						largeicon.setstrIcon4(strBase64Image);
					}
				} else if (5 == i) {
					if (strBase64Image != null) {
						largeicon.setstrIcon5(strBase64Image);
					}
				} else if (mnImageNum == i) {
					if (strBase64Image != null) {
						largeicon.setstrIcon6(strBase64Image);
					}
				}
			} else {// ֻҪ����һ��ͼƬ�����ھͷ���false
				bIsFind = false;
				break;
			}
		}
		return bIsFind;
	}

	// �ӱ����ļ�����ȡͼƬ����
	private void GainImageData() {
		boolean bIsSmallFind = GetOtherSmallImageData();
		boolean bIsLargeFind = GetLargeImageData();
		// �洢ͼƬ�ļ���·��
		String strImageFilePath = "";
		// ��Ŀ¼·��
		String strRootPath = mUtils.GetFileRootPath();
		strImageFilePath = strRootPath + strTaskId + "_" + 1 + "_" + 1;
		// ����һ��ͼƬ�ӱ����ж���
		Bitmap bmp = mUtils.GetBitMapFromPathNew(strImageFilePath);
		// ��bmpת��base64
		String strBase64Image = mUtils.BitmapToBase64BySize(bmp);
		// �������ͼƬҲ������,��ô����һ��ͼƬ��ֵҲ����ȥ
		if (!bIsSmallFind) {
			taskicon = null;
		} else {
			if (strBase64Image != null) {
				taskicon.setstrIcon1(strBase64Image);
			}
		}
		if (!bIsLargeFind) {
			largeicon = null;
		}
	}

	private void ShowComments() {
		Runnable localrun = new Runnable() {
			public void run() {
				String strAccountName = msettings.getString("TruePersonName",
						"");

				// �ӱ������ݿ��л�ȡ��������
				mCommentInfos = mDatabase.GetCommentData(
						Integer.parseInt(strTaskId), 1);
				String strTime = mDatabase.GetCommentTime(
						Integer.parseInt(strTaskId), 1);
				if (!strTime.equals("")) {
					strCommentTime = strTime;
				}

				mNewCommentInfos = goodService.GetCommentsForTaskNew1(
						strTaskId, "1", strAccountName, strCommentTime);

				Message msg = myhandler.obtainMessage();
				msg.what = 15;
				myhandler.sendMessage(msg);
			}
		};
		// ������ȡ���������߳�
		Thread thread = new Thread(localrun);
		thread.start();
	}

	private void ShowOtherData() {
		Runnable run2 = new Runnable() {
			public void run() {
				String strAccountName = msettings.getString("TruePersonName",
						"");
				mnRemainTime = goodService.GetTaskRemainTime(strTaskId, "1");

				if (!strAccountName1.equals("")) {
					// ���������ƻ�ȡ��Ӧ����Ʒֵ����ֵ
					mnCredit = goodService.GetCreditValue(strAccountName1);
					mnCharmValue = goodService.GetCharmValue(strAccountName1);
				}
				// �����ǰ�������ɸ��û�ִ�е�,�Ҹ������Ѿ�����
				if (strImpleName2.equals(strAccountName)
						&& (2 == nVerfiValue || 3 == nVerfiValue)) {
					// taskvalue =
					// goodService.GetCreditAndCharmForTask(strTaskId,
					// "1");
				}

				Message msg = myhandler.obtainMessage();
				msg.what = 3;
				myhandler.sendMessage(msg);
			}
		};
		// ������ȡ���������߳�
		Thread thread = new Thread(run2);
		thread.start();
	}

	private void ShowPictures() {
		Runnable runpic = new Runnable() {
			public void run() {
				int nRet = 1;
				GainImageData();
				// ����������ݿ���û���ҵ�СͼƬ,��ô�͵���������ȥ��ȡ
				if (null == taskicon) {
					nRet = 0;
					// ��ȡ��������ͼƬ
					taskicon = goodService.GetTaskSmallIcon(strTaskId, "1");
				}
				// ����������ݿ���û���ҵ���ͼƬ,��ô�͵���������ȥ��ȡ
				if (null == largeicon) {
					nRet = 0;
					// ��ȡÿ����Сͼ�ķŴ�ͼƬ
					strIcons[0] = goodService.GetTaskLargeIcon(strTaskId, "1",
							1);
					strIcons[1] = goodService.GetTaskLargeIcon(strTaskId, "1",
							2);
					strIcons[2] = goodService.GetTaskLargeIcon(strTaskId, "1",
							3);
					strIcons[3] = goodService.GetTaskLargeIcon(strTaskId, "1",
							4);
					strIcons[4] = goodService.GetTaskLargeIcon(strTaskId, "1",
							5);
					strIcons[5] = goodService.GetTaskLargeIcon(strTaskId, "1",
							6);
				} else {
					strIcons[0] = largeicon.getstrIcon1();
					strIcons[1] = largeicon.getstrIcon2();
					strIcons[2] = largeicon.getstrIcon3();
					strIcons[3] = largeicon.getstrIcon4();
					strIcons[4] = largeicon.getstrIcon5();
					strIcons[5] = largeicon.getstrIcon6();
				}
				// ���û���ҵ�ͼƬ����,��ô�ͽ�ͼƬ���ݱ��浽�������ݿ���
				/*
				 * if (0 == nRet) {
				 * mDatabase.InsertImage(Integer.parseInt(strTaskId), 1,
				 * taskicon.getstrIcon1(), taskicon.getstrIcon2(),
				 * taskicon.getstrIcon3(), taskicon.getstrIcon4(),
				 * taskicon.getstrIcon5(), taskicon.getstrIcon6(), strIcons[0],
				 * strIcons[1], strIcons[2], strIcons[3], strIcons[4],
				 * strIcons[5]); }
				 */
				Message msg = myhandler.obtainMessage();
				msg.what = 11;
				Bundle b = new Bundle();// �������
				b.putInt("nRet", nRet);
				msg.setData(b);
				myhandler.sendMessage(msg);
			}
		};
		// ������ȡ���������߳�
		Thread thread = new Thread(runpic);
		thread.start();
	}

	private void ShowBrowseTime() {
		Runnable browserun = new Runnable() {
			public void run() {
				int nRet = goodService.AddBrowseTimes(strTaskId, "1");

				Message msg = myhandler.obtainMessage();
				Bundle b = new Bundle();// �������
				b.putInt("nRet", nRet);
				msg.setData(b);
				msg.what = 13;
				myhandler.sendMessage(msg);
			}
		};
		// ������ȡ���������߳�
		Thread thread = new Thread(browserun);
		thread.start();
	}

	private boolean IsShowHotDot(String strTaskId, String CurrentPersonName) {
		boolean bIsTure = false;
		if (RenPinMainActivity.mDynamicNew != null) {
			int nCount = RenPinMainActivity.mDynamicNew.size();
			for (int i = 0; i < nCount; i++) {
				String strAccounceName = RenPinMainActivity.mDynamicNew.get(i)
						.getstrTaskPersonTrueName();
				String strId = RenPinMainActivity.mDynamicNew.get(i)
						.getmstrId();
				String strImpleAccouceName = RenPinMainActivity.mDynamicNew
						.get(i).getstrTaskImplementTrueName();
				// �������������ɵ�ǰ�û�������
				if (CurrentPersonName.equals(strAccounceName)) {
					boolean b1 = strTaskId.equals(strId);
					boolean b3 = !strImpleAccouceName.equals("");

					if (b1 && b3) {
						if (2 == RenPinMainActivity.mDynamicNew.get(i)
								.getnTaskImplementCommentType()) {
							bIsTure = true;
							break;
						}
					}

				} else if (CurrentPersonName.equals(strImpleAccouceName)) {// �������������ɵ�ǰ�û�ִ�е�
					boolean b1 = strTaskId.equals(strId);
					if (b1) {
						if (2 == RenPinMainActivity.mDynamicNew.get(i)
								.getnTaskAnnounceCommentType()) {
							bIsTure = true;
							break;
						}
					}
				}

			}
		}
		return bIsTure;
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
		}
	}

	private void InitActivities(final String strCustomerNameTemp,
			String strTaskAnnounceTime, String strTimeLimit,
			String strTaskTitle, String strDetail, String strRunSeconds,
			final String strImplementName, int nTaskTimeStatus,
			int nTaskImpleStatus, int nTaskSelectType, int nTaskFinishType,
			int nTaskVerifiType, int nTaskAnnounceCommentType,
			int nTaskImplementCommentType, String strAnnounceComment,
			String strAnnounceBase64Image, String strImpleComment,
			String strImpleBase64Image) {

		imageview1 = (ImageView) findViewById(R.id.mytask_imageview1);
		textview5_1 = (TextView) findViewById(R.id.mytask_textview_title);
		textview1 = (TextView) findViewById(R.id.mytask_textview1);
		textview2 = (TextView) findViewById(R.id.mytask_textview2);
		textview4 = (TextView) findViewById(R.id.mytask_textview4);
		textview5 = (TextView) findViewById(R.id.mytask_textview5);
		textview5.setClickable(false);
		textview6 = (TextView) findViewById(R.id.mytask_textview6);
		textview6.setClickable(false);
		imageview3 = (ImageView) findViewById(R.id.mytask_imageview3);
		textview9 = (TextView) findViewById(R.id.mytask_textview9);
		credittextview = (TextView) findViewById(R.id.mytask_credittextview);
		charmvaluetextview = (TextView) findViewById(R.id.mytask_charmtextview);
		imageview4 = (ImageView) findViewById(R.id.customer_login_image1);
		edittext1 = (ClearEditText) findViewById(R.id.my_task_edittext1);
		edittext1.SetType(1);
		button5 = (Button) findViewById(R.id.my_task_button1);
		imageview5 = (ImageView) findViewById(R.id.mytask_imageview4);
		linearlayout1 = (LinearLayout) findViewById(R.id.mytask_linearlayout1);
		linearlayout2 = (LinearLayout) findViewById(R.id.comment_item_linearlayout1);
		textview16 = (TextView) findViewById(R.id.mytask_textview16);
		imageview10 = (ImageView) findViewById(R.id.mytask_imageview10);
		textview18 = (TextView) findViewById(R.id.mytask_textview17);
		thanksbutton = (Button) findViewById(R.id.mytask_thanksbutton);
		sendpictureimageview = (ImageView) findViewById(R.id.mytask_sendpicture);
		resendbutton = (Button) findViewById(R.id.mytask_resendbutton);
		taskendtextview = (TextView) findViewById(R.id.mytask_endinfo);
		taskendtextview.setText("");
		textviewthanksfrom = (TextView) findViewById(R.id.mytask_textviewthanksfrom);
		textviewthanksfrom.setVisibility(View.GONE);
		smalliconslinearlayout1 = (LinearLayout) findViewById(R.id.mytask_smallicons1);
		smalliconslinearlayout2 = (LinearLayout) findViewById(R.id.mytask_smallicons2);
		smalliconslinearlayout1.setVisibility(View.GONE);
		smalliconslinearlayout2.setVisibility(View.GONE);
		scrollview = (ScrollView) findViewById(R.id.mytask_scrollview);
		zanimageviewlayout = (LinearLayout) findViewById(R.id.mytask_zanimageviewlayout);
		zantextview = (TextView) findViewById(R.id.mytask_zantextview);
		zantextview.setText("");
		talktextview = (TextView) findViewById(R.id.mytask_talknumtextview);
		talktextview.setText("");
		looktimetextview = (TextView) findViewById(R.id.mytask_looktimetextview);
		looktimetextview.setText("");
		addresstextview = (TextView) findViewById(R.id.mytask_addresstextview);
		addresstextview.setText(strAddressRegion);
		gridviewlinearlayout = (LinearLayout) findViewById(R.id.mytask_gridviewlayout);
		// һ��ʼ����
		gridviewlinearlayout.setVisibility(View.GONE);
		ShareImageView = (ImageView) findViewById(R.id.mytask_shareimageview);
		// ���񷢲�ͼƬ�ؼ�
		imagetask1 = (ImageView) findViewById(R.id.mytask_icon1);
		imagetask2 = (ImageView) findViewById(R.id.mytask_icon2);
		imagetask3 = (ImageView) findViewById(R.id.mytask_icon3);
		imagetask4 = (ImageView) findViewById(R.id.mytask_icon4);
		imagetask5 = (ImageView) findViewById(R.id.mytask_icon5);
		imagetask6 = (ImageView) findViewById(R.id.mytask_icon6);
		// ��ͼƬ��ʾ֮ǰ�ÿؼ����ɵ��
		imagetask1.setClickable(false);
		imagetask2.setClickable(false);
		imagetask3.setClickable(false);
		imagetask4.setClickable(false);
		imagetask5.setClickable(false);
		imagetask6.setClickable(false);

		textview18.setText("��������");

		Bitmap bmp = RenPinMainActivity.mAnnounceImage;
		if (bmp != null) {
			imageview1.setImageBitmap(bmp);
		}

		textview1.setText(strCustomerNameNick);
		textview2.setText(strTaskAnnounceTime);
		int nLimit = Integer.parseInt(strTimeLimit);
		int nRunSecond = Integer.parseInt(strRunSeconds);
		mnRemainTime = nLimit - nRunSecond;
		// ���ʣ��ʱ��С��0,��ô��Ĭ�ϸ�ֵ0
		if (mnRemainTime <= 0) {
			mnRemainTime = 0;
		}
		String strAccountName = msettings.getString("TruePersonName", "");
		String strTime = mUtils.GetStringBySeconds(mnRemainTime);
		textview4.setText(strTime);
		textview5.setText(strTaskTitle);
		textview6.setText(strDetail);
		textview5_1.setText(strTaskTitle);

		if (null == mthre/* && mnRemainTime > 0 */) {
			mbIsContinue = true;
			mthre = new Thread(run1);
			mthre.start();
		}

		// �������������ɵ�ǰ�û�������,��ô�Ϳ���ˢ��
		if (strAccountName.equals(strCustomerNameTemp)) {
			resendbutton.setVisibility(View.VISIBLE);
			Drawable drawable = getResources().getDrawable(
					R.drawable.makepublic1);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			textview5.setCompoundDrawables(null, null, drawable, null);
			textview6.setCompoundDrawables(null, null, drawable, null);
			textview5.setClickable(true);
			textview6.setClickable(true);
		} else {
			textview5.setCompoundDrawables(null, null, null, null);
			textview6.setCompoundDrawables(null, null, null, null);
			resendbutton.setVisibility(View.GONE);
		}
		// �������������ɵ�ǰ�û�������
		if (strAccountName.equals(strCustomerNameTemp)) {
			// ���������Ѿ�����,����������������
			/*
			 * if (1 == nTaskVerifiType) {
			 * resendbutton.setVisibility(View.VISIBLE); } else {
			 * resendbutton.setVisibility(View.GONE); }
			 */

			thanksbutton.setVisibility(View.VISIBLE);
			// ����������û���˽���
			if (strImplementName.equals("")) {

				thanksbutton.setVisibility(View.GONE);
				imageview10.setVisibility(View.GONE);

			} else if (2 == nTaskVerifiType || 3 == nTaskVerifiType) {// ������������֤�ɹ�
				textview16.setText(strImpleComment);
				// ��������Ѿ�����
				thanksbutton.setVisibility(View.GONE);

				if (strImpleBase64Image.equals("")) {
					imageview10.setVisibility(View.GONE);
				} else {
					imageview10.setImageBitmap(mUtils
							.base64ToBitmap(strImpleBase64Image));
				}

				textviewthanksfrom.setVisibility(View.VISIBLE);
				String strThanks = strImplementNameNick;
				strThanks += "�������������";
				textviewthanksfrom.setText(strThanks);

			} else if (4 == nTaskVerifiType) {// ������������֤ʧ��
				textview16.setText(strImpleComment);

				if (strImpleBase64Image.equals("")) {
					imageview10.setVisibility(View.GONE);
				} else {
					imageview10.setImageBitmap(mUtils
							.base64ToBitmap(strImpleBase64Image));
				}

				textviewthanksfrom.setVisibility(View.VISIBLE);
				String strThanks = strImplementNameNick;
				strThanks += "�������������";
				textviewthanksfrom.setText(strThanks);

			} else if (2 == nTaskFinishType || 3 == nTaskFinishType) {// �����������Ѿ���ɵȴ���������֤

				imageview10.setVisibility(View.GONE);

				textviewthanksfrom.setVisibility(View.VISIBLE);
				String strThanks = strImplementNameNick;
				strThanks += "�������������";
				textviewthanksfrom.setText(strThanks);

			} else if (!strImplementName.equals("")
					&& (2 == nTaskSelectType || 3 == nTaskSelectType)) {// �����������Ѿ����˽���,����ִ��

				imageview10.setVisibility(View.GONE);

				textviewthanksfrom.setVisibility(View.VISIBLE);
				String strThanks = strImplementNameNick;
				strThanks += "�������������";
				textviewthanksfrom.setText(strThanks);
			}
		} else {// �������������ɵ�ǰ�û�ִ�е�

			// resendbutton.setVisibility(View.GONE);
			thanksbutton.setVisibility(View.GONE);

			if (2 == nTaskVerifiType || 3 == nTaskVerifiType) {// ���������֤�ɹ�

				textview16.setText(strImpleComment);

				if (strImpleBase64Image.equals("")) {
					imageview10.setVisibility(View.GONE);
				} else {
					imageview10.setImageBitmap(mUtils
							.base64ToBitmap(strImpleBase64Image));
				}
				// ��ʾ����˭�Ĵ�л
				textviewthanksfrom.setVisibility(View.VISIBLE);
				String strThanks = "����";
				strThanks += strCustomerNameNick;
				strThanks += "�Ĵ�л";
				textviewthanksfrom.setText(strThanks);
			} else if (4 == nTaskVerifiType) {// ���������֤ʧ��

				textview16.setText(strImpleComment);

				if (strImpleBase64Image.equals("")) {
					imageview10.setVisibility(View.GONE);
				} else {
					imageview10.setImageBitmap(mUtils
							.base64ToBitmap(strImpleBase64Image));
				}

			} else if (2 == nTaskFinishType || 3 == nTaskFinishType) {// �������ȴ�ȷ��

				imageview10.setVisibility(View.GONE);

			} else if (2 == nTaskSelectType || 3 == nTaskSelectType) {// �����������ִ��

				imageview10.setVisibility(View.GONE);

			}
		}

		imageview4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mbIsContinue = false;
				if (bIsAddBrowseTime) {
					String strZan = zantextview.getText().toString();
					String strTalk = talktextview.getText().toString();
					String strLook = looktimetextview.getText().toString();
					Intent data = new Intent();
					data.putExtra("strTaskId", strTaskId);
					data.putExtra("strTaskType", "1");
					data.putExtra("strZan", strZan);
					data.putExtra("strTalk", strTalk);
					data.putExtra("strLook", strLook);
					setResult(1, data);
				} else {
					setResult(2);
				}
				DynamicDetailActivity.this.finish();
			}
		});

		thanksbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {// //���������״̬�޸�,ֵ��Ϊ2
				Intent it = new Intent(DynamicDetailActivity.this,
						TaskAnnounceVertifOperaActivity.class);
				it.putExtra(
						"com.renpin.DynamicDetailActivity.strImplementName",
						strImplementName);
				it.putExtra("com.renpin.RenPinMainActivity.TaskId", strTaskId);
				it.putExtra("com.renpin.RenPinMainActivity.TaskType", "1");
				startActivityForResult(it, 2);
			}
		});

		resendbutton.setOnClickListener(new OnClickListener() {

			Runnable runre = new Runnable() {
				public void run() {
					int nRet = 0;

					// �����������������Ϊû�����»�����״̬
					nRet = goodService.ResetTaskStatue(strTaskId, "1");

					String strOperMsg;
					if (1 == nRet) {
						strOperMsg = "�����ɹ�";
					} else {
						strOperMsg = "����ʧ��";
					}
					Message msg = myhandler.obtainMessage();
					msg.what = 9;
					Bundle b = new Bundle();// �������
					b.putString("OperMsg", strOperMsg);
					msg.setData(b);
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						DynamicDetailActivity.this);
				dialog.setMessage("�������񽫻ָ�������ʱ�ĳ�ʼ״̬");
				dialog.setTitle("��ʾ");
				dialog.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// ��������״̬�����߳�
								Thread thread = new Thread(runre);
								thread.start();
							}
						}).setNegativeButton("ȡ��",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				dialog.show();
			}
		});

		sendpictureimageview.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(0);
			}
		});

		imageview3.setOnClickListener(new OnClickListener() {

			Runnable GetCommentRun = new Runnable() {
				public void run() {
					String strAccountNameTemp = msettings.getString(
							"TruePersonName", "");

					mNewCommentInfos = goodService.GetCommentsForTaskNew1(
							strTaskId, "1", strAccountNameTemp, strCommentTime);
					Message msg = myhandler.obtainMessage();
					msg.what = 3;
					myhandler.sendMessage(msg);

					int nRet = 0;

					// ����ִ��������״̬
					nRet = goodService.UpdateCommentType(strAccountNameTemp, 3,
							strTaskId, "1");
				}
			};

			@Override
			public void onClick(View v) {
				// ������ȡ���������߳�
				Thread thread = new Thread(GetCommentRun);
				thread.start();
			}
		});

		button5.setOnClickListener(new OnClickListener() {

			Runnable SendCommentContent = new Runnable() {
				public void run() {
					// ��ǰ�û�����
					String strCurrentName = msettings.getString(
							"TruePersonName", "");
					// ��ǰ�û�����(�ǳ�)
					String strCurrentNickName = msettings.getString(
							"PersonName", "");
					// ��ǰ�û�ͼ��
					String strCurrentImage = msettings.getString("Base64Image",
							"");
					String strContent1 = DynamicDetailActivity.this.edittext1
							.getText().toString();
					// ȥ����ǰ��ո���ַ���
					String strClearSpace = CommonUtils
							.ClearFrontSpace(strContent1);
					char cFirstChar = strClearSpace.charAt(0);
					// �ж��Ƿ������Ļ�
					int nSecretType = 0;
					if ('*' == cFirstChar) {
						nSecretType = 1;
					}
					String zhengze = "f0[0-9]{2}|f10[0-7]";
					SpannableString spannableString = ExpressionUtil
							.getExpressionString(DynamicDetailActivity.this,
									strClearSpace, zhengze);
					String strOtherPersonName = "";
					String strOtherPersonNickName = "";
					String strTime = "";
					if (bIsTalkToPerson) {
						// ��������
						strTime = goodService.SendCommentContentNewSecretTime(
								strTaskId, strCurrentName, strReceiveName,
								spannableString.toString(), "1",
								strCurrentNickName, strReceiveNickName,
								nSecretType);
						strOtherPersonName = strReceiveName;
						strOtherPersonNickName = strReceiveNickName;
					} else {
						// �����Ϣ�����˷����˵�ǰ��������,��ô������Ϣ�Ͳ���֪ͨ������
						if (!strCurrentName.equals(strAccountName1)) {
							// ��������
							strTime = goodService
									.SendCommentContentNewSecretTime(strTaskId,
											strCurrentName, strAccountName1,
											spannableString.toString(), "1",
											strCurrentNickName,
											strCustomerNameNick, nSecretType);
							strOtherPersonName = strAccountName1;
							strOtherPersonNickName = strCustomerNameNick;
						} else {
							// ��������
							strTime = goodService.SendCommentContentNewSecretTime(
									strTaskId, strCurrentName, "",
									spannableString.toString(), "1",
									strCurrentNickName, "", nSecretType);
						}
					}
					if (!strTime.equals("")) {

						// CreateDatabaseAndStoreData();
						// �����������ݱ��浽�������ݿ���
						/*
						 * mDatabase.InsertCommentData(
						 * Integer.parseInt(strTaskId), 1, 0, strCurrentImage,
						 * strCurrentNickName, strOtherPersonNickName, strTime,
						 * spannableString.toString(), strCurrentName,
						 * strOtherPersonName, "", nSecretType);
						 */

						CommentInfo commentinfo = new CommentInfo(
								strCurrentImage, strCurrentNickName,
								strOtherPersonNickName, strTime,
								spannableString.toString(), "", 0,
								strCurrentName, strOtherPersonName);
						mNewCommentInfos.clear();
						mNewCommentInfos.add(commentinfo);
						Message msg1 = myhandler.obtainMessage();
						msg1.what = 10;
						myhandler.sendMessage(msg1);
					}

					String strOperMsg;
					if (!strTime.equals("")) {
						strOperMsg = "�����ɹ�";
					} else {
						strOperMsg = "����ʧ��";
					}
					Message msg = myhandler.obtainMessage();
					msg.what = 4;
					Bundle b = new Bundle();// �������
					b.putString("OperMsg", strOperMsg);
					msg.setData(b);
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {
				String strContent1 = DynamicDetailActivity.this.edittext1
						.getText().toString();
				if (!strContent1.equals("")) {
					button5.setText("���ڷ���...");
					button5.setEnabled(false);
					button5.setTextColor(Color.rgb(96, 96, 96));
					// ������ȡ���������߳�
					Thread thread = new Thread(SendCommentContent);
					thread.start();
				} else {
					CommonUtils.ShowToastCenter(getBaseContext(), "���͵����ݲ���Ϊ��",
							Toast.LENGTH_LONG);
				}
			}
		});

		imagetask1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DynamicDetailActivity.this,
						ViewPagerActivity.class);
				intent.putExtra("type", 1);
				intent.putExtra("iconposion", 0);
				startActivity(intent);
			}

		});

		imagetask2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DynamicDetailActivity.this,
						ViewPagerActivity.class);
				intent.putExtra("type", 1);
				intent.putExtra("iconposion", 1);
				startActivity(intent);
			}

		});

		imagetask3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DynamicDetailActivity.this,
						ViewPagerActivity.class);
				intent.putExtra("type", 1);
				intent.putExtra("iconposion", 2);
				startActivity(intent);
			}

		});

		imagetask4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DynamicDetailActivity.this,
						ViewPagerActivity.class);
				intent.putExtra("type", 1);
				intent.putExtra("iconposion", 3);
				startActivity(intent);
			}

		});

		imagetask5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DynamicDetailActivity.this,
						ViewPagerActivity.class);
				intent.putExtra("type", 1);
				intent.putExtra("iconposion", 4);
				startActivity(intent);
			}

		});

		imagetask6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DynamicDetailActivity.this,
						ViewPagerActivity.class);
				intent.putExtra("type", 1);
				intent.putExtra("iconposion", 5);
				startActivity(intent);
			}

		});

		imageview10.setOnClickListener(new OnClickListener() {

			Runnable runimageview10 = new Runnable() {
				public void run() {
					if (strAccountPersonToRecImage.equals("")) {
						strAccountPersonToRecImage = goodService
								.GetTaskVerifiLargeImage(strTaskId, "1");
					}
					Message msg = myhandler.obtainMessage();
					msg.what = 6;
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {
				// ������ȡ���������߳�
				Thread thread = new Thread(runimageview10);
				thread.start();
			}

		});

		zanimageviewlayout.setOnClickListener(new OnClickListener() {

			Runnable zanrun = new Runnable() {
				public void run() {
					int nRet = 0;
					// �Ȼ�ȡ��ǰ�û�����
					String strCurName = msettings.getString("TruePersonName",
							"");
					// �����Լ����Լ���
					if (!strAccountName1.equals(strCurName)) {

						nRet = goodService.PraiseToTaskOrShare(strTaskId,
								strCurName, strAccountName1, "1");
					} else {
						nRet = 3;// ��ʾ���ܸ��Լ���
					}
					Message msg = myhandler.obtainMessage();
					Bundle b = new Bundle();// �������
					b.putInt("nRet", nRet);
					msg.setData(b);
					msg.what = 12;
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {
				Thread thre = new Thread(zanrun);
				thre.start();
			}
		});

		textview5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String strName = msettings.getString("TruePersonName", "");
				if (strName.equals(strCustomerNameTemp)) {
					final String strDetialText = textview5.getText().toString();
					AboutDialog.SetInitialText(strDetialText);
					AboutDialog aboutDialog = new AboutDialog(
							DynamicDetailActivity.this, 4,
							new PriorityListener() {

								String strTextTemp = "";
								Runnable updaterun = new Runnable() {
									public void run() {
										int nRet = goodService
												.UpdateItemTitleText(strTaskId,
														"1", strTextTemp);

										Message msg = myhandler.obtainMessage();
										Bundle b = new Bundle();// �������
										b.putInt("nRet", nRet);
										b.putInt("nType", 1);
										b.putString("strtext", strTextTemp);
										msg.setData(b);
										msg.what = 14;
										myhandler.sendMessage(msg);
									}
								};

								@Override
								public void refreshPriorityUI(String str1,
										String str2, String str3, String str4) {
									// �ж��ǲ��ǵ���˱��水ť
									if (str1.equals("1")) {
										// ��������޸���
										if (!str2.equals(strDetialText)) {
											strTextTemp = str2;
											// �����޸����ݵ��߳�
											Thread thre = new Thread(updaterun);
											thre.start();
										} else {
											Message msg = myhandler
													.obtainMessage();
											Bundle b = new Bundle();// �������
											b.putInt("nRet", 1);
											b.putInt("nType", 1);
											b.putString("strtext", str2);
											msg.setData(b);
											msg.what = 14;
											myhandler.sendMessage(msg);
										}
									}
								}

							});
					aboutDialog.show();
				}
			}

		});

		textview6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String strName = msettings.getString("TruePersonName", "");
				if (strName.equals(strCustomerNameTemp)) {
					final String strDetialText = textview6.getText().toString();
					AboutDialog.SetInitialText(strDetialText);
					AboutDialog aboutDialog = new AboutDialog(
							DynamicDetailActivity.this, 4,
							new PriorityListener() {

								String strTextTemp = "";
								Runnable updaterun = new Runnable() {
									public void run() {
										int nRet = goodService
												.UpdateItemDetailText(
														strTaskId, "1",
														strTextTemp);

										Message msg = myhandler.obtainMessage();
										Bundle b = new Bundle();// �������
										b.putInt("nRet", nRet);
										b.putInt("nType", 2);
										b.putString("strtext", strTextTemp);
										msg.setData(b);
										msg.what = 14;
										myhandler.sendMessage(msg);
									}
								};

								@Override
								public void refreshPriorityUI(String str1,
										String str2, String str3, String str4) {
									// �ж��ǲ��ǵ���˱��水ť
									if (str1.equals("1")) {
										// ��������޸���
										if (!str2.equals(strDetialText)) {
											strTextTemp = str2;
											// �����޸����ݵ��߳�
											Thread thre = new Thread(updaterun);
											thre.start();
										} else {
											Message msg = myhandler
													.obtainMessage();
											Bundle b = new Bundle();// �������
											b.putInt("nRet", 1);
											b.putInt("nType", 2);
											b.putString("strtext", str2);
											msg.setData(b);
											msg.what = 14;
											myhandler.sendMessage(msg);
										}
									}
								}

							});
					aboutDialog.show();
				}
			}

		});

		ShareImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ��ȡ��ĻͼƬ
				ScreenShot screen = new ScreenShot(DynamicDetailActivity.this);
				Bitmap screenbitmap = screen.GetCurrenScreenBitmap();
				if (screenbitmap != null) {
					// ��ȡ����ͼƬ�ı���·��
					String strFilePath = screen
							.GetImageFilePath(MyApplication.sharefile);
					// ��ͼƬ���浽ָ����·����
					try {
						mUtils.saveBitmapToFile(screenbitmap, strFilePath);
					} catch (IOException e) {
						e.printStackTrace();
					}
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("image/*");
					intent.putExtra(Intent.EXTRA_SUBJECT, "����");
					intent.putExtra(Intent.EXTRA_STREAM,
							Uri.fromFile(new File(strFilePath)));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(Intent.createChooser(intent, getTitle()));
				} else {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"����ʧ��", Toast.LENGTH_LONG);
				}
			}

		});
	}

	// �����û���̬
	private void UpdateDynamicStatus(final String strAnnounceName,
			final String strImpleName, final int nValue,
			final String strTaskId, final int nTaskSelectType,
			final int nTaskFinishType, final int nTaskVerifiType,
			final int nTaskAnnounceCommentType,
			final int nTaskImplementCommentType) {

		Runnable runup = new Runnable() {
			public void run() {
				int nRet = 0;
				String strAccountNameTemp = msettings.getString(
						"TruePersonName", "");

				if (2 == nTaskAnnounceCommentType) {
					// ���·���������״̬
					nRet = goodService.UpdateCommentType(strAccountNameTemp,
							nValue, strTaskId, "1");
				}
			}
		};
		// ��������״̬�����߳�
		Thread thread = new Thread(runup);
		thread.start();
	}

	private void CombineCommentData() {
		int nSize1 = mNewCommentInfos.size();
		int nSize2 = mCommentInfos.size();
		if (nSize1 > 0) {
			// ������������ʱ��
			strCommentTime = mNewCommentInfos.get(nSize1 - 1)
					.getstrCommentTime();
		}
		for (int i = 0; i < nSize1; i++) {
			boolean bIsFind = false;
			for (int j = 0; j < nSize2; j++) {
				// ����������ݡ�ʱ�䡢�����ˡ���������ͬ,��ô��˵����ͬһ������
				if (mNewCommentInfos.get(i).getstrCommentContent()
						.equals(mCommentInfos.get(j).getstrCommentContent())
						&& mNewCommentInfos
								.get(i)
								.getstrCommentTime()
								.equals(mCommentInfos.get(j)
										.getstrCommentTime())
						&& mNewCommentInfos
								.get(i)
								.getstrCommentPersonTrueName()
								.equals(mCommentInfos.get(j)
										.getstrCommentPersonTrueName())
						&& mNewCommentInfos
								.get(i)
								.getstrCommentReceivePersonTrueName()
								.equals(mCommentInfos.get(j)
										.getstrCommentReceivePersonTrueName())) {
					mNewCommentInfos.remove(i);
					nSize1 -= 1;
					i -= 1;
					bIsFind = true;
					break;
				}
			}
			// ���û���ҵ�,��ô�ͽ��������ݲ��뵽�������ݿ���
			if (!bIsFind && nSize2 > 0) {
				mCommentInfos.add(mNewCommentInfos.get(i));
				int nSecretType = 0;
				// ���Ϊ��������
				if (0 == mNewCommentInfos.get(i).getnCommentIndex()) {
					String strContent1 = mNewCommentInfos.get(i)
							.getstrCommentContent();
					// ȥ����ǰ��ո���ַ���
					String strClearSpace = CommonUtils
							.ClearFrontSpace(strContent1);
					if (strClearSpace.length() > 0) {
						char cFirstChar = strClearSpace.charAt(0);
						// �ж��Ƿ������Ļ�
						if ('*' == cFirstChar) {
							nSecretType = 1;
						}
					}
				}
				// �����ݱ��浽�������ݿ���
				mDatabase
						.InsertCommentData(Integer.parseInt(strTaskId), 1,
								mNewCommentInfos.get(i).getnCommentIndex(),
								mNewCommentInfos.get(i)
										.getstrCommentPersonImage(),
								mNewCommentInfos.get(i)
										.getstrCommentPersonName(),
								mNewCommentInfos.get(i)
										.getstrCommentReceivePersonName(),
								mNewCommentInfos.get(i).getstrCommentTime(),
								mNewCommentInfos.get(i).getstrCommentContent(),
								mNewCommentInfos.get(i)
										.getstrCommentPersonTrueName(),
								mNewCommentInfos.get(i)
										.getstrCommentReceivePersonTrueName(),
								mNewCommentInfos.get(i).getstrSmallImage(),
								nSecretType);
			}
			if (nSize2 <= 0) {
				int nSecretType = 0;
				// ���Ϊ��������
				if (0 == mNewCommentInfos.get(i).getnCommentIndex()) {
					String strContent1 = mNewCommentInfos.get(i)
							.getstrCommentContent();
					// ȥ����ǰ��ո���ַ���
					String strClearSpace = CommonUtils
							.ClearFrontSpace(strContent1);
					if (strClearSpace.length() > 0) {
						char cFirstChar = strClearSpace.charAt(0);
						// �ж��Ƿ������Ļ�
						if ('*' == cFirstChar) {
							nSecretType = 1;
						}
					}
				}
				// �����ݱ��浽�������ݿ���
				mDatabase
						.InsertCommentData(Integer.parseInt(strTaskId), 1,
								mNewCommentInfos.get(i).getnCommentIndex(),
								mNewCommentInfos.get(i)
										.getstrCommentPersonImage(),
								mNewCommentInfos.get(i)
										.getstrCommentPersonName(),
								mNewCommentInfos.get(i)
										.getstrCommentReceivePersonName(),
								mNewCommentInfos.get(i).getstrCommentTime(),
								mNewCommentInfos.get(i).getstrCommentContent(),
								mNewCommentInfos.get(i)
										.getstrCommentPersonTrueName(),
								mNewCommentInfos.get(i)
										.getstrCommentReceivePersonTrueName(),
								mNewCommentInfos.get(i).getstrSmallImage(),
								nSecretType);
			}
		}
		if (nSize2 <= 0) {
			mCommentInfos = mNewCommentInfos;
		}
	}

	// ��ͼƬ���浽����
	private void SaveImageToLocal() {
		// �洢ͼƬ�ļ���·��
		String strImageFilePath = "";
		// ��Ŀ¼·��
		String strRootPath = mUtils.GetFileRootPath();
		Bitmap bmp = null;
		for (int i = 2; i <= mnImageNum; i++) {
			strImageFilePath = strRootPath + strTaskId + "_" + 1 + "_" + i;
			if (2 == i) {
				bmp = mUtils.base64ToBitmap(taskicon.getstrIcon2());
			} else if (3 == i) {
				bmp = mUtils.base64ToBitmap(taskicon.getstrIcon3());
			} else if (4 == i) {
				bmp = mUtils.base64ToBitmap(taskicon.getstrIcon4());
			} else if (5 == i) {
				bmp = mUtils.base64ToBitmap(taskicon.getstrIcon5());
			} else if (mnImageNum == i) {
				bmp = mUtils.base64ToBitmap(taskicon.getstrIcon6());
			}
			try {
				mUtils.saveBitmapToFileNew(bmp, strImageFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (int i = 1; i <= mnImageNum; i++) {
			strImageFilePath = strRootPath + strTaskId + "_" + 1 + "_" + i
					+ "_" + "l";
			bmp = mUtils.base64ToBitmap(strIcons[i - 1]);
			try {
				mUtils.saveBitmapToFileNew(bmp, strImageFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class MyHandler extends Handler {
		// ���������д�˷���,��������
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Bundle b = msg.getData();
				String strMsg = b.getString("Msg");
				Toast.makeText(DynamicDetailActivity.this, strMsg,
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				if (mnRemainTime >= 0) {
					String strTime = mUtils.GetStringBySeconds(mnRemainTime);
					textview4.setText(strTime);
					mnRemainTime -= 1;
					if (mnRemainTime <= 0) {
					}
				} else if (mnRemainTime < 0) {

				}
				Bundle bhot = msg.getData();
				String hottype = bhot.getString("nShowHot");
				int nHotType = Integer.parseInt(hottype);
				if (1 == nHotType) {
					Resources res = getResources();
					Bitmap bmp = BitmapFactory.decodeResource(res,
							R.drawable.red_oval);
					imageview5.setImageBitmap(bmp);
					if (mCommentInfos != null) {
						// ������������
						textview9.setText("" + mCommentInfos.size());
					} else {
						// ������������
						textview9.setText("0");
					}
				} else {
					imageview5.setImageBitmap(null);
				}

				// ��ʾ��������״̬
				// ShowTaskRunStatus();
				// ʵʱ��̬��ʾ�������й���
				// UpdateStatusText(strTaskId);
				break;

			case 2:
				break;
			case 3:

				credittextview.setText("��Ʒ:" + mnCredit);
				charmvaluetextview.setText(mnCharmValue + "");
				zantextview.setText(nTaskCharmValue + "");

				if (taskvalue != null) {
					String strText = "�����������,����Ʒֵ+";
					strText += taskvalue.getnCreditValue();
					strText += "����+";
					strText += taskvalue.getnCharmValue();
					taskendtextview.setText(strText);
				}

				break;
			case 4:
				Bundle b2 = msg.getData();
				String strOperMsg1 = b2.getString("OperMsg");
				Toast.makeText(DynamicDetailActivity.this, strOperMsg1,
						Toast.LENGTH_LONG).show();
				button5.setText("����");
				button5.setEnabled(true);
				button5.setTextColor(Color.rgb(255, 255, 255));
				edittext1.setHint("����");
				edittext1.setText("");
				// �����˱��˵������Ὣ��������ʾ��"�ҵ�"��
				RenPinMainActivity.UpdateDynamicData(strTaskId, 1);
				break;
			case 5:
				Intent intent = new Intent(DynamicDetailActivity.this,
						TaskFinishOrVertifiImage.class);
				intent.putExtra("type", 1);// 1��ʾ����,2��ʾ����
				intent.putExtra("icontype", 1);// ͼƬ����,1��ʾִ�и�����,2��ʾ������ִ��
				startActivity(intent);
				break;
			case 6:
				Intent intent1 = new Intent(DynamicDetailActivity.this,
						TaskFinishOrVertifiImage.class);
				intent1.putExtra("type", 1);
				intent1.putExtra("icontype", 2);
				startActivity(intent1);
				break;
			case 7:
				Bundle b3 = msg.getData();
				String strCommentImage = b3.getString("strLargeImage");
				//int nCommentIndex = b3.getInt("commentindex");
				// CommentMaps.put("" + nCommentIndex, strCommentImage);
				// ��ʾͼƬ
				Intent intent3 = new Intent(DynamicDetailActivity.this,
						CommentActivity.class);
				//intent3.putExtra("nCommentIndex", nCommentIndex);
				//intent3.putExtra("nType", 1);
				intent3.putExtra("strLargeImage", strCommentImage);

				startActivity(intent3);
				break;
			case 8:
				// ȡ���ȴ���
				if (m_ProgressDialog != null) {
					m_ProgressDialog.dismiss();
					m_ProgressDialog = null;
				}

				Bundle b4 = msg.getData();
				int nCommentRet = b4.getInt("nRet");
				if (0 == nCommentRet) {
					CommonUtils.ShowToastCenter(getBaseContext(), "����ʧ��",
							Toast.LENGTH_LONG);
				} else {
					CommonUtils.ShowToastCenter(getBaseContext(), "���ͳɹ�",
							Toast.LENGTH_LONG);
				}
				// �����˱��˵������Ὣ��������ʾ��"�ҵ�"��
				RenPinMainActivity.UpdateDynamicData(strTaskId, 1);
				break;
			case 9:
				Bundle b5 = msg.getData();
				String strResetTaskMsg = b5.getString("OperMsg");
				CommonUtils.ShowToastCenter(getBaseContext(), strResetTaskMsg,
						Toast.LENGTH_LONG);
				break;
			case 10:
				if (mNewCommentInfos != null) {
					// ��̬�������۵Ľ���
					AutoCreateCommentWidows(mNewCommentInfos);
					scrollview.fullScroll(ScrollView.FOCUS_DOWN);
					mCommentInfos.add(mNewCommentInfos.get(0));
					int nSize = mCommentInfos.size() + 1;
					talktextview.setText(nSize + "");
				}
				break;
			case 11:
				Bundle b9 = msg.getData();
				int nRetvalue = b9.getInt("nRet");
				if (0 == nRetvalue && taskicon != null) {
					SaveImageToLocal();
				}
				if (taskicon != null) {
					// ���û��ͼƬ,��ô���ؿؼ�
					if (taskicon.getstrIcon1().length() < 10
							&& taskicon.getstrIcon2().length() < 10
							&& taskicon.getstrIcon3().length() < 10) {
						smalliconslinearlayout1.setVisibility(View.GONE);
					} else {
						smalliconslinearlayout1.setVisibility(View.VISIBLE);
						imagetask1.setImageBitmap(mUtils
								.base64ToBitmap(taskicon.getstrIcon1()));
						imagetask2.setImageBitmap(mUtils
								.base64ToBitmap(taskicon.getstrIcon2()));
						imagetask3.setImageBitmap(mUtils
								.base64ToBitmap(taskicon.getstrIcon3()));
						imagetask1.setClickable(true);
						imagetask2.setClickable(true);
						imagetask3.setClickable(true);
					}
					if (taskicon.getstrIcon4().length() < 10
							&& taskicon.getstrIcon5().length() < 10
							&& taskicon.getstrIcon6().length() < 10) {
						smalliconslinearlayout2.setVisibility(View.GONE);
					} else {
						smalliconslinearlayout2.setVisibility(View.VISIBLE);
						imagetask4.setImageBitmap(mUtils
								.base64ToBitmap(taskicon.getstrIcon4()));
						imagetask5.setImageBitmap(mUtils
								.base64ToBitmap(taskicon.getstrIcon5()));
						imagetask6.setImageBitmap(mUtils
								.base64ToBitmap(taskicon.getstrIcon6()));
						imagetask4.setClickable(true);
						imagetask5.setClickable(true);
						imagetask6.setClickable(true);
					}
				} else {
					smalliconslinearlayout1.setVisibility(View.GONE);
					smalliconslinearlayout2.setVisibility(View.GONE);
				}
				break;
			case 12:
				Bundle b6 = msg.getData();
				int nRet = b6.getInt("nRet");
				String strMsg1 = "";
				if (0 == nRet) {
					strMsg1 = "����ʧ��";
				} else if (1 == nRet) {
					strMsg1 = "�����ɹ�";
					int nSumCharmValue = mnCharmValue + 1;
					charmvaluetextview.setText(nSumCharmValue + "");
					nSumCharmValue = nTaskCharmValue + 1;
					zantextview.setText(nSumCharmValue + "");
				} else if (2 == nRet) {
					strMsg1 = "�Ѿ��޹�";
				} else if (3 == nRet) {
					strMsg1 = "���ܸ��Լ���Ŷ";
				}
				CommonUtils.ShowToastCenter(getBaseContext(), strMsg1,
						Toast.LENGTH_LONG);
				break;
			case 13:
				Bundle b7 = msg.getData();
				int nRet1 = b7.getInt("nRet");
				if (nRet1 > 0) {
					looktimetextview.setText(nRet1 + "");
					bIsAddBrowseTime = true;
				} else {
					looktimetextview.setText("0");
				}
				break;
			case 14:
				Bundle b8 = msg.getData();
				int nRet2 = b8.getInt("nRet");
				int nType = b8.getInt("nType");
				String strText = b8.getString("strtext");
				if (nRet2 > 0) {
					if (1 == nType) {
						textview5.setText(strText);
						RenPinMainActivity.UpdateAccountPersonInfo(strTaskId,
								1, 1, strText);
					} else if (2 == nType) {
						textview6.setText(strText);
						RenPinMainActivity.UpdateAccountPersonInfo(strTaskId,
								1, 2, strText);
					}
					CommonUtils.ShowToastCenter(getBaseContext(), "�޸ĳɹ�",
							Toast.LENGTH_LONG);
				} else {
					CommonUtils.ShowToastCenter(getBaseContext(), "�޸�ʧ��",
							Toast.LENGTH_LONG);
				}
				break;
			case 15:
				// �������������ݺϲ�������������
				CombineCommentData();
				if (mCommentInfos != null) {
					// ��̬�������۵Ľ���
					AutoCreateCommentWidows(mCommentInfos);
					talktextview.setText(mCommentInfos.size() + "");
				} else {
					if (!isConnectInternet()) {
						Toast.makeText(DynamicDetailActivity.this, "���粻����Ŷ!",
								Toast.LENGTH_LONG).show();
					}
				}
				break;
			}
		}
	}

	private void AutoCreateCommentWidows(List<CommentInfo> comment) {

		int nCount = comment.size();
		// �Ƚ�linearlayout1���
		// linearlayout1.removeAllViews();
		if (nCount <= 0) {
			textview9.setText("��������");
		} else {
			// ������������
			textview9.setText("" + comment.size());

			for (int i = 0; i < nCount; i++) {

				final CommentInfo commentinfo = comment.get(i);

				View cfg_view = getLayoutInflater().inflate(
						R.layout.comment_item, null);

				// ͼ��
				ImageView imageview = (ImageView) cfg_view
						.findViewById(R.id.comment_item_imageview1);
				// ����������
				TextView AccountPerosn = (TextView) cfg_view
						.findViewById(R.id.comment_item_textview1);
				// ����������
				TextView AccountPerosnTrueName = (TextView) cfg_view
						.findViewById(R.id.comment_item_textviewtruename);
				// ��������������
				TextView ReceivePerson = (TextView) cfg_view
						.findViewById(R.id.comment_item_textview3);
				// ����ʱ��
				TextView CommentTime = (TextView) cfg_view
						.findViewById(R.id.comment_item_textview4);

				// ��������
				TextView Contenttext = (TextView) cfg_view
						.findViewById(R.id.comment_item_textview5);
				// ����ͼƬ
				ImageView largeimageview = (ImageView) cfg_view
						.findViewById(R.id.comment_item_imageview2);

				TextView textview10 = (TextView) cfg_view
						.findViewById(R.id.comment_item_textview2);

				Bitmap image = mUtils.base64ToBitmap(commentinfo
						.getstrCommentPersonImage());
				// ���û��ͼ��,��ô����Ĭ��ͼ��
				if (null == image) {
					Resources res = getResources();
					image = BitmapFactory.decodeResource(res,
							R.drawable.noperson);
				}
				imageview.setImageBitmap(image);
				AccountPerosn.setText(commentinfo.getstrCommentPersonName());
				AccountPerosnTrueName.setText(commentinfo
						.getstrCommentPersonTrueName());
				// ���û�н���������ʾ˭�ظ�˭
				if (commentinfo.getstrCommentReceivePersonName().equals("")) {
					textview10.setText("");
				} else {
					textview10.setText("�ظ�");
				}
				ReceivePerson.setText(commentinfo
						.getstrCommentReceivePersonName());
				CommentTime.setText(commentinfo.getstrCommentTime());
				// �ж�������ͼƬ��ʽ����������ʽ
				if (commentinfo.getnCommentIndex() > 0) {
					Contenttext.setVisibility(View.GONE);
					largeimageview.setVisibility(View.VISIBLE);
					// ���۲���ͼƬ��ʽ
					largeimageview.setImageBitmap(mUtils
							.base64ToBitmap(commentinfo.getstrSmallImage()));
				} else {// ���۲���������ʽ
					largeimageview.setVisibility(View.GONE);
					// �������Ϊ��,˵��������������Ļ�
					if (commentinfo.getstrCommentContent().equals("")) {
						Contenttext.setTextColor(Color.rgb(250, 60, 70));
						Contenttext.setText("*���Ļ�");
					} else {
						Contenttext.setVisibility(View.VISIBLE);
						String zhengze = "f0[0-9]{2}|f10[0-7]";
						// ������ת����ͼ����ʽ
						SpannableString spancontent = ExpressionUtil
								.getExpressionString(
										DynamicDetailActivity.this,
										commentinfo.getstrCommentContent(),
										zhengze);
						Contenttext.setTextColor(Color.rgb(105, 105, 105));
						Contenttext.setText(spancontent);
					}

				}
				linearlayout1.addView(cfg_view);

				largeimageview.setOnClickListener(new OnClickListener() {
					Runnable runcomment = new Runnable() {
						public void run() {
							// �ȵ��������ݿ�����
							String strCommentImage = "";
							strCommentImage = goodService
									.GetCommentsLargeImage(strTaskId, "1",
											commentinfo.getnCommentIndex());
							if (!strCommentImage.equals("")) {
								// ��ͼƬ���浽�������ݿ���
								mDatabase.SetCommentLargeImage(
										Integer.parseInt(strTaskId), 1,
										commentinfo.getnCommentIndex(),
										strCommentImage);
							}
							if (!strCommentImage.equals("")) {
								//CommentMaps.put(commentinfo.getnCommentIndex()
								//		+ "", strCommentImage);
								Message msg = myhandler.obtainMessage();
								Bundle b = new Bundle();
								b.putInt("commentindex",
										commentinfo.getnCommentIndex());
								b.putString("strLargeImage", strCommentImage);
								msg.setData(b);
								msg.what = 7;
								myhandler.sendMessage(msg);
							}

						}
					};

					@Override
					public void onClick(View v) {
						// ���ж����۴�ͼƬ�����Ƿ�����������
						int nIndex = commentinfo.getnCommentIndex();
						// ���ж����۴�ͼƬ�����Ƿ�����������
						String strImage = null;
						// �ȵ��������ݿ�����
						strImage = mDatabase.GetCommentLargeImage(
								Integer.parseInt(strTaskId), 1, nIndex);
						// ���֮ǰû�����ظ�ͼƬ,��ô������
						if (strImage.equals("")) {
							// ������ȡ���������߳�
							Thread thread = new Thread(runcomment);
							thread.start();
						} else {// ֱ����ʾͼƬ
							//CommentMaps.put(nIndex + "", strImage);

							Intent intent = new Intent(
									DynamicDetailActivity.this,
									CommentActivity.class);
							//intent.putExtra("nCommentIndex",
							//		commentinfo.getnCommentIndex());
							//intent.putExtra("nType", 1);
							intent.putExtra("strLargeImage", strImage);

							startActivity(intent);
						}
					}

				});
				// ��������¼�
				cfg_view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String strCommentPersonName;// ����������
						String strCommentPersonNickName;// ����������(�ǳ�)
						// ����������
						TextView AccountPerosn = (TextView) v
								.findViewById(R.id.comment_item_textviewtruename);

						strCommentPersonName = AccountPerosn.getText()
								.toString();
						// ����������(�ǳ�)
						TextView AccountPerosnNick = (TextView) v
								.findViewById(R.id.comment_item_textview1);

						strCommentPersonNickName = AccountPerosnNick.getText()
								.toString();

						String strCurrentName = msettings.getString(
								"TruePersonName", "");
						// ���ָ���ظ������Լ�,��ô�Ͳ���ָ������
						if (!strCommentPersonName.equals(strCurrentName)) {
							String strContent = "�ظ�" + strCommentPersonNickName;
							strReceiveName = strCommentPersonName;
							strReceiveNickName = strCommentPersonNickName;
							edittext1.setHint(strContent);
							bIsTalkToPerson = true;
						} else {
							edittext1.setHint("������ֻظ��Է�");
							bIsTalkToPerson = false;
						}
					}
				});
			}
		}
	}

	public boolean isConnectInternet() {

		ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

		if (networkInfo != null) { // ����ж�һ��Ҫ���ϣ�Ҫ��Ȼ�����
			return networkInfo.isAvailable();
		}
		return false;
	}

	// ��ʼ������������������ݵ�����
	Runnable run1 = new Runnable() {
		public void run() {
			while (mbIsContinue) {
				int nHotType = 0;

				String strAccountNameTemp = msettings.getString(
						"TruePersonName", "");
				// �ж��Ƿ������µ�����
				if (IsShowHotDot(strTaskId, strAccountNameTemp)) {
					nHotType = 1;
				}
				Message msg = myhandler.obtainMessage();
				Bundle b = new Bundle();// �������
				b.putString("nShowHot", "" + nHotType);
				msg.setData(b);
				msg.what = 1;
				myhandler.sendMessage(msg);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	};

	// ��ʼ���������������ͼƬ
	Runnable commentrun = new Runnable() {
		public void run() {
			// ��ǰ�û�����
			String strCurrentName = msettings.getString("TruePersonName", "");
			// ��ǰ�û��ǳ�
			String strCurrentNickName = msettings.getString("PersonName", "");
			// ��ǰ�û�ͼ��
			String strCurrentImage = msettings.getString("Base64Image", "");

			String strOtherPersonName = "";
			String strOtherPersonNickName = "";
			String strTime = "";
			if (bIsTalkToPerson) {
				strTime = goodService.SendCommentImageNewTime(strTaskId,
						strCurrentName, strReceiveName, strCommentSmallImage,
						strCommentLargeImage, "1", strCurrentNickName,
						strReceiveNickName);
				strOtherPersonName = strReceiveName;
				strOtherPersonNickName = strReceiveNickName;
			} else {
				if (!strCurrentName.equals(strAccountName1)) {
					strTime = goodService.SendCommentImageNewTime(strTaskId,
							strCurrentName, strAccountName1,
							strCommentSmallImage, strCommentLargeImage, "1",
							strCurrentNickName, strCustomerNameNick);
					strOtherPersonName = strAccountName1;
					strOtherPersonNickName = strCustomerNameNick;
				} else {
					strTime = goodService.SendCommentImageNewTime(strTaskId,
							strCurrentName, "", strCommentSmallImage,
							strCommentLargeImage, "1", strCurrentNickName, "");
				}
			}
			// ���ͼƬ���ͳɹ�
			if (!strTime.equals("")) {

				// CreateDatabaseAndStoreData();
				// �����������ݱ��浽�������ݿ���
				/*
				 * mDatabase.InsertCommentData(Integer.parseInt(strTaskId), 1,
				 * mCommentInfos.size() + 1, strCurrentImage,
				 * strCurrentNickName, strOtherPersonNickName, strTime, "",
				 * strCurrentName, strOtherPersonName, strCommentSmallImage, 0);
				 */
				// �����ͼ
				mDatabase.SetCommentLargeImage(Integer.parseInt(strTaskId), 1,
						mCommentInfos.size() + 1, strCommentLargeImage);

				CommentInfo commentinfo = new CommentInfo(strCurrentImage,
						strCurrentNickName, strOtherPersonNickName, strTime,
						"", strCommentSmallImage, mCommentInfos.size() + 1,
						strCurrentName, strOtherPersonName);
				mNewCommentInfos.clear();
				mNewCommentInfos.add(commentinfo);
				Message msg1 = myhandler.obtainMessage();
				msg1.what = 10;
				myhandler.sendMessage(msg1);
			}

			Message msg = myhandler.obtainMessage();
			Bundle b = new Bundle();
			b.putInt("nRet", 1);
			msg.setData(b);
			msg.what = 8;
			myhandler.sendMessage(msg);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Bitmap bitmap = null;
		if (requestCode == 0) {
			String strImagePath = Environment.getExternalStorageDirectory()
					+ "/" + "carPhoto" + "/" + "Car.jpg";
			// ���жϸ��ļ��Ƿ����
			File pImageFile = new File(strImagePath);
			if (pImageFile.exists()) {
				// ��ͼƬ������ת����
				mUtils.rotatePhoto(strImagePath);

				File imgFile = new File(strImagePath);
				try {
					Uri imgUri = Uri
							.parse(android.provider.MediaStore.Images.Media
									.insertImage(getContentResolver(),
											imgFile.getAbsolutePath(), null,
											null));
					String[] proj = { MediaStore.Images.Media.DATA };
					@SuppressWarnings("deprecation")
					Cursor cursor = managedQuery(imgUri, proj, null, null, null);
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String path = cursor.getString(column_index);
					mImgPaths = path;

					ContentResolver cr = this.getContentResolver();
					bitmap = BitmapFactory.decodeStream(cr
							.openInputStream(imgUri));

					strCommentSmallImage = mUtils.BitmapToBase64BySize(mUtils
							.zoomBitmap(bitmap, MyApplication.nImageWidth,
									MyApplication.nImageHeight));
					strCommentLargeImage = mUtils.BitmapToBase64BySize(mUtils
							.zoomBitmap(bitmap, MyApplication.nLargeImageWidth,
									MyApplication.nLargeImageHeight));

					m_ProgressDialog = ProgressDialog.show(
							DynamicDetailActivity.this, "��ʾ", "���ڷ���ͼƬ,��ȴ�...",
							true);
					m_ProgressDialog.setCancelable(true);
					// �����߳�,������ͼƬ���ͳ�ȥ
					Thread comment1 = new Thread(commentrun);
					comment1.start();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (OutOfMemoryError e) {
					CommonUtils.ShowToastCenter(this, "�ڴ治��!",
							Toast.LENGTH_LONG);
				}
			}
		} else if (requestCode == 1) {
			if (data == null) {
				return;
			}
			try {
				Uri uri = data.getData();
				ContentResolver cr = this.getContentResolver();
				String[] proj = { MediaStore.Images.Media.DATA };
				@SuppressWarnings("deprecation")
				Cursor cursor = managedQuery(uri, proj, null, null, null);
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				String path = cursor.getString(column_index);
				mImgPaths = path;
				// ��ͼƬ������ת����
				bitmap = mUtils.PhotoRotation(uri);
				if (null == bitmap) {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"���ܷ��͸����͵�ͼƬ", Toast.LENGTH_LONG);
				} else {
					strCommentSmallImage = mUtils.BitmapToBase64BySize(mUtils
							.zoomBitmap(bitmap, MyApplication.nImageWidth,
									MyApplication.nImageHeight));
					strCommentLargeImage = mUtils.BitmapToBase64BySize(mUtils
							.zoomBitmap(bitmap, MyApplication.nLargeImageWidth,
									MyApplication.nLargeImageHeight));
					// �����߳�,������ͼƬ���ͳ�ȥ
					Thread comment1 = new Thread(commentrun);
					comment1.start();
					m_ProgressDialog = ProgressDialog.show(
							DynamicDetailActivity.this, "��ʾ", "���ڷ���ͼƬ,��ȴ�...",
							true);
					m_ProgressDialog.setCancelable(true);
				}
			} catch (OutOfMemoryError e) {
				CommonUtils.ShowToastCenter(this, "�ڴ治��!", Toast.LENGTH_LONG);
			}
		} else if (requestCode == 2) {
			if (data != null) {
				int nRet = data.getExtras().getInt("nRet");
				// ��������ɹ�
				if (1 == nRet) {
					thanksbutton.setVisibility(View.GONE);
				}
			}
		}
	}

	protected Dialog onCreateDialog(int id) {
		AlertDialog dialog = null;
		AlertDialog.Builder builder = null;
		switch (id) {
		case 0:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("����ͼƬ");
			builder.setItems(addPhoto, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == 0) {
						String haveSD = Environment.getExternalStorageState();
						if (!haveSD.equals(Environment.MEDIA_MOUNTED)) {
							Toast.makeText(DynamicDetailActivity.this,
									"�洢��������", Toast.LENGTH_LONG).show();
							return;
						}
						File dir = new File(Environment
								.getExternalStorageDirectory()
								+ "/"
								+ "carPhoto");
						if (!dir.exists()) {
							dir.mkdirs();
						} else {// ����Ѿ������˸��ļ���
							// �жϸ��ļ������Ƿ����Car.jpg�ļ�,���������ɾ��
							String strPhotoPath = Environment
									.getExternalStorageDirectory()
									+ "/"
									+ "carPhoto" + "/" + "Car.jpg";
							File pPhotoFile = new File(strPhotoPath);
							if (pPhotoFile.exists()) {
								pPhotoFile.delete();
							}
						}
						Intent intent = new Intent(
								android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						File imgFile = new File(dir, "Car.jpg");
						Uri u = Uri.fromFile(imgFile);
						intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
						startActivityForResult(intent, 0);
					}
					if (which == 1) {
						Intent intent = new Intent(
								"android.intent.action.GET_CONTENT");
						intent.addCategory("android.intent.category.OPENABLE");
						intent.setType("image/*");
						DynamicDetailActivity.this.startActivityForResult(
								intent, 1);
					}
				}
			});
			dialog = builder.create();
			break;

		default:
			break;
		}
		return dialog;
	}

	public void onDestroy() {
		super.onDestroy();
		mbIsContinue = false;
		mthre = null;
		run1 = null;
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onBackPressed() {
		// ���жϱ��鴰���Ƿ���ʾ
		if (View.VISIBLE == gridviewlinearlayout.getVisibility()) {
			gridviewlinearlayout.setVisibility(View.GONE);
		} else {
			mbIsContinue = false;
			mthre = null;
			if (m_ProgressDialog != null) {
				m_ProgressDialog.dismiss();
				m_ProgressDialog = null;
			}
			if (bIsAddBrowseTime) {
				String strZan = zantextview.getText().toString();
				String strTalk = talktextview.getText().toString();
				String strLook = looktimetextview.getText().toString();
				Intent data = new Intent();
				data.putExtra("strTaskId", strTaskId);
				data.putExtra("strTaskType", "1");
				data.putExtra("strZan", strZan);
				data.putExtra("strTalk", strTalk);
				data.putExtra("strLook", strLook);
				setResult(1, data);
			} else {
				setResult(2);
			}
			finish();
		}
	}

	private void InitGridView() {

		gridView = (GridView) findViewById(R.id.mytask_gridview);

		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		// ����107�������id����װ
		for (int i = 0; i < 107; i++) {
			try {
				if (i < 10) {
					Field field = R.drawable.class.getDeclaredField("f00" + i);
					int resourceId = Integer.parseInt(field.get(null)
							.toString());
					imageIds[i] = resourceId;
				} else if (i < 100) {
					Field field = R.drawable.class.getDeclaredField("f0" + i);
					int resourceId = Integer.parseInt(field.get(null)
							.toString());
					imageIds[i] = resourceId;
				} else {
					Field field = R.drawable.class.getDeclaredField("f" + i);
					int resourceId = Integer.parseInt(field.get(null)
							.toString());
					imageIds[i] = resourceId;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image", imageIds[i]);
			listItems.add(listItem);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(
				DynamicDetailActivity.this, listItems,
				R.layout.team_layout_single_expression_cell,
				new String[] { "image" }, new int[] { R.id.image });
		gridView.setAdapter(simpleAdapter);
		gridView.setGravity(Gravity.CENTER);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Bitmap bitmap = null;
				bitmap = BitmapFactory.decodeResource(getResources(),
						imageIds[arg2 % imageIds.length]);
				ImageSpan imageSpan = new ImageSpan(DynamicDetailActivity.this,
						bitmap);
				String str = null;
				if (arg2 < 10) {
					str = "f00" + arg2;
				} else if (arg2 < 100) {
					str = "f0" + arg2;
				} else {
					str = "f" + arg2;
				}
				SpannableString spannableString = new SpannableString(str);
				spannableString.setSpan(imageSpan, 0, 4,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				edittext1.append(spannableString);
			}
		});
	}

	// ���������ݿ��ļ���ɾ����
	private void CreateDatabaseAndStoreData() {
		// ��ǰ�û�ͼ��
		String strCurrentImage = msettings.getString("Base64Image", "");
		if (!mDatabase.IsTableExist(Integer.parseInt(strTaskId), 1)) {
			mDatabase.CloseDatabase();
			mDatabase = null;
			mDatabase = new OperaDatabase(DynamicDetailActivity.this);
			// ��ʱ˵�����ݿ��ļ���ɾ����,�����������ݿ��,��ԭ�����������´洢�����ݿ���
			if (mCommentInfos != null) {
				int nSize = mCommentInfos.size();
				for (int i = 0; i < nSize; i++) {
					String strContent1 = mCommentInfos.get(i)
							.getstrCommentContent();
					// ȥ����ǰ��ո���ַ���
					String strClearSpace = CommonUtils
							.ClearFrontSpace(strContent1);
					char cFirstChar = strClearSpace.charAt(0);
					// �ж��Ƿ������Ļ�
					int nSecretType = 0;
					if ('*' == cFirstChar) {
						nSecretType = 1;
					}
					mDatabase.InsertCommentData(Integer.parseInt(strTaskId), 1,
							mCommentInfos.get(i).getnCommentIndex(),
							strCurrentImage, mCommentInfos.get(i)
									.getstrCommentPersonName(), mCommentInfos
									.get(i).getstrCommentReceivePersonName(),
							mCommentInfos.get(i).getstrCommentTime(),
							mCommentInfos.get(i).getstrCommentContent(),
							mCommentInfos.get(i).getstrCommentPersonTrueName(),
							mCommentInfos.get(i)
									.getstrCommentReceivePersonTrueName(),
							mCommentInfos.get(i).getstrSmallImage(),
							nSecretType);
				}
			}
		}
	}
}