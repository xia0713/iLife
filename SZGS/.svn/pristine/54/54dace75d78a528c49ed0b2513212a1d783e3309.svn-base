package com.szgs.bbs.register;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.LoginActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.interfaces.Initialization;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;

public class ForgotPwdSecondActivity extends BaseActivity implements
		OnClickListener {

	private EditText et_forgot_pwd;
	private EditText et_forgot_pwdagain;
	private Button btn_forgotcomplete;
	private TextView top_title;
	private TextView top_left_tv;
	private String code;
	private ProgressDialog myProgressDialog;
	private String telphone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_pwd_second);
		code = getIntent().getStringExtra("code");
		telphone = getIntent().getStringExtra("telphone");
		initHeaderView();
		onInitViews();
		onInitData();
		onInitListener();
	}

	public void onInitViews() {
		et_forgot_pwd = (EditText) findViewById(R.id.et_forgot_pwd);
		et_forgot_pwdagain = (EditText) findViewById(R.id.et_forgot_pwdagain);
		btn_forgotcomplete = (Button) findViewById(R.id.btn_forgotcomplete);
	}

	public void onInitListener() {
		btn_forgotcomplete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(et_forgot_pwd.getText().toString())
						&& TextUtils.isEmpty(et_forgot_pwdagain.getText()
								.toString())) {
					LggsUtils.ShowToast(ForgotPwdSecondActivity.this, "密码不能为空");
				} else if (!et_forgot_pwd.getText().toString()
						.equals(et_forgot_pwdagain.getText().toString())) {
					LggsUtils.ShowToast(ForgotPwdSecondActivity.this,
							"两次密码输入不一致");
				} else {
					sendrequest();
				}

			}

		});

	}

	public void sendrequest() {

		AsyncHttpClient client = new AsyncHttpClient();
		myProgressDialog = new ProgressDialog(this);
		myProgressDialog.setCancelable(true);
		myProgressDialog.setMessage("正在加载。。。");
		myProgressDialog.show();
		RequestParams params = new RequestParams();
		params.put("mobilePhone", telphone);
		params.put("code", code);
		params.put("password", et_forgot_pwd.getText().toString());
		String url = Constans.URL + "user/password/reset";
		client.setTimeout(5000);
		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				myProgressDialog.dismiss();
				Gson gson = new Gson();
				if (statusCode == 200) {

				} else {
					LggsUtils.ShowToast(ForgotPwdSecondActivity.this,
							"服务器异常，注册失败！");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				myProgressDialog.dismiss();
				LggsUtils
						.ShowToast(ForgotPwdSecondActivity.this, "服务器异常，注册失败！");
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				myProgressDialog.dismiss();
				LggsUtils.ShowToast(ForgotPwdSecondActivity.this,
						responseString);
				if (statusCode == 200) {
					LggsUtils.ShowToast(ForgotPwdSecondActivity.this, "重置密码成功");
					LggsUtils.StartIntent(ForgotPwdSecondActivity.this,
							LoginActivity.class);
					finish();
				}

				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				myProgressDialog.dismiss();
				LggsUtils
						.ShowToast(ForgotPwdSecondActivity.this, "服务器异常，注册失败！");
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});

	}

	public void onInitData() {
		// TODO Auto-generated method stub

	}

	/**
	 * 初始化头部布局
	 */
	public void initHeaderView() {
		top_title = (TextView) findViewById(R.id.tv_title);
		top_title.setText("确认新密码");
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
	}

	public void LeftAction(View v) {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_left_tv:
			finish();
			break;

		default:
			break;
		}

	}
}
