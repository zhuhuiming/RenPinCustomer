package com.renpin.renpincustomer;

import java.io.File;
import java.io.FileNotFoundException;

import com.renpin.location.MyApplication;
import com.renpin.location.MyLocation;
import com.renpin.location.MySearch;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class TaskAnnounceActivity extends Activity {
	// ���ؼ�
	ImageView imageview1;
	// ���ͼƬ��ͼ��
	ImageView imageview2;
	// Ҫ��ӵĵ�һ��ͼƬ�ؼ�(������������)
	ImageView imageview3;
	ImageView imageview4;
	ImageView imageview5;
	ImageView imageview6;
	ImageView imageview7;
	ImageView imageview8;
	// ��һ�ŷŴ�ͼƬ����(������������)
	String strIcon1 = "";
	String strIcon2 = "";
	String strIcon3 = "";
	String strIcon4 = "";
	String strIcon5 = "";
	String strIcon6 = "";
	// ��һ����СͼƬ����(������������)
	String strIcon7 = "";
	String strIcon8 = "";
	String strIcon9 = "";
	String strIcon10 = "";
	String strIcon11 = "";
	String strIcon12 = "";
	// �������
	EditText edittext1;
	// ������������
	EditText edittext2;
	// ������Чʱ��
	TextView textview3;
	// ������ϸ����
	EditText edittext3;
	// ���񷢲���ť
	Button button1;
	// ������ȡ�����������ݵĶ���
	private GoodService goodService = new GoodServiceImpl();
	private MySearch mMySearch = null;// �����Ծ�γ�����ַ֮���ת��
	private static CommonUtils mUtils = null;
	private String mBase64Icon;
	// �����洢�û���Ϣ
	SharedPreferences msettings = null;
	// ��ǰ�û�����
	String mCurrentPersonName;
	// ��ǰ�û��ǳ�
	String mCurrentPersonNameTemp;
	MyHandler myhandler;
	// ���õ�ʱ��(��)
	int mnSeconds = 24 * 3600;
	// ������ѡ��ť
	RadioButton radio1;
	// ����ѡ��ť
	RadioButton radio2;
	// ����ǰ����,1��ʾ��������,2��ʾ��������
	int mTaskCurrentType = 1;
	// ������ѡͼƬ��·��
	private String mImgPaths = "";
	public static final String[] addPhoto = new String[] { "��������", "�����ѡ��",
			"ȡ��" };
	// ͼƬ������
	int mnImageIndex = 0;
	// �ܹ�������ͼƬ����
	int mnImageCount = 6;
	// ����������
	public static ProgressDialog m_ProgressDialog = null;
	// ��СͼƬ�Ŀ��
	private final int nImageWidth = 400;
	// ��СͼƬ�ĸ߶�
	private final int nImageHeight = 400;
	// ��Ӱ�ť
	private ImageView addimageview;
	// ��������ʱ��Ʒֵ����
	private int nCreditValue = 1;
	// �ж��Ƿ��ȡ���˾�γ��
	private boolean bIsGetPosition = false;
	// ��ȡ��γ�ȵĴ���
	private int nGetPosTimes = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// ����������ʱ������괦��editview�ж��������뷨����
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setContentView(R.layout.makepublic);

		mnImageIndex = 0;

		if (2 == RenPinMainActivity.mIsShowingWindow) {
			mTaskCurrentType = 1;
		} else if (3 == RenPinMainActivity.mIsShowingWindow) {
			mTaskCurrentType = 2;
		}

		if (null == mMySearch) {
			mMySearch = new MySearch(this);
		}
		if (null == mUtils) {
			mUtils = new CommonUtils(this);
		}
		myhandler = new MyHandler();

		msettings = getSharedPreferences("MekeSharedPreferences", 0);
		mCurrentPersonNameTemp = msettings.getString("PersonName", "");
		// �ؼ���ʼ��
		InitActivities();
	}

	private void InitActivities() {
		edittext1 = (EditText) findViewById(R.id.makepublic_EditText1);
		edittext2 = (EditText) findViewById(R.id.makepublic_EditText2);
		textview3 = (TextView) findViewById(R.id.makepublic_textview1);
		edittext3 = (EditText) findViewById(R.id.makepublic_edittext3);
		button1 = (Button) findViewById(R.id.makepublic_button1);
		radio1 = (RadioButton) findViewById(R.id.hotel_order_radiobutton1);
		radio2 = (RadioButton) findViewById(R.id.hotel_order_radiobutton2);
		imageview1 = (ImageView) findViewById(R.id.customer_login_image1);
		imageview2 = (ImageView) findViewById(R.id.makepublic_imageview7);
		imageview3 = (ImageView) findViewById(R.id.makepublic_imageview1);
		imageview4 = (ImageView) findViewById(R.id.makepublic_imageview2);
		imageview5 = (ImageView) findViewById(R.id.makepublic_imageview3);
		imageview6 = (ImageView) findViewById(R.id.makepublic_imageview4);
		imageview7 = (ImageView) findViewById(R.id.makepublic_imageview5);
		imageview8 = (ImageView) findViewById(R.id.makepublic_imageview6);
		addimageview = (ImageView) findViewById(R.id.makepublic_addimageview);

		imageview1.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});

		imageview2.setOnClickListener(new ImageView.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(0);
			}

		});

		if (1 == mTaskCurrentType) {
			// Ĭ���������������ѡ��ť��ѡ��
			radio1.setChecked(true);
			radio2.setChecked(false);
		} else if (2 == mTaskCurrentType) {
			// Ĭ���������������ѡ��ť��ѡ��
			radio1.setChecked(false);
			radio2.setChecked(true);
		}

		String strText = "1��" + "0Сʱ" + "0��";
		textview3.setText(strText);

		edittext2.setText(MyLocation.mStrAddress);

		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String strTitleText = edittext1.getText().toString();

				mCurrentPersonName = msettings.getString("TruePersonName", "");
				if (mCurrentPersonName.equals("")) {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"���ȵ�¼", Toast.LENGTH_LONG);
					return;
				}
				if (strTitleText.equals("")) {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"�������������", Toast.LENGTH_LONG);
					edittext1.requestFocus();
					return;
				}
				if (edittext2.getText().toString().equals("")) {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"������ַ����Ϊ��", Toast.LENGTH_LONG);
					edittext2.requestFocus();
					return;
				}
				if (1 == mTaskCurrentType) {
					m_ProgressDialog = ProgressDialog.show(
							TaskAnnounceActivity.this, "��ʾ", "���ڷ�������,��ȴ�...",
							true);
					m_ProgressDialog.setCancelable(true);
				} else {
					m_ProgressDialog = ProgressDialog.show(
							TaskAnnounceActivity.this, "��ʾ", "���ڷ�������,��ȴ�...",
							true);
					m_ProgressDialog.setCancelable(true);
				}
				// ��ʼ������
				nGetPosTimes = 0;
				bIsGetPosition = false;
				// ���������߳�,�������η���,������ζ�ʧ��,��ô����Ϊ����������ʧ��
				// ��ʼ�����������������ݵ�����
				Runnable Accountrun = new Runnable() {
					public void run() {
						while (nGetPosTimes < 3 && !bIsGetPosition) {
							Message msg = myhandler.obtainMessage();
							msg.what = 1;
							myhandler.sendMessage(msg);
							nGetPosTimes++;
							try {
								// ˯5��,�ȴ���ȡ��γ��
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						// ������������ζ�ʧ��,��ô����ʾ�û�����ʧ��
						if (3 == nGetPosTimes && !bIsGetPosition) {
							Message msg = myhandler.obtainMessage();
							msg.what = 2;
							myhandler.sendMessage(msg);
						}
					}
				};
				Thread thread = new Thread(Accountrun);
				thread.start();
			}
		});

		textview3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AboutDialog aboutDialog = new AboutDialog(
						TaskAnnounceActivity.this, 0, new PriorityListener() {

							@Override
							public void refreshPriorityUI(String str1,
									String str2, String str3, String str4) {
								String strText;
								strText = str1 + "��" + str2 + "Сʱ" + str3 + "��";
								textview3.setText(strText);
								mnSeconds = mUtils.GetSumSeconds(str1, str2,
										str3, str4);
							}

						});
				aboutDialog.show();
			}

		});

		radio1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				radio2.setChecked(false);
				mTaskCurrentType = 1;
			}

		});

		radio2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				radio1.setChecked(false);
				mTaskCurrentType = 2;
			}

		});

		addimageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AboutDialog aboutDialog = null;
				aboutDialog = new AboutDialog(TaskAnnounceActivity.this, 3,
						new PriorityListener() {

							@Override
							public void refreshPriorityUI(String str1,
									String str2, String str3, String str4) {
								nCreditValue = AboutDialog.GetCreditValue();
							}

						});
				aboutDialog.show();
			}

		});
	}

	class MyHandler extends Handler {
		// ���������д�˷���,��������
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle b = msg.getData();
			switch (msg.what) {
			case 0:
				int nRet = b.getInt("nRet");
				int nRet1 = b.getInt("nRet1");

				if (0 == nRet) {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"��������ʧ��", Toast.LENGTH_LONG);
				} else {
					// CommonUtils.ShowToastCenter(getApplicationContext(),
					// "��������ɹ�", Toast.LENGTH_LONG);
				}
				if (0 == nRet1) {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"�ϴ�ͼƬʧ��", Toast.LENGTH_LONG);
				} else {
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"��������ɹ�", Toast.LENGTH_LONG);
				}
				if (m_ProgressDialog != null) {
					m_ProgressDialog.dismiss();
					m_ProgressDialog = null;
				}
				button1.setText("����");
				button1.setEnabled(true);
				button1.setTextColor(Color.rgb(255, 255, 255));
				//��������ɹ���,��ô���˳���ǰactivity
				if(nRet > 0 && nRet1 > 0){
					finish();	
				}
				break;
			case 1:
				String strAddress = edittext2.getText().toString();// ��ַ
				String strCity = "�Ϻ�";// ��ǰ��������
				if (mMySearch != null) {
					strAddress = edittext2.getText().toString();

					if (strAddress != null && strCity != null) {
						// ���ݵط����ƻ�ȡ���ľ�γ��
						mMySearch.GetLatiLong(strAddress, strCity);
					}
				}
				button1.setText("���ڷ���...");
				button1.setEnabled(false);
				button1.setTextColor(Color.rgb(96, 96, 96));
				break;
			case 2:
				button1.setText("����");
				button1.setEnabled(true);
				button1.setTextColor(Color.rgb(255, 255, 255));
				CommonUtils.ShowToastCenter(getApplicationContext(), "����ʧ��",
						Toast.LENGTH_LONG);
				break;
			}
		}
	}

	// ��ȡ��ָ����ַ�ľ�γ�Ⱥ�����������
	public void DealAddressPositionInfo(final double dLongitude,
			final double dLatitude) {

		// �ж�֮ǰ�ǲ����Ѿ�����˾�γ��,������һ���������������
		if (bIsGetPosition) {
			// ��������,��ô�Ͳ��ٷ���
			return;
		}
		// �����ȡ���˾�γ��
		bIsGetPosition = true;
		nGetPosTimes = 0;

		if (0 == dLongitude && 0 == dLatitude) {
			if (m_ProgressDialog != null) {
				m_ProgressDialog.dismiss();
				m_ProgressDialog = null;
			}
			button1.setText("����");
			button1.setEnabled(true);
			button1.setTextColor(Color.rgb(255, 255, 255));
			CommonUtils.ShowToastCenter(getApplicationContext(), "����ʧ��",
					Toast.LENGTH_LONG);
		} else {
			// ��ʼ��ֵ
			final String str1 = edittext1.getText().toString();// �������
			final String str2 = edittext2.getText().toString();// ��������
			final String str3 = edittext3.getText().toString();// ������ϸ����
			String strPersonName = msettings.getString("TruePersonName", "");
			if (strPersonName.equals("")) {
				CommonUtils.ShowToastCenter(getApplicationContext(), "���ȵ�¼",
						Toast.LENGTH_LONG);
				button1.setText("����");
				button1.setEnabled(true);
				button1.setTextColor(Color.rgb(255, 255, 255));
				return;
			}
			String strBase64Image = msettings.getString("Base64Image", "");
			if (!strBase64Image.equals("")) {
				mBase64Icon = strBase64Image;
			} else {
				Resources res = getResources();
				Bitmap bmp = BitmapFactory.decodeResource(res,
						R.drawable.noperson);
				mBase64Icon = mUtils.BitmapToBase64BySize(bmp);
			}
			// ��ʼ�����������������ݵ�����
			Runnable run1 = new Runnable() {
				public void run() {

					int nRet1 = 0;
					int nRet = goodService.AddTaskInfoForLimit(
							mTaskCurrentType, str2, str1, str3, "", "",
							mCurrentPersonNameTemp, "", mCurrentPersonName, "",
							dLongitude, dLatitude, mBase64Icon, mnSeconds + "",
							strIcon7, strIcon8, strIcon9, strIcon10, strIcon11,
							strIcon12, nCreditValue, 0);

					if (nRet > 0) {// �����������û��ʧ��
						String strTrueName = msettings.getString(
								"TruePersonName", "");
						if (2 == mTaskCurrentType) {
							// ��Ʒֵ��һ
							goodService.AddCreditValue(strTrueName, 1);
						}
						nRet1 = goodService.UploadLargeImage(nRet + "",
								mTaskCurrentType + "", strIcon1, strIcon2,
								strIcon3, strIcon4, strIcon5, strIcon6);
					}
					Message msg = myhandler.obtainMessage();
					Bundle b = new Bundle();// �������
					b.putInt("nRet", nRet);
					b.putInt("nRet1", nRet1);
					msg.setData(b);
					msg.what = 0;
					myhandler.sendMessage(msg);
				}
			};
			Thread thread = new Thread(run1);
			thread.start();
		}
	}

	private void SaveLargeImage(Bitmap bmp) {
		if (0 == mnImageIndex) {
			strIcon1 = mUtils.BitmapToBase64BySize(mUtils.zoomBitmap(bmp,
					MyApplication.nLargeImageWidth,
					MyApplication.nLargeImageHeight));
		} else if (1 == mnImageIndex) {
			strIcon2 = mUtils.BitmapToBase64BySize(mUtils.zoomBitmap(bmp,
					MyApplication.nLargeImageWidth,
					MyApplication.nLargeImageHeight));
		} else if (2 == mnImageIndex) {
			strIcon3 = mUtils.BitmapToBase64BySize(mUtils.zoomBitmap(bmp,
					MyApplication.nLargeImageWidth,
					MyApplication.nLargeImageHeight));
		} else if (3 == mnImageIndex) {
			strIcon4 = mUtils.BitmapToBase64BySize(mUtils.zoomBitmap(bmp,
					MyApplication.nLargeImageWidth,
					MyApplication.nLargeImageHeight));
		} else if (4 == mnImageIndex) {
			strIcon5 = mUtils.BitmapToBase64BySize(mUtils.zoomBitmap(bmp,
					MyApplication.nLargeImageWidth,
					MyApplication.nLargeImageHeight));
		} else if (5 == mnImageIndex) {
			strIcon6 = mUtils.BitmapToBase64BySize(mUtils.zoomBitmap(bmp,
					MyApplication.nLargeImageWidth,
					MyApplication.nLargeImageHeight));
		}
	}

	private void ShowSmallImageInImageView(Bitmap bmp) {// ��ͼƬ��ʾ��ָ����ͼƬ������
		if (0 == mnImageIndex) {
			imageview3.setImageBitmap(bmp);
			strIcon7 = mUtils.BitmapToBase64BySize(mUtils.zoomBitmap(bmp,
					MyApplication.nImageWidth, MyApplication.nImageHeight));
		} else if (1 == mnImageIndex) {
			imageview4.setImageBitmap(bmp);
			strIcon8 = mUtils.BitmapToBase64BySize(mUtils.zoomBitmap(bmp,
					MyApplication.nImageWidth, MyApplication.nImageHeight));
		} else if (2 == mnImageIndex) {
			imageview5.setImageBitmap(bmp);
			strIcon9 = mUtils.BitmapToBase64BySize(mUtils.zoomBitmap(bmp,
					MyApplication.nImageWidth, MyApplication.nImageHeight));
		} else if (3 == mnImageIndex) {
			imageview6.setImageBitmap(bmp);
			strIcon10 = mUtils.BitmapToBase64BySize(mUtils.zoomBitmap(bmp,
					MyApplication.nImageWidth, MyApplication.nImageHeight));
		} else if (4 == mnImageIndex) {
			imageview7.setImageBitmap(bmp);
			strIcon11 = mUtils.BitmapToBase64BySize(mUtils.zoomBitmap(bmp,
					MyApplication.nImageWidth, MyApplication.nImageHeight));
		} else if (5 == mnImageIndex) {
			imageview8.setImageBitmap(bmp);
			strIcon12 = mUtils.BitmapToBase64BySize(mUtils.zoomBitmap(bmp,
					MyApplication.nImageWidth, MyApplication.nImageHeight));
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

						if (mnImageIndex >= mnImageCount) {
							mnImageIndex = 0;
						}
						// ���Ŵ�ͼƬ��base64����
						SaveLargeImage(bitmap/*
											 * ThumbnailUtils.extractThumbnail(
											 * bitmap, 500, 500)
											 */);

						// bitmap = ThumbnailUtils
						// .extractThumbnail(bitmap, 500, 500);
						// ��ͼƬ��ʾ��ͼƬ�ؼ���
						ShowSmallImageInImageView(bitmap);

						mnImageIndex++;
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

					if (mnImageIndex >= mnImageCount) {
						mnImageIndex = 0;
					}
					// ���Ŵ�ͼƬ��base64����
					SaveLargeImage(bitmap/*
										 * ThumbnailUtils.extractThumbnail(bitmap
										 * , 500, 500)
										 */);

					// bitmap = ThumbnailUtils.extractThumbnail(bitmap, 500,
					// 500);
					// ��ͼƬ��ʾ��ͼƬ�ؼ���
					ShowSmallImageInImageView(bitmap);

					mnImageIndex++;
				} catch (OutOfMemoryError e) {
					CommonUtils.ShowToastCenter(this, "�ڴ治��!",
							Toast.LENGTH_LONG);
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
			builder.setTitle("����û�ͼƬ");
			builder.setItems(addPhoto, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == 0) {
						String haveSD = Environment.getExternalStorageState();
						if (!haveSD.equals(Environment.MEDIA_MOUNTED)) {
							Toast.makeText(TaskAnnounceActivity.this, "�洢��������",
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
						TaskAnnounceActivity.this.startActivityForResult(
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

	// ��Ļ��ת
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
		}
	}

	@Override
	public void onBackPressed() {
		if (m_ProgressDialog != null) {
			m_ProgressDialog.dismiss();
			m_ProgressDialog = null;
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
