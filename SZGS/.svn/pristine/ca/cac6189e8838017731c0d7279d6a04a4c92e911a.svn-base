package com.szgs.bbs.mine;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.szgs.bbs.adapter.MyCollectionAdapter;
import com.szgs.bbs.ask.QuestionDetailActivity;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.QuestionListResponse;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;

/**
 * 我的收藏
 * 
 * @author db
 * 
 */
public class MyCollectionActivity extends BaseActivity implements
		OnClickListener, OnRefreshListener2<ListView>, OnItemClickListener {

	private TextView tv_title;
	private View top_left_tv;
	private PullToRefreshListView myquestions_pull;
	private MyCollectionAdapter adapter;
	private LinearLayout li_my_collection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_questions);
		initHeaderView();
		initView();
		getData();
	}

	public void initView() {
		myquestions_pull = (PullToRefreshListView) findViewById(R.id.myanswer_pull);
		myquestions_pull.setOnRefreshListener(this);
		myquestions_pull.setOnItemClickListener(this);
		li_my_collection = (LinearLayout) findViewById(R.id.li_my_collection);
		adapter = new MyCollectionAdapter(this, null);
		myquestions_pull.setAdapter(adapter);
		myquestions_pull.setMode(Mode.BOTH);
	}

	public void initHeaderView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(this);
		tv_title.setText("我的收藏");
	}

	private int page = 1;
	private ProgressDialog progressdialog;

	private void getData() {
		progressdialog = new ProgressDialog(MyCollectionActivity.this);
		progressdialog.setMessage("正在加载。。。");
		progressdialog.setCancelable(true);
		progressdialog.show();
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "favourites/answers";
		RequestParams params = new RequestParams();
		params.put("userId", CacheUtils.getUserId(this));
		params.put("page", page);
		params.put("size", 10);
		client.setConnectTimeout(5000);
		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				Gson gson = new Gson();
				if (statusCode == 200) {
					QuestionListResponse rp = gson.fromJson(
							response.toString(), QuestionListResponse.class);

					myquestions_pull.setMode(Mode.BOTH);
					if (page == 1) {
						adapter.setData(rp.content);
					} else {
						adapter.addData(rp.content);
					}
					if (rp.last) {
						if (page != 1) {
							LggsUtils.ShowToast(MyCollectionActivity.this,
									"已经是最后一页了");
						} else {
							page++;
						}
					}
					adapter.notifyDataSetChanged();
					if (adapter.getCount() == 0) {
						li_my_collection.setVisibility(View.VISIBLE);
					} else {
						li_my_collection.setVisibility(View.GONE);
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				if (statusCode == 401) {
					sendAutoLoginRequest();
				}
				LggsUtils.ShowToast(MyCollectionActivity.this, responseString);
			}

			@Override
			public void onFinish() {
				progressdialog.dismiss();
				myquestions_pull.onRefreshComplete();
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

		default:
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
		getData();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Bundle bundle = new Bundle();
		bundle.putString("issocool", "yes");
		bundle.putSerializable("questiondetail",
				adapter.getItem(position - 1).question);
		LggsUtils.StartIntent(MyCollectionActivity.this,
				QuestionDetailActivity.class, bundle);

	}
}
