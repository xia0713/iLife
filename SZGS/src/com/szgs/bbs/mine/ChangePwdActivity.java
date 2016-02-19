package com.szgs.bbs.mine;

import org.apache.http.Header;
import org.apache.http.util.TextUtils;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;

public class ChangePwdActivity extends BaseActivity implements OnClickListener {

	private TextView top_left_tv;
	private TextView tv_title;
	private TextView top_right_tv;
	private EditText change_pwd_old, change_pwd_new, change_pwd_new_comfirmed;
	private ProgressDialog myProgressDialog;
	private Button btn_ok;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_pwd);
		initHeaderView();
		initView();
	}

	private void initView() {
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
		change_pwd_old = (EditText) findViewById(R.id.change_pwd_old);
		change_pwd_new = (EditText) findViewById(R.id.change_pwd_new);
		change_pwd_new_comfirmed = (EditText) findViewById(R.id.change_pwd_new_comfirmed);
	}

	public void initHeaderView() {
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("修改密码");
		top_right_tv = (TextView) findViewById(R.id.top_right_tv);
		top_right_tv.setText("确定");
		top_right_tv.setOnClickListener(this);
		top_right_tv.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.top_left_tv:
			finish();
			break;
		case R.id.btn_ok:// 确定按钮，提交数据
			if(TextUtils.isEmpty(change_pwd_old.getText().toString())){
				Toast.makeText(ChangePwdActivity.this, "请输入原密码",
						Toast.LENGTH_SHORT).show();
			}else if (!TextUtils.isEmpty(change_pwd_old.getText().toString())
					&& !TextUtils.isEmpty(change_pwd_new.getText().toString())
					&& !TextUtils.isEmpty(change_pwd_new_comfirmed.getText()
							.toString())) {
				if (change_pwd_new.getText().toString()
						.equals(change_pwd_new_comfirmed.getText().toString())) {
					// 提交数据，监测old是否正确····
					sendData();
				} else {
					Toast.makeText(ChangePwdActivity.this, "两次输入的密码不一致",
							Toast.LENGTH_SHORT).show();
					// change_pwd_new.setText("");
					// change_pwd_new_comfirmed.setText("");
				}
			} else {
				Toast.makeText(ChangePwdActivity.this, "密码不能为空",
						Toast.LENGTH_SHORT).show();
			}
			break;

		}

	}

	public void sendData() {

		AsyncHttpClient client = getClient();
		myProgressDialog = new ProgressDialog(this);
		myProgressDialog.setCancelable(true);
		myProgressDialog.setMessage("正在加载。。。");
		myProgressDialog.show();
		RequestParams params = new RequestParams();
		params.put("userId", CacheUtils.getUserId(ChangePwdActivity.this));
		params.put("password", change_pwd_old.getText().toString());
		params.put("newPassword", change_pwd_new.getText().toString());
		String url = Constans.URL + "password/edit";
		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				myProgressDialog.dismiss();
				if (statusCode == 200) {
					LggsUtils.ShowToast(ChangePwdActivity.this, "修改密码成功");
					CacheUtils.savePassWord(ChangePwdActivity.this,
							change_pwd_new.getText().toString());
					finish();
				} else {
					LggsUtils.ShowToast(ChangePwdActivity.this, "服务器异常，注册失败！");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(ChangePwdActivity.this, "服务器异常，注册失败！");
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				LggsUtils.ShowToast(ChangePwdActivity.this, responseString);
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
