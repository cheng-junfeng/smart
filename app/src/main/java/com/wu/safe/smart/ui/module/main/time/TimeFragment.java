package com.wu.safe.smart.ui.module.main.time;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatFragment;
import com.wu.safe.smart.ui.widget.WatcherBoard;

import java.util.Calendar;

import butterknife.BindView;

public class TimeFragment extends BaseCompatFragment {
    private final static String TAG = "TimeFragment";
    @BindView(R.id.watch)
    WatcherBoard watch;
    @BindView(R.id.time_hour)
    TextView timeHour;
    @BindView(R.id.time_min)
    TextView timeMin;
    @BindView(R.id.time_second)
    TextView timeSecond;

    @Override
    protected int setContentView() {
        return R.layout.main_time;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = super.onCreateView(inflater, container, savedInstanceState);

        initView();
        return containerView;
    }

    private void initView() {
        watch.setOnMoveListener(new WatcherBoard.OnMoveListener() {
            @Override
            public void onMove() {
                onDraw();
            }
        });
    }

    private void onDraw() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY); //时
        int minute = calendar.get(Calendar.MINUTE); //分
        int second = calendar.get(Calendar.SECOND); //秒
        timeHour.setText(checkTime(hour));
        timeMin.setText(checkTime(minute));
        timeSecond.setText(checkTime(second));
    }

    private String checkTime(int time){
        if(time < 10){
            return "0"+time;
        }else{
            return ""+time;
        }
    }
}
