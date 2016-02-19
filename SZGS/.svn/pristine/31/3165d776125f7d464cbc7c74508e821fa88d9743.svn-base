package com.szgs.bbs.mine;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.adapter.MyQuestionsAdapter;
import com.szgs.bbs.ask.QuestionDetailActivity;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.QuestionListResponse;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.find.CategoryActivity;

/**
 * 我的提问
 * 
 * @author db
 * 
 */
public class MyQuestionsActivity extends BaseActivity implements
		OnClickListener, OnRefreshListener2<ListView>, OnItemClickListener {

	private TextView tv_title;
	private View top_left_tv;
	private PullToRefreshListView myquestion_pull;
	private MyQuestionsAdapter adapter;
	private static final int SIZE = 10;
	private int page = 1;
	private LinearLayout li_my_ask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_questions);
		initHeaderView();
		initView();

	}

	@Override
	protected void onResume() {
		getData();
		super.onResume();
	}

	public void initView() {
		myquestion_pull = (PullToRefreshListView) findViewById(R.id.myanswer_pull);
		myquestion_pull.setOnRefreshListener(this);
		myquestion_pull.setOnItemClickListener(this);
		adapter = new MyQuestionsAdapter(this, null, 1);
		myquestion_pull.setAdapter(adapter);
	}

	public void initHeaderView() {
		li_my_ask = (LinearLayout) findViewById(R.id.li_my_ask);
		tv_title = (TextView) findViewById(R.id.tv_title);
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(this);
		tv_title.setText("我的提问");
	}

	public void getData() {
		final AsyncHttpClient client = getClient();
		String url = Constans.URL + "questions/by_user";
		RequestParams params = new RequestParams();
		params.put("userId", CacheUtils.getUserId(MyQuestionsActivity.this));
		params.put("page", page);
		params.put("size", SIZE);
		client.setConnectTimeout(5000);
		client.get(url, params, new JsonHttpResponseHandler() {

			private ProgressDialog pd;

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				pd = new ProgressDialog(MyQuestionsActivity.this);
				pd.setCancelable(true);
				pd.setMessage("正在加载。。。");
				pd.setCancelable(true);
				pd.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						client.cancelAllRequests(true);
					}
				});
				pd.show();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				Gson gson = new Gson();
				if (statusCode == 200) {
					myquestion_pull.onRefreshComplete();
					QuestionListResponse rp = gson.fromJson(
							response.toString(), QuestionListResponse.class);
					if (rp.last) {
						if (page != 1) {
							LggsUtils.ShowToast(MyQuestionsActivity.this,
									"已经是最后一页了");
						}
					}
					myquestion_pull.setMode(Mode.BOTH);
					if (page == 1) {
						adapter.clearData();
						adapter.setData(rp.content);
					} else {
						adapter.addData(rp.content);
					}
					adapter.notifyDataSetChanged();
					if (adapter.getCount() == 0) {
						li_my_ask.setVisibility(View.VISIBLE);
					} else {
						li_my_ask.setVisibility(View.GONE);
					}
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				LggsUtils.ShowToast(MyQuestionsActivity.this, responseString);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(MyQuestionsActivity.this, getResources()
						.getString(R.string.network_connection_error));
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFinish() {
				pd.dismiss();
				super.onFinish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_left_tv:
			finish();
			break;
		}

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> arg0) {
		page = 1;
		getData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> arg0) {
		++page;
		getData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("questiondetail", adapter.getItem(position - 1));
		LggsUtils.StartIntent(MyQuestionsActivity.this,
				QuestionDetailActivity.class, bundle);
	}

}
