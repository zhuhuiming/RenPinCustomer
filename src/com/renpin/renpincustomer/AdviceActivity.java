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
	// ������Ϣ����
	MyHandler myhandler = null;
	// ������ȡ�����������ݵĶ���
	private GoodService goodService = new GoodServiceImpl();
	// ����������
	public ProgressDialog m_ProgressDialog = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// ����������ʱ������괦��editview�ж��������뷨����
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setContentView(R.layout.advice);

		InitActivitys();

		// ����������Ϣ����
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
					Bundle b = new Bundle();// �������
					b.putInt("nRet", nRet);
					msg.setData(b);
					msg.what = 0;
					myhandler.sendMessage(msg);
				}
			};

			@Override
			public void onClick(View v) {

				m_ProgressDialog = ProgressDialog.show(AdviceActivity.this,
						"��ʾ", "���ڷ����������,��ȴ�...", true);
				m_ProgressDialog.setCancelable(true);

				strAdvice = AdviceActivity.this.adviceedittext.getText()
						.toString();
				Thread thread = new Thread(run1);
				thread.start();
			}

		});
	}

	class MyHandler extends Handler {
		// ���������д�˷���,��������
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
							"���ͳɹ�,лл���Ľ���,���ǻᾡ�촦��!", Toast.LENGTH_LONG);
				} else {
					CommonUtils.ShowToastCenter(AdviceActivity.this, "����ʧ��",
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
