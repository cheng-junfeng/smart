package com.smart.ui.module.main.more;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.base.app.event.RxBusHelper;
import com.base.utils.ToolbarUtil;
import com.custom.widget.DragGridView;
import com.custom.widget.ScrollGridView;
import com.smart.R;
import com.smart.app.activity.BaseCompatActivity;
import com.smart.app.event.NoteEvent;
import com.smart.app.event.NoteType;
import com.smart.db.entity.MoreEntity;
import com.smart.db.helper.MoreHelper;
import com.smart.ui.module.main.more.adapter.MoreAdapter;


import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

public class MoreActivity extends BaseCompatActivity {

    public static final String TAG = "MoreActivity";
    @BindView(R.id.top_view)
    DragGridView topView;
    @BindView(R.id.bottom_view)
    ScrollGridView bottomView;

    private MoreAdapter mTopAdapter;
    private MoreAdapter mBottomAdapter;

    private List<MoreEntity> topList = new LinkedList<MoreEntity>();
    private List<MoreEntity> bottomList = new LinkedList<MoreEntity>();

    private boolean isEdit = false;

    @Override
    protected int setContentView() {
        return R.layout.activity_more;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "更多", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ToolbarUtil.setToolbarRight(toolbar, "完成", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdit) {
                    MoreHelper helper = MoreHelper.getInstance();
                    List<MoreEntity> allTop = mTopAdapter.getData();
                    List<MoreEntity> allBottom = mBottomAdapter.getData();

                    int topSize = allTop.size();
                    for (int i = 0; i < allTop.size(); i++) {
                        MoreEntity tempEntity = allTop.get(i);
                        tempEntity.index = (i + 1);
                        helper.update(tempEntity);
                    }

                    for (int i = 0; i < allBottom.size(); i++) {
                        MoreEntity tempEntity = allBottom.get(i);
                        tempEntity.index = (i + 1 + topSize);
                        helper.update(tempEntity);
                    }
                    RxBusHelper.post(new NoteEvent.Builder(NoteType.MORE).build());
                }
                onBackPressed();
            }
        });
        initData();
        initTopView();
        initBottomView();
    }

    private void initData() {
        MoreHelper helper = MoreHelper.getInstance();
        List<MoreEntity> allEntity = helper.queryListBottom(false);//only for top
        for (int i = 1; i < allEntity.size(); i++) {
            MoreEntity entity = allEntity.get(i);
            topList.add(entity);
        }

        List<MoreEntity> allBottomEntity = helper.queryListBottom(true);//only for bottom
        for (int i = 0; i < allBottomEntity.size(); i++) {
            MoreEntity entity = allBottomEntity.get(i);
            bottomList.add(entity);
        }
    }

    private void initTopView() {
        mTopAdapter = new MoreAdapter(this, topList, new MoreAdapter.OnChangeListener() {
            @Override
            public void onChange(MoreEntity bean) {
                isEdit = true;
                mBottomAdapter.addItem(bean);
            }
        });
        topView.setAdapter(mTopAdapter);
        topView.setOnChangeListener(new DragGridView.OnChangeListener() {

            @Override
            public void onChange(int from, int to) {
                isEdit = true;
                mTopAdapter.onChange(from, to);
            }
        });
    }

    private void initBottomView() {
        mBottomAdapter = new MoreAdapter(this, bottomList, new MoreAdapter.OnChangeListener() {
            @Override
            public void onChange(MoreEntity bean) {
                isEdit = true;
                mTopAdapter.addItem(bean);
            }
        });
        bottomView.setAdapter(mBottomAdapter);
    }
}
