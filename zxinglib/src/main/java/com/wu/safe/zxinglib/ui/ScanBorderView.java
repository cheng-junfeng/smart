package com.wu.safe.zxinglib.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;


public class ScanBorderView extends View {
    private int mViewWidth;
    private int mViewHeight;
    private Paint paint;

    private boolean drawCenterLine = false;

    public void setDrawCenterLine(boolean drawCenterLine) {
        this.drawCenterLine = drawCenterLine;
    }

    public ScanBorderView(Context context) {
        super(context);

        init();
    }

    public ScanBorderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ScanBorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private float lineLen;
    private float lineW;
    private float halfLineW;
    private int middleLineW;
    private int margin;

    private void init() {

        lineLen = dp2px(getContext(), 16f);
        lineW = dp2px(getContext(), 3f);
        halfLineW = lineW / 2;
        middleLineW = dp2px(getContext(), 2f);
        margin = dp2px(getContext(), 6f);

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL); //设置空心
        paint.setStrokeWidth(lineW);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.parseColor("#2f89ff"));
        paint.setAntiAlias(true);  //消除锯齿
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mViewWidth != w || mViewHeight != h) {
            mViewWidth = w;
            mViewHeight = h;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float startX, startY, stopX, stopY;

        paint.setStrokeWidth(lineW);

        //左上角
        startX = 0;
        startY = halfLineW;
        stopX = lineLen;
        stopY = startY;
        canvas.drawLine(startX, startY, stopX, stopY, paint);
        startX = halfLineW;
        startY = 0;
        stopX = startX;
        stopY = lineLen;
        canvas.drawLine(startX, startY, stopX, stopY, paint);

        //左下角
        startX = halfLineW;
        startY = mViewHeight - lineLen;
        stopX = startX;
        stopY = mViewHeight;
        canvas.drawLine(startX, startY, stopX, stopY, paint);
        startX = 0;
        startY = mViewHeight - halfLineW;
        stopX = lineLen;
        stopY = startY;
        canvas.drawLine(startX, startY, stopX, stopY, paint);

        //右上角
        startX = mViewWidth - lineLen;
        startY = halfLineW;
        stopX = mViewWidth;
        stopY = startY;
        canvas.drawLine(startX, startY, stopX, stopY, paint);
        startX = mViewWidth - halfLineW;
        startY = 0;
        stopX = startX;
        stopY = lineLen;
        canvas.drawLine(startX, startY, stopX, stopY, paint);

        //右下角
        startX = mViewWidth - lineLen;
        startY = mViewHeight - halfLineW;
        stopX = mViewWidth;
        stopY = startY;
        canvas.drawLine(startX, startY, stopX, stopY, paint);
        startX = mViewWidth - halfLineW;
        startY = mViewHeight - lineLen;
        stopX = startX;
        stopY = mViewHeight;
        canvas.drawLine(startX, startY, stopX, stopY, paint);

        if (drawCenterLine) {
            paint.setStrokeWidth(middleLineW);

            startX = margin;
            startY = mViewHeight / 2;
            stopX = mViewWidth - margin;
            stopY = startY;

            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
    }

    /**
     * dp转px
     *
     * @param context
     * @return
     */
    private static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

}
