package com.context.bean;

import android.graphics.drawable.Drawable;


public class TaskInfo {
    //图标
    public Drawable task_icon;
    //名称
    public String task_name;
    //占用的内存
    public long task_memory;
    //包名
    public String packageName;
    //进程id
    public int pid;
    // true 用户进程 false 系统进程
    private boolean userTask = true;

    public TaskInfo() {
        super();
    }

    public TaskInfo(Drawable task_icon, String task_name, long task_memory,
                    String packageName, int pid) {
        super();
        this.task_icon = task_icon;
        this.task_name = task_name;
        this.task_memory = task_memory;
        this.packageName = packageName;
        this.pid = pid;
    }

    public Drawable getTask_icon() {
        return task_icon;
    }
    public void setTask_icon(Drawable task_icon) {
        this.task_icon = task_icon;
    }
    public String getTask_name() {
        return task_name;
    }
    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }
    public long getTask_memory() {
        return task_memory;
    }
    public void setTask_memory(long task_memory) {
        this.task_memory = task_memory;
    }
    public String getPackageName() {
        return packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public int getPid() {
        return pid;
    }
    public void setPid(int pid) {
        this.pid = pid;
    }
    public boolean isUserTask() {
        return userTask;
    }
    public void setUserTask(boolean userTask) {
        this.userTask = userTask;
    }

    @Override
    public String toString() {
        return "TaskInfo [task_icon=" + task_icon + ", task_name=" + task_name
                + ", task_memory=" + task_memory + ", packageName="
                + packageName + ", pid=" + pid + "]";
    }
}
