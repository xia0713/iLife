package com.szgs.bbs.common.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.szgs.bbs.R;
import com.szgs.bbs.common.util.LggsUtils;


public class SelectPopupWindow extends PopupWindow {
	public SelectPopupWindow(final Activity context, final View mMenuView) {
		super(context);

		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottoms);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(R.color.black);
		// 设置SelectPicPopupF
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_UP) {
					LggsUtils.setWindowAlpha(context, 1f);
					dismiss();
//				}
				return true;
			}
		});
	}
}
