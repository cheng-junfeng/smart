package com.custom.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import com.custom.R;


public class CommEditText extends AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {

    private Drawable mClearDrawable;
    private boolean hasFoucs;

    private Drawable mLeftDrawable;

    private Bitmap pswVisibleBitmap;
    private Bitmap pswUnVisibleBitmap;
    private Bitmap accountVisibleBitmap;

    private int clearDrawableW;

    private Paint paint;
    private int pswBitmapW;
    private int pswBitmapH;

    private boolean pswVisibleEnable;
    private boolean accountVisibleEnable;
    private boolean clearVisibleEnable;

    public CommEditText(Context context) {
        this(context, null);
    }

    public CommEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }


    public CommEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CommEditText);
        pswVisibleEnable = a.getBoolean(R.styleable.CommEditText_ce_psw_visible_enable, false);
        accountVisibleEnable = a.getBoolean(R.styleable.CommEditText_ce_account_visible_enable, false);
        clearVisibleEnable = a.getBoolean(R.styleable.CommEditText_ce_clear_visible_enable, true);
        a.recycle();
    }

    private void init() {
        if(clearVisibleEnable){
            mClearDrawable = getCompoundDrawables()[2];
            if (mClearDrawable == null) {
                mClearDrawable = getResources().getDrawable(R.mipmap.ic_icon_clear);
            }

            clearDrawableW = mClearDrawable.getIntrinsicWidth();
            mClearDrawable.setBounds(0, 0, clearDrawableW, mClearDrawable.getIntrinsicHeight());
            setClearIconVisible(false);
        }
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
        mLeftDrawable = getCompoundDrawables()[0];

        if (mLeftDrawable != null) {
            int intrinsicWidth = mLeftDrawable.getIntrinsicWidth();
            int intrinsicHeight = mLeftDrawable.getIntrinsicHeight();
            int width = dp2px(getContext(), 20f);
            int height = dp2px(getContext(), 20f);
            if (intrinsicWidth >= width) {
                intrinsicWidth = width;
                intrinsicHeight = height;
            }

            mLeftDrawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
            setCompoundDrawables(mLeftDrawable, getCompoundDrawables()[1], getCompoundDrawables()[2], getCompoundDrawables()[3]);
        }

        initBitmap();
    }

    private void initBitmap() {
        paint = new Paint();
        //密码眼睛
        pswUnVisibleBitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_psw_not_visible));
        pswVisibleBitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_psw_visible));
        accountVisibleBitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_account_visible));

        pswBitmapW = pswUnVisibleBitmap.getWidth();
        pswBitmapH = pswUnVisibleBitmap.getHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight()) && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    this.setText("");
                    if (onTextClearListener != null) {
                        onTextClearListener.onTextClear();
                    }
                }
            }

            if (pswVisibleEnable) {
                boolean left = (event.getX() - (viewW - clearBitmapTouchW - pswBitmapTouchW)) > 0;
                boolean right = (event.getX() - (viewW - pswBitmapTouchW)) < 0;
                if (left && right) {
                    setPswVisible(!pswVisible);
                    invalidate();
                }
            }
            if(accountVisibleEnable){
                boolean left = (event.getX() - (viewW - clearBitmapTouchW - accountBitmapTouchW)) > 0;
                boolean right = (event.getX() - (viewW - accountBitmapTouchW)) < 0;
                if (left && right) {
                    if(onGetPopupListener != null){
                        onGetPopupListener.onClick(isShow);
                        isShow = !isShow;
                    }
                    invalidate();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    OnGetPopupListener onGetPopupListener;
    boolean isShow = true;
    public void setListener(OnGetPopupListener listener){
        this.onGetPopupListener = listener;
    }
    public interface OnGetPopupListener {
        void onClick(boolean show);
    }

    public void setPswVisible(boolean pswVisible) {
        this.pswVisible = pswVisible;
        if (pswVisible) {
            setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
        } else {
            setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
        }
    }

    public void setAccountVisible(boolean accountVisible) {
        this.accountVisibleEnable = accountVisible;
        invalidate();
    }

    private OnTextClearListener onTextClearListener;
    private boolean pswVisible = false;
    public void setOnTextClearListener(OnTextClearListener onTextClearListener) {
        this.onTextClearListener = onTextClearListener;
    }

    public interface OnTextClearListener {
        void onTextClear();
    }

    private boolean clearIconEnable = true;
    public void setClearIconEnable(boolean clearIconEnable) {
        this.clearIconEnable = clearIconEnable;
        setClearIconVisible(false);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if (!clearIconEnable){
            return;
        }

        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    public void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    private int viewW;
    private int viewH;

    float left;
    float top;

    int clearBitmapTouchW;
    int pswBitmapTouchW;
    int accountBitmapTouchW;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewW = w;
        viewH = h;

        clearBitmapTouchW = clearDrawableW + dp2px(getContext(), 9f);
        pswBitmapTouchW = pswBitmapW + dp2px(getContext(), 9f);
        accountBitmapTouchW = pswBitmapW + dp2px(getContext(),12f);

        left = viewW - clearBitmapTouchW - pswBitmapTouchW;
        top = (viewH - pswBitmapH) / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (pswVisibleEnable) {
            if (pswVisible) {
                canvas.drawBitmap(pswVisibleBitmap, left, top, paint);
            } else {
                canvas.drawBitmap(pswUnVisibleBitmap, left, top, paint);
            }
        }
        if(accountVisibleEnable){
            canvas.drawBitmap(accountVisibleBitmap, left, top, paint);
        }
    }

    @Override

    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (hasFoucs) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void setShakeAnimation() {
        this.setAnimation(shakeAnimation(5));
    }

    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

    private static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
