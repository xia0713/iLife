package com.szgs.bbs;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.google.gson.Gson;
import com.jianq.base.util.JQApplicationConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.mine.NotificationActivity;
import com.szgs.bbs.register.UserMsgResponse;

public class BaseActivity extends FragmentActivity {

	private InputMethodManager mIMEManger;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setTitleBarShow(false);
		// if (mAutoInitlizaion && this instanceof Initialization) {
		// Initialization init = (Initialization) this;
		// init.onInitViews();
		// init.onInitListener();
		// init.onInitData();
		// }
		LggsApplication.getInstance().pushActivity(this);
		mIMEManger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	/**
	 * 根据请求类型获取不同的baseurl
	 * 
	 * @return
	 */
	public String getBaseUrl() {
		String baseUrl = null;
		String requestType = JQApplicationConfig.getConfigValue("requestType");
		if (requestType.equals("http")) {

			baseUrl = JQApplicationConfig.getDomain();
		} else if (requestType.equals("https")) {

			baseUrl = JQApplicationConfig
					.getDomain(JQApplicationConfig.SCHEME_HTTPS);
		}
		return baseUrl;
	}

	/** 点击空白处，收回软键盘 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			View focusView = getCurrentFocus();
			if (focusView != null
					&& focusView.getApplicationWindowToken() != null
					&& mIMEManger.isActive())
				mIMEManger.hideSoftInputFromWindow(
						focusView.getApplicationWindowToken(), 0);
			return true;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 隐藏keybord
	 */
	public void hideKeybord(View view) {
		if (mIMEManger.isActive()) {
			mIMEManger.hideSoftInputFromWindow(
					view.getApplicationWindowToken(), 0);
		}
	}

	/**
	 * 调整Activity整个窗口的alpha，制作变暗效果
	 * 
	 * @param alpha
	 */
	public void setWindowAlpha(float alpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = alpha;
		getWindow().setAttributes(lp);
	}

	public AsyncHttpClient getClient() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("Authorization", CacheUtils.getAuthor(this));
		return client;
	}

	public void sendAutoLoginRequest() {

		AsyncHttpClient client = getClient();
		final ProgressDialog myProgressDialog = new ProgressDialog(this);
		myProgressDialog.setCancelable(true);
		myProgressDialog.setMessage("正在登录。。。");
		myProgressDialog.show();
		RequestParams params = new RequestParams();
		String url = Constans.URL + "remember/signin";
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
					// LggsUtils.ShowToast(getApplicationContext(), "登录成功");
					UserMsgResponse userresponse = gson.fromJson(
							response.toString(), UserMsgResponse.class);
					long userId = userresponse.id;
					CacheUtils.saveUserID(getApplicationContext(), userId);
					CacheUtils.saveAvatar(getApplicationContext(),
							userresponse.avatar);
					CacheUtils.saveUserName(getApplicationContext(),
							userresponse.nickname);
					
				} else {
					LggsUtils.ShowToast(getApplicationContext(), "服务器异常，登录失败！");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(getApplicationContext(), "服务器异常，登录失败！");
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				if (statusCode == 401) {
					LggsUtils.ShowToast(getApplicationContext(), "请重新登录");
					LggsUtils.StartIntent(getApplicationContext(),
							LoginActivity.class);
				} else {
					LggsUtils.ShowToast(getApplicationContext(),
							LggsUtils.replaceAll(responseString));
				}
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFinish() {
				myProgressDialog.dismiss();
				super.onFinish();
			}
		});

	}
}
