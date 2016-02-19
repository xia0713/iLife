package com.szgs.bbs.adapter;

import java.util.List;

import org.apache.http.util.TextUtils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.szgs.bbs.R;
import com.szgs.bbs.common.util.LggsUtils;
import com.szgs.bbs.find.FindCategoryResponse;

/**
 * 发现首页Adapter
 * 
 * @author db
 * 
 */
public class FindIndexAdapter extends MyBaseAdapter<FindCategoryResponse> {

	private DisplayImageOptions options;

	public FindIndexAdapter(Context context, List<FindCategoryResponse> list) {
		super(context, list);
		options = LggsUtils.inImageLoaderOptions();
	}

	@Override
	public View getConvertView(int position, View convertView,
			LayoutInflater inflater, ViewGroup parent) {
		ViewHolder holder;
		Log.e("Tag", "FindIndexAdapter==" + getCount());
		// 观察convertView随ListView滚动情况
		if (convertView == null) {
			inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.findindex_item, null);
			holder = new ViewHolder();
			holder.find_icon = (ImageView) convertView
					.findViewById(R.id.find_icon);
			holder.find_title = (TextView) convertView
					.findViewById(R.id.find_title);
			holder.find_describe = (TextView) convertView
					.findViewById(R.id.find_describe);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(getItem(position).icon,
				holder.find_icon, options);
		holder.find_title.setText(getItem(position).name);
		if (TextUtils.isEmpty(getItem(position).description)) {
			holder.find_describe.setVisibility(View.GONE);
		} else {
			holder.find_describe.setText(getItem(position).description);
		}
		return convertView;
	}

	class ViewHolder {
		public ImageView find_icon;
		public TextView find_title;
		public TextView find_describe;
	}
}
