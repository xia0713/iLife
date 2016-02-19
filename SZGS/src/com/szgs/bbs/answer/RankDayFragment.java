package com.szgs.bbs.answer;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.szgs.bbs.BaseFragment;
import com.szgs.bbs.R;
import com.szgs.bbs.adapter.RankListAdapter;
import com.szgs.bbs.common.Constans;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.HttpUtils;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.common.view.CircleImageView;
import com.szgs.bbs.ui.OtherUserInformationActivity;

public class RankDayFragment extends BaseFragment implements
		OnItemClickListener {

	private ListView rank_list_pull;
	private View view;
	private ProgressDialog progressdialog;

	private ArrayList<RankListResponse.RankDetail> list;
	private RankListAdapter adapter;
	private LinearLayout rank_ll_bottom;
	private ImageView rank_list_icon;
	private TextView rank_list_my;
	private TextView rank_list_cns;
	private DisplayImageOptions options;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.rank_list, null);
		list = new ArrayList<RankListResponse.RankDetail>();
		options = LggsUtils.inImageLoaderOptions();
		initView();
		initListener();
		initDate();
		sendDayListRequest();
		return view;
	}

	private void initListener() {

	}

	private void initView() {
		rank_list_pull = (ListView) view
				.findViewById(R.id.rank_list_pull);
		rank_ll_bottom = (LinearLayout) view.findViewById(R.id.rank_ll_bottom);
		rank_list_icon = (CircleImageView) view
				.findViewById(R.id.rank_list_icon);
		/** 我的排行 */
		rank_list_my = (TextView) view.findViewById(R.id.rank_list_my);
		rank_list_pull.setOnItemClickListener(this);
		/** 采纳数 */
		rank_list_cns = (TextView) view.findViewById(R.id.rank_list_cns);
	}

	public void initDate() {
		adapter = new RankListAdapter(getActivity(), list);
		rank_list_pull.setAdapter(adapter);
	}

	public void sendDayListRequest() {
		progressdialog = new ProgressDialog(getActivity());
		progressdialog.setMessage("正在加载。。。");
		progressdialog.setCancelable(true);
		progressdialog.show();
		AsyncHttpClient client =HttpUtils.getClient(getActivity());
		String url = Constans.URL + "user/rank/daily";
		RequestParams params = new RequestParams();
		params.put("userId", CacheUtils.getUserId(getActivity()));
		client.setTimeout(5000);
		client.get(url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				progressdialog.dismiss();
				Gson gson = new Gson();
				if (statusCode == 200) {
					RankListResponse questionlistRep = gson.fromJson(
							response.toString(), RankListResponse.class);
					list.addAll(questionlistRep.list);
					if (questionlistRep.userRank != null) {
						rank_list_my
								.setText(questionlistRep.userRank.rank + "");
						rank_list_cns
								.setText(questionlistRep.userRank.totalBest
										+ "");
						ImageLoader.getInstance().displayImage(
								questionlistRep.userRank.avatar,
								rank_list_icon, options);
						rank_ll_bottom.setVisibility(View.VISIBLE);
					} else {
						rank_ll_bottom.setVisibility(View.GONE);
					}
					adapter.notifyDataSetChanged();
					LggsUtils.setListViewHeightBasedOnChildren(rank_list_pull);
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {

				if(statusCode==401){
					HttpUtils.sendAutoLoginRequest(getActivity());
				}
				LggsUtils.ShowToast(getActivity(),
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
				LggsUtils.ShowToast(
						getActivity(),
						getResources().getString(
								R.string.network_connection_error));
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Bundle bundle = new Bundle();
		bundle.putString("avatar", list.get(position).avatar);
		bundle.putLong("userId", list.get(position).userId);
		bundle.putString("username", list.get(position).nickname);
		bundle.putInt("rank", list.get(position).rank);
		bundle.putInt("totalBest", list.get(position).totalBest);
		bundle.putString("percent", list.get(position).percent);
		bundle.putInt("score", list.get(position).score);
		LggsUtils.StartIntent(getActivity(),
				OtherUserInformationActivity.class, bundle);
	}

}
