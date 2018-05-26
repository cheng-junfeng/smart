package com.base.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.R;
import com.base.app.listener.OnClickLongListener;

import java.util.ArrayList;
import java.util.List;


public class BaseListAdapter extends BaseRecyAdapter<BaseListAdapter.ViewHolder> {

    OnClickLongListener mOnClickListener;
    List<String> data;

    public BaseListAdapter(List<String> data) {
        this.data = data;
    }

    public void setDatas(List<String> datas) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        this.data = datas;
    }

    public void setOnListener(OnClickLongListener mOnItemClickListener) {
        this.mOnClickListener = mOnItemClickListener;
    }

    @Override
    public void myBindViewHolder(final ViewHolder holder, final int position) {

        String userModel = data.get(position);
        holder.hmContent.setText(userModel);

        holder.hmRoot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mOnClickListener.onItemLongClick(position);
                return false;
            }
        });
        holder.hmRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.base_list_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public String getItem(int pos) {
        if (data == null || pos >= data.size()) {
            return null;
        }
        return data.get(pos);
    }

    public void removeData(int adapterPosition) {
        data.remove(adapterPosition);
    }

    public void clear() {
        data.clear();
    }

    public List<String> getData() {
        return data;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView hmContent;
        LinearLayout hmRoot;

        ViewHolder(View view) {
            super(view);
            hmContent = view.findViewById(R.id.hm_content);
            hmRoot = view.findViewById(R.id.hm_root);
        }
    }
}
