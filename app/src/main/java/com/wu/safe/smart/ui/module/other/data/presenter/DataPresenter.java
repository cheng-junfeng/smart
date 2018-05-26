package com.wu.safe.smart.ui.module.other.data.presenter;

import com.blankj.utilcode.util.LogUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.base.net.helper.ApiExceptionHelper;
import com.base.utils.LogUtil;
import com.wu.safe.smart.db.entity.DataEntity;
import com.wu.safe.smart.db.helper.DataHelper;
import com.wu.safe.smart.ui.module.other.data.bean.DataListBean;
import com.wu.safe.smart.ui.module.other.data.contract.DataContract;
import com.wu.safe.smart.net.bean.AreaInput;
import com.wu.safe.smart.net.bean.AreaOutput;
import com.base.net.control.HttpResult;
import com.base.net.helper.HttpHelper;
import com.wu.safe.smart.net.control.RetrofitHelper;
import com.wu.safe.smart.net.service.AreaService;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class DataPresenter implements DataContract.Presenter {
    private final static String TAG = "DataPresenter";

    private DataContract.View mView;
    private RxAppCompatActivity activity;

    private LinkedList<DataListBean> allData;
    private DataHelper helper;


    public DataPresenter(DataContract.View androidView) {
        this.mView = androidView;
        this.activity = androidView.getRxActivity();
        this.helper = DataHelper.getInstance();
    }

    @Override
    public void initNetworkData() {
        AreaInput input = new AreaInput();
        Observable<HttpResult<AreaOutput>> deviceObservable = RetrofitHelper.tokenCreate(AreaService.class).queryArea(input);
        HttpHelper.getObservable(deviceObservable, activity, ActivityEvent.DESTROY)
                .subscribe(new Consumer<HttpResult<AreaOutput>>() {
                    @Override
                    public void accept(HttpResult<AreaOutput> value) {
                        LogUtil.d(TAG, "onSucceed:");
                        mView.showStatus("onSucceed");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(TAG, "onFailed:" + throwable.getMessage());
                        mView.showStatus(ApiExceptionHelper.getMessage(throwable));
                    }
                });
    }

    @Override
    public void initAdapterData(int pageIndex, int pageSize) {
        if(allData == null){
            allData = new LinkedList<DataListBean>();
        }else{
            allData.clear();
        }

        List<DataEntity> allMess = helper.queryList();//for user
        if (allMess == null) {
            return;
        }

        for (int i = 0; (i < allMess.size() && i < (pageIndex+pageSize)); i++) {
            DataEntity oneMess = allMess.get(i);

            DataListBean temp = new DataListBean.Builder(oneMess.getData_id())
                    .content(oneMess.getData_name()).build();
            allData.add(temp);
        }
        mView.setRecyclerData(allData);
    }

    @Override
    public void deleteBeans(List<DataListBean> allBean) {
        for (DataListBean bean : allBean) {
            DataEntity entity = helper.queryListByVideoId(bean.getId());
            helper.delete(entity);
        }
    }
}
