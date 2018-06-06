package com.user.ui.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import com.base.utils.LogUtil;
import com.bumptech.glide.Glide;
import com.custom.widget.CommEditText;
import com.hint.listener.OnInputListener;
import com.hint.utils.DialogUtils;
import com.hint.utils.ToastUtils;
import com.base.utils.NotNull;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.base.config.GlobalConfig;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.user.BuildConfig;
import com.user.config.NetConfig;
import com.base.utils.IPUtil;
import com.base.utils.ShareUtil;
import com.base.utils.ToolbarUtil;
import com.user.R;
import com.user.R2;
import com.user.app.acitvity.UserBaseCompatActivity;
import com.user.db.entity.UserEntity;
import com.user.db.helper.UserHelper;
import com.user.ui.adapter.MyLoginPopupAdapter;
import com.user.ui.contract.LoginContract;
import com.user.ui.presenter.LoginPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MyLoginActivity extends UserBaseCompatActivity implements LoginContract.View {

    private final static String TAG = "MyLoginActivity";

    @BindView(R2.id.et_phone)
    CommEditText etPhone;
    @BindView(R2.id.et_psw)
    CommEditText etPsw;
    @BindView(R2.id.iv_header)
    ImageView ivHeader;
    @BindView(R2.id.bt_login)
    Button btLogin;

    private Context mContext;
    private ListPopupWindow listPopup;
    private MyLoginPopupAdapter adapter;
    private List<String> accountList;
    private LoginPresenter presenter;

    @Override
    protected int setContentView() {
        return R.layout.activity_me_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        presenter = new LoginPresenter(this);

        ToolbarUtil.setToolbarLeft(toolbar, "登录", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ToolbarUtil.setToolbarRight(toolbar, R.mipmap.base_icon_setting, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtils.showInputDialog(mContext, "服务器地址", NetConfig.IP_ADDRESS, new OnInputListener() {
                    @Override
                    public void onClickPositive(String inputStr) {
                        String oldAddress = NetConfig.IP_ADDRESS;
                        if(oldAddress.equals(inputStr)){
                            ToastUtils.showToast(mContext, "地址未做任何修改");
                            return;
                        }
                        if (IPUtil.isValidAddress(inputStr)) {
                            ShareUtil.put(NetConfig.PRE_SERVICE_IP, inputStr);
                            UserHelper.getInstance().clear();
                            ToastUtils.showToast(mContext, "即将退出，请重新进入");
                            toolbar.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    appExit();
                                }
                            }, 1000);
                        } else {
                            ToastUtils.showToast(mContext, "地址格式有误");
                        }
                    }

                    @Override
                    public void onClickNegative() {
                    }
                });
            }
        });

        initUserView();
    }

    private void initUserView() {
        accountList = new ArrayList<>();
        List<UserEntity> allEntity = UserHelper.getInstance().queryList();
        for (UserEntity entity : allEntity) {
            accountList.add(entity.getUser_name());
        }

        if (accountList.size() > 0) {
            initUserImg(accountList.get(0));
            initPopup();
            etPhone.setText(accountList.get(0));
            etPhone.setListener(new CommEditText.OnGetPopupListener() {
                @Override
                public void onClick(boolean show) {
                    if (show) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            listPopup.setDropDownGravity(Gravity.START);
                        }
                        listPopup.setAnchorView(etPhone);
                        listPopup.show();
                    } else {
                        if (listPopup != null) {
                            listPopup.dismiss();
                        }
                    }
                }
            });
        } else {
            etPhone.setAccountVisible(false);
        }

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initUserImg(String userName) {
        UserEntity userEntity = UserHelper.getInstance().queryByUserName(userName);
        if (userEntity != null) {
            byte[] img = userEntity.getUser_img();
            if (img != null) {
                Glide.with(this).load(img).into(ivHeader);
            } else {
                ivHeader.setBackgroundResource(R.mipmap.avatar_default);
            }
        }
    }

    private void initPopup() {
        listPopup = new ListPopupWindow(this);
        adapter = new MyLoginPopupAdapter(this, accountList);
        listPopup.setAdapter(adapter);
        listPopup.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopup.setModal(true);
        listPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etPhone.setText(accountList.get(position));
                if (listPopup != null) {
                    listPopup.dismiss();
                }
            }
        });
    }

    @OnClick({R2.id.bt_login, R2.id.bt_register, R2.id.hm_qq, R2.id.hm_wechat, R2.id.hm_sina})
    public void onViewClicked(View view) {
        int viewId = view.getId();
        if(viewId == R.id.bt_login){
            String user = etPhone.getText().toString().trim();
            String pwd = etPsw.getText().toString().trim();
            presenter.login(mContext, "junfeng", "123", this);
        }else if(viewId == R.id.bt_register){
            readGo(MyRegistActivity.class);
        }else if(viewId == R.id.hm_qq){
            authorization(SHARE_MEDIA.QQ);
        }else if(viewId == R.id.hm_wechat){
            authorization(SHARE_MEDIA.WEIXIN);
        }else if(viewId == R.id.hm_sina){
            authorization(SHARE_MEDIA.SINA);
        }
    }

    private long firstTime;

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            ToastUtils.showToast(this, "再按一次退出程序");
            firstTime = secondTime;
        } else {
            appExit();
        }
    }

    @Override
    public void loginProgress() {
        DialogUtils.showProgressDialog(this, "正在登录");
    }

    @Override
    public void loginSuccess() {
        DialogUtils.dismissProgressDialog();
        Intent intent = new Intent(GlobalConfig.MAIN_INTENT);
        String className = queryPkgName(intent);
        if(NotNull.isNotNull(className)){
            intent.setClassName(BuildConfig.APP_ID, className);
            startActivity(intent);
            finish();
        }else{
            ToastUtils.showToast(this, "应用内部错误，请清除数据");
        }
    }

    @Override
    public void loginFail(String statusStr) {
        DialogUtils.dismissProgressDialog();
        ToastUtils.showToast(mContext, statusStr);
    }

    @Override
    public RxAppCompatActivity getRxActivity() {
        return this;
    }

    private String queryPkgName(Intent intent){
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activitis = packageManager.queryIntentActivities(intent, 0);
        for(ResolveInfo info : activitis){
            ActivityInfo activityInfo = info.activityInfo;
            if(activityInfo != null){
                if(BuildConfig.APP_ID.equals(activityInfo.packageName)){
                    return activityInfo.name;
                }
            }
        }
        return null;
    }

    private void authorization(SHARE_MEDIA share_media) {
        UMShareAPI.get(this).getPlatformInfo(this, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                LogUtil.d(TAG, "onStart " + "授权开始");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                LogUtil.d(TAG, "onComplete " + "授权完成");
                //sdk是6.4.4的,但是获取值的时候用的是6.2以前的(access_token)才能获取到值,未知原因
                String uid = map.get("uid");
                String openid = map.get("openid");//微博没有
                String unionid = map.get("unionid");//微博没有
                String access_token = map.get("access_token");
                String refresh_token = map.get("refresh_token");//微信,qq,微博都没有获取到
                String expires_in = map.get("expires_in");
                String name = map.get("name");
                String gender = map.get("gender");
                String iconurl = map.get("iconurl");

                LogUtil.d(TAG, "uid:"+uid+":"+name+":"+gender+":"+iconurl);
                LogUtil.d(TAG, "openid:"+openid+":"+unionid+":"+access_token+":"+refresh_token+":"+expires_in);

                thirdLoginSuccess(name, iconurl);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                LogUtil.d(TAG, "onError " + "授权失败");
                loginFail("三方授权失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                LogUtil.d(TAG, "onCancel " + "三方授权取消");
                loginFail("三方授权取消");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void thirdLoginSuccess(String userName, String userurl){
        UserHelper helper = UserHelper.getInstance();
        UserEntity oldEntity = helper.queryByUserName(userName);

        int userId = LoginPresenter.ROOT_ID_THIRD;
        String token = Long.toString(System.currentTimeMillis()); // Token
        long current = System.currentTimeMillis();
        if(oldEntity == null){
            UserEntity user = new UserEntity();
            user.setUser_id(userId);
            user.setUser_name(userName);
            user.setUser_token(token);
            user.setUser_url(userurl);
            user.setUser_lasttime(Long.toString(current));
            UserHelper.getInstance().insert(user);
        }else{
            oldEntity.setUser_id(userId);
            oldEntity.setUser_token(token);
            oldEntity.setUser_url(userurl);
            oldEntity.setUser_lasttime(Long.toString(current));
            UserHelper.getInstance().update(oldEntity);
        }
        ShareUtil.put(GlobalConfig.MY_USERNAME, userName);          // 存下token到SP中
        loginSuccess();
    }
}
