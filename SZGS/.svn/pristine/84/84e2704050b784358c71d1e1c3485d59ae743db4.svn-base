package com.szgs.bbs.answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.SerializableMap;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.find.FindCategoryResponse;

/**
 * 选择感兴趣的专区
 * 
 * @author db
 * 
 */
public class SelectInterestQuesActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

	private TextView tv_title;
	private TextView top_left_tv;
	private ListView select_interest_listview;
	private int[] findicon = { R.drawable.tabbar2_list_icon1, R.drawable.tabbar2_list_iicon2, R.drawable.tabbar2_list_icon3,
			R.drawable.tabbar2_list_icon4, R.drawable.tabbar2_list_icon5, R.drawable.tabbar2_list_icon6 };
	private SelectInterestQuesAdapter adapter;
	private List<FindCategoryResponse> catagoryList;
	private Map<Long, String> interestingCategory = new HashMap<Long, String>();
	private TextView top_right_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_interest_ques);
		catagoryList = new ArrayList<FindCategoryResponse>();
		initHeaderView();
		initData();
		initView();
		getData();
	}

	public void initHeaderView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		top_left_tv = (TextView) findViewById(R.id.top_left_tv);
		top_left_tv.setBackgroundResource(R.drawable.navbar_back_selector);
		top_left_tv.setOnClickListener(this);
		top_right_tv = (TextView) findViewById(R.id.top_right_tv);
		top_right_tv.setText("完成");
		top_right_tv.setVisibility(View.VISIBLE);
		top_right_tv.setOnClickListener(this);
		tv_title.setText("我关注的专区");
	}

	public void initView() {
		select_interest_listview = (ListView) findViewById(R.id.select_interest_listview);
		TextView topTV = new TextView(this);
		topTV.setText("请选择您感兴趣的专区");
		topTV.setTextSize(16);
		topTV.setPadding(30, 20, 10, 20);
		select_interest_listview.addHeaderView(topTV);
		// TextView footTV = new TextView(this);
		// footTV.setText("完成");
		// footTV.setGravity(Gravity.CENTER);
		// footTV.setHeight(200);
		// footTV.setPadding(0, 20, 0, 20);
		// footTV.setTextSize(18);
		// footTV.setTextColor(getResources().getColor(R.color.mine_blue));
		// select_interest_listview.addFooterView(footTV);
		select_interest_listview.setAdapter(adapter);
		select_interest_listview.setOnItemClickListener(this);
	}

	public void initData() {
		adapter = new SelectInterestQuesAdapter(this, catagoryList);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_left_tv:
			finish();
			break;
		case R.id.top_right_tv:
			SerializableMap<Long, String> map = new SerializableMap<Long, String>();
			map.setMap(interestingCategory);
			Bundle bundle = new Bundle();
			bundle.putBoolean("isFromSelectInterestQuesActivity", true);
			bundle.putSerializable("InterestingCategory", map);
			LggsUtils.StartIntent(SelectInterestQuesActivity.this, MyInterestingQuestionActivity.class, bundle);
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ViewHolder holder = (ViewHolder) view.getTag();
		// 在每次获取点击的item时改变checkbox的状态
		if (position == 0) {

		} else {
			if (holder.cb_interest.isChecked()) {
				CancelFocus(catagoryList.get(position - 1).id);
				interestingCategory.remove(new Long(catagoryList.get(position - 1).id));
			} else {
				Focus(catagoryList.get(position - 1).id);
				interestingCategory.put(new Long(catagoryList.get(position - 1).id), catagoryList.get(position - 1).name);
			}
			holder.cb_interest.toggle();
		}
	}

	private void getData() {
		final AsyncHttpClient client = getClient();
		final ProgressDialog myProgressDialog = new ProgressDialog(this);
		myProgressDialog.setCancelable(true);
		myProgressDialog.setMessage("正在加载。。。");
		String url = Constans.URL + "categories";
		client.setConnectTimeout(5000);
		client.get(url, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				myProgressDialog.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						client.cancelAllRequests(true);
					}
				});
				myProgressDialog.show();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				super.onSuccess(statusCode, headers, response);

				if (statusCode == 200) {
					Gson gson = new Gson();
					for (int i = 0; i < response.length(); i++) {
						try {
							FindCategoryResponse responseEntity = gson.fromJson(response.get(i).toString(), FindCategoryResponse.class);
							catagoryList.add(responseEntity);
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				if(statusCode==401){
					sendAutoLoginRequest();
				}
				LggsUtils.ShowToast(SelectInterestQuesActivity.this, LggsUtils.replaceAll(responseString));
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(SelectInterestQuesActivity.this, getResources().getString(R.string.network_connection_error));
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
	 * 添加关注
	 * 
	 * @param categoryId
	 */
	private void Focus(long categoryId) {
		final AsyncHttpClient client = getClient();
		final ProgressDialog myProgressDialog = new ProgressDialog(this);
		myProgressDialog.setCancelable(true);
		myProgressDialog.setMessage("正在加载。。。");
		String url = Constans.URL + "category/focus";
		client.setConnectTimeout(5000);
		RequestParams params = new RequestParams();
		params.put("userId", CacheUtils.getUserId(SelectInterestQuesActivity.this));
		params.put("categoryId", categoryId);
		client.post(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				myProgressDialog.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						client.cancelAllRequests(true);
					}
				});
				myProgressDialog.show();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				if (statusCode == 200) {
					LggsUtils.ShowToast(SelectInterestQuesActivity.this, "关注成功");
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				if(statusCode==401){
					sendAutoLoginRequest();
				}
				LggsUtils.ShowToast(SelectInterestQuesActivity.this, responseString);
			}

			@Override
			public void onFinish() {
				myProgressDialog.dismiss();
				super.onFinish();
			}
		});
	}

	/**
	 * 取消关注
	 * 
	 * @param categoryId
	 */
	private void CancelFocus(long categoryId) {
		final AsyncHttpClient client = new AsyncHttpClient();
		final ProgressDialog myProgressDialog = new ProgressDialog(this);
		myProgressDialog.setCancelable(true);
		myProgressDialog.setMessage("正在加载。。。");
		String url = Constans.URL + "category/cancel_focused";
		client.setConnectTimeout(5000);
		RequestParams params = new RequestParams();
		params.put("userId", CacheUtils.getUserId(SelectInterestQuesActivity.this));
		params.put("categoryId", categoryId);
		client.post(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				myProgressDialog.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						client.cancelAllRequests(true);
					}
				});
				myProgressDialog.show();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				if (statusCode == 200) {
					LggsUtils.ShowToast(SelectInterestQuesActivity.this, "取消关注成功");
				}
				super.onSuccess(statusCode, headers, responseString);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				LggsUtils.ShowToast(SelectInterestQuesActivity.this, responseString);
			}

			@Override
			public void onFinish() {
				myProgressDialog.dismiss();
				super.onFinish();
			}
		});
	}

	class SelectInterestQuesAdapter extends BaseAdapter {
		private Context context;
		private List<FindCategoryResponse> myList;
		private LayoutInflater mInflater;
		private DisplayImageOptions options;

		public SelectInterestQuesAdapter(Context context, List<FindCategoryResponse> myList) {
			this.context = context;
			this.myList = myList;
			options = LggsUtils.inImageLoaderOptions();
		}

		@Override
		public int getCount() {
			return myList.size();
		}

		@Override
		public Object getItem(int position) {
			return myList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			Log.e("Tag", "FindIndexAdapter==" + myList.size());
			// 观察convertView随ListView滚动情况
			if (convertView == null) {
				mInflater = LayoutInflater.from(context);
				convertView = mInflater.inflate(R.layout.select_interest_item, null);
				holder = new ViewHolder();
				holder.interest_icon = (ImageView) convertView.findViewById(R.id.interest_icon);
				holder.interest_title = (TextView) convertView.findViewById(R.id.interest_title);
				holder.cb_interest = (CheckBox) convertView.findViewById(R.id.cb_interest);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ImageLoader.getInstance().displayImage(myList.get(position).icon, holder.interest_icon, options);
			holder.interest_title.setText(myList.get(position).name);
			return convertView;
		}
	}

	class ViewHolder {
		public ImageView interest_icon;
		public TextView interest_title;
		public CheckBox cb_interest;
	}
}
