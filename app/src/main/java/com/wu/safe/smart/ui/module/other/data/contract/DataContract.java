package com.wu.safe.smart.ui.module.other.data.contract;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.wu.safe.smart.ui.module.other.data.bean.DataListBean;

import java.util.List;

public interface DataContract {

    interface View {
        void setRecyclerData(List<DataListBean> data);
        void showStatus(String statusStr);
        RxAppCompatActivity getRxActivity();
    }

    interface Presenter {
        void initAdapterData(int pageIndex, int pageSize);
        void initNetworkData();
        void deleteBeans(List<DataListBean> allBean);
    }
}
