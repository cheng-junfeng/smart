package com.zxing.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zxing.R;
import com.zxing.control.ICropRect;
import com.zxing.control.camera.CameraManager;
import com.zxing.control.camera.CameraPreview;
import com.zxing.control.decode.DecodeAsyncTask;
import com.zxing.control.decode.DecodeInfo;
import com.zxing.control.decode.DecodeType;
import com.zxing.control.decode.Size;
import com.zxing.control.inter.CameraOpenCallBack;
import com.zxing.control.inter.OnDiscernListener;
import com.zxing.control.manager.AmbientLightManager;
import com.zxing.control.manager.BeepManager;
import com.zxing.control.manager.InactivityTimer;


public class ZxingView extends RelativeLayout implements ICropRect {

    private static final String TAG = ZxingView.class.getSimpleName();

    public static int CROP_STYLE_BAR_CODE = 1;
    public static int CROP_STYLE_QR_CODE = 2;

    /**
     * 背景色
     */
    private int bgColor = Color.parseColor("#70000000");

    private boolean isDebug = false;
    private ScanBorderView scanBorderView;
    private ImageView scanLine;
    private View topBgView;
    private View bottomBgView;
    private View leftBgView;
    private View rightBgView;

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    private Handler handler;
    private Context mContext;
    private CameraPreview cameraPreview;
    //    private MultiFormatReader multiFormatReader;
    private FrameLayout cropLayout;

    public FrameLayout getCropLayout() {
        return cropLayout;
    }

    private Rect cropRect;
    private DecodeType decodeType;

    private int cropStyle;

    public void setCropStyle(int cropStyle) {
        if (this.cropStyle != cropStyle) {
            if (cropStyle == CROP_STYLE_BAR_CODE) {
                initCropView(cropStyle);
            } else if (cropStyle == CROP_STYLE_QR_CODE) {
                initCropView(cropStyle);
            }
        }
    }

    private boolean needCrop = true;

    public void setCameraOpenCallBack(CameraOpenCallBack cameraOpenCallBack) {
        if (cameraPreview != null) {
            cameraPreview.setCameraOpenCallBack(cameraOpenCallBack);
        }
    }

    /**
     * 设置是否需要裁剪图片识别 如果设置为false 则从整个预览的图片中识别
     *
     * @param needCrop
     */
    public void setNeedCrop(boolean needCrop) {
        this.needCrop = needCrop;
    }

    private OnDiscernListener onDiscernListener;

    public void setOnDiscernListener(OnDiscernListener onDiscernListener) {
        this.onDiscernListener = onDiscernListener;
    }

    public ZxingView(Context context) {
        super(context);

        init(context, null, -1);
    }

