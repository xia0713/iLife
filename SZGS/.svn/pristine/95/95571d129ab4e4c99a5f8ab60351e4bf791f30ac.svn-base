package com.szgs.bbs.ask;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

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
import com.szgs.bbs.adapter.CommentsAdapter;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.util.LggsUtils;

public class CommentListActivity extends BaseActivity implements OnClickListener, OnItemClickListener, OnRefreshListener2<ListView> {

	private PullToRefreshListView comment_list_pull;
	private CommentsAdapter adapter;

	private long answerId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_list);
		answerId = getIntent().getLongExtra("answerId", 0);

		initHeaderView();
		initView();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mPage = 1;
		getCommentList();
	}

	private void initView() {
		comment_list_pull = (PullToRefreshListView) findViewById(R.id.comment_list_pull);
		comment_list_pull.setOnRefreshListener(this);
		ListView listView = comment_list_pull.getRefreshableView();
		adapter = new CommentsAdapter(this, null);
		listView.setAdapter(adapter);
	}

	private void initHeaderView() {
		TextView top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(this);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("评论");
		TextView top_right_tv = (TextView) findViewById(R.id.top_right_tv);
		top_right_tv.setBackgroundResource(R.drawable.navbar_comment_selector);
		top_right_tv.setVisibility(View.VISIBLE);
		top_right_tv.setOnClickListener(this);
	}

	private int mPage = 1;

	private void getCommentList() {
		final ProgressDialog pd = new ProgressDialog(CommentListActivity.this);

		final AsyncHttpClient client = getClient();
		String url = Constans.URL + "comments/of_answer";
		RequestParams params = new RequestParams();
		params.put("answerId", answerId);
		params.put("page", mPage);
		params.put("size", 10);
		client.setConnectTimeout(5000);
		client.get(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				pd.setCancelable(true);
				pd.setMessage("正在加载。。。");
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
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				comment_list_pull.onRefreshComplete();
				pd.dismiss();
				if (statusCode == 200 && response != null) {
					Gson gson = new Gson();
					CommentResponse rp = gson.fromJson(response.toString(), CommentResponse.class);
					if (rp.totalElements != 0) {
						if (rp.last) {
							comment_list_pull.setMode(Mode.PULL_FROM_START);
						} else {
							comment_list_pull.setMode(Mode.BOTH);
						}
						if (mPage == 1) {
							adapter.setData(rp.content);
						} else {
							adapter.addData(rp.content);
							++mPage;
						}
						adapter.notifyDataSetChanged();
					} else {
						LggsUtils.ShowToast(CommentListActivity.this, "还没有评论。赶紧发表评论吧！");
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				comment_list_pull.onRefreshComplete();
				pd.dismiss();
				LggsUtils.ShowToast(CommentListActivity.this, responseString);
			}
		});
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> arg0) {
		// TODO Auto-generated method stub
		mPage = 1;
		getCommentList();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> arg0) {
		// TODO Auto-generated method stub
		getCommentList();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_left_tv:
			finish();
			break;
		case R.id.top_right_tv:
			Bundle bundle = new Bundle();
			bundle.putLong("answerId", answerId);
			bundle.putString("answerById", getIntent().getStringExtra("answerById"));
			bundle.putString("title", "评论答案");
			LggsUtils.StartIntent(this, AnswerOrCommentActivity.class, bundle);
			break;
		}
	}

}
