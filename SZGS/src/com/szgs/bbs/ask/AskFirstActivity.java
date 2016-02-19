package com.szgs.bbs.ask;

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
import android.widget.Button;
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
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.QuestionListResponse;
import com.szgs.bbs.common.util.LggsUtils;

public class AskFirstActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener, OnRefreshListener2<ListView> {

	private EditText ask_input_question;
	private Button ask_needtoask;
	private PullToRefreshListView aks_first_pull;
	private TextView tv_title;
	private TextView top_left_tv;
	private ProgressDialog progressdialog;
	private int PAGE = 1;
	private int mCurrentAction = PULL_DOWN;
	protected boolean last = true;
	/** 下拉刷新 */
	private static final int PULL_DOWN = 0x01;
	/** 上拉加载 */
	private static final int PULL_UP = 0x02;
	ArrayList<QuestionListResponse.Question> contentlist;
	private SearchAdapter myAdapter;
	protected boolean isNotData = false;
	protected int inputLength = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask_first);
		contentlist = new ArrayList<QuestionListResponse.Question>();
		initView();
		initHeaderView();
		initData();
		initListener();
	}

	public void initView() {
		ask_input_question = (EditText) findViewById(R.id.ask_input_question);
		ask_needtoask = (Button) findViewById(R.id.ask_needtoask);
		ask_needtoask.setOnClickListener(this);
		aks_first_pull = (PullToRefreshListView) findViewById(R.id.aks_first_pull);
		aks_first_pull.setOnRefreshListener(this);
		aks_first_pull.setOnItemClickListener(this);
	}

	public void initData() {
		myAdapter = new SearchAdapter(AskFirstActivity.this, contentlist);
		aks_first_pull.setAdapter(myAdapter);

	}

	public void initListener() {
		ask_input_question.addTextChangedListener(new TextWatcher() {

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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_left_tv:
			finish();
			break;
		case R.id.ask_needtoask:
			Bundle bundle = new Bundle();
			bundle.putString("questiontitle", ask_input_question.getText()
					.toString());
			LggsUtils.StartIntent(AskFirstActivity.this,
					AskIndexActivity.class, bundle);
			overridePendingTransition(R.anim.zoomin,
					R.anim.zoomout);
			finish();
			break;

		default:
			break;
		}

	}

	public void initHeaderView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("提问");
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setOnClickListener(this);
		top_left_tv.setText("取消");
	}

	public void sendRequest(final String keyword) {
		progressdialog = new ProgressDialog(AskFirstActivity.this);
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
				Gson gson = new Gson();
				if (statusCode == 200) {
					Log.i("Tag==", "请求成功");
					QuestionListResponse questionlistRep = gson.fromJson(
							response.toString(), QuestionListResponse.class);
					last = questionlistRep.last;
					if (!last) {
						aks_first_pull.setMode(Mode.PULL_FROM_END);
					} else {
						aks_first_pull.setMode(Mode.PULL_FROM_START);
					}
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
				if (statusCode == 401) {
					sendAutoLoginRequest();
				} else {
					LggsUtils.ShowToast(AskFirstActivity.this, responseString);
				}

				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFinish() {
				aks_first_pull.onRefreshComplete();
				super.onFinish();
			}
		});
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> arg0) {
		mCurrentAction = PULL_DOWN;
		PAGE = 1;
		sendRequest(ask_input_question.getText().toString());
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> arg0) {
		mCurrentAction = PULL_UP;
		++PAGE;
		sendRequest(ask_input_question.getText().toString());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Bundle bundle = new Bundle();
		bundle.putSerializable("questiondetail", contentlist.get(position - 1));
		LggsUtils.StartIntent(AskFirstActivity.this,
				QuestionDetailActivity.class, bundle);
	}
}
