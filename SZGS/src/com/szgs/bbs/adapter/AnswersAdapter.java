package com.szgs.bbs.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.szgs.bbs.R;
import com.szgs.bbs.ask.QuestionAnswerListResponse;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.common.view.CircleImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AnswersAdapter extends
		MyBaseAdapter<QuestionAnswerListResponse.PageAnswer.Content> {

	private DisplayImageOptions options;

	private long bestId = 0;

	public void setBestid(long bestId) {
		this.bestId = bestId;
	}

	public AnswersAdapter(Context context,
			List<QuestionAnswerListResponse.PageAnswer.Content> list) {
		super(context, list);
		options = LggsUtils.inImageLoaderOptions();
	}

	@Override
	public View getConvertView(int position, View convertView,
			LayoutInflater inflater, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(
					R.layout.layout_question_detail_list_item, null);
			holder.answer_user_icon_iv = (CircleImageView) convertView
					.findViewById(R.id.answer_user_icon_iv);
			holder.answer_user_name_tv = (TextView) convertView
					.findViewById(R.id.answer_user_name_tv);
			holder.answer_time_tv = (TextView) convertView
					.findViewById(R.id.answer_time_tv);
			holder.answer_like_num_tv = (TextView) convertView
					.findViewById(R.id.answer_like_num_tv);
			holder.answer_collect_num_tv = (TextView) convertView
					.findViewById(R.id.answer_collect_num_tv);
			holder.answer_content_tv = (TextView) convertView
					.findViewById(R.id.answer_content_tv);
			holder.answer_like_RL = convertView
					.findViewById(R.id.answer_like_RL);
			holder.answer_collect_RL = convertView
					.findViewById(R.id.answer_collect_RL);
			holder.imgview_accept_icon = convertView
					.findViewById(R.id.imgview_accept_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 数据处理
		QuestionAnswerListResponse.PageAnswer.Content content = getItem(position);
		holder.answer_user_name_tv.setText(content.answerBy.nickname);
		ImageLoader.getInstance().displayImage(content.answerBy.avatar,
				holder.answer_user_icon_iv, options);
		holder.answer_time_tv.setText(LggsUtils.caculateTime(
				content.answerTime, LggsUtils.getCurrentTime(), null));
		holder.answer_like_num_tv.setText(content.agreeCount + "");
		holder.answer_collect_num_tv.setText(content.favouritesCount + "");
		holder.answer_content_tv.setText(content.excerpt);
		if (bestId != 0) {
			if (content.id == bestId) {
				holder.imgview_accept_icon.setVisibility(View.VISIBLE);
				holder.answer_like_RL.setVisibility(View.GONE);
				holder.answer_collect_RL.setVisibility(View.GONE);
			} else {
				holder.imgview_accept_icon.setVisibility(View.GONE);
				holder.answer_like_RL.setVisibility(View.VISIBLE);
				holder.answer_collect_RL.setVisibility(View.VISIBLE);
			}
		}
		return convertView;
	}

	class ViewHolder {
		public View imgview_accept_icon;
		CircleImageView answer_user_icon_iv;
		TextView answer_user_name_tv, answer_time_tv, answer_like_num_tv,
				answer_collect_num_tv, answer_content_tv;
		View answer_like_RL, answer_collect_RL;
	}
}
