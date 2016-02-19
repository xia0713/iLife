package com.szgs.bbs.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.szgs.bbs.R;
import com.szgs.bbs.find.FindCategoryResponse;

public class SelectInterestQuesAdapter extends BaseAdapter {
	private Context context;
	private List<FindCategoryResponse> myList;
	private LayoutInflater mInflater;

	public SelectInterestQuesAdapter(Context context,
			List<FindCategoryResponse> myList) {
		this.context = context;
		this.myList = myList;
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
			convertView = mInflater
					.inflate(R.layout.select_interest_item, null);
			holder = new ViewHolder();
			holder.interest_icon = (ImageView) convertView
					.findViewById(R.id.interest_icon);
			holder.interest_title = (TextView) convertView
					.findViewById(R.id.interest_title);
			holder.cb_interest = (CheckBox) convertView
					.findViewById(R.id.cb_interest);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// if (!TextUtils.isEmpty(myList.get(position).getImgUrl())) {
		// // ImageLoader.getInstance().displayImage(
		// // myList.get(position).getImgUrl(), holder.find_icon);
		// }
		// holder.interest_icon
		// .setBackgroundResource(myList.get(position).getImgUrl());
		// holder.interest_title.setText(myList.get(position).getCategoryname());
		// holder.cb_interest.setText(myList.get(position).getCategorydetail());
		return convertView;
	}

	class ViewHolder {
		public ImageView interest_icon;
		public TextView interest_title;
		public TextView cb_interest;
	}
}
