package com.szgs.bbs.ui;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.adapter.MyAnswerAdapter;
import com.szgs.bbs.ask.AnswerListResponse;
import com.szgs.bbs.ask.QuestionDetailActivity;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.common.view.CircleImageView;

/**
 * 查看其他用户的信息
 * 
 * @author db
 * 
 */
public class OtherUserInformationActivity extends BaseActivity implements
		OnRefreshListener2<ListView> {

	private TextView tv_title;
	private View top_left_tv;
	private Intent myintent;
	private long userId;
	private String avatar;
	private String username;
	private int rank;
	private int totalBest;
	private PullToRefreshListView other_userinfor_pull;
	private View msg_view;
	private CircleImageView mine_user_icon;
	private DisplayImageOptions options;
	private TextView mine_adoption_rate;
	private TextView mine_integrate;
	private String percent;
	private int score;
	private ListView listView;
	private ArrayList<AnswerListResponse.Content> contentlist;
	private MyAnswerAdapter myAdapter;
	private TextView mine_username;
	private ProgressDialog progressdialog;

	private int SIZE = 10;
	private int PAGE = 1;
	private int mCurrentAction = PULL_DOWN;
	protected boolean last = true;
	private View empyt_view;
	/** 下拉刷新 */
	private static final int PULL_DOWN = 0x01;
	/** 上拉加载 */
	private static final int PULL_UP = 0x02;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_user_information);
		myintent = getIntent();
		userId = myintent.getLongExtra("userId", 0);
		avatar = myintent.getStringExtra("avatar");
		username = myintent.getStringExtra("username");
		rank = myintent.getIntExtra("rank", 0);
		totalBest = myintent.getIntExtra("totalBest", 0);
		percent = myintent.getStringExtra("percent");
		score = myintent.getIntExtra("score", 0);
		options = LggsUtils.inImageLoaderOptions();
		contentlist = new ArrayList<AnswerListResponse.Content>();
		initHeaderView();
		initView();
		sendRequset();
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
//		tv_title.setText("" + rank);
	}

	public void initView() {
		msg_view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.othermsg_top, null);

		mine_user_icon = (CircleImageView) msg_view
				.findViewById(R.id.mine_user_icon);
		mine_adoption_rate = (TextView) msg_view
				.findViewById(R.id.mine_adoption_rate);
		mine_username = (TextView) msg_view.findViewById(R.id.mine_username);
		mine_username.setText(username);
		mine_integrate = (TextView) msg_view.findViewById(R.id.mine_integrate);
		ImageLoader.getInstance().displayImage(avatar, mine_user_icon, options);
		mine_adoption_rate.setText(percent);
		mine_integrate.setText(score + "");
		other_userinfor_pull = (PullToRefreshListView) findViewById(R.id.other_userinfor_pull);
		other_userinfor_pull.setOnRefreshListener(this);
		other_userinfor_pull.setMode(Mode.PULL_FROM_END);
		listView = other_userinfor_pull.getRefreshableView();
		listView.addHeaderView(msg_view);
		empyt_view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.ll_otheruermsg_empty, null);
//		listView.addFooterView(empyt_view);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 1) {
					return;
				} else {
					Bundle bundle = new Bundle();
					bundle.putSerializable("questiondetail",
							myAdapter.getItem(position - 2).question);
					LggsUtils.StartIntent(OtherUserInformationActivity.this,
							QuestionDetailActivity.class, bundle);
				}
			}
		});
		myAdapter = new MyAnswerAdapter(OtherUserInformationActivity.this,
				contentlist);
		listView.setAdapter(myAdapter);
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

	public void sendRequset() {
		progressdialog = new ProgressDialog(OtherUserInformationActivity.this);
		progressdialog.setMessage("正在加载。。。");
		progressdialog.setCancelable(true);
		progressdialog.show();
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "answers/of_user";
		url = url + "?userId=" + userId + "&page=" + PAGE + "&size=" + SIZE;
		client.get(url, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				Gson gson = new Gson();
				if (statusCode == 200) {
					AnswerListResponse questionlistRep = gson.fromJson(
							response.toString(), AnswerListResponse.class);
					last = questionlistRep.last;
					if (last) {
						if (PAGE != 1) {
							LggsUtils.ShowToast(
									OtherUserInformationActivity.this,
									"已经是最后一页了");
						}
					}
					other_userinfor_pull.setMode(Mode.BOTH);
					if (mCurrentAction == PULL_DOWN) {
						contentlist.clear();
					}
					contentlist.addAll(questionlistRep.content);
					if (questionlistRep.content.size() < 1) {

					}
					if (contentlist.size() == 0) {
						listView.addFooterView(empyt_view);
						listView.setOnItemClickListener(null);
					}
					myAdapter.notifyDataSetChanged();
					
					// contentlist = questionlistRep.content;
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {

				LggsUtils.ShowToast(OtherUserInformationActivity.this,
						responseString);
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFinish() {
				other_userinfor_pull.onRefreshComplete();
				progressdialog.dismiss();
				super.onFinish();
			}
		});
	}
}
