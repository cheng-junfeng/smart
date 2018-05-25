package com.wu.safe.user.ui.view;

import android.content.Context;
import android.content.Intent;
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

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.smart.base.app.listener.OnInputClickListener;
import com.smart.base.config.GlobalConfig;
import com.wu.safe.user.config.NetConfig;
import com.smart.base.ui.widget.CommEditText;
import com.smart.base.utils.DialogUtils;
import com.smart.base.utils.IPUtil;
import com.smart.base.utils.ShareUtil;
import com.smart.base.utils.ToolbarUtil;
import com.wu.safe.user.R;
import com.wu.safe.user.R2;
import com.wu.safe.user.app.acitvity.UserBaseCompatActivity;
import com.wu.safe.user.db.entity.UserEntity;
import com.wu.safe.user.db.helper.UserHelper;
import com.wu.safe.user.ui.adapter.MyLoginPopupAdapter;
import com.wu.safe.user.ui.contract.LoginContract;
import com.wu.safe.user.ui.presenter.LoginPresenter;

import java.util.ArrayList;
import java.util.List;

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
                DialogUtils.showInputDialog(mContext, "服务器地址", NetConfig.IP_ADDRESS, new OnInputClickListener() {
                    @Override
                    public void onClickPositive(String inputStr) {
                        String oldAddress = NetConfig.IP_ADDRESS;
                        if(oldAddress.equals(inputStr)){
                            DialogUtils.showToast(mContext, "地址未做任何修改");
                            return;
                        }
                        if (IPUtil.isValidAddress(inputStr)) {
                            ShareUtil.put(NetConfig.PRE_SERVICE_IP, inputStr);
                            UserHelper.getInstance().clear();
                            DialogUtils.showToast(mContext, "即将退出，请重新进入");
                            toolbar.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    appExit();
                                }
                            }, 1000);
                        } else {
                            DialogUtils.showToast(mContext, "地址格式有误");
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

    @OnClick({R2.id.bt_login, R2.id.bt_register})
    public void onViewClicked(View view) {
        int viewId = view.getId();
        if(viewId == R.id.bt_login){
            String user = etPhone.getText().toString().trim();
            String pwd = etPsw.getText().toString().trim();
            presenter.login(mContext, "junfeng", "123", this);
        }else if(viewId == R.id.bt_register){
            readGo(MyRegistActivity.class);
        }
    }

    private long firstTime;

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            ToastUtils.showLong("再按一次退出程序");
            firstTime = secondTime;
        } else {
            appExit();
        }
    }

    @Override
    public void loginProgress() {
        showProgress("正在登录");
    }

    @Override
    public void loginSuccess() {
        dismissProgress();
        Intent intent = new Intent(GlobalConfig.MAIN_INTENT);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginFail(String statusStr) {
        dismissProgress();
        DialogUtils.showToast(mContext, statusStr);
    }

    @Override
    public RxAppCompatActivity getRxActivity() {
        return this;
    }
}
