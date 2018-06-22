package com.smart.ui.module.main.more;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.base.app.event.RxBusHelper;
import com.base.app.listener.OnClickLongListener;
import com.base.utils.ToolbarUtil;
import com.hint.utils.ToastUtils;
import com.smart.R;
import com.smart.app.activity.BaseCompatActivity;
import com.smart.app.event.NoteEvent;
import com.smart.app.event.NoteType;
import com.smart.db.entity.MoreEntity;
import com.smart.db.helper.MoreHelper;
import com.smart.ui.module.main.more.adapter.MoreSimpleAdapter;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MoreSimpleActivity extends BaseCompatActivity {

    public static final String TAG = "MoreSimpleActivity";
    @BindView(R.id.rv_function)
    RecyclerView rvFunction;

    List<MoreEntity> allData;
    MoreSimpleAdapter mAdapter;

    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.activity_more_simple;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ToolbarUtil.setToolbarLeft(toolbar, "More", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        RxBusHelper.doOnMainThread(this, NoteEvent.class, new RxBusHelper.OnEventListener<NoteEvent>() {
            @Override
            public void onEvent(NoteEvent noteEvent) {
                if (noteEvent.getType() == NoteType.MORE) {
                    initData();
                    initView();
                }
            }
        });
        initData();
        initView();
    }

    private void initData() {
        allData = new ArrayList<>();
        MoreHelper helper = MoreHelper.getInstance();
        List<MoreEntity> allEntity = helper.queryListBottom(false);//only for top
        if(allEntity == null || allEntity.size() == 0){
            return;
        }
        for (int i = 1; i < allEntity.size(); i++) {
            MoreEntity entity = allEntity.get(i);
            allData.add(entity);
        }
        allData.add(allEntity.get(0));//add 0 last
    }

    private void initView() {
        if (allData == null || allData.size() <= 0) {
            return;
        }

        if (mAdapter == null) {
            mAdapter = new MoreSimpleAdapter(allData);
            mAdapter.setOnListener(new OnClickLongListener() {
                @Override
                public void onItemClick(int position) {
                    if ((position + 1) == mAdapter.getItemCount()) {//ic_category_more
                        readGo(MoreActivity.class);
                    } else {
                        final String[] proName = mContext.getResources().getStringArray(R.array.more_title);
                        MoreEntity entity = mAdapter.getItem(position);
                        ToastUtils.showToast(mContext, proName[entity.entry_type]);
                    }
                }

                @Override
                public void onItemLongClick(int position) {
                }
            });

            rvFunction.setLayoutManager(new GridLayoutManager(mContext, 4));
            rvFunction.setHasFixedSize(true);
            rvFunction.setAdapter(mAdapter);
        } else {
            mAdapter.setDatas(allData);
            mAdapter.notifyDataSetChanged();
        }
    }
}
