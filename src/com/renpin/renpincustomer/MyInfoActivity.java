package com.renpin.renpincustomer;

import java.io.File;
import java.io.FileNotFoundException;

import com.renpin.location.MyLocation;
import com.renpin.myservice.UpdateDataService;
import com.renpin.service.GoodService;
import com.renpin.service.Impl.GoodServiceImpl;
import com.renpin.utils.CommonUtils;
import com.renpin.utils.PollingUtils;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyInfoActivity extends Activity {
	// 更换头像的控件
	RelativeLayout presonimagerelativelayout;
	// 头像控件
	ImageView personimageview;
	// 昵称控件
	TextView nicktextview;
	// 账号控件
	TextView accounttextview;
	// 性别
	TextView sextextview;
	// 退出登录按钮
	Button exitbutton;
	// 保存所选图片的路径
	private String mImgPaths = "";
	private static CommonUtils mUtils = null;
	MyHandler myhandler;
	// 进度条对象
	public static ProgressDialog m_ProgressDialog = null;

	public static final String[] addPhoto = new String[] { "现在拍摄", "从相册选择",
			"取消" };
	// 人头像小图片的base64类型
	private String strSmallPersonImage;
	// 人头像大图片的base64类型
	private String strLargePersonImage;
	// 用户头像图片的宽度(小图)
	private static final int nImageWidth = 200;
	// 用户头像图片的高度(小图)
	private static final int nImageHeight = 200;

	// 用户头像图片的宽度(大图)
	private static final int nLargeImageWidth = 500;
	// 用户头像图片的高度(大图)
	private static final int nLargeImageHeight = 500;

	private GoodService goodService = new GoodServiceImpl();
	// 用来存储用户信息
	SharedPreferences msettings = null;
	// 返回按钮
	ImageView returnimageview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 程序启动的时候避免光标处在editview中而弹出输入法窗口
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setContentView(R.layout.my_info);

		msettings = getSharedPreferences("MekeSharedPreferences", 0);

		mImgPaths = "";
		mUtils = new CommonUtils(this);
		InitActivities();
		myhandler = new MyHandler();
	}

	class MyHandler extends Handler {
		// 子类必须重写此方法,接受数据
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (m_ProgressDialog != null) {
				m_ProgressDialog.dismiss();
				m_ProgressDialog = null;
			}
			if (1 == msg.what) {

				personimageview.setImageBitmap(mUtils
						.base64ToBitmap(strSmallPersonImage));

				SharedPreferences.Editor editor = msettings.edit();
				// 将base64类型的图标保存起来
				editor.putString("Base64Image", strSmallPersonImage);
				editor.commit();

				String strName = msettings.getString("TruePersonName", "");
				// 将我的、求助、分享中相应的图标更新过来
				RenPinMainActivity.UpdateAccountPersonImage(strName,
						strSmallPersonImage);
				Toast.makeText(getApplicationContext(), "修改头像成功",
						Toast.LENGTH_LONG).show();
			} else if (2 == msg.what) {
				Toast.makeText(getApplicationContext(), "没有找到该用户,修改失败",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "修改失败",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public void InitActivities() {
		presonimagerelativelayout = (RelativeLayout) findViewById(R.id.myinfo_customericonlayout);
		personimageview = (ImageView) findViewById(R.id.myinfo_customericonimageview);
		nicktextview = (TextView) findViewById(R.id.myinfo_nicktextview);
		accounttextview = (TextView) findViewById(R.id.myinfo_accounttextview);
		sextextview = (TextView) findViewById(R.id.myinfo_sextextview);
		exitbutton = (Button) findViewById(R.id.myinfo_exitbutton);
		returnimageview = (ImageView) findViewById(R.id.myinfo_returnimageview);

		String strTrueName = msettings.getString("TruePersonName", "");
		String strNickName = msettings.getString("PersonName", "");
		String strImage = msettings.getString("Base64Image", "");
		String strSex = msettings.getString("sex", "");

		personimageview.setImageBitmap(mUtils.base64ToBitmap(strImage));
		nicktextview.setText(strNickName);
		accounttextview.setText(strTrueName);
		sextextview.setText(strSex);
		// 修改图片
		presonimagerelativelayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(0);
			}
		});
		// 放大图片
		personimageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}

		});
		// 退出登录
		exitbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 将后台服务关掉
				PollingUtils.stopPollingService(getApplicationContext(),
						UpdateDataService.class, UpdateDataService.ACTION);
				// 关掉百度定位服务
				if (MyLocation.mLocationClient != null) {
					MyLocation.mLocationClient.stop();
					MyLocation.mLocationClient = null;
				}
				SharedPreferences.Editor editor = msettings.edit();
				editor.putString("TruePersonName", "");
				editor.putString("PersonName", "");
				// 将base64类型的图标保存起来
				editor.putString("Base64Image", "");
				editor.putInt("CreditValue", 0);
				editor.putInt("CharmValue", 0);
				editor.commit();

				Intent it = new Intent();
				setResult(0, it);
				finish();
			}
		});

		returnimageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent();
				setResult(1, it);
				finish();
			}
		});
	}

	// 开始向服务器发送评论图片
	Runnable commentrun = new Runnable() {
		public void run() {
			String strCurrentPersonName = msettings.getString("TruePersonName",
					"");
			// 开始修改头像
			int nRetType = goodService.ChangePersonImage(strCurrentPersonName,
					strSmallPersonImage, strLargePersonImage);

			Message msg = myhandler.obtainMessage();
			msg.what = nRetType;
			myhandler.sendMessage(msg);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Bitmap bitmap = null;

		// if (resultCode == RESULT_OK)
		{
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
												imgFile.getAbsolutePath(),
												null, null));
						String[] proj = { MediaStore.Images.Media.DATA };
						@SuppressWarnings("deprecation")
						Cursor cursor = managedQuery(imgUri, proj, null, null,
								null);
						int column_index = cursor
								.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						String path = cursor.getString(column_index);
						mImgPaths = path;

						ContentResolver cr = this.getContentResolver();
						bitmap = BitmapFactory.decodeStream(cr
								.openInputStream(imgUri));

						strSmallPersonImage = mUtils
								.BitmapToBase64BySize(mUtils.zoomBitmap(bitmap,
										nImageWidth, nImageHeight));
						strLargePersonImage = mUtils
								.BitmapToBase64BySize(mUtils.zoomBitmap(bitmap,
										nLargeImageWidth, nLargeImageHeight));

						m_ProgressDialog = ProgressDialog.show(
								MyInfoActivity.this, "提示", "正在上传图片,请等待...",
								true);
						m_ProgressDialog.setCancelable(true);
						// 启动线程,开始将图片上传到服务器上
						Thread thre = new Thread(commentrun);
						thre.start();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				} else {
				}

			} else if (requestCode == 1) {
				if (data == null) {
					return;
				}
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

				strSmallPersonImage = mUtils.BitmapToBase64BySize(mUtils
						.zoomBitmap(bitmap, nImageWidth, nImageHeight));
				strLargePersonImage = mUtils
						.BitmapToBase64BySize(mUtils.zoomBitmap(bitmap,
								nLargeImageWidth, nLargeImageHeight));

				m_ProgressDialog = ProgressDialog.show(MyInfoActivity.this,
						"提示", "正在上传图片,请等待...", true);
				m_ProgressDialog.setCancelable(true);
				// 启动线程,开始将图片上传到服务器上
				Thread thre = new Thread(commentrun);
				thre.start();
				// bitmap = mUtils.zoomBitmap(bitmap, 70, 70);
				/* 将Bitmap设定到ImageView */
				// personimageview.setImageBitmap(bitmap);
			}
		}
	}

	protected Dialog onCreateDialog(int id) {
		AlertDialog dialog = null;
		AlertDialog.Builder builder = null;
		switch (id) {
		case 0:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("修改用户头像");
			builder.setItems(addPhoto, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == 0) {
						String haveSD = Environment.getExternalStorageState();
						if (!haveSD.equals(Environment.MEDIA_MOUNTED)) {
							CommonUtils.ShowToastCenter(MyInfoActivity.this,
									"存储卡不可用", Toast.LENGTH_LONG);
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
						MyInfoActivity.this.startActivityForResult(intent, 1);
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

	@Override
	public void onBackPressed() {
		if (m_ProgressDialog != null) {
			m_ProgressDialog.dismiss();
			m_ProgressDialog = null;
		}

		Intent it = new Intent();
		setResult(1, it);

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
