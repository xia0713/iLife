package com.szgs.bbs.ask;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;

/**
 * 回答详情 可对回答进行点赞等操作
 * 
 * @author db
 * 
 */
public class AnswerDetailActivity extends BaseActivity implements
		OnClickListener {

	private RadioGroup bottom_radiogroup;
	private CheckedTextView answer_like;
	private CheckedTextView answer_collection;
	private long answerId;
	private boolean likeChecked;
	private TextView top_right_tv;
	private int ZAN = 0X12;
	private int isFromzan;
	private int CANCELZAN = 0X10;
	private int agreeCount;
	private int favouritesCount;
	private int againstCount;
	private int commentCount;
	private TextView answer_content;
	private String nickname;
	private boolean collectionChecked;
	private String questionbyid;
	private long questionId;
	private String answerbyid;
	private TextView tv_like_sum;
	private TextView tv_collection_sum;
	private TextView answer_adopt;
	private TextView answer_accept;
	private boolean ishasBestAnswer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer_detail);
		answerId = getIntent().getLongExtra("answerId", 0);// 回答ID
		agreeCount = getIntent().getIntExtra("agreeCount", 0);
		favouritesCount = getIntent().getIntExtra("favouritesCount", 0);
		againstCount = getIntent().getIntExtra("againstCount", 0);
		commentCount = getIntent().getIntExtra("commentCount", 0);
		nickname = getIntent().getStringExtra("nickname");
		questionbyid = getIntent().getStringExtra("questionbyid");// 原提问者ID
		questionId = getIntent().getLongExtra("questionId", 0);// 问题ID
		answerbyid = getIntent().getStringExtra("answerbyid");// 回答者id
		ishasBestAnswer = getIntent().getBooleanExtra("ishasBestAnswer", false);
		initHeaderView();
		initView();
		requestContent();
		Associated();
	}

	public void initView() {
		// bottom_radiogroup = (RadioGroup)
		// findViewById(R.id.bottom_radiogroup);
		// bottom_radiogroup.setOnCheckedChangeListener(this);
		answer_content = (TextView) findViewById(R.id.answer_content);
		// 设置textview不包裹scrollview也能滑动
		answer_content.setMovementMethod(new ScrollingMovementMethod());
		answer_like = (CheckedTextView) findViewById(R.id.answer_like);
		answer_like.setOnClickListener(this);
		answer_collection = (CheckedTextView) findViewById(R.id.answer_collection);
		answer_collection.setOnClickListener(this);
		tv_like_sum = (TextView) findViewById(R.id.tv_like_sum);
		tv_collection_sum = (TextView) findViewById(R.id.tv_collection_sum);
		tv_like_sum.setText(agreeCount + "");
		tv_collection_sum.setText("" + favouritesCount);
		answer_accept = (TextView) findViewById(R.id.answer_accept);
		answer_accept.setOnClickListener(this);
		answer_adopt = (TextView) findViewById(R.id.answer_adopt);
		answer_adopt.setOnClickListener(this);
		if (ishasBestAnswer) {
			answer_accept.setSelected(false);
		} else {
			answer_accept.setSelected(true);
		}
	}

	public void initHeaderView() {
		TextView top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(this);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(nickname);
		// top_right_tv = (TextView) findViewById(R.id.top_right_tv);
		// top_right_tv.setBackgroundResource(R.drawable.navbar_edit_selector);
		// top_right_tv.setVisibility(View.VISIBLE);
		// top_right_tv.setOnClickListener(this);
		// tv_title.setText(categort.askBy.nickname + "的提问");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_left_tv:
			finish();
			break;
		case R.id.answer_like:
			isFromzan = ZAN;
			if (likeChecked) {
				requestCancelVote();
			} else {
				requestVote(true);
			}
			break;
		case R.id.answer_collection:
			isFromzan = CANCELZAN;
			if (collectionChecked) {
				requestCancelCollect();
			} else {
				requestCollect();
			}
			break;
		case R.id.top_right_tv:
			setWindowAlpha(0.4f);
			hideKeybord(top_right_tv);

			SelectPopupWindow popup = new SelectPopupWindow(
					AnswerDetailActivity.this);
			popup.showAtLocation(top_right_tv, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
			popup.setOutsideTouchable(true);
			popup.setFocusable(false);
			popup.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					// 背景变亮
					setWindowAlpha(1f);
				}
			});
			break;
		case R.id.answer_accept:
			if (questionbyid.equals(CacheUtils
					.getUserId(getApplicationContext()) + "")) {
				if (ishasBestAnswer) {
					LggsUtils.ShowToast(AnswerDetailActivity.this, "您已采纳过最佳答案");
				} else {
					requestAdopt();
				}
			} else {
				LggsUtils.showMyToast(getApplicationContext(), null,
						"只有提问人才可以", "对回答进行采纳哦");
			}
			break;
		case R.id.answer_adopt:
			if (answerbyid.equals(CacheUtils
					.getUserId(AnswerDetailActivity.this) + "")) {
				LggsUtils.showDelationSelfDialog(AnswerDetailActivity.this,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								sendRequestInfrom();
							}
						}, null, true);
			} else {
				LggsUtils.showDelationDialog(AnswerDetailActivity.this,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								sendRequestInfrom();
							}
						}, null, true);
			}

		default:
			break;
		}

	}

	/**
	 * 赞同、反对回答
	 * 
	 * @param isAgree
	 */
	public void requestVote(boolean isAgree) {
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "answer/vote";
		RequestParams params = new RequestParams();
		params.put("answerId", answerId);
		params.put("voteBy", CacheUtils.getUserId(AnswerDetailActivity.this));
		params.put("isAgree", isAgree);
		client.post(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == 200) {
					if (isFromzan == ZAN) {
						answer_like.setChecked(true);
						int zansum = Integer.parseInt(tv_like_sum.getText()
								.toString());
						zansum++;
						tv_like_sum.setText("" + zansum);
					} else {
						if (answer_like.isChecked()) {
							int zansum = Integer.parseInt(tv_like_sum.getText()
									.toString());
							zansum--;
							tv_like_sum.setText("" + zansum);
						}
						answer_like.setChecked(false);
					}
				}
				likeChecked = answer_like.isChecked();
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				if (statusCode == 401) {
					sendAutoLoginRequest();
				} else {
					LggsUtils.ShowToast(AnswerDetailActivity.this,
							LggsUtils.replaceAll(responseString));
				}
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFinish() {

				super.onFinish();
			}
		});
	}

	/**
	 * 取消对回答的赞同、反对
	 */
	public void requestCancelVote() {
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "answer/cancel_vote";
		RequestParams params = new RequestParams();
		params.put("answerId", answerId);
		params.put("voteBy", CacheUtils.getUserId(AnswerDetailActivity.this));
		client.post(url, params, new JsonHttpResponseHandler() {

			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {

			};

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				if (statusCode == 200) {
					if (isFromzan == ZAN) {
						answer_like.setChecked(false);
						int zansum = Integer.parseInt(tv_like_sum.getText()
								.toString());
						zansum--;
						tv_like_sum.setText("" + zansum);
					}
				}
				if (statusCode == 401) {
					sendAutoLoginRequest();
				} else {
					LggsUtils.ShowToast(AnswerDetailActivity.this,
							LggsUtils.replaceAll(responseString));
				}
				likeChecked = answer_like.isChecked();
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	/**
	 * 获取回答的内容
	 * 
	 * @param isAgree
	 */
	public void requestContent() {
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "answer/content";
		RequestParams params = new RequestParams();
		params.put("answerId", answerId);
		params.put("userId", CacheUtils.getUserId(AnswerDetailActivity.this));
		client.get(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				super.onSuccess(statusCode, headers, responseString);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				responseString = LggsUtils.replaceAll(responseString);
				if (statusCode == 200) {
					answer_content.setText(responseString + "");
				} else if (statusCode == 401) {
					sendAutoLoginRequest();
				} else {
					LggsUtils.ShowToast(AnswerDetailActivity.this,
							LggsUtils.replaceAll(responseString));
				}
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	/**
	 * 收藏回答
	 * 
	 */
	public void requestCollect() {
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "answer/collect";
		RequestParams params = new RequestParams();
		params.put("answerId", answerId);
		params.put("userId", CacheUtils.getUserId(AnswerDetailActivity.this));
		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == 200) {
					LggsUtils.ShowToast(AnswerDetailActivity.this, "收藏成功");
					int sum = Integer.parseInt(tv_collection_sum.getText()
							.toString());
					sum++;
					tv_collection_sum.setText("" + sum);
					answer_collection.setChecked(true);
				}
				collectionChecked = answer_collection.isChecked();
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				if (statusCode == 401) {
					sendAutoLoginRequest();
				} else {
					LggsUtils.ShowToast(AnswerDetailActivity.this,
							LggsUtils.replaceAll(responseString));
				}
				answer_collection.setChecked(false);
				collectionChecked = answer_collection.isChecked();
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	/**
	 * 取消收藏
	 * 
	 */
	public void requestCancelCollect() {
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "answer/cancel_collect";
		RequestParams params = new RequestParams();
		params.put("answerId", answerId);
		params.put("userId", CacheUtils.getUserId(AnswerDetailActivity.this));
		client.post(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				LggsUtils.ShowToast(AnswerDetailActivity.this, responseString);
				if (statusCode == 200) {
					int sum = Integer.parseInt(tv_collection_sum.getText()
							.toString());
					sum--;
					tv_collection_sum.setText("" + sum);
					answer_collection.setChecked(false);
				} else if (statusCode == 401) {
					sendAutoLoginRequest();
				}
				collectionChecked = answer_collection.isChecked();
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});
	}

	/**
	 * 采纳为满意答案
	 * 
	 */
	public void requestAdopt() {
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setCancelable(true);
		pd.setMessage("正在加载。。。");
		pd.show();
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "question/best_answer";
		RequestParams params = new RequestParams();
		params.put("answerId", answerId);
		params.put("userId", CacheUtils.getUserId(AnswerDetailActivity.this));
		params.put("questionId", questionId);
		params.put("answerBy", answerbyid);
		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				LggsUtils.ShowToast(AnswerDetailActivity.this, responseString);
				super.onSuccess(statusCode, headers, responseString);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				if (statusCode == 401) {
					sendAutoLoginRequest();
				} else if (statusCode == 200) {
					LggsUtils.showAdoptaccessToast(AnswerDetailActivity.this);
					ishasBestAnswer = true;
					answer_accept.setSelected(false);
				} else {
					LggsUtils.ShowToast(AnswerDetailActivity.this,
							LggsUtils.replaceAll(responseString));
				}
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFinish() {
				pd.dismiss();
				super.onFinish();
			}
		});
	}

	/**
	 * 获取用户和回答的相关联的数据
	 */
	public void Associated() {
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "answer/associated";
		RequestParams params = new RequestParams();
		params.put("answerId", answerId);
		params.put("userId", CacheUtils.getUserId(AnswerDetailActivity.this));
		client.get(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == 200) {
					Gson gson = new Gson();
					AssociatedResponse associated = gson.fromJson(
							response.toString(), AssociatedResponse.class);
					if (associated.agree) {
						answer_like.setChecked(true);
					}
					if (associated.favouritesAnswerId != null) {
						answer_collection.setChecked(true);
					}
					likeChecked = answer_like.isChecked();
					collectionChecked = answer_collection.isChecked();
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				if (statusCode == 401) {
					sendAutoLoginRequest();
				} else {
					LggsUtils.ShowToast(AnswerDetailActivity.this,
							LggsUtils.replaceAll(responseString));
				}
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});

	}

	/**
	 * 举报回答
	 */
	public void sendRequestInfrom() {

		AsyncHttpClient client = getClient();
		String url = Constans.URL + "delation/answer";
		RequestParams params = new RequestParams();
		params.put("answerId", answerId);
		params.put("userId", CacheUtils.getUserId(AnswerDetailActivity.this));
		// params.put("questionId", questionId);
		// params.put("answerBy", answerbyid);
		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				LggsUtils.ShowToast(AnswerDetailActivity.this, responseString);
				super.onSuccess(statusCode, headers, responseString);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				if (statusCode == 401) {
					sendAutoLoginRequest();
				} else if (statusCode == 200) {
					LggsUtils.showMyToast(getApplicationContext(), "举报成功",
							"我们将尽快核查", "并进行处理");
				} else {
					LggsUtils.ShowToast(AnswerDetailActivity.this,
							LggsUtils.replaceAll(responseString));
				}
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});

	}

	class SelectPopupWindow extends PopupWindow {
		private View mMenuView;
		private Button popup_adopt;

		public SelectPopupWindow(Context context) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mMenuView = inflater.inflate(R.layout.popupwindow_answer_detail,
					null);
			popup_adopt = (Button) mMenuView.findViewById(R.id.popup_adopt);
			if (questionbyid.equals(CacheUtils
					.getUserId(AnswerDetailActivity.this) + "")) {
				popup_adopt.setVisibility(View.VISIBLE);
			} else {
				popup_adopt.setVisibility(View.GONE);
			}
			popup_adopt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					requestAdopt();
					setWindowAlpha(1f);
					dismiss();
				}
			});
			mMenuView.findViewById(R.id.popup_inform).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							sendRequestInfrom();
							setWindowAlpha(1f);
							dismiss();
						}
					});
			mMenuView.findViewById(R.id.popup_cancel).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							dismiss();
						}
					});

			// 设置SelectPicPopupWindow的View
			this.setContentView(mMenuView);
			// 设置SelectPicPopupWindow弹出窗体的宽
			this.setWidth(LayoutParams.MATCH_PARENT);
			// 设置SelectPicPopupWindow弹出窗体的高
			this.setHeight(LayoutParams.WRAP_CONTENT);
			// 设置SelectPicPopupWindow弹出窗体可点击
			this.setFocusable(true);
			// 设置SelectPicPopupWindow弹出窗体动画效果
			this.setAnimationStyle(R.style.AnimBottoms);
			// 实例化一个ColorDrawable颜色为半透明
			ColorDrawable dw = new ColorDrawable(R.color.black);
			// 设置SelectPicPopupF
			this.setBackgroundDrawable(dw);
		}
	}

}
