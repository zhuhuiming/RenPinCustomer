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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
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
import android.widget.Toast;

public class TaskImplementFinishOperaActivity extends Activity {
	MyHandler myhandler;
	private GoodService goodService = new GoodServiceImpl();
	public static final String[] addPhoto = new String[] { "现在拍摄", "从相册选择",
			"取消" };
	private static CommonUtils mUtils = null;
	// 保存所选图片的路径
	private String mImgPaths = "";
	// 存储任务执行者给发布者的评论
	private EditText edittext1;
	// 提交按钮
	private Button button1;
	// 提交图片按钮
	private ImageView imageview1;
	// 任务发布者名称
	private String strCustomerName;
	// 任务id号
	private String strTaskId;
	// 任务类型
	private int mnTaskType;
	// 用来存储用户信息
	SharedPreferences msettings = null;
	// 进度条对象
	public static ProgressDialog m_ProgressDialog = null;
	// 缩小图片的宽度
	private final int nImageWidth = 200;
	// 缩小图片的高度
	private final int nImageHeight = 180;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 程序启动的时候避免光标处在editview中而弹出输入法窗口
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.taskresult_send);
		mImgPaths = "";
		mUtils = new CommonUtils(this);
		InitActivities();
		myhandler = new MyHandler();

		msettings = getSharedPreferences("MekeSharedPreferences", 0);

		strCustomerName = (String) getIntent().getStringExtra(
				"com.renpin.DynamicDetailActivity.customername");
		strTaskId = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.TaskId");
		mnTaskType = Integer.parseInt(getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.TaskType"));
		if (2 == mnTaskType) {
			edittext1.setHint("我已经分享给你了,希望能帮到你!");
		}
	}

	class MyHandler extends Handler {
		// 子类必须重写此方法,接受数据
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (0 == msg.what) {

				if (m_ProgressDialog != null) {
					m_ProgressDialog.dismiss();
					m_ProgressDialog = null;
				}

				Bundle b = msg.getData();
				String strMsg = b.getString("Msg");
				int nRet = b.getInt("nRet");
				Toast.makeText(getApplicationContext(), strMsg,
						Toast.LENGTH_LONG).show();
				// 判断空，我就不判断了。。。。
				Intent data = new Intent();
				data.putExtra("nRet", nRet);
				// 请求代码可以自己设置
				setResult(0, data);
				finish();
			}
		}
	}

	private void InitActivities() {
		edittext1 = (EditText) findViewById(R.id.taskresult_send_edittext1);
		button1 = (Button) findViewById(R.id.taskresult_send_button1);
		imageview1 = (ImageView) findViewById(R.id.taskresult_send_imageview1);
		button1.setOnClickListener(new OnClickListener() {

			// 将图片数据转换成base64型
			String strImageBase64;
			// 放大图的base64
			String strLargeImageBase64;

			Runnable run1 = new Runnable() {
				public void run() {

					int nRet = goodService.UploadTaskFinishTypeForLargeImage(
							strTaskId, mnTaskType + "", strLargeImageBase64);

					nRet = goodService.UpdateTaskFinishType(mnTaskType,
							strCustomerName, 2, strTaskId, strImageBase64,
							edittext1.getText().toString());

					String strCurName = msettings.getString("TruePersonName",
							"");

					goodService.SetUpdateSignal(strCurName, 1);
					Message msg = new Message();
					Bundle b = new Bundle();// 存放数据
					String strMsg;
					if (1 == nRet) {
						strMsg = "操作成功";
					} else {
						strMsg = "操作失败";
					}
					b.putInt("nRet", nRet);
					b.putString("Msg", strMsg);
					msg.setData(b);
					msg.what = 0;
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {// //将任务完成状态修改,值改为2

				m_ProgressDialog = ProgressDialog.show(
						TaskImplementFinishOperaActivity.this, "提示",
						"操作中,请等待...", true);

				if (mImgPaths.equals("")) {
					strImageBase64 = "";
					strLargeImageBase64 = "";
				} else {
					strImageBase64 = mUtils.imgToBase64(mImgPaths, 200, 180);
					strLargeImageBase64 = mUtils.imgToBase64(mImgPaths, 1024,
							768);
				}
				Thread thread = new Thread(run1);
				thread.start();
			}

		});

		imageview1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(0);
			}

		});
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
						imageview1.setImageBitmap(bitmap);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
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
				//
				bitmap = ThumbnailUtils.extractThumbnail(bitmap, 70, 70);
				/* 将Bitmap设定到ImageView */
				imageview1.setImageBitmap(bitmap);
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
							Toast.makeText(
									TaskImplementFinishOperaActivity.this,
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
						TaskImplementFinishOperaActivity.this
								.startActivityForResult(intent, 1);
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
