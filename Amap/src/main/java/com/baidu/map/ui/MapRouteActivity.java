package com.baidu.map.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.baidu.map.config.Const;
import com.baidu.map.utils.BaiduMapUtils;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.track.R;
import com.baidu.track.R2;
import com.baidu.track.activity.BmapBaseCompatActivity;
import com.wu.safe.base.app.listener.OnPositionSelectListener;
import com.wu.safe.base.utils.DialogUtils;
import com.wu.safe.base.utils.LogUtil;
import com.wu.safe.base.utils.ToolbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 地图中级应用
 * 小窗，路线，导航
 * 2018-05-23
 */
public class MapRouteActivity extends BmapBaseCompatActivity implements OnGetRoutePlanResultListener {

    private final static String TAG = "MapRouteActivity";
    @BindView(R2.id.mapView)
    MapView mapView;
    @BindView(R2.id.btn_mark)
    CheckBox btnMark;

    //控制器
    private BaiduMap mBaiduMap;
    //搜索控制器
    private RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.activity_map_route;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "路线地图", null, new View.OnClickListener() {
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
        mBaiduMap = mapView.getMap();
        //不显示放大放小按钮
        mapView.showZoomControls(false);
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        BaiduMapUtils.targetPoint(mBaiduMap, new LatLng(Const.DEFAULT_CITY_LATITUDE, Const.DEFAULT_CITY_LONGITUDE), 16.0f);
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

    private void setListener() {
        btnMark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mBaiduMap.setTrafficEnabled(b);
            }
        });
    }

    private void setRouteView() {
        mBaiduMap.clear();
        // 处理搜索按钮响应
        PlanNode stNode = PlanNode.withCityNameAndPlaceName("武汉", "华师附中");
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("武汉", "光谷广场");
        mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // activity 销毁时同时销毁地图控件
        mapView.onDestroy();
        mapView = null;
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult var1) {
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult var1) {
    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult var1) {
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            DialogUtils.showToast(mContext, "抱歉，未找到结果");
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            if (result.getRouteLines().size() > 1) {
                final List<DrivingRouteLine> allLines = result.getRouteLines();
                ArrayList<String> allTitle = new ArrayList<>();
                for(int i =0; i<allLines.size(); i++){
                    DrivingRouteLine line = allLines.get(i);
                    allTitle.add((i+1)+". "+getRouteTitle(line.getDuration()));
                }
                DialogUtils.showChooseDialog(mContext, allTitle, new OnPositionSelectListener() {
                    @Override
                    public void onPositiveSelect(int pos) {
                        DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
                        mBaiduMap.setOnMarkerClickListener(overlay);
                        overlay.setData(allLines.get(pos));
                        overlay.addToMap();
                        overlay.zoomToSpan();
                    }
                });
            } else if (result.getRouteLines().size() == 1) {
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            } else {
                DialogUtils.showToast(mContext, "无有效路径");
                return;
            }
        }
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult var1) {
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult var1) {
    }

    @OnClick(R2.id.btn_route)
    public void onViewClicked() {
        setRouteView();
    }

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.icon_start);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.icon_end);
        }
    }

    private String getRouteTitle(int duration){
        String result = "";
        if(duration < 60){
            int last = duration / 60;
            result = "大约 "+duration+"秒";
        }else if(duration < 60*60){
            int mod = duration % 60;
            int last = duration / 60;
            result = "大约 "+last+"分钟 "+mod+"秒";
        }else {
            int last = duration / (60*60);
            result = "大约 "+last+"小时 ";
        }
        return result;
    }
}
