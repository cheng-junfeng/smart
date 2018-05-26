package com.base.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseRecyAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemLongClickLitener {
        void onItemLongClick(View view, int position);
    }

    private OnItemLongClickLitener onItemLongClickLitener;

    public void setOnItemLongClickLitener(OnItemLongClickLitener onItemLongClickLitener) {
        this.onItemLongClickLitener = onItemLongClickLitener;
    }

    @Override
    public final void onBindViewHolder(final VH holder, int position) {
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
        }
        if (onItemLongClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemLongClickLitener.onItemLongClick(holder.itemView, pos);
                }
            });
        }
        myBindViewHolder(holder, position);
    }

    public abstract void myBindViewHolder(final VH holder, int position);
}
