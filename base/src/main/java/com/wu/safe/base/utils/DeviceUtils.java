package com.wu.safe.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;


public class DeviceUtils {
	public static 	int 	displayWidth	= 0;
	public static 	int 	displayHeight	= 0;
	public static 	float	displaydensity	= 0;
	
	/**
	 * 返回屏幕分辨率的宽度  px
	 * @param act
	 * @return
	 */
	public static int getDisplayWidth(Context act){
		if(displayWidth == 0) {
    		DisplayMetrics dm 	= new DisplayMetrics();
    		((Activity)act).getWindowManager().getDefaultDisplay().getMetrics(dm);
    		displayWidth 		= dm.widthPixels;
    	}
		return displayWidth;
	}
	
	/**
	 * 返回屏幕分辨率的高度  px
	 * @param act
	 * @return
	 */
	public static int getDisplayHeight(Context act) {
		if(displayHeight == 0) {
    		DisplayMetrics dm 	= new DisplayMetrics();
    		((Activity)act).getWindowManager().getDefaultDisplay().getMetrics(dm);
    		displayHeight 		= dm.heightPixels;
    	}
		return displayHeight;
	}
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(float dpValue, Context act) {
    	if(displaydensity == 0) {
    		DisplayMetrics dm 	= new DisplayMetrics();
    		((Activity)act).getWindowManager().getDefaultDisplay().getMetrics(dm);
    		displaydensity 		= dm.density;
    	}
    	return (int) (dpValue * displaydensity + 0.5f);
    }
    
	/** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(float pxValue, Context act) {  
    	if(displaydensity == 0) {
    		DisplayMetrics dm 	= new DisplayMetrics();
    		((Activity)act).getWindowManager().getDefaultDisplay().getMetrics(dm);
    		displaydensity 		= dm.density;
    	}
        return (int) (pxValue / displaydensity + 0.5f);  
    }
    
    /**
	 * 方法功能说明 ：判断当前网络状态是否可用
	 * @param 
	 * @return void
	 */
	public static boolean haveInternet(Context context){   
		//delete by wez 2012.11.21  
		return true;   
	}

	/**
	 * 获取当前程序的版本名
	 * @return
	 * @throws Exception
	 */
	public static String getVersionName(Context context) throws Exception{
		//获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		//getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
		return packInfo.versionName;
	}
}