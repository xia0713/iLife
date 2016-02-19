package com.szgs.bbs.ui;

import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.LoginActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.index.HomeActivity;
import com.szgs.bbs.register.RegisterActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class SelectLoginorRegisActivity extends BaseActivity implements
		OnClickListener {

	private LinearLayout not_loginin;
	private Button btn_register;
	private Button btn_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_loginor_regis);
		initView();
	}

	public void initView() {
		not_loginin = (LinearLayout) findViewById(R.id.not_loginin);
		not_loginin.setOnClickListener(this);
		btn_register = (Button) findViewById(R.id.btn_register);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		btn_register.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.not_loginin:
			LggsUtils.StartIntent(SelectLoginorRegisActivity.this,
					HomeActivity.class);
			finish();
			break;
		case R.id.btn_register:
			LggsUtils.StartIntent(SelectLoginorRegisActivity.this,
					RegisterActivity.class);
			break;
		case R.id.btn_login:
			LggsUtils.StartIntent(SelectLoginorRegisActivity.this,
					LoginActivity.class);
			break;
		default:
			break;
		}

	}
}
