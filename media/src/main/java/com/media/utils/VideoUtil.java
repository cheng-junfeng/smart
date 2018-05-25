package com.media.utils;

import android.os.Environment;

import com.media.videocapture.configuration.CaptureConfiguration;
import com.media.videocapture.configuration.PredefinedCaptureConfigurations;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gray on 2017/11/10.
 * 录制视频工具类
 */

public class VideoUtil {
    //视频存放路径
    private static final String VIDEO_URL="/flame_video";
    //视频码率
    private static final PredefinedCaptureConfigurations.CaptureResolution resolution = PredefinedCaptureConfigurations.CaptureResolution.RES_480P;
    //视频质量
    private static final PredefinedCaptureConfigurations.CaptureQuality quality = PredefinedCaptureConfigurations.CaptureQuality.MEDIUM;
    //视频时长，单位为秒
    private static final int fileDuration = 10 ;
    //视频大小，单位为兆
    private static final int fileSize = CaptureConfiguration.NO_FILESIZE_LIMIT;

    /**
     *创建视频文件路径
     */
    public static String createFileName(){
        // 如果 SD 卡存在，则在外部存储建立一个文件夹用于存放视频
        if ((Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))) {
            // 选择自己的文件夹
            String path = Environment.getExternalStorageDirectory().getPath();
            // Constants.video_url 是个常量，代表存放视频的文件夹
            File mediaStorageDir = new File(path + VIDEO_URL);
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    LogUtil.e("TAG", "文件夹创建失败");
                    return null;
                }
            }

            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyMMddHHmmss");
            String imageFileName = "V" + simpleDateFormat.format(new Date());
            String suffix = ".mp4";
            return mediaStorageDir + File.separator + imageFileName + suffix;
        }
        return null;
    }

    /**
     * 创建录制视频配置信息
     */
    public static CaptureConfiguration createCaptureConfiguration()
    {
        return new CaptureConfiguration(resolution, quality, fileDuration, fileSize);
    }
}
