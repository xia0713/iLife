package com.szgs.bbs.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.szgs.bbs.R;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.common.view.CircleImageView;
import com.szgs.bbs.mine.CommentsMeResponse;

public class CommentMeAdapter extends
		MyBaseAdapter<CommentsMeResponse.ContentQestion> {

	private DisplayImageOptions options;

	public CommentMeAdapter(Context context,
			List<CommentsMeResponse.ContentQestion> list) {
		super(context, list);
		options = LggsUtils.inImageLoaderOptions();
	}

	@Override
	public View getConvertView(int position, View convertView,
			LayoutInflater inflater, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.layout_comment_list_item,
					null);
			holder.comment_list_item_avatar = (CircleImageView) convertView
					.findViewById(R.id.comment_list_item_avatar);
			holder.comment_list_item_username = (TextView) convertView
					.findViewById(R.id.comment_list_item_username);
			holder.comment_list_item_time = (TextView) convertView
					.findViewById(R.id.comment_list_item_time);
			holder.comment_list_item_content = (TextView) convertView
					.findViewById(R.id.comment_list_item_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CommentsMeResponse.ContentQestion content = getItem(position);
		if (content.commentBy != null) {
			if (!TextUtils.isEmpty(content.commentBy.nickname)) {
				holder.comment_list_item_username
						.setText(content.commentBy.nickname);
			}
			if (!TextUtils.isEmpty(content.commentBy.avatar)) {
				// 头像处理
				ImageLoader.getInstance().displayImage(
						content.commentBy.avatar,
						holder.comment_list_item_avatar, options);
			}
		}
		holder.comment_list_item_time.setText(LggsUtils.caculateTime(
				content.commentTime, LggsUtils.getCurrentTime(), null));
		holder.comment_list_item_content.setText(content.message);
		return convertView;
	}

	class ViewHolder {
		CircleImageView comment_list_item_avatar;
		TextView comment_list_item_username, comment_list_item_time,
				comment_list_item_content;
	}
}