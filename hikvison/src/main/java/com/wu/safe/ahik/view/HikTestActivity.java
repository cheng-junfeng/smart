package com.wu.safe.ahik.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_PLAYBACK_INFO;
import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;
import com.hikvision.netsdk.NET_DVR_TIME;
import com.hikvision.netsdk.PTZCommand;
import com.hikvision.netsdk.PlaybackCallBack;
import com.hikvision.netsdk.PlaybackControlCommand;
import com.hikvision.netsdk.RealPlayCallBack;
import com.wu.safe.ahik.R;
import com.wu.safe.ahik.R2;
import com.wu.safe.ahik.test.JNATest;
import com.wu.safe.ahik.test.VoiceTalk;
import com.wu.safe.ahik.view.contract.HikContract;
import com.wu.safe.ahik.view.presenter.HikPresenter;
import com.wu.safe.ahik.view.widget.PlaySurfaceView;

import org.MediaPlayer.PlayM4.Player;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HikTestActivity extends Activity implements Callback, HikContract.View {

    @BindView(R2.id.EDT_IPAddr)
    EditText m_oIPAddr;
    @BindView(R2.id.EDT_Port)
    EditText m_oPort;
    @BindView(R2.id.EDT_User)
    EditText m_oUser;
    @BindView(R2.id.EDT_Psd)
    EditText m_oPsd;

    @BindView(R2.id.Sur_Player)
    SurfaceView m_osurfaceView;

    @BindView(R2.id.btn_Login)
    Button m_oLoginBtn;
    @BindView(R2.id.btn_Preview)
    Button m_oPreviewBtn;
    @BindView(R2.id.btn_Playback)
    Button m_oPlaybackBtn;
    @BindView(R2.id.btn_ParamCfg)
    Button m_oParamCfgBtn;
    @BindView(R2.id.btn_Capture)
    Button m_oCaptureBtn;
    @BindView(R2.id.btn_Record)
    Button m_oRecordBtn;
    @BindView(R2.id.btn_Talk)
    Button m_oTalkBtn;
    @BindView(R2.id.btn_OTHER)
    Button m_oOtherBtn;
    @BindView(R2.id.btn_PTZ)
    Button m_oPTZBtn;

    private NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = null;

    private int m_iLogID = -1; // return by NET_DVR_Login_v30
    private int m_iPlayID = -1; // return by NET_DVR_RealPlay_V30
    private int m_iPlaybackID = -1; // return by NET_DVR_PlayBackByTime

    private int m_iPort = -1; // play port
    private int m_iStartChan = 0; // start channel no
    private int m_iChanNum = 0; // channel number
    private static PlaySurfaceView[] playView = new PlaySurfaceView[4];

    private final String TAG = "HikTestActivity";

    private boolean m_bTalkOn = false;
    private boolean m_bPTZL = false;
    private boolean m_bMultiPlay = false;

    private boolean m_bNeedDecode = true;
    private boolean m_bSaveRealData = false;
    private boolean m_bStopPlayback = false;

    HikContract.Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new HikPresenter(this);

        setContentView(R.layout.main_hikvison);
        ButterKnife.bind(this);

        if (!presenter.initSdk()) {
            finish();
            return;
        }

        m_osurfaceView.getHolder().addCallback(this);
        setListeners();

        m_oIPAddr.setText("10.17.132.49");
        m_oPort.setText("8000");
        m_oUser.setText("admin");
        m_oPsd.setText("hik12345");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("m_iPort", m_iPort);
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        m_iPort = savedInstanceState.getInt("m_iPort");
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState");
    }

    private void setListeners() {
        m_oLoginBtn.setOnClickListener(Login_Listener);
        m_oPreviewBtn.setOnClickListener(Preview_Listener);
        m_oPlaybackBtn.setOnClickListener(Playback_Listener);
        m_oParamCfgBtn.setOnClickListener(ParamCfg_Listener);
        m_oCaptureBtn.setOnClickListener(Capture_Listener);
        m_oRecordBtn.setOnClickListener(Record_Listener);
        m_oTalkBtn.setOnClickListener(Talk_Listener);
        m_oOtherBtn.setOnClickListener(OtherFunc_Listener);
        m_oPTZBtn.setOnTouchListener(PTZ_Listener);
    }

    // ptz listener
    private OnTouchListener PTZ_Listener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            try {
                if (m_iLogID < 0) {
                    Log.e(TAG, "please login on a device first");
                    return false;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (m_bPTZL == false) {
                        if (!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(
                                m_iLogID, m_iStartChan, PTZCommand.PAN_LEFT, 0)) {
                            Log.e(TAG,
                                    "start PAN_LEFT failed with error code: "
                                            + HCNetSDK.getInstance()
                                            .NET_DVR_GetLastError());
                        } else {
                            Log.i(TAG, "start PAN_LEFT succ");
                        }
                    } else {
                        if (!HCNetSDK.getInstance()
                                .NET_DVR_PTZControl_Other(m_iLogID,
                                        m_iStartChan, PTZCommand.PAN_RIGHT, 0)) {
                            Log.e(TAG,
                                    "start PAN_RIGHT failed with error code: "
                                            + HCNetSDK.getInstance()
                                            .NET_DVR_GetLastError());
                        } else {
                            Log.i(TAG, "start PAN_RIGHT succ");
                        }
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (m_bPTZL == false) {
                        if (!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(
                                m_iLogID, m_iStartChan, PTZCommand.PAN_LEFT, 1)) {
                            Log.e(TAG, "stop PAN_LEFT failed with error code: "
                                    + HCNetSDK.getInstance()
                                    .NET_DVR_GetLastError());
                        } else {
                            Log.i(TAG, "stop PAN_LEFT succ");
                        }
                        m_bPTZL = true;
                        m_oPTZBtn.setText("PTZ(R)");
                    } else {
                        if (!HCNetSDK.getInstance()
                                .NET_DVR_PTZControl_Other(m_iLogID,
                                        m_iStartChan, PTZCommand.PAN_RIGHT, 1)) {
                            Log.e(TAG,
                                    "stop PAN_RIGHT failed with error code: "
                                            + HCNetSDK.getInstance()
                                            .NET_DVR_GetLastError());
                        } else {
                            Log.i(TAG, "stop PAN_RIGHT succ");
                        }
                        m_bPTZL = false;
                        m_oPTZBtn.setText("PTZ(L)");
                    }
                }
                return true;
            } catch (Exception err) {
                Log.e(TAG, "error: " + err.toString());
                return false;
            }
        }
    };

    private OnClickListener OtherFunc_Listener = new OnClickListener() {
        public void onClick(View v) {
            JNATest.TEST_Config(m_iPlayID, m_iLogID, m_iStartChan);
        }
    };

    private OnClickListener Talk_Listener = new OnClickListener() {
        public void onClick(View v) {
            try {
                if (m_bTalkOn == false) {
                    if (VoiceTalk.startVoiceTalk(m_iLogID) >= 0) {
                        m_bTalkOn = true;
                        m_oTalkBtn.setText("Stop");
                    }
                } else {
                    if (VoiceTalk.stopVoiceTalk()) {
                        m_bTalkOn = false;
                        m_oTalkBtn.setText("Talk");
                    }
                }
            } catch (Exception err) {
                Log.e(TAG, "error: " + err.toString());
            }
        }
    };

    private OnClickListener Record_Listener = new OnClickListener() {
        public void onClick(View v) {
            if (!m_bSaveRealData) {
                if (!HCNetSDK.getInstance().NET_DVR_SaveRealData(m_iPlayID,
                        "/sdcard/test.mp4")) {
                    System.out.println("NET_DVR_SaveRealData failed! error: "
                            + HCNetSDK.getInstance().NET_DVR_GetLastError());
                    return;
                } else {
                    System.out.println("NET_DVR_SaveRealData succ!");
                }
                m_bSaveRealData = true;
            } else {
                if (!HCNetSDK.getInstance().NET_DVR_StopSaveRealData(m_iPlayID)) {
                    System.out
                            .println("NET_DVR_StopSaveRealData failed! error: "
                                    + HCNetSDK.getInstance()
                                    .NET_DVR_GetLastError());
                } else {
                    System.out.println("NET_DVR_StopSaveRealData succ!");
                }
                m_bSaveRealData = false;
            }
        }
    };

    private OnClickListener Capture_Listener = new OnClickListener() {
        public void onClick(View v) {
            try {
                if (m_iPort < 0) {
                    Log.e(TAG, "please start preview first");
                    return;
                }
                Player.MPInteger stWidth = new Player.MPInteger();
                Player.MPInteger stHeight = new Player.MPInteger();
                if (!Player.getInstance().getPictureSize(m_iPort, stWidth,
                        stHeight)) {
                    Log.e(TAG, "getPictureSize failed with error code:"
                            + Player.getInstance().getLastError(m_iPort));
                    return;
                }
                int nSize = 5 * stWidth.value * stHeight.value;
                byte[] picBuf = new byte[nSize];
                Player.MPInteger stSize = new Player.MPInteger();
                if (!Player.getInstance()
                        .getBMP(m_iPort, picBuf, nSize, stSize)) {
                    Log.e(TAG, "getBMP failed with error code:"
                            + Player.getInstance().getLastError(m_iPort));
                    return;
                }

                SimpleDateFormat sDateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd-hh:mm:ss");
                String date = sDateFormat.format(new Date());
                FileOutputStream file = new FileOutputStream("/mnt/sdcard/"
                        + date + ".bmp");
                file.write(picBuf, 0, stSize.value);
                file.close();
            } catch (Exception err) {
                Log.e(TAG, "error: " + err.toString());
            }
        }
    };

    private OnClickListener Playback_Listener = new OnClickListener() {

        public void onClick(View v) {
            try {
                if (m_iLogID < 0) {
                    Log.e(TAG, "please login on a device first");
                    return;
                }
                if (m_iPlaybackID < 0) {
                    if (m_iPlayID >= 0) {
                        Log.i(TAG, "Please stop preview first");
                        return;
                    }
                    PlaybackCallBack fPlaybackCallBack = presenter.getPlayerbackPlayerCbf();
                    if (fPlaybackCallBack == null) {
                        Log.e(TAG, "fPlaybackCallBack object is failed!");
                        return;
                    }
                    NET_DVR_TIME struBegin = new NET_DVR_TIME();
                    NET_DVR_TIME struEnd = new NET_DVR_TIME();

                    struBegin.dwYear = 2016;
                    struBegin.dwMonth = 6;
                    struBegin.dwDay = 28;

                    struEnd.dwYear = 2016;
                    struEnd.dwMonth = 6;
                    struEnd.dwDay = 29;

                    m_iPlaybackID = HCNetSDK.getInstance()
                            .NET_DVR_PlayBackByTime(m_iLogID, m_iStartChan,
                                    struBegin, struEnd);
                    if (m_iPlaybackID >= 0) {
                        if (!HCNetSDK.getInstance()
                                .NET_DVR_SetPlayDataCallBack(m_iPlaybackID,
                                        fPlaybackCallBack)) {
                            Log.e(TAG, "Set playback callback failed!");
                            return;
                        }
                        NET_DVR_PLAYBACK_INFO struPlaybackInfo = null;
                        if (!HCNetSDK
                                .getInstance()
                                .NET_DVR_PlayBackControl_V40(
                                        m_iPlaybackID,
                                        PlaybackControlCommand.NET_DVR_PLAYSTART,
                                        null, 0, struPlaybackInfo)) {
                            Log.e(TAG, "net sdk playback start failed!");
                            return;
                        }
                        m_bStopPlayback = false;
                        m_oPlaybackBtn.setText("Stop");

                        Thread thread = new Thread() {
                            public void run() {
                                int nProgress = -1;
                                while (true) {
                                    nProgress = HCNetSDK.getInstance()
                                            .NET_DVR_GetPlayBackPos(
                                                    m_iPlaybackID);
                                    System.out
                                            .println("NET_DVR_GetPlayBackPos:"
                                                    + nProgress);
                                    if (nProgress < 0 || nProgress >= 100) {
                                        break;
                                    }

                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                        thread.start();
                    } else {
                        Log.i(TAG, "NET_DVR_PlayBackByTime failed, error code: "
                                        + HCNetSDK.getInstance()
                                        .NET_DVR_GetLastError());
                    }
                } else {
                    m_bStopPlayback = true;
                    if (!HCNetSDK.getInstance().NET_DVR_StopPlayBack(
                            m_iPlaybackID)) {
                        Log.e(TAG, "net sdk stop playback failed");
                    }
                    // player stop play
                    stopSinglePlayer();
                    m_oPlaybackBtn.setText("Playback");
                    m_iPlaybackID = -1;
                }
            } catch (Exception err) {
                Log.e(TAG, "error: " + err.toString());
            }
        }
    };

    private OnClickListener Login_Listener = new OnClickListener() {
        public void onClick(View v) {
            try {
                if (m_iLogID < 0) {
                    // login on the device
                    m_iLogID = loginDevice();
                    if (m_iLogID < 0) {
                        Log.e(TAG, "This device logins failed!");
                        return;
                    } else {
                        System.out.println("m_iLogID=" + m_iLogID);
                    }
                    // get instance of exception callback and set
                    ExceptionCallBack oexceptionCbf = presenter.getExceptiongCbf();
                    if (oexceptionCbf == null) {
                        Log.e(TAG, "ExceptionCallBack object is failed!");
                        return;
                    }

                    if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(
                            oexceptionCbf)) {
                        Log.e(TAG, "NET_DVR_SetExceptionCallBack is failed!");
                        return;
                    }

                    m_oLoginBtn.setText("Logout");
                    Log.i(TAG, "Login sucess ****************************1***************************");
                } else {
                    // whether we have logout
                    if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(m_iLogID)) {
                        Log.e(TAG, " NET_DVR_Logout is failed!");
                        return;
                    }
                    m_oLoginBtn.setText("Login");
                    m_iLogID = -1;
                }
            } catch (Exception err) {
                Log.e(TAG, "error: " + err.toString());
            }
        }
    };

    private OnClickListener Preview_Listener = new OnClickListener() {
        public void onClick(View v) {
            try {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(HikTestActivity.this
                                        .getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                if (m_iLogID < 0) {
                    Log.e(TAG, "please login on device first");
                    return;
                }
                if (m_bNeedDecode) {
                    if (m_iChanNum > 1)// preview more than a channel
                    {
                        if (!m_bMultiPlay) {
                            startMultiPreview();
                            m_bMultiPlay = true;
                            m_oPreviewBtn.setText("Stop");
                        } else {
                            stopMultiPreview();
                            m_bMultiPlay = false;
                            m_oPreviewBtn.setText("Preview");
                        }
                    } else // preivew a channel
                    {
                        if (m_iPlayID < 0) {
                            startSinglePreview();
                        } else {
                            stopSinglePreview();
                            m_oPreviewBtn.setText("Preview");
                        }
                    }
                } else {

                }
            } catch (Exception err) {
                Log.e(TAG, "error: " + err.toString());
            }
        }
    };

    private OnClickListener ParamCfg_Listener = new OnClickListener() {
        public void onClick(View v) {
            try {
                presenter.paramCfg(m_iLogID, m_iStartChan);
            } catch (Exception err) {
                Log.e(TAG, "error: " + err.toString());
            }
        }
    };

    private void startSinglePreview() {
        if (m_iPlaybackID >= 0) {
            Log.i(TAG, "Please stop palyback first");
            return;
        }
        RealPlayCallBack fRealDataCallBack = presenter.getRealPlayerCbf();
        if (fRealDataCallBack == null) {
            Log.e(TAG, "fRealDataCallBack object is failed!");
            return;
        }
        Log.i(TAG, "m_iStartChan:" + m_iStartChan);

        NET_DVR_PREVIEWINFO previewInfo = new NET_DVR_PREVIEWINFO();
        previewInfo.lChannel = m_iStartChan;
        previewInfo.dwStreamType = 0; // substream
        previewInfo.bBlocked = 1;
        m_iPlayID = HCNetSDK.getInstance().NET_DVR_RealPlay_V40(m_iLogID,
                previewInfo, fRealDataCallBack);
        if (m_iPlayID < 0) {
            Log.e(TAG, "NET_DVR_RealPlay is failed!Err:"
                    + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return;
        }

        Log.i(TAG, "NetSdk Play sucess ***********************3***************************");
        m_oPreviewBtn.setText("Stop");
    }

    private void startMultiPreview() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int i = 0;
        for (i = 0; i < 4; i++) {
            if (playView[i] == null) {
                playView[i] = new PlaySurfaceView(this);
                playView[i].setParam(metric.widthPixels);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                params.bottomMargin = playView[i].getCurHeight() - (i / 2)
                        * playView[i].getCurHeight();
                params.leftMargin = (i % 2) * playView[i].getCurWidth();
                params.gravity = Gravity.BOTTOM | Gravity.LEFT;
                addContentView(playView[i], params);
            }
            playView[i].startPreview(m_iLogID, m_iStartChan + i);
        }
        m_iPlayID = playView[0].m_iPreviewHandle;
    }

    private void stopMultiPreview() {
        int i = 0;
        for (i = 0; i < 4; i++) {
            playView[i].stopPreview();
        }
        m_iPlayID = -1;
    }

    private void stopSinglePreview() {
        if (m_iPlayID < 0) {
            Log.e(TAG, "m_iPlayID < 0");
            return;
        }

        // net sdk stop preview
        if (!HCNetSDK.getInstance().NET_DVR_StopRealPlay(m_iPlayID)) {
            Log.e(TAG, "StopRealPlay is failed!Err:"
                    + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return;
        }

        m_iPlayID = -1;
        stopSinglePlayer();
    }

    private void stopSinglePlayer() {
        Player.getInstance().stopSound();
        // player stop play
        if (!Player.getInstance().stop(m_iPort)) {
            Log.e(TAG, "stop is failed!");
            return;
        }

        if (!Player.getInstance().closeStream(m_iPort)) {
            Log.e(TAG, "closeStream is failed!");
            return;
        }
        if (!Player.getInstance().freePort(m_iPort)) {
            Log.e(TAG, "freePort is failed!" + m_iPort);
            return;
        }
        m_iPort = -1;
    }

    private int loginNormalDevice() {
        // get instance
        m_oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
        if (null == m_oNetDvrDeviceInfoV30) {
            Log.e(TAG, "HKNetDvrDeviceInfoV30 new is failed!");
            return -1;
        }
        String strIP = m_oIPAddr.getText().toString();
        int nPort = Integer.parseInt(m_oPort.getText().toString());
        String strUser = m_oUser.getText().toString();
        String strPsd = m_oPsd.getText().toString();
        // call NET_DVR_Login_v30 to login on, port 8000 as default
        int iLogID = HCNetSDK.getInstance().NET_DVR_Login_V30(strIP, nPort,
                strUser, strPsd, m_oNetDvrDeviceInfoV30);
        if (iLogID < 0) {
            Log.e(TAG, "NET_DVR_Login is failed!Err:"
                    + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return -1;
        }
        if (m_oNetDvrDeviceInfoV30.byChanNum > 0) {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartChan;
            m_iChanNum = m_oNetDvrDeviceInfoV30.byChanNum;
        } else if (m_oNetDvrDeviceInfoV30.byIPChanNum > 0) {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartDChan;
            m_iChanNum = m_oNetDvrDeviceInfoV30.byIPChanNum
                    + m_oNetDvrDeviceInfoV30.byHighDChanNum * 256;
        }
        Log.i(TAG, "NET_DVR_Login is Successful!");

        return iLogID;
    }

    private int loginDevice() {
        int iLogID = -1;
        iLogID = loginNormalDevice();
        return iLogID;
    }

    @Override
    public void processRealData(int iPlayViewNo, int iDataType,
                                byte[] pDataBuffer, int iDataSize, int iStreamMode) {
        if (!m_bNeedDecode) {
            // Log.i(TAG, "iPlayViewNo:" + iPlayViewNo + ",iDataType:" +
            // iDataType + ",iDataSize:" + iDataSize);
        } else {
            if (HCNetSDK.NET_DVR_SYSHEAD == iDataType) {
                if (m_iPort >= 0) {
                    return;
                }
                m_iPort = Player.getInstance().getPort();
                if (m_iPort == -1) {
                    Log.e(TAG, "getPort is failed with: "
                            + Player.getInstance().getLastError(m_iPort));
                    return;
                }
                Log.i(TAG, "getPort succ with: " + m_iPort);
                if (iDataSize > 0) {
                    if (!Player.getInstance().setStreamOpenMode(m_iPort,
                            iStreamMode)) // set stream mode
                    {
                        Log.e(TAG, "setStreamOpenMode failed");
                        return;
                    }
                    if (!Player.getInstance().openStream(m_iPort, pDataBuffer,
                            iDataSize, 2 * 1024 * 1024)) // open stream
                    {
                        Log.e(TAG, "openStream failed");
                        return;
                    }
                    if (!Player.getInstance().play(m_iPort,
                            m_osurfaceView.getHolder())) {
                        Log.e(TAG, "play failed");
                        return;
                    }
                    if (!Player.getInstance().playSound(m_iPort)) {
                        Log.e(TAG, "playSound failed with error code:"
                                + Player.getInstance().getLastError(m_iPort));
                        return;
                    }
                }
            } else {
                if (!Player.getInstance().inputData(m_iPort, pDataBuffer,
                        iDataSize)) {
                    // Log.e(TAG, "inputData failed with: " +
                    // Player.getInstance().getLastError(m_iPort));
                    for (int i = 0; i < 4000 && m_iPlaybackID >= 0
                            && !m_bStopPlayback; i++) {
                        if (Player.getInstance().inputData(m_iPort,
                                pDataBuffer, iDataSize)) {
                            break;

                        }

                        if (i % 100 == 0) {
                            Log.e(TAG, "inputData failed with: "
                                    + Player.getInstance()
                                    .getLastError(m_iPort) + ", i:" + i);
                        }

                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    // @Override
    public void surfaceCreated(SurfaceHolder holder) {
        m_osurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        Log.i(TAG, "surface is created" + m_iPort);
        if (-1 == m_iPort) {
            return;
        }
        Surface surface = holder.getSurface();
        if (true == surface.isValid()) {
            if (false == Player.getInstance()
                    .setVideoWindow(m_iPort, 0, holder)) {
                Log.e(TAG, "Player setVideoWindow failed!");
            }
        }
    }

    // @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    // @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "Player setVideoWindow release!" + m_iPort);
        if (-1 == m_iPort) {
            return;
        }
        if (true == holder.getSurface().isValid()) {
            if (false == Player.getInstance().setVideoWindow(m_iPort, 0, null)) {
                Log.e(TAG, "Player setVideoWindow failed!");
            }
        }
    }

    @Override
    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
