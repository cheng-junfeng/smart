package com.push.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hint.utils.DialogUtils;
import com.hint.utils.ToastUtils;
import com.base.utils.LogUtil;
import com.base.utils.ShareUtil;
import com.push.R;
import com.push.R2;
import com.push.app.activity.PushBaseCompatFragment;
import com.base.app.event.RxBusHelper;
import com.push.app.event.MsgEvent;
import com.push.app.event.MsgType;
import com.base.app.listener.OnClickLongListener;
import com.push.app.control.Push;
import com.push.config.PushSharePre;
import com.push.ui.adapter.MessageListAdapter;
import com.push.ui.bean.MessageListBean;
import com.push.ui.contract.MsgContract;
import com.push.ui.presenter.MsgPresenter;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import butterknife.BindView;


public class MsgFragment extends PushBaseCompatFragment implements MsgContract.View{

    private final static String TAG = "MsgFragment";
    @BindView(R2.id.recyclerView)
    PullLoadMoreRecyclerView recyclerView;
    @BindView(R2.id.tv_empty)
    TextView tvEmpty;
    @BindView(R2.id.view_empty)
    FrameLayout viewEmpty;
    @BindView(R2.id.iv_msg_error)
    RelativeLayout ivMsgError;

    private Context mContext;
    private MsgPresenter presenter;

    MessageListAdapter mAdapter;

    @Override
    protected int setContentView() {
        return R.layout.fragment_msg;
    }

    @Override
    protected int setErrorView() {
        return R.layout.base_empty;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = super.onCreateView(inflater, container, savedInstanceState);

        mContext = this.getActivity();
        presenter = new MsgPresenter(this);

        RxBusHelper.doOnMainThread(this, MsgEvent.class, new RxBusHelper.OnEventListener<MsgEvent>() {
            @Override
            public void onEvent(MsgEvent msgEvent) {
                if (msgEvent.getType() == MsgType.STATUS) {
                    refreshStatus();
                } else {
                    refresh();
                }
            }
        });

        initView();
        presenter.initAdapterData(pageIndex, pageSize);
        return containerView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Push.resume(getActivity());
            refreshStatus();
        }
    }

    private void initView() {
        recyclerView.setLinearLayout();
        recyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                LogUtil.d(TAG, "onRefresh");
                loadFresh();
                presenter.initAdapterData(pageIndex, pageSize);
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setPullLoadMoreCompleted();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
                LogUtil.d(TAG, "onLoadMore");
                loadMore();
                presenter.initAdapterData(pageIndex, pageSize);
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setPullLoadMoreCompleted();
                    }
                }, 2000);
            }
        });
        ivMsgError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtils.showProgressDialog(mContext, "正在刷新");
                Push.resume(mContext);
                Push.resumeMqtt(mContext);
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshStatus();
                        DialogUtils.dismissProgressDialog();
                    }
                }, 3000);
            }
        });
    }

    @Override
    public void setRecyclerData(List<MessageListBean> data) {
        if (data == null || data.size() <= 0) {
            recyclerView.setVisibility(View.GONE);
            viewEmpty.setVisibility(View.VISIBLE);
            return;
        } else {
            viewEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        if (mAdapter == null) {
            mAdapter = new MessageListAdapter(getActivity(), data);

            mAdapter.setOnListener(new OnClickLongListener() {
                @Override
                public void onItemClick(int position) {
                    MessageListBean bean = mAdapter.getItem(position);
                    presenter.setRead(bean.mess_id);
                    bean.read = true;
                    mAdapter.notifyDataSetChanged();
                    ToastUtils.showToast(mContext, "已读 Line"+(position+1)+":"+ShareUtil.getBoolean(PushSharePre.MQTT_IS_LOGIN, false));
                }

                @Override
                public void onItemLongClick(int position) {
                }
            });

            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setDatas(data);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void refresh() {
        presenter.initAdapterData(pageIndex, pageSize);
    }

    private void refreshStatus() {
        if (!Push.getStatus(mContext)) {
            ivMsgError.setVisibility(View.VISIBLE);
        } else {
            ivMsgError.setVisibility(View.GONE);
        }
    }
}