    public ZxingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, -1);
    }

    public ZxingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mContext = context;
        handler = new Handler();

        //添加surfaceView
        cameraPreview = new CameraPreview(mContext);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(cameraPreview, layoutParams);

        setCropStyle(CROP_STYLE_QR_CODE);
    }

    private void initCropView(int cropStyle) {
        this.cropStyle = cropStyle;
        //添加cropView
        int cropW;
        int cropH;
        if (cropStyle == CROP_STYLE_QR_CODE) {
            cropW = dp2px(mContext, 200f);
            cropH = cropW;
        } else {
            //获取屏幕宽度 左右各减去20dp
            cropW = getScreenWidth(mContext) - dp2px(mContext, 40f);
            cropH = dp2px(mContext, 160f);
        }

        if (cropLayout == null) {
            cropLayout = new FrameLayout(mContext);
            cropLayout.setId(R.id.crop_view);
            LayoutParams relativeLayoutLp = new LayoutParams(cropW, cropH);
            relativeLayoutLp.addRule(RelativeLayout.CENTER_IN_PARENT);
            addView(cropLayout, relativeLayoutLp);

            //添加背景框
            scanBorderView = new ScanBorderView(mContext);
            FrameLayout.LayoutParams frameLayoutLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            cropLayout.addView(scanBorderView, frameLayoutLp);

            //添加图片
            scanLine = new ImageView(mContext);
            scanLine.setScaleType(ImageView.ScaleType.FIT_XY);
            scanLine.setImageResource(R.mipmap.zxing_scan_line);

            frameLayoutLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            frameLayoutLp.setMargins(dp2px(mContext, 3f), 0, dp2px(mContext, 3f), 0);
            cropLayout.addView(scanLine, frameLayoutLp);

            //添加上面背景图
            topBgView = new View(mContext);
            topBgView.setId(R.id.top_bg_view);
            topBgView.setBackgroundColor(bgColor);
            relativeLayoutLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            relativeLayoutLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            relativeLayoutLp.addRule(RelativeLayout.ABOVE, cropLayout.getId());
            addView(topBgView, relativeLayoutLp);

            //添加底部背景图
            bottomBgView = new View(mContext);
            bottomBgView.setId(R.id.bottom_bg_view);
            bottomBgView.setBackgroundColor(bgColor);
            relativeLayoutLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            relativeLayoutLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            relativeLayoutLp.addRule(RelativeLayout.BELOW, cropLayout.getId());
            addView(bottomBgView, relativeLayoutLp);

            //添加左边背景图
            leftBgView = new View(mContext);
            leftBgView.setId(R.id.left_bg_view);
            leftBgView.setBackgroundColor(bgColor);
            relativeLayoutLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            relativeLayoutLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            relativeLayoutLp.addRule(RelativeLayout.BELOW, topBgView.getId());
            relativeLayoutLp.addRule(RelativeLayout.ABOVE, bottomBgView.getId());
            relativeLayoutLp.addRule(RelativeLayout.LEFT_OF, cropLayout.getId());
            addView(leftBgView, relativeLayoutLp);

            //添加右边背景图
            rightBgView = new View(mContext);
            rightBgView.setId(R.id.right_bg_view);
            rightBgView.setBackgroundColor(bgColor);
            relativeLayoutLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            relativeLayoutLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            relativeLayoutLp.addRule(RelativeLayout.BELOW, topBgView.getId());
            relativeLayoutLp.addRule(RelativeLayout.ABOVE, bottomBgView.getId());
            relativeLayoutLp.addRule(RelativeLayout.RIGHT_OF, cropLayout.getId());
            addView(rightBgView, relativeLayoutLp);
        } else {
            ViewGroup.LayoutParams layoutParams = cropLayout.getLayoutParams();
            layoutParams.width = cropW;
            layoutParams.height = cropH;
            cropLayout.setLayoutParams(layoutParams);
        }
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }


    private boolean animationRunning;
    private TranslateAnimation animation;

    public void startRepeatAnimation() {
        if (animationRunning) {
            return;
        }
        if (animation == null) {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation
                    .RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                    0.90f);
            animation.setDuration(3000);
            animation.setRepeatCount(-1);
            animation.setRepeatMode(Animation.RESTART);
        }

        animationRunning = true;
        scanLine.setVisibility(View.VISIBLE);
        scanLine.startAnimation(animation);
    }

    public void stopRepeatAnimation() {
        if (animationRunning) {
            animationRunning = false;
            scanLine.clearAnimation();
            scanLine.setVisibility(View.GONE);
        }
    }

    /**
     * 开始识别
     */
    public void startDiscern() {
        boolean surfaceCreated = cameraPreview.isHasSurface();
        if (surfaceCreated) {
            startDiscernDelayed(0);
        } else {
            startDiscernDelayed(1500);
        }
    }

    /**
     * 这里延时是因为第一次进来需要等待相机打开
     *
     * @param delayMillis
     */
    public void startDiscernDelayed(long delayMillis) {
        if (delayMillis <= 0) {
            startSingleHandFrame();
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startSingleHandFrame();
                }
            }, delayMillis);
        }
    }

    /**
     * 设置解码类型
     *
     * @param decodeType
     */
    public void setDecodeType(DecodeType decodeType) {
        this.decodeType = decodeType;
    }

    private DecodeAsyncTask decodeAsyncTask;

    /**
     * 开始处理frame数据
     */
    private void startSingleHandFrame() {
        cameraPreview.startSingleHandFrame(new CameraPreview.CameraFrameCallBack() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {

                DecodeInfo decodeInfo = new DecodeInfo();
                decodeInfo.decodeData = data;
                decodeInfo.decodeType = decodeType;

                if (decodeInfo.decodeType == null) {
                    decodeType = DecodeType.SINGLE_ALL;
                }

                Camera.Size previewSize = camera.getParameters().getPreviewSize();
                Camera.Size pictureSize = camera.getParameters().getPictureSize();

                decodeInfo.cameraPreviewSize = new Size(previewSize.width, previewSize.height);
                decodeInfo.cameraPictureSize = new Size(pictureSize.width, pictureSize.height);

                decodeAsyncTask = new DecodeAsyncTask(onDiscernListener, ZxingView.this);
                decodeAsyncTask.setDebug(isDebug);
                decodeAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, decodeInfo);
            }
        });
    }

    /**
     * 停止识别
     */
    public void stopDiscern() {
        if (decodeAsyncTask != null) {
            decodeAsyncTask.cancel(true);
            decodeAsyncTask = null;
        }
    }

    /**
     * 释放相机
     */
    public void releaseCamera() {
        CameraManager cameraManager = cameraPreview.getCameraManager();
        if (cameraManager != null) {
            cameraManager.closeDriver();
        }
    }

    /**
     * 开始预览
     */
    public void startPreview() {
        if (cameraPreview != null) {
            cameraPreview.startPreview();
        }
    }

    /**
     * 停止预览
     */
    public void stopPreview() {
        if (cameraPreview != null) {
            cameraPreview.stopPreview();
        }
    }

    /**
     * 播放震动和声音
     */
    public void playBeepSoundAndVibrate() {
        if (beepManager != null) {
            beepManager.playBeepSoundAndVibrate();
        }
    }

    /**
     * dp转px
     *
     * @param context
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    @Override
    public Rect getCropRect(int previewW) {
        if (cropLayout != null && getWidth() != 0) {

            float ratio = previewW / (float) getWidth();

            //计算cropLayout
            int left = (int) (cropLayout.getLeft() * ratio);
            int top = (int) (cropLayout.getTop() * ratio);
            int bottom = (int) (cropLayout.getBottom() * ratio);
            int right = (int) (cropLayout.getRight() * ratio);

            cropRect = new Rect(left, top, right, bottom);
        }
        return cropRect;
    }

    @Override
    public boolean needCrop() {
        return needCrop;
    }

    /********* 生命周期方法 **********/

