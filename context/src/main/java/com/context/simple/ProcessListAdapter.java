package com.context.simple;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.context.R;
import com.context.bean.ProcessInfo;

import java.util.List;

class ProcessListAdapter extends BaseAdapter {
    private List<ProcessInfo> apps;
    private Context context;

    public ProcessListAdapter(Context context, List<ProcessInfo> app){
        this.context = context;
        this.apps = app;
    }

    @Override

    public int getCount() {
        return apps.size();
    }

    @Override
    public ProcessInfo getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.process_list_item, null);
            holder.tvLayout = (LinearLayout) convertView.findViewById(R.id.hm_root);

            holder.tvCount = (TextView) convertView.findViewById(R.id.hm_count);
            holder.tvPid = (TextView) convertView.findViewById(R.id.hm_pid);
            holder.tvUid = (TextView) convertView.findViewById(R.id.hm_uid);

            holder.tvProcesName = (TextView) convertView.findViewById(R.id.hm_processname);
            holder.tvPkgList = (TextView) convertView.findViewById(R.id.hm_pkglist);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ProcessInfo appInfo = getItem(position);
        holder.tvCount.setText(""+(position+1));
        holder.tvPid.setText(appInfo.pid);
        holder.tvUid.setText(appInfo.uid);

        holder.tvProcesName.setText(appInfo.processName);
        holder.tvPkgList.setText(appInfo.pkgList+"");
        return convertView;
    }

    class ViewHolder {
        LinearLayout tvLayout;

        TextView tvCount;
        TextView tvPid;
        TextView tvUid;

        TextView tvProcesName;
        TextView tvPkgList;
    }
}
