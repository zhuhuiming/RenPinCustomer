package com.renpin.renpincustomer;

import com.renpin.service.GoodService;
import com.renpin.service.Impl.GoodServiceImpl;
import com.renpin.utils.CommonUtils;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AdviceActivity extends Activity {

	private Button sendadvicebutton;
	private ImageView returnImageView;
	private EditText adviceedittext;
	private String strAdvice;
	// 接收消息对象
	MyHandler myhandler = null;
	// 用来获取服务器上数据的对象
	private GoodService goodService = new GoodServiceImpl();
	// 进度条对象
	public ProgressDialog m_ProgressDialog = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 程序启动的时候避免光标处在editview中而弹出输入法窗口
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setContentView(R.layout.advice);

		InitActivitys();

		// 创建接收消息对象
		myhandler = new MyHandler();
	}

	private void InitActivitys() {
		sendadvicebutton = (Button) findViewById(R.id.advice_sendadvice);
		returnImageView = (ImageView) findViewById(R.id.advice_returnimage1);
		adviceedittext = (EditText) findViewById(R.id.advice_adviceedittext);

		returnImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});

		sendadvicebutton.setOnClickListener(new OnClickListener() {

			Runnable run1 = new Runnable() {
				public void run() {
					int nRet = goodService.SendAdviceText(strAdvice);
					Message msg = myhandler.obtainMessage();
					Bundle b = new Bundle();// 存放数据
					b.putInt("nRet", nRet);
					msg.setData(b);
					msg.what = 0;
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {

				m_ProgressDialog = ProgressDialog.show(AdviceActivity.this,
						"提示", "正在发送您的意见,请等待...", true);
				m_ProgressDialog.setCancelable(true);

				strAdvice = AdviceActivity.this.adviceedittext.getText()
						.toString();
				Thread thread = new Thread(run1);
				thread.start();
			}

		});
	}

	class MyHandler extends Handler {
		// 子类必须重写此方法,接受数据
		@SuppressWarnings("deprecation")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				if (m_ProgressDialog != null) {
					m_ProgressDialog.dismiss();
					m_ProgressDialog = null;
				}
				Bundle b = msg.getData();
				int nRet = b.getInt("nRet");
				if (1 == nRet) {
					CommonUtils.ShowToastCenter(AdviceActivity.this,
							"发送成功,谢谢您的建议,我们会尽快处理!", Toast.LENGTH_LONG);
				} else {
					CommonUtils.ShowToastCenter(AdviceActivity.this, "发送失败",
							Toast.LENGTH_LONG);
				}
				break;
			}
		}
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
