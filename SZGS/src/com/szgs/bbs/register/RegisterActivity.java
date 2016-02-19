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
import com.szgs.bbs.R;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.interfaces.Initialization;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;

/**
 * 注册
 * 
 * @author db
 * 
 */
public class RegisterActivity extends BaseActivity implements Initialization,
		OnClickListener {

	private EditText et_register_tel;
	private EditText et_register_yzm;
	private Button btn_register_yzm;
	private Button btn_register;
	private TextView tv_register_agreement;
	private TextView tv_register_tologin;
	private MyCount count;
	private EditText et_register_pwd;
	private EditText et_register_pwdagain;
	private ProgressDialog myProgressDialog;
	private String register_tel;
	private String register_pwd;
	private String register_pwdagain;
	private TextView tv_title;
	private TextView top_left_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initHeaderView();
		onInitViews();
		onInitData();
		onInitListener();
	}

	public void initHeaderView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("注册");
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setText("取消");
		top_left_tv.setOnClickListener(this);
	}

	@Override
	public void onInitViews() {
		et_register_pwd = (EditText) findViewById(R.id.et_register_pwd);
		et_register_pwdagain = (EditText) findViewById(R.id.et_register_pwdagain);
		et_register_tel = (EditText) findViewById(R.id.et_register_tel);
		et_register_yzm = (EditText) findViewById(R.id.et_register_yzm);
		btn_register_yzm = (Button) findViewById(R.id.btn_register_yzm);
		btn_register = (Button) findViewById(R.id.btn_register);
		tv_register_agreement = (TextView) findViewById(R.id.tv_register_agreement);
		tv_register_tologin = (TextView) findViewById(R.id.tv_register_tologin);
		btn_register_yzm.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		tv_register_agreement.setOnClickListener(this);
		tv_register_tologin.setOnClickListener(this);
	}

	@Override
	public void onInitListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitData() {
		count = new MyCount(60000, 1000);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_left_tv:
			RegisterActivity.this.finish();
			break;
		case R.id.btn_register_yzm:
			toGetCode();
			break;
		case R.id.btn_register:
			toRegister();
			break;
		case R.id.tv_register_agreement:
			LggsUtils.StartIntent(RegisterActivity.this,
					UserAgreeMentActivity.class);
			break;
		case R.id.tv_register_tologin:
			RegisterActivity.this.finish();
			break;
		default:
			break;
		}

	}

	private void toGetCode() {
		if (TextUtils.isEmpty(et_register_tel.getText().toString())) {
			LggsUtils.ShowToast(RegisterActivity.this, "请输入您的电话号码");
		} else if (!LggsUtils.isMobileNum(et_register_tel.getText().toString())) {
			LggsUtils.ShowToast(RegisterActivity.this, "请输入正确的手机号码");
		} else {
			sendCode(et_register_tel.getText().toString());
		}

	}

	private void toRegister() {
		register_tel = et_register_tel.getText().toString();
		register_pwd = et_register_pwd.getText().toString();
		register_pwdagain = et_register_pwdagain.getText().toString();

		if (TextUtils.isEmpty(register_tel)) {
			LggsUtils.ShowToast(RegisterActivity.this, "请输入您的电话号码");
		} else if (!LggsUtils.isMobileNum(register_tel)) {
			LggsUtils.ShowToast(RegisterActivity.this, "请输入正确的手机号码");
		} else if (TextUtils.isEmpty(register_pwd)) {
			LggsUtils.ShowToast(RegisterActivity.this, "请输入您的密码");
		} else if (TextUtils.isEmpty(register_pwdagain)) {
			LggsUtils.ShowToast(RegisterActivity.this, "请确认您的密码");
		} else if (TextUtils.isEmpty(et_register_yzm.getText().toString())) {
			LggsUtils.ShowToast(RegisterActivity.this, "请输入您的验证码");
		} else if (!register_pwd.equals(register_pwdagain)) {
			LggsUtils.ShowToast(RegisterActivity.this, "两次输入的密码不一致");
		} else {
			verifyCode(et_register_yzm.getText().toString());
		}
	}

	/* 定义一个倒计时的内部类 */
	class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			// 完成
			btn_register_yzm
					.setBackgroundResource(R.drawable.blue_button_nopadding);
			btn_register_yzm.setClickable(true);
			btn_register_yzm.setText("重新获取");
		}

		@Override
		public void onTick(long millisUntilFinished) {
			btn_register_yzm.setClickable(false);
			btn_register_yzm
					.setBackgroundResource(R.drawable.gray_button_nopadding);
			btn_register_yzm.setText(millisUntilFinished / 1000 + "s后重发");
		}
	}

	private void sendRegister() {
		AsyncHttpClient client = new AsyncHttpClient();
		myProgressDialog = new ProgressDialog(this);
		myProgressDialog.setCancelable(true);
		myProgressDialog.setMessage("正在加载。。。");
		myProgressDialog.show();
		RequestParams params = new RequestParams();
		params.put("mobilePhone", register_tel);
		params.put("password", register_pwdagain);
		String url = Constans.URL + "signup";
		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				myProgressDialog.dismiss();
				Header Authorization = headers[1];
				String author = Authorization.getValue();
				CacheUtils.saveAuthor(getApplicationContext(), author);
				Gson gson = new Gson();
				if (statusCode == 201) {
					LggsUtils.ShowToast(RegisterActivity.this, "注册成功");
					UserMsgResponse userresponse = gson.fromJson(
							response.toString(), UserMsgResponse.class);
					long userId = userresponse.id;
					CacheUtils.saveUserID(RegisterActivity.this, userId);
					LggsUtils.StartIntent(RegisterActivity.this,
							RegisterSetNameActivity.class);
					finish();
				} else {
					LggsUtils.ShowToast(RegisterActivity.this, "服务器异常，注册失败！");
				}
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				if (statusCode == 201) {
					LggsUtils.ShowToast(RegisterActivity.this, "注册成功");
				} else {
					LggsUtils.ShowToast(RegisterActivity.this, "服务器异常，注册失败！");
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				myProgressDialog.dismiss();
				LggsUtils.ShowToast(RegisterActivity.this, "服务器异常，注册失败！");
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				myProgressDialog.dismiss();
				LggsUtils.ShowToast(RegisterActivity.this, responseString);
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONArray errorResponse) {
				myProgressDialog.dismiss();
				LggsUtils.ShowToast(RegisterActivity.this, "服务器异常，注册失败！");
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	/**
	 * 验证验证码
	 * 
	 * @param code
	 */
	private void verifyCode(String code) {
		AVOSCloud.verifySMSCodeInBackground(code, et_register_tel.getText()
				.toString(), new AVMobilePhoneVerifyCallback() {
			@Override
			public void done(AVException e) {
				if (e == null) {
					sendRegister();
				} else {
					e.printStackTrace();
					LggsUtils.ShowToast(RegisterActivity.this, "验证失败");
				}
			}
		});
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
					LggsUtils.ShowToast(RegisterActivity.this, "获取验证码成功");
				} else {
					LggsUtils.ShowToast(RegisterActivity.this, "获取验证码失败");
				}
			}
		}.execute();
	}
}
