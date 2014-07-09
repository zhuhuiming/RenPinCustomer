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
	// ����ͷ��Ŀؼ�
	RelativeLayout presonimagerelativelayout;
	// ͷ��ؼ�
	ImageView personimageview;
	// �ǳƿؼ�
	TextView nicktextview;
	// �˺ſؼ�
	TextView accounttextview;
	// �Ա�
	TextView sextextview;
	// �˳���¼��ť
	Button exitbutton;
	// ������ѡͼƬ��·��
	private String mImgPaths = "";
	private static CommonUtils mUtils = null;
	MyHandler myhandler;
	// ����������
	public static ProgressDialog m_ProgressDialog = null;

	public static final String[] addPhoto = new String[] { "��������", "�����ѡ��",
			"ȡ��" };
	// ��ͷ��СͼƬ��base64����
	private String strSmallPersonImage;
	// ��ͷ���ͼƬ��base64����
	private String strLargePersonImage;
	// �û�ͷ��ͼƬ�Ŀ���(Сͼ)
	private static final int nImageWidth = 200;
	// �û�ͷ��ͼƬ�ĸ߶�(Сͼ)
	private static final int nImageHeight = 200;

	// �û�ͷ��ͼƬ�Ŀ���(��ͼ)
	private static final int nLargeImageWidth = 500;
	// �û�ͷ��ͼƬ�ĸ߶�(��ͼ)
	private static final int nLargeImageHeight = 500;

	private GoodService goodService = new GoodServiceImpl();
	// �����洢�û���Ϣ
	SharedPreferences msettings = null;
	// ���ذ�ť
	ImageView returnimageview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����������ʱ������괦��editview�ж��������뷨����
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
		// ���������д�˷���,��������
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
				// ��base64���͵�ͼ�걣������
				editor.putString("Base64Image", strSmallPersonImage);
				editor.commit();

				String strName = msettings.getString("TruePersonName", "");
				// ���ҵġ���������������Ӧ��ͼ����¹���
				RenPinMainActivity.UpdateAccountPersonImage(strName,
						strSmallPersonImage);
				Toast.makeText(getApplicationContext(), "�޸�ͷ��ɹ�",
						Toast.LENGTH_LONG).show();
			} else if (2 == msg.what) {
				Toast.makeText(getApplicationContext(), "û���ҵ����û�,�޸�ʧ��",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "�޸�ʧ��",
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
		// �޸�ͼƬ
		presonimagerelativelayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(0);
			}
		});
		// �Ŵ�ͼƬ
		personimageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}

		});
		// �˳���¼
		exitbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ����̨����ص�
				PollingUtils.stopPollingService(getApplicationContext(),
						UpdateDataService.class, UpdateDataService.ACTION);
				// �ص��ٶȶ�λ����
				if (MyLocation.mLocationClient != null) {
					MyLocation.mLocationClient.stop();
					MyLocation.mLocationClient = null;
				}
				SharedPreferences.Editor editor = msettings.edit();
				editor.putString("TruePersonName", "");
				editor.putString("PersonName", "");
				// ��base64���͵�ͼ�걣������
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

	// ��ʼ���������������ͼƬ
	Runnable commentrun = new Runnable() {
		public void run() {
			String strCurrentPersonName = msettings.getString("TruePersonName",
					"");
			// ��ʼ�޸�ͷ��
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
								MyInfoActivity.this, "��ʾ", "�����ϴ�ͼƬ,��ȴ�...",
								true);
						m_ProgressDialog.setCancelable(true);
						// �����߳�,��ʼ��ͼƬ�ϴ�����������
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
				// ��ͼƬ������ת����
				bitmap = mUtils.PhotoRotation(uri);

				strSmallPersonImage = mUtils.BitmapToBase64BySize(mUtils
						.zoomBitmap(bitmap, nImageWidth, nImageHeight));
				strLargePersonImage = mUtils
						.BitmapToBase64BySize(mUtils.zoomBitmap(bitmap,
								nLargeImageWidth, nLargeImageHeight));

				m_ProgressDialog = ProgressDialog.show(MyInfoActivity.this,
						"��ʾ", "�����ϴ�ͼƬ,��ȴ�...", true);
				m_ProgressDialog.setCancelable(true);
				// �����߳�,��ʼ��ͼƬ�ϴ�����������
				Thread thre = new Thread(commentrun);
				thre.start();
				// bitmap = mUtils.zoomBitmap(bitmap, 70, 70);
				/* ��Bitmap�趨��ImageView */
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
			builder.setTitle("�޸��û�ͷ��");
			builder.setItems(addPhoto, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == 0) {
						String haveSD = Environment.getExternalStorageState();
						if (!haveSD.equals(Environment.MEDIA_MOUNTED)) {
							CommonUtils.ShowToastCenter(MyInfoActivity.this,
									"�洢��������", Toast.LENGTH_LONG);
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