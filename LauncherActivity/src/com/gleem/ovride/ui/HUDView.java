package com.gleem.ovride.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.gleem.ovride.service.OvrideService;

public class HUDView extends ViewGroup {
	public static final float SUCCESS = Float.NEGATIVE_INFINITY;
	public static final float FAILURE = Float.POSITIVE_INFINITY;

	private Paint mLoadPaint;
	public HUDView(Context context, OvrideService theService) {
		super(context);
		// Toast.makeText(getContext(), "HUDView", Toast.LENGTH_LONG).show();
		// initialized with data in onDraw
		OvrideService.ovrRectangle = new Rect();
		mLoadPaint = new Paint();
		mLoadPaint.setAntiAlias(true);
		mLoadPaint.setTextSize(10);
		mLoadPaint.setARGB(255, 255, 0, 0);
	}

	// this class and method are called from service to 'take over' the screen and paint ovuride button
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		final String text = "OVURide";

		// initiate ovrRectangle coordinates (right-side overlay)
		OvrideService.ovrRectangle.set(0, 0, 24, 80);
		// set font properties ...
		mLoadPaint.setAntiAlias(true);
		mLoadPaint.setStrokeWidth(2);
		mLoadPaint.setStrokeCap(Paint.Cap.ROUND);
		mLoadPaint.setTextSize(15);
		mLoadPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF,
				Typeface.BOLD));
		// ... to measure the text width
		float textWidth = mLoadPaint.measureText(text);

		// 25% from the top and 24px wide, 80px high
		mLoadPaint.setColor(Color.WHITE);
		canvas.drawRect(OvrideService.ovrRectangle, mLoadPaint);

		mLoadPaint.setColor(Color.BLACK);
		canvas.save();
		// TODO: This HAS TO be based on device screen size
		// overridden textWidth - seems auto calc is wrong
		textWidth = 80;
		// textWidth-10 - textWidth and the margin
		// width-5 - screen width and the margin
		canvas.rotate(-90, 24 - 6, textWidth - 10);
		canvas.drawText(text, 24 - 6, textWidth - 10, mLoadPaint);
		canvas.restore();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true; // consume the event, touch listener takes care of these events
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		// convention requires this method be implemented, nothing to do
	}
}
