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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TaskAnnounceVertifOperaActivity extends Activity {
	MyHandler myhandler;
	private GoodService goodService = new GoodServiceImpl();
	public static final String[] addPhoto = new String[] { "��������", "�����ѡ��",
			"ȡ��" };
	private static CommonUtils mUtils = null;
	// ������ѡͼƬ��·��
	private String mImgPaths = "";
	// �洢����ִ���߸������ߵ�����
	private EditText edittext1;
	// �ύ��ť
	private Button button1;
	// �ύͼƬ��ť
	private ImageView imageview1;
	// ����ִ�������ƻ���շ���������
	private String strImplementName;
	// ����id��
	private String strTaskId;
	// ��������
	private int mnTaskType;
	// �����洢�û���Ϣ
	SharedPreferences msettings = null;
	// �ް�ť
	private ImageView imageview2;
	// ��ǰ����޸���
	private int nCharmValue;
	// ����ܸ����ؼ�
	private TextView textview4;
	// ����������
	public static ProgressDialog m_ProgressDialog = null;
	// ��ͼ��ؼ�
	private LinearLayout linearlayout1;
	// ���ؿؼ�
	private ImageView returnimageview;
	// ��СͼƬ�Ŀ���
	private final int nImageWidth = 200;
	// ��СͼƬ�ĸ߶�
	private final int nImageHeight = 180;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����������ʱ������괦��editview�ж��������뷨����
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.task_confirm);
		mImgPaths = "";
		mUtils = new CommonUtils(this);
		nCharmValue = 0;
		InitActivities();
		myhandler = new MyHandler();

		msettings = getSharedPreferences("MekeSharedPreferences", 0);

		strImplementName = (String) getIntent().getStringExtra(
				"com.renpin.DynamicDetailActivity.strImplementName");
		strTaskId = (String) getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.TaskId");
		mnTaskType = Integer.parseInt(getIntent().getStringExtra(
				"com.renpin.RenPinMainActivity.TaskType"));
		if (2 == mnTaskType) {
			edittext1.setHint("��л�ţ�Ĭ��Ϊ������˽�����Һܴ�İ�����������Ʒ���Ҹж�!");
		}
	}

	class MyHandler extends Handler {
		// ���������д�˷���,��������
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

				Intent data = new Intent();
				data.putExtra("nRet", nRet);
				// �����������Լ�����
				setResult(0, data);
				finish();
			}
		}
	}

	private void InitActivities() {
		edittext1 = (EditText) findViewById(R.id.task_confirm_edittext1);
		button1 = (Button) findViewById(R.id.task_confirm_button1);
		imageview1 = (ImageView) findViewById(R.id.task_confirm_imageview1);
		imageview2 = (ImageView) findViewById(R.id.task_confirm_charmimageview1);
		textview4 = (TextView) findViewById(R.id.task_confirm_textview4);
		linearlayout1 = (LinearLayout) findViewById(R.id.task_confirm_linearlayout1);
		textview4.setText("+0");
		returnimageview = (ImageView) findViewById(R.id.customer_login_image1);

		button1.setOnClickListener(new OnClickListener() {
			// ��ͼƬ����ת����base64��,����ͼ
			String strImageBase64;
			// �Ŵ�ͼ��base64
			String strLargeImageBase64;

			Runnable run1 = new Runnable() {
				public void run() {
					// �Ȼ�ȡ��ǰ�û�����
					String strCurName = msettings.getString("TruePersonName",
							"");

					/** ��ԶԷ� **/
					// �Ȼ�ȡ�Է���ǰ���޸���
					int nValue = goodService.GetCharmValue(strImplementName)
							+ nCharmValue;
					// �����û��޸���
					int nRet = goodService.SetCharmValue(strImplementName,
							nValue);

					// ���¶Է���Ʒֵ(��һ)
					nRet = goodService.AddCreditValue(strImplementName, 1);
					// ����������ȡ����Ʒֵ����ֵ���浽���ݿ���
					nRet = goodService.RecordCreditAndCharmForTask(strTaskId,
							mnTaskType + "", 1, nCharmValue);

					/** ��Ա��� **/
					// ��ȡ������Ʒֵ(��һ)
					// int nCurCredit = msettings.getInt("CreditValue", -1);
					// ������Ʒֵ
					// if (nCurCredit > 0) {
					// nRet = goodService.SetCreditValue(strCurName,
					// nCurCredit - 1);
					// Editor editor = msettings.edit();
					// editor.putInt("CreditValue", nCurCredit - 1);
					// editor.commit();
					// }

					nRet = goodService.UploadTaskVerifiTypeForLargeImage(
							strTaskId, mnTaskType + "", strLargeImageBase64);

					nRet = goodService.UpdateTaskVerifiType(mnTaskType,
							strImplementName, 2, strTaskId, strImageBase64,
							edittext1.getText().toString());

					goodService.SetUpdateSignal(strCurName, 1);

					Message msg = new Message();
					Bundle b = new Bundle();// �������
					String strMsg;
					if (1 == nRet) {
						strMsg = "�����ɹ�";
					} else {
						strMsg = "����ʧ��";
					}
					b.putInt("nRet", nRet);
					b.putString("Msg", strMsg);
					msg.setData(b);
					msg.what = 0;
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {// //���������״̬�޸�,ֵ��Ϊ2
				// if (1 == mnTaskType) {
				m_ProgressDialog = ProgressDialog.show(
						TaskAnnounceVertifOperaActivity.this, "��ʾ",
						"������,��ȴ�...", true);
				// } else {
				// m_ProgressDialog = ProgressDialog.show(
				// TaskAnnounceVertifOperaActivity.this, "��ʾ", "���ڷ�������,��ȴ�...",
				// true);
				// }
				if (mImgPaths.equals("")) {
					strImageBase64 = "";
					strLargeImageBase64 = "";
				} else {
					strImageBase64 = mUtils.imgToBase64(mImgPaths, nImageWidth,
							nImageHeight);
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

		linearlayout1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nCharmValue++;
				if (nCharmValue > 3) {
					nCharmValue = 0;
				}
				textview4.setText("+" + nCharmValue);
			}

		});

		returnimageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
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
				// ��ͼƬ������ת����
				bitmap = mUtils.PhotoRotation(uri);
				bitmap = mUtils.zoomBitmap(bitmap, nImageWidth, nImageHeight);
				/* ��Bitmap�趨��ImageView */
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
			builder.setTitle("�����û�ͼƬ");
			builder.setItems(addPhoto, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == 0) {
						String haveSD = Environment.getExternalStorageState();
						if (!haveSD.equals(Environment.MEDIA_MOUNTED)) {
							Toast.makeText(
									TaskAnnounceVertifOperaActivity.this,
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
						TaskAnnounceVertifOperaActivity.this
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