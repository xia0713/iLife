package com.szgs.bbs.ask;

import java.util.Arrays;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.SendCallback;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;

/**
 * “回答”与“评论”通用 ,startactivity时传一个key为title 的string，值为“回答”或者“评论答案”
 * 
 * @author liinfeng
 * 
 */
public class AnswerOrCommentActivity extends BaseActivity implements
		OnClickListener {

	private Intent intent;

	private EditText answer_comment_content_et;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer_comment);
		intent = getIntent();
		initHeaderView();
		initView();
	}

	private void initHeaderView() {
		TextView top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(this);
		TextView top_right_tv = (TextView) findViewById(R.id.top_right_tv);
		top_right_tv.setText("发布");
		top_right_tv.setVisibility(View.VISIBLE);
		top_right_tv.setOnClickListener(this);
		((TextView) findViewById(R.id.tv_title)).setText(intent
				.getStringExtra("title"));
	}

	private void initView() {
		answer_comment_content_et = (EditText) findViewById(R.id.answer_comment_content_et);
		if (intent.getStringExtra("title").equals("回答")) {
			answer_comment_content_et.setHint("写下你的回答...");
		} else {
			answer_comment_content_et.setHint("写下你的评论...");
		}
		// answer_comment_content_et.setText(LggsUtils
		// .readFiles(AnswerOrCommentActivity.this));
	}

	private void publishAnswer() {
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setCancelable(false);
		pd.setMessage("正在加载。。。");
		pd.show();
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "answer/publish";
		RequestParams params = new RequestParams();
		params.put("content", answer_comment_content_et.getText().toString());
		params.put("questionId", intent.getLongExtra("questionId", 0));
		params.put("userId", CacheUtils.getUserId(this));
		client.setConnectTimeout(5000);
		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (statusCode == 201) {
					LggsUtils.ShowToast(AnswerOrCommentActivity.this, "回答成功");

					// AVPush push = new AVPush();
					// AVQuery<AVInstallation> query =
					// AVInstallation.getQuery();
					// query.whereEqualTo("channels", "回答我的");
					// query.whereContainsAll("deviceType",
					// Arrays.asList("ios", "android"));
					// query.whereEqualTo("answer", "106");
					// //intent.getStringExtra("askById")
					// push.setQuery(query);
					// JSONObject jsonObject = new JSONObject();
					// try {
					// jsonObject.put("action", "com.szgs.answer");
					// jsonObject.put(
					// "alert",
					// CacheUtils
					// .getUserName(AnswerOrCommentActivity.this)
					// + "已回答了您的问题，赶紧点击查看吧");
					// } catch (JSONException e1) {
					// e1.printStackTrace();
					// }
					// push.setData(jsonObject);
					// // push.setMessage(CacheUtils
					// // .getUserName(AnswerOrCommentActivity.this)
					// // + "已回答了您的问题，赶紧点击查看吧");
					// // push.setPushToAndroid(true);
					// // push.setPushToIOS(true);
					// push.sendInBackground(new SendCallback() {
					// @Override
					// public void done(AVException e) {
					// Toast.makeText(getApplicationContext(),
					// "send successfully", Toast.LENGTH_SHORT);
					// }
					// });

					finish();
				}
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				super.onSuccess(statusCode, headers, responseString);
				LggsUtils.ShowToast(AnswerOrCommentActivity.this,
						responseString);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				if (statusCode == 401) {
					sendAutoLoginRequest();
				} else {
					LggsUtils.ShowToast(AnswerOrCommentActivity.this,
							responseString);
				}
			}

			@Override
			public void onFinish() {
				pd.dismiss();
				super.onFinish();
			}
		});
	}

	private void publishComment() {
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "comment/answer";
		RequestParams params = new RequestParams();
		params.put("commentBy", CacheUtils.getUserId(this));
		params.put("answerId", intent.getLongExtra("answerId", 0));
		params.put("answerBy", intent.getStringExtra("answerById"));
		params.put("message", answer_comment_content_et.getText().toString());
		client.setConnectTimeout(5000);
		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				if (statusCode == 200) {
					LggsUtils.ShowToast(AnswerOrCommentActivity.this, "评论成功");
					finish();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				if (statusCode == 401) {
					sendAutoLoginRequest();
				} else {
					LggsUtils.ShowToast(AnswerOrCommentActivity.this,
							responseString);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_left_tv:
			finish();
			break;
		case R.id.top_right_tv:
			if (!TextUtils.isEmpty(answer_comment_content_et.getText()
					.toString())) {
				if (intent.getStringExtra("title").equals("回答")) {// 发布回答
					publishAnswer();
				} else {// 发布评论
					publishComment();
				}
			} else {
				LggsUtils.ShowToast(this, "内容不能为空");
			}
			break;
		}
	}

	// @Override
	// protected void onStop() {
	// LggsUtils.writeFiles(AnswerOrCommentActivity.this,
	// answer_comment_content_et.getText().toString());
	// super.onStop();
	// }
}
