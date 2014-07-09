package com.renpin.renpincustomer;

import com.renpin.domin.CustomerInfo;
import com.renpin.service.GoodService;
import com.renpin.service.Impl.GoodServiceImpl;
import com.renpin.utils.CommonUtils;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	ImageView image1;
	Button button1;
	EditText edittext1;
	EditText edittext2;
	MyHandler myhandler;
	// ע�ᰴť
	TextView textview1;
	private GoodService goodService = new GoodServiceImpl();
	SharedPreferences msettings = null;
	// �洢��¼���û���Ϣ
	public static CustomerInfo mcustomer;
	// ����������
	public ProgressDialog m_ProgressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����������ʱ������괦��editview�ж��������뷨����
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setContentView(R.layout.activity_login);

		// ��ʼ����ͼ
		InitActivities();

		myhandler = new MyHandler();

		msettings = getSharedPreferences("MekeSharedPreferences", 0);
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
				CommonUtils.ShowToastCenter(getApplicationContext(), "��¼ʧ��",
						Toast.LENGTH_LONG);
			} else {
				CommonUtils.ShowToastCenter(getApplicationContext(), "��¼�ɹ�",
						Toast.LENGTH_LONG);

				SharedPreferences.Editor editor = msettings.edit();
				editor.putString("TruePersonName",
						mcustomer.getstrCustomerName());
				editor.putString("PersonName", mcustomer.getstrNickName());
				// ��base64���͵�ͼ�걣������
				editor.putString("Base64Image", mcustomer.getstrIcon());
				editor.putInt("CreditValue", mcustomer.getnCreditValue());
				editor.putInt("CharmValue", mcustomer.getnCharmValue());
				editor.putString("sex", mcustomer.getsex());
				editor.commit();
				// �жϿգ��ҾͲ��ж��ˡ�������
				Intent data = new Intent();
				data.putExtra("personname", mcustomer.getstrNickName());
				data.putExtra("CreditValue", mcustomer.getnCreditValue());
				data.putExtra("CharmValue", mcustomer.getnCharmValue());
				// �����������Լ����ã��������ó�20
				setResult(20, data);
				// �رյ����Activity
				finish();
			}
		}
	}

	private void InitActivities() {
		image1 = (ImageView) findViewById(R.id.customer_login_image1);
		button1 = (Button) findViewById(R.id.customer_login_button1);
		edittext1 = (EditText) findViewById(R.id.customer_login_edit1);
		edittext2 = (EditText) findViewById(R.id.customer_login_edit2);
		textview1 = (TextView) findViewById(R.id.customer_login_textview2);

		image1.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});

		button1.setOnClickListener(new Button.OnClickListener() {

			Runnable run1 = new Runnable() {
				public void run() {
					mcustomer = goodService.LogIn(edittext1.getText()
							.toString(), edittext2.getText().toString());
					Message msg = myhandler.obtainMessage();
					if (null == mcustomer) {
						msg.what = 1;
					} else {
						msg.what = 0;
						Bundle b = new Bundle();// �������
						b.putString("sex", mcustomer.getsex());
						b.putString("detail", mcustomer.getdetail());
						b.putInt("age", mcustomer.getage());
						b.putInt("score", mcustomer.getscore());
						msg.setData(b);

						// ������ˢ�±�־����Ϊ1
						// goodService.SetUpdateSignal(edittext1.getText()
						// .toString(), 1);
					}
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {
				String strTemp1 = edittext1.getText().toString();
				String strTemp2 = edittext2.getText().toString();
				if (strTemp1.isEmpty()) {
					edittext1.setSelection(0);
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"�������˺�", Toast.LENGTH_LONG);
					return;
				} else if (strTemp2.isEmpty()) {
					edittext2.setSelection(0);
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"����������", Toast.LENGTH_LONG);
					return;
				}
				m_ProgressDialog = ProgressDialog.show(LoginActivity.this,
						"��ʾ", "���ڵ�¼,��ȴ�...", true);
				m_ProgressDialog.setCancelable(true);

				Thread thread = new Thread(run1);
				thread.start();
			}

		});

		textview1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegeditActivity.class);
				startActivity(intent);
			}

		});
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