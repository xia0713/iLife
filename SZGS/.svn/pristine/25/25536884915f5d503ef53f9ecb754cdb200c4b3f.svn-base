package com.szgs.bbs.register;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;

public class UserAgreeMentActivity extends BaseActivity {

	private TextView top_left_tv;
	private TextView tv_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_agree_ment);
		initHeaderView();
	}

	public void initHeaderView() {
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("问税你我帮用户使用协议");
	}
}
