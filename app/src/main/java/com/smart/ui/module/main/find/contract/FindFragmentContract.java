package com.smart.ui.module.main.find.contract;


import android.support.v7.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.smart.app.adapter.BaseDelegateAdapter;

public interface FindFragmentContract {

    interface View {
//        void setOnclick(int position);
//        void setGridClick(int position);
    }

    interface Presenter {
        //初始化
        DelegateAdapter initRecyclerView(RecyclerView recyclerView);
        BaseDelegateAdapter initMenu1();
        BaseDelegateAdapter initMenu2();
        BaseDelegateAdapter initMenu3();
        DelegateAdapter.Adapter initMenu4();
    }
}
