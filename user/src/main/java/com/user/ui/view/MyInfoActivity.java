package com.user.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.utils.LogUtil;
import com.bumptech.glide.Glide;
import com.hint.listener.OnConfirmListener;
import com.hint.utils.DialogUtils;
import com.base.app.event.RxBusHelper;
import com.base.config.GlobalConfig;
import com.base.utils.ShareUtil;
import com.base.utils.ToolbarUtil;

import com.thirdlogin.simple.TestDefault;
import com.thirdlogin.utils.ShareUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.user.R;
import com.user.R2;
import com.user.app.acitvity.UserBaseCompatActivity;
import com.user.config.UserExtra;
import com.user.db.entity.UserEntity;
import com.user.db.helper.UserHelper;
import com.user.ui.event.MyEvent;
import com.user.ui.event.MyType;
import com.user.ui.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class MyInfoActivity extends UserBaseCompatActivity {
    private final static String TAG = "MyInfoActivity";

    @BindView(R2.id.iv_person_image)
    ImageView ivPersonImage;
    @BindView(R2.id.tv_user_name)
    TextView tvUserName;
    @BindView(R2.id.tv_user_email)
    TextView tvUserEmail;
    @BindView(R2.id.tv_comm_footer)
    TextView tvCommFooter;

    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.activity_me_info;
    }

    @Override
    protected int setFootView() {
        return R.layout.base_bar_footer;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        ToolbarUtil.setToolbarLeft(toolbar, "个人中心", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ToolbarUtil.setToolbarRight(toolbar, "分享", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtils.shareWeb(MyInfoActivity.this, TestDefault.url, TestDefault.title
                        , TestDefault.text, TestDefault.imageurl, com.thirdlogin.R.mipmap.ic_launcher, SHARE_MEDIA.SINA
                );
            }
        });
        RxBusHelper.doOnMainThread(this, MyEvent.class, new RxBusHelper.OnEventListener<MyEvent>() {
            @Override
            public void onEvent(MyEvent myEvent) {
                if (myEvent.getType() == MyType.UPDATE) {
                    updateStatus(myEvent);
                }
            }
        });
        initView();
    }

    private void initView() {
        String userName = ShareUtil.getString(GlobalConfig.MY_USERNAME, "");
        String email = "";
        UserEntity userEntity = UserHelper.getInstance().queryByUserName(userName);
        if (userEntity != null) {
            byte[] img = userEntity.getUser_img();
            if (img != null) {
                Glide.with(this).load(img).into(ivPersonImage);
            } else {
                String url = userEntity.getUser_url();
                LogUtil.d(TAG, "url:"+url);
                if(url != null && url.length() > 0){
                    Glide.with(this).load(url).into(ivPersonImage);
                }else{
                    ivPersonImage.setBackgroundResource(R.mipmap.avatar_default);
                }
            }
            email = userEntity.getUser_email();
        }
        tvUserName.setText(userName);
        tvUserEmail.setText(email);

        tvCommFooter.setText("退出登录");
    }

    private void updateStatus(MyEvent myEvent) {
        tvUserName.setText(myEvent.getUserName());
        tvUserEmail.setText(myEvent.getEmail());
    }

    @OnClick({R2.id.rl_user_name, R2.id.rl_user_email, R2.id.rl_change_pwd, R2.id.rl_qrcode, R2.id.tv_comm_footer})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        int viewId = view.getId();
        if(viewId == R.id.rl_user_name){
            bundle.putInt(UserExtra.MY_MODIFY_TYPE, 0);
            readGo(MyModifyActivity.class, bundle);
        }else if(viewId == R.id.rl_user_email){
            bundle.putInt(UserExtra.MY_MODIFY_TYPE, 1);
            readGo(MyModifyActivity.class, bundle);
        }else if(viewId == R.id.rl_change_pwd){
            readGo(MyModifyPswActivity.class);
        }else if(viewId == R.id.rl_qrcode){
            readGo(MyQrCodeActvity.class);
        }else if(viewId == R.id.tv_comm_footer){
            exitApp();
        }
    }

    private void exitApp() {
        DialogUtils.showConfirmDialog(this, "确定退出当前账号？", new OnConfirmListener() {
            @Override
            public void onClickPositive() {
                new LoginPresenter().logout(mContext);
                readGoFinishAnim(MyLoginActivity.class);
                finishAll();
            }

            @Override
            public void onClickNegative() {
            }
        });
    }
}
