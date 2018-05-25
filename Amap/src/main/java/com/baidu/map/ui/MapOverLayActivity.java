package com.baidu.map.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.map.config.Const;
import com.baidu.map.utils.BaiduMapUtils;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.track.R;
import com.baidu.track.R2;
import com.baidu.track.activity.BmapBaseCompatActivity;
import com.smart.base.utils.ToolbarUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 地图覆盖物
 * 小窗，覆盖物
 * 2018-05-23
 */
public class MapOverLayActivity extends BmapBaseCompatActivity {

    private final static String TAG = "MapOverLayActivity";
    @BindView(R2.id.mapView)
    MapView mapView;

    private Marker mMarkerA;
    private Marker mMarkerB;
    private Marker mMarkerC;
    private Marker mMarkerD;

    // 初始化全局 bitmap 信息，不用时 recycle
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_mark1);
    BitmapDescriptor bdB = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_mark2);
    BitmapDescriptor bdC = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_mark3);
    BitmapDescriptor bdD = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_mark4);

    //控制器
    private BaiduMap mBaiduMap;
    private InfoWindow mInfoWindow;
    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.activity_map_overlay;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "覆盖物", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        init();
    }

    private void init() {
        mContext = this;
        mBaiduMap = mapView.getMap();
        //不显示放大放小按钮
        mapView.showZoomControls(false);
        BaiduMapUtils.targetPoint(mBaiduMap, new LatLng(Const.DEFAULT_CITY_LATITUDE, Const.DEFAULT_CITY_LONGITUDE), 16.0f);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                Button button = new Button(getApplicationContext());
                button.setBackgroundResource(R.drawable.dialog_shape_bg);
                InfoWindow.OnInfoWindowClickListener listener = null;
                if (marker == mMarkerA || marker == mMarkerD) {
                    button.setText("更改位置");
                    button.setTextColor(Color.BLACK);
                    button.setWidth(300);
                    listener = new InfoWindow.OnInfoWindowClickListener() {
                        public void onInfoWindowClick() {
                            LatLng ll = marker.getPosition();
                            LatLng llNew = new LatLng(ll.latitude + 0.005,
                                    ll.longitude + 0.005);
                            marker.setPosition(llNew);
                            mBaiduMap.hideInfoWindow();
                        }
                    };
                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                } else if (marker == mMarkerB) {
                    button.setText("更改图标");
                    button.setTextColor(Color.BLACK);
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            marker.setIcon(bdA);
                            mBaiduMap.hideInfoWindow();
                        }
                    });
                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(button, ll, -47);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                } else if (marker == mMarkerC) {
                    button.setText("删除");
                    button.setTextColor(Color.BLACK);
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            marker.remove();
                            mBaiduMap.hideInfoWindow();
                        }
                    });
                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(button, ll, -47);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                }
                return true;
            }
        });
        initOverlay();
    }

    @Override
    protected void onResume() {
        // activity 恢复时同时恢复地图控件
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        // activity 暂停时同时暂停地图控件
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // activity 销毁时同时销毁地图控件
        mapView.onDestroy();
        mapView = null;
        bdA.recycle();
        bdB.recycle();
        bdC.recycle();
        bdD.recycle();
    }

    @OnClick(R2.id.btn_reset)
    public void onViewClicked() {
        clearOverlay();
        initOverlay();
    }

    public void initOverlay() {
        // add marker overlay
        LatLng llA = new LatLng(30.4621437344, 114.3831993812);
        LatLng llB = new LatLng(30.4631437844, 114.3841993812);
        LatLng llC = new LatLng(30.4641437844, 114.3851993812);
        LatLng llD = new LatLng(30.4651437844, 114.3861993812);

        MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdA)
                .zIndex(9).draggable(true);
        ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));

        MarkerOptions ooB = new MarkerOptions().position(llB).icon(bdB)
                .zIndex(5);
        ooB.animateType(MarkerOptions.MarkerAnimateType.drop);
        mMarkerB = (Marker) (mBaiduMap.addOverlay(ooB));

        MarkerOptions ooC = new MarkerOptions().position(llC).icon(bdC)
                .perspective(false).anchor(0.5f, 0.5f).rotate(30).zIndex(7);
        ooC.animateType(MarkerOptions.MarkerAnimateType.grow);
        mMarkerC = (Marker) (mBaiduMap.addOverlay(ooC));

        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
        giflist.add(bdA);
        giflist.add(bdB);
        giflist.add(bdC);
        MarkerOptions ooD = new MarkerOptions().position(llD).icons(giflist)
                .zIndex(0).period(10);
        ooD.animateType(MarkerOptions.MarkerAnimateType.grow);
        mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));

        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
            }

            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(mContext, "拖拽结束，新位置：" + marker.getPosition().latitude + ", " + marker.getPosition().longitude,
                        Toast.LENGTH_LONG).show();
            }

            public void onMarkerDragStart(Marker marker) {
            }
        });
    }

    public void clearOverlay() {
        mBaiduMap.clear();
        mMarkerA = null;
        mMarkerB = null;
        mMarkerC = null;
        mMarkerD = null;
    }
}
