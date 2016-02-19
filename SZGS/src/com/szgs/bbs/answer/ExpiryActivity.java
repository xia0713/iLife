package com.szgs.bbs.answer;

import com.szgs.bbs.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 积分兑换
 * 
 * @author db
 * 
 */
public class ExpiryActivity extends Activity {

	private TextView tv_title;
	private TextView top_left_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expiry);
		initHeaderView();
	}

	public void initHeaderView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_title.setText("积分兑换");
	}
}
