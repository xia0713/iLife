package com.szgs.bbs.daohang;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.LoginActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.common.util.SharedPranceUtils;
import com.szgs.bbs.ui.SelectLoginorRegisActivity;

/**
 * 第一次进入应用的导航页面
 */
public class StartActivity extends BaseActivity {

	private ViewPager viewPager;

	private ViewPagerAdapter vpAdapter;

	private ArrayList<View> views;

	private View view1, view2, view3;

	private ImageView pointImage0, pointImage1, pointImage2;

	private int currIndex = 0;
	private int currentPageScrollStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		initView();
		initData();
	}

	/**
	 * 初始界面
	 */
	private void initView() {
		LayoutInflater mLi = LayoutInflater.from(this);
		view1 = mLi.inflate(R.layout.guide_view01, null);
		view2 = mLi.inflate(R.layout.guide_view02, null);
		view3 = mLi.inflate(R.layout.guide_view03, null);
		viewPager = (ViewPager) findViewById(R.id.viewpager);

		views = new ArrayList<View>();

		vpAdapter = new ViewPagerAdapter(views);

		pointImage0 = (ImageView) findViewById(R.id.page0);
		pointImage1 = (ImageView) findViewById(R.id.page1);
		pointImage2 = (ImageView) findViewById(R.id.page2);

		Button button_view = (Button) view3.findViewById(R.id.button_view);
		button_view.setLongClickable(true);
		button_view.setOnTouchListener(new MyGestureListener(this));
	}

	private class MyGestureListener extends GestureListener {
		public MyGestureListener(Context context) {
			super(context);
		}

		@Override
		public boolean left() {
			Intent intent = new Intent();
			intent.setClass(StartActivity.this,
					SelectLoginorRegisActivity.class);
			startActivity(intent);
			StartActivity.this.finish();
			return true;
		}

		@Override
		public boolean right() {
			return super.right();
		}
	}

	public void StratAction(View v) {
		Intent intent = new Intent();
		intent.setClass(StartActivity.this, SelectLoginorRegisActivity.class);
		startActivity(intent);
		this.finish();
	}

	private void initData() {
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setAdapter(vpAdapter);
		views.add(view1);
		views.add(view2);
		views.add(view3);
		vpAdapter.notifyDataSetChanged();
		SharedPranceUtils.SaveBolDate(this, false, "isFirstIn");

	}

	class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int position) {
			switch (position) {
			case 0:
				pointImage0.setImageDrawable(getResources().getDrawable(
						R.drawable.circle02));
				pointImage1.setImageDrawable(getResources().getDrawable(
						R.drawable.circle01));
				pointImage2.setImageDrawable(getResources().getDrawable(
						R.drawable.circle01));
				break;
			case 1:
				pointImage0.setImageDrawable(getResources().getDrawable(
						R.drawable.circle01));
				pointImage1.setImageDrawable(getResources().getDrawable(
						R.drawable.circle02));
				pointImage2.setImageDrawable(getResources().getDrawable(
						R.drawable.circle01));
				break;
			case 2:
				pointImage0.setImageDrawable(getResources().getDrawable(
						R.drawable.circle01));
				pointImage1.setImageDrawable(getResources().getDrawable(
						R.drawable.circle01));
				pointImage2.setImageDrawable(getResources().getDrawable(
						R.drawable.circle02));
				break;
			}
			currIndex = position;

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			currentPageScrollStatus = arg0;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if (currIndex == 4) {
				if (arg2 == 0 && currentPageScrollStatus == 1) {
					LggsUtils.StartIntent(StartActivity.this,
							SelectLoginorRegisActivity.class);
					StartActivity.this.finish();
				}
			}
		}
	}

}