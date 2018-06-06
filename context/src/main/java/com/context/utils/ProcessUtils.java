package com.context.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.context.R;
import com.context.bean.TaskInfo;
import com.context.processes.ProcessManager;
import com.context.processes.models.AndroidAppProcess;

import java.util.ArrayList;
import java.util.List;

public class ProcessUtils {
    public static List<TaskInfo> getTaskInfos(Context context) {
        // 应用程序管理器
        ActivityManager am = (ActivityManager) context
                .getSystemService(context.ACTIVITY_SERVICE);

        // 应用程序包管理器
        PackageManager pm = context.getPackageManager();

        // 获取正在运行的程序信息, 就是以下粗体的这句代码,获取系统运行的进程     要使用这个方法，需要加载

        List<AndroidAppProcess> processInfos = ProcessManager.getRunningAppProcesses();

        List<TaskInfo> taskinfos = new ArrayList<TaskInfo>();
        // 遍历运行的程序,并且获取其中的信息
        for (AndroidAppProcess processInfo : processInfos) {
            TaskInfo taskinfo = new TaskInfo();
            // 应用程序的包名
            String packname = processInfo.name;
            taskinfo.setPackageName(packname);
            taskinfo.setPid(processInfo.pid);
            // 湖区应用程序的内存 信息
            android.os.Debug.MemoryInfo[] memoryInfos = am
                    .getProcessMemoryInfo(new int[] { processInfo.pid });
            long memsize = memoryInfos[0].getTotalPrivateDirty() * 1024L;
            taskinfo.setTask_memory(memsize);
            try {
                // 获取应用程序信息
                ApplicationInfo applicationInfo = pm.getApplicationInfo(
                        packname, 0);
                Drawable icon = applicationInfo.loadIcon(pm);
                taskinfo.setTask_icon(icon);
                String name = applicationInfo.loadLabel(pm).toString();
                taskinfo.setTask_name(name);
                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    // 用户进程
                    taskinfo.setUserTask(true);
                } else {
                    // 系统进程
                    taskinfo.setUserTask(false);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                // 系统内核进程 没有名称
                taskinfo.setTask_name(packname);
                Drawable icon = context.getResources().getDrawable(
                        R.drawable.photo_default_image);
                taskinfo.setTask_icon(icon);
            }
            if (taskinfo != null) {
                taskinfos.add(taskinfo);
            }
        }
        return taskinfos;
    }
}
