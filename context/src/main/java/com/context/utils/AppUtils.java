package com.context.utils;


import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.context.BuildConfig;
import com.context.R;
import com.context.bean.AppInfo;
import com.context.bean.ProcessInfo;
import com.context.bean.TaskInfo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {
    private static final String TAG = "AppUtils";
    public static List<AppInfo> getAppList(Context context){
        List<AppInfo> apps = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> applicationInfos = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        Log.d(TAG, "app list:"+applicationInfos.size());
        for (ApplicationInfo applicationInfo : applicationInfos) {
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && (applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0) {
                // 非系统应用
                AppInfo app = new AppInfo();
                // 图标
                app.appIcon = ((BitmapDrawable) applicationInfo.loadIcon(pm)).getBitmap();
                // 应用程序名称
                app.appName = applicationInfo.loadLabel(pm).toString();
                // 应用程序包名
                app.appPackageName = applicationInfo.packageName;
                // 应用路径
                app.appPath = applicationInfo.publicSourceDir;
                app.packageInfo = getPackageInfo(context, app.appPackageName);
                apps.add(app);
            }
        }
        return apps;
    }

    public static PackageInfo getPackageInfo(Context context, String packagename) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        }catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }finally {
            return packageInfo;
        }
    }

    public static  List<ProcessInfo> getProcessList(Context context){
        List<ProcessInfo> processes = new ArrayList<>();
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> allProcess = am.getRunningAppProcesses();
        Log.d(TAG, "process list:"+allProcess.size());
        for (ActivityManager.RunningAppProcessInfo info : allProcess) {
            ProcessInfo processInfo = new ProcessInfo();
            processInfo.pid = info.pid+"";
            processInfo.processName = info.processName;
            processInfo.uid = info.uid+"";

            if(info.pkgList != null && info.pkgList.length >0){
                StringBuffer stringBuffer = new StringBuffer();
                for(String temp: info.pkgList){
                    stringBuffer.append(temp+"--");
                }
                processInfo.pkgList = stringBuffer.toString();
            }
            processes.add(processInfo);
        }
        return processes;
    }

    //内存情况
    public static String getUsedPercentValue(Context context) {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
            long availableSize = getAvailableMemory(context) / 1024;
            int percent = (int) ((totalMemorySize - availableSize) / (float) totalMemorySize * 100);
            return percent + "%";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "0%";
    }

    public static long getAvailableMemory(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        mActivityManager.getMemoryInfo(mi);
        return mi.availMem;
    }

    //CPU Name
    public static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //CPU情况 最小
    public static String getMinCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = { "/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq" };
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }

    //CPU情况 最大
    public static String getMaxCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = { "/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }

    //CPU rate
    public static String getCPURateDesc(){
        String path = "/proc/stat";// 系统CPU信息文件
        long totalJiffies[]=new long[2];
        long totalIdle[]=new long[2];
        int firstCPUNum=0;//设置这个参数，这要是防止两次读取文件获知的CPU数量不同，导致不能计算。这里统一以第一次的CPU数量为基准
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        Pattern pattern=Pattern.compile(" [0-9]+");
        for(int i=0;i<2;i++) {
            totalJiffies[i]=0;
            totalIdle[i]=0;
            try {
                fileReader = new FileReader(path);
                bufferedReader = new BufferedReader(fileReader, 8192);
                int currentCPUNum=0;
                String str;
                while ((str = bufferedReader.readLine()) != null&&(i==0||currentCPUNum<firstCPUNum)) {
                    if (str.toLowerCase().startsWith("cpu")) {
                        currentCPUNum++;
                        int index = 0;
                        Matcher matcher = pattern.matcher(str);
                        while (matcher.find()) {
                            try {
                                long tempJiffies = Long.parseLong(matcher.group(0).trim());
                                totalJiffies[i] += tempJiffies;
                                if (index == 3) {//空闲时间为该行第4条栏目
                                    totalIdle[i] += tempJiffies;
                                }
                                index++;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if(i==0){
                        firstCPUNum=currentCPUNum;
                        try {//暂停50毫秒，等待系统更新信息。
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        double rate=-1;
        if (totalJiffies[0]>0&&totalJiffies[1]>0&&totalJiffies[0]!=totalJiffies[1]){
            rate=1.0*((totalJiffies[1]-totalIdle[1])-(totalJiffies[0]-totalIdle[0]))/(totalJiffies[1]-totalJiffies[0]);
        }

//        return String.format("%.2f",rate);
        return (int)(rate*100)+"%";
    }

    //task
    public static List<TaskInfo> getTaskInfos(Context context){
        List<TaskInfo> taskInfos  = new ArrayList<TaskInfo>();
        PackageManager pm = context.getPackageManager();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        Log.d(TAG, "task list:"+runningAppProcesses.size());
        for(ActivityManager.RunningAppProcessInfo info : runningAppProcesses){
            TaskInfo taskInfo = new TaskInfo();
            //进程名称
            String packageName = info.processName;
            taskInfo.setPackageName(packageName);
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
                //图标
                Drawable task_icon = applicationInfo.loadIcon(pm);
                if(task_icon == null){
                    taskInfo.setTask_icon(context.getResources().getDrawable(R.drawable.photo_default_image));
                }else{
                    taskInfo.setTask_icon(task_icon);
                }
                //名称
                String task_name = applicationInfo.loadLabel(pm).toString();
                taskInfo.setTask_name(task_name);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            //进程id
            int pid = info.pid;
            taskInfo.setPid(pid);
            //获取进程占用的内存
            android.os.Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{pid});
            android.os.Debug.MemoryInfo memoryInfo  = processMemoryInfo[0];
            long totalPrivateDirty = memoryInfo.getTotalPrivateDirty(); //KB
            taskInfo.setTask_memory(totalPrivateDirty);
            taskInfos.add(taskInfo);
        }
        return taskInfos;
    }
}
