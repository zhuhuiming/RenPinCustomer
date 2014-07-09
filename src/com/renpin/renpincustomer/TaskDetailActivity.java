package com.renpin.renpincustomer;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.renpin.domin.CommentInfo;
import com.renpin.domin.DistanceDetail;
import com.renpin.domin.TaskIcon;
import com.renpin.domin.TaskInfoDetail;
import com.renpin.location.MyApplication;
import com.renpin.renpincustomer.AboutDialog.PriorityListener;
import com.renpin.service.GoodService;
import com.renpin.service.Impl.GoodServiceImpl;
import com.renpin.utils.CommonUtils;
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
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskDetailActivity extends Activity {
	ImageView imageview1;// ���񷢲���ͼƬ
	TextView textview1;// ���񷢲��ߵ�����
	TextView textview2;// ���񷢲�ʱ��
	TextView textview3;// ������Чʱ������
	TextView textview4;// �������
	TextView textview5;// ������ϸ����
	ImageView imageview2;// ����ͼ��
	Button button1;// ���°�ť
	ImageView imageview3;// ����ͼ��
	TextView textview6;// ��������

	// ��ʾͼƬ�ؼ�
	ImageView icon1;
	ImageView icon2;
	ImageView icon3;
	ImageView icon4;
	ImageView icon5;
	ImageView icon6;

	private static CommonUtils mUtils = null;
	// �����洢�û���Ϣ
	SharedPreferences msettings = null;
	// �ж��Ƿ��������ʱ
	boolean mbIsContinue = true;
	MyHandler myhandler = null;
	// ��ʱ����ʱ����̶߳���
	private static Thread mthre = null;
	// ��ȡ����ʱ��������̶߳���
	private static Thread mthre1 = null;
	// ��ʱ��ʣ��ʱ��(��)
	private static long mnRemainTime;
	// ������ȡ�����������ݵĶ���
	private GoodService goodService = new GoodServiceImpl();
	// ����id��
	private String strTaskId;
	// ��������
	private String strTaskType;
	// �洢����ͼƬ�Ķ���
	private TaskIcon taskicon;
	// ������񷢲��Ŵ�ͼƬ
	public static String[] strIcons = new String[6];
	// �жϷŴ�ͼƬ�Ƿ��Ѿ����ص�������
	private boolean mbIsLoad = false;
	// ����������
	String mstrCustomerNameTemp;
	// �������ǳ�
	String strCustomerNameNick;
	// ִ�����ǳ�
	String strImplementNick;
	// ��Ʒֵ
	int mnCredit = 0;
	// ��ֵ
	int mnCharmValue = 0;
	// ��ʾ��Ʒֵ�ؼ�
	TextView credittextview;
	// ��ʾ��ֵ�ؼ�
	TextView charmvaluetextview;
	// ���»����¸�������Ҫ����Ʒֵ
	int mnCreditLimit = 1;
	int mnCharmLimit = 0;
	// ��ǰ�û���Ʒֵ
	int mnCurrentCredit = 0;
	// ��ǰ�û���ֵ
	int mnCurrentCharmValue = 0;
	// ����
	LinearLayout myshare;
	// ����
	LinearLayout mytask;
	// �������۵İ�ť
	Button SendCommentButton;
	// ����ͼƬ�İ�ť
	ImageView SendPictureImageView;
	// ��ʾ���۵�LinearLayout
	private LinearLayout linearlayout1 = null;
	// �洢��������
	private static List<CommentInfo> mCommentInfos = null;
	// �洢�����еĴ�ͼƬ
	public static HashMap<String, String> CommentMaps;
	// �������۵�������
	private String strReceiveName;
	// �������۵�������
	private String strReceiveNickName;
	// �������۱༭��
	EditText edittext1;
	// �ж������Ƿ����ָ������
	boolean bIsTalkToPerson = false;
	// �������ؼ�
	ScrollView scrollview;
	// ������ѡͼƬ��·��
	private String mImgPaths = "";
	public static final String[] addPhoto = new String[] { "��������", "�����ѡ��",
			"ȡ��" };
	// ����ո�ѡ�������ѹ��ͼƬ
	private String strCommentSmallImage;
	// ����ո�ѡ������۴�ͼƬ
	private String strCommentLargeImage;
	// ����������
	public static ProgressDialog m_ProgressDialog = null;
	// ��СͼƬ�Ŀ��
	private final int nImageWidth = 200;
	// ��СͼƬ�ĸ߶�
	private final int nImageHeight = 180;
	// ��������Ƿ�ɹ���һ
	private boolean bIsAddBrowseTime = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// ����������ʱ������괦��editview�ж��������뷨����
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.task_detail);

		mbIsLoad = false;

		if (null == mUtils) {
			mUtils = new CommonUtils(this);
		}
		if (null == myhandler) {
			myhandler = new MyHandler();
		}
		msettings = getSharedPreferences("MekeSharedPreferences", 0);
		// ��ʼ������ͼƬ����
		CommentMaps = new HashMap<String, String>();

		String strTaskAnnounceTime;// ����ʱ��
		String strTimeLimit;// ʱ������
		String strTaskTitle;// �������
		String strDetail;// ������ϸ����
		String strRunSeconds;// ������е�����
		String strImplementName;// ����ִ����

		mstrCustomerNameTemp = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.customername");
		strTaskAnnounceTime = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.TaskAnnounceTime");
		strTimeLimit = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.TimeLimit");
		strTaskTitle = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.TaskTitle");
		strDetail = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.Detail");
		strRunSeconds = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.RunSeconds");
		strTaskId = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.TaskId");
		strImplementName = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.TaskImplementName");
		strCustomerNameNick = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.strCustomerNameNick");
		strImplementNick = msettings.getString("PersonName", "");
		strTaskType = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.strTaskType");
		mnCreditLimit = (int) getIntent().getIntExtra(
				"com.renpin.RenPinMainActivity.nCreditValue", 1);
		mnCharmLimit = (int) getIntent().getIntExtra(
				"com.renpin.RenPinMainActivity.nCharmValue", 0);

		InitActivities(mstrCustomerNameTemp, strTaskAnnounceTime, strTimeLimit,
				strTaskTitle, strDetail, strRunSeconds, strImplementName,
				strTaskType);
		// ��ʾ����
		ShowComments();
		// ��ʾ�������
		ShowBrowseTime();
	}

	private void ShowComments() {
		Runnable runcomments = new Runnable() {
			public void run() {
				mCommentInfos = goodService.GetCommentsForTask(strTaskId,
						strTaskType);

				Message msg = myhandler.obtainMessage();
				msg.what = 7;
				myhandler.sendMessage(msg);
			}
		};
		// ������ȡ���������߳�
		Thread thread = new Thread(runcomments);
		thread.start();
	}

	private void ShowBrowseTime() {
		Runnable browserun = new Runnable() {
			public void run() {
				int nRet = goodService.AddBrowseTimes(strTaskId, strTaskType);

				Message msg = myhandler.obtainMessage();
				Bundle b = new Bundle();// �������
				b.putInt("nRet", nRet);
				msg.setData(b);
				msg.what = 11;
				myhandler.sendMessage(msg);
			}
		};
		// ������ȡ���������߳�
		Thread thread = new Thread(browserun);
		thread.start();
	}

	private void InitActivities(String strCustomerNameTemp,
			String strTaskAnnounceTime, String strTimeLimit,
			String strTaskTitle, String strDetail, String strRunSeconds,
			String strImplementName, final String strTaskType) {

		imageview1 = (ImageView) findViewById(R.id.task_detail_imageview1);
		textview1 = (TextView) findViewById(R.id.task_detail_textview1);
		textview2 = (TextView) findViewById(R.id.task_detail_textview2);
		textview3 = (TextView) findViewById(R.id.task_detail_textview3);
		textview4 = (TextView) findViewById(R.id.task_detail_textview4);
		textview5 = (TextView) findViewById(R.id.task_detail_textview5);
		imageview2 = (ImageView) findViewById(R.id.customer_login_image1);
		button1 = (Button) findViewById(R.id.task_detail_button1);
		button1.setVisibility(View.GONE);
		textview6 = (TextView) findViewById(R.id.task_detail_textview6);
		imageview3 = (ImageView) findViewById(R.id.task_detail_imageview2);
		credittextview = (TextView) findViewById(R.id.task_detail_credit);
		charmvaluetextview = (TextView) findViewById(R.id.task_detail_charm);
		mytask = (LinearLayout) findViewById(R.id.taskdetail_mytasklayout);
		myshare = (LinearLayout) findViewById(R.id.taskdetail_mysharelayout);
		SendCommentButton = (Button) findViewById(R.id.task_detail_button2);
		SendPictureImageView = (ImageView) findViewById(R.id.task_detail_sendpicture);
		linearlayout1 = (LinearLayout) findViewById(R.id.task_detail_linearlayout1);
		edittext1 = (EditText) findViewById(R.id.task_detail_edittext1);
		scrollview = (ScrollView) findViewById(R.id.task_detail_scrollview);

		icon1 = (ImageView) findViewById(R.id.task_detail_icon1);
		icon2 = (ImageView) findViewById(R.id.task_detail_icon2);
		icon3 = (ImageView) findViewById(R.id.task_detail_icon3);
		icon4 = (ImageView) findViewById(R.id.task_detail_icon4);
		icon5 = (ImageView) findViewById(R.id.task_detail_icon5);
		icon6 = (ImageView) findViewById(R.id.task_detail_icon6);

		mytask.setVisibility(View.GONE);
		myshare.setVisibility(View.GONE);
		/*
		 * if (2 == Integer.parseInt(strTaskType)) { button1.setText("����");
		 * myshare.setVisibility(View.VISIBLE); mytask.setVisibility(View.GONE);
		 * }else{ myshare.setVisibility(View.GONE);
		 * mytask.setVisibility(View.VISIBLE); }
		 */

		Bitmap bmp = RenPinMainActivity.mAnnounceImage;
		imageview1.setImageBitmap(bmp);
		textview1.setText(strCustomerNameNick);
		textview2.setText(strTaskAnnounceTime);
		// int nLimit = Integer.parseInt(strTimeLimit);
		// int nRunSecond = Integer.parseInt(strRunSeconds);
		// mnRemainTime = nLimit - nRunSecond;
		// ���ʣ��ʱ��С��0,��ô��Ĭ�ϸ�ֵ0
		// if (mnRemainTime <= 0) {
		// mnRemainTime = 0;
		// }
		String strAccountName = msettings.getString("TruePersonName", "");
		// ����������Ѿ����������»��߸��������Լ�������,��ô�Ͳ��ܽ������²���
		if (strCustomerNameTemp.equals(strAccountName)
				|| !strImplementName.equals("")) {
			button1.setTextColor(Color.rgb(96, 96, 96));
			button1.setEnabled(false);
		}
		mthre1 = new Thread(run2);
		mthre1.start();

		// ������ȡ����ͼ���߳�
		Thread thread = new Thread(run3);
		thread.start();

		// String strTime = mUtils.GetStringBySeconds(mnRemainTime);
		textview3.setText("");
		textview4.setText(strTaskTitle);
		textview5.setText(strDetail);

		imageview2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mbIsContinue = false;
				if (bIsAddBrowseTime) {
					Intent data = new Intent();
					data.putExtra("strTaskId", strTaskId);
					data.putExtra("strTaskType", strTaskType);
					setResult(1, data);
				} else {
					setResult(2);
				}
				TaskDetailActivity.this.finish();
			}
		});

		button1.setOnClickListener(new OnClickListener() {

			String strAccountName = msettings.getString("TruePersonName", "");
			// �Ȼ�ȡ��ǰ�û�����Ʒֵ����ֵ
			Runnable creditandcharm = new Runnable() {
				public void run() {
					if (!strAccountName.equals("")) {
						// ���������ƻ�ȡ��Ӧ����Ʒֵ����ֵ
						mnCurrentCredit = goodService
								.GetCreditValue(strAccountName);
						mnCurrentCharmValue = goodService
								.GetCharmValue(strAccountName);
					}
					// ������Ϣ,����run1�߳�
					Message msg = myhandler.obtainMessage();
					msg.what = 6;
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {
				Thread thre = new Thread(creditandcharm);
				thre.start();
			}

		});

		imageview3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		icon1.setOnClickListener(new OnClickListener() {
			Runnable runicon1 = new Runnable() {
				public void run() {
					if (!mbIsLoad) {
						// ��ȡÿ����Сͼ�ķŴ�ͼƬ
						strIcons[0] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 1);
						strIcons[1] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 2);
						strIcons[2] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 3);
						strIcons[3] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 4);
						strIcons[4] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 5);
						strIcons[5] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 6);
						mbIsLoad = true;
					}
					Message msg = myhandler.obtainMessage();
					Bundle b = new Bundle();
					b.putInt("icontype", 0);
					msg.setData(b);
					msg.what = 5;
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {
				// ������ȡ���������߳�
				Thread thread = new Thread(runicon1);
				thread.start();
			}

		});

		icon2.setOnClickListener(new OnClickListener() {

			Runnable runicon2 = new Runnable() {
				public void run() {
					if (!mbIsLoad) {
						// ��ȡÿ����Сͼ�ķŴ�ͼƬ
						strIcons[0] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 1);
						strIcons[1] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 2);
						strIcons[2] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 3);
						strIcons[3] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 4);
						strIcons[4] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 5);
						strIcons[5] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 6);
						mbIsLoad = true;
					}
					Message msg = myhandler.obtainMessage();
					Bundle b = new Bundle();
					b.putInt("icontype", 1);
					msg.setData(b);
					msg.what = 5;
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {
				// ������ȡ���������߳�
				Thread thread = new Thread(runicon2);
				thread.start();
			}

		});

		icon3.setOnClickListener(new OnClickListener() {

			Runnable runicon3 = new Runnable() {
				public void run() {
					if (!mbIsLoad) {
						// ��ȡÿ����Сͼ�ķŴ�ͼƬ
						strIcons[0] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 1);
						strIcons[1] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 2);
						strIcons[2] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 3);
						strIcons[3] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 4);
						strIcons[4] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 5);
						strIcons[5] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 6);
						mbIsLoad = true;
					}
					Message msg = myhandler.obtainMessage();
					Bundle b = new Bundle();
					b.putInt("icontype", 2);
					msg.setData(b);
					msg.what = 5;
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {
				// ������ȡ���������߳�
				Thread thread = new Thread(runicon3);
				thread.start();
			}

		});

		icon4.setOnClickListener(new OnClickListener() {
			Runnable runicon4 = new Runnable() {
				public void run() {
					if (!mbIsLoad) {
						// ��ȡÿ����Сͼ�ķŴ�ͼƬ
						strIcons[0] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 1);
						strIcons[1] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 2);
						strIcons[2] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 3);
						strIcons[3] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 4);
						strIcons[4] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 5);
						strIcons[5] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 6);
						mbIsLoad = true;
					}
					Message msg = myhandler.obtainMessage();
					Bundle b = new Bundle();
					b.putInt("icontype", 3);
					msg.setData(b);
					msg.what = 5;
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {
				// ������ȡ���������߳�
				Thread thread = new Thread(runicon4);
				thread.start();
			}

		});

		icon5.setOnClickListener(new OnClickListener() {

			Runnable runicon5 = new Runnable() {
				public void run() {
					if (!mbIsLoad) {
						// ��ȡÿ����Сͼ�ķŴ�ͼƬ
						strIcons[0] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 1);
						strIcons[1] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 2);
						strIcons[2] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 3);
						strIcons[3] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 4);
						strIcons[4] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 5);
						strIcons[5] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 6);
						mbIsLoad = true;
					}
					Message msg = myhandler.obtainMessage();
					Bundle b = new Bundle();
					b.putInt("icontype", 4);
					msg.setData(b);
					msg.what = 5;
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {
				// ������ȡ���������߳�
				Thread thread = new Thread(runicon5);
				thread.start();
			}

		});

		icon6.setOnClickListener(new OnClickListener() {

			Runnable runicon6 = new Runnable() {
				public void run() {
					if (!mbIsLoad) {
						// ��ȡÿ����Сͼ�ķŴ�ͼƬ
						strIcons[0] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 1);
						strIcons[1] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 2);
						strIcons[2] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 3);
						strIcons[3] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 4);
						strIcons[4] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 5);
						strIcons[5] = goodService.GetTaskLargeIcon(strTaskId,
								strTaskType, 6);
						mbIsLoad = true;
					}
					Message msg = myhandler.obtainMessage();
					Bundle b = new Bundle();
					b.putInt("icontype", 5);
					msg.setData(b);
					msg.what = 5;
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {
				// ������ȡ���������߳�
				Thread thread = new Thread(runicon6);
				thread.start();
			}

		});

		SendCommentButton.setOnClickListener(new OnClickListener() {

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
					String strContent1 = TaskDetailActivity.this.edittext1
							.getText().toString();

					String strOtherPersonName = "";
					String strOtherPersonNickName = "";
					int nRet = 0;
					if (bIsTalkToPerson) {
						// ��������
						/*
						 * nRet = goodService.SendCommentContentNew(strTaskId,
						 * strCurrentName, strReceiveName, strContent1,
						 * strTaskType, strCurrentNickName, strReceiveNickName);
						 */
						strOtherPersonName = strReceiveName;
						strOtherPersonNickName = strReceiveNickName;

					} else {
						// �����Ϣ�����˷����˵�ǰ��������,��ô������Ϣ�Ͳ���֪ͨ������
						if (!strCurrentName.equals(mstrCustomerNameTemp)) {
							// ��������
							/*
							 * nRet =
							 * goodService.SendCommentContentNew(strTaskId,
							 * strCurrentName, mstrCustomerNameTemp,
							 * strContent1, strTaskType, strCurrentNickName,
							 * strCustomerNameNick);
							 */
						} else {
							// ��������
							/*
							 * nRet =
							 * goodService.SendCommentContentNew(strTaskId,
							 * strCurrentName, "", strContent1, strTaskType,
							 * strCurrentNickName, "");
							 */
						}
					}
					if (nRet > 0) {
						// ��ȡϵͳ��ǰʱ��
						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy��MM��dd��HH:mm:ss");
						Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
						String strCurrentTime = formatter.format(curDate);

						CommentInfo commentinfo = new CommentInfo(
								strCurrentImage, strCurrentNickName,
								strOtherPersonNickName, strCurrentTime,
								strContent1, "", 0, strCurrentName,
								strOtherPersonName);
						mCommentInfos.add(commentinfo);
						Message msg1 = myhandler.obtainMessage();
						msg1.what = 9;
						myhandler.sendMessage(msg1);
					}

					String strOperMsg;
					if (1 == nRet) {
						strOperMsg = "�����ɹ�";
					} else {
						strOperMsg = "����ʧ��";
					}
					Message msg = myhandler.obtainMessage();
					msg.what = 10;
					Bundle b = new Bundle();// �������
					b.putString("OperMsg", strOperMsg);
					b.putString("strTaskId", strTaskId);
					b.putString("strTaskType", strTaskType);
					msg.setData(b);
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {
				String strContent1 = TaskDetailActivity.this.edittext1
						.getText().toString();
				if (!strContent1.equals("")) {
					SendCommentButton.setText("���ڷ���...");
					SendCommentButton.setEnabled(false);
					SendCommentButton.setTextColor(Color.rgb(96, 96, 96));
					// ������ȡ���������߳�
					Thread thread = new Thread(SendCommentContent);
					thread.start();
				} else {
					CommonUtils.ShowToastCenter(getBaseContext(), "���͵����ݲ���Ϊ��",
							Toast.LENGTH_LONG);
				}
			}
		});

		SendPictureImageView.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(0);
			}
		});
	}

	class MyHandler extends Handler {
		// ���������д�˷���,��������
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (mnRemainTime >= 0) {
					String strTime = mUtils.GetStringBySeconds(mnRemainTime);
					textview3.setText(strTime);
					mnRemainTime -= 1;
					if (mnRemainTime <= 0) {
						button1.setTextColor(Color.rgb(96, 96, 96));
						button1.setEnabled(false);
					}
				} else if (mnRemainTime < 0) {
					mbIsContinue = false;
					textview3.setText("0��0Сʱ0��0��");
					button1.setTextColor(Color.rgb(96, 96, 96));
					button1.setEnabled(false);
				}
				break;

			case 2:// �ж��Ƿ���������
				Bundle b = msg.getData();
				String strMsg = b.getString("strMsg");
				Toast.makeText(TaskDetailActivity.this, strMsg,
						Toast.LENGTH_LONG).show();
				break;
			case 3:
				if (null == mthre/* && mnRemainTime > 0 */) {
					mbIsContinue = true;
					mthre = new Thread(run1);
					mthre.start();
				}
				break;
			case 4:
				if (taskicon != null) {
					if (taskicon.getstrIcon1().length() < 10) {
						icon1.setVisibility(View.GONE);
					} else {
						icon1.setImageBitmap(mUtils.base64ToBitmap(taskicon
								.getstrIcon1()));
					}

					if (taskicon.getstrIcon2().length() < 10) {
						icon2.setVisibility(View.GONE);
					} else {
						icon2.setImageBitmap(mUtils.base64ToBitmap(taskicon
								.getstrIcon2()));
					}

					if (taskicon.getstrIcon3().length() < 10) {
						icon3.setVisibility(View.GONE);
					} else {
						icon3.setImageBitmap(mUtils.base64ToBitmap(taskicon
								.getstrIcon3()));
					}

					if (taskicon.getstrIcon4().length() < 10) {
						icon4.setVisibility(View.GONE);
					} else {
						icon4.setImageBitmap(mUtils.base64ToBitmap(taskicon
								.getstrIcon4()));
					}

					if (taskicon.getstrIcon5().length() < 10) {
						icon5.setVisibility(View.GONE);
					} else {
						icon5.setImageBitmap(mUtils.base64ToBitmap(taskicon
								.getstrIcon5()));
					}

					if (taskicon.getstrIcon6().length() < 10) {
						icon6.setVisibility(View.GONE);
					} else {
						icon6.setImageBitmap(mUtils.base64ToBitmap(taskicon
								.getstrIcon6()));
					}

				} else {
					icon1.setVisibility(View.GONE);
					icon2.setVisibility(View.GONE);
					icon3.setVisibility(View.GONE);
					icon4.setVisibility(View.GONE);
					icon5.setVisibility(View.GONE);
					icon6.setVisibility(View.GONE);
				}
				credittextview.setText("��Ʒ:" + mnCredit);
				charmvaluetextview.setText(mnCharmValue + "");
				break;
			case 5:
				Bundle b1 = msg.getData();
				int nPos = b1.getInt("icontype");
				Intent intent = new Intent(TaskDetailActivity.this,
						TaskDetailViewPagerActivity.class);
				intent.putExtra("iconposion", nPos);
				startActivity(intent);
				break;
			case 6:
				String strAccountName = msettings.getString("TruePersonName",
						"");
				if (!strAccountName.equals("")) {
					if (mnCurrentCredit < mnCreditLimit
							&& mnCurrentCharmValue < mnCharmLimit) {
						CommonUtils.ShowToastCenter(TaskDetailActivity.this,
								"������Ʒֵ����" + mnCreditLimit + "��ֵ����"
										+ mnCharmLimit, Toast.LENGTH_LONG);
						return;
					}
					// �����Ʒֵ����
					if (mnCurrentCredit < mnCreditLimit) {
						CommonUtils.ShowToastCenter(TaskDetailActivity.this,
								"������Ʒֵ����" + mnCreditLimit, Toast.LENGTH_LONG);
						return;
					}
					// �����Ʒֵ����
					if (mnCurrentCharmValue < mnCharmLimit) {
						CommonUtils.ShowToastCenter(TaskDetailActivity.this,
								"������ֵ����" + mnCharmLimit, Toast.LENGTH_LONG);
						return;
					}
					// �жϵ�ǰ�û�����Ʒֵ����ֵ�Ƿ����Ҫ��
					String strMsg1 = "����ʣ��ʱ��";
					strMsg1 += textview3.getText().toString();
					strMsg1 += "������ʣ��ʱ������������ύ������������������Ч��";
					AboutDialog.SetMsg(strMsg1);
					AboutDialog aboutDialog = null;
					if (2 == Integer.parseInt(strTaskType)) {
						aboutDialog = new AboutDialog(TaskDetailActivity.this,
								2, new PriorityListener() {

									@Override
									public void refreshPriorityUI(String str1,
											String str2, String str3,
											String str4) {
										int nType = AboutDialog.GetSelectType();
										if (1 == nType) {
											final String strImplementAccountName = msettings
													.getString(
															"TruePersonName",
															"");

											if (strImplementAccountName
													.equals("")) {
												Message msg = new Message();
												Bundle b = new Bundle();// �������
												b.putString("strMsg",
														"���ȵ�¼�ٽ�����Ӧ����!");
												msg.setData(b);
												msg.what = 2;
												myhandler.sendMessage(msg);
											} else {
												Runnable run11 = new Runnable() {
													public void run() {
														int nSelectTaskRetValue = 0;
														String strPersonName = msettings
																.getString(
																		"TruePersonName",
																		"");
														// �����ȷ����ť,���ʷ�����,�������ݿ��
														nSelectTaskRetValue = goodService
																.SelectTask(
																		strTaskId,
																		strPersonName,
																		strTaskType,
																		strImplementNick);
														String strMsg = "";
														if (1 == nSelectTaskRetValue) {
															strMsg = "�����ɹ�!";
														} else {
															strMsg = "����ʧ��!";
														}
														Message msg = new Message();
														Bundle b = new Bundle();// �������
														b.putString("strMsg",
																strMsg);
														msg.setData(b);
														msg.what = 2;
														myhandler
																.sendMessage(msg);
													}
												};
												// ���¶�̬��Ϣ
												Thread thread = new Thread(
														run11);
												thread.start();
											}
										}
									}

								});
					} else {
						aboutDialog = new AboutDialog(TaskDetailActivity.this,
								1, new PriorityListener() {

									@Override
									public void refreshPriorityUI(String str1,
											String str2, String str3,
											String str4) {
										int nType = AboutDialog.GetSelectType();
										if (1 == nType) {
											final String strImplementAccountName = msettings
													.getString(
															"TruePersonName",
															"");

											if (strImplementAccountName
													.equals("")) {
												Message msg = new Message();
												Bundle b = new Bundle();// �������
												b.putString("strMsg",
														"���ȵ�¼�ٽ�����Ӧ����!");
												msg.setData(b);
												msg.what = 2;
												myhandler.sendMessage(msg);
											} else {
												Runnable run12 = new Runnable() {
													public void run() {
														int nSelectTaskRetValue = 0;
														String strPersonName = msettings
																.getString(
																		"TruePersonName",
																		"");
														// �����ȷ����ť,���ʷ�����,�������ݿ��
														nSelectTaskRetValue = goodService
																.SelectTask(
																		strTaskId,
																		strPersonName,
																		strTaskType,
																		strImplementNick);
														String strMsg = "";
														if (1 == nSelectTaskRetValue) {
															strMsg = "�����ɹ�!";
														} else {
															strMsg = "����ʧ��!";
														}
														Message msg = new Message();
														Bundle b = new Bundle();// �������
														b.putString("strMsg",
																strMsg);
														msg.setData(b);
														msg.what = 2;
														myhandler
																.sendMessage(msg);
													}
												};
												// ���¶�̬��Ϣ
												Thread thread = new Thread(
														run12);
												thread.start();
											}
										}
									}

								});
					}
					aboutDialog.show();
					credittextview.setText("��Ʒ:" + mnCredit);
					charmvaluetextview.setText(mnCharmValue + "");
				} else {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"���ȵ�¼", Toast.LENGTH_LONG);
				}
				break;
			case 7:
				if (mCommentInfos != null) {
					// ��̬�������۵Ľ���
					AutoCreateCommentWidows();
				} else {
					if (!isConnectInternet()) {
						Toast.makeText(TaskDetailActivity.this, "���粻����Ŷ!",
								Toast.LENGTH_LONG).show();
					}
				}
				break;
			case 8:
				Bundle b3 = msg.getData();
				int nCommentIndex = b3.getInt("commentindex");
				// ��ʾͼƬ
				Intent intent3 = new Intent(TaskDetailActivity.this,
						TaskDetailShowLargeImageActivity.class);
				intent3.putExtra("nCommentIndex", nCommentIndex);

				startActivity(intent3);
				break;
			case 9:
				if (mCommentInfos != null) {
					// ��̬�������۵Ľ���
					AutoCreateCommentWidows();
					scrollview.fullScroll(ScrollView.FOCUS_DOWN);
				}
				if (m_ProgressDialog != null) {
					m_ProgressDialog.dismiss();
					m_ProgressDialog = null;
				}
				break;
			case 10:
				Bundle b2 = msg.getData();
				String strOperMsg1 = b2.getString("OperMsg");
				String strId = b2.getString("strTaskId");
				String strType = b2.getString("strTaskType");
				Toast.makeText(TaskDetailActivity.this, strOperMsg1,
						Toast.LENGTH_LONG).show();
				SendCommentButton.setText("����");
				SendCommentButton.setEnabled(true);
				SendCommentButton.setTextColor(Color.rgb(255, 255, 255));
				edittext1.setHint("����");
				edittext1.setText("");
				// �����ݼ��뵽�ҵ���
				AddToMine(strId, strType);
				break;
			case 11:
				Bundle b4 = msg.getData();
				int nRet1 = b4.getInt("nRet");
				if (nRet1 > 0) {
					bIsAddBrowseTime = true;
				}
				break;
			}
		}
	}

	// �ж��ҵ����Ƿ��������������,���û�оͼӽ�ȥ
	public void AddToMine(String strid, String strType) {
		if (RenPinMainActivity.mDynamicNew != null) {
			int nType = Integer.parseInt(strType);
			boolean bFind = false;
			int nSize = RenPinMainActivity.mDynamicNew.size();
			for (int i = 0; i < nSize; i++) {
				if (RenPinMainActivity.mDynamicNew.get(i).getmstrId()
						.equals(strid)
						&& RenPinMainActivity.mDynamicNew.get(i)
								.getmnTaskType() == nType) {
					bFind = true;
					break;
				}
			}
			// ���û��,��ô�ͼ����ȥ
			if (!bFind) {
				DistanceDetail item = null;
				if (strTaskType.equals("1")) {
					nSize = RenPinMainActivity.mHelpTaskDataDistance.size();
					for (int i = 0; i < nSize; i++) {
						if (RenPinMainActivity.mHelpTaskDataDistance.get(i)
								.getmstrId().equals(strid)
								&& RenPinMainActivity.mHelpTaskDataDistance
										.get(i).getmnTaskType() == nType) {
							item = RenPinMainActivity.mHelpTaskDataDistance
									.get(i);
							// �����ݲ��뵽�ҵ���
							TaskInfoDetail taskitem = new TaskInfoDetail(
									item.getmstrTaskRegion(),
									item.getmTaskAskPersonIcon(),
									item.getmPersonName(),
									item.getmTaskTitle(), item.getmstrId(),
									item.getmdLongitude(),
									item.getmdLatidude(),
									item.getmTaskAnnounceTime(),
									item.getmTimeLimit(),
									item.getmTaskDetail(),
									item.getmRunSeconds(),
									item.getmTaskImplementName(),
									item.getmnValiableStatus(),
									item.getmnImplementStatus(),
									item.getmnTaskType(),
									item.getnTaskSelectType(),
									item.getnTaskFinishType(),
									item.getnTaskVerifiType(),
									item.getnTaskAnnounceCommentType(),
									item.getnTaskImplementCommentType(),
									item.getnDynamicNewsNum(),
									item.getstrTaskAccountCommentContent(),
									item.getstrTaskAccountImage(),
									item.getstrTaskImplementCommentContent(),
									item.getstrTaskImplementImage(),
									item.getstrTaskPersonTrueName(),
									item.getstrTaskImplementTrueName(),
									item.getnCreditValue(),
									item.getnCharmValue(),
									item.getnTaskCharmValue(),
									item.getnCommentRecordNum(),
									item.getnBrowseTimes());
							RenPinMainActivity.mDynamicNew.add(taskitem);
							break;
						}
					}
				} else if (strTaskType.equals("2")) {
					nSize = RenPinMainActivity.mShareTaskDataDistance.size();
					for (int i = 0; i < nSize; i++) {
						if (RenPinMainActivity.mShareTaskDataDistance.get(i)
								.getmstrId().equals(strid)
								&& RenPinMainActivity.mShareTaskDataDistance
										.get(i).getmnTaskType() == nType) {
							item = RenPinMainActivity.mShareTaskDataDistance
									.get(i);
							// �����ݲ��뵽�ҵ���
							TaskInfoDetail taskitem = new TaskInfoDetail(
									item.getmstrTaskRegion(),
									item.getmTaskAskPersonIcon(),
									item.getmPersonName(),
									item.getmTaskTitle(), item.getmstrId(),
									item.getmdLongitude(),
									item.getmdLatidude(),
									item.getmTaskAnnounceTime(),
									item.getmTimeLimit(),
									item.getmTaskDetail(),
									item.getmRunSeconds(),
									item.getmTaskImplementName(),
									item.getmnValiableStatus(),
									item.getmnImplementStatus(),
									item.getmnTaskType(),
									item.getnTaskSelectType(),
									item.getnTaskFinishType(),
									item.getnTaskVerifiType(),
									item.getnTaskAnnounceCommentType(),
									item.getnTaskImplementCommentType(),
									item.getnDynamicNewsNum(),
									item.getstrTaskAccountCommentContent(),
									item.getstrTaskAccountImage(),
									item.getstrTaskImplementCommentContent(),
									item.getstrTaskImplementImage(),
									item.getstrTaskPersonTrueName(),
									item.getstrTaskImplementTrueName(),
									item.getnCreditValue(),
									item.getnCharmValue(),
									item.getnTaskCharmValue(),
									item.getnCommentRecordNum(),
									item.getnBrowseTimes());
							RenPinMainActivity.mDynamicNew.add(taskitem);
							break;
						}
					}
				}
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

	// ��ʼ�����������������ݵ�����
	Runnable run1 = new Runnable() {
		public void run() {
			while (mbIsContinue) {
				Message msg = myhandler.obtainMessage();
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

	// ��ʼ���������ȡ������ļ�ʱ����
	Runnable run2 = new Runnable() {
		public void run() {
			mnRemainTime = goodService
					.GetTaskRemainTime(strTaskId, strTaskType);
			// ������Ϣ,����run1�߳�
			Message msg = myhandler.obtainMessage();
			msg.what = 3;
			myhandler.sendMessage(msg);
		}
	};

	// ��ʼ���������ȡ��������ͼƬ
	Runnable run3 = new Runnable() {
		public void run() {
			taskicon = goodService.GetTaskSmallIcon(strTaskId, strTaskType);
			if (!mstrCustomerNameTemp.equals("")) {
				// ���������ƻ�ȡ��Ӧ����Ʒֵ����ֵ
				mnCredit = goodService.GetCreditValue(mstrCustomerNameTemp);
				mnCharmValue = goodService.GetCharmValue(mstrCustomerNameTemp);
			}
			// ������Ϣ,����run1�߳�
			Message msg = myhandler.obtainMessage();
			msg.what = 4;
			myhandler.sendMessage(msg);
		}
	};

	private void AutoCreateCommentWidows() {

		int nCount = mCommentInfos.size();
		// �Ƚ�linearlayout1���
		linearlayout1.removeAllViews();
		if (nCount <= 0) {
			textview6.setText("��������");
		} else {
			// ������������
			textview6.setText("" + mCommentInfos.size());

			for (int i = 0; i < nCount; i++) {

				final CommentInfo commentinfo = mCommentInfos.get(i);

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
					Contenttext.setVisibility(View.VISIBLE);
					Contenttext.setText(commentinfo.getstrCommentContent());
				}
				linearlayout1.addView(cfg_view, i);

				largeimageview.setOnClickListener(new OnClickListener() {
					Runnable runcomment = new Runnable() {
						public void run() {
							String strCommentImage = goodService
									.GetCommentsLargeImage(strTaskId,
											strTaskType,
											commentinfo.getnCommentIndex());
							if (!strCommentImage.equals("")) {
								CommentMaps.put(commentinfo.getnCommentIndex()
										+ "", strCommentImage);
								Message msg = myhandler.obtainMessage();
								Bundle b = new Bundle();
								b.putInt("commentindex",
										commentinfo.getnCommentIndex());
								msg.setData(b);
								msg.what = 8;
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
						if (CommentMaps != null) {
							strImage = CommentMaps.get(nIndex + "");
						}
						// ���֮ǰû�����ظ�ͼƬ,��ô������
						if (null == strImage) {
							// ������ȡ���������߳�
							Thread thread = new Thread(runcomment);
							thread.start();
						} else {// ֱ����ʾͼƬ
							Intent intent = new Intent(TaskDetailActivity.this,
									TaskDetailShowLargeImageActivity.class);
							intent.putExtra("nCommentIndex",
									commentinfo.getnCommentIndex());

							startActivity(intent);
						}
					}

				});
				// �������¼�
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
							edittext1.setHint("����Է�����ֱ�ӻظ��Է�");
							bIsTalkToPerson = false;
						}
					}
				});
			}
		}
	}

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
			int nRet = 0;
			if (bIsTalkToPerson) {
				/*
				 * nRet = goodService.SendCommentImageNew(strTaskId,
				 * strCurrentName, strReceiveName, strCommentSmallImage,
				 * strCommentLargeImage, strTaskType, strCurrentNickName,
				 * strReceiveNickName);
				 */
				strOtherPersonName = strReceiveName;
				strOtherPersonNickName = strReceiveNickName;
			} else {
				if (!strCurrentName.equals(mstrCustomerNameTemp)) {
					/*
					 * nRet = goodService.SendCommentImageNew(strTaskId,
					 * strCurrentName, mstrCustomerNameTemp,
					 * strCommentSmallImage, strCommentLargeImage, strTaskType,
					 * strCurrentNickName, strCustomerNameNick);
					 */
				} else {
					/*
					 * nRet = goodService.SendCommentImageNew(strTaskId,
					 * strCurrentName, "", strCommentSmallImage,
					 * strCommentLargeImage, strTaskType, strCurrentNickName,
					 * "");
					 */
				}
			}
			// ���ͼƬ���ͳɹ�
			if (nRet > 0) {
				// ��ȡϵͳ��ǰʱ��
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy��MM��dd��HH:mm:ss");
				Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
				String strCurrentTime = formatter.format(curDate);

				CommentInfo commentinfo = new CommentInfo(strCurrentImage,
						strCurrentNickName, strOtherPersonNickName,
						strCurrentTime, "", strCommentSmallImage, nRet,
						strCurrentName, strOtherPersonName);
				mCommentInfos.add(commentinfo);
				Message msg1 = myhandler.obtainMessage();
				msg1.what = 9;
				myhandler.sendMessage(msg1);
			}

			String strOperMsg;
			if (nRet > 0) {
				strOperMsg = "�����ɹ�";
			} else {
				strOperMsg = "����ʧ��";
			}

			Message msg = myhandler.obtainMessage();
			Bundle b = new Bundle();
			b.putInt("nRet", nRet);
			b.putString("OperMsg", strOperMsg);
			b.putString("strTaskId", strTaskId);
			b.putString("strTaskType", strTaskType);
			msg.setData(b);
			msg.what = 10;
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
							TaskDetailActivity.this, "��ʾ", "���ڷ���ͼƬ,��ȴ�...",
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
				strCommentSmallImage = mUtils.BitmapToBase64BySize(mUtils
						.zoomBitmap(bitmap, nImageWidth, nImageHeight));
				strCommentLargeImage = mUtils.BitmapToBase64BySize(mUtils
						.zoomBitmap(bitmap));
				// �����߳�,������ͼƬ���ͳ�ȥ
				Thread comment1 = new Thread(commentrun);
				comment1.start();
				m_ProgressDialog = ProgressDialog.show(TaskDetailActivity.this,
						"��ʾ", "���ڷ���ͼƬ,��ȴ�...", true);
				m_ProgressDialog.setCancelable(true);
			} catch (OutOfMemoryError e) {
				CommonUtils.ShowToastCenter(this, "�ڴ治��!", Toast.LENGTH_LONG);
			}
		}
	}

	protected Dialog onCreateDialog(int id) {
		AlertDialog dialog = null;
		AlertDialog.Builder builder = null;
		switch (id) {
		case 0:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("����û�ͼƬ");
			builder.setItems(addPhoto, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == 0) {
						String haveSD = Environment.getExternalStorageState();
						if (!haveSD.equals(Environment.MEDIA_MOUNTED)) {
							Toast.makeText(TaskDetailActivity.this, "�洢��������",
									Toast.LENGTH_LONG).show();
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
						TaskDetailActivity.this.startActivityForResult(intent,
								1);
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

	@Override
	public void onBackPressed() {
		mbIsContinue = false;
		mthre = null;
		if (m_ProgressDialog != null) {
			m_ProgressDialog.dismiss();
			m_ProgressDialog = null;
		}
		if (bIsAddBrowseTime) {
			Intent data = new Intent();
			data.putExtra("strTaskId", strTaskId);
			data.putExtra("strTaskType", strTaskType);
			setResult(1, data);
		} else {
			setResult(2);
		}
		finish();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
