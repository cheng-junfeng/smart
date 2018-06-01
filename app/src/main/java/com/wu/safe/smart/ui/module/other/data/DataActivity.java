package com.wu.safe.smart.ui.module.other.data;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;


import com.hint.utils.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.base.utils.LogUtil;
import com.base.utils.ToolbarUtil;
import com.wu.safe.smart.R;
import com.wu.safe.smart.app.activity.BaseCompatActivity;
import com.base.app.listener.OnClickLongListener;
import com.wu.safe.smart.ui.module.other.data.adapter.DataListAdapter;
import com.wu.safe.smart.ui.module.other.data.bean.DataListBean;
import com.wu.safe.smart.ui.module.other.data.contract.DataContract;
import com.wu.safe.smart.ui.module.other.data.presenter.DataPresenter;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DataActivity extends BaseCompatActivity implements DataContract.View {

    public static final String TAG = "DataActivity";
    @BindView(R.id.main_view)
    PullLoadMoreRecyclerView mainView;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;

    private Context mContext;
    DataListAdapter mAdapter;

    private DataContract.Presenter presenter;

    @Override
    protected int setContentView() {
        return R.layout.activity_data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ToolbarUtil.setToolbarLeft(toolbar, "分页", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        presenter = new DataPresenter(this);
        presenter.initNetworkData();
        presenter.initAdapterData(pageIndex, pageSize);
    }

    private void refresh() {
        presenter.initAdapterData(pageIndex, pageSize);
    }

    @Override
    public void setRecyclerData(List<DataListBean> data) {
        if (data == null || data.size() <= 0) {
            mainView.setVisibility(View.GONE);
            return;
        } else {
            mainView.setVisibility(View.VISIBLE);
        }
        if (mAdapter == null) {
            mAdapter = new DataListAdapter(mContext, data);

            mAdapter.setOnListener(new OnClickLongListener() {
                @Override
                public void onItemClick(int position) {
                    showStatus("click:" + position);
                }

                @Override
                public void onItemLongClick(int position) {
                    llBottom.setVisibility(View.VISIBLE);
                    mAdapter.setEditable(true);
                }
            });

            mainView.setLinearLayout();
            mainView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
                @Override
                public void onRefresh() {
                    LogUtil.d(TAG, "onRefresh");
                    loadFresh();
                    presenter.initAdapterData(pageIndex, pageSize);
                    mainView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mainView.setPullLoadMoreCompleted();
                        }
                    }, 2000);
                }

                @Override
                public void onLoadMore() {
                    LogUtil.d(TAG, "onLoadMore"+pageIndex);
                    loadMore();
                    presenter.initAdapterData(pageIndex, pageSize);
                    mainView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mainView.setPullLoadMoreCompleted();
                        }
                    }, 2000);
                }
            });
            mainView.setAdapter(mAdapter);
        } else {
            mAdapter.setDatas(data);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showStatus(String toastStr) {
        ToastUtils.showToast(mContext, toastStr);
    }

    @Override
    public RxAppCompatActivity getRxActivity() {
        return this;
    }

    @OnClick({R.id.btn_cancel, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                llBottom.setVisibility(View.GONE);
                mAdapter.setEditable(false);
                break;
            case R.id.btn_commit:
                llBottom.setVisibility(View.GONE);
                List<DataListBean> allBean = mAdapter.getRemoveData();
                presenter.deleteBeans(allBean);

                mAdapter.setEditable(false);
                refresh();
                break;
        }
    }
}
