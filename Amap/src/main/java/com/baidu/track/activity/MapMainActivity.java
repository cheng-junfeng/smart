package com.baidu.track.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baidu.map.ui.MapBaseActivity;
import com.baidu.map.ui.MapRouteActivity;
import com.baidu.track.R;
import com.baidu.track.R2;
import com.baidu.track.adapter.MyAdapter;
import com.baidu.track.control.Bmap;
import com.baidu.track.model.ItemInfo;
import com.baidu.track.utils.BitmapUtil;
import com.baidu.track.utils.CommonUtil;
import com.wu.safe.base.utils.LogUtil;
import com.wu.safe.base.utils.ShareUtil;
import com.wu.safe.base.utils.ToolbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MapMainActivity extends BmapBaseCompatActivity {

    private final static String TAG = "MapMainActivity";

    @BindView(R2.id.list)
    ListView mListView;

    private MyAdapter myAdapter = null;
    public List<ItemInfo> itemInfos = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.activity_map_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "百度地图", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initView();
        BitmapUtil.init();
    }

    private void initView() {
        ItemInfo base = new ItemInfo(R.mipmap.icon_tracing, R.string.map_title, R.string.map_desc,
                MapBaseActivity.class);
        ItemInfo route = new ItemInfo(R.mipmap.icon_tracing, R.string.map_route, R.string.map_route_desc,
                MapRouteActivity.class);
        ItemInfo tracing = new ItemInfo(R.mipmap.icon_track_query, R.string.tracing_title, R.string.tracing_desc,
                TracingActivity.class);
        ItemInfo trackQuery = new ItemInfo(R.mipmap.icon_track_query, R.string.track_query_title,
                R.string.track_query_desc, TrackQueryActivity.class);
        itemInfos.add(base);
        itemInfos.add(route);
        itemInfos.add(tracing);
        itemInfos.add(trackQuery);

        myAdapter = new MyAdapter(this, itemInfos);
        mListView.setAdapter(myAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {
                Intent intent = new Intent(MapMainActivity.this, itemInfos.get(index).clazz);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 适配android M，检查权限
        List<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isNeedRequestPermissions(permissions)) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
        }
    }

    private boolean isNeedRequestPermissions(List<String> permissions) {
        // 定位精确位置
        addPermission(permissions, Manifest.permission.ACCESS_FINE_LOCATION);
        // 存储权限
        addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // 读取手机状态
        addPermission(permissions, Manifest.permission.READ_PHONE_STATE);
        return permissions.size() > 0;
    }

    private void addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy");
        Bmap trackApp = Bmap.getInstance();
        CommonUtil.saveCurrentLocation();
        if (ShareUtil.contains("is_trace_started")
                && ShareUtil.getBoolean("is_trace_started", true)) {
            // 退出app停止轨迹服务时，不再接收回调，将OnTraceListener置空
            trackApp.mClient.setOnTraceListener(null);
            trackApp.mClient.stopTrace(trackApp.mTrace, null);
        } else {
            trackApp.mClient.clear();
        }
        trackApp.isTraceStarted = false;
        trackApp.isGatherStarted = false;
        ShareUtil.remove("is_trace_started");
        ShareUtil.remove("is_gather_started");

        BitmapUtil.clear();
    }
}
