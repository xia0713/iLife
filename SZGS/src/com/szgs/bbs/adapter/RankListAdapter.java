package com.szgs.bbs.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.szgs.bbs.R;
import com.szgs.bbs.answer.RankListResponse;
import com.szgs.bbs.common.util.LggsUtils;

/**
 * 排行adapter
 * 
 * @author db
 * 
 */
public class RankListAdapter extends BaseAdapter {
	public Context mContext;
	public List<RankListResponse.RankDetail> myList;
	public LayoutInflater mInflater;
	private DisplayImageOptions options;

	public RankListAdapter(Context mContext,
			List<RankListResponse.RankDetail> myList) {
		this.mContext = mContext;
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
		ViewHolder viewHolder;
		if (convertView == null) {
			mInflater = LayoutInflater.from(mContext);
			convertView = mInflater.inflate(R.layout.rank_list_detail, null);
			viewHolder = new ViewHolder();
			viewHolder.rank_num = (TextView) convertView
					.findViewById(R.id.rank_num);
			viewHolder.rank_icon = (ImageView) convertView
					.findViewById(R.id.rank_icon);
			viewHolder.rank_username = (TextView) convertView
					.findViewById(R.id.rank_username);
			viewHolder.rank_accept_count = (TextView) convertView
					.findViewById(R.id.rank_accept_count);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(myList.get(position).avatar,
				viewHolder.rank_icon, options);

		viewHolder.rank_num.setText(myList.get(position).rank + "");
		viewHolder.rank_username.setText(myList.get(position).nickname);
		viewHolder.rank_accept_count.setText(myList.get(position).totalBest
				+ "");
		return convertView;
	}

	class ViewHolder {
		TextView rank_num;
		ImageView rank_icon;
		TextView rank_username;
		TextView rank_accept_count;
	}
}
