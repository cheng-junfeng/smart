package com.baidu.map.utils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.baidu.map.listener.NavigationRoutePlanListener;
import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.track.BuildConfig;

import java.util.ArrayList;
import java.util.List;


public class NavigationUtil implements BaiduNaviManager.NavEventListener{
    private Activity activity;

    /**
     * 系统SD卡根目录路径
     */
    private String mSDCardPath;

    /**
     * 应用在SD卡中的目录名
     */
    private String appFolderName;

    /**
     * 标识初始化是否成功
     */
    private boolean hasInitSuccess = false;

    /**
     * 校验信息
     */
    private String authinfo;

    private BNRoutePlanNode.CoordinateType mCoordinateType = null;

    public NavigationUtil(Activity activity, String mSDCardPath, String appFolderName) {
        this.activity = activity;
        this.mSDCardPath = mSDCardPath;
        this.appFolderName = appFolderName;
    }


    /**
     * 百度导航服务授权和引擎初始化
     */
    public void initNavi() {
        BaiduNaviManager.getInstance().init(activity, mSDCardPath, appFolderName, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (BuildConfig.DEBUG) {
                            Toast.makeText(activity, authinfo, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void initStart() {
                if (BuildConfig.DEBUG) {
                    Toast.makeText(activity, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void initSuccess() {
                if (BuildConfig.DEBUG) {
                    Toast.makeText(activity, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                }
                hasInitSuccess = true;
                initSetting();
            }

            @Override
            public void initFailed() {
                if (BuildConfig.DEBUG) {
                    Toast.makeText(activity, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, null, ttsHandler, new BaiduNaviManager.TTSPlayStateListener() {
            @Override
            public void playStart() {
            }

            @Override
            public void playEnd() {
            }
        });
    }

    /**
     * 异步获取百度内部TTS播报状态
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG:
                    //Handler : TTS play start...
                    break;
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG:
                    //Handler : TTS play end...
                    break;
            }
        }
    };

    /**
     * 导航设置
     */
    private void initSetting() {
        //显示路况条 预览条显示
        BNaviSettingManager.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        // 导航中语音播报模式 老手模式
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // 实际道路条件 路况条 开
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
        //到达时自动退出
        BNaviSettingManager.setIsAutoQuitWhenArrived(true);
        //创建Bundle
        Bundle bundle = new Bundle();
        //必须设置APPID，否则会静音，这里的id传入你们自己申请的id
        bundle.putString(BNCommonSettingParam.TTS_APP_ID, "10483907");
        //设置语音播报
        BNaviSettingManager.setNaviSdkParam(bundle);
    }

    /**
     * 路线规划导航
     * @param coType
     * @param longitudeStarting 起点经度
     * @param latitudeStarting 起点维度
     * @param longitudeEnd 终点经度
     * @param latitudeEnd 终点维度
     * @param startingName 起点名字
     * @param endName 终点名字
     */
    public void routePlanToNavi(BNRoutePlanNode.CoordinateType coType, double longitudeStarting, double latitudeStarting,
                                double longitudeEnd, double latitudeEnd, String startingName, String endName) {
        mCoordinateType = coType;
        if (!hasInitSuccess) {
            Toast.makeText(activity, "还未初始化!", Toast.LENGTH_SHORT).show();
            return;
        }

        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        switch (coType) {
            case GCJ02:
                sNode = new BNRoutePlanNode(longitudeStarting, latitudeStarting, startingName, null, coType);
                eNode = new BNRoutePlanNode(longitudeEnd, latitudeEnd, endName, null, coType);
                break;
            case WGS84:
                sNode = new BNRoutePlanNode(longitudeStarting, latitudeStarting, startingName, null, coType);
                eNode = new BNRoutePlanNode(longitudeEnd, latitudeEnd, endName, null, coType);
                break;
            case BD09_MC:
                sNode = new BNRoutePlanNode(longitudeStarting, latitudeStarting, startingName, null, coType);
                eNode = new BNRoutePlanNode(longitudeEnd, latitudeEnd, endName, null, coType);
                break;
            case BD09LL:
                sNode = new BNRoutePlanNode(longitudeStarting, latitudeStarting, startingName, null, coType);
                eNode = new BNRoutePlanNode(longitudeEnd, latitudeEnd, endName, null, coType);
                break;
        }
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<>();
            list.add(sNode);
            list.add(eNode);

            // 开发者可以使用旧的算路接口，也可以使用新的算路接口,可以接收诱导信息等
            // 第四个参数如果为false则是模拟导航
            // BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
            BaiduNaviManager.getInstance().launchNavigator(activity, list, 1, true, new NavigationRoutePlanListener(sNode, activity), this);
        }
    }

    /**
     * 导航过程信息回调接口
     * @param what
     * @param arg1
     * @param arg2
     * @param bundle
     */
    @Override
    public void onCommonEventCall(int what, int arg1, int arg2, Bundle bundle) {
    }
}
