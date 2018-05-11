package com.jmolsmobile.videocapture.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jmolsmobile.videocapture.R;
import com.jmolsmobile.videocapture.ui.bean.ImageInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoInfoGridAdapter extends BaseAdapter
{
    private static final int TYPE_IMPORT = 0;
    private static final int TYPE_IMAGE_NORMAL = 1;
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ImageInfo> mImages = new ArrayList<ImageInfo>();
    private int mItemSize;
    private GridView.LayoutParams mItemLayoutParams;

    public VideoInfoGridAdapter(Context context)
    {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItemLayoutParams = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 添加数据集
     *
     * @param images
     */
    public void addData(List<ImageInfo> images)
    {
        mImages.clear();
        if (images != null && images.size() > 0)
        {
            mImages.addAll(images);
        }
        notifyDataSetChanged();
    }

    public ArrayList<ImageInfo> getDatas()
    {
        return mImages;
    }

    /**
     * 添加数据集
     *
     * @param image
     */
    public void addData(ImageInfo image)
    {
        if (image == null)
        {
            return;
        }
        mImages.add(image);
//        if(mImages.size() == 4)
//        {
//            mImages.remove(mImages.size() - 1);
//        }
        notifyDataSetChanged();
    }

    /**
     * 添加数据集
     *
     * @param position
     */
    public void removeData(int position)
    {
        mImages.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 重置每个Column的Size
     *
     * @param columnWidth
     */
    public void setItemSize(int columnWidth)
    {

        if (mItemSize == columnWidth)
        {
            return;
        }

        mItemSize = columnWidth;

        mItemLayoutParams = new GridView.LayoutParams(mItemSize, mItemSize);

        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount()
    {
        return 3;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position == (getCount() - 1))
        {
            return TYPE_IMPORT;
        }
        return TYPE_IMAGE_NORMAL;
    }

    @Override
    public int getCount()
    {
        return mImages.size() + 1;
    }

    @Override
    public ImageInfo getItem(int position)
    {
        if (position == (getCount() - 1))
        {
            return null;
        }
        else
        {
            return mImages.get(position);
        }

    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        int type = getItemViewType(position);
        if (type == TYPE_IMPORT)
        {
            view = mInflater.inflate(R.layout.comm_list_item_add_image, parent, false);
            view.setTag(null);
        }
        else if (type == TYPE_IMAGE_NORMAL)
        {
            ViewHolde holde;
            if (view == null)
            {
                view = mInflater.inflate(R.layout.comm_item_import_image, parent, false);
                holde = new ViewHolde(view, position);
            }
            else
            {
                holde = (ViewHolde) view.getTag();
                if (holde == null)
                {
                    view = mInflater.inflate(R.layout.comm_item_import_image, parent, false);
                    holde = new ViewHolde(view, position);
                }
            }
            if (holde != null)
            {
                holde.bindData(getItem(position));
            }
        }
        /** Fixed View Size */
        GridView.LayoutParams lp = (GridView.LayoutParams) view.getLayoutParams();
        if (lp.height != mItemSize)
        {
            view.setLayoutParams(mItemLayoutParams);
        }

        return view;
    }

    class ViewHolde
    {
        ImageView image;
        ImageView delete;

        ViewHolde(View view, final int position)
        {
            image = (ImageView) view.findViewById(R.id.image);
            delete = (ImageView) view.findViewById(R.id.ivDeleteImage);
            delete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AlertDialog dialog = null;
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("确认删除该视频")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    File file = new File(getItem(position).getImagePath());
                                    removeData(position);
                                    // 删除文件
                                    if(file.exists())
                                    {
                                        file.delete();
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    dialog = builder.create();
                    dialog.show();

                }
            });
            view.setTag(this);
        }

        void bindData(final ImageInfo data)
        {
            if (data == null)
                return;

            if (mItemSize > 0)
            {
                if (data.getImageType() == 2)
                {
                    Glide.with(mContext).load(data.getImagePath())
                            .placeholder(R.drawable.default_error)
                            .error(R.drawable.default_error).centerCrop()
                            .into(image);
                }
                else if (data.getImageType() == 1)
                {
                    File imageFile = new File(data.getImagePath());
                    // 显示图片
                    Glide.with(mContext).load(imageFile)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.default_error)
                            .error(R.drawable.default_error).crossFade()
                            .centerCrop().into(image);
                }

            }
        }
    }
}
