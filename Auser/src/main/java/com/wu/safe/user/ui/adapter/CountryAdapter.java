package com.wu.safe.user.ui.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hintlib.listener.OnChooseListener;
import com.wu.safe.user.R;
import com.wu.safe.user.ui.bean.CountryBean;

import java.util.List;


public class CountryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CountryBean> data;
    private OnChooseListener mOnClickListener;

    public CountryAdapter(List<CountryBean> data) {
        this.data = data;
    }

    public void setOnItemClickListener(OnChooseListener mOnItemClickListener) {
        this.mOnClickListener = mOnItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    public List<CountryBean> getData() {
        return data;
    }

    public void setData(List<CountryBean> datas){
        this.data = datas;
    }

    public CountryBean getItem(int position) {
        return data.get(position);
    }

    public int getLetterPosition(String letter) {
        if (data != null) {
            CountryBean bean;
            for (int i = 0; i < data.size(); i++) {
                bean = data.get(i);
                if (bean.type == 1 && bean.letter.equals(letter)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 1) {
            return new PinnedHolder(inflater.inflate(R.layout.item_country_pinned, parent, false));
        } else {
            return new CountryHolder(inflater.inflate(R.layout.item_country, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        CountryBean bean = data.get(position);
        if (holder instanceof CountryHolder) {
            ((CountryHolder) holder).city_name.setText(bean.name);
        } else {
            ((PinnedHolder) holder).city_tip.setText(bean.name);
        }

        // 如果设置了回调，则设置点击事件
        if (mOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnClickListener.onPositiveSelect(pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CountryHolder extends RecyclerView.ViewHolder {
        TextView city_name;

        public CountryHolder(View view) {
            super(view);
            city_name = (TextView) view.findViewById(R.id.city_name);
        }
    }

    class PinnedHolder extends RecyclerView.ViewHolder {
        TextView city_tip;

        public PinnedHolder(View view) {
            super(view);
            city_tip = (TextView) view.findViewById(R.id.city_tip);
        }
    }
}
