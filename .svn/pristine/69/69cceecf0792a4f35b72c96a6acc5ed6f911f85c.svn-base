package com.renpin.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.renpin.renpincustomer.DynamicDetailActivity;
import com.renpin.renpincustomer.R;
import com.renpin.renpincustomer.ShareDynamicDetailActivity;
import com.renpin.utils.CommonUtils;
import com.renpin.utils.Util;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;

import android.content.ContextWrapper;

public class ShareInfoToWX extends ContextWrapper {

	private static final int THUMB_SIZE = 150;
	// 微信appId
	public static final String APP_ID = "wxc048deb3db7f6839";
	// 分享到朋友圈需要的版本
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
    
	private static final int DETAILMAXCHANUM = 20;
	
	Context mContext = null;
	// 类型,1表示求助,2表示分享
	int mnType = 0;

	public static final String[] addPhoto = new String[] { "分享到微信好友",
			"分享到微信朋友圈", "发送短信" };

	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api = null;

	public ShareInfoToWX(Context base) {
		super(base);
	}

	public ShareInfoToWX(Context base, int nType) {
		super(base);
		mContext = base;
		mnType = nType;
		if (mContext != null) {
			api = WXAPIFactory
					.createWXAPI(mContext, ShareInfoToWX.APP_ID, true);
		}
	}

	public Bitmap GetCurrentScreenImage() {
		Bitmap bmp = null;
		if (mContext != null) {
			// 如果为求助
			if (1 == mnType) {
				DynamicDetailActivity activity = (DynamicDetailActivity) mContext;
				// 1.构建Bitmap
				WindowManager windowManager = activity.getWindowManager();
				Display display = windowManager.getDefaultDisplay();
				@SuppressWarnings("deprecation")
				int w = display.getWidth();
				@SuppressWarnings("deprecation")
				int h = display.getHeight();

				bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);

				// 2.获取屏幕
				View decorview = activity.getWindow().getDecorView();
				decorview.setDrawingCacheEnabled(true);
				bmp = decorview.getDrawingCache();
			} else if (2 == mnType) {// 如果为分享
				ShareDynamicDetailActivity activity = (ShareDynamicDetailActivity) mContext;
				// 1.构建Bitmap
				WindowManager windowManager = activity.getWindowManager();
				Display display = windowManager.getDefaultDisplay();
				@SuppressWarnings("deprecation")
				int w = display.getWidth();
				@SuppressWarnings("deprecation")
				int h = display.getHeight();

				bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);

				// 2.获取屏幕
				View decorview = activity.getWindow().getDecorView();
				decorview.setDrawingCacheEnabled(true);
				bmp = decorview.getDrawingCache();
			}
		}
		return bmp;
	}

	public void ShareInfoToOthers() {
		AlertDialog dialog = null;
		AlertDialog.Builder builder = null;
		builder = new AlertDialog.Builder(this);
		builder.setTitle("分享方式");
		builder.setItems(addPhoto, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {// 分享到微信朋友
					// 获取当前屏幕图片
					Bitmap bmp = BitmapFactory.decodeResource(getResources(),
							R.drawable.send_img);// GetCurrentScreenImage();//BitmapFactory.decodeResource(getResources(),
													// R.drawable.send_img);
					WXImageObject imgObj = new WXImageObject(bmp);

					WXMediaMessage msg = new WXMediaMessage();
					msg.mediaObject = imgObj;

					Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp,
							THUMB_SIZE, THUMB_SIZE, true);
					bmp.recycle();
					msg.thumbData = Util.bmpToByteArray(thumbBmp, true); // 设置缩略图

					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = buildTransaction("img");
					req.message = msg;
					req.scene = SendMessageToWX.Req.WXSceneSession;// SendMessageToWX.Req.WXSceneTimeline;//isTimelineCb.isChecked()
																	// ?
																	// SendMessageToWX.Req.WXSceneTimeline
																	// :
																	// SendMessageToWX.Req.WXSceneSession;
					boolean b = api.sendReq(req);
					if (b) {
						CommonUtils.ShowToastCenter(mContext, "发送成功",
								Toast.LENGTH_LONG);
					}
					dialog.dismiss();
				} else if (which == 1) {// 分享到微信朋友圈
					// 获取当前屏幕图片
					Bitmap bmp = BitmapFactory.decodeResource(getResources(),
							R.drawable.send_img);
					GetCurrentScreenImage();// BitmapFactory.decodeResource(getResources(),
											// R.drawable.send_img);
					WXImageObject imgObj = new WXImageObject(bmp);

					WXMediaMessage msg = new WXMediaMessage();
					msg.mediaObject = imgObj;

					Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp,
							THUMB_SIZE, THUMB_SIZE, true);
					bmp.recycle();
					msg.thumbData = Util.bmpToByteArray(thumbBmp, true); // 设置缩略图

					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = buildTransaction("img");
					req.message = msg;
					req.scene = SendMessageToWX.Req.WXSceneTimeline;// isTimelineCb.isChecked()
																	// ?
																	// SendMessageToWX.Req.WXSceneTimeline
																	// :
																	// SendMessageToWX.Req.WXSceneSession;
					boolean b = api.sendReq(req);
					if (b) {
						CommonUtils.ShowToastCenter(mContext, "发送成功",
								Toast.LENGTH_LONG);
					}
					dialog.dismiss();
				} else if (which == 2) {// 发送短信

					String strText2 = " 详细信息请看http://loveshare.isitestar.cn（大家都在攒人品）";
					String strText = "";
					if (1 == mnType) {
						DynamicDetailActivity activity = (DynamicDetailActivity) mContext;
						strText = "【帮个忙】";
						strText += activity.strItemTitleText;
						strText += "。";
						//判断详细信息是否超过了20个字
						if(activity.strItemDetailText.length() > DETAILMAXCHANUM){
							strText += activity.strItemDetailText.substring(0,DETAILMAXCHANUM);
							strText += "...";
						}else{
							strText += activity.strItemDetailText;
						}
					} else if (2 == mnType) {
						ShareDynamicDetailActivity activity = (ShareDynamicDetailActivity) mContext;
						strText = "【免费分享】";
						strText += activity.strItemTitleText;
						strText += "。";
						//判断详细信息是否超过了20个字
						if(activity.strItemDetailText.length() > DETAILMAXCHANUM){
							strText += activity.strItemDetailText.substring(0,DETAILMAXCHANUM);
							strText += "...";
						}else{
							strText += activity.strItemDetailText;
						}
					}
					strText += strText2;
					Uri smsToUri = Uri.parse("smsto:");

					Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

					intent.putExtra("sms_body", strText);

					startActivity(intent);
				}
			}
		});
		dialog = builder.create();
		dialog.show();
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}
}
