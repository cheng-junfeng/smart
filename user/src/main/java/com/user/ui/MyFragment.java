package com.user.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.base.app.event.RxBusHelper;
import com.base.config.GlobalConfig;
import com.base.utils.ShareUtil;
import com.user.R;
import com.user.R2;
import com.user.app.acitvity.UserBaseCompatFragment;
import com.user.db.entity.UserEntity;
import com.user.db.helper.UserHelper;
import com.user.ui.event.MyEvent;
import com.user.ui.event.MyType;
import com.user.ui.view.MyAboutActivity;
import com.user.ui.view.MyFeedBackActivity;
import com.user.ui.view.MyInfoActivity;
import com.user.ui.view.MySettingActivity;

import butterknife.BindView;
import butterknife.OnClick;


public class MyFragment extends UserBaseCompatFragment {
    @BindView(R2.id.tv_person_name)
    TextView tvPersonName;
    @BindView(R2.id.iv_person_image)
    ImageView ivPersonImage;

    @Override
    protected int setContentView() {
        return R.layout.fragment_me;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = super.onCreateView(inflater, container, savedInstanceState);
        RxBusHelper.doOnMainThread(this, MyEvent.class, new RxBusHelper.OnEventListener<MyEvent>() {
            @Override
            public void onEvent(MyEvent myEvent) {
                if (myEvent.getType() == MyType.UPDATE) {
                    updateStatus(myEvent);
                }
            }
        });
        initView();
        return containerView;
    }

    private void updateStatus(MyEvent myEvent) {
        tvPersonName.setText(myEvent.getUserName());
    }

    public void initView() {
        String userName = ShareUtil.getString(GlobalConfig.MY_USERNAME, "");
        UserEntity userEntity = UserHelper.getInstance().queryByUserName(userName);
        if (userEntity != null) {
            byte[] img = userEntity.getUser_img();
            if (img != null) {
                Glide.with(this).load(img).into(ivPersonImage);
            } else {
                ivPersonImage.setBackgroundResource(R.mipmap.avatar_default);
            }
        }
        tvPersonName.setText(userName);
    }

    @OnClick({R2.id.ll_person, R2.id.rl_setting, R2.id.rl_feedback, R2.id.ml_aboutUs})
    public void onViewClicked(View view) {
        int viewId = view.getId();
        if (viewId == R.id.ll_person) {
            readGo(MyInfoActivity.class);
        } else if (viewId == R.id.rl_setting) {
            readGo(MySettingActivity.class);
        } else if (viewId == R.id.rl_feedback) {
            readGo(MyFeedBackActivity.class);
        } else if (viewId == R.id.ml_aboutUs) {
            readGo(MyAboutActivity.class);
        }
    }
}
