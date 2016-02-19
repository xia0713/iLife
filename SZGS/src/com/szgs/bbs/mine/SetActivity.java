package com.szgs.bbs.mine;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.LoginActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.common.view.CircleImageView;

public class SetActivity extends BaseActivity implements OnClickListener {

	private TextView top_left_tv;
	private TextView tv_title;
	private RelativeLayout ll_set_user;
	private LinearLayout ll_set_notif;
	private LinearLayout ll_set_comment;
	private LinearLayout ll_set_about;
	private Button btn_exit_user;
	private TextView set_username;
	private CircleImageView myinfo_setting_avatar_iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);
		initHeaderView();
		initView();
	}

	public void initView() {
		ll_set_user = (RelativeLayout) findViewById(R.id.ll_set_user);
		ll_set_notif = (LinearLayout) findViewById(R.id.ll_set_notif);
		// ll_set_comment = (LinearLayout) findViewById(R.id.ll_set_comment);
		ll_set_about = (LinearLayout) findViewById(R.id.ll_set_about);
		btn_exit_user = (Button) findViewById(R.id.btn_exit_user);
		set_username = (TextView) findViewById(R.id.set_username);
		myinfo_setting_avatar_iv = (com.szgs.bbs.common.view.CircleImageView) findViewById(R.id.myinfo_setting_avatar_iv);
		ll_set_user.setOnClickListener(this);
		ll_set_notif.setOnClickListener(this);
		// ll_set_comment.setOnClickListener(this);
		ll_set_about.setOnClickListener(this);
		btn_exit_user.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		ImageLoader.getInstance().displayImage(
				CacheUtils.getAvatar(SetActivity.this),
				myinfo_setting_avatar_iv);
		set_username.setText(CacheUtils.getUserName(SetActivity.this));
		super.onResume();
	}

	public void initHeaderView() {
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("设置");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** 头部返回 */
		case R.id.top_left_tv:
			finish();
			break;

		case R.id.ll_set_user:
			LggsUtils
					.StartIntent(SetActivity.this, MyInfoSettingActivity.class);
			break;
		case R.id.ll_set_about:
			LggsUtils.StartIntent(SetActivity.this, AboutActivity.class);
			break;
		case R.id.btn_exit_user:
			LggsUtils.showConfirmExitDialog(SetActivity.this, "温馨提示",
					"您确定要退出当前帐号？", new OnClickListener() {

						@Override
						public void onClick(View v) {
							CacheUtils.cleanUserMsg(SetActivity.this);
							Bundle bundle = new Bundle();
							bundle.putString("soyoulla", "exit");
							LggsUtils.StartIntent(SetActivity.this,
									LoginActivity.class, bundle);
							finish();
						}
					}, null, true, false);

			break;
		case R.id.ll_set_notif:
			LggsUtils.StartIntent(SetActivity.this,
					NotifacationSetActivity.class);
			break;
		default:
			break;
		}

	}
}
