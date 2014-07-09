package com.renpin.renpincustomer;

import com.renpin.myphotoview.PhotoView;
import com.renpin.renpincustomer.CommentActivity.SamplePagerAdapter;
import com.renpin.utils.CommonUtils;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

public class TaskDetailShowLargeImageActivity extends Activity {
	private ViewPager mViewPager;
	private static CommonUtils mUtils = null;
	private static String strCommentImage = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 程序启动的时候避免光标处在editview中而弹出输入法窗口
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		int nCommentsIndex = getIntent().getIntExtra("nCommentIndex", -1);

		strCommentImage = TaskDetailActivity.CommentMaps.get(nCommentsIndex
				+ "");

		// 对图片数据进行排序

		if (null == mUtils) {
			// 创建公共操作类对象
			mUtils = new CommonUtils(this);
		}

		mViewPager = new HackyViewPager(this);
		mViewPager.setBackgroundColor(Color.rgb(0, 0, 0));
		setContentView(mViewPager);

		mViewPager.setAdapter(new SamplePagerAdapter());
	}

	static class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return 1;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			if (mUtils != null) {
				photoView
						.setImageBitmap(mUtils.base64ToBitmap(strCommentImage));
			}
			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
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
