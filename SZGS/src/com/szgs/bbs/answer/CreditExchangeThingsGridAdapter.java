package com.szgs.bbs.answer;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.szgs.bbs.R;
import com.szgs.bbs.adapter.MyBaseAdapter;
import com.szgs.bbs.answer.CreditExchangeListResponse.Content;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CreditExchangeThingsGridAdapter extends MyBaseAdapter<CreditExchangeListResponse.Content> {

	public CreditExchangeThingsGridAdapter(Context context, List<Content> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getConvertView(int position, View convertView, LayoutInflater inflater, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_credit_exchange_things_item, null);
			holder.ivPic = (ImageView) convertView
					.findViewById(R.id.credit_exchange_item_pic_iv);

			int width = (context.getResources().getDisplayMetrics().widthPixels - convertView
					.getPaddingLeft() * 4) / 2 - 1;// 计算iv的高度，使宽高相同。
			LayoutParams params = (LayoutParams) holder.ivPic.getLayoutParams();
			params.height = width;
			holder.ivPic.setLayoutParams(params);

			holder.tvName = (TextView) convertView
					.findViewById(R.id.credit_exchange_item_name_tv);
			holder.tvPrice = (TextView) convertView
					.findViewById(R.id.credit_exchange_item_price_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 数据处理
		CreditExchangeListResponse.Content item = getItem(position);
		holder.tvName.setText(item.title);
		holder.tvPrice.setText(item.requireScore+"积分");
		ImageLoader.getInstance().displayImage(item.image, holder.ivPic);
		return convertView;
	}

	private class ViewHolder {
		ImageView ivPic;
		TextView tvName;
		TextView tvPrice;
	}


}
