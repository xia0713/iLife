package com.szgs.bbs.adapter;

import java.util.List;

import com.szgs.bbs.R;
import com.szgs.bbs.ask.AnswerListResponse;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.find.CategoryActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyAnswerAdapter extends MyBaseAdapter<AnswerListResponse.Content> {

	public MyAnswerAdapter(Context context,
			List<AnswerListResponse.Content> list) {
		super(context, list);
	}

	@Override
	public View getConvertView(int position, View convertView,
			LayoutInflater inflater, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.layout_myanswer_item, null);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.myanswer_item_time_tv);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.myanswer_item_target_tv);
			holder.tvLabel = (TextView) convertView
					.findViewById(R.id.myanswer_label_tv);
			holder.tvAnswerNum = (TextView) convertView
					.findViewById(R.id.myanswer_zan_num_tv);
			holder.myanswer_item_content_tv = (TextView) convertView
					.findViewById(R.id.myanswer_item_content_tv);
			holder.iv_answer_solve = (ImageView) convertView
					.findViewById(R.id.iv_answer_solve);
			holder.myanswer_item_label = (RelativeLayout) convertView
					.findViewById(R.id.myanswer_item_label);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 数据处理
		final AnswerListResponse.Content item = getItem(position);
		holder.tvTime.setText(LggsUtils.caculateTime(item.answerTime,
				LggsUtils.getCurrentTime(), "yyyy-MM-dd hh:mm:ss"));
		holder.tvContent.setText(item.question.title);
		holder.tvLabel.setText(item.question.category.name);
		holder.tvAnswerNum.setText(item.agreeCount + "赞");
		holder.myanswer_item_content_tv.setText(item.excerpt);
		if(item.question.status.description.equals("已解决")){
			holder.iv_answer_solve.setVisibility(View.VISIBLE);
		}else{
			holder.iv_answer_solve.setVisibility(View.GONE);
		}
		holder.myanswer_item_label.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putLong("catagoryId", item.question.category.id);
				bundle.putString("catagoryName", item.question.category.name);
				LggsUtils.StartIntent(context, CategoryActivity.class, bundle);
			}
		});
		// holder.tvLabel.setText(item.category.name);
		return convertView;
	}

	class ViewHolder {
		TextView tvTime, tvContent, tvLabel, tvAnswerNum,
				myanswer_item_content_tv;
		RelativeLayout myanswer_item_label;
		ImageView iv_answer_solve;
	}

}
