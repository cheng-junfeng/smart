package com.audio.utils;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;

import com.base.utils.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 录音
 * Created by Wuguojun on 16/11/12.
 */
public class AudioRecoderUtils {

    private static final String AUDIO_URL="/record/";
    private static final String SUFFIX = ".amr";
    //文件路径
    private String filePath;
    //文件夹路径
    private String FolderPath;

    private MediaRecorder mMediaRecorder;
    private final String TAG = this.getClass().getName();
    public static final int MAX_LENGTH = 1000 * 60 * 10;// 最大录音时长1000*60*10;

    public OnAudioStatusUpdateListener audioStatusUpdateListener;

    /**
     * 文件存储默认sdcard/record
     */
    public AudioRecoderUtils() {

        //默认保存路径为/sdcard/record/下
        this(Environment.getExternalStorageDirectory() + AUDIO_URL);
    }

    public AudioRecoderUtils(String filePath) {

        File path = new File(filePath);
        if (!path.exists()) {
            path.mkdirs();
        }

        this.FolderPath = filePath;
    }

    public static List<String> getExistAudioFile(){
        List<String> allAudio = new ArrayList<>();
        if ((Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))) {
            // 选择自己的文件夹
            String path = Environment.getExternalStorageDirectory().getPath();
            // Constants.video_url 是个常量，代表存放视频的文件夹
            File mediaStorageDir = new File(path + AUDIO_URL);
            if (mediaStorageDir.exists()) {
                String [] allPath = mediaStorageDir.list();
                for(int i =0; i < allPath.length; i++){
                    String tempPath = allPath[i];
                    if(tempPath.endsWith(SUFFIX)){
                        allAudio.add(mediaStorageDir.getAbsolutePath()+tempPath);
                    }
                }
            }
        }
        return allAudio;
    }

    private long startTime;
    private long endTime;

    private boolean runControll = true;       // 控制获取声音分贝线程开启


    /**
     * 开始录音 使用amr格式
     * 录音文件
     *
     * @return
     */
    public void startRecord() {
        // 开始录音
        /* ①Initial：实例化MediaRecorder对象 */
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        startRunnable();
        try {
            /* ②setAudioSource/setVedioSource */
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mMediaRecorder.setAudioSamplingRate(8000);
            mMediaRecorder.setAudioEncodingBitRate(16);
            mMediaRecorder.setAudioChannels(1);

            filePath = FolderPath + TimeUtils.getCurrentTime() + SUFFIX;
            /* ③准备 */
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.setMaxDuration(MAX_LENGTH);
            mMediaRecorder.prepare();
            /* ④开始 */
            mMediaRecorder.start();
            // AudioRecord audioRecord.
            /* 获取开始时间* */
            startTime = System.currentTimeMillis();
            updateMicStatus();
            LogUtil.d(TAG, "startTime" + startTime);
        } catch (IllegalStateException e) {
            LogUtil.d(TAG, "启动录音失败" + e.getMessage());
        } catch (IOException e) {
            LogUtil.d(TAG, "启动录音失败" + e.getMessage());
        }
    }

    /**
     * 停止录音
     */
    public long stopRecord() {
        if (mMediaRecorder == null) {
            return 0L;
        }
        endTime = System.currentTimeMillis();
        long audioTime = endTime - startTime;
        stopRunnable();
        // 加入录音时间过短
        if (audioTime < 1000) {
            mMediaRecorder.release();
            mMediaRecorder = null;

            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            filePath = "";
        } else {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }

        audioStatusUpdateListener.onStop(filePath, audioTime);
        filePath = "";
        return audioTime;
    }

    /**
     * 取消录音
     */
    public void cancelRecord() {
        stopRunnable();
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        filePath = "";

    }

    private final Handler mHandler = new Handler();
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };


    private int BASE = 1;
    private int SPACE = 100;// 间隔取样时间

    public void setOnAudioStatusUpdateListener(OnAudioStatusUpdateListener audioStatusUpdateListener) {
        this.audioStatusUpdateListener = audioStatusUpdateListener;
    }

    /**
     * 更新麦克状态
     */
    private void updateMicStatus() {

        if (mMediaRecorder != null) {
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / BASE;
            double db = 0;            // 分贝
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
                if (null != audioStatusUpdateListener) {
                    audioStatusUpdateListener.onUpdate(db, System.currentTimeMillis() - startTime);
                }
            }
            if (runControll) {
                mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
            }
        }
    }

    public void stopRunnable() {
        runControll = false;
    }

    public void startRunnable() {
        runControll = true;
    }

    public interface OnAudioStatusUpdateListener {
        /**
         * 录音中...
         *
         * @param db   当前声音分贝
         * @param time 录音时长
         */
        public void onUpdate(double db, long time);

        /**
         * 停止录音
         *
         * @param filePath 保存路径
         */
        public void onStop(String filePath, long time);
    }
}
