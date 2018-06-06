package com.context.simple;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.context.R;
import com.context.bean.AppInfo;
import com.context.utils.TimeUtil;

import java.util.List;

class AppListAdapter extends BaseAdapter {
    private List<AppInfo> apps;
    private Context context;

    public AppListAdapter(Context context, List<AppInfo> app){
        this.context = context;
        this.apps = app;
    }

    @Override

    public int getCount() {
        return apps.size();
    }

    @Override
    public AppInfo getItem(int position) {
        return apps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.app_list_item, null);
            holder.tvLayout = (LinearLayout) convertView.findViewById(R.id.hm_root);

            holder.tvCount = (TextView) convertView.findViewById(R.id.hm_count);
            holder.tvIcon = (ImageView) convertView.findViewById(R.id.hm_icon);
            holder.tvName = (TextView) convertView.findViewById(R.id.hm_name);

            holder.tvPackageName = (TextView) convertView.findViewById(R.id.hm_packagename);
            holder.tvCode = (TextView) convertView.findViewById(R.id.hm_vercode);
            holder.tvCodeName = (TextView) convertView.findViewById(R.id.hm_vername);

            holder.tvPath = (TextView) convertView.findViewById(R.id.hm_path);
            holder.tvTime = (TextView) convertView.findViewById(R.id.hm_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AppInfo appInfo = getItem(position);
        PackageInfo packageInfo = appInfo.packageInfo;
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
        if((applicationInfo.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0){
            holder.tvLayout.setBackgroundColor(Color.RED);
        }else{
            holder.tvLayout.setBackgroundColor(Color.WHITE);
        }
        holder.tvCount.setText(""+(position+1));
        holder.tvIcon.setImageBitmap(appInfo.appIcon);
        holder.tvName.setText(appInfo.appName);

        holder.tvPackageName.setText(appInfo.appPackageName);
        holder.tvCode.setText(packageInfo.versionCode+"");
        holder.tvCodeName.setText(packageInfo.versionName);
        holder.tvPath.setText(appInfo.appPath);
        holder.tvTime.setText(TimeUtil.milliseconds2String(packageInfo.lastUpdateTime));
        return convertView;
    }

    class ViewHolder {
        LinearLayout tvLayout;

        TextView tvCount;
        ImageView tvIcon;
        TextView tvName;

        TextView tvPackageName;
        TextView tvCode;
        TextView tvCodeName;

        TextView tvPath;
        TextView tvTime;
    }
}
