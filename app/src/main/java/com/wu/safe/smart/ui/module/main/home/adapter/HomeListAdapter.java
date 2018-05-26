package com.wu.safe.smart.ui.module.main.home.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wu.safe.smart.R;
import com.base.app.adapter.BaseRecyAdapter;
import com.base.app.listener.OnClickLongListener;
import com.wu.safe.smart.ui.module.main.home.bean.HomeListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeListAdapter extends BaseRecyAdapter<HomeListAdapter.ViewHolder> {

    OnClickLongListener mOnClickListener;
    List<HomeListBean> data;

    public HomeListAdapter(List<HomeListBean> data) {
        this.data = data;
    }

    public void setDatas(List<HomeListBean> datas) {
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

        HomeListBean userModel = data.get(position);
        holder.hmContent.setText(userModel.getContent());

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
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public HomeListBean getItem(int pos) {
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

    public List<HomeListBean> getData() {
        return data;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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
