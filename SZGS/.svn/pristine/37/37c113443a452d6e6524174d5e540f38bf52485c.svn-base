package com.szgs.bbs.mine;


import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.szgs.bbs.R;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.HttpUtils;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.common.util.SharedPranceUtils;
import com.szgs.bbs.common.view.CircleImageView;

public class MineFragment extends Fragment implements OnClickListener {

	private Context context;

	private ImageView mine_notification;
	private CircleImageView mine_user_icon;
	private TextView mine_username;
	private TextView mine_adoption_rate;
	private TextView mine_integrate;
	private LinearLayout ll_mine_myquestion;
	private LinearLayout ll_mine_myresponse;
	private LinearLayout ll_mine_mycollect;
	private LinearLayout ll_mine_set;

	private DisplayImageOptions options;

	private ImageView home_notification_point;

	public MineFragment(Context context) {
		super();
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mine, null);
		options = LggsUtils.inImageLoaderOptions();
		mine_notification = (ImageView) view
				.findViewById(R.id.mine_notification);
		mine_user_icon = (CircleImageView) view
				.findViewById(R.id.mine_user_icon);
		mine_username = (TextView) view.findViewById(R.id.mine_username);
		/** 采纳率 */
		mine_adoption_rate = (TextView) view
				.findViewById(R.id.mine_adoption_rate);
		/** 我的积分 */
		mine_integrate = (TextView) view.findViewById(R.id.mine_integrate);

		ll_mine_myquestion = (LinearLayout) view
				.findViewById(R.id.ll_mine_myquestion);
		ll_mine_myresponse = (LinearLayout) view
				.findViewById(R.id.ll_mine_myresponse);
		ll_mine_mycollect = (LinearLayout) view
				.findViewById(R.id.ll_mine_mycollect);
		ll_mine_set = (LinearLayout) view.findViewById(R.id.ll_mine_set);
		mine_notification.setOnClickListener(this);
		ll_mine_myquestion.setOnClickListener(this);
		ll_mine_myresponse.setOnClickListener(this);
		ll_mine_mycollect.setOnClickListener(this);
		ll_mine_set.setOnClickListener(this);
		home_notification_point = (ImageView) view
				.findViewById(R.id.home_notification_point);
		IntentFilter filter = new IntentFilter();
		filter.addAction("action.refresh");
		context.registerReceiver(mRefreshBroadcastReceiver, filter);
		return view;
	}

	@Override
	public void onResume() {
		mine_username.setText(CacheUtils.getUserName(getActivity()));
		ImageLoader.getInstance().displayImage(
				CacheUtils.getAvatar(getActivity()), mine_user_icon, options);
		sendRequest();
		if (SharedPranceUtils.GetBolDate("hasnewmsg", context, false)) {
			home_notification_point.setVisibility(View.VISIBLE);
		} else {
			home_notification_point.setVisibility(View.GONE);
		}

		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_mine_myquestion:
			LggsUtils.StartIntent(context, MyQuestionsActivity.class);
			break;
		case R.id.ll_mine_myresponse:
			LggsUtils.StartIntent(context, MyAnswerActivity.class);
			break;
		case R.id.ll_mine_mycollect:
			LggsUtils.StartIntent(context, MyCollectionActivity.class);
			break;
		case R.id.ll_mine_set:
			LggsUtils.StartIntent(context, SetActivity.class);
			break;
		case R.id.mine_notification:
			LggsUtils.StartIntent(context, NotificationActivity.class);
			break;
		default:
			break;
		}
	}

	/**
	 * 获取我的信息
	 * 
	 */
	private void sendRequest() {
		final AsyncHttpClient client = HttpUtils.getClient(getActivity());
		final ProgressDialog myProgressDialog = new ProgressDialog(
				getActivity());
		myProgressDialog.setCancelable(true);
		myProgressDialog.setMessage("正在加载。。。");
		String url = Constans.URL + "user/statistics";
		RequestParams params = new RequestParams();
		params.put("userId", CacheUtils.getUserId(getActivity()));
		client.setConnectTimeout(5000);
		client.get(url, params, new JsonHttpResponseHandler() {

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
					JSONObject response) {
				Gson gson = new Gson();

				UserStatisticsResponse responseEntity = gson.fromJson(
						response.toString(), UserStatisticsResponse.class);
				String str = responseEntity.percent;
				String[] strs = str.split("\\.");
				mine_adoption_rate.setText(strs[0] + "%");
				mine_integrate.setText(responseEntity.score + "");
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}

			@Override
			public void onFinish() {
				// myProgressDialog.dismiss();
				super.onFinish();
			}
		});
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
