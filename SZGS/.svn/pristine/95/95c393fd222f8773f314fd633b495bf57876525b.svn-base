package com.szgs.bbs.answer;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.adapter.TaskAdapter;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.find.FindCategoryResponse;

public class TaskActivity extends BaseActivity implements OnClickListener {
	private TextView tv_title;
	private View top_left_tv;
	private ListView dayof_task;
	private ListView addof_task;
	private ProgressDialog progressdialog;
	private List<MineTaskResponse> dayTaskList;
	private List<MineTaskResponse> addTaskList;
	private TaskAdapter dayTaskAdapter;
	private TaskAdapter addTaskAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_list);
		dayTaskList = new ArrayList<MineTaskResponse>();
		addTaskList = new ArrayList<MineTaskResponse>();
		initHeaderView();
		initView();
		sendTaskRequest();
	}

	private void initView() {
		dayTaskAdapter = new TaskAdapter(getApplicationContext(), addTaskList);
		addTaskAdapter = new TaskAdapter(getApplicationContext(), addTaskList);
		dayof_task = (ListView) findViewById(R.id.dayof_task);
		addof_task = (ListView) findViewById(R.id.addof_task);
		dayof_task.setAdapter(addTaskAdapter);
		addof_task.setAdapter(addTaskAdapter);
	}

	public void initHeaderView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(this);
		tv_title.setText("我的任务");
	}

	public void sendTaskRequest() {
		progressdialog = new ProgressDialog(TaskActivity.this);
		progressdialog.setMessage("正在加载。。。");
		progressdialog.setCancelable(true);
		progressdialog.show();
		AsyncHttpClient client = getClient();
		String url = Constans.URL + "tasks";
		RequestParams params = new RequestParams();
		params.put("userId", CacheUtils.getUserId(TaskActivity.this));
		client.setTimeout(5000);
		client.get(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response) {
				progressdialog.dismiss();
				Gson gson = new Gson();
				if (statusCode == 200) {

					for (int i = 0; i < response.length(); i++) {
						try {
							MineTaskResponse taskResponse = gson.fromJson(
									response.get(i).toString(),
									MineTaskResponse.class);
							if (taskResponse.task.type.description.equals("每日")) {
								dayTaskList.add(taskResponse);
							} else {
								addTaskList.add(taskResponse);
							}
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
					dayTaskAdapter.notifyDataSetChanged();
					addTaskAdapter.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(addof_task);
					setListViewHeightBasedOnChildren(dayof_task);
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {

				if (statusCode == 401) {
					sendAutoLoginRequest();
				}
				LggsUtils.ShowToast(TaskActivity.this,
						LggsUtils.replaceAll(responseString));
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFinish() {
				progressdialog.dismiss();
				super.onFinish();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(TaskActivity.this, getResources()
						.getString(R.string.network_connection_error));
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
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
	public void setListViewHeightBasedOnChildren(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		((MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除
		listView.setLayoutParams(params);
	}
	
}
