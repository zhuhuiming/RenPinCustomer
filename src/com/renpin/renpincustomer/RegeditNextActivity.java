package com.renpin.renpincustomer;

import java.io.File;
import java.io.FileNotFoundException;

import com.renpin.service.GoodService;
import com.renpin.service.Impl.GoodServiceImpl;
import com.renpin.utils.CommonUtils;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class RegeditNextActivity extends Activity {
	// 用来存储用户信息
	SharedPreferences msettings = null;
	// 用来输入用户昵称的编辑框
	EditText nickedittext;
	// 用户头像控件
	ImageView imageview;
	// 性别单选框
	RadioButton malebutton;
	RadioButton femalebutton;
	// 完成按钮
	Button finishbutton;
	// 用户性别
	String strCustomerSex;

	public static final String[] addPhoto = new String[] { "现在拍摄", "从相册选择",
			"取消" };
	private static CommonUtils mUtils = null;
	// 保存所选图片的路径
	private String mImgPaths = "";
	// 进度条对象
	public static ProgressDialog m_ProgressDialog = null;

	// 用户头像图片的宽度(小图)
	private static final int nImageWidth = 200;
	// 用户头像图片的高度(小图)
	private static final int nImageHeight = 200;

	// 用户头像图片的宽度(大图)
	private static final int nLargeImageWidth = 500;
	// 用户头像图片的高度(大图)
	private static final int nLargeImageHeight = 500;

	private GoodService goodService = new GoodServiceImpl();
	MyHandler myhandler;
	// 将图片数据转换成base64型(小图片)
	String strImageBase64;
	// base64格式的头像(大图片)
	String strLargeImageBase64;
	// 返回按钮
	RelativeLayout returnlayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 程序启动的时候避免光标处在editview中而弹出输入法窗口
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.regedit_name);
		msettings = getSharedPreferences("MekeSharedPreferences", 0);
		strCustomerSex = "男";

		mImgPaths = "";
		mUtils = new CommonUtils(this);
		myhandler = new MyHandler();

		InitActivity();
	}

	private void InitActivity() {
		nickedittext = (EditText) findViewById(R.id.regeditname_nick);
		imageview = (ImageView) findViewById(R.id.regeditname_personpicture);
		malebutton = (RadioButton) findViewById(R.id.regeditname_maleradiobutton);
		femalebutton = (RadioButton) findViewById(R.id.regeditname_femaleradiobutton);
		finishbutton = (Button) findViewById(R.id.regeditname_finishbutton);
		returnlayout = (RelativeLayout) findViewById(R.id.regeditname_returnrelativelayout);

		malebutton.setChecked(true);
		femalebutton.setChecked(false);

		malebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				strCustomerSex = "男";
				femalebutton.setChecked(false);
			}

		});

		femalebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				strCustomerSex = "女";
				malebutton.setChecked(false);
			}

		});

		imageview.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(0);
			}

		});

		finishbutton.setOnClickListener(new OnClickListener() {

			Runnable run1 = new Runnable() {
				public void run() {
					// 开始注册用户
					int nRetType = goodService.RegisterCustomerDetail(
							strImageBase64,
							StartRegeditActivity.strCustomerName,
							StartRegeditActivity.strCustomerPassword, "1", "0",
							nickedittext.getText().toString(), strCustomerSex,
							strLargeImageBase64);

					Message msg = new Message();
					msg.what = nRetType;
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {
				// 如果昵称为空,那么就返回
				if (nickedittext.getText().toString().equals("")) {

					CommonUtils.ShowToastCenter(getApplicationContext(),
							"昵称不能为空", Toast.LENGTH_LONG);
				}
				m_ProgressDialog = ProgressDialog.show(
						RegeditNextActivity.this, "提示", "正在注册,请等待...", true);
				m_ProgressDialog.setCancelable(true);

				if (mImgPaths.equals("")) {
					/*
					 * Resources res = getResources(); Bitmap bmp =
					 * BitmapFactory.decodeResource(res, R.drawable.noperson);
					 * Bitmap largebmp = mUtils.zoomBitmap(bmp,
					 * nLargeImageWidth, nLargeImageHeight); strLargeImageBase64
					 * = mUtils.BitmapToBase64(largebmp);
					 * 
					 * Bitmap smallbmp = mUtils.zoomBitmap(bmp, nImageWidth,
					 * nImageHeight); strImageBase64 =
					 * mUtils.BitmapToBase64(smallbmp);
					 */
					strImageBase64 = "";
					strLargeImageBase64 = "";
				} else {
					strImageBase64 = mUtils.imgToBase64(mImgPaths, nImageWidth,
							nImageHeight);

					strLargeImageBase64 = mUtils.imgToBase64(mImgPaths,
							nLargeImageWidth, nLargeImageHeight);
				}

				Thread thre = new Thread(run1);
				thre.start();
			}
		});

		returnlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});
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
			if (0 == msg.what) {
				CommonUtils.ShowToastCenter(getApplicationContext(), "注册成功",
						Toast.LENGTH_LONG);
				SharedPreferences.Editor editor = msettings.edit();
				editor.putString("TruePersonName",
						StartRegeditActivity.strCustomerName);
				editor.putString("PersonName", nickedittext.getText()
						.toString());
				// 将base64类型的图标保存起来
				editor.putString("Base64Image", strImageBase64);
				editor.putInt("CreditValue", 1);
				editor.putInt("CharmValue", 0);
				editor.putString("sex", strCustomerSex);
				editor.commit();
				Intent it = new Intent(RegeditNextActivity.this,
						RenPinMainActivity.class);
				startActivity(it);
				finish();
			} else if (2 == msg.what) {
				CommonUtils.ShowToastCenter(getApplicationContext(),
						"已经存在该账号，请选择其他账号", Toast.LENGTH_LONG);
			} else {
				CommonUtils.ShowToastCenter(getApplicationContext(), "注册失败",
						Toast.LENGTH_LONG);
			}
		}
	}

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
						bitmap = mUtils.zoomBitmap(bitmap, nImageWidth,
								nImageHeight);
						imageview.setImageBitmap(bitmap);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				} else {
					Resources res = getResources();
					Bitmap bmp = BitmapFactory.decodeResource(res,
							R.drawable.noperson);
					imageview.setImageBitmap(bmp);
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
				bitmap = mUtils.zoomBitmap(bitmap, nImageWidth, nImageHeight);
				/* 将Bitmap设定到ImageView */
				imageview.setImageBitmap(bitmap);
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
							CommonUtils.ShowToastCenter(
									RegeditNextActivity.this, "存储卡不可用",
									Toast.LENGTH_LONG);
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
						RegeditNextActivity.this.startActivityForResult(intent,
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

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
