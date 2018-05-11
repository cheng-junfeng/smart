package com.baidu.track.control;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.entity.LocRequest;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.track.LatestPointRequest;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.OnCustomAttributeListener;
import com.baidu.trace.model.ProcessOption;
import com.baidu.track.R;
import com.baidu.track.activity.TracingActivity;
import com.baidu.track.utils.CommonUtil;
import com.baidu.track.utils.NetUtil;
import com.wu.safe.base.utils.LogUtil;
import com.wu.safe.base.utils.ShareUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Bmap {
    public final static String TAG = "Bmap";

    public static String entityName = "myTrace";
    private AtomicInteger mSequenceGenerator = new AtomicInteger();

    private LocRequest locRequest = null;
    public LBSTraceClient mClient = null;
    public Trace mTrace = null;
    public long serviceId = 165006;

    public boolean isTraceStarted = false;
    public boolean isGatherStarted = false;

    private static Bmap map;
    public static Bmap getInstance(){
        if(map == null){
            map = new Bmap();
        }
        return map;
    }

    public void init(Context mContext){
        entityName = CommonUtil.getImei(mContext);
        LogUtil.d(TAG, "init:"+entityName);

        SDKInitializer.initialize(mContext);

        mClient = new LBSTraceClient(mContext);
        mTrace = new Trace(serviceId, entityName);
        mTrace.setNotification(getNotification(mContext));

        locRequest = new LocRequest(serviceId);
        mClient.setOnCustomAttributeListener(new OnCustomAttributeListener() {
            @Override
            public Map<String, String> onTrackAttributeCallback() {
                LogUtil.d(TAG,"onTrackAttributeCallback");
                Map<String, String> map = new HashMap<>();
                map.put("key1", "value1");
                map.put("key2", "value2");
                return map;
            }

            @Override
            public Map<String, String> onTrackAttributeCallback(long locTime) {
                LogUtil.d(TAG,"onTrackAttributeCallback, locTime : " + locTime);
                Map<String, String> map = new HashMap<>();
                map.put("key1", "value1");
                map.put("key2", "value2");
                return map;
            }
        });

        clearTraceStatus();
    }

    @TargetApi(16)
    private Notification getNotification(Context mContext) {
        Notification notification = null;
        Notification.Builder builder = new Notification.Builder(mContext);
        Intent notificationIntent = new Intent(mContext, TracingActivity.class);

        Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(),
                R.mipmap.icon_tracing);

        // 设置PendingIntent
        builder.setContentIntent(PendingIntent.getActivity(mContext, 0, notificationIntent, 0))
                .setLargeIcon(icon)  // 设置下拉列表中的图标(大图标)
                .setContentTitle("百度鹰眼") // 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.icon_tracing) // 设置状态栏内的小图标
                .setContentText("服务正在运行...") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

        notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        return notification;
    }

    /**
     * 获取当前位置
     */
    public void getCurrentLocation(Context mContext, OnEntityListener entityListener, OnTrackListener trackListener) {
        // 网络连接正常，开启服务及采集，则查询纠偏后实时位置；否则进行实时定位
        if (NetUtil.isNetworkAvailable(mContext)
                && ShareUtil.contains("is_trace_started")
                && ShareUtil.contains("is_gather_started")
                && ShareUtil.getBoolean("is_trace_started", false)
                && ShareUtil.getBoolean("is_gather_started", false)) {
            LatestPointRequest request = new LatestPointRequest(getTag(), serviceId, entityName);
            ProcessOption processOption = new ProcessOption();
            processOption.setNeedDenoise(true);
            processOption.setRadiusThreshold(100);
            request.setProcessOption(processOption);
            mClient.queryLatestPoint(request, trackListener);
        } else {
            mClient.queryRealTimeLoc(locRequest, entityListener);
        }
    }

    /**
     * 清除Trace状态：初始化app时，判断上次是正常停止服务还是强制杀死进程，根据trackConf中是否有is_trace_started字段进行判断。
     * <p>
     * 停止服务成功后，会将该字段清除；若未清除，表明为非正常停止服务。
     */
    private void clearTraceStatus() {
        if (ShareUtil.contains("is_trace_started") || ShareUtil.contains("is_gather_started")) {
            ShareUtil.remove("is_trace_started");
            ShareUtil.remove("is_gather_started");
        }
    }

    /**
     * 初始化请求公共参数
     *
     * @param request
     */
    public void initRequest(BaseRequest request) {
        request.setTag(getTag());
        request.setServiceId(serviceId);
    }

    /**
     * 获取请求标识
     *
     * @return
     */
    public int getTag() {
        return mSequenceGenerator.incrementAndGet();
    }
}
