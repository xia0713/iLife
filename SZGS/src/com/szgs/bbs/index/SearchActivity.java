package com.szgs.bbs.index;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
import com.szgs.bbs.adapter.SearchAdapter;
import com.szgs.bbs.ask.QuestionDetailActivity;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.QuestionListResponse;
import com.szgs.bbs.common.QuestionListResponse.Question;
import com.szgs.bbs.common.util.LggsUtils;

public class SearchActivity extends BaseActivity implements
		OnItemClickListener, OnRefreshListener2<ListView> {

	private EditText et_search;
	private TextView search_confirm;
	private PullToRefreshListView search_pull;
	private SearchAdapter myAdapter;
	private ArrayList<Question> contentlist;

	private int PAGE = 1;
	private int mCurrentAction = PULL_DOWN;
	protected boolean last = true;
	private ProgressDialog progressdialog;
	protected boolean isNotData = false;
	protected int inputLength = 0;
	/** 下拉刷新 */
	private static final int PULL_DOWN = 0x01;
	/** 上拉加载 */
	private static final int PULL_UP = 0x02;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		contentlist = new ArrayList<QuestionListResponse.Question>();
		initView();
		initData();
		initListener();
	}

	public void initView() {
		et_search = (EditText) findViewById(R.id.et_search);
		search_confirm = (TextView) findViewById(R.id.search_confirm);
		search_pull = (PullToRefreshListView) findViewById(R.id.search_pull);
		search_pull.setOnRefreshListener(this);
		search_pull.setOnItemClickListener(this);
		search_pull.setMode(Mode.BOTH);
	}

	public void initData() {
		myAdapter = new SearchAdapter(SearchActivity.this, contentlist);
		search_pull.setAdapter(myAdapter);
	}

	public void initListener() {
		et_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() != 0) {
					if (!(isNotData && (inputLength < s.length()))) {
						sendRequest(s.toString());
					}
				} else {
					contentlist.clear();
					myAdapter.notifyDataSetChanged();
				}
				inputLength = s.length();
			}
		});
		search_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void sendRequest(final String keyword) {
		progressdialog = new ProgressDialog(SearchActivity.this);
		progressdialog.setMessage("正在加载。。。");
		progressdialog.setCancelable(true);
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "/questions/by_title";
		RequestParams params = new RequestParams();
		params.put("keyword", keyword);
		params.put("page", PAGE);
		client.get(url, params, new JsonHttpResponseHandler() {

			private boolean last;

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				search_pull.onRefreshComplete();
				Gson gson = new Gson();
				if (statusCode == 200) {
					Log.i("Tag==", "请求成功");
					QuestionListResponse questionlistRep = gson.fromJson(
							response.toString(), QuestionListResponse.class);
					last = questionlistRep.last;
					if (last) {
						if (PAGE != 1) {
							LggsUtils
									.ShowToast(SearchActivity.this, "已经是最后一页了");
						}
					}
					search_pull.setMode(Mode.BOTH);
					if (mCurrentAction == PULL_DOWN) {
						contentlist.clear();
					}
					for (int i = 0; i < questionlistRep.content.size(); i++) {
						QuestionListResponse.Question question = questionlistRep.content
								.get(i);
						question.tag = "其他";
						contentlist.add(question);
					}
					if (contentlist.size() == 0) {
						isNotData = true;
					}
					myAdapter.SetKeyword(keyword);
					myAdapter.notifyDataSetChanged();
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				search_pull.onRefreshComplete();
				if (statusCode == 401) {
					sendAutoLoginRequest();
				} else {
					LggsUtils.ShowToast(SearchActivity.this, responseString);
				}
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> arg0) {
		mCurrentAction = PULL_DOWN;
		PAGE = 1;
		sendRequest(et_search.getText().toString());
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> arg0) {
		mCurrentAction = PULL_UP;
		++PAGE;
		sendRequest(et_search.getText().toString());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("questiondetail", contentlist.get(position - 1));
		LggsUtils.StartIntent(SearchActivity.this,
				QuestionDetailActivity.class, bundle);
	}
}
