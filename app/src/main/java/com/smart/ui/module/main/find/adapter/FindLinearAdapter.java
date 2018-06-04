package com.smart.ui.module.main.find.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.hint.listener.OnChooseListener;
import com.hint.utils.DialogUtils;
import com.hint.utils.ToastUtils;
import com.smart.R;
import com.smart.db.entity.NoteEntity;

import java.util.ArrayList;
import java.util.List;


public class FindLinearAdapter extends DelegateAdapter.Adapter<FindLinearAdapter.FindLinearHolder> {
    private static final int TYPE_IMPORT = 0;
    private static final int TYPE_IMAGE_NORMAL = 1;

    private Context context;
    private LayoutHelper layoutHelper;
    private List<NoteEntity> allData;

    public FindLinearAdapter(Context context, LayoutHelper layoutHelper, List<NoteEntity> data) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.allData = new ArrayList<>();
        this.allData.addAll(data);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public FindLinearHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_IMPORT){
            return new FindLinearHolder(LayoutInflater.from(context).inflate(R.layout.item_add_image, parent,false), viewType);
        }else{
            return new FindLinearHolder(LayoutInflater.from(context).inflate(R.layout.item_linear_new, parent, false), viewType);
        }
    }

    @Override
    public void onBindViewHolder(FindLinearHolder holder, final int position) {
        if(holder.viewType == TYPE_IMPORT){
            holder.ivItemContent.setText(allData.get(position).getNote_content());
            holder.ivTime.setText(allData.get(position).getNote_lasttime());
            holder.ivItemview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ToastUtils.showToast(context, "Add" + position);
                    return false;
                }
            });
        }else{
            holder.ivItemContent.setText(allData.get(position).getNote_content());
            holder.ivTime.setText(allData.get(position).getNote_lasttime());
            holder.ivItemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<String> allStrs = new ArrayList<>();
                    allStrs.add("删除");
                    allStrs.add("清空所有");
                    DialogUtils.showChooseDialog(context, allStrs, new OnChooseListener() {
                        @Override
                        public void onPositive(int pos) {
                            ToastUtils.showToast(context, "Click" + position);
                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == (getItemCount() - 1)) {
            return TYPE_IMPORT;
        }
        return TYPE_IMAGE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return allData.size()+1;
    }

    public static class FindLinearHolder extends RecyclerView.ViewHolder {
        TextView ivItemContent;
        TextView ivTime;
        LinearLayout ivItemview;

        FrameLayout ivAddView;
        int viewType;

        FindLinearHolder(View view, int viewtype) {
            super(view);
            this.viewType = viewtype;
            if(viewType == TYPE_IMPORT){
                ivAddView = view.findViewById(R.id.iv_addview);
            }else{
                ivItemContent = view.findViewById(R.id.iv_itemContent);
                ivTime = view.findViewById(R.id.iv_Time);
                ivItemview = view.findViewById(R.id.iv_itemview);
            }
        }
    }
}
