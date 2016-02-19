package com.szgs.bbs.mine;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
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
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.adapter.MyAnswerAdapter;
import com.szgs.bbs.ask.AnswerListResponse;
import com.szgs.bbs.ask.QuestionDetailActivity;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;

/**
 * 我的回答
 * 
 * @author db
 * 
 */
public class MyAnswerActivity extends BaseActivity implements OnClickListener,
		OnRefreshListener2<ListView> {

	private TextView tv_title;
	private View top_left_tv;
	private PullToRefreshListView myanswer_pull;
	private ArrayList<AnswerListResponse.Content> contentlist;
	private MyAnswerAdapter myAdapter;
	private ProgressDialog progressdialog;
	private int SIZE = 10;
	private int PAGE = 1;
	private int mCurrentAction = PULL_DOWN;
	protected boolean last = true;
	/** 下拉刷新 */
	private static final int PULL_DOWN = 0x01;
	/** 上拉加载 */
	private static final int PULL_UP = 0x02;
	private LinearLayout li_share_myasw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_answer);
		contentlist = new ArrayList<AnswerListResponse.Content>();
		initHeaderView();
		initView();
		initData();
		sendRequset();
		initListener();

	}

	public void initView() {
		myanswer_pull = (PullToRefreshListView) findViewById(R.id.myanswers_pull);
		// ListView listView = myanswer_pull.getRefreshableView();
		// listView.setDivider(new ColorDrawable(Color.parseColor("#ECECEC")));
		// listView.setDividerHeight((int) TypedValue.applyDimension(
		// TypedValue.COMPLEX_UNIT_DIP, 10, getResources()
		// .getDisplayMetrics()));

	}

	public void initData() {
		myAdapter = new MyAnswerAdapter(MyAnswerActivity.this, contentlist);
		myanswer_pull.setAdapter(myAdapter);
		myanswer_pull.setMode(Mode.BOTH);
	}

	public void initListener() {
		myanswer_pull.setOnRefreshListener(this);
		myanswer_pull.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("questiondetail",
						contentlist.get(position - 1).question);
				LggsUtils.StartIntent(MyAnswerActivity.this,
						QuestionDetailActivity.class, bundle);
			}
		});
		myanswer_pull.setOnRefreshListener(this);
	}

	public void initHeaderView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(this);
		li_share_myasw = (LinearLayout) findViewById(R.id.li_share_myasw);
		tv_title.setText("我的回答");
	}

	public void sendRequset() {
		progressdialog = new ProgressDialog(MyAnswerActivity.this);
		progressdialog.setMessage("正在加载。。。");
		progressdialog.setCancelable(true);
		progressdialog.show();
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "answers/of_user";
		url = url + "?userId=" + CacheUtils.getUserId(MyAnswerActivity.this)
				+ "&page=" + PAGE + "&size=" + SIZE;
		client.setTimeout(5000);
		client.get(url, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				myanswer_pull.onRefreshComplete();

				Gson gson = new Gson();
				if (statusCode == 200) {
					AnswerListResponse questionlistRep = gson.fromJson(
							response.toString(), AnswerListResponse.class);
					last = questionlistRep.last;
					if (last) {
						if (PAGE != 1) {
							LggsUtils.ShowToast(MyAnswerActivity.this,
									"已经是最后一页了");
						}
					}
					myanswer_pull.setMode(Mode.BOTH);
					if (mCurrentAction == PULL_DOWN) {
						contentlist.clear();
					}
					contentlist.addAll(questionlistRep.content);
					myAdapter.notifyDataSetChanged();
					if (myAdapter.getCount() == 0) {
						li_share_myasw.setVisibility(View.VISIBLE);
					} else {
						li_share_myasw.setVisibility(View.GONE);
					}
					// contentlist = questionlistRep.content;
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {

				if (statusCode == 401) {
					sendAutoLoginRequest();
				}
				LggsUtils.ShowToast(MyAnswerActivity.this, responseString);
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(MyAnswerActivity.this, getResources()
						.getString(R.string.network_connection_error));
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFinish() {
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
		++PAGE;
		mCurrentAction = PULL_UP;
		sendRequset();
	}
}
