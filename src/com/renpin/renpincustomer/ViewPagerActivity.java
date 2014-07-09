package com.renpin.renpincustomer;

import com.renpin.myphotoview.PhotoView;
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

public class ViewPagerActivity extends Activity {

	private ViewPager mViewPager;
	private static CommonUtils mUtils = null;
	//private static String[] strIconsTemp = new String[6];

	static int mnCount = 0;
	static int nType = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mnCount = 0;

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// ����������ʱ������괦��editview�ж��������뷨����
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		int nPos = getIntent().getIntExtra("iconposion", -1);
		nType = getIntent().getIntExtra("type", -1);
		if (1 == nType) {
			// ���ж��м���ͼƬ
			for (int i = 0; i < DynamicDetailActivity.strIcons.length; i++) {
				if (DynamicDetailActivity.strIcons[i].length() > 100) {
					//strIconsTemp[mnCount] = DynamicDetailActivity.strIcons[i];
					mnCount++;
				}
			}
		} else if (2 == nType) {
			// ���ж��м���ͼƬ
			for (int i = 0; i < ShareDynamicDetailActivity.strIcons.length; i++) {
				if (ShareDynamicDetailActivity.strIcons[i].length() > 100) {
					//strIconsTemp[mnCount] = ShareDynamicDetailActivity.strIcons[i];
					mnCount++;
				}
			}
		}

		// ��ͼƬ���ݽ�������
		if (null == mUtils) {
			// �����������������
			mUtils = new CommonUtils(this);
		}

		mViewPager = new HackyViewPager(this);
		mViewPager.setBackgroundColor(Color.rgb(0, 0, 0));
		setContentView(mViewPager);

		mViewPager.setAdapter(new SamplePagerAdapter());
		mViewPager.setCurrentItem(nPos);
	}

	static class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mnCount;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			if (mUtils != null) {
				if(1 == nType){
					photoView.setImageBitmap(mUtils
							.base64ToBitmap(DynamicDetailActivity.strIcons[position]));	
				}else if(2 == nType){
					photoView.setImageBitmap(mUtils
							.base64ToBitmap(ShareDynamicDetailActivity.strIcons[position]));
				}
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
