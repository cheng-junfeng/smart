package com.baidu.track.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.baidu.trace.model.LocationMode;

import com.baidu.track.R;
import com.baidu.track.utils.Constants;
import com.base.utils.ToolbarUtil;

import static com.baidu.trace.model.LocationMode.High_Accuracy;

public class TracingOptionsActivity extends BmapBaseCompatActivity {

    // 返回结果
    private Intent result = null;

    private EditText gatherIntervalText = null;
    private EditText packIntervalText = null;

    @Override
    protected int setContentView() {
        return R.layout.activity_tracing_options;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, getString(R.string.tracing_options_title), null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        init();
    }

    private void init() {
        gatherIntervalText = (EditText) findViewById(R.id.gather_interval);
        packIntervalText = (EditText) findViewById(R.id.pack_interval);

        gatherIntervalText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                EditText textView = (EditText) view;
                String hintStr = textView.getHint().toString();
                if (hasFocus) {
                    textView.setHint("");
                } else {
                    textView.setHint(hintStr);
                }
            }
        });

        packIntervalText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                EditText textView = (EditText) view;
                String hintStr = textView.getHint().toString();
                if (hasFocus) {
                    textView.setHint("");
                } else {
                    textView.setHint(hintStr);
                }
            }
        });
    }

    public void onCancel(View v) {
        super.onBackPressed();
    }

    public void onFinish(View v) {
        result = new Intent();

        RadioGroup locationModeGroup = (RadioGroup) findViewById(R.id.location_mode);
        RadioButton locationModeRadio = (RadioButton) findViewById(locationModeGroup.getCheckedRadioButtonId());
        LocationMode locationMode = High_Accuracy;
        int viewId = locationModeRadio.getId();
        if(viewId == R.id.device_sensors){
            locationMode = LocationMode.Device_Sensors;
        }else if(viewId == R.id.battery_saving){
            locationMode = LocationMode.Battery_Saving;
        }else if(viewId == R.id.high_accuracy){
            locationMode = High_Accuracy;
        }
        result.putExtra("locationMode", locationMode.name());

        RadioGroup needBosGroup = (RadioGroup) findViewById(R.id.object_storage);
        RadioButton needBosRadio = (RadioButton) findViewById(needBosGroup.getCheckedRadioButtonId());
        boolean isNeedObjectStorage = false;
        viewId = needBosRadio.getId();
        if(viewId == R.id.close_bos){
            isNeedObjectStorage = false;
        }else if (viewId == R.id.open_bos){
            isNeedObjectStorage = true;
        }
        result.putExtra("isNeedObjectStorage", isNeedObjectStorage);

        EditText gatherIntervalText = (EditText) findViewById(R.id.gather_interval);
        EditText packIntervalText = (EditText) findViewById(R.id.pack_interval);
        String gatherIntervalStr = gatherIntervalText.getText().toString();
        String packIntervalStr = packIntervalText.getText().toString();

        if (!TextUtils.isEmpty(gatherIntervalStr)) {
            try {
                result.putExtra("gatherInterval", Integer.parseInt(gatherIntervalStr));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(packIntervalStr)) {
            try {
                result.putExtra("packInterval", Integer.parseInt(packIntervalStr));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        setResult(Constants.RESULT_CODE, result);
        super.onBackPressed();
    }
}
