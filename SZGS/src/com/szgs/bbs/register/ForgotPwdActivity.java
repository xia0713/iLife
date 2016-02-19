package com.szgs.bbs.register;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.LoginActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.interfaces.Initialization;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.register.RegisterActivity.MyCount;

public class ForgotPwdActivity extends BaseActivity implements OnClickListener {

	private EditText et_forgot_telphone;
	private EditText et_forgot_yzm;
	private Button btn_forgot_yzm;
	private MyCount count;
	private Button btn_forgotnext;
	private TextView tv_title;
	private TextView top_left_tv;
	private EditText et_forgot_pwd;
	private EditText et_forgot_pwdagain;
	private ProgressDialog myProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_pwd);
		initHeaderView();
		onInitViews();
		onInitListener();
		onInitData();
	}

	public void initHeaderView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("忘记密码");
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setText("取消");
		top_left_tv.setOnClickListener(this);
	}

	public void onInitViews() {
		et_forgot_telphone = (EditText) findViewById(R.id.et_forgot_telphone);
		et_forgot_yzm = (EditText) findViewById(R.id.et_forgot_yzm);
		et_forgot_pwd = (EditText) findViewById(R.id.et_forgot_pwd);
		et_forgot_pwdagain = (EditText) findViewById(R.id.et_forgot_pwdagain);
		btn_forgot_yzm = (Button) findViewById(R.id.btn_forgot_yzm);
		btn_forgotnext = (Button) findViewById(R.id.btn_forgotnext);
	}

	private void toGetCode() {
		if (TextUtils.isEmpty(et_forgot_telphone.getText().toString())) {
			LggsUtils.ShowToast(ForgotPwdActivity.this, "请输入您的电话号码");
		} else if (!LggsUtils.isMobileNum(et_forgot_telphone.getText()
				.toString())) {
			LggsUtils.ShowToast(ForgotPwdActivity.this, "请输入正确的手机号码");
		} else {
			sendCode(et_forgot_telphone.getText().toString());
		}

	}

	/**
	 * 获取验证码
	 * 
	 * @param phone
	 */
	public void sendCode(final String phone) {
		new AsyncTask<Void, Void, Void>() {
			boolean res;

			@Override
			protected Void doInBackground(Void... params) {
				try {
					AVOSCloud.requestSMSCode(phone);
					res = true;
				} catch (AVException e) {
					e.printStackTrace();
					res = false;
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				super.onPostExecute(aVoid);
				if (res) {
					count.start();
					LggsUtils.ShowToast(ForgotPwdActivity.this, "获取验证码成功");
				} else {
					LggsUtils.ShowToast(ForgotPwdActivity.this, "获取验证码失败");
				}
			}
		}.execute();
	}

	/**
	 * 验证验证码
	 * 
	 * @param code
	 */
	private void verifyCode(final String code) {
		AVOSCloud.verifySMSCodeInBackground(code, et_forgot_telphone.getText()
				.toString(), new AVMobilePhoneVerifyCallback() {
			@Override
			public void done(AVException e) {
				if (e == null) {

				} else {
					e.printStackTrace();
					LggsUtils.ShowToast(ForgotPwdActivity.this, "验证失败");
				}
			}
		});
	}

	public void onInitListener() {
		btn_forgot_yzm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toGetCode();

			}
		});
		btn_forgotnext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(et_forgot_telphone.getText().toString())) {
					LggsUtils.ShowToast(ForgotPwdActivity.this, "请输入您的电话号码");
				} else if (TextUtils
						.isEmpty(et_forgot_yzm.getText().toString())) {
					LggsUtils.ShowToast(ForgotPwdActivity.this, "请输入您的验证码");
				} else if (TextUtils
						.isEmpty(et_forgot_pwd.getText().toString())
						&& TextUtils.isEmpty(et_forgot_pwdagain.getText()
								.toString())) {
					LggsUtils.ShowToast(ForgotPwdActivity.this, "请输入您的密码");
				} else if (!et_forgot_pwd.getText().toString()
						.equals(et_forgot_pwdagain.getText().toString())) {
					LggsUtils.ShowToast(ForgotPwdActivity.this, "两次密码输入不一致");
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
		params.put("mobilePhone", et_forgot_telphone.getText().toString());
		params.put("code", et_forgot_yzm.getText().toString());
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
					LggsUtils.ShowToast(ForgotPwdActivity.this, "服务器异常，注册失败！");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				myProgressDialog.dismiss();
				LggsUtils.ShowToast(ForgotPwdActivity.this, "服务器异常，注册失败！");
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				myProgressDialog.dismiss();
				LggsUtils.ShowToast(ForgotPwdActivity.this, responseString);
				if (statusCode == 200) {
					LggsUtils.ShowToast(ForgotPwdActivity.this, "重置密码成功");
					LggsUtils.StartIntent(ForgotPwdActivity.this,
							LoginActivity.class);
					finish();
				}

				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				myProgressDialog.dismiss();
				LggsUtils.ShowToast(ForgotPwdActivity.this, "服务器异常，注册失败！");
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});

	}

	public void onInitData() {
		count = new MyCount(60000, 1000);

	}

	/* 定义一个倒计时的内部类 */
	class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			// 完成
			btn_forgot_yzm
					.setBackgroundResource(R.drawable.blue_button_nopadding);
			btn_forgot_yzm.setClickable(true);
			btn_forgot_yzm.setText("重新获取");
		}

		@Override
		public void onTick(long millisUntilFinished) {
			btn_forgot_yzm.setClickable(false);
			btn_forgot_yzm
					.setBackgroundResource(R.drawable.gray_button_nopadding);
			btn_forgot_yzm.setText(millisUntilFinished / 1000 + "s后重发");
		}
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
