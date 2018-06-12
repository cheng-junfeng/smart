package com.file.simple;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.utils.FileUtil;
import com.file.R;
import com.file.bean.FileInfo;

import java.util.List;

class FileListAdapter extends BaseAdapter {
    private List<FileInfo> apps;
    private Context context;

    public FileListAdapter(Context context, List<FileInfo> app){
        this.context = context;
        this.apps = app;
    }

    @Override

    public int getCount() {
        return apps.size();
    }

    @Override
    public FileInfo getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.file_list_item, null);
            holder.tvLayout = (LinearLayout) convertView.findViewById(R.id.hm_root);

            holder.tvType = (TextView) convertView.findViewById(R.id.hm_type);
            holder.tvName = (TextView) convertView.findViewById(R.id.hm_name);

            holder.tvPermission = (TextView) convertView.findViewById(R.id.hm_permission);
            holder.tvPath = (TextView) convertView.findViewById(R.id.hm_path);
            holder.tvExtra = (TextView) convertView.findViewById(R.id.hm_extra);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final FileInfo appInfo = getItem(position);
        String type = appInfo.isDir ? "Dir": "File";
        Resources res = context.getResources();
        if(appInfo.isDir){
            holder.tvLayout.setBackgroundColor(res.getColor(R.color.color_alpha_blue));
            holder.tvLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FileSecondActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("PATH", appInfo.appPath);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }else{
            holder.tvLayout.setBackgroundColor(res.getColor(R.color.color_light_white));
            holder.tvLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FileThirdActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("PATH", appInfo.appPath);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }

        if(appInfo.isHidden){
            holder.tvLayout.setBackgroundColor(res.getColor(R.color.color_red));
        }
        holder.tvType.setText(type);
        holder.tvName.setText(appInfo.appName);

        holder.tvPermission.setText(appInfo.permission);
        holder.tvPath.setText(appInfo.appPath);
        holder.tvExtra.setText(FileUtil.formetFileSize(appInfo.size));
        return convertView;
    }

    class ViewHolder {
        LinearLayout tvLayout;

        TextView tvType;
        TextView tvName;

        TextView tvPermission;
        TextView tvPath;
        TextView tvExtra;
    }
}
