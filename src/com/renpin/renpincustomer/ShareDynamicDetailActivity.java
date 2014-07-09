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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ShareDynamicDetailActivity extends Activity {
	// 任务id号
	private String strTaskId;
	// 发布者图标
	ImageView imageview1;
	// 发布者名称
	TextView textview1;
	// 任务发布时间
	TextView textview2;
	// 任务剩余时间
	TextView textview4;
	// 任务标题
	TextView textview5;
	TextView textview5_1;
	// 任务详细信息
	TextView textview6;
	// 分享者给接收者的总结
	// TextView textview8;
	// 接收者给分享者的总结
	TextView textview16;
	// 分享者给接收者发送的图片
	// ImageView imageview2;
	// 接收者给分享者发送的图片
	ImageView imageview10;
	// 评论图标
	ImageView imageview3;
	// 返回图标
	ImageView imageview4;
	// 评论条数
	TextView textview9;
	// 任务类型
	TextView textview18;

	private List<CommentInfo> mCommentInfos = null;
	// 存储最新获得的评论数据
	private List<CommentInfo> mNewCommentInfos = null;
	// 当前评论的最新时间
	private String strCommentTime = "2012年06月06日06:06:08";
	private// 用来存储从数据库中获取到的数据
	// 用来存储从数据库中获取到的数据
	List<HashMap<String, Object>> mListData = null;
	// 计时的剩余时间(秒)
	private static long mnRemainTime;
	private static CommonUtils mUtils = null;
	// 用来存储用户信息
	SharedPreferences msettings = null;
	// 判断是否继续倒计时
	boolean mbIsContinue = true;
	MyHandler myhandler = null;
	// 定时更新时间的线程对象
	private static Thread mthre = null;
	// 用来获取服务器上数据的对象
	private GoodService goodService = new GoodServiceImpl();
	// 发送评论编辑框
	ClearEditText edittext1;
	// 发送评论按钮
	Button button5;
	// 判断评论是否针对指定的人
	boolean bIsTalkToPerson = false;

	// 任务发布者名称
	private String strAccountName1;
	// 任务执行者名称
	private String strImpleName2;
	// 任务发布者昵称
	private String strCustomerNameNick;
	// 任务执行者昵称
	private String strImplementNameNick;
	// 任务发布者评论状态
	private int nAccountComment1;
	// 任务执行者评论状态
	private int nImpleComment1;

	// 接收评论的人名称
	private String strReceiveName;
	// 接收评论的人昵称
	private String strReceiveNickName;
	// 显示红点控件
	private ImageView imageview5;
	// 判断任务执行到的位置
	private int nTaskPosition = 1;
	// 状态图片的索引
	private int nStatusImageIndex = 0;
	// 显示评论的LinearLayout
	private LinearLayout linearlayout1 = null;
	private LinearLayout linearlayout2 = null;

	// 任务完成或分享图片控件
	private ImageView imagetask1;
	private ImageView imagetask2;
	private ImageView imagetask3;
	private ImageView imagetask4;
	private ImageView imagetask5;
	private ImageView imagetask6;

	// 存放任务或分享中的放大图片
	public static String[] strIcons = new String[6];
	// 存放任务缩放图片
	TaskIcon taskicon = null;
	// 存储本地数据库中的大图片数据
	TaskIcon largeicon = null;
	// 分享者发送给接收者的图片
	static String strSharePersonToRecImage = "";
	// 接收者发送给分享者的图片
	static String strRecToSharePersonImage = "";

	// 答谢按钮
	private Button thanksbutton;
	// 发送图片控件
	private ImageView sendpictureimageview;
	// 任务类型,1表示求助,2表示分享
	// private int mnTaskType;
	// 保存所选图片的路径
	private String mImgPaths = "";
	public static final String[] addPhoto = new String[] { "现在拍摄", "从相册选择",
			"取消" };
	// 存储评论中的大图片
	//public static HashMap<String, String> CommentMaps;
	// 保存刚刚选择的评论压缩图片
	private String strCommentSmallImage;
	// 保存刚刚选择的评论大图片
	private String strCommentLargeImage;
	// 人品值
	int mnCredit = 0;
	// 赞值
	int mnCharmValue = 0;
	// 显示人品值控件
	TextView credittextview;
	// 显示赞值控件
	TextView charmvaluetextview;
	// 任务重发按钮
	Button resendbutton;
	// 任务结束之后人品值和赞值的计数结果控件
	TextView taskendtextview;
	// 滚动条控件
	ScrollView scrollview;
	// 存储当前任务用户获取到的人品值和赞值
	CreditAndCharmForTask taskvalue;
	// 判断任务是否已经结束的标记
	int nVerfiValue = 1;
	// 答谢人信息控件
	TextView textviewthanksfrom;
	// 显示第一层缩小图片的linearlayout
	private LinearLayout smalliconslinearlayout1;
	// 显示第二层缩小图片的linearlayout
	private LinearLayout smalliconslinearlayout2;
	// 进度条对象
	public static ProgressDialog m_ProgressDialog = null;
	// 赞控件
	private LinearLayout zanimageviewlayout;
	//赞图标控件
	private ImageView zanimageview;
	// 赞值个数控件
	private TextView zantextview;
	// 显示评论条数的控件
	private TextView talktextview;
	// 显示浏览次数的控件
	private TextView looktimetextview;
	// 赞值
	private int nTaskCharmValue;
	// 评论条数
	private int nCommentNum;
	// 浏览数
	private int nBrowseNum;
	// 浏览次数是否成功加一
	private boolean bIsAddBrowseTime = false;
	// 显示发布区地址的控件
	private TextView addresstextview;
	// 发布区域地址
	private String strAddressRegion;
	// 显示图标的控件
	public static LinearLayout gridviewlinearlayout;
	// 显示图标的控件
	GridView gridView;
	// private Dialog builder;
	private int[] imageIds = new int[107];
	// 本地数据库对象
	private OperaDatabase mDatabase = null;
	// 用于分享到其他应用平台的图片按钮控件
	private ImageView ShareImageView;
	// 任务标题
	public String strItemTitleText;
	// 任务详细信息
	public String strItemDetailText;
	// 发布图片的数量
	private static final int mnImageNum = 6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 程序启动的时候避免光标处在editview中而弹出输入法窗口
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setContentView(R.layout.my_task);

		// 创建本地数据库对象
		mDatabase = new OperaDatabase(this);

		//CommentMaps = new HashMap<String, String>();

		strSharePersonToRecImage = "";
		strRecToSharePersonImage = "";

		String strCustomerNameTemp;// 发布人名称
		String strTaskAnnounceTime;// 发布时间
		String strTimeLimit;// 时间限制
		String strTaskTitle;// 任务标题
		String strDetail;// 任务详细内容
		String strRunSeconds;// 任务进行的秒数
		String strImplementName;// 任务执行人
		int nTaskTimeStatus;// 任务有效期状态
		int nTaskImpleStatus;// 任务执行状态
		// 动态信息
		int nTaskSelectType;
		int nTaskFinishType;
		int nTaskVerifiType;
		int nTaskAnnounceCommentType;
		int nTaskImplementCommentType;

		// 分享者对接收者的评论
		String strAnnounceComment;
		// 发布者给执行者的图片
		String strAnnounceBase64Image;
		// 接收者对分享者的评论
		String strImpleComment;
		// 执行者给发布者的图片
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
		/*
		 * mnTaskType = getIntent().getIntExtra(
		 * "com.renpin.RenPinMainActivity.nTaskType", -1);
		 */

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

		// 创建本地数据库对象
		mDatabase = new OperaDatabase(this);
		if (!mDatabase.IsTableExist(Integer.parseInt(strTaskId), 2)) {
			mDatabase.CloseDatabase();
			mDatabase = null;
			mDatabase = new OperaDatabase(this);
		}

		msettings = getSharedPreferences("MekeSharedPreferences", 0);
		// 创建接收消息对象
		myhandler = new MyHandler();
		if (null == mUtils) {
			// 创建公共操作类对象
			mUtils = new CommonUtils(this);
		}
		strAccountName1 = strCustomerNameTemp;
		strImpleName2 = strImplementName;
		nAccountComment1 = nTaskAnnounceCommentType;
		nImpleComment1 = nTaskImplementCommentType;
		// 保存任务是否完成的标记
		nVerfiValue = nTaskVerifiType;
		// 接收评论人名称默认为任务发布人名称
		strReceiveName = strCustomerNameTemp;
		strReceiveNickName = strCustomerNameNick;
		// 初始化界面
		InitActivities(strCustomerNameTemp, strTaskAnnounceTime, strTimeLimit,
				strTaskTitle, strDetail, strRunSeconds, strImplementName,
				nTaskTimeStatus, nTaskImpleStatus, nTaskSelectType,
				nTaskFinishType, nTaskVerifiType, nTaskAnnounceCommentType,
				nTaskImplementCommentType, strAnnounceComment,
				strAnnounceBase64Image, strImpleComment, strImpleBase64Image);
		InitGridView();
		// 隐藏界面
		// init();
		// 更新红点状态
		UpdateDynamicStatus(strCustomerNameTemp, strImplementName, 3,
				strTaskId, nTaskSelectType, nTaskFinishType, nTaskVerifiType,
				nTaskAnnounceCommentType, nTaskImplementCommentType);
		// 显示评论数据
		ShowComments();
		// 显示人品值,赞值,任务到期剩余值
		ShowOtherData();
		// 显示图片
		ShowPictures();
		// 显示浏览次数
		ShowBrowseTime();
	}

	private boolean GetOtherSmallImageData() {
		boolean bIsFind = true;
		taskicon = new TaskIcon("", "", "", "", "", "");
		// 存储图片文件的路径
		String strImageFilePath = "";
		// 根目录路径
		String strRootPath = mUtils.GetFileRootPath();
		boolean bIsExist = false;
		// 图片
		Bitmap bmp = null;
		// 先判断其他图片是否存在
		for (int i = 2; i <= mnImageNum; i++) {

			strImageFilePath = strRootPath + strTaskId + "_" + 2 + "_" + i;
			// 判断本地中是否保存了该图片
			bIsExist = mUtils.fileIsExists(strImageFilePath);
			// 如果本地存在该图片数据
			if (bIsExist) {
				// 将图片从本地中读出
				bmp = mUtils.GetBitMapFromPathNew(strImageFilePath);
				// 将bmp转成base64
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

			} else {// 只要发现一张图片不存在就返回false
				bIsFind = false;
				break;
			}
		}
		return bIsFind;
	}

	// 获取大图
	private boolean GetLargeImageData() {
		largeicon = new TaskIcon("", "", "", "", "", "");
		boolean bIsFind = true;
		// 存储图片文件的路径
		String strImageFilePath = "";
		// 根目录路径
		String strRootPath = mUtils.GetFileRootPath();
		boolean bIsExist = false;
		// 图片
		Bitmap bmp = null;
		// 先判断其他图片是否存在
		for (int i = 1; i <= mnImageNum; i++) {

			strImageFilePath = strRootPath + strTaskId + "_" + 2 + "_" + i
					+ "_" + "l";
			// 判断本地中是否保存了该图片
			bIsExist = mUtils.fileIsExists(strImageFilePath);
			// 如果本地存在该图片数据
			if (bIsExist) {
				// 将图片从本地中读出
				bmp = mUtils.GetBitMapFromPathNew(strImageFilePath);
				// 将bmp转成base64
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
			} else {// 只要发现一张图片不存在就返回false
				bIsFind = false;
				break;
			}
		}
		return bIsFind;
	}

	// 从本地文件中提取图片数据
	private void GainImageData() {
		boolean bIsSmallFind = GetOtherSmallImageData();
		boolean bIsLargeFind = GetLargeImageData();
		// 存储图片文件的路径
		String strImageFilePath = "";
		// 根目录路径
		String strRootPath = mUtils.GetFileRootPath();
		strImageFilePath = strRootPath + strTaskId + "_" + 2 + "_" + 1;
		// 将第一张图片从本地中读出
		Bitmap bmp = mUtils.GetBitMapFromPathNew(strImageFilePath);
		// 将bmp转成base64
		String strBase64Image = mUtils.BitmapToBase64BySize(bmp);
		// 如果其他图片也加载了,那么将第一张图片的值也赋过去
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

				// 从本地数据库中获取评论数据
				mCommentInfos = mDatabase.GetCommentData(
						Integer.parseInt(strTaskId), 2);
				String strTime = mDatabase.GetCommentTime(
						Integer.parseInt(strTaskId), 2);
				if (!strTime.equals("")) {
					strCommentTime = strTime;
				}

				mNewCommentInfos = goodService.GetCommentsForTaskNew1(
						strTaskId, "2", strAccountName, strCommentTime);

				Message msg = myhandler.obtainMessage();
				msg.what = 15;
				myhandler.sendMessage(msg);
			}
		};
		// 启动获取评论数据线程
		Thread thread = new Thread(localrun);
		thread.start();
	}

	private void ShowOtherData() {
		Runnable run2 = new Runnable() {
			public void run() {
				String strAccountName = msettings.getString("TruePersonName",
						"");

				mnRemainTime = goodService.GetTaskRemainTime(strTaskId, "2");

				if (!strAccountName1.equals("")) {
					// 根据人名称获取相应的人品值和赞值
					mnCredit = goodService.GetCreditValue(strAccountName1);
					mnCharmValue = goodService.GetCharmValue(strAccountName1);
				}
				// 如果当前任务是由该用户分享的,且该分享已经结束
				if (strAccountName1.equals(strAccountName)
						&& (2 == nVerfiValue || 3 == nVerfiValue)) {
					// taskvalue =
					// goodService.GetCreditAndCharmForTask(strTaskId,
					// "2");
				}

				Message msg = myhandler.obtainMessage();
				msg.what = 3;
				myhandler.sendMessage(msg);
			}
		};
		// 启动获取评论数据线程
		Thread thread = new Thread(run2);
		thread.start();
	}

	private void ShowPictures() {
		Runnable runpic = new Runnable() {
			public void run() {
				int nRet = 1;

				GainImageData();

				// 如果本地数据库中没有找到小图片,那么就到服务器中去获取
				if (null == taskicon) {
					nRet = 0;
					// 获取任务缩放图片
					taskicon = goodService.GetTaskSmallIcon(strTaskId, "2");
				}
				// 如果本地数据库中没有找到大图片,那么就到服务器中去获取
				if (null == largeicon) {
					nRet = 0;
					// 获取每张缩小图的放大图片
					strIcons[0] = goodService.GetTaskLargeIcon(strTaskId, "2",
							1);
					strIcons[1] = goodService.GetTaskLargeIcon(strTaskId, "2",
							2);
					strIcons[2] = goodService.GetTaskLargeIcon(strTaskId, "2",
							3);
					strIcons[3] = goodService.GetTaskLargeIcon(strTaskId, "2",
							4);
					strIcons[4] = goodService.GetTaskLargeIcon(strTaskId, "2",
							5);
					strIcons[5] = goodService.GetTaskLargeIcon(strTaskId, "2",
							6);
				} else {
					strIcons[0] = largeicon.getstrIcon1();
					strIcons[1] = largeicon.getstrIcon2();
					strIcons[2] = largeicon.getstrIcon3();
					strIcons[3] = largeicon.getstrIcon4();
					strIcons[4] = largeicon.getstrIcon5();
					strIcons[5] = largeicon.getstrIcon6();
				}
				// 如果没有找到图片数据,那么就将图片数据保存到本地数据库中
				/*
				 * if (0 == nRet) {
				 * mDatabase.InsertImage(Integer.parseInt(strTaskId), 2,
				 * taskicon.getstrIcon1(), taskicon.getstrIcon2(),
				 * taskicon.getstrIcon3(), taskicon.getstrIcon4(),
				 * taskicon.getstrIcon5(), taskicon.getstrIcon6(), strIcons[0],
				 * strIcons[1], strIcons[2], strIcons[3], strIcons[4],
				 * strIcons[5]); }
				 */

				Message msg = myhandler.obtainMessage();
				msg.what = 11;
				Bundle b = new Bundle();// 存放数据
				b.putInt("nRet", nRet);
				msg.setData(b);
				myhandler.sendMessage(msg);
			}
		};
		// 启动获取评论数据线程
		Thread thread = new Thread(runpic);
		thread.start();
	}

	private void ShowBrowseTime() {
		Runnable browserun = new Runnable() {
			public void run() {
				int nRet = goodService.AddBrowseTimes(strTaskId, "2");

				Message msg = myhandler.obtainMessage();
				Bundle b = new Bundle();// 存放数据
				b.putInt("nRet", nRet);
				msg.setData(b);
				msg.what = 13;
				myhandler.sendMessage(msg);
			}
		};
		// 启动获取评论数据线程
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
				// 如果这个任务是由当前用户创建的
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

				} else if (CurrentPersonName.equals(strImpleAccouceName)) {// 如果这个任务是由当前用户执行的
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
		textview1 = (TextView) findViewById(R.id.mytask_textview1);
		textview2 = (TextView) findViewById(R.id.mytask_textview2);
		textview4 = (TextView) findViewById(R.id.mytask_textview4);
		textview5 = (TextView) findViewById(R.id.mytask_textview5);
		textview5.setClickable(false);
		textview5_1 = (TextView) findViewById(R.id.mytask_textview_title);
		textview6 = (TextView) findViewById(R.id.mytask_textview6);
		textview6.setClickable(false);
		imageview3 = (ImageView) findViewById(R.id.mytask_imageview3);
		textview9 = (TextView) findViewById(R.id.mytask_textview9);
		credittextview = (TextView) findViewById(R.id.mytask_credittextview);
		charmvaluetextview = (TextView) findViewById(R.id.mytask_charmtextview);

		imageview4 = (ImageView) findViewById(R.id.customer_login_image1);
		edittext1 = (ClearEditText) findViewById(R.id.my_task_edittext1);
		edittext1.SetType(2);
		button5 = (Button) findViewById(R.id.my_task_button1);
		resendbutton = (Button) findViewById(R.id.mytask_resendbutton);
		imageview5 = (ImageView) findViewById(R.id.mytask_imageview4);
		linearlayout1 = (LinearLayout) findViewById(R.id.mytask_linearlayout1);
		linearlayout2 = (LinearLayout) findViewById(R.id.comment_item_linearlayout1);

		imageview10 = (ImageView) findViewById(R.id.mytask_imageview10);
		textview16 = (TextView) findViewById(R.id.mytask_textview16);
		textview18 = (TextView) findViewById(R.id.mytask_textview17);
		thanksbutton = (Button) findViewById(R.id.mytask_thanksbutton);
		sendpictureimageview = (ImageView) findViewById(R.id.mytask_sendpicture);
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
		zanimageview = (ImageView)findViewById(R.id.mytask_zanimageview);
		Resources res = getResources();
		//判断是否点过赞
		if(RenPinMainActivity.StoreZanMap.containsKey(strTaskId)){
			// 判断该任务的状态
			Bitmap zanbmp = BitmapFactory.decodeResource(res, R.drawable.zan1);
			zanimageview.setImageBitmap(zanbmp);	
		}else{
			Bitmap zanbmp = BitmapFactory.decodeResource(res, R.drawable.zan0);
			zanimageview.setImageBitmap(zanbmp);
		}
		zantextview = (TextView) findViewById(R.id.mytask_zantextview);
		zantextview.setText("");
		talktextview = (TextView) findViewById(R.id.mytask_talknumtextview);
		talktextview.setText("");
		looktimetextview = (TextView) findViewById(R.id.mytask_looktimetextview);
		looktimetextview.setText("");
		addresstextview = (TextView) findViewById(R.id.mytask_addresstextview);
		addresstextview.setText(strAddressRegion);
		gridviewlinearlayout = (LinearLayout) findViewById(R.id.mytask_gridviewlayout);
		ShareImageView = (ImageView) findViewById(R.id.mytask_shareimageview);
		// 一开始隐藏
		gridviewlinearlayout.setVisibility(View.GONE);
		// 任务发布图片控件
		imagetask1 = (ImageView) findViewById(R.id.mytask_icon1);
		imagetask2 = (ImageView) findViewById(R.id.mytask_icon2);
		imagetask3 = (ImageView) findViewById(R.id.mytask_icon3);
		imagetask4 = (ImageView) findViewById(R.id.mytask_icon4);
		imagetask5 = (ImageView) findViewById(R.id.mytask_icon5);
		imagetask6 = (ImageView) findViewById(R.id.mytask_icon6);
		imagetask1.setClickable(false);
		imagetask2.setClickable(false);
		imagetask3.setClickable(false);
		imagetask4.setClickable(false);
		imagetask5.setClickable(false);
		imagetask6.setClickable(false);

		textview18.setText("【分享】");

		Bitmap bmp = RenPinMainActivity.mAnnounceImage;
		if (bmp != null) {
			imageview1.setImageBitmap(bmp);
		}
		textview1.setText(strCustomerNameNick);
		textview2.setText(strTaskAnnounceTime);
		int nLimit = Integer.parseInt(strTimeLimit);
		int nRunSecond = Integer.parseInt(strRunSeconds);
		mnRemainTime = nLimit - nRunSecond;
		String strAccountName = msettings.getString("TruePersonName", "");
		String strTime = mUtils.GetStringBySeconds(mnRemainTime);
		// textview4.setText(strTime);
		textview5.setText(strTaskTitle);
		textview6.setText(strDetail);
		textview5_1.setText(strTaskTitle);

		if (null == mthre/* && mnRemainTime > 0 */) {
			mbIsContinue = true;
			mthre = new Thread(run1);
			mthre.start();
		}

		// 如果这个任务是由当前用户发布的,那么就可以刷新
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
		// 如果这个任务是由当前用户抢下的
		if (strImplementName.equals(strAccountName)) {

			// resendbutton.setVisibility(View.GONE);
			thanksbutton.setVisibility(View.VISIBLE);

			if (2 == nTaskVerifiType || 3 == nTaskVerifiType) {// 如果这个任务验证成功
				// textview8.setText(strAnnounceComment);
				textview16.setText(strImpleComment);
				thanksbutton.setVisibility(View.GONE);

				if (strImpleBase64Image.equals("")) {
					imageview10.setVisibility(View.GONE);
				} else {
					imageview10.setImageBitmap(mUtils
							.base64ToBitmap(strImpleBase64Image));
				}

			} else if (4 == nTaskVerifiType) {// 如果这个任务验证失败

				textview16.setText(strImpleComment);

				if (strImpleBase64Image.equals("")) {
					imageview10.setVisibility(View.GONE);
				} else {
					imageview10.setImageBitmap(mUtils
							.base64ToBitmap(strImpleBase64Image));
				}

			} else if (2 == nTaskFinishType || 3 == nTaskFinishType) {// 如果这个分享已经完成等待接收者确认

				imageview10.setVisibility(View.GONE);

			} else if (!strImplementName.equals("")
					&& (2 == nTaskSelectType || 3 == nTaskSelectType)) {// 如果这个任务已经有人接收,正在执行

				imageview10.setVisibility(View.GONE);
			}
		} else {// 如果这个任务是由当前用户分享的

			// 除非任务已经结束,否则都允许任务重置
			/*
			 * if (1 == nTaskVerifiType) {
			 * resendbutton.setVisibility(View.VISIBLE); } else {
			 * resendbutton.setVisibility(View.GONE); }
			 */
			thanksbutton.setVisibility(View.GONE);
			// 如果这个任务还没有人接收
			if (strImplementName.equals("")) {
				imageview10.setVisibility(View.GONE);
			} else if (2 == nTaskVerifiType || 3 == nTaskVerifiType) {// 这个任务验证成功
				// textview8.setText(strAnnounceComment);
				textview16.setText(strImpleComment);
				if (strImpleBase64Image.equals("")) {
					imageview10.setVisibility(View.GONE);
				} else {
					imageview10.setImageBitmap(mUtils
							.base64ToBitmap(strImpleBase64Image));
				}
				// 显示来自谁的答谢
				textviewthanksfrom.setVisibility(View.VISIBLE);
				String strThanks = "来自";
				strThanks += strImplementNameNick;
				strThanks += "的答谢";
				textviewthanksfrom.setText(strThanks);

			} else if (4 == nTaskVerifiType) {// 这个任务验证失败
				// textview8.setText(strAnnounceComment);
				textview16.setText(strImpleComment);
				if (strImpleBase64Image.equals("")) {
					imageview10.setVisibility(View.GONE);
				} else {
					imageview10.setImageBitmap(mUtils
							.base64ToBitmap(strImpleBase64Image));
				}
				textviewthanksfrom.setVisibility(View.VISIBLE);
				String strThanks = strImplementNameNick;
				strThanks += "抢下了这个分享";
				textviewthanksfrom.setText(strThanks);

			} else if (2 == nTaskFinishType || 3 == nTaskFinishType) {// 这个任务等待确认

				textviewthanksfrom.setVisibility(View.VISIBLE);
				String strThanks = strImplementNameNick;
				strThanks += "抢下了这个分享";
				textviewthanksfrom.setText(strThanks);

			} else if (2 == nTaskSelectType || 3 == nTaskSelectType) {// 这个任务正在执行

				textviewthanksfrom.setVisibility(View.VISIBLE);
				String strThanks = strImplementNameNick;
				strThanks += "抢下了这个分享";
				textviewthanksfrom.setText(strThanks);
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
					data.putExtra("strTaskType", "2");
					data.putExtra("strZan", strZan);
					data.putExtra("strTalk", strTalk);
					data.putExtra("strLook", strLook);
					setResult(1, data);
				} else {
					setResult(2);
				}
				ShareDynamicDetailActivity.this.finish();
			}
		});

		thanksbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {// //将任务完成状态修改,值改为2
				Intent it = new Intent(ShareDynamicDetailActivity.this,
						TaskAnnounceVertifOperaActivity.class);
				it.putExtra(
						"com.renpin.DynamicDetailActivity.strImplementName",
						strCustomerNameTemp);
				it.putExtra("com.renpin.RenPinMainActivity.TaskId", strTaskId);
				it.putExtra("com.renpin.RenPinMainActivity.TaskType", "2");
				startActivityForResult(it, 2);
				// startActivity(it);
			}
		});

		resendbutton.setOnClickListener(new OnClickListener() {

			Runnable runre = new Runnable() {
				public void run() {
					int nRet = 0;

					// 将这个任务重新设置为没有拿下或抢下状态
					nRet = goodService.ResetTaskStatue(strTaskId, "2");

					String strOperMsg;
					if (1 == nRet) {
						strOperMsg = "操作成功";
					} else {
						strOperMsg = "操作失败";
					}
					Message msg = myhandler.obtainMessage();
					msg.what = 9;
					Bundle b = new Bundle();// 存放数据
					b.putString("OperMsg", strOperMsg);
					msg.setData(b);
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						ShareDynamicDetailActivity.this);
				dialog.setMessage("您的分享将恢复到发布时的初始状态");
				dialog.setTitle("提示");
				dialog.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 启动更新状态数据线程
								Thread thread = new Thread(runre);
								thread.start();
							}
						}).setNegativeButton("取消",
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
							strTaskId, "2", strAccountNameTemp, strCommentTime);
					Message msg = myhandler.obtainMessage();
					msg.what = 3;
					myhandler.sendMessage(msg);

					int nRet = 0;
					// 更新执行者评论状态
					nRet = goodService.UpdateCommentType(strAccountNameTemp, 3,
							strTaskId, "2");
					// 如果查看的任务是由当前用户发布的
					/*
					 * if (strAccountNameTemp.equals(strAccountName1)) { // if
					 * (2 == nImpleComment1) { // 更新执行者评论状态 nRet =
					 * goodService.UpdateCommentType( strAccountName1, 3,
					 * strTaskId, "2"); // }
					 * 
					 * } else if (strAccountNameTemp.equals(strImpleName2)) {//
					 * 如果查看的任务是由当前用户执行的 // if (2 == nAccountComment1) { //
					 * 更新发布者评论状态 nRet = goodService.UpdateCommentType(
					 * strImpleName2, 3, strTaskId, "2"); // } }
					 */
				}
			};

			@Override
			public void onClick(View v) {
				// 启动获取评论数据线程
				Thread thread = new Thread(GetCommentRun);
				thread.start();
			}
		});

		button5.setOnClickListener(new OnClickListener() {

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
					String strContent1 = ShareDynamicDetailActivity.this.edittext1
							.getText().toString();
					// 去除了前面空格的字符串
					String strClearSpace = CommonUtils
							.ClearFrontSpace(strContent1);
					char cFirstChar = strClearSpace.charAt(0);
					// 判断是否是悄悄话
					int nSecretType = 0;
					if ('*' == cFirstChar) {
						nSecretType = 1;
					}

					String zhengze = "f0[0-9]{2}|f10[0-7]";
					SpannableString spannableString = ExpressionUtil
							.getExpressionString(
									ShareDynamicDetailActivity.this,
									strClearSpace, zhengze);

					String strOtherPersonName = "";
					String strOtherPersonNickName = "";
					String strTime = "";
					if (bIsTalkToPerson) {
						// 评论内容
						strTime = goodService.SendCommentContentNewSecretTime(
								strTaskId, strCurrentName, strReceiveName,
								spannableString.toString(), "2",
								strCurrentNickName, strReceiveNickName,
								nSecretType);
						strOtherPersonName = strReceiveName;
						strOtherPersonNickName = strReceiveNickName;
					} else {
						// 如果消息发送人发布了当前任务或分享,那么发送消息就不用通知发布人
						if (!strCurrentName.equals(strAccountName1)) {
							// 评论内容
							strTime = goodService
									.SendCommentContentNewSecretTime(strTaskId,
											strCurrentName, strAccountName1,
											spannableString.toString(), "2",
											strCurrentNickName,
											strCustomerNameNick, nSecretType);
							strOtherPersonName = strAccountName1;
							strOtherPersonNickName = strCustomerNameNick;
						} else {
							// 评论内容
							strTime = goodService.SendCommentContentNewSecretTime(
									strTaskId, strCurrentName, "",
									spannableString.toString(), "2",
									strCurrentNickName, "", nSecretType);
						}
					}
					// 如果发送成功,那么就刷新评论内容
					if (!strTime.equals("")) {

						// CreateDatabaseAndStoreData();
						// 将新评论内容保存到本地数据库中
						/*
						 * mDatabase.InsertCommentData(
						 * Integer.parseInt(strTaskId), 2, 0, strCurrentImage,
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
						strOperMsg = "操作成功";
					} else {
						strOperMsg = "操作失败";
					}
					Message msg = myhandler.obtainMessage();
					msg.what = 4;
					Bundle b = new Bundle();// 存放数据
					b.putString("OperMsg", strOperMsg);
					msg.setData(b);
					myhandler.sendMessage(msg);

				}
			};

			@Override
			public void onClick(View v) {
				String strContent1 = ShareDynamicDetailActivity.this.edittext1
						.getText().toString();
				if (!strContent1.equals("")) {
					button5.setText("正在发送...");
					button5.setEnabled(false);
					button5.setTextColor(Color.rgb(96, 96, 96));
					// 启动获取评论数据线程
					Thread thread = new Thread(SendCommentContent);
					thread.start();
				} else {
					CommonUtils.ShowToastCenter(getBaseContext(), "发送的内容不能为空",
							Toast.LENGTH_LONG);
				}
			}
		});

		imagetask1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShareDynamicDetailActivity.this,
						ViewPagerActivity.class);
				intent.putExtra("type", 2);
				intent.putExtra("iconposion", 0);
				startActivity(intent);
			}

		});

		imagetask2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShareDynamicDetailActivity.this,
						ViewPagerActivity.class);
				intent.putExtra("type", 2);
				intent.putExtra("iconposion", 1);
				startActivity(intent);
			}

		});

		imagetask3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShareDynamicDetailActivity.this,
						ViewPagerActivity.class);
				intent.putExtra("type", 2);
				intent.putExtra("iconposion", 2);
				startActivity(intent);
			}

		});

		imagetask4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShareDynamicDetailActivity.this,
						ViewPagerActivity.class);
				intent.putExtra("type", 2);
				intent.putExtra("iconposion", 3);
				startActivity(intent);
			}

		});

		imagetask5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShareDynamicDetailActivity.this,
						ViewPagerActivity.class);
				intent.putExtra("type", 2);
				intent.putExtra("iconposion", 4);
				startActivity(intent);
			}

		});

		imagetask6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShareDynamicDetailActivity.this,
						ViewPagerActivity.class);
				intent.putExtra("type", 2);
				intent.putExtra("iconposion", 5);
				startActivity(intent);
			}

		});

		imageview10.setOnClickListener(new OnClickListener() {

			Runnable runimageview10 = new Runnable() {
				public void run() {
					if (strRecToSharePersonImage.equals("")
							|| null == strRecToSharePersonImage) {
						strRecToSharePersonImage = goodService
								.GetTaskVerifiLargeImage(strTaskId, "2");
					}
					Message msg = myhandler.obtainMessage();
					msg.what = 6;
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {
				// 启动获取评论数据线程
				Thread thread = new Thread(runimageview10);
				thread.start();
			}

		});

		zanimageviewlayout.setOnClickListener(new OnClickListener() {

			Runnable zanrun = new Runnable() {
				public void run() {
					int nRet = 0;
					// 先获取当前用户名称
					String strCurName = msettings.getString("TruePersonName",
							"");
					// 避免自己给自己赞
					if (!strAccountName1.equals(strCurName)) {

						nRet = goodService.PraiseToTaskOrShare(strTaskId,
								strCurName, strAccountName1, "2");
						if(1 == nRet){
							//判断容器是否超过了最大的容量,如果超过了就清空
							if(RenPinMainActivity.StoreZanMap.size() >= RenPinMainActivity.MaxStoreMap){
								RenPinMainActivity.StoreZanMap.clear();
							}
							RenPinMainActivity.StoreZanMap.put(strTaskId, 1);
						}
					} else {
						nRet = 3;// 表示不能给自己赞
					}
					Message msg = myhandler.obtainMessage();
					Bundle b = new Bundle();// 存放数据
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
							ShareDynamicDetailActivity.this, 4,
							new PriorityListener() {

								String strTextTemp = "";
								Runnable updaterun = new Runnable() {
									public void run() {
										int nRet = goodService
												.UpdateItemTitleText(strTaskId,
														"2", strTextTemp);

										Message msg = myhandler.obtainMessage();
										Bundle b = new Bundle();// 存放数据
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
									// 判断是不是点击了保存按钮
									if (str1.equals("1")) {
										// 如果内容修改了
										if (!str2.equals(strDetialText)) {
											strTextTemp = str2;
											// 启动修改数据的线程
											Thread thre = new Thread(updaterun);
											thre.start();
										} else {
											Message msg = myhandler
													.obtainMessage();
											Bundle b = new Bundle();// 存放数据
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
							ShareDynamicDetailActivity.this, 4,
							new PriorityListener() {

								String strTextTemp = "";
								Runnable updaterun = new Runnable() {
									public void run() {
										int nRet = goodService
												.UpdateItemDetailText(
														strTaskId, "2",
														strTextTemp);

										Message msg = myhandler.obtainMessage();
										Bundle b = new Bundle();// 存放数据
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
									// 判断是不是点击了保存按钮
									if (str1.equals("1")) {
										// 如果内容修改了
										if (!str2.equals(strDetialText)) {
											strTextTemp = str2;
											// 启动修改数据的线程
											Thread thre = new Thread(updaterun);
											thre.start();
										} else {
											Message msg = myhandler
													.obtainMessage();
											Bundle b = new Bundle();// 存放数据
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
				// 截取屏幕图片
				ScreenShot screen = new ScreenShot(
						ShareDynamicDetailActivity.this);
				Bitmap screenbitmap = screen.GetCurrenScreenBitmap();
				if (screenbitmap != null) {
					// 获取分享图片的保存路径
					String strFilePath = screen
							.GetImageFilePath(MyApplication.sharefile);
					// 将图片保存到指定的路径下
					try {
						mUtils.saveBitmapToFile(screenbitmap, strFilePath);
					} catch (IOException e) {
						e.printStackTrace();
					}
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("image/*");
					intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
					intent.putExtra(Intent.EXTRA_STREAM,
							Uri.fromFile(new File(strFilePath)));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(Intent.createChooser(intent, getTitle()));
				} else {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"分享失败", Toast.LENGTH_LONG);
				}
			}

		});
	}

	@SuppressWarnings("deprecation")
	private void ShowTaskStatus(String strImpleAccouceName,
			int nTaskSelectType, int nTaskFinishType, int nTaskVerifiType) {

		nTaskPosition = 1;

		if (2 == nTaskSelectType || 3 == nTaskSelectType) {

			nTaskPosition = 2;
		}

		if (2 == nTaskFinishType || 3 == nTaskFinishType) {
			nTaskPosition = 3;
		}

		if (2 == nTaskVerifiType || 3 == nTaskVerifiType) {
			nTaskPosition = 4;
		}
	}

	// 更新用户动态
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
					// 更新发布者评论状态
					nRet = goodService.UpdateCommentType(strAccountNameTemp,
							nValue, strTaskId, "2");
				}
				// 如果查看的是由当前用户分享的
				/*
				 * if (strAccountNameTemp.equals(strAnnounceName)) { if (2 ==
				 * nTaskSelectType) { // 更新分享抢下状态 nRet =
				 * goodService.UpdateTaskSelectType( strAnnounceName, nValue,
				 * strTaskId, "2"); } if (2 == nTaskVerifiType) { // 更新验证状态 nRet
				 * = goodService.UpdateTaskVerifiType(2, strAnnounceName,
				 * nValue, strTaskId, "", ""); } if (2 ==
				 * nTaskImplementCommentType) { // 更新执行者评论状态 nRet =
				 * goodService.UpdateCommentType( strAnnounceName, nValue,
				 * strTaskId, "2"); }
				 * 
				 * } else if (strAccountNameTemp.equals(strImpleName)) {//
				 * 如果查看的任务是由当前用户接收的 if (2 == nTaskFinishType) { // 更新完成状态 nRet =
				 * goodService.UpdateTaskFinishType(2, strImpleName, nValue,
				 * strTaskId, "", ""); } if (2 == nTaskAnnounceCommentType) { //
				 * 更新发布者评论状态 nRet = goodService.UpdateCommentType( strImpleName,
				 * nValue, strTaskId, "2"); } }
				 */
				String strOperMsg;
				if (1 == nRet) {
					strOperMsg = "操作成功";
				} else {
					strOperMsg = "操作失败";
				}
				Message msg = myhandler.obtainMessage();
				msg.what = 2;
				Bundle b = new Bundle();// 存放数据
				b.putString("OperMsg", strOperMsg);
				msg.setData(b);
				myhandler.sendMessage(msg);
			}
		};
		// 启动更新状态数据线程
		Thread thread = new Thread(runup);
		thread.start();
	}

	private void CombineCommentData() {
		int nSize1 = mNewCommentInfos.size();
		int nSize2 = mCommentInfos.size();
		if (nSize1 > 0) {
			// 更新最新评论时间
			strCommentTime = mNewCommentInfos.get(nSize1 - 1)
					.getstrCommentTime();
		}
		for (int i = 0; i < nSize1; i++) {
			boolean bIsFind = false;
			for (int j = 0; j < nSize2; j++) {
				// 如果评论内容、时间、评论人、接收人相同,那么就说明是同一条评论
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
			// 如果没有找到,那么就将该条数据插入到本地数据库中
			if (!bIsFind && nSize2 > 0) {
				mCommentInfos.add(mNewCommentInfos.get(i));
				int nSecretType = 0;
				// 如果为文字内容
				if (0 == mNewCommentInfos.get(i).getnCommentIndex()) {
					String strContent1 = mNewCommentInfos.get(i)
							.getstrCommentContent();
					// 去除了前面空格的字符串
					String strClearSpace = CommonUtils
							.ClearFrontSpace(strContent1);
					if (strClearSpace.length() > 0) {
						char cFirstChar = strClearSpace.charAt(0);
						// 判断是否是悄悄话
						if ('*' == cFirstChar) {
							nSecretType = 1;
						}
					}
				}
				// 将数据保存到本地数据库中
				mDatabase
						.InsertCommentData(Integer.parseInt(strTaskId), 2,
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
				// 如果为文字内容
				if (0 == mNewCommentInfos.get(i).getnCommentIndex()) {
					String strContent1 = mNewCommentInfos.get(i)
							.getstrCommentContent();
					// 去除了前面空格的字符串
					String strClearSpace = CommonUtils
							.ClearFrontSpace(strContent1);
					if (strClearSpace.length() > 0) {
						char cFirstChar = strClearSpace.charAt(0);
						// 判断是否是悄悄话
						if ('*' == cFirstChar) {
							nSecretType = 1;
						}
					}
				}
				// 将数据保存到本地数据库中
				mDatabase
						.InsertCommentData(Integer.parseInt(strTaskId), 2,
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

	// 将图片保存到本地
	private void SaveImageToLocal() {
		// 存储图片文件的路径
		String strImageFilePath = "";
		// 根目录路径
		String strRootPath = mUtils.GetFileRootPath();
		Bitmap bmp = null;
		for (int i = 2; i <= mnImageNum; i++) {
			strImageFilePath = strRootPath + strTaskId + "_" + 2 + "_" + i;
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
			strImageFilePath = strRootPath + strTaskId + "_" + 2 + "_" + i
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
		// 子类必须重写此方法,接受数据
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Bundle b = msg.getData();
				String strMsg = b.getString("Msg");
				CommonUtils.ShowToastCenter(ShareDynamicDetailActivity.this,
						strMsg, Toast.LENGTH_LONG);
				break;
			case 1:
				long nTime = mnRemainTime;
				if (nTime >= 0) {
					String strTime = mUtils.GetStringBySeconds(nTime);
					textview4.setText(strTime);
					mnRemainTime -= 1;
					if (mnRemainTime <= 0) {
						/*
						 * button1.setTextColor(Color.rgb(96, 96, 96));
						 * button1.setEnabled(false);
						 * button2.setTextColor(Color.rgb(96, 96, 96));
						 * button2.setEnabled(false);
						 * button3.setTextColor(Color.rgb(96, 96, 96));
						 * button3.setEnabled(false);
						 * button4.setTextColor(Color.rgb(96, 96, 96));
						 * button4.setEnabled(false);
						 */
					}
				} else if (mnRemainTime < 0) {
					// mbIsContinue = false;
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
						// 设置评论条数
						textview9.setText("" + mCommentInfos.size());
					} else {
						// 设置评论条数
						textview9.setText("0");
					}
				} else {
					imageview5.setImageBitmap(null);
				}
				// 显示任务运行状态
				// ShowTaskRunStatus();
				// 实时动态显示任务运行过程
				// UpdateStatusText(strTaskId);
				break;

			case 2:
				/*
				 * Bundle b1 = msg.getData(); String strOperMsg =
				 * b1.getString("OperMsg");
				 * Toast.makeText(ShareDynamicDetailActivity.this, strOperMsg,
				 * Toast.LENGTH_LONG).show();
				 */
				break;
			case 3:
				credittextview.setText("人品:" + mnCredit);
				charmvaluetextview.setText(mnCharmValue + "");
				zantextview.setText(nTaskCharmValue + "");

				if (taskvalue != null) {
					String strText = "本次任务结束,您人品值+";
					strText += taskvalue.getnCreditValue();
					strText += ", 赞+";
					strText += taskvalue.getnCharmValue();
					taskendtextview.setText(strText);
				}

				break;
			case 4:
				Bundle b2 = msg.getData();
				String strOperMsg1 = b2.getString("OperMsg");
				Toast.makeText(ShareDynamicDetailActivity.this, strOperMsg1,
						Toast.LENGTH_LONG).show();
				button5.setText("发送");
				button5.setEnabled(true);
				button5.setTextColor(Color.rgb(255, 255, 255));
				// edittext1.setHint("评论");
				edittext1.setText("");
				// 评论了别人的任务后会将该任务显示在"我的"中
				RenPinMainActivity.UpdateDynamicData(strTaskId, 2);
				break;
			case 5:
				Intent intent = new Intent(ShareDynamicDetailActivity.this,
						TaskFinishOrVertifiImage.class);
				intent.putExtra("type", 2);
				intent.putExtra("icontype", 1);// 图片类型,1表示分享给接收,2表示接收给分享
				startActivity(intent);
				break;
			case 6:
				Intent intent1 = new Intent(ShareDynamicDetailActivity.this,
						TaskFinishOrVertifiImage.class);
				intent1.putExtra("type", 2);
				intent1.putExtra("icontype", 2);
				startActivity(intent1);
				break;
			case 7:
				Bundle b3 = msg.getData();
				String strCommentImage = b3.getString("strLargeImage");
				//int nCommentIndex = b3.getInt("commentindex");
				// CommentMaps.put("" + nCommentIndex, strCommentImage);
				// 显示图片
				Intent intent3 = new Intent(ShareDynamicDetailActivity.this,
						CommentActivity.class);
				//intent3.putExtra("nCommentIndex", nCommentIndex);
				//intent3.putExtra("nType", 2);
				intent3.putExtra("strLargeImage", strCommentImage);

				startActivity(intent3);
				break;
			case 8:

				// 取消等待框
				if (m_ProgressDialog != null) {
					m_ProgressDialog.dismiss();
					m_ProgressDialog = null;
				}

				Bundle b4 = msg.getData();
				int nCommentRet = b4.getInt("nRet");
				if (0 == nCommentRet) {
					CommonUtils.ShowToastCenter(getBaseContext(), "发送失败",
							Toast.LENGTH_LONG);
				} else {
					CommonUtils.ShowToastCenter(getBaseContext(), "发送成功",
							Toast.LENGTH_LONG);
				}
				// 评论了别人的任务后会将该任务显示在"我的"中
				RenPinMainActivity.UpdateDynamicData(strTaskId, 2);
				break;
			case 9:
				Bundle b5 = msg.getData();
				String strResetTaskMsg = b5.getString("OperMsg");
				CommonUtils.ShowToastCenter(getBaseContext(), strResetTaskMsg,
						Toast.LENGTH_LONG);
				break;
			case 10:
				if (mNewCommentInfos != null) {
					// 动态生成评论的界面
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
					// 如果没有图片,那么隐藏控件
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
					strMsg1 = "操作失败";
				} else if (1 == nRet) {
					strMsg1 = "操作成功";
					int nSumCharmValue = mnCharmValue + 1;
					charmvaluetextview.setText(nSumCharmValue + "");
					nSumCharmValue = nTaskCharmValue + 1;
					zantextview.setText(nSumCharmValue + "");
					
					Resources res = getResources();
					Bitmap zanbmp = BitmapFactory.decodeResource(res, R.drawable.zan1);
					zanimageview.setImageBitmap(zanbmp);
				} else if (2 == nRet) {
					strMsg1 = "已经赞过";
				} else if (3 == nRet) {
					strMsg1 = "不能给自己赞哦";
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
								2, 1, strText);
					} else if (2 == nType) {
						textview6.setText(strText);
						RenPinMainActivity.UpdateAccountPersonInfo(strTaskId,
								2, 2, strText);
					}
					CommonUtils.ShowToastCenter(getBaseContext(), "修改成功",
							Toast.LENGTH_LONG);
				} else {
					CommonUtils.ShowToastCenter(getBaseContext(), "修改失败",
							Toast.LENGTH_LONG);
				}
				break;
			case 15:
				// 将最新评论数据合并到评论数据中
				CombineCommentData();
				if (mCommentInfos != null) {
					// 动态生成评论的界面
					AutoCreateCommentWidows(mCommentInfos);
					talktextview.setText(mCommentInfos.size() + "");
				} else {
					if (!isConnectInternet()) {
						Toast.makeText(ShareDynamicDetailActivity.this,
								"网络不给力哦!", Toast.LENGTH_LONG).show();
					}
				}
				break;
			}
		}
	}

	private void AutoCreateCommentWidows(List<CommentInfo> comment) {

		if (null == comment) {
			return;
		}
		int nCount = comment.size();
		// 先将linearlayout1清空
		// linearlayout1.removeAllViews();

		if (nCount <= 0) {
			textview9.setText("暂无评论");
		} else {
			// 设置评论条数
			textview9.setText("" + comment.size());

			for (int i = 0; i < nCount; i++) {

				final CommentInfo commentinfo = comment.get(i);

				View cfg_view = getLayoutInflater().inflate(
						R.layout.comment_item, null);

				// 图标
				ImageView imageview = (ImageView) cfg_view
						.findViewById(R.id.comment_item_imageview1);
				// 评论人名称
				TextView AccountPerosn = (TextView) cfg_view
						.findViewById(R.id.comment_item_textview1);
				// 接收评论人名称
				TextView ReceivePerson = (TextView) cfg_view
						.findViewById(R.id.comment_item_textview3);
				// 评论人真名
				TextView AccountPerosnTrueName = (TextView) cfg_view
						.findViewById(R.id.comment_item_textviewtruename);
				// 评论时间
				TextView CommentTime = (TextView) cfg_view
						.findViewById(R.id.comment_item_textview4);

				// 评论内容
				TextView Contenttext = (TextView) cfg_view
						.findViewById(R.id.comment_item_textview5);

				TextView textview10 = (TextView) cfg_view
						.findViewById(R.id.comment_item_textview2);
				// 评论图片
				ImageView largeimageview = (ImageView) cfg_view
						.findViewById(R.id.comment_item_imageview2);

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
					// 如果评论为空,说明这句评论是悄悄话
					if (commentinfo.getstrCommentContent().equals("")) {
						Contenttext.setTextColor(Color.rgb(250, 60, 70));
						Contenttext.setText("*悄悄话");
					} else {
						Contenttext.setVisibility(View.VISIBLE);
						String zhengze = "f0[0-9]{2}|f10[0-7]";
						// 将内容转换成图标形式
						SpannableString spancontent = ExpressionUtil
								.getExpressionString(
										ShareDynamicDetailActivity.this,
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
							String strCommentImage = "";
							strCommentImage = goodService
									.GetCommentsLargeImage(strTaskId, "2",
											commentinfo.getnCommentIndex());
							if (!strCommentImage.equals("")) {
								// 将图片保存到本地数据库中
								mDatabase.SetCommentLargeImage(
										Integer.parseInt(strTaskId), 2,
										commentinfo.getnCommentIndex(),
										strCommentImage);
							}
							if (!strCommentImage.equals("")) {
								//CommentMaps.put(commentinfo.getnCommentIndex()
								//		+ "", strCommentImage);
								Message msg = myhandler.obtainMessage();
								Bundle b = new Bundle();
								b.putString("strLargeImage", strCommentImage);
								//b.putInt("commentindex",
								//		commentinfo.getnCommentIndex());
								msg.setData(b);
								msg.what = 7;
								myhandler.sendMessage(msg);
							}

						}
					};

					@Override
					public void onClick(View v) {
						int nIndex = commentinfo.getnCommentIndex();
						// 先判断评论大图片数据是否下载下来了
						String strImage = null;
						// 先到本地数据库中找
						strImage = mDatabase.GetCommentLargeImage(
								Integer.parseInt(strTaskId), 2, nIndex);
						// 如果之前没有下载该图片,那么就下载
						if (strImage.equals("")) {
							// 启动获取评论数据线程
							Thread thread = new Thread(runcomment);
							thread.start();
						} else {// 直接显示图片
							//CommentMaps.put(nIndex + "", strImage);

							Intent intent = new Intent(
									ShareDynamicDetailActivity.this,
									CommentActivity.class);
							//intent.putExtra("nCommentIndex",
							//		commentinfo.getnCommentIndex());
							//intent.putExtra("nType", 2);
							intent.putExtra("strLargeImage", strImage);
							
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
							edittext1.setHint("点击名字回复对方");
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

		if (networkInfo != null) { // 这个判断一定要加上，要不然会出错
			return networkInfo.isAvailable();
		}
		return false;
	}

	private List<HashMap<String, Object>> getListData(int nCount) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = null;

		for (int i = 0; i < nCount; i++) {
			// 评论人的图片
			String strCommentPersonImage;
			// 评论人的名称
			String strCommentPersonName;
			// 被评论人的名称
			String strCommentReceivePersonName;
			// 评论时间
			String strCommentTime;
			// 评论内容
			String strCommentContent;

			strCommentPersonImage = mCommentInfos.get(i)
					.getstrCommentPersonImage();
			strCommentPersonName = mCommentInfos.get(i)
					.getstrCommentPersonName();
			strCommentReceivePersonName = mCommentInfos.get(i)
					.getstrCommentReceivePersonName();
			strCommentTime = mCommentInfos.get(i).getstrCommentTime();
			strCommentContent = mCommentInfos.get(i).getstrCommentContent();

			// 将获取到的数据存储到容器中
			map = new HashMap<String, Object>();
			map.put("strCommentPersonImage", strCommentPersonImage);
			map.put("strCommentPersonName", strCommentPersonName);
			map.put("strCommentReceivePersonName", strCommentReceivePersonName);
			map.put("strCommentTime", strCommentTime);
			map.put("strCommentContent", strCommentContent);
			list.add(map);
		}
		return list;
	}

	class CommentsListAdpater extends SimpleAdapter {

		private LayoutInflater mInflater;
		Context context;
		int count = 0;
		private List<HashMap<String, Object>> mItemList;

		@SuppressWarnings("unchecked")
		public CommentsListAdpater(Context context,
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
				convertView = mInflater.inflate(R.layout.comment_item, null);
			}
			// 图标
			ImageView imageview = (ImageView) convertView
					.findViewById(R.id.comment_item_imageview1);
			// 评论人名称
			TextView AccountPerosn = (TextView) convertView
					.findViewById(R.id.comment_item_textview1);
			// 接收评论人名称
			TextView ReceivePerson = (TextView) convertView
					.findViewById(R.id.comment_item_textview3);
			// 评论时间
			TextView CommentTime = (TextView) convertView
					.findViewById(R.id.comment_item_textview4);

			// 评论内容
			TextView Contenttext = (TextView) convertView
					.findViewById(R.id.comment_item_textview5);

			TextView textview10 = (TextView) convertView
					.findViewById(R.id.comment_item_textview2);

			Bitmap image = mUtils.base64ToBitmap(map.get(
					"strCommentPersonImage").toString());
			imageview.setImageBitmap(image);
			AccountPerosn.setText(map.get("strCommentPersonName").toString());
			// 如果没有接收人则不显示谁回复谁
			if (map.get("strCommentReceivePersonName").toString().equals("")) {
				textview10.setText("");
			} else {
				textview10.setText("回复");
			}
			ReceivePerson.setText(map.get("strCommentReceivePersonName")
					.toString());
			CommentTime.setText(map.get("strCommentTime").toString());
			Contenttext.setText(map.get("strCommentContent").toString());
			return convertView;
		}

	}

	// 开始向服务器发送添加数据的请求
	Runnable run1 = new Runnable() {
		public void run() {
			while (mbIsContinue) {
				int nHotType = 0;

				String strAccountNameTemp = msettings.getString(
						"TruePersonName", "");
				// 判读是否有最新的评论
				if (IsShowHotDot(strTaskId, strAccountNameTemp)) {
					nHotType = 1;
				}

				mnRemainTime = goodService.GetTaskRemainTime(strTaskId, "2");

				Message msg = myhandler.obtainMessage();
				Bundle b = new Bundle();// 存放数据
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
			String strTime = "";
			if (bIsTalkToPerson) {
				strTime = goodService.SendCommentImageNewTime(strTaskId,
						strCurrentName, strReceiveName, strCommentSmallImage,
						strCommentLargeImage, "2", strCurrentNickName,
						strReceiveNickName);
				strOtherPersonName = strReceiveName;
				strOtherPersonNickName = strReceiveNickName;
			} else {

				if (!strCurrentName.equals(strAccountName1)) {
					strTime = goodService.SendCommentImageNewTime(strTaskId,
							strCurrentName, strAccountName1,
							strCommentSmallImage, strCommentLargeImage, "2",
							strCurrentNickName, strCustomerNameNick);
					strOtherPersonName = strAccountName1;
					strOtherPersonNickName = strCustomerNameNick;
				} else {
					strTime = goodService.SendCommentImageNewTime(strTaskId,
							strCurrentName, "", strCommentSmallImage,
							strCommentLargeImage, "2", strCurrentNickName, "");
				}
			}
			// 如果没有失败就刷新评论内容
			if (!strTime.equals("")) {

				// CreateDatabaseAndStoreData();
				// 将新评论内容保存到本地数据库中
				/*
				 * mDatabase.InsertCommentData(Integer.parseInt(strTaskId), 2,
				 * mCommentInfos.size() + 1, strCurrentImage,
				 * strCurrentNickName, strOtherPersonNickName, strTime, "",
				 * strCurrentName, strOtherPersonName, strCommentSmallImage, 0);
				 */

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
							ShareDynamicDetailActivity.this, "提示",
							"正在发送图片,请等待...", true);
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
				if (null == bitmap) {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"不能发送该类型的图片", Toast.LENGTH_LONG);
				} else {
					strCommentSmallImage = mUtils.BitmapToBase64BySize(mUtils
							.zoomBitmap(bitmap, MyApplication.nImageWidth,
									MyApplication.nImageHeight));
					strCommentLargeImage = mUtils.BitmapToBase64BySize(mUtils
							.zoomBitmap(bitmap, MyApplication.nLargeImageWidth,
									MyApplication.nLargeImageHeight));

					m_ProgressDialog = ProgressDialog.show(
							ShareDynamicDetailActivity.this, "提示",
							"正在发送图片,请等待...", true);
					m_ProgressDialog.setCancelable(true);
					// 启动线程,将评论图片发送出去
					Thread comment1 = new Thread(commentrun);
					comment1.start();
				}
			} catch (OutOfMemoryError e) {
				CommonUtils.ShowToastCenter(this, "内存不足!", Toast.LENGTH_LONG);
			}
		} else if (requestCode == 2) {
			if (data != null) {
				int nRet = data.getExtras().getInt("nRet");
				// 如果操作成功
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
			builder.setTitle("发送图片");
			builder.setItems(addPhoto, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == 0) {
						String haveSD = Environment.getExternalStorageState();
						if (!haveSD.equals(Environment.MEDIA_MOUNTED)) {
							Toast.makeText(ShareDynamicDetailActivity.this,
									"存储卡不可用", Toast.LENGTH_LONG).show();
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
						ShareDynamicDetailActivity.this.startActivityForResult(
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

	@Override
	public void onBackPressed() {
		// 先判断表情窗口是否显示
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
				data.putExtra("strTaskType", "2");
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
		// builder = new Dialog(this.getContext());
		gridView = (GridView) findViewById(R.id.mytask_gridview);

		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		// 生成107个表情的id，封装
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
				ShareDynamicDetailActivity.this, listItems,
				R.layout.team_layout_single_expression_cell,
				new String[] { "image" }, new int[] { R.id.image });
		gridView.setAdapter(simpleAdapter);
		// gridView.setNumColumns(6);
		// gridView.setBackgroundColor(Color.rgb(214, 211, 214));
		// gridView.setHorizontalSpacing(1);
		// gridView.setVerticalSpacing(1);
		// gridView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT));
		gridView.setGravity(Gravity.CENTER);
		// builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// builder.setContentView(gridView);
		// builder.show();
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Bitmap bitmap = null;
				bitmap = BitmapFactory.decodeResource(getResources(),
						imageIds[arg2 % imageIds.length]);
				ImageSpan imageSpan = new ImageSpan(
						ShareDynamicDetailActivity.this, bitmap);
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
				// builder.dismiss();
			}
		});
	}

	// 处理当数据库文件被删除了
	private void CreateDatabaseAndStoreData() {
		// 当前用户图标
		String strCurrentImage = msettings.getString("Base64Image", "");
		if (!mDatabase.IsTableExist(Integer.parseInt(strTaskId), 2)) {
			mDatabase.CloseDatabase();
			mDatabase = null;
			mDatabase = new OperaDatabase(ShareDynamicDetailActivity.this);
			// 此时说明数据库文件被删除了,重新生成数据库后,将原来的数据重新存储到数据库中
			if (mCommentInfos != null) {
				int nSize = mCommentInfos.size();

				for (int i = 0; i < nSize; i++) {
					// 判断是否是悄悄话
					int nSecretType = 0;

					String strContent1 = mCommentInfos.get(i)
							.getstrCommentContent();
					if (!strContent1.equals("")) {
						// 去除了前面空格的字符串
						String strClearSpace = CommonUtils
								.ClearFrontSpace(strContent1);
						char cFirstChar = strClearSpace.charAt(0);
						if ('*' == cFirstChar) {
							nSecretType = 1;
						}
					}

					mDatabase.InsertCommentData(Integer.parseInt(strTaskId), 2,
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

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
