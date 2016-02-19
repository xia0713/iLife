package com.szgs.bbs.ask;

import java.util.List;

import com.szgs.bbs.R;
import com.szgs.bbs.adapter.MyBaseAdapter;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.find.FindCategoryResponse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

public class TopicGridxsfAdapter extends MyBaseAdapter<Integer> {
	public int userscore;

	public TopicGridxsfAdapter(Context context, List<Integer> list) {
		super(context, list);
	}

	public TopicGridxsfAdapter(Context context, List<Integer> list,
			int userscore) {
		super(context, list);
		this.userscore = userscore;
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
		if (getItem(position) > userscore) {
			ctv.setBackgroundResource(R.drawable.bg_edittext_cannotfocus);
			ctv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					LggsUtils.ShowToast(context, "您的积分不够");

				}
			});
		}
		ctv.setText(getItem(position) + "分");
		return convertView;
	}

}
