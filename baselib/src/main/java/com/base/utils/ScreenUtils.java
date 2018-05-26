package com.base.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenUtils {
	public static 	int 	displayWidth	= 0;
	public static 	int 	displayHeight	= 0;
	
	/**
	 * 返回屏幕分辨率的宽度  px
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
	 */
	public static int getDisplayHeight(Context act) {
		if(displayHeight == 0) {
    		DisplayMetrics dm 	= new DisplayMetrics();
    		((Activity)act).getWindowManager().getDefaultDisplay().getMetrics(dm);
    		displayHeight 		= dm.heightPixels;
    	}
		return displayHeight;
	}
}