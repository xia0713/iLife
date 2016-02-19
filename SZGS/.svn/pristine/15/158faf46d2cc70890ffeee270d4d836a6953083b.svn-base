package com.szgs.bbs.adapter;

import java.util.List;

import com.szgs.bbs.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * item形式为“我感兴趣的问题”中的样式 其中list中的泛型类为object，有接口后换成目标类 ”搜索结果“可用
 * 适用于：搜索结果、提问推送、我感兴趣的问题
 * 
 * @author liinfeng
 * 
 */
public class UserQuestionsAdapter extends MyBaseAdapter {

	public UserQuestionsAdapter(Context context, List<Object> list) {
		super(context, list);
	}

	@Override
	public View getConvertView(int position, View convertView,
			LayoutInflater inflater, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.homeadapter_item_layout,
					null);
			holder.ivAvatar = (ImageView) convertView
					.findViewById(R.id.img_homeitem_icon);
			holder.tvUsername = (TextView) convertView
					.findViewById(R.id.tv_homeitem_username);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_homeitem_time);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_homeitem_content);
			holder.tvLabel = (TextView) convertView
					.findViewById(R.id.tv_tagname);
			holder.tvAnswerNum = (TextView) convertView
					.findViewById(R.id.tv_homeitem_responsesum);
			convertView.findViewById(R.id.ll_home_hot).setVisibility(View.GONE);
			convertView.findViewById(R.id.img_homeitem_listedit).setVisibility(
					View.GONE);
			convertView.findViewById(R.id.tv_homeitem_response).setVisibility(
					View.GONE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 数据处理

		return convertView;
	}

	private class ViewHolder {
		ImageView ivAvatar;
		TextView tvUsername, tvTime, tvContent, tvLabel, tvAnswerNum;
	}
}
