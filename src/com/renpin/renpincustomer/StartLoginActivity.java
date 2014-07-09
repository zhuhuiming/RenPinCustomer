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
	// 用来存储用户信息
	SharedPreferences msettings = null;
	// 用户名编辑框
	EditText logincustomername;
	// 新增用户按钮
	TextView loginnewcustomer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 程序启动的时候避免光标处在editview中而弹出输入法窗口
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.login);
		msettings = getSharedPreferences("MekeSharedPreferences", 0);
		InitActivity();
	}

	private void InitActivity() {
		logincustomername = (EditText) findViewById(R.id.login_customername);
		loginnewcustomer = (TextView) findViewById(R.id.login_newcustomer);

		// 点击新增用户
		loginnewcustomer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 切换到注册界面
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
