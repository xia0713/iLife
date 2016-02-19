package com.szgs.bbs.answer;

import java.util.ArrayList;
import java.util.List;

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
import com.szgs.bbs.adapter.QuestionDetailAdapter;
import com.szgs.bbs.adapter.TheNewQuestionAdapter;
import com.szgs.bbs.ask.QuestionDetailActivity;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.QuestionListResponse;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.find.CategoryActivity;

public class TheLatestQuestionsActivity extends BaseActivity implements
		OnClickListener, OnRefreshListener2<ListView>, OnItemClickListener {

	private PullToRefreshListView new_question_pull;
	private TextView tv_title;
	private TextView top_left_tv;
	private ArrayList<String> testData;
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
	private TheNewQuestionAdapter myAdapter;
	private LinearLayout ll_nodata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_the_new_questions);
		contentlist = new ArrayList<QuestionListResponse.Question>();
		initHeaderView();
		initView();
		initData();
		sendRequset();
	}

	public void initData() {
		myAdapter = new TheNewQuestionAdapter(TheLatestQuestionsActivity.this,
				contentlist);
		new_question_pull.setAdapter(myAdapter);

	}

	public void initView() {
		ll_nodata = (LinearLayout) findViewById(R.id.ll_nodata);
		new_question_pull = (PullToRefreshListView) findViewById(R.id.new_question_pull);
		new_question_pull.setOnRefreshListener(this);
		new_question_pull.setOnItemClickListener(this);
		new_question_pull.setMode(Mode.BOTH);
	}

	public void initHeaderView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(this);
		tv_title.setText("最新问题");
	}

	public void sendRequset() {
		progressdialog = new ProgressDialog(TheLatestQuestionsActivity.this);
		progressdialog.setMessage("正在加载。。。");
		progressdialog.setCancelable(true);
		progressdialog.show();
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "questions/latest";
		RequestParams params = new RequestParams();
		// params.put("categoryId", catagoryId);
		params.put("page", PAGE);
		params.put("size", SIZE);
		client.setTimeout(5000);
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
						new_question_pull.setMode(Mode.BOTH);
					} else {
						if (PAGE != 1) {
							LggsUtils
									.ShowToast(TheLatestQuestionsActivity.this,
											"已经是最后一页了");
						}
						new_question_pull.setMode(Mode.BOTH);
					}
					if (mCurrentAction == PULL_DOWN) {
						contentlist.clear();
					}
					contentlist.addAll(questionlistRep.content);
					if (contentlist.size() == 0) {
						ll_nodata.setVisibility(View.VISIBLE);
					} else {
						ll_nodata.setVisibility(View.GONE);
					}
					myAdapter.notifyDataSetChanged();
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				if (statusCode == 401) {
					sendAutoLoginRequest();
				}
				LggsUtils.ShowToast(TheLatestQuestionsActivity.this,
						LggsUtils.replaceAll(responseString));
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(
						TheLatestQuestionsActivity.this,
						getResources().getString(
								R.string.network_connection_error));
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFinish() {
				progressdialog.dismiss();
				new_question_pull.onRefreshComplete();
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
		LggsUtils.StartIntent(TheLatestQuestionsActivity.this,
				QuestionDetailActivity.class, bundle);
	}
}
