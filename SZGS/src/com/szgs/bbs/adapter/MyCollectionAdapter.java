package com.szgs.bbs.adapter;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.szgs.bbs.R;
import com.szgs.bbs.common.QuestionListResponse;
import com.szgs.bbs.common.QuestionListResponse.Question;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.common.view.CircleImageView;
import com.szgs.bbs.find.CategoryActivity;

public class MyCollectionAdapter extends
		MyBaseAdapter<QuestionListResponse.Question> {

	public MyCollectionAdapter(Context context, List<Question> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getConvertView(int position, View convertView,
			LayoutInflater inflater, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		// 观察convertView随ListView滚动情况
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.homeadapter_item_layout,
					null);
			holder = new ViewHolder();
			/** 得到各个控件的对象 */
			holder.ll_home_hot = (LinearLayout) convertView
					.findViewById(R.id.ll_home_hot);
			holder.tv_home_hot = (TextView) convertView
					.findViewById(R.id.tv_home_hot);
			holder.tv_home_topline = (TextView) convertView
					.findViewById(R.id.tv_home_topline);
			holder.img_homeitem_icon = (CircleImageView) convertView
					.findViewById(R.id.img_homeitem_icon);
			holder.tv_homeitem_username = (TextView) convertView
					.findViewById(R.id.tv_homeitem_username);
			holder.tv_homeitem_time = (TextView) convertView
					.findViewById(R.id.tv_homeitem_time);
			holder.tv_homeitem_content = (TextView) convertView
					.findViewById(R.id.tv_homeitem_content);
			holder.tv_homeitem_response = (TextView) convertView
					.findViewById(R.id.tv_homeitem_response);
			holder.img_homeitem_listedit = (ImageView) convertView
					.findViewById(R.id.img_homeitem_listedit);
			holder.tv_tagname = (TextView) convertView
					.findViewById(R.id.tv_tagname);
			holder.tv_homeitem_responsesum = (TextView) convertView
					.findViewById(R.id.tv_homeitem_responsesum);
			holder.tv_tiwenl = (TextView) convertView
					.findViewById(R.id.tv_tiwenl);
			holder.ll_homeitem_tag = (LinearLayout) convertView
					.findViewById(R.id.ll_homeitem_tag);
			holder.ll_homeitem_listedit = (LinearLayout) convertView
					.findViewById(R.id.ll_homeitem_listedit);
			holder.myquestion_item_solve = (RelativeLayout) convertView
					.findViewById(R.id.myquestion_item_solve);
			holder.img_homeitem_bestanswer = (ImageView) convertView
					.findViewById(R.id.img_homeitem_bestanswer);
			convertView.setTag(holder);// 绑定ViewHolder对象
		} else {
			holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
		}
		final QuestionListResponse.Question item = getItem(position);
		holder.ll_home_hot.setVisibility(View.GONE);
		holder.tv_home_topline.setVisibility(View.GONE);
		ImageLoader.getInstance().displayImage(item.answerBy.avatar,
				holder.img_homeitem_icon);
		if (TextUtils.isEmpty(item.answerBy.nickname)) {
			holder.tv_homeitem_username.setText(item.answerBy.mobilePhone);
		} else {
			if(item.answerBy.nickname.length()>7){
				holder.tv_homeitem_username.setText(item.answerBy.nickname.substring(0, 7) + "...");
			}else{
				holder.tv_homeitem_username.setText(item.answerBy.nickname);
			}
		}
		holder.tv_tiwenl.setText("回答了：");
		holder.tv_homeitem_time.setText(LggsUtils.caculateTime(item.answerTime,
				LggsUtils.getCurrentTime(), null));
		holder.tv_homeitem_content.setText(item.question.title);
		holder.tv_homeitem_response.setText(item.excerpt);
		holder.tv_tagname.setText(item.question.category.name);
		holder.img_homeitem_listedit.setVisibility(View.GONE);
		if (item.question.status.description.equals("已解决")) {
			holder.img_homeitem_bestanswer.setVisibility(View.VISIBLE);
		} else {
			holder.img_homeitem_bestanswer.setVisibility(View.GONE);
		}
		holder.ll_homeitem_tag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putLong("catagoryId", item.question.category.id);
				bundle.putString("catagoryName", item.question.category.name);
				LggsUtils.StartIntent(context, CategoryActivity.class, bundle);

			}
		});
		holder.tv_tagname.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		holder.tv_homeitem_responsesum.setText(item.agreeCount + "赞");
		return convertView;
	}

	public final class ViewHolder {
		/** 热门头部 用于控制隐藏或显示 */
		public LinearLayout ll_home_hot;
		/** 热门提问或热门回答 */
		public TextView tv_home_hot;

		public TextView tv_home_topline;
		/** 用户头像 */
		public com.szgs.bbs.common.view.CircleImageView img_homeitem_icon;
		/** 用户昵称 */
		public TextView tv_homeitem_username;
		/** 时间 */
		public TextView tv_homeitem_time;
		/** 提问内容 */
		public TextView tv_homeitem_content;
		/** 提问回答 */
		public TextView tv_homeitem_response;
		/** 当时我的提问时显示 其他隐藏 */
		public ImageView img_homeitem_listedit;
		/** 提问种类 */
		public TextView tv_tagname;
		/** 已有回答数 */
		public TextView tv_homeitem_responsesum;
		public TextView tv_tiwenl;
		/** 区 linearLayout */
		public LinearLayout ll_homeitem_tag;
		public LinearLayout ll_homeitem_listedit;
		public RelativeLayout myquestion_item_solve;
		public ImageView img_homeitem_bestanswer;

	}
}
