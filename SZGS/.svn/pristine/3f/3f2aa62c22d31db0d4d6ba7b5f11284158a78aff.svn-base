package com.szgs.bbs.answer;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.common.view.FlowPagerAdapter;

/**
 * 排行表
 * 
 * @author db
 * 
 */
public class RankListActivity extends BaseActivity {

	private TextView tv_title;
	private TextView top_left_tv;
	private RadioGroup rank_radio_group;
	private ViewPager rank_vp;
	private FlowPagerAdapter mFlowPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rank_list);

		initHeaderView();
		initView();
		initData();
		initListener();
	}

	private void initHeaderView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("排行榜");
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

		rank_radio_group = (RadioGroup) findViewById(R.id.rank_radio_group);
		rank_vp = (ViewPager) findViewById(R.id.rank_vp);
	}

	public void initListener() {
		rank_vp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int pos) {
				switch (pos) {
				case 0:
					// 待办
					// setSwipeBackEnable(true);
					rank_radio_group.check(R.id.rank_day_rb);
					break;
				case 1:
					// 已办
					// setSwipeBackEnable(false);
					rank_radio_group.check(R.id.rank_month_rb);
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

		rank_radio_group
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {

						int radioButtonId = arg0.getCheckedRadioButtonId();
						switch (radioButtonId) {
						case R.id.rank_day_rb:
							rank_vp.setCurrentItem(0);
							break;
						case R.id.rank_month_rb:
							rank_vp.setCurrentItem(1);
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
		RankDayFragment rankDayFragment = new RankDayFragment();
		tFragments.add(rankDayFragment);
		// 评论我的
		RankMonthFragment rankMonthFragment = new RankMonthFragment();
		tFragments.add(rankMonthFragment);
		mFlowPagerAdapter.setFragments(tFragments);
		rank_vp.setAdapter(mFlowPagerAdapter);
		mFlowPagerAdapter.notifyDataSetChanged();
	}

}