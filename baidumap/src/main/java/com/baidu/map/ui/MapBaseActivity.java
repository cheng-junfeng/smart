package com.baidu.map.ui;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.map.config.Const;
import com.baidu.map.utils.BaiduMapUtils;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.track.R;
import com.baidu.track.R2;
import com.baidu.track.activity.BmapBaseCompatActivity;
import com.base.utils.LogUtil;
import com.base.utils.ToolbarUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 地图基础应用
 * 2018-05-23
 */
public class MapBaseActivity extends BmapBaseCompatActivity implements BDLocationListener, SensorEventListener {

    private final static String TAG = "MapBaseActivity";
    @BindView(R2.id.mapView)
    MapView mapView;
    @BindView(R2.id.btn_zoom)
    CheckBox btnZoom;
    @BindView(R2.id.btn_other)
    CheckBox btnOther;
    @BindView(R2.id.btn_star)
    CheckBox btnStar;
    @BindView(R2.id.btn_traffic)
    CheckBox btnTraffic;

    //控制器
    private BaiduMap mBaiduMap;
    //UI手势控制器
    private UiSettings mUiSettings;
    //定位对象
    private BDLocation curBDLocation;
    //定位客户端
    private LocationClient mLocClient;
    //定位数据
    private MyLocationData mLocationData;
    //当前点击点
    private LatLng currentPoint;
    //方位
    private int mCurrentDirection = 0;
    //标记
    BitmapDescriptor tempMarker = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_mark1);
    BitmapDescriptor mLocationMarker = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_point);


    private boolean isRequestLocation;
    private Context mContext;
    //方位
    private Double lastX = 0.0;
    private SensorManager mSensorManager;

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
        setListener();
    }

    private void init() {
        mContext = this;
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务

        mBaiduMap = mapView.getMap();
        mUiSettings = mBaiduMap.getUiSettings();
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //不显示放大放小按钮
        mapView.showZoomControls(false);
        getLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mapView.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    private void setListener() {
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            public void onMapClick(LatLng point) {
                currentPoint = point;
                setMarkerView();
                LogUtil.d(TAG, "单击地图");
            }

            public boolean onMapPoiClick(MapPoi poi) {
                LogUtil.d(TAG, "单击地图Poi");
                return false;
            }
        });
        btnZoom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mUiSettings.setZoomGesturesEnabled(b);
            }
        });
        btnOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //移动
                mUiSettings.setScrollGesturesEnabled(b);
                //旋转
                //mUiSettings.setRotateGesturesEnabled();
            }
        });
        btnStar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                } else {
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                }
            }
        });
        btnTraffic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mBaiduMap.setTrafficEnabled(b);
            }
        });
    }

    @OnClick(R2.id.btn_location)
    public void onViewClicked() {
        LogUtil.d(TAG, "onViewClicked");
        getLocation();
    }

    private void getLocation() {
        if (curBDLocation != null) {
            setLocationView(curBDLocation);
        } else {
            LogUtil.d(TAG, "getLocation");
            isRequestLocation = true;
            startLocation();
            curBDLocation = new BDLocation();
            curBDLocation.setLatitude(Const.DEFAULT_CITY_LATITUDE);
            curBDLocation.setLongitude(Const.DEFAULT_CITY_LONGITUDE);
        }
    }

    private void startLocation() {
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
        setLocationView(location);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            mLocationData = new MyLocationData.Builder()
                    .accuracy(curBDLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection)
                    .latitude(curBDLocation.getLatitude())
                    .longitude(curBDLocation.getLongitude()).build();
            mBaiduMap.setMyLocationData(mLocationData);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    private void setLocationView(BDLocation location) {
        LogUtil.d(TAG, "onReceiveLocation:" + location);
        if (location == null || mapView == null) {
            return;
        }
        curBDLocation = location;
        LogUtil.d(TAG, "onReceiveLocation:" + location.getLatitude() + ":" + location.getLongitude());
        mLocationData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                .direction(location.getDirection())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(mLocationData);
        MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mLocationMarker);
        mBaiduMap.setMyLocationConfiguration(config);
        if (isRequestLocation) {
            BaiduMapUtils.targetPoint(mBaiduMap, new LatLng(location.getLatitude(), location.getLongitude()), 16.0f);
        }
    }

    private void setMarkerView() {
        MarkerOptions ooA = new MarkerOptions().position(currentPoint).icon(tempMarker);
        mBaiduMap.addOverlay(ooA);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        // activity 销毁时同时销毁地图控件
        mapView.onDestroy();
        mapView = null;
    }
}
