package com.baidu.map.ui;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.map.config.Const;
import com.baidu.map.utils.NavigationUtil;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.track.R;
import com.baidu.track.R2;
import com.baidu.track.activity.BmapBaseCompatActivity;
import com.wu.safe.base.utils.ToolbarUtil;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 地图高级应用
 * 导航
 * 2018-05-23
 */
public class MapNaviActivity extends BmapBaseCompatActivity {

    private final static String TAG = "MapNaviActivity";
    @BindView(R2.id.tvstarting)
    TextView tvstarting;
    @BindView(R2.id.tvending)
    TextView tvending;
    @BindView(R2.id.iv_exchange)
    ImageView ivExchange;

    private boolean changeStartEnd = false;
    private NavigationUtil navigationUtil;

    @Override
    protected int setContentView() {
        return R.layout.activity_map_navi;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "导航", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        navigationUtil = new NavigationUtil(this, "/sdcard/", "FLAMEBNSDK");
        navigationUtil.initNavi();
    }

    @OnClick({R2.id.iv_exchange, R2.id.start_navi})
    public void onViewClicked(View view) {
        int viewId = view.getId();
        if (viewId == R.id.iv_exchange) {
            String startdest = tvstarting.getText().toString();
            String enddest = tvending.getText().toString();
            Animation textanimup = AnimationUtils.loadAnimation(this, R.anim.text_translate_up);
            Animation textanimdown = AnimationUtils.loadAnimation(this, R.anim.text_translate_down);

            tvstarting.setText(enddest);
            tvending.setText(startdest);
            tvstarting.startAnimation(textanimup);
            tvending.startAnimation(textanimdown);

            if (changeStartEnd) {
                changeStartEnd = false;
                Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.route_left);
                ivExchange.startAnimation(operatingAnim);
            } else {
                changeStartEnd = true;
                Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.route_right);
                ivExchange.startAnimation(operatingAnim);
            }
        } else if (viewId == R.id.start_navi) {
            startNavi();
        }
    }

    /**
     * 开始导航
     */
    private void startNavi() {
        if(!changeStartEnd){
            navigationUtil.routePlanToNavi(BNRoutePlanNode.CoordinateType.BD09LL,
                    Const.DEFAULT_CITY_LONGITUDE, Const.DEFAULT_CITY_LATITUDE,
                    Const.END_CITY_LONGITUDE, Const.END_CITY_LATITUDE, "安普顿小镇", "光谷广场");
        }else{
            navigationUtil.routePlanToNavi(BNRoutePlanNode.CoordinateType.BD09LL,
                    Const.END_CITY_LONGITUDE, Const.END_CITY_LATITUDE,
                    Const.DEFAULT_CITY_LONGITUDE, Const.DEFAULT_CITY_LATITUDE, "光谷广场", "安普顿小镇");
        }
    }
}
