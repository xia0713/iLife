package com.szgs.bbs.common.util;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.szgs.bbs.LoginActivity;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.register.UserMsgResponse;

public class HttpUtils {
	public static AsyncHttpClient getClient(Context context) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("Authorization", CacheUtils.getAuthor(context));
		return client;
	}

	public static void sendAutoLoginRequest(final Context context) {

		AsyncHttpClient client = getClient(context);
		final ProgressDialog myProgressDialog = new ProgressDialog(context);
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
				CacheUtils.saveAuthor(context, author);
				Gson gson = new Gson();
				if (statusCode == 200) {
					LggsUtils.ShowToast(context, "登录成功");
					UserMsgResponse userresponse = gson.fromJson(
							response.toString(), UserMsgResponse.class);
					long userId = userresponse.id;
					CacheUtils.saveUserID(context, userId);
					CacheUtils.saveAvatar(context, userresponse.avatar);
					CacheUtils.saveUserName(context, userresponse.nickname);
				} else {
					LggsUtils.ShowToast(context, "服务器异常，登录失败！");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(context, "服务器异常，登录失败！");
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				if (statusCode == 401) {
					LggsUtils.ShowToast(context, "请重新登录");
					LggsUtils.StartIntent(context, LoginActivity.class);
				} else {
					LggsUtils.ShowToast(context,
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
