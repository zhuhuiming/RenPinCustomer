package com.renpin.renpincustomer;

import com.renpin.service.GoodService;
import com.renpin.service.Impl.GoodServiceImpl;
import com.renpin.utils.CommonUtils;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class StartRegeditActivity extends Activity {
	// �����洢�û���Ϣ
	SharedPreferences msettings = null;
	// ���������ֻ��Ż�QQ�ŵı༭��
	EditText edittext1;
	// ���������û�����ı༭��
	EditText edittext2;
	// ���������û�ȷ������ı༭��
	EditText edittext3;
	// ��һ����ť
	Button button1;
	// �洢�û�����
	public static String strCustomerName = "";
	// �洢�û�����
	public static String strCustomerPassword = "";
	// �洢�û�ȷ������
	public static String strCustomerConfirmPassword = "";
	private GoodService goodService = new GoodServiceImpl();
	// ����������
	public static ProgressDialog m_ProgressDialog = null;
	MyHandler myhandler;
	// ���ذ�ť
	RelativeLayout retrunrelativelayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// ����������ʱ������괦��editview�ж��������뷨����
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.regedit_id);
		msettings = getSharedPreferences("MekeSharedPreferences", 0);
		InitActivity();
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
				// Toast.makeText(getApplicationContext(), "ע��ɹ�",
				// Toast.LENGTH_LONG).show();
				// ����ע��������һ������
				Intent it = new Intent(StartRegeditActivity.this,
						RegeditNextActivity.class);
				startActivity(it);
			} else if (2 == msg.what) {
				Toast.makeText(getApplicationContext(), "�Ѿ����ڸ��˺ţ���ѡ�������˺�",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "���ִ���",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private void InitActivity() {
		edittext1 = (EditText) findViewById(R.id.regeditid_edittext1);
		edittext2 = (EditText) findViewById(R.id.regeditid_password);
		edittext3 = (EditText) findViewById(R.id.regeditid_passwordconfirm);
		button1 = (Button) findViewById(R.id.regeditid_nextbutton);
		retrunrelativelayout = (RelativeLayout) findViewById(R.id.regeditid_returnarelativelayout);

		button1.setOnClickListener(new OnClickListener() {

			Runnable run1 = new Runnable() {
				public void run() {
					// ��ʼע���û�
					int nRetType = goodService.JudgeCustomerValidity(
							strCustomerName, strCustomerPassword);
					Message msg = new Message();
					msg.what = nRetType;
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {
				strCustomerName = edittext1.getText().toString();
				strCustomerPassword = edittext2.getText().toString();
				strCustomerConfirmPassword = edittext3.getText().toString();

				if (strCustomerName.equals("")) {
					CommonUtils.ShowToastCenter(StartRegeditActivity.this,
							"�û�������Ϊ��", Toast.LENGTH_LONG);
					return;
				}
				if (strCustomerPassword.equals("")) {
					CommonUtils.ShowToastCenter(StartRegeditActivity.this,
							"���벻��Ϊ��", Toast.LENGTH_LONG);
					return;
				}
				if (strCustomerConfirmPassword.equals("")) {
					CommonUtils.ShowToastCenter(StartRegeditActivity.this,
							"������ȷ������", Toast.LENGTH_LONG);
					return;
				}
				if (!strCustomerPassword.equals(strCustomerConfirmPassword)) {
					CommonUtils.ShowToastCenter(StartRegeditActivity.this,
							"ȷ�������������,����������", Toast.LENGTH_LONG);
					return;
				}
				m_ProgressDialog = ProgressDialog.show(
						StartRegeditActivity.this, "��ʾ", "������֤�û��˺�,��ȴ�...",
						true);
				m_ProgressDialog.setCancelable(true);
				Thread thread = new Thread(run1);
				thread.start();
			}
		});

		retrunrelativelayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
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
