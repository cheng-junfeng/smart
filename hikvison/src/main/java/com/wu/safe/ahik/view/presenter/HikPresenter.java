package com.wu.safe.ahik.view.presenter;

import android.util.Log;
import android.widget.Toast;

import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_COMPRESSIONCFG_V30;
import com.hikvision.netsdk.PlaybackCallBack;
import com.hikvision.netsdk.RealPlayCallBack;
import com.wu.safe.ahik.view.HikTestActivity;
import com.wu.safe.ahik.view.contract.HikContract;

import org.MediaPlayer.PlayM4.Player;


public class HikPresenter implements HikContract.Presenter {
    private final static String TAG = "DataPresenter";

    private HikContract.View mView;

    public HikPresenter(HikContract.View view){
        this.mView = view;
    }

    @Override
    public boolean initSdk(){
        if (!HCNetSDK.getInstance().NET_DVR_Init()) {
            mView.showToast("HCNetSDK init is failed!");
            Log.e(TAG, "HCNetSDK init is failed!");
            return false;
        }
        HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, "/mnt/sdcard/sdklog/",
                true);
        return true;
    }

    @Override
    public void paramCfg(final int iUserID, int m_iStartChan) {
        // whether have logined on
        if (iUserID < 0) {
            Log.e(TAG, "iUserID < 0");
            return;
        }

        NET_DVR_COMPRESSIONCFG_V30 struCompress = new NET_DVR_COMPRESSIONCFG_V30();
        if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,
                HCNetSDK.NET_DVR_GET_COMPRESSCFG_V30, m_iStartChan,
                struCompress)) {
            Log.e(TAG, "NET_DVR_GET_COMPRESSCFG_V30 failed with error code:"
                    + HCNetSDK.getInstance().NET_DVR_GetLastError());
        } else {
            Log.i(TAG, "NET_DVR_GET_COMPRESSCFG_V30 succ");
        }
        // set substream resolution to cif
        struCompress.struNetPara.byResolution = 1;
        if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID,
                HCNetSDK.NET_DVR_SET_COMPRESSCFG_V30, m_iStartChan,
                struCompress)) {
            Log.e(TAG, "NET_DVR_SET_COMPRESSCFG_V30 failed with error code:"
                    + HCNetSDK.getInstance().NET_DVR_GetLastError());
        } else {
            Log.i(TAG, "NET_DVR_SET_COMPRESSCFG_V30 succ");
        }
    }

    @Override
    public ExceptionCallBack getExceptiongCbf() {
        ExceptionCallBack oExceptionCbf = new ExceptionCallBack() {
            public void fExceptionCallBack(int iType, int iUserID, int iHandle) {
                System.out.println("recv exception, type:" + iType);
            }
        };
        return oExceptionCbf;
    }

    @Override
    public RealPlayCallBack getRealPlayerCbf() {
        RealPlayCallBack cbf = new RealPlayCallBack() {
            public void fRealDataCallBack(int iRealHandle, int iDataType,
                                          byte[] pDataBuffer, int iDataSize) {
                // player channel 1
                mView.processRealData(1, iDataType, pDataBuffer,
                        iDataSize, Player.STREAM_REALTIME);
            }
        };
        return cbf;
    }

    @Override
    public PlaybackCallBack getPlayerbackPlayerCbf() {
        PlaybackCallBack cbf = new PlaybackCallBack() {
            @Override
            public void fPlayDataCallBack(int iPlaybackHandle, int iDataType,
                                          byte[] pDataBuffer, int iDataSize) {
                // player channel 1
                mView.processRealData(1, iDataType, pDataBuffer,
                        iDataSize, Player.STREAM_FILE);
            }
        };
        return cbf;
    }
}
