package com.baidu.map.ui;

import android.content.Context;
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
import com.wu.safe.base.utils.LogUtil;
import com.wu.safe.base.utils.ToolbarUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 地图基础应用
 * 2018-05-23
 */
public class MapBaseActivity extends BmapBaseCompatActivity implements BDLocationListener {

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
    //当前点击点
    private LatLng currentPoint;
    //标记
    BitmapDescriptor tempMarker = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_mark1);
    BitmapDescriptor mLocationMarker = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_point);

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
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // activity 销毁时同时销毁地图控件
        mapView.onDestroy();
    }

    private void init() {
        mContext = this;
        mBaiduMap = mapView.getMap();
        mUiSettings = mBaiduMap.getUiSettings();
        //不显示放大放小按钮
        mapView.showZoomControls(false);
        getLocation();
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

    private void setLocationView(BDLocation location) {
        LogUtil.d(TAG, "onReceiveLocation:" + location);
        if (location == null || mapView == null) {
            return;
        }
        curBDLocation = location;
        LogUtil.d(TAG, "onReceiveLocation:" + location.getLatitude() + ":" + location.getLongitude());
        MyLocationData data = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                .direction(location.getDirection())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(data);
        MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        MarkerOptions locationMarker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).icon(mLocationMarker);
        mBaiduMap.addOverlay(locationMarker);

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
}
