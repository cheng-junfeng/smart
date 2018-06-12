package com.file.simple;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.base.app.activity.BaseAppCompatActivity;
import com.base.utils.ToolbarUtil;
import com.file.R;
import com.file.R2;
import com.file.bean.FileInfo;
import com.file.utils.FileUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TestSecondActivity extends BaseAppCompatActivity {
    public final static String TAG = "TestSecondActivity";
    @BindView(R2.id.main_view)
    ListView mainView;
    @BindView(R2.id.main_path)
    TextView mainPath;

    private String mPath;
    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.activity_second;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        ToolbarUtil.setToolbarLeft(toolbar, "File List", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mContext = this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPath = bundle.getString("PATH", "/data");
        }

        mainPath.setText(mPath);
        initAppView(mPath);
    }

    private void initAppView(String path) {
        List<FileInfo> allInfo = FileUtil.getFiles(path);
        Log.d(TAG, "allInfo:" + allInfo.size());
        FileListAdapter mAdapter = new FileListAdapter(mContext, allInfo);
        mainView.setAdapter(mAdapter);
    }
}
