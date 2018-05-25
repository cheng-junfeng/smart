package com.wu.safe.user.ui.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.download.DownloadListener;
import com.tencent.bugly.beta.download.DownloadTask;
import com.smart.base.utils.FileUtil;
import com.smart.base.utils.ShareUtil;
import com.smart.base.utils.TimeUtil;
import com.wu.safe.user.R;
import com.wu.safe.user.R2;
import com.wu.safe.user.app.acitvity.UserBaseActivity;
import com.wu.safe.user.config.UserSharePre;

import butterknife.BindView;
import butterknife.OnClick;


public class MyUpgradeActvity extends UserBaseActivity {

    @BindView(R2.id.dialog_title)
    TextView title;
    @BindView(R2.id.version)
    TextView version;
    @BindView(R2.id.size)
    TextView size;
    @BindView(R2.id.time)
    TextView time;
    @BindView(R2.id.tv)
    TextView tv;
    @BindView(R2.id.content)
    TextView content;
    @BindView(R2.id.cancel)
    Button cancel;
    @BindView(R2.id.start)
    Button start;

    @Override
    protected boolean setToolbar() {
        return false;
    }

    @Override
    protected int setContentView() {
        return R.layout.dialog_my_upgrade;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareUtil.put(UserSharePre.MY_UPGRADE_CODE, Beta.getUpgradeInfo().versionCode);

        updateBtn(Beta.getStrategyTask());
        tv.setText("下载进度：" + getRate(Beta.getStrategyTask().getSavedLength(), Beta.getStrategyTask().getTotalLength()));
        title.setText(Beta.getUpgradeInfo().title);
        version.setText("版本：" + Beta.getUpgradeInfo().versionName);
        size.setText("包大小：" + FileUtil.formetFileSize(Beta.getUpgradeInfo().fileSize));
        time.setText("更新时间：" + TimeUtil.milliseconds2String(Beta.getUpgradeInfo().publishTime));
        content.setText(Beta.getUpgradeInfo().newFeature);

        Beta.registerDownloadListener(new DownloadListener() {
            @Override
            public void onReceive(DownloadTask task) {
                updateBtn(task);
                tv.setText("下载进度：" + getRate(task.getSavedLength(), task.getTotalLength()));
            }

            @Override
            public void onCompleted(DownloadTask task) {
                updateBtn(task);
                tv.setText("下载进度：" + getRate(task.getSavedLength(), task.getTotalLength()));
            }

            @Override
            public void onFailed(DownloadTask task, int code, String extMsg) {
                updateBtn(task);
                tv.setText("下载进度：失败，请重试");
            }
        });
    }

    public void updateBtn(DownloadTask task) {
        switch (task.getStatus()) {
            case DownloadTask.INIT:
            case DownloadTask.DELETED:
            case DownloadTask.FAILED: {
                start.setText("开始下载");
            }
            break;
            case DownloadTask.COMPLETE: {
                start.setText("安装");
            }
            break;
            case DownloadTask.DOWNLOADING: {
                start.setText("暂停");
            }
            break;
            case DownloadTask.PAUSED: {
                start.setText("继续下载");
            }
            break;
        }
    }

    @OnClick({R2.id.cancel, R2.id.start})
    public void onViewClicked(View view) {
        int viewId = view.getId();
        if (viewId == R.id.cancel) {
            Beta.cancelDownload();
            finish();
        } else if (viewId == R.id.start) {
            DownloadTask task = Beta.startDownload();
            updateBtn(task);
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Beta.unregisterDownloadListener();
    }

    private String getRate(long saved, long total) {
        if (total <= 0) {
            return "0%";
        } else {
            if (saved == total) {
                return "100%";
            } else {
                double curSize = (double) saved;
                double totalSize = (double) total;
                double rate = (curSize / totalSize) * 100;
                return (long) rate + "%";
            }
        }
    }
}
