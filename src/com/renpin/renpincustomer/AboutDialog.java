﻿package com.renpin.renpincustomer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("SdCardPath")
public class AboutDialog extends AlertDialog implements OnClickListener {

	Button button1;
	EditText edittext1;// 天
	EditText edittext2;// 时
	EditText edittext3;// 分
	// EditText edittext4;// 秒

	String strDay;// 天
	String strHour;// 小时
	String strMinute;// 分钟
	String strSecond;// 秒
	static String strMsg;// 拿下任务的提示信息

	// 点击了拿下按钮后显示的提示窗口中显示提示信息的textview
	TextView textview1;
	TextView textview2;
	// 确定按钮
	Button button2;
	// 取消按钮
	Button button3;
	// 填入人品值得编辑框
	private EditText creditedittext;
	// 设置人品值限制的按钮
	private Button creditbutton;
	// 人品值限制值
	private static int nCreditValue = 1;
	// 选择类型,如果点击了确定按钮则该值为1,取消按钮该值为2
	private static int nSelectType = 0;
	//修改项目信息的编辑框
	private EditText itemedittext;
	//保存修改信息的按钮
	private Button savebutton;
	//取消按钮
	private Button canclebutton;
	//项目初始信息
	private static String strItemText = "";

	PriorityListener mlistener = null;

	/**
	 * 自定义Dialog监听器
	 */
	public interface PriorityListener {
		/**
		 * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
		 */
		public void refreshPriorityUI(String str1, String str2, String str3,
				String str4);
	}

	public AboutDialog(final Context context, int nType,
			PriorityListener listener) {
		super(context);
		mlistener = listener;
		final View cfg_view;
		switch (nType) {
		case 0:
			cfg_view = getLayoutInflater().inflate(R.layout.validtime_setting,
					null);
			setView(cfg_view);

			button1 = (Button) cfg_view.findViewById(R.id.validtime_button1);
			edittext1 = (EditText) cfg_view
					.findViewById(R.id.validtime_edittext1);
			edittext2 = (EditText) cfg_view
					.findViewById(R.id.validtime_edittext2);
			edittext3 = (EditText) cfg_view
					.findViewById(R.id.validtime_edittext3);
			// edittext4 = (EditText) cfg_view
			// .findViewById(R.id.validtime_edittext4);

			edittext1.setText("1");
			edittext2.setText("0");
			edittext3.setText("0");
			// edittext4.setText("0");

			button1.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					strDay = edittext1.getText().toString();
					strHour = edittext2.getText().toString();
					strMinute = edittext3.getText().toString();
					// strSecond = edittext4.getText().toString();

					mlistener.refreshPriorityUI(strDay, strHour, strMinute,
							0 + "");
					AboutDialog.this.dismiss();
				}

			});
			break;
		case 1:
			cfg_view = getLayoutInflater().inflate(R.layout.taskget_confirm,
					null);
			setView(cfg_view);

			textview1 = (TextView) cfg_view
					.findViewById(R.id.taskget_confirm_textview1);
			button2 = (Button) cfg_view
					.findViewById(R.id.taskget_confirm_button1);
			button3 = (Button) cfg_view
					.findViewById(R.id.taskget_confirm_button2);

			textview1.setText(strMsg);

			button2.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					nSelectType = 1;
					mlistener.refreshPriorityUI("", "", "", "");
					AboutDialog.this.dismiss();
				}

			});

			button3.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					nSelectType = 2;
					mlistener.refreshPriorityUI("", "", "", "");
					AboutDialog.this.dismiss();
				}

			});
			break;
		case 2:
			cfg_view = getLayoutInflater().inflate(R.layout.taskget_confirm,
					null);
			setView(cfg_view);

			textview1 = (TextView) cfg_view
					.findViewById(R.id.taskget_confirm_textview1);
			textview2 = (TextView) cfg_view
					.findViewById(R.id.taskget_confirm_textview2);
			button2 = (Button) cfg_view
					.findViewById(R.id.taskget_confirm_button1);
			button3 = (Button) cfg_view
					.findViewById(R.id.taskget_confirm_button2);

			textview1.setText(strMsg);
			textview2.setText("确定抢下?");

			button2.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					nSelectType = 1;
					mlistener.refreshPriorityUI("", "", "", "");
					AboutDialog.this.dismiss();
				}

			});

			button3.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					nSelectType = 2;
					mlistener.refreshPriorityUI("", "", "", "");
					AboutDialog.this.dismiss();
				}

			});
			break;
		case 3:
			cfg_view = getLayoutInflater().inflate(R.layout.credit_setting,
					null);
			setView(cfg_view);

			creditedittext = (EditText) cfg_view
					.findViewById(R.id.credit_setting_valueedittext);
			creditbutton = (Button) cfg_view
					.findViewById(R.id.credit_setting_valuebutton);
			creditedittext.setText("1");

			creditbutton.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {

					nCreditValue = Integer.parseInt(creditedittext.getText()
							.toString());

					mlistener.refreshPriorityUI("", "", "", "");

					AboutDialog.this.dismiss();
				}

			});
			break;
		case 4:
			cfg_view = getLayoutInflater().inflate(R.layout.text_edit,
					null);
			setView(cfg_view);

			itemedittext = (EditText) cfg_view
					.findViewById(R.id.textedit_edittext1);
			itemedittext.setText(strItemText);
			
			savebutton = (Button) cfg_view
					.findViewById(R.id.textedit_confirm_button1);
			canclebutton = (Button) cfg_view
					.findViewById(R.id.textedit_confirm_button2);

			savebutton.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					//获取新的数据信息
					String strText = itemedittext.getText().toString();
					mlistener.refreshPriorityUI("1", strText, "", "");
					AboutDialog.this.dismiss();
				}
			});
			
			canclebutton.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					mlistener.refreshPriorityUI("2", "", "", "");
					AboutDialog.this.dismiss();
				}

			});
			break;
		}
	}

	public static void SetMsg(String strMessg) {
		strMsg = strMessg;
	}

	public static int GetSelectType() {
		return nSelectType;
	}

	public static int GetCreditValue() {
		return nCreditValue;
	}

	public static void SetInitialText(String strText){
		strItemText = strText;
	}
	
	@Override
	public void onClick(View v) {
	}
}