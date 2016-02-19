package com.szgs.bbs.find;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.adapter.CategoryListAdapter;
import com.szgs.bbs.adapter.QuestionDetailAdapter;
import com.szgs.bbs.ask.QuestionDetailActivity;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.QuestionListResponse;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.index.SearchActivity;

/**
 * 
 * 分区
 * 
 * @author db
 * 
 */
public class CategoryActivity extends BaseActivity implements OnClickListener,
		OnRefreshListener2<ListView> {

	private TextView tv_title;
	private TextView top_left_tv;
	private long catagoryId;
	private PullToRefreshListView category_pull;
	private ListView listView;
	private int SIZE = 10;
	private int PAGE = 1;
	private int mCurrentAction = PULL_DOWN;
	/** 下拉刷新 */
	private static final int PULL_DOWN = 0x01;
	/** 上拉加载 */
	private static final int PULL_UP = 0x02;

	private ProgressDialog progressdialog;
	ArrayList<QuestionListResponse.Question> contentlist;
	private CategoryListAdapter myAdapter;
	protected boolean last = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		contentlist = new ArrayList<QuestionListResponse.Question>();
		initHeaderView();
		initView();
		initData();
		sendRequset();
		initListener();
	}

	public void initHeaderView() {
		String title = getIntent().getStringExtra("catagoryName");
		catagoryId = getIntent().getLongExtra("catagoryId", 0);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(title);
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(this);
	}

	public void initView() {
		category_pull = (PullToRefreshListView) findViewById(R.id.category_pull);
		listView = category_pull.getRefreshableView();
	}

	public void initData() {
		myAdapter = new CategoryListAdapter(CategoryActivity.this,
				contentlist);
		category_pull.setAdapter(myAdapter);
		category_pull.setOnRefreshListener(this);
		category_pull.setMode(Mode.BOTH);
	}

	public void initListener() {
		category_pull.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bundle = new Bundle();
				if (contentlist.get(position - 1).question != null) {
					bundle.putString("issocool", "yes");
					bundle.putSerializable("questiondetail",
							contentlist.get(position - 1).question);
				} else {
					bundle.putSerializable("questiondetail",
							contentlist.get(position - 1));
				}
				LggsUtils.StartIntent(CategoryActivity.this,
						QuestionDetailActivity.class, bundle);
			}
		});
		category_pull.setOnRefreshListener(this);
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

	public void sendRequset() {
		progressdialog = new ProgressDialog(CategoryActivity.this);
		progressdialog.setMessage("正在加载。。。");
		progressdialog.setCancelable(true);
		progressdialog.show();
		AsyncHttpClient client =getClient();
		String url = Constans.URL + "questions/by_category";
		url = url + "?categoryId=" + catagoryId + "&page=" + PAGE + "&size="
				+ SIZE;
		client.setTimeout(5000);
		client.get(url, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				Gson gson = new Gson();
				if (statusCode == 200) {
					CategoryQuestionListResponse rp = gson.fromJson(
							response.toString(),
							CategoryQuestionListResponse.class);
					last = rp.questions.last;
					if (!last) {
						category_pull.setMode(Mode.BOTH);
					} else {
						if (PAGE != 1) {
							LggsUtils.ShowToast(CategoryActivity.this,
									"已经是最后一页了");
						}
						category_pull.setMode(Mode.BOTH);
					}
					if (mCurrentAction == PULL_DOWN) {
						contentlist.clear();
					}
					if (rp.gloryAnswer != null && PAGE == 1) {
						contentlist.add(rp.gloryAnswer);
					}
					if (rp.questions.content != null) {
						myAdapter.hotAnswerCount = rp.hotAnswerCount;
						contentlist.addAll(rp.questions.content);
					}
					myAdapter.notifyDataSetChanged();
					// contentlist = questionlistRep.content;
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				if (statusCode == 401) {
					sendAutoLoginRequest();
				}else{
					LggsUtils.ShowToast(CategoryActivity.this, responseString);
				}
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(CategoryActivity.this, getResources()
						.getString(R.string.network_connection_error));
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFinish() {
				super.onFinish();
				progressdialog.dismiss();
				category_pull.onRefreshComplete();
			}
		});
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> arg0) {
		mCurrentAction = PULL_DOWN;
		PAGE = 1;
		sendRequset();
		category_pull.setMode(Mode.BOTH);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> arg0) {
		++PAGE;
		mCurrentAction = PULL_UP;
		sendRequset();
	}

}
