package com.context.simple;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.context.R;
import com.context.R2;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class TestActivity extends AppCompatActivity {
    public final static String TAG = "TestActivity";

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        setTitle("Test");
    }

    @OnClick({R2.id.all_app, R2.id.all_process, R2.id.all_ram, R2.id.all_task, R2.id.all_process2})
    public void onViewClicked(View view) {
        int viewId = view.getId();
        if(viewId == R.id.all_app){
            Intent intent = new Intent(this, TestSecondActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("POS", 0);
            intent.putExtras(bundle);
            startActivity(intent);
        }else if(viewId == R.id.all_process){
            Intent intent = new Intent(this, TestSecondActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("POS", 1);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if(viewId == R.id.all_ram){
            Intent intent = new Intent(this, TestSecondActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("POS", 2);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if(viewId == R.id.all_task){
            Intent intent = new Intent(this, TestSecondActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("POS", 3);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if(viewId == R.id.all_process2){
            Intent intent = new Intent(this, TestSecondActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("POS", 4);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
