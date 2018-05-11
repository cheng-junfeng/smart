package com.wu.safe.smart.ui.module.other.data.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wu.safe.smart.R;
import com.wu.safe.base.ui.adapter.BaseRecyAdapter;
import com.wu.safe.base.app.listener.OnClickLongListener;
import com.wu.safe.smart.ui.module.other.data.bean.DataListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataListAdapter extends BaseRecyAdapter<DataListAdapter.ViewHolder> {

    OnClickLongListener mOnClickListener;
    List<DataListBean> data;
    List<DataListBean> removeData;
    boolean isEditable = false;

    Context mContext;

    public DataListAdapter(Context context, List<DataListBean> data) {
        this.mContext = context;
        this.data = data;
    }

    public List<DataListBean> getRemoveData() {
        return removeData;
    }

    public void setEditable(boolean isEdit) {
        if (isEdit) {
            removeData = new ArrayList<>();
        } else {
            if (removeData != null) {
                removeData.clear();
            }
        }
        isEditable = isEdit;
        notifyDataSetChanged();
    }

    public void setDatas(List<DataListBean> datas) {
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
        DataListBean userModel = data.get(position);
        holder.hmContent.setText(userModel.getContent());
        if (isEditable) {
            holder.hmCheckbox.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
            holder.hmCheckbox.setVisibility(View.VISIBLE);
        } else {
            holder.hmCheckbox.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out));
            holder.hmCheckbox.setVisibility(View.GONE);
        }

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
                if (isEditable) {
                    boolean isCheck = !holder.hmCheckbox.isChecked();
                    holder.hmCheckbox.setChecked(isCheck);
                } else {
                    mOnClickListener.onItemClick(position);
                }
            }
        });
        holder.hmCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                if (isCheck) {
                    removeData.add(data.get(position));
                } else {
                    removeData.remove(data.get(position));
                }
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.data_list_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public DataListBean getItem(int pos) {
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

    public List<DataListBean> getData() {
        return data;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.hm_content)
        TextView hmContent;
        @BindView(R.id.hm_time)
        TextView hmTime;
        @BindView(R.id.hm_checkbox)
        CheckBox hmCheckbox;
        @BindView(R.id.hm_root)
        RelativeLayout hmRoot;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
