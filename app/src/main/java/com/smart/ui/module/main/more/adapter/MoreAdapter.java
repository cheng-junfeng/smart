package com.smart.ui.module.main.more.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.smart.R;
import com.smart.db.entity.MoreEntity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MoreAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private LinkedList<MoreEntity> mData = new LinkedList<>();
    private OnChangeListener onClickListener;

    public MoreAdapter(Context context, List<MoreEntity> images, OnChangeListener listener) {
        this.mData.addAll(images);
        this.onClickListener = listener;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public LinkedList<MoreEntity> getData() {
        return mData;
    }

    public void addItem(MoreEntity bean) {
        mData.add(bean);
        notifyDataSetChanged();
    }

    public void onChange(int from, int to) {
        MoreEntity temp = mData.get(from);
        if (from < to) {
            for (int i = from; i < to; i++) {
                Collections.swap(mData, i, i + 1);
            }
        } else if (from > to) {
            for (int i = from; i > to; i--) {
                Collections.swap(mData, i, i - 1);
            }
        }
        mData.set(to, temp);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public MoreEntity getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolde holde;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_more_import, parent, false);
            holde = new ViewHolde(view, position);
        } else {
            holde = (ViewHolde) view.getTag();
            if (holde == null) {
                view = mInflater.inflate(R.layout.item_more_import, parent, false);
                holde = new ViewHolde(view, position);
            }
        }
        if (holde != null) {
            holde.bindData(getItem(position));
        }
        return view;
    }

    class ViewHolde {
        LinearLayout hmRoot;
        ImageView hmImage;
        TextView hmContent;
        ImageView deleteImage;

        ViewHolde(View view, final int position) {
            hmRoot = view.findViewById(R.id.hm_root);
            hmImage = view.findViewById(R.id.hm_image);
            hmContent = view.findViewById(R.id.hm_content);
            deleteImage = view.findViewById(R.id.ivDeleteImage);
            MoreEntity bean = mData.get(position);
            if (bean.bottom) {
                deleteImage.setImageResource(R.mipmap.ic_add);
            } else {
                deleteImage.setImageResource(R.mipmap.ic_delete);
            }

            hmRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MoreEntity bean = mData.get(position);
                    bean.bottom = !bean.bottom;
                    if (onClickListener != null) {
                        onClickListener.onChange(bean);
                    }
                    mData.remove(position);
                    notifyDataSetChanged();
                }
            });
            view.setTag(this);
        }

        void bindData(final MoreEntity data) {
            if (data == null) {
                return;
            }
            hmImage.setImageResource(data.image_id);
            hmContent.setText(data.title);
        }
    }

    public interface OnChangeListener {
        void onChange(MoreEntity bean);
    }
}

