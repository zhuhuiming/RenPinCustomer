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
	// 注册按钮
	TextView textview1;
	private GoodService goodService = new GoodServiceImpl();
	SharedPreferences msettings = null;
	// 存储登录的用户信息
	public static CustomerInfo mcustomer;
	// 进度条对象
	public ProgressDialog m_ProgressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 程序启动的时候避免光标处在editview中而弹出输入法窗口
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setContentView(R.layout.activity_login);

		// 初始化视图
		InitActivities();

		myhandler = new MyHandler();

		msettings = getSharedPreferences("MekeSharedPreferences", 0);
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
				CommonUtils.ShowToastCenter(getApplicationContext(), "登录失败",
						Toast.LENGTH_LONG);
			} else {
				CommonUtils.ShowToastCenter(getApplicationContext(), "登录成功",
						Toast.LENGTH_LONG);

				SharedPreferences.Editor editor = msettings.edit();
				editor.putString("TruePersonName",
						mcustomer.getstrCustomerName());
				editor.putString("PersonName", mcustomer.getstrNickName());
				// 将base64类型的图标保存起来
				editor.putString("Base64Image", mcustomer.getstrIcon());
				editor.putInt("CreditValue", mcustomer.getnCreditValue());
				editor.putInt("CharmValue", mcustomer.getnCharmValue());
				editor.putString("sex", mcustomer.getsex());
				editor.commit();
				// 判断空，我就不判断了。。。。
				Intent data = new Intent();
				data.putExtra("personname", mcustomer.getstrNickName());
				data.putExtra("CreditValue", mcustomer.getnCreditValue());
				data.putExtra("CharmValue", mcustomer.getnCharmValue());
				// 请求代码可以自己设置，这里设置成20
				setResult(20, data);
				// 关闭掉这个Activity
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
						Bundle b = new Bundle();// 存放数据
						b.putString("sex", mcustomer.getsex());
						b.putString("detail", mcustomer.getdetail());
						b.putInt("age", mcustomer.getage());
						b.putInt("score", mcustomer.getscore());
						msg.setData(b);

						// 将数据刷新标志设置为1
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
							"请输入账号", Toast.LENGTH_LONG);
					return;
				} else if (strTemp2.isEmpty()) {
					edittext2.setSelection(0);
					CommonUtils.ShowToastCenter(getApplicationContext(),
							"请输入密码", Toast.LENGTH_LONG);
					return;
				}
				m_ProgressDialog = ProgressDialog.show(LoginActivity.this,
						"提示", "正在登录,请等待...", true);
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
