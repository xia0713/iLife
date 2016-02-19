package com.szgs.bbs.common.view;

import java.util.Properties;

import android.R.interpolator;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * 下拉弹簧效果ScrollView
 * 
 * @author liinfeng
 * 
 */
public class BounceScrollView extends ScrollView {

	private View inner;// 孩子View

	private float y;// 点击时y坐标

	private Rect normal = new Rect();// 矩形，记录layout数据

	private boolean isCount = false;// 是否开始计算

	public BounceScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		if (getChildCount() > 0) {
			inner = getChildAt(0);
		}
	}

	/***
	 * 监听touch
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (inner != null) {
			commOnTouchEvent(ev);
		}

		return super.onTouchEvent(ev);
	}

	/***
	 * 触摸事件
	 * 
	 * @param ev
	 */
	public void commOnTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
			// 手指松开.
			if (isNeedAnimation()) {
				animation();
			}
			isCount = false;
			break;

		case MotionEvent.ACTION_MOVE:
			final float preY = y;// 按下时的y坐标
			float nowY = ev.getY();// 时时y坐标
			int deltaY = (int) (preY - nowY);// 滑动距离

			if (!isCount) {
				deltaY = 0; // 在这里要归0.
			}
			Log.i("y", deltaY + "");
			y = nowY;
			// 当滚动到最上或者最下时就不会再滚动，这时移动布局
			if (isNeedMove() && deltaY < 0) {
				// 初始化头部矩形
				if (normal.isEmpty()) {
					// 保存正常的布局位置
					normal.set(inner.getLeft(), inner.getTop(),
							inner.getRight(), inner.getBottom());
				}
				Log.e("jj", "矩形：" + inner.getLeft() + "," + inner.getTop()
						+ "," + inner.getRight() + "," + inner.getBottom());
				// 移动布局
				inner.layout(inner.getLeft(), inner.getTop() - deltaY / 2,
						inner.getRight(), inner.getBottom() - deltaY / 2);
			}
			isCount = true;
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isChildrenTouchable) {
			return super.onInterceptTouchEvent(ev);
		} else {
			return true;
		}
	}

	private boolean isChildrenTouchable = true;

	public void animation() {

		// 开启移动动画
		TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),
				normal.top);
		ta.setDuration(500);
		ta.setInterpolator(new DecelerateInterpolator());
//		ta.setAnimationListener(new AnimationListener() {
//
//			@Override
//			public void onAnimationStart(Animation animation) {
//				isChildrenTouchable = false;
//				setOnTouchListener(new OnTouchListener() {
//
//					@Override
//					public boolean onTouch(View v, MotionEvent event) {
//						return true;
//					}
//				});
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				isChildrenTouchable = true;
//				setOnTouchListener(new OnTouchListener() {
//
//					@Override
//					public boolean onTouch(View v, MotionEvent event) {
//						return false;
//					}
//				});
//			}
//		});
		inner.startAnimation(ta);
		// 设置回到正常的布局位置
		inner.layout(normal.left, normal.top, normal.right, normal.bottom);

		Log.e("jj", "回归：" + normal.left + "," + normal.top + "," + normal.right
				+ "," + normal.bottom);

		normal.setEmpty();

	}

	// 是否需要开启动画
	public boolean isNeedAnimation() {
		return !normal.isEmpty();
	}

	public boolean isNeedMove() {
		int offset = inner.getMeasuredHeight() - getHeight();
		int scrollY = getScrollY();
		Log.e("jj", "scrolly=" + scrollY);
		// 0是顶部，后面那个是底部
		if (scrollY == 0 || scrollY == offset) {
			return true;
		}
		return false;
	}

}
