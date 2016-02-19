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
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.Log;
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
import com.loopj.android.http.TextHttpResponseHandler;
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
public class SelectInterestQuesActivity2 extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	private TextView tv_title;
	private TextView top_left_tv;
	private ListView select_interest_listview;
	private int[] findicon = { R.drawable.tabbar2_list_icon1,
			R.drawable.tabbar2_list_iicon2, R.drawable.tabbar2_list_icon3,
			R.drawable.tabbar2_list_icon4, R.drawable.tabbar2_list_icon5,
			R.drawable.tabbar2_list_icon6 };
	private ArrayList<FindCategoryResponse> myList;
	private SelectInterestQuesAdapter adapter;
	private List<SelectInterest> selectList;
	private List<FindCategoryResponse> catagoryList;
	private TextView top_right_tv;
	private long[] careid;

	private Map<Long, String> interestingCategory = new HashMap<Long, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_interest_ques);
		catagoryList = new ArrayList<FindCategoryResponse>();
		careid = getIntent().getLongArrayExtra("careid");
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
		top_right_tv.setText("确定");
		top_right_tv.setVisibility(View.VISIBLE);
		top_right_tv.setOnClickListener(this);
		tv_title.setText("我关注的专区");
	}

	public void initView() {
		select_interest_listview = (ListView) findViewById(R.id.select_interest_listview);
		TextView topTV = new TextView(this);
		topTV.setText("请选择您感兴趣的专区");
		topTV.setTextSize(16);
		topTV.setPadding(10, 20, 10, 20);
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
		selectList = new ArrayList<SelectInterest>();
		for (int i = 0; i < 6; i++) {
			SelectInterest select = new SelectInterest();
			select.setName("这是测试" + i);
			select.setIschecked(false);
			selectList.add(select);
		}

		myList = new ArrayList<FindCategoryResponse>();
		for (int i = 0; i < 6; i++) {
			FindCategoryResponse resoponse = new FindCategoryResponse();
			// resoponse.setImgUrl(findicon[i]);
			// resoponse.setCategoryname("这是测试" + i);
			myList.add(resoponse);
		}
		adapter = new SelectInterestQuesAdapter(this, catagoryList);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_left_tv:
			SerializableMap<Long, String> map1 = new SerializableMap<Long, String>();
			map1.setMap(interestingCategory);
			Intent intent1 = new Intent();
			intent1.putExtra("InterestingCategory", map1);
			this.setResult(1000, intent1);
			finish();
			break;
		case R.id.top_right_tv:
			SerializableMap<Long, String> map = new SerializableMap<Long, String>();
			map.setMap(interestingCategory);
			Intent intent = new Intent();
			intent.putExtra("InterestingCategory", map);
			this.setResult(1000, intent);
			finish();
			break;
		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		SerializableMap<Long, String> map = new SerializableMap<Long, String>();
		map.setMap(interestingCategory);
		Intent intent = new Intent();
		intent.putExtra("InterestingCategory", map);
		this.setResult(1000, intent);
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ViewHolder holder = (ViewHolder) view.getTag();
		// 在每次获取点击的item时改变checkbox的状态
		if (position == 0) {

		} else {
			if (holder.cb_interest.isChecked()) {
				CancelFocus(catagoryList.get(position - 1).id);

			} else {
				Focus(catagoryList.get(position - 1).id,
						catagoryList.get(position - 1).name);
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
							for (int j = 0; j < careid.length; j++) {
								if (responseEntity.id == careid[j]) {
									interestingCategory.put(careid[j],
											responseEntity.name);
								}
							}
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
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				if(statusCode==401){
					sendAutoLoginRequest();
				}
				super.onFailure(statusCode, headers, responseString, throwable);
				LggsUtils.ShowToast(SelectInterestQuesActivity2.this,
						responseString);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(
						SelectInterestQuesActivity2.this,
						getResources().getString(
								R.string.network_connection_error));
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
	private void Focus(final long categoryId, final String name) {
		final AsyncHttpClient client = getClient();
		final ProgressDialog myProgressDialog = new ProgressDialog(this);
		myProgressDialog.setCancelable(true);
		myProgressDialog.setMessage("正在加载。。。");
		String url = Constans.URL + "category/focus";
		client.setConnectTimeout(5000);
		RequestParams params = new RequestParams();
		params.put("userId",
				CacheUtils.getUserId(SelectInterestQuesActivity2.this));
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
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (statusCode == 200) {
					LggsUtils.ShowToast(SelectInterestQuesActivity2.this,
							"关注成功");
					interestingCategory.put(new Long(categoryId), name);
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				if(statusCode==401){
					sendAutoLoginRequest();
				}
				LggsUtils.ShowToast(SelectInterestQuesActivity2.this,
						responseString);
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
	private void CancelFocus(final long categoryId) {
		final AsyncHttpClient client = getClient();
		final ProgressDialog myProgressDialog = new ProgressDialog(this);
		myProgressDialog.setCancelable(true);
		myProgressDialog.setMessage("正在加载。。。");
		String url = Constans.URL + "category/cancel_focused";
		client.setConnectTimeout(5000);
		RequestParams params = new RequestParams();
		params.put("userId",
				CacheUtils.getUserId(SelectInterestQuesActivity2.this));
		params.put("categoryId", categoryId);
		client.post(url, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				if (arg0 == 200) {
					LggsUtils.ShowToast(SelectInterestQuesActivity2.this,
							"取消关注成功");
					interestingCategory.remove(categoryId);
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				if(arg0==401){
					sendAutoLoginRequest();
				}
				LggsUtils.ShowToast(SelectInterestQuesActivity2.this,
						LggsUtils.replaceAll(arg2));
			}

			@Override
			public void onFinish() {
				super.onFinish();
				myProgressDialog.dismiss();
			}
		});
	}
	class SelectInterestQuesAdapter extends BaseAdapter {
		private Context context;
		private List<FindCategoryResponse> myList;
		private LayoutInflater mInflater;
		private DisplayImageOptions options;

		public SelectInterestQuesAdapter(Context context,
				List<FindCategoryResponse> myList) {
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
				convertView = mInflater.inflate(R.layout.select_interest_item,
						null);
				holder = new ViewHolder();
				holder.interest_icon = (ImageView) convertView
						.findViewById(R.id.interest_icon);
				holder.interest_title = (TextView) convertView
						.findViewById(R.id.interest_title);
				holder.cb_interest = (CheckBox) convertView
						.findViewById(R.id.cb_interest);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			for (int i = 0; i < careid.length; i++) {
				if (myList.get(position).id == careid[i]) {
					holder.cb_interest.setChecked(true);
					break;
				}
			}
			ImageLoader.getInstance().displayImage(myList.get(position).icon,
					holder.interest_icon, options);
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
