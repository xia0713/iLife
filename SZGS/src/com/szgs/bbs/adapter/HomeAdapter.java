package com.szgs.bbs.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.szgs.bbs.R;
import com.szgs.bbs.ask.AskIndexActivity;
import com.szgs.bbs.common.QuestionListResponse;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.common.view.CircleImageView;
import com.szgs.bbs.common.view.SelectPopupWindow;
import com.szgs.bbs.find.CategoryActivity;
import com.szgs.bbs.mine.MyQuestionsActivity;

/**
 * 首页列表Adapter
 * 
 * @author db
 * 
 */
public class HomeAdapter extends BaseAdapter {
	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	public ArrayList<QuestionListResponse.Question> myList;
	public Context mContext;
	protected SelectPopupWindow popupWindow;
	private DisplayImageOptions options;
	private String keyword;

	public void SetKeyword(String keyword) {
		this.keyword = keyword;
	}

	public HomeAdapter(Context mContext,
			ArrayList<QuestionListResponse.Question> myList) {
		this.mContext = mContext;
		this.myList = myList;
		options = LggsUtils.inImageLoaderOptions();
	}

	@Override
	public int getCount() {
		return myList.size();
	}

	@Override
	public Object getItem(int position) {
		return myList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		// 观察convertView随ListView滚动情况
		mInflater = LayoutInflater.from(mContext);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.homeadapter_item_layout,
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
			holder.img_homeitem_bestanswer = (ImageView) convertView
					.findViewById(R.id.img_homeitem_bestanswer);
			convertView.setTag(holder);// 绑定ViewHolder对象
		} else {
			holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
		}
		/** 设置TextView显示的内容，即我们存放在动态数组中的数据 */

		String tag = myList.get(position).tag;
		if (tag.endsWith("热门回答") || tag.endsWith("热门提问")) {
			holder.ll_home_hot.setVisibility(View.VISIBLE);
			holder.tv_home_topline.setVisibility(View.VISIBLE);
			holder.tv_home_hot.setText(tag);
		} else {
			holder.ll_home_hot.setVisibility(View.GONE);
			holder.tv_home_topline.setVisibility(View.GONE);
		}

		String username = "null";
		if (myList.get(position).askBy != null) {
			username = myList.get(position).askBy.nickname;
			ImageLoader.getInstance().displayImage(
					myList.get(position).askBy.avatar,
					holder.img_homeitem_icon, options);
		}
		if (tag.equals("我的提问")) {
			holder.ll_homeitem_listedit.setVisibility(View.VISIBLE);
			holder.tv_homeitem_username.setText("我的提问");
			holder.tv_tiwenl.setVisibility(View.GONE);
			holder.img_homeitem_bestanswer.setVisibility(View.GONE);
		} else {
			if (myList.get(position).status.description.equals("已解决")) {
				holder.img_homeitem_bestanswer.setVisibility(View.VISIBLE);
			} else {
				holder.img_homeitem_bestanswer.setVisibility(View.GONE);
			}
			holder.ll_homeitem_listedit.setVisibility(View.GONE);
			
			if(username!=null)
			{
				if (username.length() > 7) {
					username = username.substring(0, 7) + "...";
				}
			}else{
				username="null";
			}
			
			holder.tv_homeitem_username.setText(username);
			holder.tv_tiwenl.setVisibility(View.VISIBLE);
		}
		holder.tv_homeitem_time
				.setText(LggsUtils.caculateTime(myList.get(position).askTime,
						LggsUtils.getCurrentTime(), null));

		if (myList.get(position).category != null) {
			holder.tv_tagname.setText(myList.get(position).category.name);
		}
		holder.tv_homeitem_responsesum.setText(myList.get(position).answerCount
				+ "人回答");
		String questioncontent = "";

