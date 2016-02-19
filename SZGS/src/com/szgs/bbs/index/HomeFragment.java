package com.szgs.bbs.index;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.szgs.bbs.LoginActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.adapter.HomeAdapter;
import com.szgs.bbs.ask.QuestionDetailActivity;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.QuestionListResponse;
import com.szgs.bbs.common.QuestionListResponse.Question;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.HttpUtils;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.common.util.SharedPranceUtils;
import com.szgs.bbs.common.view.DrawableCenterTextView;
import com.szgs.bbs.mine.NotificationActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class HomeFragment extends Fragment implements OnClickListener,
		OnItemClickListener, OnRefreshListener2<ListView> {

	private Context context;

	private ImageView home_notification;
	private DrawableCenterTextView home_search;

	private PullToRefreshListView pullView;
	private ListView listView;
	private HomeAdapter myAdapter;
	private ArrayList<Question> contentlist;

	private int PAGE = 1;
	private int mCurrentAction = PULL_DOWN;
	protected boolean last = true;
	/** 下拉刷新 */
	private static final int PULL_DOWN = 0x01;
	/** 上拉加载 */
	private static final int PULL_UP = 0x02;

	private ProgressDialog progressdialog;
	private boolean istoAsk = false;

	private ImageView home_notification_point;

	public boolean isIstoAsk() {
		return istoAsk;
	}

	public void setIstoAsk(boolean istoAsk) {
		this.istoAsk = istoAsk;
	}

	public HomeFragment(Context context) {
		super();
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, null);
		home_notification = (ImageView) view
				.findViewById(R.id.home_notification);
		home_notification.setOnClickListener(this);
		home_search = (DrawableCenterTextView) view
				.findViewById(R.id.home_search);
		home_notification_point = (ImageView) view
				.findViewById(R.id.home_notification_point);
		home_search.setOnClickListener(this);
		pullView = (PullToRefreshListView) view.findViewById(R.id.home_pull);
		initData();
		initListener();
		sendRequestHot();
		IntentFilter filter = new IntentFilter();
		filter.addAction("action.refresh");
		context.registerReceiver(mRefreshBroadcastReceiver, filter);
		return view;
	}

	@Override
	public void onResume() {
		// contentlist.clear();
		// PAGE = 1;
		if (istoAsk) {
			istoAsk = false;
			contentlist.clear();
			PAGE = 1;
			sendRequestHot();
		}
		if (SharedPranceUtils.GetBolDate("hasnewmsg", context, false)) {
			home_notification_point.setVisibility(View.VISIBLE);
		} else {
			home_notification_point.setVisibility(View.GONE);
		}
		super.onResume();
	}

	private void initListener() {
		pullView.setOnRefreshListener(this);
		pullView.setOnItemClickListener(this);

	}

	public void initData() {
		contentlist = new ArrayList<QuestionListResponse.Question>();
		myAdapter = new HomeAdapter(getActivity(), contentlist);
		pullView.setAdapter(myAdapter);
		pullView.setMode(Mode.BOTH);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_search:
			LggsUtils.StartIntent(context, SearchActivity.class);
			break;
		case R.id.home_notification:

			if (CacheUtils.getUserId(getActivity()) != -1) {
				LggsUtils.StartIntent(context, NotificationActivity.class);
			} else {
				LggsUtils.StartIntent(context, LoginActivity.class);
			}

			break;
		default:
			break;
		}

	}

	public void sendRequestHot() {

		progressdialog = new ProgressDialog(getActivity());
		progressdialog.setMessage("正在加载。。。");
		progressdialog.setCancelable(true);
		// progressdialog.show();
		AsyncHttpClient client = HttpUtils.getClient(getActivity());
		String url = Constans.URL + "home/hot";
		RequestParams params = new RequestParams();
		params.put("userId", CacheUtils.getUserId(getActivity()));
		client.get(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				pullView.setMode(Mode.PULL_FROM_START);
				Gson gson = new Gson();
				if (statusCode == 200) {
					Log.i("Tag==", "请求成功");
					if (mCurrentAction == PULL_DOWN) {
						contentlist.clear();
					}
					IndexHotResponse hotResp = gson.fromJson(
							response.toString(), IndexHotResponse.class);
					if (hotResp.myQuestion != null) {
						hotResp.myQuestion.tag = "我的提问";
						contentlist.add(hotResp.myQuestion);
					}
					if (hotResp.hotQuestion != null) {
						hotResp.hotQuestion.tag = "热门提问";
						contentlist.add(hotResp.hotQuestion);
					}
					if (hotResp.hotAnswer != null) {
						hotResp.hotAnswer.tag = "热门回答";
						contentlist.add(hotResp.hotAnswer);
					}
					myAdapter.notifyDataSetChanged();
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				LggsUtils.ShowToast(getActivity(), responseString);
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFinish() {
				// progressdialog.dismiss();
				sendRequest();
				super.onFinish();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(
						getActivity(),
						getResources().getString(
								R.string.network_connection_error));
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	public void sendRequest() {

		progressdialog = new ProgressDialog(getActivity());
		progressdialog.setMessage("正在加载。。。");
		progressdialog.setCancelable(true);
		// progressdialog.show();
		AsyncHttpClient client = HttpUtils.getClient(getActivity());
		String url = Constans.URL + "home/questions";
		RequestParams params = new RequestParams();
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
						pullView.setMode(Mode.BOTH);
					} else {
						if (PAGE != 1) {
							LggsUtils.ShowToast(getActivity(), "已经是最后一页了");
						}
						pullView.setMode(Mode.BOTH);
					}
					for (int i = 0; i < questionlistRep.content.size(); i++) {
						QuestionListResponse.Question question = questionlistRep.content
								.get(i);
						question.tag = "其他";
						contentlist.add(question);
					}
					// contentlist.addAll(questionlistRep.content);
					myAdapter.notifyDataSetChanged();
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				LggsUtils.ShowToast(getActivity(), responseString);
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFinish() {
				pullView.onRefreshComplete();
				// progressdialog.dismiss();
				super.onFinish();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(
						getActivity(),
						getResources().getString(
								R.string.network_connection_error));
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> arg0) {
		mCurrentAction = PULL_DOWN;
		PAGE = 1;
		sendRequestHot();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> arg0) {
		++PAGE;
		mCurrentAction = PULL_UP;
		sendRequest();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Bundle bundle = new Bundle();
		if (contentlist.get(position - 1).tag.equals("热门回答")) {
			bundle.putString("issocool", "yes");
			bundle.putSerializable("questiondetail",
					contentlist.get(position - 1).question);
		} else {
			bundle.putSerializable("questiondetail",
					contentlist.get(position - 1));
		}

		LggsUtils.StartIntent(getActivity(), QuestionDetailActivity.class,
				bundle);
	}

	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("action.refresh")) {
				if (SharedPranceUtils.GetBolDate("hasnewmsg", context, false)) {
					home_notification_point.setVisibility(View.VISIBLE);
				}
			}
		}
	};

}
