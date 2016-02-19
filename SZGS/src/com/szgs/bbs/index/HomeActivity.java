package com.szgs.bbs.index;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.LoginActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.answer.AnswerFragment;
import com.szgs.bbs.ask.AskFirstActivity;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.common.view.CustomViewPager;
import com.szgs.bbs.find.FindFragment;
import com.szgs.bbs.mine.MineFragment;
import com.szgs.bbs.mine.NotificationActivity;

/**
 * 首页
 * 
 * @author db
 * 
 */
public class HomeActivity extends BaseActivity implements
		OnCheckedChangeListener {

	private RadioButton index_radio, mine_radio, answer_radio, find_radio;
	private RadioGroup bottom_radiogroup;

	private CustomViewPager pager;
	private HomePagerAdapter adapter;

	private HomeFragment homeFragment;
	private FindFragment findFragment;
	private AnswerFragment answerFragment;
	private MineFragment mineFragment;

	private RadioButton checkedButton;// 当前checked的button

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initBottomView();
		initView();
		PushService.setDefaultPushCallback(HomeActivity.this,
				NotificationActivity.class);
		String installationId = AVInstallation
				.getCurrentInstallation()
				.getInstallationId();
		if(CacheUtils.getUserId(getApplicationContext())!=-1){
			sendAutoLoginRequest();
		}
	}

	@Override
	protected void onResume() {
		checkedButton.setChecked(true);
		super.onResume();
	}

	/**
	 * 返回事件
	 */
	@Override
	public void onBackPressed() {
		LggsUtils.onBackPressed(this);
	}

	public void initView() {
		pager = (CustomViewPager) findViewById(R.id.pager);
		pager.isScrollable(false);
		adapter = new HomePagerAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(3);
		// adapter.addFragment(new HomeFragment(), "HomeFragmet");
		Log.i("fdsgd", "asdf");
		homeFragment = new HomeFragment(HomeActivity.this);
		setPagerFragment(index_radio, homeFragment, "HomeFragmet");
		index_radio.setChecked(true);
		storeCheckedInfo(index_radio);
	}

	public void initBottomView() {
		index_radio = (RadioButton) findViewById(R.id.index_radio);
		mine_radio = (RadioButton) findViewById(R.id.mine_radio);
		answer_radio = (RadioButton) findViewById(R.id.answer_radio);
		find_radio = (RadioButton) findViewById(R.id.find_radio);
		bottom_radiogroup = (RadioGroup) findViewById(R.id.footer_layout);
		bottom_radiogroup.setOnCheckedChangeListener(this);
	}

	private void setPagerFragment(RadioButton radio, Fragment fragment,
			String fragmentName) {
		// if (!radio.isChecked()) {
		// radio.setChecked(true);
		int id = adapter.getFragmentsIdByName(fragmentName);
		if (id >= 0) {
			pager.setCurrentItem(id, false);
		} else {
			adapter.addFragment(fragment, fragmentName);
			adapter.notifyDataSetChanged();
			// int ddd = adapter.getFragmentsIdByName(fragmentName);
			pager.setCurrentItem(adapter.getFragmentsIdByName(fragmentName),
					false);
		}
	}

	private void storeCheckedInfo(RadioButton btn) {
		checkedButton = btn;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.index_radio:
			if (homeFragment == null) {
				homeFragment = new HomeFragment(HomeActivity.this);
			}
			setPagerFragment(index_radio, homeFragment, "HomeFragmet");
			storeCheckedInfo(index_radio);
			break;
		case R.id.mine_radio:
			if (CacheUtils.getUserId(HomeActivity.this) != -1) {
				if (mineFragment == null) {
					mineFragment = new MineFragment(HomeActivity.this);
				}
				setPagerFragment(mine_radio, mineFragment, "MineFragment");
				storeCheckedInfo(mine_radio);
			} else {
				LggsUtils.StartIntent(HomeActivity.this, LoginActivity.class);
			}

			break;
		case R.id.ask_radio:
			if (CacheUtils.getUserId(HomeActivity.this) != -1) {
				if (homeFragment == null) {
					homeFragment = new HomeFragment(HomeActivity.this);
				}
				homeFragment.setIstoAsk(true);
				LggsUtils
						.StartIntent(HomeActivity.this, AskFirstActivity.class);
			} else {
				LggsUtils.StartIntent(HomeActivity.this, LoginActivity.class);
			}
			break;
		case R.id.answer_radio:
			if (CacheUtils.getUserId(HomeActivity.this) != -1) {
				if (answerFragment == null) {
					answerFragment = new AnswerFragment(HomeActivity.this);
				}
				setPagerFragment(answer_radio, answerFragment, "AnswerFragmet");
				storeCheckedInfo(answer_radio);
			} else {
				LggsUtils.StartIntent(HomeActivity.this, LoginActivity.class);
			}

			break;
		case R.id.find_radio:
			if (findFragment == null) {
				findFragment = new FindFragment(HomeActivity.this);
			}
			setPagerFragment(find_radio, findFragment, "FindFragmet");
			storeCheckedInfo(find_radio);
			break;
		default:
			break;
		}

	}
}
