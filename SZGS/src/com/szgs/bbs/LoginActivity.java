package com.szgs.bbs;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.common.util.SharedPranceUtils;
import com.szgs.bbs.index.HomeActivity;
import com.szgs.bbs.mine.NotificationActivity;
import com.szgs.bbs.register.ForgotPwdActivity;
import com.szgs.bbs.register.RegisterActivity;
import com.szgs.bbs.register.UserMsgResponse;

/**
 * 登录
 * 
 * @author db
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private EditText et_login_phone;
	private EditText et_login_pwd;
	private CheckBox cb_login_rbpwd;
	private Button btn_login;
	private TextView tv_login_fgtpwd;
	private TextView tv_login_register;
	private ProgressDialog myProgressDialog;
	private String telphoone;
	private String password;
	private TextView tv_title;
	private TextView top_left_tv;
	private String FILENAME = "andputname";
	private String soyoulla;
	private LinearLayout ll_login_cb;
	protected String installationId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		soyoulla = getIntent().getStringExtra("soyoulla");
		installationId = SharedPranceUtils.GetStringDate(installationId,
				LoginActivity.this);
		initHeaderView();
		onInitViews();
		onInitData();
		onInitListener();
	}

	public void initHeaderView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("登录");
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setText("取消");
		if (soyoulla == null) {
			top_left_tv.setVisibility(View.VISIBLE);
		} else {
			top_left_tv.setVisibility(View.GONE);
		}
		top_left_tv.setOnClickListener(this);
	}

	public void onInitViews() {
		et_login_phone = (EditText) findViewById(R.id.et_login_phone);
		et_login_pwd = (EditText) findViewById(R.id.et_login_pwd);
		cb_login_rbpwd = (CheckBox) findViewById(R.id.cb_login_rbpwd);
		btn_login = (Button) findViewById(R.id.btn_login);
		tv_login_fgtpwd = (TextView) findViewById(R.id.tv_login_fgtpwd);
		tv_login_register = (TextView) findViewById(R.id.tv_login_register);
		ll_login_cb = (LinearLayout) findViewById(R.id.ll_login_cb);
		ll_login_cb.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		tv_login_fgtpwd.setOnClickListener(this);
		tv_login_register.setOnClickListener(this);
		// cb_login_rbpwd.setChecked(CacheUtils
		// .getIsRememberPwd(LoginActivity.this));
		// if (CacheUtils.getIsRememberPwd(LoginActivity.this)) {
		// et_login_phone.setText(CacheUtils.getUserName(LoginActivity.this));
		// et_login_pwd.setText(CacheUtils.getPassWord(LoginActivity.this));
		// sendLoginRequest();
		// }
		/** 推送注册 */
		PushService.setDefaultPushCallback(LoginActivity.this,
				NotificationActivity.class);
		installationId = AVInstallation
				.getCurrentInstallation()
				.getInstallationId();
//		SharedPranceUtils.SaveStringDate(WelcomeActivity.this, installationId, "installationId");
	
	}

	public void onInitListener() {
	}

	public void onInitData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_left_tv:
			if (soyoulla != null) {
				LggsApplication.getInstance().exit();
			} else {
				finish();
			}
			break;
		case R.id.btn_login:
			// LggsUtils.StartIntent(LoginActivity.this, HomeActivity.class);
			doLogin();
			break;
		case R.id.tv_login_register:
			LggsUtils.StartIntent(LoginActivity.this, RegisterActivity.class);
			break;
		case R.id.tv_login_fgtpwd:
			LggsUtils.StartIntent(LoginActivity.this, ForgotPwdActivity.class);
			break;
		case R.id.ll_login_cb:
			cb_login_rbpwd.toggle();
			break;

		default:
			break;
		}

	}

	private void doLogin() {
		telphoone = et_login_phone.getText().toString();
		password = et_login_pwd.getText().toString();
		if (TextUtils.isEmpty(telphoone)) {
			LggsUtils.ShowToast(LoginActivity.this, "请输入您的电话号码");
		} else if (!LggsUtils.isMobileNum(telphoone)) {
			LggsUtils.ShowToast(LoginActivity.this, "请输入正确的手机号码");
		} else if (TextUtils.isEmpty(password)) {
			LggsUtils.ShowToast(LoginActivity.this, "请输入您的密码");
		} else {
			sendLoginRequest();
		}
	}

	public void sendLoginRequest() {

		AsyncHttpClient client = new AsyncHttpClient();
		myProgressDialog = new ProgressDialog(this);
		myProgressDialog.setCancelable(true);
		myProgressDialog.setMessage("正在登录。。。");
		myProgressDialog.show();
		RequestParams params = new RequestParams();
		params.put("mobilePhone", et_login_phone.getText().toString());
		params.put("password", et_login_pwd.getText().toString());
		params.put("deviceType", "android");
		params.put("deviceId", installationId);
		String url = Constans.URL + "signin";
		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				Header Authorization = headers[1];
				String author = Authorization.getValue();
				CacheUtils.saveAuthor(getApplicationContext(), author);
				Gson gson = new Gson();
				if (statusCode == 200) {
					LggsUtils.ShowToast(LoginActivity.this, "登录成功");
					UserMsgResponse userresponse = gson.fromJson(
							response.toString(), UserMsgResponse.class);
					long userId = userresponse.id;
					CacheUtils.saveUserID(LoginActivity.this, userId);
					CacheUtils.saveIsRememberPwd(LoginActivity.this,
							cb_login_rbpwd.isChecked());
					CacheUtils.saveAvatar(LoginActivity.this,
							userresponse.avatar);
					if (userresponse.nickname != null) {
						CacheUtils.saveUserName(LoginActivity.this,
								userresponse.nickname);
					} else {
						CacheUtils.saveUserName(LoginActivity.this,
								et_login_phone.getText().toString());
					}
					if (cb_login_rbpwd.isChecked()) {
						CacheUtils.saveTelphone(LoginActivity.this,
								et_login_phone.getText().toString());
						CacheUtils.savePassWord(LoginActivity.this,
								et_login_pwd.getText().toString());
					}
					LggsUtils.StartIntent(LoginActivity.this,
							HomeActivity.class);
					finish();
				} else {
					LggsUtils.ShowToast(LoginActivity.this, "服务器异常，登录失败！");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(LoginActivity.this, "服务器异常，登录失败！");
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				LggsUtils.ShowToast(LoginActivity.this,
						LggsUtils.replaceAll(responseString));
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				LggsUtils.ShowToast(LoginActivity.this, "服务器异常，登录失败！");
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFinish() {
				myProgressDialog.dismiss();
				super.onFinish();
			}
		});

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		if (soyoulla != null) {
			LggsApplication.getInstance().exit();
		} else {
			finish();
		}
		super.onBackPressed();
	}
}
