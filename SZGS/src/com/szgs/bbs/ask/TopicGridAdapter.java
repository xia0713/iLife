package com.szgs.bbs.ask;

import java.util.List;

import com.szgs.bbs.R;
import com.szgs.bbs.adapter.MyBaseAdapter;
import com.szgs.bbs.find.FindCategoryResponse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

public class TopicGridAdapter extends MyBaseAdapter<FindCategoryResponse> {

	public TopicGridAdapter(Context context, List<FindCategoryResponse> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getConvertView(int position, View convertView,
			LayoutInflater inflater, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.layout_topic_grid_item,
					null);
		}
		CheckedTextView ctv = (CheckedTextView) convertView
				.findViewById(R.id.topic_grid_item_tv);
		ctv.setText(getItem(position).name);
		return convertView;
	}

}
