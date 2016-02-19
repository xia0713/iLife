package com.szgs.bbs.mine;

import org.apache.http.Header;
import org.apache.http.util.TextUtils;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.Usermsg;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;

public class ChangeUsernameAcitvity extends BaseActivity implements
		OnClickListener {

	private TextView top_left_tv;
	private TextView tv_title;
	private TextView top_right_tv;
	private EditText change_name_et;
	private ImageView change_name_clear_iv;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_username);
		initHeaderView();
		initView();
	}

	public void initView() {
		change_name_et = (EditText) findViewById(R.id.change_name_et);
		change_name_et.setText(CacheUtils
				.getUserName(ChangeUsernameAcitvity.this));
		change_name_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 11) {
					LggsUtils.ShowToast(ChangeUsernameAcitvity.this, "用户昵称过长");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				System.out.println("before");

			}

			@Override
			public void afterTextChanged(Editable s) {
				System.out.println("after");

			}
		});
		change_name_clear_iv = (ImageView) findViewById(R.id.change_name_clear_iv);
		change_name_clear_iv.setOnClickListener(this);
		change_name_clear_iv.setVisibility(View.VISIBLE);
	}

	public void initHeaderView() {
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("修改昵称");
		top_right_tv = (TextView) findViewById(R.id.top_right_tv);
		top_right_tv.setText("确定");
		top_right_tv.setOnClickListener(this);
		top_right_tv.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.top_left_tv:
			finish();
			break;
		case R.id.top_right_tv:// 确定按钮，提交数据
			if (!TextUtils.isEmpty(change_name_et.getText().toString())) {
				sendData();
			} else {
				Toast.makeText(this, "用户名不能为空。", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.change_name_clear_iv:
			change_name_et.setText("");
			break;
		}

	}

	private void sendData() {
		final ProgressDialog pd = new ProgressDialog(
				ChangeUsernameAcitvity.this);
		final AsyncHttpClient client = getClient();
		String url = Constans.URL + "signup/step/1";
		client.setConnectTimeout(5000);
		RequestParams params = new RequestParams();
		params.put("userId", CacheUtils.getUserId(this));
		params.put("nickname", change_name_et.getText().toString());

		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				pd.setCancelable(true);
				pd.setMessage("正在加载。。。");
				pd.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						client.cancelAllRequests(true);
					}
				});
				pd.show();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				Usermsg userResponse = new Usermsg();
				Gson gson = new Gson();
				userResponse = gson.fromJson(response.toString(), Usermsg.class);
				CacheUtils.saveUserName(ChangeUsernameAcitvity.this,
						userResponse.nickname);
				finish();
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				if (arg0 == 401) {
					sendAutoLoginRequest();
				}
				LggsUtils.ShowToast(ChangeUsernameAcitvity.this,
						LggsUtils.replaceAll(arg2));
			}

			@Override
			public void onFinish() {
				pd.dismiss();
				super.onFinish();
			}
		});
	}
}