		if (tag.equals("热门回答")) {
			if (myList.get(position).question.status.description.equals("已解决")) {
				holder.img_homeitem_bestanswer.setVisibility(View.VISIBLE);
			} else {
				holder.img_homeitem_bestanswer.setVisibility(View.GONE);
			}
			holder.tv_homeitem_content.setMaxLines(1);
			holder.tv_homeitem_content.setEllipsize(TruncateAt.END);
			if (myList.get(position).answerBy.nickname == null) {
				holder.tv_homeitem_username
						.setText(myList.get(position).answerBy.mobilePhone);
			} else {
				username = myList.get(position).answerBy.nickname;
				if (username.length() > 7) {
					username = username.substring(0, 7) + "...";
				}
				holder.tv_homeitem_username.setText(username);
			}

			ImageLoader.getInstance().displayImage(
					myList.get(position).answerBy.avatar,
					holder.img_homeitem_icon, options);
			holder.tv_homeitem_response.setText(myList.get(position).excerpt);
			holder.tv_homeitem_responsesum
					.setText(myList.get(position).agreeCount + "赞");
			holder.tv_homeitem_time.setText(LggsUtils.caculateTime(
					myList.get(position).answerTime,
					LggsUtils.getCurrentTime(), null));
			holder.tv_tagname
					.setText(myList.get(position).question.category.name);
			holder.tv_homeitem_response.setVisibility(View.VISIBLE);
			holder.tv_tiwenl.setText("回答了：");

			if (myList.get(position).question.rewardScore != 0) {
				ImageGetter imageGetter = new ImageGetter() {
					@Override
					public Drawable getDrawable(String source) {
						int id = Integer.parseInt(source);
						// 根据id从资源文件中获取图片对象
						Drawable d = mContext.getResources().getDrawable(id);
						d.setBounds(0, 0, d.getIntrinsicWidth(),
								d.getIntrinsicHeight());
						return d;
					}
				};
				holder.tv_homeitem_content.setText(Html.fromHtml("<img src='"
						+ R.drawable.list_credit + "'/>", imageGetter, null));
				questioncontent += myList.get(position).question.rewardScore
						+ " ";
				questioncontent += myList.get(position).question.title;
				holder.tv_homeitem_content.append(LggsUtils.SetTextColorOrange(
						mContext, questioncontent,
						myList.get(position).question.rewardScore + ""));
				// holder.tv_question_content.append(list.get(position).rewardScore+"");
			} else {
				holder.tv_homeitem_content
						.setText(myList.get(position).question.title);
			}

		} else {
			holder.tv_homeitem_content.setMaxLines(3);
			holder.tv_homeitem_content.setEllipsize(TruncateAt.END);
			if (myList.get(position).rewardScore != 0) {
				ImageGetter imageGetter = new ImageGetter() {
					@Override
					public Drawable getDrawable(String source) {
						int id = Integer.parseInt(source);
						// 根据id从资源文件中获取图片对象
						Drawable d = mContext.getResources().getDrawable(id);
						d.setBounds(0, -20, d.getIntrinsicWidth(),
								d.getIntrinsicHeight()-20);
						return d;
					}
				};
				holder.tv_homeitem_content.setText(Html.fromHtml("<img src='"
						+ R.drawable.list_credit + "'/>", imageGetter, null));
				questioncontent += myList.get(position).rewardScore + " ";
				questioncontent += myList.get(position).title;
				holder.tv_homeitem_content.append(LggsUtils.SetTextColorOrange(
						mContext, questioncontent,
						myList.get(position).rewardScore + ""));
				// holder.tv_question_content.append(list.get(position).rewardScore+"");
			} else {
				holder.tv_homeitem_content.setText(myList.get(position).title);
			}
			holder.tv_homeitem_response.setVisibility(View.GONE);
			holder.tv_tiwenl.setText("提问了：");
		}

		/** 为Button添加点击事件 */
		holder.ll_homeitem_tag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				if (myList.get(position).tag.equals("热门回答")) {
					bundle.putLong("catagoryId",
							myList.get(position).question.category.id);
					bundle.putString("catagoryName",
							myList.get(position).question.category.name);
				} else {
					bundle.putLong("catagoryId",
							myList.get(position).category.id);
					bundle.putString("catagoryName",
							myList.get(position).category.name);
				}
				LggsUtils.StartIntent(mContext, CategoryActivity.class, bundle);
			}
		});
		// holder.img_homeitem_icon.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// LggsUtils.StartIntent(mContext,
		// OtherUserInformationActivity.class);
		// }
		// });
		holder.ll_homeitem_listedit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LayoutInflater imgInflater = LayoutInflater.from(mContext);
				View view = imgInflater.inflate(R.layout.modif_question_popup,
						null);
				TextView tv1 = (TextView) view
						.findViewById(R.id.modif_modification);
				TextView modif_seeall = (TextView) view
						.findViewById(R.id.modif_seeall);
				tv1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						LggsUtils.setWindowAlpha(mContext, 1f);
						popupWindow.dismiss();
						Bundle bundle = new Bundle();
						bundle.putBoolean("isEdit", true);
						bundle.putString("title", myList.get(position).title);
						bundle.putLong("id", myList.get(position).id);
						bundle.putInt("reward",
								myList.get(position).rewardScore);
						bundle.putSerializable("category",
								myList.get(position).category);
						LggsUtils.StartIntent(mContext, AskIndexActivity.class,
								bundle);
					}
				});
				modif_seeall.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						LggsUtils.setWindowAlpha(mContext, 1f);
						popupWindow.dismiss();
						LggsUtils.StartIntent(mContext,
								MyQuestionsActivity.class);
					}
				});
				popupWindow = new SelectPopupWindow((Activity) mContext, view);
				popupWindow.showAtLocation(holder.img_homeitem_listedit,
						Gravity.BOTTOM, 0, 0);
				popupWindow.setOutsideTouchable(true);
				popupWindow.setFocusable(false);
				popupWindow.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss() {
						LggsUtils.setWindowAlpha(mContext, 1f);

					}
				});
				LggsUtils.setWindowAlpha(mContext, 0.4f);
			}
		});
		return convertView;
	}

	/** 存放控件 */
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
		public ImageView img_homeitem_bestanswer;

	}

}
