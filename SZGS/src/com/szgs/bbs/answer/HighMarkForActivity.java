package com.szgs.bbs.answer;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.adapter.QuestionDetailAdapter;
import com.szgs.bbs.ask.QuestionDetailActivity;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.QuestionListResponse;
import com.szgs.bbs.common.util.LggsUtils;

/**
 * 高分悬赏
 * 
 * @author db
 * 
 */
public class HighMarkForActivity extends BaseActivity implements
		OnClickListener, OnRefreshListener2<ListView>, OnItemClickListener {

	private PullToRefreshListView high_reward_pull;
	private TextView tv_title;
	private TextView top_left_tv;
	private ProgressDialog progressdialog;
	private int SIZE = 10;
	private int PAGE = 1;
	private int mCurrentAction = PULL_DOWN;
	protected boolean last = true;
	/** 下拉刷新 */
	private static final int PULL_DOWN = 0x01;
	/** 上拉加载 */
	private static final int PULL_UP = 0x02;
	ArrayList<QuestionListResponse.Question> contentlist;
	private QuestionDetailAdapter myAdapter;
	private LinearLayout ll_empty_highmark;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_high_mark_for);
		contentlist = new ArrayList<QuestionListResponse.Question>();
		initHeaderView();
		initView();
		initData();
		sendRequset();
	}

	public void initData() {
		myAdapter = new QuestionDetailAdapter(HighMarkForActivity.this,
				contentlist, 3);
		high_reward_pull.setAdapter(myAdapter);

	}

	public void initView() {
		ll_empty_highmark = (LinearLayout) findViewById(R.id.ll_empty_highmark);
		high_reward_pull = (PullToRefreshListView) findViewById(R.id.high_reward_pull);
		high_reward_pull.setOnRefreshListener(this);
		high_reward_pull.setOnItemClickListener(this);
		high_reward_pull.setMode(Mode.BOTH);
	}

	public void initHeaderView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_title.setText("高分悬赏");
	}

	public void sendRequset() {
		progressdialog = new ProgressDialog(HighMarkForActivity.this);
		progressdialog.setMessage("正在加载。。。");
		progressdialog.setCancelable(true);
		progressdialog.show();
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "questions/high_reward";
		RequestParams params = new RequestParams();
		// params.put("categoryId", catagoryId);
		params.put("page", PAGE);
		params.put("size", SIZE);
		client.get(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				Gson gson = new Gson();
				if (statusCode == 200) {
					QuestionListResponse questionlistRep = gson.fromJson(
							response.toString(), QuestionListResponse.class);
					last = questionlistRep.last;
					if (!last) {
						high_reward_pull.setMode(Mode.BOTH);
					} else {
						if (PAGE != 1) {
							LggsUtils.ShowToast(HighMarkForActivity.this,
									"已经是最后一页了");
						}
						high_reward_pull.setMode(Mode.BOTH);
					}
					if (mCurrentAction == PULL_DOWN) {
						contentlist.clear();
					}
					contentlist.addAll(questionlistRep.content);
					myAdapter.notifyDataSetChanged();
					if (myAdapter.getCount() == 0) {
						ll_empty_highmark.setVisibility(View.VISIBLE);
					} else {
						ll_empty_highmark.setVisibility(View.GONE);
					}
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				if (statusCode == 401) {
					sendAutoLoginRequest();
				}
				LggsUtils.ShowToast(HighMarkForActivity.this,
						LggsUtils.replaceAll(responseString));
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(HighMarkForActivity.this, getResources()
						.getString(R.string.network_connection_error));
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFinish() {
				high_reward_pull.onRefreshComplete();
				progressdialog.dismiss();
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
		mCurrentAction = PULL_DOWN;
		PAGE = 1;
		sendRequset();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> arg0) {
		mCurrentAction = PULL_UP;
		++PAGE;
		sendRequset();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("questiondetail", contentlist.get(position - 1));
		LggsUtils.StartIntent(HighMarkForActivity.this,
				QuestionDetailActivity.class, bundle);
	}
}
