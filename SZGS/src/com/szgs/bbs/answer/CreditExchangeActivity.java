package com.szgs.bbs.answer;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.common.view.CircleImageView;
import com.szgs.bbs.mine.UserStatisticsResponse;

/**
 * 积分兑换
 * 
 * @author db
 * 
 */
public class CreditExchangeActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {

	private TextView tv_title;
	private TextView top_left_tv;

	private TextView credit_exchange_sum;

	private GridView credit_exchange_things_grid;
	private CreditExchangeThingsGridAdapter adapter;
	private ImageView img_jfdh;
	private CircleImageView credit_exchange_avatar_iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_credit_exchange);

		initHeaderView();
		initView();
		getListData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getMyScore();
	}

	private void initView() {
		credit_exchange_avatar_iv = (CircleImageView) findViewById(R.id.credit_exchange_avatar_iv);
		ImageLoader.getInstance().displayImage(
				CacheUtils.getAvatar(CreditExchangeActivity.this),
				credit_exchange_avatar_iv, LggsUtils.inImageLoaderOptions());

		credit_exchange_things_grid = (GridView) findViewById(R.id.credit_exchange_things_grid);
		adapter = new CreditExchangeThingsGridAdapter(this, null);
		credit_exchange_things_grid.setAdapter(adapter);
		credit_exchange_things_grid.setOnItemClickListener(this);
		credit_exchange_sum = (TextView) findViewById(R.id.credit_exchange_sum);
		// img_jfdh = (ImageView) findViewById(R.id.img_jfdh);
		// img_jfdh.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// LggsUtils.ShowToast(CreditExchangeActivity.this, "您未达到兑换条件");
		// }
		// });
	}

	public void initHeaderView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(this);
		tv_title.setText("积分兑换");
	}

	private int mPage = 1;
	private int mSize = 100;

	private void getListData() {
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setMessage("正在加载。。。");
		pd.setCancelable(true);
		pd.show();
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "/gifts";
		url = url + "?page=" + mPage + "&size=" + mSize;
		client.setTimeout(5000);
		client.get(url, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (statusCode == 200) {
					Gson gson = new Gson();
					CreditExchangeListResponse rp = gson.fromJson(
							response.toString(),
							CreditExchangeListResponse.class);
					adapter.setData(rp.content);
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				LggsUtils.ShowToast(CreditExchangeActivity.this,
						LggsUtils.replaceAll(responseString));
			}

			@Override
			public void onFinish() {
				super.onFinish();
				pd.dismiss();
			}
		});
	}

	private void getMyScore() {
		final AsyncHttpClient client = getClient();
		String url = Constans.URL + "user/statistics";
		RequestParams params = new RequestParams();
		params.put("userId", CacheUtils.getUserId(this));
		client.setConnectTimeout(5000);
		client.get(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				Gson gson = new Gson();
				if (statusCode == 200) {
					UserStatisticsResponse responseEntity = gson.fromJson(
							response.toString(), UserStatisticsResponse.class);
					credit_exchange_sum.setText(responseEntity.score + "");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				LggsUtils.ShowToast(CreditExchangeActivity.this,
						LggsUtils.replaceAll(responseString));
			}

			@Override
			public void onFinish() {
				// myProgressDialog.dismiss();
				super.onFinish();
			}
		});
	}

	private void getAuthority(final CreditExchangeListResponse.Content gift) {
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setMessage("正在加载。。。");
		pd.setCancelable(true);
		pd.show();
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "gift/check_score";
		RequestParams params = new RequestParams();
		params.put("userId", CacheUtils.getUserId(this));
		params.put("giftId", gift.id);
		client.setTimeout(5000);
		client.post(url, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				if (arg0 == 200) {
					if (arg2.equals("true")) {
						Bundle bundle = new Bundle();
						bundle.putSerializable("gift", gift);
						LggsUtils.StartIntent(CreditExchangeActivity.this,
								ExchangeInfoActivity.class, bundle);
					} else {
						LggsUtils.ShowToast(CreditExchangeActivity.this,
								"您未达到兑换条件");
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				LggsUtils.ShowToast(CreditExchangeActivity.this, arg2);
			}

			@Override
			public void onFinish() {
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

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		getAuthority(adapter.getItem(position));

	}
}
