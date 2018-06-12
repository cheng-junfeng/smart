package com.context.simple;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.context.R;
import com.context.R2;
import com.context.bean.AppInfo;
import com.context.bean.ProcessInfo;
import com.context.bean.TaskInfo;
import com.context.utils.AppUtils;
import com.context.utils.FileUtil;
import com.context.utils.ProcessUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ContextSecondActivity extends AppCompatActivity {
    public final static String TAG = "ContextSecondActivity";
    @BindView(R2.id.main_view)
    ListView mainView;

    private int mPos = 0;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.context_list);
        ButterKnife.bind(this);
        mContext = this;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            mPos = bundle.getInt("POS", 0);
        }

        switch (mPos){
            case 0:
                initAppView();
                break;
            case 1:
                initProcessView();
                break;
            case 2:
                initRAMView();
                break;
            case 3:
                initTaskView();
                break;
            case 4:
                initTaskView2();
                break;
            default:break;
        }
    }

    private void initAppView() {
        List<AppInfo> apps = AppUtils.getAppList(this);
        Log.d(TAG, "apps:"+apps.size());
        AppListAdapter mAdapter = new AppListAdapter(mContext, apps);
        mainView.setAdapter(mAdapter);
    }

    private void initProcessView() {
        List<ProcessInfo> apps = AppUtils.getProcessList(this);
        Log.d(TAG, "apps:"+apps.size());
        ProcessListAdapter mAdapter = new ProcessListAdapter(mContext, apps);
        mainView.setAdapter(mAdapter);
    }

    private void initRAMView() {
        List<String> allStrs = new ArrayList<>();
        allStrs.add("内存: 占用"+AppUtils.getUsedPercentValue(this)+"  剩余"+ FileUtil.formetFileSize(AppUtils.getAvailableMemory(this)));
        allStrs.add("CPU Rate: 占用"+AppUtils.getCPURateDesc());
        allStrs.add("CPU Name:"+AppUtils.getCpuName());
        allStrs.add("CPU MIN Freq:"+AppUtils.getMinCpuFreq());
        allStrs.add("CPU MAX Freq:"+AppUtils.getMaxCpuFreq());
        RamListAdapter mAdapter = new RamListAdapter(mContext, allStrs);
        mainView.setAdapter(mAdapter);
    }

    private void initTaskView() {
        List<TaskInfo> apps = AppUtils.getTaskInfos(this);
        Log.d(TAG, "task1:"+apps.size());
        TaskListAdapter mAdapter = new TaskListAdapter(mContext, apps);
        mainView.setAdapter(mAdapter);
    }

    private void initTaskView2() {
        List<TaskInfo> apps = ProcessUtils.getTaskInfos(this);
        Log.d(TAG, "task2:"+apps.size());
        TaskListAdapter mAdapter = new TaskListAdapter(mContext, apps);
        mainView.setAdapter(mAdapter);
    }
}
