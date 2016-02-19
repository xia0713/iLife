package com.szgs.bbs.mine;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.szgs.bbs.BaseFragment;
import com.szgs.bbs.R;
import com.szgs.bbs.adapter.ResponseMeAdapter;
import com.szgs.bbs.ask.QuestionDetailActivity;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.QuestionListResponse;
import com.szgs.bbs.common.QuestionListResponse.Question;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.HttpUtils;
import com.szgs.bbs.common.util.LggsUtils;

public class ResponseMeFragment extends BaseFragment implements
		OnRefreshListener2<ListView> {
	private View view;
	private TextView textview;
	private PullToRefreshListView pull_to_refresh;
	private int SIZE = 10;
	private int PAGE = 1;
	private int mCurrentAction = PULL_DOWN;
	private ProgressDialog progressdialog;
	private ArrayList<Question> contentlist;
	private ResponseMeAdapter myAdapter;
	private View ll_empty;
	/** 下拉刷新 */
	private static final int PULL_DOWN = 0x01;
	/** 上拉加载 */
	private static final int PULL_UP = 0x02;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.responseme_fragment, null);
		initView();
		contentlist = new ArrayList<QuestionListResponse.Question>();
		initData();
		sendRequset();
		initListener();
		return view;
	}

	public void initData() {
		myAdapter = new ResponseMeAdapter(getActivity(), contentlist);
		pull_to_refresh.setAdapter(myAdapter);
		pull_to_refresh.setOnRefreshListener(this);
		pull_to_refresh.setMode(Mode.BOTH);
	}

	public void initListener() {
		pull_to_refresh.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putString("issocool", "yes");
				bundle.putSerializable("questiondetail",
						contentlist.get(position - 1).question);
				LggsUtils.StartIntent(getActivity(),
						QuestionDetailActivity.class, bundle);
			}
		});
		pull_to_refresh.setOnRefreshListener(this);
	}

	private void initView() {
		pull_to_refresh = (PullToRefreshListView) view
				.findViewById(R.id.pull_to_refresh);
		ll_empty = view.findViewById(R.id.ll_empty);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> arg0) {
		mCurrentAction = PULL_DOWN;
		PAGE = 1;
		sendRequset();
		pull_to_refresh.setMode(Mode.BOTH);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> arg0) {
		++PAGE;
		mCurrentAction = PULL_UP;
		sendRequset();
	}

	public void sendRequset() {
		progressdialog = new ProgressDialog(getActivity());
		progressdialog.setMessage("正在加载。。。");
		progressdialog.setCancelable(true);
		progressdialog.show();
		AsyncHttpClient client = HttpUtils.getClient(getActivity());
		String url = Constans.URL + "answers/of_question_ask_by";
		RequestParams params = new RequestParams();
		params.put("askBy", CacheUtils.getUserId(getActivity()));
		params.put("page", PAGE);
		client.get(url, params, new JsonHttpResponseHandler() {
			private boolean last;

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				progressdialog.dismiss();
				Gson gson = new Gson();
				if (statusCode == 200) {

					QuestionListResponse questionlistRep = gson.fromJson(
							response.toString(), QuestionListResponse.class);
					last = questionlistRep.last;
					if (!last) {
						pull_to_refresh.setMode(Mode.BOTH);
					} else {
						if (PAGE != 1) {
							LggsUtils.ShowToast(getActivity(), "已经是最后一页了");
						}
						pull_to_refresh.setMode(Mode.BOTH);
					}
					if (mCurrentAction == PULL_DOWN) {
						contentlist.clear();
					}
					contentlist.addAll(questionlistRep.content);
					if (contentlist.size() < 1) {
						ll_empty.setVisibility(View.VISIBLE);
						pull_to_refresh.setVisibility(View.GONE);
					} else {
						ll_empty.setVisibility(View.GONE);
						pull_to_refresh.setVisibility(View.VISIBLE);
					}
					myAdapter.notifyDataSetChanged();
					// contentlist = questionlistRep.content;
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				progressdialog.dismiss();
				LggsUtils.ShowToast(getActivity(), responseString);
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFinish() {
				pull_to_refresh.onRefreshComplete();
				super.onFinish();
			}
		});
	}

}
