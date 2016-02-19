package com.szgs.bbs.find;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.szgs.bbs.R;
import com.szgs.bbs.adapter.FindIndexAdapter;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.util.HttpUtils;
import com.szgs.bbs.common.util.LggsUtils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

@SuppressLint("ValidFragment")
public class FindFragment extends Fragment implements OnItemClickListener {

	private Context context;
	private ListView find_listview;

	private FindIndexAdapter adapter;
	private List<FindCategoryResponse> catagoryList;
	private TextView tv_refresh;

	public FindFragment(Context context) {
		super();
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_find, null);
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText("发现");
		find_listview = (ListView) view.findViewById(R.id.find_listview);
		tv_refresh = (TextView) view.findViewById(R.id.tv_refresh);
		catagoryList = new ArrayList<FindCategoryResponse>();
		adapter = new FindIndexAdapter(context, catagoryList);
		find_listview.setAdapter(adapter);
		find_listview.setOnItemClickListener(this);
		tv_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getData();
			}
		});
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Bundle bundle = new Bundle();
		bundle.putLong("catagoryId", catagoryList.get(position).id);
		bundle.putString("catagoryName", catagoryList.get(position).name);
		LggsUtils.StartIntent(context, CategoryActivity.class, bundle);

	}

	private void getData() {
		final AsyncHttpClient client = HttpUtils.getClient(getActivity());
		final ProgressDialog myProgressDialog = new ProgressDialog(context);
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
				// myProgressDialog.show();
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
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					adapter.notifyDataSetChanged();
					tv_refresh.setVisibility(View.GONE);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				if (statusCode == 401) {
					HttpUtils.sendAutoLoginRequest(getActivity());
				}else{
					LggsUtils.ShowToast(context, responseString);
				}
				tv_refresh.setVisibility(View.VISIBLE);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				LggsUtils.ShowToast(
						context,
						getResources().getString(
								R.string.network_connection_error));
				tv_refresh.setVisibility(View.VISIBLE);
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFinish() {
				// myProgressDialog.dismiss();
				super.onFinish();
			}
		});
	}

}
