package com.smart.ui.module.floatwindows.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.smart.R;
import com.smart.service.FloatWindowService;
import com.smart.ui.module.floatwindows.control.MyWindowManager;
import com.smart.ui.module.guide.view.GuideActivity;
import com.custom.widget.WatcherBoard;

public class FloatWindowBigView extends LinearLayout {

	/**
	 * 记录大悬浮窗的宽度
	 */
	public static int viewWidth;

	/**
	 * 记录大悬浮窗的高度
	 */
	public static int viewHeight;

	public FloatWindowBigView(final Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.float_window_big, this);
		View view = findViewById(R.id.big_window_layout);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
		ImageView close = (ImageView) findViewById(R.id.close);
		Button back = (Button) findViewById(R.id.back);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 点击关闭悬浮窗的时候，移除所有悬浮窗，并停止Service
				MyWindowManager.removeBigWindow(context);
				MyWindowManager.removeSmallWindow(context);
				Intent intent = new Intent(getContext(), FloatWindowService.class);
				context.stopService(intent);
			}
		});
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 点击返回的时候，移除大悬浮窗，创建小悬浮窗
				MyWindowManager.removeBigWindow(context);
				MyWindowManager.createSmallWindow(context);
			}
		});
		WatcherBoard watch = (WatcherBoard) findViewById(R.id.watch);
		watch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//进入首页
				Intent intent = new Intent(getContext(), GuideActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
			}
		});
	}
}
