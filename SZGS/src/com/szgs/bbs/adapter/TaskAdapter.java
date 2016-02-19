package com.szgs.bbs.adapter;

import java.util.List;

import com.szgs.bbs.R;
import com.szgs.bbs.answer.MineTaskResponse;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskAdapter extends MyBaseAdapter<MineTaskResponse> {

	public TaskAdapter(Context context, List<MineTaskResponse> list) {
		super(context, list);
	}

	@Override
	public View getConvertView(int position, View convertView,
			LayoutInflater inflater, ViewGroup parent) {
		final ViewHolder holder;
		Log.e("Tag", "FindIndexAdapter==" + getCount());
		// 观察convertView随ListView滚动情况
		if (convertView == null) {
			inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.task_item, null);
			holder = new ViewHolder();
			holder.img_task = (ImageView) convertView
					.findViewById(R.id.img_task);
			holder.minetask_more = (ImageView) convertView
					.findViewById(R.id.minetask_more);
			holder.minetask_title = (TextView) convertView
					.findViewById(R.id.minetask_title);
			holder.task_complete = (TextView) convertView
					.findViewById(R.id.task_complete);
			holder.task_recommend = (TextView) convertView
					.findViewById(R.id.task_recommend);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.minetask_title.setText(getItem(position).task.title);
		holder.task_recommend.setText(getItem(position).task.description);
		if (getItem(position).task.operationCount == getItem(position).operationCount) {
			holder.task_complete.setVisibility(View.VISIBLE);
		} else {
			holder.task_complete.setVisibility(View.INVISIBLE);
		}
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (holder.task_recommend.getVisibility() == View.GONE) {
					holder.task_recommend.setVisibility(View.VISIBLE);
					holder.minetask_more
							.setBackgroundResource(R.drawable.list_task_more_pre);
				} else {
					holder.task_recommend.setVisibility(View.GONE);
					holder.minetask_more
							.setBackgroundResource(R.drawable.list_more);
				}

			}
		});
		return convertView;
	}

	class ViewHolder {
		public ImageView img_task;
		public ImageView minetask_more;
		public TextView minetask_title;
		public TextView task_complete;
		public TextView task_recommend;
	}
}
