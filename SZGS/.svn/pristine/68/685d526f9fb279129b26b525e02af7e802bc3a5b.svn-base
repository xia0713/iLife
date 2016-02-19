package com.szgs.bbs.ask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.QuestionListResponse;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.find.FindCategoryResponse;

public class AskIndexActivity extends BaseActivity implements OnClickListener {

	private TextView tv_title, top_left_tv, top_right_tv, tv_ask_selectzq,
			tv_ask_userxs;

	private FindCategoryResponse selectedTopic;
	private int selectScore = -1;
	private String selectscore = "确定";
	public Button btn_score_ok;
	private ProgressDialog myProgressDialog;
	private EditText question_title;
	// private EditText question_content;
	private int FROMFQ = 0001;
	private int FROMSCORE = 0002;
	private int FROM;

	private int UserScore;
	private ArrayList<Integer> ScoreList = new ArrayList<Integer>();
	private List<FindCategoryResponse> catagoryList = new ArrayList<FindCategoryResponse>();

	private boolean isEdit;

	private String questiontitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask_index);
		isEdit = getIntent().getBooleanExtra("isEdit", false);
		getCategoryData();
		OptionsScore();
	}

	public void initView() {
		tv_ask_selectzq = (TextView) findViewById(R.id.tv_ask_selectzq);
		tv_ask_userxs = (TextView) findViewById(R.id.tv_ask_userxs);
		tv_ask_selectzq.setOnClickListener(this);
		tv_ask_userxs.setOnClickListener(this);
		question_title = (EditText) findViewById(R.id.question_title);
		if (isEdit) {
			question_title.setText(getIntent().getStringExtra("title"));
			// getQuestionContent();
			QuestionListResponse.Question.Categoryd c = (QuestionListResponse.Question.Categoryd) getIntent()
					.getSerializableExtra("category");
			selectedTopic = new FindCategoryResponse();
			selectedTopic.name = c.name;
			selectedTopic.id = c.id;
			tv_ask_selectzq.setText(selectedTopic.name);
			tv_ask_selectzq.setTextColor(getResources().getColor(
					R.color.mine_blue));
			tv_ask_userxs.setText(getIntent().getIntExtra("reward", 0) + "分");
			tv_ask_userxs.setTextColor(getResources().getColor(
					R.color.mine_blue));
		} else {
			questiontitle = getIntent().getStringExtra("questiontitle");
			question_title.setText(questiontitle);

		}
	}

	@Override
	protected void onResume() {
		initHeaderView();
		initView();
		super.onResume();
	}

	public void initHeaderView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		if (isEdit) {
			tv_title.setText("编辑问题");
		} else {
			tv_title.setText("提问");
		}
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_right_tv = (TextView) findViewById(R.id.top_right_tv);
		top_left_tv.setOnClickListener(this);
		top_right_tv.setOnClickListener(this);
		top_left_tv.setText("取消");
		top_right_tv.setText("提交");
		top_right_tv.setVisibility(View.VISIBLE);
	}

	private void getQuestionContent() {
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setCancelable(true);
		pd.setMessage("正在加载。。。");
		pd.show();
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "question/content";
		RequestParams params = new RequestParams();
		params.put("questionId", getIntent().getLongExtra("id", 0));
		params.put("userId", CacheUtils.getUserId(this));
		client.setConnectTimeout(5000);
		client.get(url, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				question_title.setText(LggsUtils.replaceAll(arg2));
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				if (arg0 == 401) {
					sendAutoLoginRequest();
				} else {
					LggsUtils.ShowToast(AskIndexActivity.this, arg2);
				}
			}

			@Override
			public void onFinish() {
				pd.dismiss();
				super.onFinish();
			}
		});

	}

	private void getCategoryData() {
		AsyncHttpClient client = getClient();
		final ProgressDialog myProgressDialog = new ProgressDialog(this);
		myProgressDialog.setCancelable(true);
		myProgressDialog.setMessage("正在加载。。。");
		myProgressDialog.show();
		client.setConnectTimeout(5000);
		String url = Constans.URL + "categories";
		client.get(url, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				super.onSuccess(statusCode, headers, response);
				if (statusCode == 200) {
					Gson gson = new Gson();
					for (int i = 0; i < response.length(); i++) {
						try {
							FindCategoryResponse responseEntity = gson
									.fromJson(response.get(i).toString(),
											FindCategoryResponse.class);
							catagoryList.add(responseEntity);
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(AskIndexActivity.this, getResources()
						.getString(R.string.network_connection_error));
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFinish() {
				myProgressDialog.dismiss();
				super.onFinish();
			}
		});
	}

	private void OptionsScore() {
		AsyncHttpClient client = getClient();
		final ProgressDialog myProgressDialog = new ProgressDialog(this);
		myProgressDialog.setCancelable(true);
		myProgressDialog.setMessage("正在加载。。。");
		myProgressDialog.show();
		client.setConnectTimeout(5000);
		String url = Constans.URL + "setting/question/score_options";
		RequestParams params = new RequestParams();
		params.put("userId", CacheUtils.getUserId(AskIndexActivity.this));
		client.get(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == 200) {
					Gson gson = new Gson();
					try {
						OptionsScoresResponse responseEntity = gson.fromJson(
								response.toString(),
								OptionsScoresResponse.class);
						ScoreList = responseEntity.scoreOptions;
						UserScore = responseEntity.userScore;
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
					}
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(AskIndexActivity.this, getResources()
						.getString(R.string.network_connection_error));
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFinish() {
				myProgressDialog.dismiss();
				super.onFinish();
			}
		});
	}

	/**
	 * 编辑问题
	 */
	private void editQuestion() {
		final ProgressDialog myProgressDialog = new ProgressDialog(this);
		myProgressDialog.setCancelable(true);
		myProgressDialog.setMessage("正在加载。。。");
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "question/edit";
		RequestParams params = new RequestParams();
		params.put("questionId", getIntent().getLongExtra("id", 0));
		params.put("updateBy", CacheUtils.getUserId(this));
		params.put("title", question_title.getText().toString());
		// params.put("content", question_content.getText().toString());
		params.put("categoryId", selectedTopic.id);
		client.setConnectTimeout(5000);
		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (statusCode == 200) {
					LggsUtils.ShowToast(AskIndexActivity.this, "编辑成功");
					finish();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				if (statusCode == 401) {
					sendAutoLoginRequest();
				} else {
					LggsUtils.ShowToast(AskIndexActivity.this, responseString);
				}
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(AskIndexActivity.this, getResources()
						.getString(R.string.network_connection_error));
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFinish() {
				myProgressDialog.show();
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
		case R.id.top_right_tv:
			if (TextUtils.isEmpty(question_title.getText().toString())) {
				LggsUtils.ShowToast(AskIndexActivity.this, "请为您的问题添加内容");
			} else if (selectedTopic == null) {
				LggsUtils.showCustomeDialog(AskIndexActivity.this, "温馨提示",
						"您还未为问题选择专区");
			} else {
				if (isEdit) {
					editQuestion();
				} else {
					sendRequest();
				}
			}
			break;
		case R.id.tv_ask_selectzq:
			// FROM = FROMFQ;
			// 背景变暗
			setWindowAlpha(0.4f);
			hideKeybord(tv_ask_selectzq);

			SelectPopupWindow popup = new SelectPopupWindow(
					AskIndexActivity.this, catagoryList);
			popup.showAtLocation(tv_ask_selectzq, Gravity.BOTTOM
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
		case R.id.tv_ask_userxs:
			if (isEdit) {
				LggsUtils.ShowToast(AskIndexActivity.this, "不能更改悬赏分");
			} else {
				FROM = FROMSCORE;
				List<String> scores = new ArrayList<String>();
				scores.add("5分");
				// 背景变暗
				setWindowAlpha(0.4f);
				hideKeybord(tv_ask_userxs);
				SelectPopupWindowxsf popup1 = new SelectPopupWindowxsf(
						AskIndexActivity.this, ScoreList);
				popup1.showAtLocation(tv_ask_userxs, Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
				popup1.setOutsideTouchable(true);
				popup1.setFocusable(false);
				popup1.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss() {
						// 背景变亮
						setWindowAlpha(1f);
					}
				});
			}
			break;
		default:
			break;
		}
	}

	private void hideKeyBoard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	class SelectPopupWindow extends PopupWindow {
		private View mMenuView;
		private Button btn_ok;
		private GridView grid;
		private TopicGridAdapter adapter;
		private CheckedTextView old;// 前一个CheckedTextView，当点击其他时，要去除他的checked状态
		private TextView userscore;

		public SelectPopupWindow(Context context,
				List<FindCategoryResponse> list) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mMenuView = inflater.inflate(R.layout.select_popupwindow, null);
			userscore = (TextView) mMenuView.findViewById(R.id.userscore);
			userscore.setVisibility(View.GONE);
			grid = (GridView) mMenuView.findViewById(R.id.grid);
			adapter = new TopicGridAdapter(context, list);
			grid.setAdapter(adapter);
			grid.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					CheckedTextView ctv = (CheckedTextView) view
							.findViewById(R.id.topic_grid_item_tv);
					selectedTopic = adapter.getItem(position);
					if (old != null) {
						old.setChecked(false);
						old.setTextColor(Color.parseColor("#525252"));
					}
					ctv.setChecked(true);
					ctv.setTextColor(Color.parseColor("#ffffff"));
					old = ctv;
				}
			});
			btn_ok = (Button) mMenuView.findViewById(R.id.btn_ok);
			btn_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (selectedTopic != null) {
						tv_ask_selectzq.setText(selectedTopic.name);
						tv_ask_selectzq.setTextColor(getResources().getColor(
								R.color.mine_blue));
					}
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
			ColorDrawable dw = new ColorDrawable(0xb0000000);
			// 设置SelectPicPopupWindow弹出窗体的背景
			this.setBackgroundDrawable(dw);
		}
	}

	class SelectPopupWindowxsf extends PopupWindow {
		private View mMenuView;
		private Button btn_ok;
		private GridView grid;
		private TopicGridxsfAdapter adapter;
		private CheckedTextView old;// 前一个CheckedTextView，当点击其他时，要去除他的checked状态
		private TextView userscore;

		public SelectPopupWindowxsf(Context context, List<Integer> list) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mMenuView = inflater.inflate(R.layout.select_popupwindow, null);
			userscore = (TextView) mMenuView.findViewById(R.id.userscore);
			userscore.setVisibility(View.VISIBLE);
			grid = (GridView) mMenuView.findViewById(R.id.grid);
			userscore.setText("我的可用积分：" + UserScore);
			adapter = new TopicGridxsfAdapter(context, list, UserScore);
			grid.setAdapter(adapter);
			grid.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					CheckedTextView ctv = (CheckedTextView) view
							.findViewById(R.id.topic_grid_item_tv);
					selectScore = adapter.getItem(position);
					if (old != null) {
						old.setChecked(false);
						old.setTextColor(Color.parseColor("#525252"));
					}
					ctv.setChecked(true);
					ctv.setTextColor(Color.parseColor("#ffffff"));
					old = ctv;
				}
			});
			btn_ok = (Button) mMenuView.findViewById(R.id.btn_ok);
			btn_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (selectScore != -1) {
						tv_ask_userxs.setText(selectScore + "分");
						tv_ask_userxs.setTextColor(getResources().getColor(
								R.color.mine_blue));
					}
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
			ColorDrawable dw = new ColorDrawable(0xb0000000);
			// 设置SelectPicPopupWindow弹出窗体的背景
			this.setBackgroundDrawable(dw);
		}
	}

	class SelectPopupWindowxs extends PopupWindow {
		private View mMenuView;

		public SelectPopupWindowxs(Context context) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mMenuView = inflater.inflate(R.layout.select_popupwindowxs, null);

			btn_score_ok = (Button) mMenuView.findViewById(R.id.btn_score_ok);
			btn_score_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					tv_ask_userxs.setText(selectscore);
					if (!selectscore.equals("确定")) {
						tv_ask_userxs.setTextColor(getResources().getColor(
								R.color.mine_blue));
					}
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
			ColorDrawable dw = new ColorDrawable(0xb0000000);
			// 设置SelectPicPopupF
			this.setBackgroundDrawable(dw);

			// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
			// mMenuView.setOnTouchListener(new OnTouchListener() {
			//
			// public boolean onTouch(View v, MotionEvent event) {
			// if (event.getAction() == MotionEvent.ACTION_UP) {
			// dismiss();
			// }
			// return true;
			// }
			// });
		}
	}

	private void sendRequest() {
		AsyncHttpClient client = getClient();
		myProgressDialog = new ProgressDialog(this);
		myProgressDialog.setCancelable(true);
		myProgressDialog.setMessage("正在加载。。。");
		myProgressDialog.show();
		RequestParams params = new RequestParams();
		params.put("title", question_title.getText().toString());
		params.put("content", "");
		if (selectScore == -1) {
			selectScore = 0;
		}
		params.put("score", selectScore);
		params.put("categoryId", selectedTopic.id);
		params.put("askById", CacheUtils.getUserId(this));
		String url = Constans.URL + "question/publish";
		client.setConnectTimeout(5000);
		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (statusCode == 201) {
					LggsUtils.ShowToast(getApplicationContext(), "发布问题成功");
					finish();
				} else {
					LggsUtils.ShowToast(getApplicationContext(), "发布问题失败");
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				if (statusCode == 401) {
					sendAutoLoginRequest();
				} else if (statusCode == 400) {
					LggsUtils.showFailDialog(AskIndexActivity.this, null, null,
							true);
				} else {
					LggsUtils.ShowToast(AskIndexActivity.this, responseString);
				}
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(AskIndexActivity.this, getResources()
						.getString(R.string.network_connection_error));
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFinish() {
				myProgressDialog.dismiss();
				super.onFinish();
			}
		});
	}
}
