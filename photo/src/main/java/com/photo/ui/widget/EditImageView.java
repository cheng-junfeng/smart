package com.photo.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.photo.ui.widget.model.DrawPoint;
import com.photo.ui.widget.model.PhotoViewAttacher;

import java.util.ArrayList;
import java.util.List;


public class EditImageView extends PhotoView implements View.OnTouchListener {
    Paint paint;
    Bitmap bitmap;
    Canvas bitCanvas;
    PhotoViewAttacher mAttacher;
    List<DrawPoint> points;

    public EditImageView(Context context) {
        this(context, null);
    }

    public EditImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        mAttacher = (PhotoViewAttacher) getIPhotoViewImplementation();
        points = new ArrayList<>();
        setOnTouchListener(this);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            bitCanvas = new Canvas(bitmap);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }


    float x, y, x1, y1;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!isEdit) {
            return mAttacher.onTouch(v, event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                points.add(new DrawPoint(x, y,true));
                break;
            case MotionEvent.ACTION_MOVE:
                x1 = x;
                y1 = y;
                x = event.getX();
                y = event.getY();
                points.add(new DrawPoint(x, y));
                bitCanvas.drawLine(x, y, x1, y1, paint);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    private boolean isEdit;

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public boolean getIsEdi() {
        return isEdit;
    }

    public List<DrawPoint> getPoints() {
        return points;
    }

    public Paint getPaint(){
        return this.paint;
    }
}
