package com.base.utils;

import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

public final class NotificationUtil {
    private static final String DEFAULT_CHANNEL_ID = "appUpdate";
    private static final String DEFAULT_CHANNEL_NAME = "Update";
    private static final int NOTIFYID = 0X0101;

    public static NotificationCompat.Builder builderNotification(Context context, int icon, String title, String content) {
        String channelId = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = DEFAULT_CHANNEL_ID;
        }
        return new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setWhen(System.currentTimeMillis())
                .setContentText(content)
                //不能删除
                .setAutoCancel(false)
                //正在交互（如播放音乐）
                .setOngoing(true);
    }

    public static void showNotification(Context context, int icon, String title, String content) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            afterO(manager);
        }
        NotificationCompat.Builder builder = builderNotification(context, icon, title, content)
                .setDefaults(Notification.DEFAULT_SOUND);
        builder.setAutoCancel(true);
        builder.setOngoing(false);
        manager.notify(NOTIFYID, builder.build());
    }

    public static void showNotification(Context context, Intent intent, int icon, String title, String content) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            afterO(manager);
        }
        PendingIntent pi = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = builderNotification(context, icon, title, content)
                .setOngoing(false)
                .setContentIntent(pi)
                .setDefaults(Notification.DEFAULT_SOUND);
        builder.setAutoCancel(true);
        builder.setOngoing(false);
        manager.notify(NOTIFYID, builder.build());
    }

    public static void showProgressNotification(Context context, int icon, String title, String content,
                                                int max, int progress) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            afterO(manager);
        }
        NotificationCompat.Builder builder = builderNotification(context, icon, title, content)
                .setProgress(max, progress, false);
        builder.setAutoCancel(true);
        builder.setOngoing(false);
        manager.notify(NOTIFYID, builder.build());
    }

    public static void showDoneNotification(Context context, int icon, String title, String content, File apk) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //不知道为什么需要先取消之前的进度通知，才能显示完成的通知。
        manager.cancel(NOTIFYID);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName(), apk);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(apk);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = builderNotification(context, icon, title, content)
                .setContentIntent(pi);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(NOTIFYID, notification);
    }

    public static void showErrorNotification(Context context, Intent intent, int icon, String title, String content) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            afterO(manager);
        }
        PendingIntent pi = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = builderNotification(context, icon, title, content)
                .setAutoCancel(true)
                .setOngoing(false)
                .setContentIntent(pi)
                .setDefaults(Notification.DEFAULT_SOUND);
        manager.notify(NOTIFYID, builder.build());
    }

    /**
     * 适配 Android O 通知渠道
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void afterO(NotificationManager manager) {
        //IMPORTANCE_LOW：默认关闭声音与震动、IMPORTANCE_DEFAULT：开启声音与震动
        NotificationChannel channel = new NotificationChannel(DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW);
        //是否在桌面icon右上角展示小圆点
        channel.enableLights(true);
        //是否在久按桌面图标时显示此渠道的通知
        channel.setShowBadge(true);
        //在Android O 上更新进度 不震动
//            channel.enableVibration(true);
        manager.createNotificationChannel(channel);
    }

    /**
     * 获取通知栏颜色
     */
    @SuppressWarnings("deprecation")
    public static int getNotificationColor(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notification = builder.build();
        int layoutId = notification.contentView.getLayoutId();
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(layoutId, null, false);
        if (viewGroup.findViewById(android.R.id.title) != null) {
            return ((TextView) viewGroup.findViewById(android.R.id.title)).getCurrentTextColor();
        }
        return findColor(viewGroup);
    }

    private static int findColor(ViewGroup viewGroupSource) {
        int color = Color.TRANSPARENT;
        LinkedList<ViewGroup> viewGroups = new LinkedList<>();
        viewGroups.add(viewGroupSource);
        while (viewGroups.size() > 0) {
            ViewGroup viewGroup1 = viewGroups.getFirst();
            for (int i = 0; i < viewGroup1.getChildCount(); i++) {
                if (viewGroup1.getChildAt(i) instanceof ViewGroup) {
                    viewGroups.add((ViewGroup) viewGroup1.getChildAt(i));
                } else if (viewGroup1.getChildAt(i) instanceof TextView) {
                    if (((TextView) viewGroup1.getChildAt(i)).getCurrentTextColor() != -1) {
                        color = ((TextView) viewGroup1.getChildAt(i)).getCurrentTextColor();
                    }
                }
            }
            viewGroups.remove(viewGroup1);
        }
        return color;
    }

    private static boolean isSimilarColor(int baseColor, int color) {
        int simpleBaseColor = baseColor | 0xff000000;
        int simpleColor = color | 0xff000000;
        int baseRed = Color.red(simpleBaseColor) - Color.red(simpleColor);
        int baseGreen = Color.green(simpleBaseColor) - Color.green(simpleColor);
        int baseBlue = Color.blue(simpleBaseColor) - Color.blue(simpleColor);
        double value = Math.sqrt(baseRed * baseRed + baseGreen * baseGreen + baseBlue * baseBlue);
        if (value < 180.0) {
            return true;
        }
        return false;
    }

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    public static boolean isNotificationEnabled(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return true;//<19 default open
        }
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
