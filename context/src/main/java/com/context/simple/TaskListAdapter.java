package com.context.simple;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.context.R;
import com.context.bean.TaskInfo;
import com.context.utils.FileUtil;

import java.util.List;

class TaskListAdapter extends BaseAdapter {
    private List<TaskInfo> apps;
    private Context context;

    public TaskListAdapter(Context context, List<TaskInfo> app){
        this.context = context;
        this.apps = app;
    }

    @Override

    public int getCount() {
        return apps.size();
    }

    @Override
    public TaskInfo getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.task_list_item, null);
            holder.tvLayout = (LinearLayout) convertView.findViewById(R.id.hm_root);

            holder.tvCount = (TextView) convertView.findViewById(R.id.hm_count);
            holder.tvIcon = (ImageView) convertView.findViewById(R.id.hm_icon);
            holder.tvName = (TextView) convertView.findViewById(R.id.hm_name);

            holder.tvPackageName = (TextView) convertView.findViewById(R.id.hm_packagename);
            holder.tvCode = (TextView) convertView.findViewById(R.id.hm_vercode);
            holder.tvCodeName = (TextView) convertView.findViewById(R.id.hm_vername);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TaskInfo appInfo = getItem(position);
        holder.tvCount.setText(""+(position+1));
        holder.tvIcon.setImageDrawable(appInfo.getTask_icon());
        holder.tvName.setText(appInfo.getTask_name());

        holder.tvPackageName.setText(appInfo.getPackageName());
        holder.tvCode.setText(appInfo.getPid()+"");
        holder.tvCodeName.setText(FileUtil.formetFileSize(appInfo.getTask_memory()));
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
    }
}
