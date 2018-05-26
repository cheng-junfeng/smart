package com.wu.safe.push.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.app.listener.OnClickLongListener;
import com.base.app.adapter.BaseRecyAdapter;
import com.wu.safe.push.R;
import com.wu.safe.push.R2;
import com.wu.safe.push.ui.bean.MessageListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MessageListAdapter extends BaseRecyAdapter<MessageListAdapter.ViewHolder> {

    private List<MessageListBean> data;
    private OnClickLongListener mOnClickListener;

    public MessageListAdapter(Context context, List<MessageListBean> data) {
        this.data = data;
    }

    public void setDatas(List<MessageListBean> datas) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        this.data = datas;
    }

    public boolean setRead(int position) {
        MessageListBean bean = getItem(position);
        if (bean.read) {
            return false;
        } else {
            bean.read = true;
            return true;
        }
    }

    public void setOnListener(OnClickLongListener mOnItemClickListener) {
        this.mOnClickListener = mOnItemClickListener;
    }

    @Override
    public void myBindViewHolder(final ViewHolder holder, final int position) {

        MessageListBean userModel = data.get(position);
        holder.ivItemTitle.setText(userModel.title);
        holder.ivItemContent.setText(userModel.content);
        holder.ivTime.setText(userModel.inTime);
        if (userModel.read) {
            holder.ivUnread.setText("已读");
            holder.ivUnread.setBackgroundResource(R.drawable.msg_read);
            ;
            holder.ivUnread.setTextColor(Color.parseColor("#666666"));
        } else {
            holder.ivUnread.setText("未读");
            holder.ivUnread.setBackgroundResource(R.drawable.msg_unread);
            holder.ivUnread.setTextColor(Color.parseColor("#FFFFFF"));
        }

        if(TextUtils.isEmpty(userModel.msgDescrip)){
            holder.ivType.setText("未知");
        }else{
            holder.ivType.setText(userModel.msgDescrip);
        }

        switch (userModel.type){
            case 0:{
                holder.ivType.setText("公告");
                holder.ivType.setBackgroundResource(R.drawable.type_com);
            }break;
            case 1:holder.ivType.setBackgroundResource(R.drawable.type_normal);break;
            case 2:holder.ivType.setBackgroundResource(R.drawable.type_error);break;
            case 3:
                holder.ivType.setText("短消息");
                holder.ivType.setBackgroundResource(R.drawable.type_other);break;
            default:break;
        }

        holder.ivItemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mOnClickListener.onItemLongClick(position);
                return false;
            }
        });
        holder.ivItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_new, parent, false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public MessageListBean getItem(int pos) {
        if (data == null || pos >= data.size()) {
            return null;
        }
        return data.get(pos);
    }

    public List<MessageListBean> getData() {
        return data;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.iv_itemTitle)
        TextView ivItemTitle;
        @BindView(R2.id.iv_itemContent)
        TextView ivItemContent;
        @BindView(R2.id.iv_Time)
        TextView ivTime;
        @BindView(R2.id.iv_itemview)
        View ivItemView;
        @BindView(R2.id.iv_unread)
        TextView ivUnread;
        @BindView(R2.id.iv_type)
        TextView ivType;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
