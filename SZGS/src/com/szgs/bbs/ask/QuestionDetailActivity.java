package com.szgs.bbs.ask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
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
import com.szgs.bbs.LoginActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.adapter.AnswersAdapter;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.QuestionListResponse;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.common.view.SelectPopupWindow;
import com.szgs.bbs.find.CategoryActivity;
import com.szgs.bbs.mine.MyQuestionsActivity;

public class QuestionDetailActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener, OnRefreshListener2<ListView> {

	private PullToRefreshListView question_detail_pull;
	private ListView listView;
	private AnswersAdapter adapter;
	private ImageView question_detail_go_top_iv;

	private TextView tv_question_date, tv_question_content;

	private boolean scrollFlag = false;// 标记是否滑动
	private int lastVisibleItemPosition = 0;// 标记上次滑动位置

	private Intent intent;
	private QuestionListResponse.Question question;
	private ProgressDialog myProgressDialog;
	private TextView top_right_tv;
	private RelativeLayout question_secore;
	private TextView tv_answerCount;
	private TextView tv_question_title;
	private View headerView;
	private TextView tv_answer_num;
	private TextView tv_answer_not;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_detail);
		intent = getIntent();
		String ff = intent.getStringExtra("issocool");
		if (ff != null) {
			QuestionListResponse.Question.QuestionDetail question1 = (QuestionListResponse.Question.QuestionDetail) intent
					.getSerializableExtra("questiondetail");
			if (question == null) {
				question = new QuestionListResponse.Question();
			}
			question.askBy = question1.askBy;
			question.title = question1.title;
			question.category = question1.category;
			question.answerCount = question1.answerCount;
			question.id = question1.id;
			question.rewardScore = question1.rewardScore;
			question.askTime = question1.askTime;
		} else {
			question = (QuestionListResponse.Question) intent
					.getSerializableExtra("questiondetail");
		}

		initHeaderView();
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mPage = 1;
		getQuestionContent();
		getAnswersList();
	}

	private void initView() {
		LayoutInflater mInflater = LayoutInflater.from(this);
		headerView = mInflater.inflate(R.layout.layout_question_detail_header,
				null);
		// 问题部分
		question_secore = (RelativeLayout) headerView
				.findViewById(R.id.question_secore);
		if (question.rewardScore != 0) {
			question_secore.setVisibility(View.VISIBLE);
			((TextView) headerView.findViewById(R.id.tv_question_score))
					.setText(question.rewardScore + "");
			;
		} else {
			question_secore.setVisibility(View.GONE);
		}
		((TextView) headerView.findViewById(R.id.tv_question_date))
				.setText(question.askTime.split(" ")[0]);
		tv_question_title = (TextView) headerView
				.findViewById(R.id.tv_question_title);
		tv_question_title.setText(question.title);
		tv_question_content = (TextView) headerView
				.findViewById(R.id.tv_question_content);
		tv_question_content.setVisibility(View.GONE);
		headerView.findViewById(R.id.question_label).setOnClickListener(this);
		((TextView) headerView.findViewById(R.id.tv_question_label))
				.setText(question.category.name);
		headerView.findViewById(R.id.question_label).setOnClickListener(this);
		tv_answerCount = (TextView) headerView.findViewById(R.id.tv_answer_num);
		tv_answer_not = (TextView) headerView.findViewById(R.id.tv_answer_not);
		if (question.answerCount == 0) {
			tv_answerCount.setVisibility(View.GONE);
			tv_answer_not.setVisibility(View.VISIBLE);

		} else {
			tv_answerCount.setVisibility(View.VISIBLE);
			tv_answer_not.setVisibility(View.GONE);
			tv_answerCount.setText(question.answerCount + "人回答");
		}

		headerView.findViewById(R.id.btn_answer).setOnClickListener(this);

		question_detail_go_top_iv = (ImageView) findViewById(R.id.question_detail_go_top_iv);
		question_detail_go_top_iv.setOnClickListener(this);

		// 答案部分
		question_detail_pull = (PullToRefreshListView) findViewById(R.id.question_detail_pull);
		question_detail_pull.setOnRefreshListener(this);
		listView = question_detail_pull.getRefreshableView();
		listView.addHeaderView(headerView);
		adapter = new AnswersAdapter(this, null);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(new OnScrollListener() {

			// @Override
			// public void onScrollStateChanged(AbsListView view, int
			// scrollState) {
			// // TODO Auto-generated method stub
			// switch (scrollState) {
			// // 当不滚动时
			// case OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
			// scrollFlag = false;
			// // 判断滚动到底部
			// if (listView.getLastVisiblePosition() == (listView.getCount() -
			// 1)) {
			// question_detail_go_top_iv.setVisibility(View.VISIBLE);
			// }
			// // 判断滚动到顶部
			// if (listView.getFirstVisiblePosition() == 0) {
			// question_detail_go_top_iv.setVisibility(View.GONE);
			// }
			//
			// break;
			// case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
			// scrollFlag = true;
			// break;
			// case OnScrollListener.SCROLL_STATE_FLING://
			// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
			// scrollFlag = false;
			// break;
			// }
			// }

			// /**
			// * firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
			// * visibleItemCount：当前能看见的列表项个数（小半个也算） totalItemCount：列表项共数
			// */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// 当开始滑动且ListView底部的Y轴点超出屏幕最大范围时，显示或隐藏顶部按钮
				// if (scrollFlag &&
				// ScreenUtil.getScreenViewBottomHeight(listView) >=
				// ScreenUtil.getScreenHeight(QuestionDetailActivity.this)) {
				// if (firstVisibleItem > lastVisibleItemPosition) {// 上滑
				// question_detail_go_top_iv.setVisibility(View.VISIBLE);
				// } else if (firstVisibleItem < lastVisibleItemPosition) {// 下滑
				// question_detail_go_top_iv.setVisibility(View.VISIBLE);
				// } else {
				// return;
				// }
				// lastVisibleItemPosition = firstVisibleItem;
				// }
				if (firstVisibleItem > 10) {
					question_detail_go_top_iv.setVisibility(view.VISIBLE);
				} else {
					question_detail_go_top_iv.setVisibility(view.GONE);
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void initHeaderView() {
		TextView top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(this);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		top_right_tv = (TextView) findViewById(R.id.top_right_tv);
		top_right_tv.setOnClickListener(this);
		top_right_tv.setBackgroundResource(R.drawable.navbar_edit_selector);
		top_right_tv.setVisibility(View.VISIBLE);
		if (question.askBy != null) {
			tv_title.setText(question.askBy.nickname + "的提问");
			if (question.askBy.id.equals(CacheUtils
					.getUserId(QuestionDetailActivity.this) + "")) {
				top_right_tv
						.setBackgroundResource(R.drawable.navbar_edit_selector);
				top_right_tv.setVisibility(View.VISIBLE);
			}
		}
	}

	private void getQuestionContent() {
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setCancelable(true);
		pd.setMessage("正在加载。。。");
		pd.show();
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "question";
		// ?questionId="+intent.getLongExtra("questionId", 0)+"&userId="+CacheUtils.getUserId(this)
		Log.i("question detail",
				"question id = "
						+ intent.getLongExtra("questionId", question.id));
		RequestParams params = new RequestParams();
		params.put("questionId", intent.getLongExtra("questionId", question.id));
		params.put("viewBy", CacheUtils.getUserId(this));
		client.setConnectTimeout(5000);
		client.get(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				Gson gson = new Gson();
				QuestionListResponse.Question questionResponse = gson.fromJson(
						response.toString(),
						QuestionListResponse.Question.class);
				tv_question_title.setText(questionResponse.title);
				((TextView) headerView.findViewById(R.id.tv_question_label))
						.setText(questionResponse.category.name);
				if (questionResponse.answerCount == 0) {
					tv_answerCount.setVisibility(View.GONE);
					tv_answer_not.setVisibility(View.VISIBLE);
				} else {
					tv_answerCount.setVisibility(View.VISIBLE);
					tv_answer_not.setVisibility(View.GONE);
					tv_answerCount
							.setText(questionResponse.answerCount + "人回答");
				}

				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				if (statusCode == 401) {
					sendAutoLoginRequest();
				} else {
					tv_question_content.setText(LggsUtils
							.replaceAll(responseString));
				}
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(QuestionDetailActivity.this, getResources()
						.getString(R.string.network_connection_error));
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFinish() {
				pd.dismiss();
				super.onFinish();
			}
		});

	}

	private int mPage = 1;
	private boolean isLastPage;
	private SelectPopupWindow popupWindow;
	protected boolean ishasBestAnswer = false;

	private void getAnswersList() {
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setCancelable(true);
		pd.setMessage("正在加载。。。");
		pd.show();
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "answers/of_question";
		RequestParams params = new RequestParams();
		params.put("questionId", intent.getLongExtra("questionId", question.id));
		params.put("page", mPage);
		params.put("size", 10);
		client.setConnectTimeout(5000);
		client.get(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				if (statusCode == 200 && response != null) {
					Gson gson = new Gson();
					QuestionAnswerListResponse rp = gson.fromJson(
							response.toString(),
							QuestionAnswerListResponse.class);
					// if (rp.pageAnswers.last) {
					// question_detail_pull.setMode(Mode.PULL_FROM_START);
					// } else {
					question_detail_pull.setMode(Mode.BOTH);
					// }
					if (mPage == 1) {
						adapter.setData(rp.pageAnswers.content);
					} else {
						adapter.addData(rp.pageAnswers.content);
					}
					
					if (rp.bestAnswer != null) {
						long id = rp.bestAnswer.id;
						adapter.setBestid(id);
						ishasBestAnswer = true;
					}
					if (mPage == 1 && rp.pageAnswers.content.size() == 0) {
						List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("title", "  ");
						list.add(map);
						listView.setAdapter(new SimpleAdapter(
								getApplicationContext(), list,
								R.layout.question_detail_empty,
								new String[] { "title" },
								new int[] { R.id.tv_empty }));
						listView.setDividerHeight(0);
					} else {
						listView.setVisibility(View.VISIBLE);
						listView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						++mPage;
					}

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				--mPage;
				if (statusCode == 401) {
					sendAutoLoginRequest();
				} else {
					tv_question_content.setText(LggsUtils
							.replaceAll(responseString));
				}

			}

			@Override
			public void onFinish() {
				pd.dismiss();
				question_detail_pull.onRefreshComplete();
				super.onFinish();
			}
		});
	}

	/**
	 * 滚动ListView到指定位置
	 * 
	 * @param pos
	 */
	private void setListViewPos(int pos) {
		if (android.os.Build.VERSION.SDK_INT >= 8) {
			listView.smoothScrollToPosition(pos);
		} else {
			listView.setSelection(pos);
		}
	}

	public void showpopupisMyQuestion() {
		LayoutInflater imgInflater = LayoutInflater
				.from(QuestionDetailActivity.this);
		View view = imgInflater.inflate(R.layout.modif_question_popup, null);
		TextView tv1 = (TextView) view.findViewById(R.id.modif_modification);
		TextView modif_seeall = (TextView) view.findViewById(R.id.modif_seeall);
		tv1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LggsUtils.setWindowAlpha(QuestionDetailActivity.this, 1f);
				popupWindow.dismiss();
				Bundle bundle = new Bundle();
				bundle.putBoolean("isEdit", true);
				bundle.putString("title", question.title);
				bundle.putLong("id", question.id);
				bundle.putInt("reward", question.rewardScore);
				bundle.putSerializable("category", question.category);
				LggsUtils.StartIntent(QuestionDetailActivity.this,
						AskIndexActivity.class, bundle);
			}
		});
		modif_seeall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LggsUtils.setWindowAlpha(QuestionDetailActivity.this, 1f);
				popupWindow.dismiss();
				LggsUtils.StartIntent(QuestionDetailActivity.this,
						MyQuestionsActivity.class);
			}
		});
		// modif_seeall.setVisibility(View.GONE);
		popupWindow = new SelectPopupWindow(
				(Activity) QuestionDetailActivity.this, view);
		popupWindow.showAtLocation(top_right_tv, Gravity.BOTTOM, 0, 0);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(false);
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				LggsUtils.setWindowAlpha(QuestionDetailActivity.this, 1f);

			}
		});
		LggsUtils.setWindowAlpha(QuestionDetailActivity.this, 0.4f);
	}

	public void showpopupisOtherQuestion() {

		LayoutInflater imgInflater = LayoutInflater
				.from(QuestionDetailActivity.this);
		View view = imgInflater.inflate(R.layout.popupwindow_answer_detail,
				null);
		view.findViewById(R.id.popup_adopt).setVisibility(View.GONE);
		TextView tv_popup_inform = (TextView) view
				.findViewById(R.id.popup_inform);
		tv_popup_inform.setText("举报问题");
		tv_popup_inform.setTextColor(getResources().getColor(R.color.red));
		view.findViewById(R.id.popup_inform).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						setWindowAlpha(1f);
						popupWindow.dismiss();
						if (CacheUtils.getUserId(QuestionDetailActivity.this) == -1) {
							LggsUtils.showMyToast(QuestionDetailActivity.this,
									null, "暂不支持匿名举报", "请登录后再进行举报");
						} else {
							LggsUtils.showDelationDialog(
									QuestionDetailActivity.this,
									new OnClickListener() {

										@Override
										public void onClick(View v) {
											delationQuestion();
										}
									}, null, true);
						}
					}

				});
		view.findViewById(R.id.popup_cancel).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						setWindowAlpha(1f);
						popupWindow.dismiss();
					}
				});

		// modif_seeall.setVisibility(View.GONE);
		popupWindow = new SelectPopupWindow(
				(Activity) QuestionDetailActivity.this, view);
		popupWindow.showAtLocation(top_right_tv, Gravity.BOTTOM, 0, 0);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(false);
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				LggsUtils.setWindowAlpha(QuestionDetailActivity.this, 1f);

			}
		});
		LggsUtils.setWindowAlpha(QuestionDetailActivity.this, 0.4f);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_right_tv:
			if (question.askBy.id.equals(CacheUtils
					.getUserId(QuestionDetailActivity.this) + "")) {
				showpopupisMyQuestion();
			} else {
				showpopupisOtherQuestion();
			}

			break;
		case R.id.top_left_tv:
			finish();
			break;
		case R.id.question_detail_go_top_iv:
			setListViewPos(0);
			break;
		case R.id.question_label:// topic 标签
			Bundle bundle = new Bundle();
			bundle.putLong("catagoryId", question.category.id);
			bundle.putString("catagoryName", question.category.name);
			LggsUtils.StartIntent(this, CategoryActivity.class, bundle);
			break;
		case R.id.btn_answer:
			if (CacheUtils.getUserId(QuestionDetailActivity.this) != -1) {
				if (question.askBy.id.equals(CacheUtils
						.getUserId(QuestionDetailActivity.this) + "")) {
					LggsUtils.ShowToast(QuestionDetailActivity.this,
							"不能回答自己的提问");
				} else {
					Intent intent = new Intent(this,
							AnswerOrCommentActivity.class);
					intent.putExtra("title", "回答");
					intent.putExtra("questionId", question.id);
					intent.putExtra("askById", question.askBy.id + "");
					// 还有其他参数要传
					startActivity(intent);
				}
			} else {
				LggsUtils.StartIntent(QuestionDetailActivity.this,
						LoginActivity.class);
			}

			break;
		}

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> arg0) {
		mPage = 1;
		getAnswersList();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> arg0) {
		getAnswersList();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (CacheUtils.getUserId(QuestionDetailActivity.this) != -1) {
			Bundle bundle = new Bundle();
			bundle.putLong("answerId", adapter.getItem(position - 2).id);
			bundle.putInt("againstCount",
					adapter.getItem(position - 2).againstCount);
			bundle.putInt("favouritesCount",
					adapter.getItem(position - 2).favouritesCount);
			bundle.putInt("agreeCount",
					adapter.getItem(position - 2).agreeCount);
			bundle.putInt("commentCount",
					adapter.getItem(position - 2).commentCount);
			bundle.putString("nickname",
					adapter.getItem(position - 2).answerBy.nickname);
			bundle.putString("answerbyid",
					adapter.getItem(position - 2).answerBy.id);
			bundle.putString("questionbyid", question.askBy.id);
			bundle.putBoolean("ishasBestAnswer", ishasBestAnswer);
			bundle.putLong("questionId", question.id);
			LggsUtils.StartIntent(this, AnswerDetailActivity.class, bundle);
		} else {
			LggsUtils.StartIntent(QuestionDetailActivity.this,
					LoginActivity.class);
		}

	}

	public void delationQuestion() {

		AsyncHttpClient client = getClient();
		String url = Constans.URL + "delation/question";
		RequestParams params = new RequestParams();
		params.put("questionId", question.id);
		params.put("userId", CacheUtils.getUserId(QuestionDetailActivity.this));
		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {

				super.onSuccess(statusCode, headers, responseString);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				if (statusCode == 401) {
					sendAutoLoginRequest();
				} else if (statusCode == 200) {
					LggsUtils.showMyToast(QuestionDetailActivity.this, "举报成功",
							"我们将尽快核查", "并进行处理");
				} else {
					LggsUtils.ShowToast(QuestionDetailActivity.this,
							LggsUtils.replaceAll(responseString));
				}
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});

	}
}
