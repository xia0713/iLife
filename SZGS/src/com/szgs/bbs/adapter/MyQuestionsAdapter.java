package com.szgs.bbs.adapter;

import java.util.List;

import com.szgs.bbs.BaseActivity;
import com.szgs.bbs.R;
import com.szgs.bbs.ask.AskIndexActivity;
import com.szgs.bbs.common.QuestionListResponse;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.common.view.SelectPopupWindow;
import com.szgs.bbs.find.CategoryActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

/**
 * 适用于：我的提问
 * 
 * @author liinfeng
 * 
 */

public class MyQuestionsAdapter extends
		MyBaseAdapter<QuestionListResponse.Question> {
	public int from;

	public MyQuestionsAdapter(Context context,
			List<QuestionListResponse.Question> list, int from) {

		super(context, list);
		this.from = from;
	}

	@Override
	public View getConvertView(int position, View convertView,
			LayoutInflater inflater, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.layout_myquestion_list_item, null);
			holder.more = convertView.findViewById(R.id.myquestion_item_more);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.myquestion_item_time_tv);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.myquestion_item_content_tv);
			holder.tvLabel = (TextView) convertView
					.findViewById(R.id.myquestion_item_label_tv);
			holder.tvAnswerNum = (TextView) convertView
					.findViewById(R.id.myquestion_item_status_tv);
			holder.myquestion_item_status_tv = (TextView) convertView
					.findViewById(R.id.myquestion_item_status_tv);
			holder.myquestion_item_label = (RelativeLayout) convertView
					.findViewById(R.id.myquestion_item_label);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 数据处理

		final QuestionListResponse.Question item = getItem(position);
		holder.tvTime.setText(LggsUtils.caculateTime(item.askTime,
				LggsUtils.getCurrentTime(), "yyyy-MM-dd hh:mm:ss"));
		holder.tvContent.setText(item.title);
		if (item.answerCount > 0) {
			holder.myquestion_item_status_tv.setText(item.answerCount + "人回答");
		} else {
			holder.myquestion_item_status_tv.setText("待回答");
		}
		holder.tvLabel.setText(item.category.name);
		holder.myquestion_item_label.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putLong("catagoryId", item.category.id);
				bundle.putString("catagoryName", item.category.name);
				LggsUtils.StartIntent(context, CategoryActivity.class, bundle);
			}
		});
		if(from==1){
			holder.more.setVisibility(View.VISIBLE);
		}else{
			holder.more.setVisibility(View.GONE);
		}
		holder.more.setOnClickListener(new OnClickListener() {

			private SelectPopupWindow popupWindow;

			@Override
			public void onClick(View v) {
				LayoutInflater imgInflater = LayoutInflater.from(context);
				View view = imgInflater.inflate(R.layout.modif_question_popup,
						null);
				TextView tv1 = (TextView) view
						.findViewById(R.id.modif_modification);
				view.findViewById(R.id.modif_seeall).setVisibility(View.GONE);
				tv1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						LggsUtils.setWindowAlpha(context, 1f);
						popupWindow.dismiss();
						Bundle bundle = new Bundle();
						bundle.putBoolean("isEdit", true);
						bundle.putString("title", item.title);
						bundle.putLong("id", item.id);
						bundle.putInt("reward", item.rewardScore);
						bundle.putSerializable("category", item.category);
						LggsUtils.StartIntent(context, AskIndexActivity.class,
								bundle);
					}
				});
				((BaseActivity) context).setWindowAlpha(0.4f);
				popupWindow = new SelectPopupWindow((Activity) context, view);
				popupWindow.showAtLocation(holder.more, Gravity.BOTTOM, 0, 0);
				popupWindow.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss() {
						// 背景变亮
						((BaseActivity) context).setWindowAlpha(1f);
					}
				});
			}
		});
		return convertView;
	}

	class ViewHolder {
		View more;
		TextView tvTime, tvContent, tvLabel, tvAnswerNum,
				myquestion_item_status_tv;
		RelativeLayout myquestion_item_label;
	}

}
