package com.renpin.renpincustomer;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class StartLoginActivity extends Activity {
	// �����洢�û���Ϣ
	SharedPreferences msettings = null;
	// �û����༭��
	EditText logincustomername;
	// �����û���ť
	TextView loginnewcustomer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// ����������ʱ������괦��editview�ж��������뷨����
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.login);
		msettings = getSharedPreferences("MekeSharedPreferences", 0);
		InitActivity();
	}

	private void InitActivity() {
		logincustomername = (EditText) findViewById(R.id.login_customername);
		loginnewcustomer = (TextView) findViewById(R.id.login_newcustomer);

		// ��������û�
		loginnewcustomer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// �л���ע�����
				Intent it = new Intent(StartLoginActivity.this,
						StartRegeditActivity.class);
				startActivity(it);
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