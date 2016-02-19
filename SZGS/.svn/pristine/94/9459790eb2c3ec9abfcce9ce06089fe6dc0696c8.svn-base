package com.szgs.bbs.answer;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.answer.CreditExchangeListResponse.Content;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;

public class ExchangeInfoActivity extends BaseActivity implements
		OnClickListener {

	private TextView tv_title, top_left_tv;

	private TextView exchange_info_prize_name_tv, exchange_info_prize_cost_tv;
	private EditText exchange_info_username_tv, exchange_info_phone_et,
			exchange_info_address_et;
	private ImageView exchange_info_prize_pic_iv;

	private CreditExchangeListResponse.Content gift;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exchange_info);
		gift = (Content) getIntent().getSerializableExtra("gift");
		initHeaderView();
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		exchange_info_prize_pic_iv = (ImageView) findViewById(R.id.exchange_info_prize_pic_iv);
		ImageLoader.getInstance().displayImage(gift.image,
				exchange_info_prize_pic_iv);
		exchange_info_prize_name_tv = (TextView) findViewById(R.id.exchange_info_prize_name_tv);
		exchange_info_prize_name_tv.setText(gift.title);
		exchange_info_prize_cost_tv = (TextView) findViewById(R.id.exchange_info_prize_cost_tv);
		exchange_info_prize_cost_tv.setText(gift.requireScore + "积分");
		exchange_info_username_tv = (EditText) findViewById(R.id.exchange_info_username_tv);
		exchange_info_phone_et = (EditText) findViewById(R.id.exchange_info_phone_et);
		exchange_info_address_et = (EditText) findViewById(R.id.exchange_info_address_et);
	}

	public void initHeaderView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(this);
		tv_title.setText("填写兑换信息");
		TextView top_right_tv = (TextView) findViewById(R.id.top_right_tv);
		top_right_tv.setText("确定");
		top_right_tv.setOnClickListener(this);
		top_right_tv.setVisibility(View.VISIBLE);
	}

	private void getGift() {
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setMessage("正在加载。。。");
		pd.setCancelable(true);
		pd.show();
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "gift/order";
		RequestParams params = new RequestParams();
		params.put("userId", CacheUtils.getUserId(this));
		params.put("giftId", gift.id);
		params.put("receiverName", exchange_info_username_tv.getText()
				.toString());
		params.put("mobilePhone", exchange_info_phone_et.getText().toString());
		params.put("address", exchange_info_address_et.getText().toString());
		client.setTimeout(5000);
		client.post(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				// System.out.println(response.toString());
				if (statusCode == 200) {
					LggsUtils.ShowToast(ExchangeInfoActivity.this, "兑换成功");
					finish();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				LggsUtils.ShowToast(ExchangeInfoActivity.this, responseString);
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
				pd.dismiss();
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
			if (TextUtils.isEmpty(exchange_info_username_tv.getText()
					.toString())) {
				LggsUtils.ShowToast(this, "请填写您的姓名");
			} else if (TextUtils.isEmpty(exchange_info_phone_et.getText()
					.toString())) {
				LggsUtils.ShowToast(this, "请填写您的联系电话");
			} else if (TextUtils.isEmpty(exchange_info_address_et.getText()
					.toString())) {
				LggsUtils.ShowToast(this, "请填写您的详细地址");
			} else {
				LggsUtils.showExchangeDialog(ExchangeInfoActivity.this,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								getGift();
							}
						}, null, true);
			}

			break;
		}
	}
}
