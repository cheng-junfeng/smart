package com.push.ui.presenter;

import com.base.utils.TimeUtil;
import com.push.db.entity.MessagesEntity;
import com.push.db.helper.MessageHelper;
import com.push.ui.bean.MessageListBean;
import com.push.ui.contract.MsgContract;


import java.util.ArrayList;
import java.util.List;


public class MsgPresenter implements MsgContract.Presenter {
    private final static String TAG = "DataPresenter";

    private MsgContract.View mView;

    private ArrayList<MessageListBean> allData;
    private MessageHelper helper;


    public MsgPresenter(MsgContract.View androidView) {
        this.mView = androidView;
        this.helper = MessageHelper.getInstance();
    }

    @Override
    public void initAdapterData(int pageIndex, int pageSize) {
        if (allData == null) {
            allData = new ArrayList<MessageListBean>();
        } else {
            allData.clear();
        }

        List<MessagesEntity> allMess = helper.queryListByUserId(0);//for user
        if (allMess == null) {
            return;
        }

        for (int i = 0; (i < allMess.size() && i < (pageIndex+pageSize)); i++) {
            MessagesEntity oneMess = allMess.get(i);
            String inTime = oneMess.getInTime();
            long current = System.currentTimeMillis();
            try {
                current = Long.parseLong(inTime);
            } catch (Exception e) {
            }

            MessageListBean temp = new MessageListBean();
            temp.mess_id = oneMess.getMess_id();
            temp.type = oneMess.getType();
            temp.title = oneMess.getTitle();
            temp.content = oneMess.getContent();
            temp.msgTypeName = oneMess.getTypeName();

            temp.inTime = TimeUtil.milliseconds2StringMsg(current);
            temp.read = (oneMess.getHadRead() == 1);
            allData.add(temp);
        }
        mView.setRecyclerData(allData);
    }

    @Override
    public void setRead(String mess_id) {
        MessagesEntity entity = helper.queryListByMessId(mess_id);
        entity.setHadRead(1);
        boolean setDb = helper.update(entity);
    }
}
