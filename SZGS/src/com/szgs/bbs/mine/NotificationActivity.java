package com.szgs.bbs.mine;

import java.util.ArrayList;
import java.util.List;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.avos.avoscloud.AVOSCloud;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.common.util.SharedPranceUtils;
import com.szgs.bbs.common.view.FlowPagerAdapter;

/**
 * 通知页面
 * 
 * @author db
 * 
 */
public class NotificationActivity extends BaseActivity {

	private RadioGroup notification_radio_group;
	private ViewPager notification_vp;
	private FlowPagerAdapter mFlowPagerAdapter;
	private TextView tv_title;
	private TextView top_left_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		initHeaderView();
		initView();
		initListener();
		initData();
		SharedPranceUtils.SaveBolDate(NotificationActivity.this, false,
				"hasnewmsg");
		NotificationManager notifiction=(NotificationManager) AVOSCloud.applicationContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notifiction.cancel(10086);
	}

	private void initHeaderView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("通知");
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	public void initView() {
		notification_radio_group = (RadioGroup) findViewById(R.id.notification_radio_group);
		notification_vp = (ViewPager) findViewById(R.id.notification_vp);
		notification_vp.setOffscreenPageLimit(2);
	}

	public void initListener() {
		notification_vp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int pos) {
				switch (pos) {
				case 0:
					// 回答我的
					// setSwipeBackEnable(true);
					notification_radio_group.check(R.id.notification_answer_rb);
					break;
				/*
				 * case 1: // setSwipeBackEnable(false);
				 * notification_radio_group
				 * .check(R.id.notification_comment_rb); break;
				 */
				case 1:
					// 系统
					// setSwipeBackEnable(false);
					notification_radio_group.check(R.id.notification_system_rb);
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		notification_radio_group
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {

						int radioButtonId = arg0.getCheckedRadioButtonId();
						switch (radioButtonId) {
						case R.id.notification_answer_rb:
							notification_vp.setCurrentItem(0);
							break;
						// case R.id.notification_comment_rb:
						// notification_vp.setCurrentItem(1);
						// break;
						case R.id.notification_system_rb:
							notification_vp.setCurrentItem(1);
							break;
						}
					}
				});
	}

	public void initData() {
		mFlowPagerAdapter = new FlowPagerAdapter(
				this.getSupportFragmentManager());
		List<Fragment> tFragments = new ArrayList<Fragment>(1);
		// 回答我的
		ResponseMeFragment responseMeFragment = new ResponseMeFragment();
		tFragments.add(responseMeFragment);
		// // 评论我的
		// CommentFragment commentFragment = new CommentFragment();
		// tFragments.add(commentFragment);
		// 系统
		SystemFragment systemFragment = new SystemFragment();
		tFragments.add(systemFragment);

		mFlowPagerAdapter.setFragments(tFragments);
		notification_vp.setAdapter(mFlowPagerAdapter);
	}

}
