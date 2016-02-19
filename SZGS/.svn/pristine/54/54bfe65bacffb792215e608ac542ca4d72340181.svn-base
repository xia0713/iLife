package com.szgs.bbs.answer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.SerializableMap;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.common.view.ViewPagerIndicator;
import com.szgs.bbs.find.FindCategoryResponse;

/**
 * 我感兴趣的问题
 * 
 * @author db
 * 
 */
public class MyInterestingQuestionActivity extends BaseActivity implements
		OnClickListener {

	// private PullToRefreshListView interesting_question_pull;
	private TextView tv_title;
	private TextView top_left_tv;

	private ViewPagerIndicator interesting_tab;
	private ViewPager interesting_pager;
	private MyInterestingQuestionPagerAdapter adapter;
	private int SELECTCODE = 0x12;
	private List<FindCategoryResponse> catagoryList = new ArrayList<FindCategoryResponse>();
	public long[] careid;
	private LinearLayout not_interest_notice;
	private ImageView imgview_add;
	private TextView tv_addinterest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_interesting_question);

		initHeaderView();
		initView();
		sendRequest();
		// if (getIntent().getParcelableArrayListExtra("tabs") != null) {
		// catagoryList = getIntent().getParcelableArrayListExtra("tabs");
		// }
		// // else {
		// // initData();
		// // }
		// if (getIntent().getBooleanExtra("isFromSelectInterestQuesActivity",
		// false)) {
		// // sendRequest();
		// SerializableMap<Long, String> map = (SerializableMap<Long, String>)
		// getIntent().getSerializableExtra("InterestingCategory");
		// getCatagoryListFromMap(map);
		// }
		// setTabs(catagoryList);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2 != null) {
			SerializableMap<Long, String> map = (SerializableMap<Long, String>) arg2
					.getSerializableExtra("InterestingCategory");
			if (map.getMap() != null) {
				catagoryList.clear();
				getCatagoryListFromMap(map);
			}
			if (catagoryList.size() == 0) {
				not_interest_notice.setVisibility(View.VISIBLE);
				imgview_add.setVisibility(View.GONE);
			} else {
				not_interest_notice.setVisibility(View.GONE);
				imgview_add.setVisibility(View.VISIBLE);
			}
			setTabs(catagoryList);
		}
	}

	private void getCatagoryListFromMap(SerializableMap<Long, String> map) {
		Iterator it = map.getMap().entrySet().iterator();
		System.out.println(map.getMap().entrySet().size());
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			FindCategoryResponse rp = new FindCategoryResponse();
			rp.id = (Long) entry.getKey();
			rp.name = entry.getValue().toString();
			catagoryList.add(rp);
		}
	}

	// @Override
	// protected void onResume() {
	// sendRequest();
	// super.onResume();
	// }

	public void initView() {
		imgview_add = (ImageView) findViewById(R.id.imgview_add);
		imgview_add.setOnClickListener(this);
		interesting_tab = (ViewPagerIndicator) findViewById(R.id.interesting_tab);
		interesting_pager = (ViewPager) findViewById(R.id.interesting_pager);
		adapter = new MyInterestingQuestionPagerAdapter(
				getSupportFragmentManager(), this);
		interesting_pager.setAdapter(adapter);
		interesting_pager.setOffscreenPageLimit(5);
		interesting_tab.setViewPager(interesting_pager);
		not_interest_notice = (LinearLayout) findViewById(R.id.not_interest_notice);
		tv_addinterest=(TextView)findViewById(R.id.tv_addinterest);
		tv_addinterest.setOnClickListener(this);
	}

	public void initData() {
		List<FindCategoryResponse> list1 = new ArrayList<FindCategoryResponse>();
		FindCategoryResponse response = new FindCategoryResponse();
		response.name = " ";
		response.id = -1;
		list1.add(response);
		setTabs(list1);
	}

	public void initHeaderView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(this);
		tv_title.setText("我感兴趣的问题");
		TextView top_right_tv = (TextView) findViewById(R.id.top_right_tv);
		top_right_tv.setBackgroundResource(R.drawable.navbar_edit_selector);
		top_right_tv.setOnClickListener(this);
		// top_right_tv.setVisibility(View.VISIBLE);

	}

	int index = 0;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_left_tv:
			finish();
			break;

		case R.id.imgview_add:
			Intent intent = new Intent();
			careid = new long[catagoryList.size()];
			for (int i = 0; i < catagoryList.size(); i++) {
				careid[i] = catagoryList.get(i).id;
			}
			intent.putExtra("careid", careid);
			intent.setClass(MyInterestingQuestionActivity.this,
					SelectInterestQuesActivity2.class);
			startActivityForResult(intent, 1000);
			// startActivity(intent);
			// startActivityForResult(intent, SELECTCODE);
			break;
		case R.id.tv_addinterest:
			Intent intent1 = new Intent();
			careid = new long[catagoryList.size()];
			for (int i = 0; i < catagoryList.size(); i++) {
				careid[i] = catagoryList.get(i).id;
			}
			intent1.putExtra("careid", careid);
			intent1.setClass(MyInterestingQuestionActivity.this,
					SelectInterestQuesActivity2.class);
			startActivityForResult(intent1, 1000);
			break;
		}
	}

	/**
	 * 设置tab的数据，包括title 和fragment （目前为String类列表，待后台数据确定，在修改）
	 * 
	 * @param tabList
	 */
	private void setTabs(List<FindCategoryResponse> tabList) {
		if (tabList != null) {
			adapter.setTabs(tabList);
			interesting_tab.notifyDataSetChanged();
			adapter.notifyDataSetChanged();

		}
	}

	/**
	 * 获取关注列表
	 * 
	 */
	private void sendRequest() {
		final AsyncHttpClient client = getClient();
		final ProgressDialog myProgressDialog = new ProgressDialog(this);
		myProgressDialog.setCancelable(true);
		myProgressDialog.setMessage("正在加载。。。");
		String url = Constans.URL + "categories/focused";
		RequestParams params = new RequestParams();
		params.put("userId",
				CacheUtils.getUserId(MyInterestingQuestionActivity.this));
		client.setConnectTimeout(5000);
		client.get(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				myProgressDialog.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						client.cancelAllRequests(true);
					}
				});
				myProgressDialog.show();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				super.onSuccess(statusCode, headers, response);

				if (statusCode == 200) {
					Gson gson = new Gson();
					careid = new long[response.length()];
					catagoryList.clear();
					for (int i = 0; i < response.length(); i++) {

						try {
							FindCategoryResponse responseEntity = gson
									.fromJson(response.get(i).toString(),
											FindCategoryResponse.class);
							catagoryList.add(responseEntity);
							careid[i] = responseEntity.id;
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					if (catagoryList.size() == 0) {
						not_interest_notice.setVisibility(View.VISIBLE);
						imgview_add.setVisibility(View.GONE);
					} else {
						not_interest_notice.setVisibility(View.GONE);
						imgview_add.setVisibility(View.VISIBLE);
					}
					setTabs(catagoryList);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				if(statusCode==401){
					sendAutoLoginRequest();
				}
				LggsUtils.ShowToast(MyInterestingQuestionActivity.this,
						LggsUtils.replaceAll(responseString));
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(
						MyInterestingQuestionActivity.this,
						getResources().getString(
								R.string.network_connection_error));
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFinish() {
				myProgressDialog.dismiss();
				super.onFinish();
			}
		});
	}
}
