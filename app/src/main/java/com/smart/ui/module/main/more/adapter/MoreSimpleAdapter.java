package com.smart.ui.module.main.more.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.app.adapter.BaseRecyAdapter;
import com.base.app.listener.OnClickLongListener;
import com.smart.R;
import com.smart.db.entity.MoreEntity;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MoreSimpleAdapter extends BaseRecyAdapter<MoreSimpleAdapter.ViewHolder> {

    OnClickLongListener mOnClickListener;
    List<MoreEntity> data;

    public MoreSimpleAdapter(List<MoreEntity> data) {
        this.data = data;
    }

    public void setDatas(List<MoreEntity> datas) {
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
        MoreEntity userModel = data.get(position);
        holder.hmImage.setImageResource(userModel.getImage_id());
        holder.hmContent.setText(userModel.getTitle());

        holder.hmRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find, parent, false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public MoreEntity getItem(int pos) {
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

    public List<MoreEntity> getData() {
        return data;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.hm_image)
        ImageView hmImage;
        @BindView(R.id.hm_content)
        TextView hmContent;
        @BindView(R.id.hm_root)
        LinearLayout hmRoot;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
