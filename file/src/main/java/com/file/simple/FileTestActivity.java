package com.file.simple;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.base.app.activity.BaseAppCompatActivity;
import com.base.utils.ToolbarUtil;
import com.file.R;
import com.file.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FileTestActivity extends BaseAppCompatActivity {

    @BindView(R2.id.search_bar)
    AutoCompleteTextView searchBar;

    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.file_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        ToolbarUtil.setToolbarLeft(toolbar, "File", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mContext = this;
    }

    @OnClick({R2.id.down_setting, R2.id.upload_file, R2.id.tvBrower})
    public void onViewClicked(View view) {
        int viewId = view.getId();
        String path = "/";
        if (viewId == R.id.down_setting) {
            path = "/data";
        } else if (viewId == R.id.upload_file) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else if (viewId == R.id.tvBrower) {
            String searchPath = searchBar.getText().toString().trim();
            if (TextUtils.isEmpty(searchPath)) {
                Toast.makeText(mContext, "path is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            path = searchPath;
        }
        Intent intent = new Intent(this, FileSecondActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("PATH", path);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
