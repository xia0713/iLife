package com.szgs.bbs.answer;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.szgs.bbs.BaseFragment;
import com.szgs.bbs.R;
import com.szgs.bbs.adapter.HomeAdapter;
import com.szgs.bbs.adapter.QuestionDetailAdapter;
import com.szgs.bbs.ask.QuestionDetailActivity;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.QuestionListResponse;
import com.szgs.bbs.common.util.HttpUtils;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.find.CategoryQuestionListResponse;

public class MyInterestingQustionFragment extends BaseFragment implements
		OnItemClickListener, OnRefreshListener2<ListView> {

	private PullToRefreshListView my_interesting_questions_pull;
	private HomeAdapter adapter;
	private long categoryId;
	private ListView listView;
	private ProgressDialog progressdialog;
	private int SIZE = 10;
	private int PAGE = 1;
	protected boolean last = true;
	/** 下拉刷新 */
	private static final int PULL_DOWN = 0x01;
	/** 上拉加载 */
	private static final int PULL_UP = 0x02;
	ArrayList<QuestionListResponse.Question> contentlist;
	private int mCurrentAction = PULL_DOWN;

	private Context context;

	@SuppressLint("ValidFragment")
	public MyInterestingQustionFragment(long categoryId, Context context) {
		this.categoryId = categoryId;
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(
				R.layout.fragment_my_interesting_question, null);
		my_interesting_questions_pull = (PullToRefreshListView) layout
				.findViewById(R.id.my_interesting_questions_pull);
		contentlist = new ArrayList<QuestionListResponse.Question>();
		// listView = my_interesting_questions_pull.getRefreshableView();
		// listView.setDivider(new
		// ColorDrawable(Color.parseColor("#FFFFFFFF")));
		// listView.setDividerHeight((int) TypedValue.applyDimension(
		// TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
		// .getDisplayMetrics()));
		// adapter = new UserQuestionsAdapter(context, null);
		// listView.setAdapter(adapter);
		return layout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		adapter = new HomeAdapter(context, contentlist);
		my_interesting_questions_pull.setAdapter(adapter);
		my_interesting_questions_pull.setOnRefreshListener(this);
		my_interesting_questions_pull.setMode(Mode.BOTH);
		my_interesting_questions_pull.setOnItemClickListener(this);
		sendRequset();
		super.onActivityCreated(savedInstanceState);
	}

	public void sendRequset() {
		if (categoryId < 0) {
			return;
		}
		progressdialog = new ProgressDialog(context);
		progressdialog.setMessage("正在加载。。。");
		progressdialog.setCancelable(true);
		progressdialog.show();
		AsyncHttpClient client = HttpUtils.getClient(getActivity());
		String url = Constans.URL + "questions/by_category";
		url = url + "?categoryId=" + categoryId + "&page=" + PAGE + "&size="
				+ SIZE;
		client.get(url, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				Gson gson = new Gson();
				if (statusCode == 200) {
					CategoryQuestionListResponse questionlistRep = gson
							.fromJson(response.toString(),
									CategoryQuestionListResponse.class);
					last = questionlistRep.questions.last;
					if (!last) {
						my_interesting_questions_pull.setMode(Mode.BOTH);
					} else {
						my_interesting_questions_pull.setMode(Mode.BOTH);
						if (PAGE != 1) {
							LggsUtils.ShowToast(context, "已经是最后一页了");
						}
					}
					if (mCurrentAction == PULL_DOWN) {
						contentlist.clear();
					}
					for (int i = 0; i < questionlistRep.questions.content
							.size(); i++) {
						QuestionListResponse.Question question = questionlistRep.questions.content
								.get(i);
						question.tag = "其他";
						contentlist.add(question);
					}
					adapter.notifyDataSetChanged();
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
           if(statusCode==401){
	              HttpUtils.sendAutoLoginRequest(getActivity());
                  }
				LggsUtils.ShowToast(context,
						LggsUtils.replaceAll(responseString));
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFinish() {
				my_interesting_questions_pull.onRefreshComplete();
				progressdialog.dismiss();
				super.onFinish();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(
						context,
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
		LggsUtils.StartIntent(context, QuestionDetailActivity.class, bundle);
	}
}
