package com.szgs.bbs.mine;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.register.UserAgreeMentActivity;

/**
 * 关于问税
 * 
 * @author db
 * 
 */
public class AboutActivity extends BaseActivity implements OnClickListener {

	private TextView top_left_tv;
	private TextView tv_title;
	private TextView about_company;
	private TextView about_versionname;
	private RelativeLayout about_user_agreement;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		initHeaderView();
		initView();
	}

	private void initView() {
		about_company = (TextView) findViewById(R.id.about_company);
		about_company.setText(R.string.dev_company);
		about_versionname = (TextView) findViewById(R.id.about_versionname);
		about_versionname.setText(LggsUtils.getVersionName(this) + "版");
		about_user_agreement = (RelativeLayout) findViewById(R.id.about_user_agreement);
		about_user_agreement.setOnClickListener(this);
	}

	public void initHeaderView() {
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("关于");
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.top_left_tv:
			finish();
			break;
		case R.id.about_user_agreement:
			LggsUtils.StartIntent(AboutActivity.this,
					UserAgreeMentActivity.class);
			break;
		default:
			break;
		}
	}
}
