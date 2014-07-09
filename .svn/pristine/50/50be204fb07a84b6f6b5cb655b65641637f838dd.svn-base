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
	// 用来存储用户信息
	SharedPreferences msettings = null;
	// 用来输入手机号或QQ号的编辑框
	EditText edittext1;
	// 用来输入用户密码的编辑框
	EditText edittext2;
	// 用来输入用户确认密码的编辑框
	EditText edittext3;
	// 下一步按钮
	Button button1;
	// 存储用户名称
	public static String strCustomerName = "";
	// 存储用户密码
	public static String strCustomerPassword = "";
	// 存储用户确认密码
	public static String strCustomerConfirmPassword = "";
	private GoodService goodService = new GoodServiceImpl();
	// 进度条对象
	public static ProgressDialog m_ProgressDialog = null;
	MyHandler myhandler;
	// 返回按钮
	RelativeLayout retrunrelativelayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 程序启动的时候避免光标处在editview中而弹出输入法窗口
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.regedit_id);
		msettings = getSharedPreferences("MekeSharedPreferences", 0);
		InitActivity();
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
				// Toast.makeText(getApplicationContext(), "注册成功",
				// Toast.LENGTH_LONG).show();
				// 进入注册界面的下一个界面
				Intent it = new Intent(StartRegeditActivity.this,
						RegeditNextActivity.class);
				startActivity(it);
			} else if (2 == msg.what) {
				Toast.makeText(getApplicationContext(), "已经存在该账号，请选择其他账号",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "出现错误",
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
					// 开始注册用户
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
							"用户名不能为空", Toast.LENGTH_LONG);
					return;
				}
				if (strCustomerPassword.equals("")) {
					CommonUtils.ShowToastCenter(StartRegeditActivity.this,
							"密码不能为空", Toast.LENGTH_LONG);
					return;
				}
				if (strCustomerConfirmPassword.equals("")) {
					CommonUtils.ShowToastCenter(StartRegeditActivity.this,
							"请输入确认密码", Toast.LENGTH_LONG);
					return;
				}
				if (!strCustomerPassword.equals(strCustomerConfirmPassword)) {
					CommonUtils.ShowToastCenter(StartRegeditActivity.this,
							"确认密码输入错误,请重新输入", Toast.LENGTH_LONG);
					return;
				}
				m_ProgressDialog = ProgressDialog.show(
						StartRegeditActivity.this, "提示", "正在认证用户账号,请等待...",
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