//    private boolean needRestorePreview;

    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private AmbientLightManager ambientLightManager;

    public void onCreate() {
        if (mContext instanceof Activity) {
            Activity activity = (Activity) this.mContext;
            // 保持屏幕常亮
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            inactivityTimer = new InactivityTimer(activity);
            beepManager = new BeepManager(activity);
            ambientLightManager = new AmbientLightManager(mContext.getApplicationContext());
        }
    }

    public void onResume() {
        CameraManager cameraManager = cameraPreview.getCameraManager();
        if (ambientLightManager != null) {
            ambientLightManager.start(cameraManager);
        }
        if (beepManager != null) {
            beepManager.updatePrefs();
        }

        if (inactivityTimer != null) {
            inactivityTimer.onResume();
        }

        if (needRestoreAnimation) {
            needRestoreAnimation = false;
            startRepeatAnimation();
        }

        if (cameraPreview.isHasSurface()) {
            // The activity was paused but not stopped, so the surface still exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            cameraPreview.initCamera();
        } else {
            cameraPreview.addSurfaceCallBack();
        }
//        if (needRestorePreview){
//            needRestorePreview = false;
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    cameraPreview.startPreview();
//                }
//            });
//        }
    }

    private boolean needRestoreAnimation;

    public void onPause() {

        //停止识别
        stopDiscern();

        if (inactivityTimer != null) {
            inactivityTimer.onPause();
        }
        if (ambientLightManager != null) {
            ambientLightManager.stop();
        }
        if (beepManager != null) {
            beepManager.close();
        }

//        boolean previewing = cameraPreview.isPreviewing();
//        if (previewing){
//            needRestorePreview = true;
//        }

        CameraManager cameraManager = cameraPreview.getCameraManager();
        if (cameraManager != null) {
            cameraManager.closeDriver();
        }

        if (!cameraPreview.isHasSurface()) {
            cameraPreview.removeSurfaceCallBack();
        }

        if (animationRunning) {
            needRestoreAnimation = true;
            stopRepeatAnimation();
        }
    }

    public void onDestroy() {
        if (inactivityTimer != null) {
            inactivityTimer.shutdown();
        }

        //移除消息
        handler.removeCallbacksAndMessages(null);

        setCameraOpenCallBack(null);
        setOnDiscernListener(null);

        releaseCamera();
    }

}
