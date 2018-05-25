package com.wu.safe.smart.ui.module.other.scan;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.smart.base.utils.DensityUtils;
import com.smart.base.utils.DialogUtils;
import com.smart.base.utils.ToolbarUtil;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;
import com.zxinglib.control.decode.DecodeType;
import com.zxinglib.control.inter.CameraOpenCallBack;
import com.zxinglib.control.inter.OnDiscernListener;
import com.zxinglib.result.ErrorResult;
import com.zxinglib.result.SuccessResult;
import com.zxinglib.ui.ZxingView;

import butterknife.BindView;

public class ScanActivity extends BaseCompatActivity {

    protected static String TAG = "ScanActivity";
    @BindView(R.id.zxingView)
    ZxingView zxingView;

    private View tv_scan_hint;
    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.activity_scan_device;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "扫一扫", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mContext = this;

        initView();
    }

    private void initView() {
        initZxingView(zxingView);

        tv_scan_hint = LayoutInflater.from(getBaseContext()).inflate(R.layout.zxing_scan_hint, null);
        addView2ZxingView(tv_scan_hint);
    }

    private void initZxingView(final ZxingView zxingView) {
        if (zxingView == null) {
            return;
        }
        zxingView.setCropStyle(ZxingView.CROP_STYLE_QR_CODE);
        zxingView.setDebug(false);
        zxingView.setDecodeType(DecodeType.SINGLE_ALL);
        zxingView.setNeedCrop(true);
        zxingView.setCameraOpenCallBack(new CameraOpenCallBack() {
            @Override
            public void onSuccess(Camera camera) {
                zxingView.startRepeatAnimation();
                zxingView.startDiscern();
            }

            @Override
            public void onException(Exception exception) {
                DialogUtils.showToast(mContext, "出现异常");
            }
        });

        zxingView.setOnDiscernListener(new OnDiscernListener() {
            @Override
            public void onSuccess(SuccessResult successResult) {
                if (successResult.getResult() != null) {
                    handScanResult(successResult.rawResult.getText());
                } else {
                    zxingView.startDiscern();
                }
            }

            @Override
            public void onError(ErrorResult errorResult) {
                zxingView.startDiscern();
            }
        });

        zxingView.onCreate();
    }

    private void addView2ZxingView(View tv_scan_hint) {
        int cropViewId = zxingView.getCropLayout().getId();

        //添加到zxingview中
        RelativeLayout.LayoutParams relativeLayoutLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLayoutLp.addRule(RelativeLayout.BELOW, cropViewId);
        relativeLayoutLp.addRule(RelativeLayout.ALIGN_LEFT, cropViewId);
        relativeLayoutLp.addRule(RelativeLayout.ALIGN_RIGHT, cropViewId);
        relativeLayoutLp.setMargins(0, DensityUtils.dp2px(this, 16f), 0, 0);
        zxingView.addView(tv_scan_hint, relativeLayoutLp);
    }

    private Vibrator vibrator;

    private void handScanResult(final String barcodes) {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400, 100, 400};
        vibrator.vibrate(pattern, 1);
        tv_scan_hint.postDelayed(new Runnable() {
            @Override
            public void run() {
                vibrator.cancel();
            }
        }, 1000);

        new AlertDialog.Builder(this).setTitle("Scan").setMessage(barcodes).setPositiveButton("sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ClipboardManager cm = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(barcodes);
                Toast.makeText(getApplicationContext(), "已复制到剪切板", Toast.LENGTH_LONG).show();
                restartPreviewAfterDelay(1000);
            }
        }).show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        zxingView.startRepeatAnimation();
        zxingView.startPreview();
        zxingView.startDiscernDelayed(delayMS);
    }

    @Override
    protected void onResume() {
        zxingView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        zxingView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        zxingView.onDestroy();
        super.onDestroy();
        if (vibrator != null) {
            vibrator.cancel();
        }
    }
}