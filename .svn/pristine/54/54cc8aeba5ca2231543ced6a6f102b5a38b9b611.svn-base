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
	ImageView imageview1;// 任务发布者图片
	TextView textview1;// 任务发布者的名称
	TextView textview2;// 任务发布时间
	TextView textview3;// 任务有效时间限制
	TextView textview4;// 任务标题
	TextView textview5;// 任务详细描述
	ImageView imageview2;// 返回图标
	Button button1;// 拿下按钮
	ImageView imageview3;// 评论图标
	TextView textview6;// 评论条数

	// 显示图片控件
	ImageView icon1;
	ImageView icon2;
	ImageView icon3;
	ImageView icon4;
	ImageView icon5;
	ImageView icon6;

	private static CommonUtils mUtils = null;
	// 用来存储用户信息
	SharedPreferences msettings = null;
	// 判断是否继续倒计时
	boolean mbIsContinue = true;
	MyHandler myhandler = null;
	// 定时更新时间的线程对象
	private static Thread mthre = null;
	// 获取任务时间计数的线程对象
	private static Thread mthre1 = null;
	// 计时的剩余时间(秒)
	private static long mnRemainTime;
	// 用来获取服务器上数据的对象
	private GoodService goodService = new GoodServiceImpl();
	// 任务id号
	private String strTaskId;
	// 任务类型
	private String strTaskType;
	// 存储任务图片的对象
	private TaskIcon taskicon;
	// 存放任务发布放大图片
	public static String[] strIcons = new String[6];
	// 判断放大图片是否已经加载到本地了
	private boolean mbIsLoad = false;
	// 发布人名称
	String mstrCustomerNameTemp;
	// 发布人昵称
	String strCustomerNameNick;
	// 执行人昵称
	String strImplementNick;
	// 人品值
	int mnCredit = 0;
	// 赞值
	int mnCharmValue = 0;
	// 显示人品值控件
	TextView credittextview;
	// 显示赞值控件
	TextView charmvaluetextview;
	// 拿下或抢下该任务需要的人品值
	int mnCreditLimit = 1;
	int mnCharmLimit = 0;
	// 当前用户人品值
	int mnCurrentCredit = 0;
	// 当前用户赞值
	int mnCurrentCharmValue = 0;
	// 我抢
	LinearLayout myshare;
	// 我拿
	LinearLayout mytask;
	// 发送评论的按钮
	Button SendCommentButton;
	// 发送图片的按钮
	ImageView SendPictureImageView;
	// 显示评论的LinearLayout
	private LinearLayout linearlayout1 = null;
	// 存储评论数据
	private static List<CommentInfo> mCommentInfos = null;
	// 存储评论中的大图片
	public static HashMap<String, String> CommentMaps;
	// 接收评论的人名称
	private String strReceiveName;
	// 接收评论的人名称
	private String strReceiveNickName;
	// 发送评论编辑框
	EditText edittext1;
	// 判断评论是否针对指定的人
	boolean bIsTalkToPerson = false;
	// 滚动条控件
	ScrollView scrollview;
	// 保存所选图片的路径
	private String mImgPaths = "";
	public static final String[] addPhoto = new String[] { "现在拍摄", "从相册选择",
			"取消" };
	// 保存刚刚选择的评论压缩图片
	private String strCommentSmallImage;
	// 保存刚刚选择的评论大图片
	private String strCommentLargeImage;
	// 进度条对象
	public static ProgressDialog m_ProgressDialog = null;
	// 缩小图片的宽度
	private final int nImageWidth = 200;
	// 缩小图片的高度
	private final int nImageHeight = 180;
	// 浏览次数是否成功加一
	private boolean bIsAddBrowseTime = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 程序启动的时候避免光标处在editview中而弹出输入法窗口
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
		// 初始化评论图片容器
		CommentMaps = new HashMap<String, String>();

		String strTaskAnnounceTime;// 发布时间
		String strTimeLimit;// 时间限制
		String strTaskTitle;// 任务标题
		String strDetail;// 任务详细内容
		String strRunSeconds;// 任务进行的秒数
		String strImplementName;// 任务执行人

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
		// 显示评论
		ShowComments();
		// 显示浏览次数
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
		// 启动获取评论数据线程
		Thread thread = new Thread(runcomments);
		thread.start();
	}

	private void ShowBrowseTime() {
		Runnable browserun = new Runnable() {
			public void run() {
				int nRet = goodService.AddBrowseTimes(strTaskId, strTaskType);

				Message msg = myhandler.obtainMessage();
				Bundle b = new Bundle();// 存放数据
				b.putInt("nRet", nRet);
				msg.setData(b);
				msg.what = 11;
				myhandler.sendMessage(msg);
			}
		};
		// 启动获取评论数据线程
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
		 * if (2 == Integer.parseInt(strTaskType)) { button1.setText("我抢");
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
		// 如果剩余时间小于0,那么就默认给值0
		// if (mnRemainTime <= 0) {
		// mnRemainTime = 0;
		// }
		String strAccountName = msettings.getString("TruePersonName", "");
		// 如果该任务已经被别人拿下或者该任务是自己发布的,那么就不能进行拿下操作
		if (strCustomerNameTemp.equals(strAccountName)
				|| !strImplementName.equals("")) {
			button1.setTextColor(Color.rgb(96, 96, 96));
			button1.setEnabled(false);
		}
		mthre1 = new Thread(run2);
		mthre1.start();

		// 启动获取缩略图的线程
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
			// 先获取当前用户的人品值和赞值
			Runnable creditandcharm = new Runnable() {
				public void run() {
					if (!strAccountName.equals("")) {
						// 根据人名称获取相应的人品值和赞值
						mnCurrentCredit = goodService
								.GetCreditValue(strAccountName);
						mnCurrentCharmValue = goodService
								.GetCharmValue(strAccountName);
					}
					// 发送消息,启动run1线程
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
						// 获取每张缩小图的放大图片
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
				// 启动获取评论数据线程
				Thread thread = new Thread(runicon1);
				thread.start();
			}

		});

		icon2.setOnClickListener(new OnClickListener() {

			Runnable runicon2 = new Runnable() {
				public void run() {
					if (!mbIsLoad) {
						// 获取每张缩小图的放大图片
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
				// 启动获取评论数据线程
				Thread thread = new Thread(runicon2);
				thread.start();
			}

		});

		icon3.setOnClickListener(new OnClickListener() {

			Runnable runicon3 = new Runnable() {
				public void run() {
					if (!mbIsLoad) {
						// 获取每张缩小图的放大图片
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
				// 启动获取评论数据线程
				Thread thread = new Thread(runicon3);
				thread.start();
			}

		});

		icon4.setOnClickListener(new OnClickListener() {
			Runnable runicon4 = new Runnable() {
				public void run() {
					if (!mbIsLoad) {
						// 获取每张缩小图的放大图片
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
				// 启动获取评论数据线程
				Thread thread = new Thread(runicon4);
				thread.start();
			}

		});

		icon5.setOnClickListener(new OnClickListener() {

			Runnable runicon5 = new Runnable() {
				public void run() {
					if (!mbIsLoad) {
						// 获取每张缩小图的放大图片
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
				// 启动获取评论数据线程
				Thread thread = new Thread(runicon5);
				thread.start();
			}

		});

		icon6.setOnClickListener(new OnClickListener() {

			Runnable runicon6 = new Runnable() {
				public void run() {
					if (!mbIsLoad) {
						// 获取每张缩小图的放大图片
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
				// 启动获取评论数据线程
				Thread thread = new Thread(runicon6);
				thread.start();
			}

		});

		SendCommentButton.setOnClickListener(new OnClickListener() {

			Runnable SendCommentContent = new Runnable() {
				public void run() {
					// 当前用户名称
					String strCurrentName = msettings.getString(
							"TruePersonName", "");
					// 当前用户名称(昵称)
					String strCurrentNickName = msettings.getString(
							"PersonName", "");
					// 当前用户图标
					String strCurrentImage = msettings.getString("Base64Image",
							"");
					String strContent1 = TaskDetailActivity.this.edittext1
							.getText().toString();

					String strOtherPersonName = "";
					String strOtherPersonNickName = "";
					int nRet = 0;
					if (bIsTalkToPerson) {
						// 评论内容
						/*
						 * nRet = goodService.SendCommentContentNew(strTaskId,
						 * strCurrentName, strReceiveName, strContent1,
						 * strTaskType, strCurrentNickName, strReceiveNickName);
						 */
						strOtherPersonName = strReceiveName;
						strOtherPersonNickName = strReceiveNickName;

					} else {
						// 如果消息发送人发布了当前任务或分享,那么发送消息就不用通知发布人
						if (!strCurrentName.equals(mstrCustomerNameTemp)) {
							// 评论内容
							/*
							 * nRet =
							 * goodService.SendCommentContentNew(strTaskId,
							 * strCurrentName, mstrCustomerNameTemp,
							 * strContent1, strTaskType, strCurrentNickName,
							 * strCustomerNameNick);
							 */
						} else {
							// 评论内容
							/*
							 * nRet =
							 * goodService.SendCommentContentNew(strTaskId,
							 * strCurrentName, "", strContent1, strTaskType,
							 * strCurrentNickName, "");
							 */
						}
					}
					if (nRet > 0) {
						// 获取系统当前时间
						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy年MM月dd日HH:mm:ss");
						Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
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
						strOperMsg = "操作成功";
					} else {
						strOperMsg = "操作失败";
					}
					Message msg = myhandler.obtainMessage();
					msg.what = 10;
					Bundle b = new Bundle();// 存放数据
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
					SendCommentButton.setText("正在发送...");
					SendCommentButton.setEnabled(false);
					SendCommentButton.setTextColor(Color.rgb(96, 96, 96));
					// 启动获取评论数据线程
					Thread thread = new Thread(SendCommentContent);
					thread.start();
				} else {
					CommonUtils.ShowToastCenter(getBaseContext(), "发送的内容不能为空",
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
		// 子类必须重写此方法,接受数据
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
					textview3.setText("0天0小时0分0秒");
					button1.setTextColor(Color.rgb(96, 96, 96));
					button1.setEnabled(false);
				}
				break;

			case 2:// 判断是否拿下任务
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
				credittextview.setText("人品:" + mnCredit);
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
								"您的人品值不足" + mnCreditLimit + "赞值不足"
										+ mnCharmLimit, Toast.LENGTH_LONG);
						return;
					}
					// 如果人品值不够
					if (mnCurrentCredit < mnCreditLimit) {
						CommonUtils.ShowToastCenter(TaskDetailActivity.this,
								"您的人品值不足" + mnCreditLimit, Toast.LENGTH_LONG);
						return;
					}
					// 如果人品值不够
					if (mnCurrentCharmValue < mnCharmLimit) {
						CommonUtils.ShowToastCenter(TaskDetailActivity.this,
								"您的赞值不足" + mnCharmLimit, Toast.LENGTH_LONG);
						return;
					}
					// 判断当前用户的人品值和赞值是否符合要求
					String strMsg1 = "任务剩余时间";
					strMsg1 += textview3.getText().toString();
					strMsg1 += "，请在剩余时间内完成任务并提交任务结果，否则任务无效！";
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
												Bundle b = new Bundle();// 存放数据
												b.putString("strMsg",
														"请先登录再进行相应操作!");
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
														// 点击了确定按钮,访问服务器,更新数据库表
														nSelectTaskRetValue = goodService
																.SelectTask(
																		strTaskId,
																		strPersonName,
																		strTaskType,
																		strImplementNick);
														String strMsg = "";
														if (1 == nSelectTaskRetValue) {
															strMsg = "操作成功!";
														} else {
															strMsg = "操作失败!";
														}
														Message msg = new Message();
														Bundle b = new Bundle();// 存放数据
														b.putString("strMsg",
																strMsg);
														msg.setData(b);
														msg.what = 2;
														myhandler
																.sendMessage(msg);
													}
												};
												// 更新动态信息
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
												Bundle b = new Bundle();// 存放数据
												b.putString("strMsg",
														"请先登录再进行相应操作!");
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
														// 点击了确定按钮,访问服务器,更新数据库表
														nSelectTaskRetValue = goodService
																.SelectTask(
																		strTaskId,
																		strPersonName,
																		strTaskType,
																		strImplementNick);
														String strMsg = "";
														if (1 == nSelectTaskRetValue) {
															strMsg = "操作成功!";
														} else {
															strMsg = "操作失败!";
														}
														Message msg = new Message();
														Bundle b = new Bundle();// 存放数据
														b.putString("strMsg",
																strMsg);
														msg.setData(b);
														msg.what = 2;
														myhandler
																.sendMessage(msg);
													}
												};
												// 更新动态信息
												Thread thread = new Thread(
														run12);
												thread.start();
											}
										}
									}

								});
					}
					aboutDialog.show();
					credittextview.setText("人品:" + mnCredit);
					charmvaluetextview.setText(mnCharmValue + "");
				} else {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"请先登录", Toast.LENGTH_LONG);
				}
				break;
			case 7:
				if (mCommentInfos != null) {
					// 动态生成评论的界面
					AutoCreateCommentWidows();
				} else {
					if (!isConnectInternet()) {
						Toast.makeText(TaskDetailActivity.this, "网络不给力哦!",
								Toast.LENGTH_LONG).show();
					}
				}
				break;
			case 8:
				Bundle b3 = msg.getData();
				int nCommentIndex = b3.getInt("commentindex");
				// 显示图片
				Intent intent3 = new Intent(TaskDetailActivity.this,
						TaskDetailShowLargeImageActivity.class);
				intent3.putExtra("nCommentIndex", nCommentIndex);

				startActivity(intent3);
				break;
			case 9:
				if (mCommentInfos != null) {
					// 动态生成评论的界面
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
				SendCommentButton.setText("发送");
				SendCommentButton.setEnabled(true);
				SendCommentButton.setTextColor(Color.rgb(255, 255, 255));
				edittext1.setHint("评论");
				edittext1.setText("");
				// 将数据加入到我的中
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

	// 判断我的中是否有这个任务或分享,如果没有就加进去
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
			// 如果没有,那么就加入进去
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
							// 将数据插入到我的中
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
							// 将数据插入到我的中
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

		if (networkInfo != null) { // 这个判断一定要加上，要不然会出错
			return networkInfo.isAvailable();
		}
		return false;
	}

	// 开始向服务器发送添加数据的请求
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

	// 开始向服务器获取该任务的计时数据
	Runnable run2 = new Runnable() {
		public void run() {
			mnRemainTime = goodService
					.GetTaskRemainTime(strTaskId, strTaskType);
			// 发送消息,启动run1线程
			Message msg = myhandler.obtainMessage();
			msg.what = 3;
			myhandler.sendMessage(msg);
		}
	};

	// 开始向服务器获取任务描述图片
	Runnable run3 = new Runnable() {
		public void run() {
			taskicon = goodService.GetTaskSmallIcon(strTaskId, strTaskType);
			if (!mstrCustomerNameTemp.equals("")) {
				// 根据人名称获取相应的人品值和赞值
				mnCredit = goodService.GetCreditValue(mstrCustomerNameTemp);
				mnCharmValue = goodService.GetCharmValue(mstrCustomerNameTemp);
			}
			// 发送消息,启动run1线程
			Message msg = myhandler.obtainMessage();
			msg.what = 4;
			myhandler.sendMessage(msg);
		}
	};

	private void AutoCreateCommentWidows() {

		int nCount = mCommentInfos.size();
		// 先将linearlayout1清空
		linearlayout1.removeAllViews();
		if (nCount <= 0) {
			textview6.setText("暂无评论");
		} else {
			// 设置评论条数
			textview6.setText("" + mCommentInfos.size());

			for (int i = 0; i < nCount; i++) {

				final CommentInfo commentinfo = mCommentInfos.get(i);

				View cfg_view = getLayoutInflater().inflate(
						R.layout.comment_item, null);

				// 图标
				ImageView imageview = (ImageView) cfg_view
						.findViewById(R.id.comment_item_imageview1);
				// 评论人名称
				TextView AccountPerosn = (TextView) cfg_view
						.findViewById(R.id.comment_item_textview1);
				// 评论人真名
				TextView AccountPerosnTrueName = (TextView) cfg_view
						.findViewById(R.id.comment_item_textviewtruename);
				// 接收评论人名称
				TextView ReceivePerson = (TextView) cfg_view
						.findViewById(R.id.comment_item_textview3);
				// 评论时间
				TextView CommentTime = (TextView) cfg_view
						.findViewById(R.id.comment_item_textview4);

				// 评论内容
				TextView Contenttext = (TextView) cfg_view
						.findViewById(R.id.comment_item_textview5);
				// 评论图片
				ImageView largeimageview = (ImageView) cfg_view
						.findViewById(R.id.comment_item_imageview2);

				TextView textview10 = (TextView) cfg_view
						.findViewById(R.id.comment_item_textview2);

				Bitmap image = mUtils.base64ToBitmap(commentinfo
						.getstrCommentPersonImage());
				// 如果没有图标,那么就用默认图标
				if (null == image) {
					Resources res = getResources();
					image = BitmapFactory.decodeResource(res,
							R.drawable.noperson);
				}
				imageview.setImageBitmap(image);
				AccountPerosn.setText(commentinfo.getstrCommentPersonName());
				AccountPerosnTrueName.setText(commentinfo
						.getstrCommentPersonTrueName());
				// 如果没有接收人则不显示谁回复谁
				if (commentinfo.getstrCommentReceivePersonName().equals("")) {
					textview10.setText("");
				} else {
					textview10.setText("回复");
				}
				ReceivePerson.setText(commentinfo
						.getstrCommentReceivePersonName());
				CommentTime.setText(commentinfo.getstrCommentTime());
				// 判断评论是图片形式还是文字形式
				if (commentinfo.getnCommentIndex() > 0) {
					Contenttext.setVisibility(View.GONE);
					largeimageview.setVisibility(View.VISIBLE);
					// 评论采用图片形式
					largeimageview.setImageBitmap(mUtils
							.base64ToBitmap(commentinfo.getstrSmallImage()));
				} else {// 评论采用文字形式
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
						// 先判断评论大图片数据是否下载下来了
						int nIndex = commentinfo.getnCommentIndex();
						// 先判断评论大图片数据是否下载下来了
						String strImage = null;
						if (CommentMaps != null) {
							strImage = CommentMaps.get(nIndex + "");
						}
						// 如果之前没有下载该图片,那么就下载
						if (null == strImage) {
							// 启动获取评论数据线程
							Thread thread = new Thread(runcomment);
							thread.start();
						} else {// 直接显示图片
							Intent intent = new Intent(TaskDetailActivity.this,
									TaskDetailShowLargeImageActivity.class);
							intent.putExtra("nCommentIndex",
									commentinfo.getnCommentIndex());

							startActivity(intent);
						}
					}

				});
				// 处理点击事件
				cfg_view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String strCommentPersonName;// 评论人名称
						String strCommentPersonNickName;// 评论人名称(昵称)
						// 评论人名称
						TextView AccountPerosn = (TextView) v
								.findViewById(R.id.comment_item_textviewtruename);

						strCommentPersonName = AccountPerosn.getText()
								.toString();
						// 评论人名称(昵称)
						TextView AccountPerosnNick = (TextView) v
								.findViewById(R.id.comment_item_textview1);

						strCommentPersonNickName = AccountPerosnNick.getText()
								.toString();

						String strCurrentName = msettings.getString(
								"TruePersonName", "");
						// 如果指定回复人是自己,那么就不做指定处理
						if (!strCommentPersonName.equals(strCurrentName)) {
							String strContent = "回复" + strCommentPersonNickName;
							strReceiveName = strCommentPersonName;
							strReceiveNickName = strCommentPersonNickName;
							edittext1.setHint(strContent);
							bIsTalkToPerson = true;
						} else {
							edittext1.setHint("点击对方名字直接回复对方");
							bIsTalkToPerson = false;
						}
					}
				});
			}
		}
	}

	// 开始向服务器发送评论图片
	Runnable commentrun = new Runnable() {
		public void run() {
			// 当前用户名称
			String strCurrentName = msettings.getString("TruePersonName", "");
			// 当前用户昵称
			String strCurrentNickName = msettings.getString("PersonName", "");
			// 当前用户图标
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
			// 如果图片发送成功
			if (nRet > 0) {
				// 获取系统当前时间
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy年MM月dd日HH:mm:ss");
				Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
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
				strOperMsg = "操作成功";
			} else {
				strOperMsg = "操作失败";
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
			// 先判断该文件是否存在
			File pImageFile = new File(strImagePath);
			if (pImageFile.exists()) {
				// 对图片进行旋转处理
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
							TaskDetailActivity.this, "提示", "正在发送图片,请等待...",
							true);
					m_ProgressDialog.setCancelable(true);
					// 启动线程,将评论图片发送出去
					Thread comment1 = new Thread(commentrun);
					comment1.start();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (OutOfMemoryError e) {
					CommonUtils.ShowToastCenter(this, "内存不足!",
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
				// 对图片进行旋转处理
				bitmap = mUtils.PhotoRotation(uri);
				strCommentSmallImage = mUtils.BitmapToBase64BySize(mUtils
						.zoomBitmap(bitmap, nImageWidth, nImageHeight));
				strCommentLargeImage = mUtils.BitmapToBase64BySize(mUtils
						.zoomBitmap(bitmap));
				// 启动线程,将评论图片发送出去
				Thread comment1 = new Thread(commentrun);
				comment1.start();
				m_ProgressDialog = ProgressDialog.show(TaskDetailActivity.this,
						"提示", "正在发送图片,请等待...", true);
				m_ProgressDialog.setCancelable(true);
			} catch (OutOfMemoryError e) {
				CommonUtils.ShowToastCenter(this, "内存不足!", Toast.LENGTH_LONG);
			}
		}
	}

	protected Dialog onCreateDialog(int id) {
		AlertDialog dialog = null;
		AlertDialog.Builder builder = null;
		switch (id) {
		case 0:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("添加用户图片");
			builder.setItems(addPhoto, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == 0) {
						String haveSD = Environment.getExternalStorageState();
						if (!haveSD.equals(Environment.MEDIA_MOUNTED)) {
							Toast.makeText(TaskDetailActivity.this, "存储卡不可用",
									Toast.LENGTH_LONG).show();
							return;
						}
						File dir = new File(Environment
								.getExternalStorageDirectory()
								+ "/"
								+ "carPhoto");
						if (!dir.exists()) {
							dir.mkdirs();
						} else {// 如果已经存在了该文件夹
							// 判断该文件夹中是否存在Car.jpg文件,如果存在则删除
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
