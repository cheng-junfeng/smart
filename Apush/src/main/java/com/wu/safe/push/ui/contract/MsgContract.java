package com.wu.safe.push.ui.contract;

import com.wu.safe.push.ui.bean.MessageListBean;

import java.util.List;

public interface MsgContract {

    interface View{
        void setRecyclerData(List<MessageListBean> data);
    }

    interface Presenter{
        void initAdapterData(int pageIndex, int pageSize);
        void setRead(String mess_id);
    }
}
