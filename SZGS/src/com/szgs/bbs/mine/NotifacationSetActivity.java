package com.szgs.bbs.mine;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.LoginActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.index.HomeActivity;
import com.szgs.bbs.register.UserMsgResponse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class NotifacationSetActivity extends BaseActivity implements
		OnClickListener {

	private TextView top_left_tv;
	private TextView tv_title;
	private ToggleButton cb_notifa_system;
	private ToggleButton cb_notifa_answerofme;
	private ProgressDialog myProgressDialog;
	private boolean systemisCheck;
	private boolean answerisCheck;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifacation_set);
		initHeaderView();
		initView();
		sendRequest();

	}

	private void initView() {
		cb_notifa_system = (ToggleButton) findViewById(R.id.cb_notifa_system);
		cb_notifa_answerofme = (ToggleButton) findViewById(R.id.cb_notifa_answerofme);
		cb_notifa_system.setOnClickListener(this);
		cb_notifa_answerofme.setOnClickListener(this);
	}

	public void initHeaderView() {
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("通知设置");
	}

	public void sendRequest() {

		AsyncHttpClient client = getClient();
		myProgressDialog = new ProgressDialog(this);
		myProgressDialog.setCancelable(true);
		myProgressDialog.setMessage("正在加载。。。");
		myProgressDialog.show();
		RequestParams params = new RequestParams();
		params.put("userId", CacheUtils.getUserId(NotifacationSetActivity.this));
		String url = Constans.URL + "channels";
		client.get(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				super.onSuccess(statusCode, headers, response);
				try {
					ArrayList<String> list = new ArrayList<String>();
					for (int i = 0; i < response.length(); i++) {
						list.add(response.getString(i));
					}
					if (list.contains("answersOfMe")) {
						cb_notifa_answerofme.setChecked(true);
						answerisCheck = true;
					} else {
						cb_notifa_answerofme.setChecked(false);
						answerisCheck = false;
					}
					if (list.contains("system")) {
						cb_notifa_system.setChecked(true);
						systemisCheck = true;
					} else {
						cb_notifa_system.setChecked(false);
						systemisCheck = false;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				LggsUtils.ShowToast(NotifacationSetActivity.this,
						LggsUtils.replaceAll(responseString));
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFinish() {
				myProgressDialog.dismiss();
				super.onFinish();
			}
		});

	}

	public void sendToggleChannel(String channel, int toggle) {
		AsyncHttpClient client = getClient();
		myProgressDialog = new ProgressDialog(this);
		myProgressDialog.setCancelable(true);
		myProgressDialog.setMessage("正在加载。。。");
		myProgressDialog.show();
		RequestParams params = new RequestParams();
		params.put("userId", CacheUtils.getUserId(NotifacationSetActivity.this));
		params.put("channel", channel);
		params.put("isAdd", toggle);
		String url = Constans.URL + "toggle/channel";
		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				try {
					ArrayList<String> list = new ArrayList<String>();
					for (int i = 0; i < response.length(); i++) {
						list.add(response.getString(i));
					}
					if (list.contains("answersOfMe")) {
						cb_notifa_answerofme.setChecked(true);
						answerisCheck = true;
					} else {
						cb_notifa_answerofme.setChecked(false);
						answerisCheck = false;
					}
					if (list.contains("system")) {
						cb_notifa_system.setChecked(true);
						systemisCheck = true;
					} else {
						cb_notifa_system.setChecked(false);
						systemisCheck = false;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				LggsUtils.ShowToast(NotifacationSetActivity.this,
						LggsUtils.replaceAll(responseString));
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFinish() {
				myProgressDialog.dismiss();
				super.onFinish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_left_tv:
			finish();
			break;
		case R.id.cb_notifa_answerofme:
			int toggle = answerisCheck ? 0 : 1;
			sendToggleChannel("answersOfMe", toggle);
			break;
		case R.id.cb_notifa_system:
			int toggle1 = systemisCheck ? 0 : 1;
			sendToggleChannel("system", toggle1);
			break;
		default:
			break;
		}

	}
}
