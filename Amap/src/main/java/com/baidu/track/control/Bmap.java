package com.baidu.track.control;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

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
import com.smart.base.utils.LogUtil;
import com.smart.base.utils.NotificationUtil;
import com.smart.base.utils.ShareUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Bmap {
    public final static String TAG = "Bmap";
    public static final long serviceId = 165006;
    public static String entityName = "myTrace";

    public boolean isTraceStarted = false;
    public boolean isGatherStarted = false;
    private AtomicInteger mSequenceGenerator = new AtomicInteger();

    public Trace mTrace = null;
    private LocRequest locRequest = null;
    public LBSTraceClient mClient = null;


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

        mTrace = new Trace(serviceId, entityName);
        mTrace.setNotification(getNotification(mContext));
        locRequest = new LocRequest(serviceId);

        mClient = new LBSTraceClient(mContext);
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
        Intent notificationIntent = new Intent(mContext, TracingActivity.class);
        NotificationCompat.Builder builder = NotificationUtil.builderNotification(mContext, R.mipmap.icon_tracing, "百度鹰眼", "服务正在运行...");
        builder.setContentIntent(PendingIntent.getActivity(mContext, 0, notificationIntent, 0));

        Notification notification = builder.build(); // 获取构建好的Notification
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
