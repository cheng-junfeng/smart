package com.baidu.map.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.map.config.Const;
import com.baidu.map.utils.BaiduMapUtils;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.track.R;
import com.baidu.track.R2;
import com.baidu.track.activity.BmapBaseCompatActivity;
import com.wu.safe.base.utils.LogUtil;
import com.wu.safe.base.utils.ToolbarUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 地图基础应用
 * 2018-05-23
 */
public class MapBaseActivity extends BmapBaseCompatActivity implements BDLocationListener{

    private final static String TAG = "MapBaseActivity";
    @BindView(R2.id.mapView)
    MapView mapView;

    //控制器
    private BaiduMap mBaiduMap;
    //定位对象
    private BDLocation curBDLocation;
    //定位客户端
    private LocationClient mLocClient;

    private Context mContext;
    private boolean isRequestLocation;

    @Override
    protected int setContentView() {
        return R.layout.activity_map_base;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "基础地图", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        init();
    }

    private void init(){
        mContext = this;
        mBaiduMap = mapView.getMap();
        BaiduMapUtils.targetPoint(mBaiduMap,
                new LatLng(Const.DEFAULT_CITY_LATITUDE, Const.DEFAULT_CITY_LONGITUDE), 16f);
    }

    @OnClick(R2.id.btn_location)
    public void onViewClicked() {
        LogUtil.d(TAG, "onViewClicked");
        getLocation();
    }

    private void getLocation() {
        if (curBDLocation != null) {
            BaiduMapUtils.targetPoint(mBaiduMap,
                    new LatLng(curBDLocation.getLatitude(), curBDLocation.getLongitude()), 16f);
        } else {
            LogUtil.d(TAG, "getLocation");
            isRequestLocation = true;
            initLocation();
        }
    }

    private void initLocation() {
        if (mLocClient == null) {
            mLocClient = new LocationClient(this);
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true); // 打开gps
            option.setCoorType("bd09ll"); // 设置坐标类型
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
            option.setScanSpan(5000);
            mLocClient.setLocOption(option);
            mLocClient.registerLocationListener(this);
            LogUtil.d(TAG, "initLocation:start");
            mLocClient.start();
        }
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        LogUtil.d(TAG, "onReceiveLocation:"+location);
        if (location == null || mapView == null) {
            return;
        }
        curBDLocation = location;
        LogUtil.d(TAG, "onReceiveLocation:"+location.getCity()+":"+location.getLatitude()+":"+location.getLongitude());
        MyLocationData data = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                .direction(location.getDirection())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(data);
        if(isRequestLocation){
            BaiduMapUtils.targetPoint(mBaiduMap, new LatLng(location.getLatitude(), location.getLongitude()), 16.0f);
        }
    }
}
