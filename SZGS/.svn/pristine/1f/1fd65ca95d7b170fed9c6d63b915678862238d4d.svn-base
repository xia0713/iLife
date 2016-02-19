package com.szgs.bbs.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.szgs.bbs.R;
import com.szgs.bbs.ask.AnswerOrCommentActivity;
import com.szgs.bbs.common.QuestionListResponse;
import com.szgs.bbs.common.util.CacheUtils;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.common.view.CircleImageView;
import com.szgs.bbs.find.CategoryActivity;

/**
 * 问题详情adapter 可多个activity共用
 * 
 * @author db
 * 
 */
public class TheNewQuestionAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<QuestionListResponse.Question> list;
	/**
	 * 通过from 判断入口 1 最新问题 3高分悬赏4分区
	 */
	private LayoutInflater mInflater;
	private DisplayImageOptions options;

	public int hotAnswerCount;// 记录最少热门回答数

	public TheNewQuestionAdapter(Context context,
			ArrayList<QuestionListResponse.Question> list) {
		this.context = context;
		this.list = list;
		options = LggsUtils.inImageLoaderOptions();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {

		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		// 观察convertView随ListView滚动情况
		if (convertView == null) {
			mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.question_detail, null);
			holder = new ViewHolder();
			/** 得到各个控件的对象 */
			holder.rl_category_top = convertView
					.findViewById(R.id.rl_category_top);
			holder.rl_category_top_like_num = (TextView) convertView
					.findViewById(R.id.rl_category_top_like_num);
			holder.tv_question_or_answer = (TextView) convertView
					.findViewById(R.id.tv_question_or_answer);
			holder.tv_answer_content = (TextView) convertView
					.findViewById(R.id.tv_answer_content);
			holder.ll_home_hot = (LinearLayout) convertView
					.findViewById(R.id.ll_home_hot);
			holder.tv_question_score = (TextView) convertView
					.findViewById(R.id.tv_question_score);
			holder.question_sum = (TextView) convertView
					.findViewById(R.id.question_sum);
			holder.tv_question_topline = (TextView) convertView
					.findViewById(R.id.tv_question_topline);

			holder.img_question_icon = (CircleImageView) convertView
					.findViewById(R.id.img_question_icon);
			holder.tv_question_username = (TextView) convertView
					.findViewById(R.id.tv_question_username);
			holder.tv_question_time = (TextView) convertView
					.findViewById(R.id.tv_question_time);
			holder.tv_question_content = (TextView) convertView
					.findViewById(R.id.tv_question_content);
			holder.img_question_listedit = (ImageView) convertView
					.findViewById(R.id.img_question_listedit);
			holder.tv_tagname = (TextView) convertView
					.findViewById(R.id.tv_tagname);
			holder.ll_question_tag = (LinearLayout) convertView
					.findViewById(R.id.ll_question_tag);
			holder.tv_question_toanswer = (TextView) convertView
					.findViewById(R.id.tv_question_toanswer);
			holder.bottomll_fqu = (LinearLayout) convertView
					.findViewById(R.id.bottomll_fqu);

			holder.question_sum2 = (TextView) convertView
					.findViewById(R.id.question_sum2);
			holder.hot_img = (ImageView) convertView.findViewById(R.id.hot_img);
			holder.img_solve_icon = (ImageView) convertView
					.findViewById(R.id.img_solve_icon);
			convertView.setTag(holder);// 绑定ViewHolder对象
		} else {
			holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
		}
		/** 设置TextView显示的内容，即我们存放在动态数组中的数据 */
		holder.question_sum2.setVisibility(View.GONE);
		String questioncontent = "";
		if (list.get(position).rewardScore != 0) {
			ImageGetter imageGetter = new ImageGetter() {
				@Override
				public Drawable getDrawable(String source) {
					int id = Integer.parseInt(source);
					// 根据id从资源文件中获取图片对象
					Drawable d = context.getResources().getDrawable(id);
					d.setBounds(0, 0, d.getIntrinsicWidth(),
							d.getIntrinsicHeight());
					return d;
				}
			};
			holder.tv_question_content.setText(Html.fromHtml("<img src='"
					+ R.drawable.list_credit + "'/>", imageGetter, null));
			questioncontent += list.get(position).rewardScore + " ";
			// holder.tv_question_content.append(list.get(position).rewardScore+"");
		}

		holder.ll_home_hot.setVisibility(View.GONE);
		holder.tv_question_topline.setVisibility(View.GONE);
		if (list.get(position).status.description.equals("已解决")) {
			holder.img_solve_icon.setVisibility(View.VISIBLE);
		} else {
			holder.img_solve_icon.setVisibility(View.GONE);
		}
		// 若是分区列表则控制底部分区名 添加回答隐藏
		holder.bottomll_fqu.setVisibility(View.VISIBLE);
		ImageLoader.getInstance().displayImage(list.get(position).askBy.avatar,
				holder.img_question_icon, options);
		if (list.get(position).askBy.nickname == null) {
			holder.tv_question_username
					.setText(list.get(position).askBy.mobilePhone);
		} else {
			if (list.get(position).askBy.nickname.length() > 7) {
				holder.tv_question_username
						.setText(list.get(position).askBy.nickname.substring(0,
								7) + "...");
			} else {
				holder.tv_question_username
						.setText(list.get(position).askBy.nickname);
			}
		}
		holder.tv_question_time.setText(LggsUtils.caculateTime(
				list.get(position).askTime, LggsUtils.getCurrentTime(), null));
		questioncontent += list.get(position).title;
		// holder.tv_question_content.append(list.get(position).title);
		if (list.get(position).askBy.id.equals(CacheUtils.getUserName(context))) {
			holder.img_question_listedit.setVisibility(View.VISIBLE);
		} else {
			holder.img_question_listedit.setVisibility(View.GONE);
		}
		if (list.get(position).answerCount == 0) {
			holder.question_sum.setText("待回答");
		} else {
			holder.question_sum.setText(list.get(position).answerCount + "人回答");
		}
		if (position == 0) {
			holder.hot_img.setBackgroundResource(R.drawable.list_hot);
			holder.tv_question_score.setText("热门提问");
			holder.hot_img.setVisibility(View.VISIBLE);
			holder.tv_question_score.setVisibility(View.VISIBLE);
		} else {
			holder.hot_img.setVisibility(View.GONE);
			holder.tv_question_score.setVisibility(View.GONE);
		}

		holder.tv_tagname.setText(list.get(position).category.name);
		if (list.get(position).rewardScore == 0) {
			holder.tv_question_content.setText(list.get(position).title);
		} else {
			holder.tv_question_content.append(LggsUtils.SetTextColorOrange(
					context, questioncontent, list.get(position).rewardScore
							+ ""));
		}
		/** 为Button添加点击事件 */
		holder.ll_question_tag.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putLong("catagoryId", list.get(position).category.id);
				bundle.putString("catagoryName",
						list.get(position).category.name);
				LggsUtils.StartIntent(context, CategoryActivity.class, bundle);
			}
		});
		// 去回答该问题
		holder.tv_question_toanswer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						AnswerOrCommentActivity.class);
				intent.putExtra("title", "回答");
				intent.putExtra("questionId", list.get(position).id);
				// 还有其他参数要传
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	/** 存放控件 */
	public final class ViewHolder {
		/** 热门头部 用于控制隐藏或显示 */
		public LinearLayout ll_home_hot;
		/**
		 * 热门标签或热门回答
		 */
		public ImageView hot_img;
		/** 该问题分数 */
		public TextView tv_question_score;
		/** 该问题回答数 */
		public TextView question_sum;
		/** 分割线 */
		public TextView tv_question_topline;
		/** 用户头像 */
		public com.szgs.bbs.common.view.CircleImageView img_question_icon;
		/** 用户昵称 */
		public TextView tv_question_username;
		/** 时间 */
		public TextView tv_question_time;
		/** 提问内容 */
		public TextView tv_question_content;
		/** 当时我的提问时显示 其他隐藏 */
		public ImageView img_question_listedit;
		public LinearLayout ll_question_tag;
		/** 提问种类 */
		public TextView tv_tagname;
		/** 添加回答 */
		public TextView tv_question_toanswer;

		/**
		 * 问题底部 分区 添加回答
		 */
		// public TextView tv_qustion_bottomline;
		public LinearLayout bottomll_fqu;

		/**
		 * 分区 本区精华
		 */
		public View rl_category_top;
		public TextView rl_category_top_like_num, tv_question_or_answer,
				tv_answer_content;
		public TextView question_sum2;
		public ImageView img_solve_icon;
	}
}
