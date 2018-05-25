package com.hikvison.view.contract;


import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.PlaybackCallBack;
import com.hikvision.netsdk.RealPlayCallBack;

public interface HikContract {

    interface View {
        void showToast(String str);

        void processRealData(int iPlayViewNo, int iDataType, byte[] pDataBuffer, int iDataSize, int iStreamMode);
    }

    interface Presenter {
        boolean initSdk();
        void paramCfg(int iUserID, int m_iStartChan);

        ExceptionCallBack getExceptiongCbf();
        RealPlayCallBack getRealPlayerCbf();
        PlaybackCallBack getPlayerbackPlayerCbf();
    }
}
