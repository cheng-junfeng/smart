package com.file.simple;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.base.app.activity.BaseAppCompatActivity;
import com.base.utils.ToolbarUtil;
import com.file.R;
import com.file.R2;
import com.file.utils.FileUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FileThirdActivity extends BaseAppCompatActivity {
    public final static String TAG = "FileThirdActivity";
    @BindView(R2.id.main_path)
    TextView mainPath;
    @BindView(R2.id.main_view)
    TextView mainView;

    private String mPath;

    @Override
    protected int setContentView() {
        return R.layout.file_third;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        ToolbarUtil.setToolbarLeft(toolbar, "File Content", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPath = bundle.getString("PATH", "/data");
        }

        mainPath.setText(mPath);
        mainView.setText(FileUtil.getFileContent(mPath));
    }
}
