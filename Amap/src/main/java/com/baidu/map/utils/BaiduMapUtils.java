package com.baidu.map.utils;


import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;

import com.baidu.mapapi.model.LatLng;


public class BaiduMapUtils {
    //地图平移
    public static void targetPoint(BaiduMap baiduMap, LatLng latLng, float zoom){
        if (baiduMap!=null&&latLng!=null){
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(latLng)
                    .zoom(zoom)
                    .build();
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            baiduMap.animateMapStatus(mMapStatusUpdate);
        }
    }
}
