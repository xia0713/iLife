package com.szgs.bbs.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.szgs.bbs.R;

public class LineText extends TextView {

	public LineText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public LineText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public LineText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);

		Paint Paint = new Paint();

		Paint.setColor(getResources().getColor(R.color.mine_blue));

		// 设置线粗

		Paint.setStrokeWidth(2);

		canvas.drawLine(0, this.getHeight() - 1, this.getWidth(),
				this.getHeight() - 1, Paint);

	}

}
