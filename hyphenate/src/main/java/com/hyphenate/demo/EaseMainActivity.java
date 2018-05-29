package com.hyphenate.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.exceptions.HyphenateException;


public class EaseMainActivity extends EaseBaseActivity {

    private final static String TAG = "EaseMainActivity";
    private Button logoutButton;
    private Button chatButton;
    private Button loginButton;

    private Context mContext;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mContext = this;
        setContentView(R.layout.activity_main_ease);

        loginButton = (Button) findViewById(R.id.main_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EMClient.getInstance().isLoggedInBefore()) {
                    Toast.makeText(mContext, "已经登录", Toast.LENGTH_LONG).show();
                } else {
                    EMClient.getInstance().login("fireman1", "123456", new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "login: onSuccess");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                                            } catch (HyphenateException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                    // ** 如果用到群组，则加载群组
                                    EMClient.getInstance().groupManager().loadAllGroups();
                                    EMClient.getInstance().chatManager().loadAllConversations();
                                    Toast.makeText(mContext, "登录成功", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void onError(final int i, final String s) {
                            Log.d(TAG, "login onError code:" + i + ", message:" + s);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void onProgress(int code, final String s) {
                            Log.d(TAG, "login onProgress: code:" + code + "," + s);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
            }
        });

        chatButton = (Button) findViewById(R.id.main_chat);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(EaseConstant.EXTRA_USER_ID, "49251080011778");
                bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
                Intent intent = new Intent(mContext, ChatEaseActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        logoutButton = (Button) findViewById(R.id.main_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (EMClient.getInstance().isLoggedInBefore()) {
                    EMClient.getInstance().logout(false, new EMCallBack() {

                        @Override
                        public void onSuccess() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext.getApplicationContext(), "退出成功", Toast.LENGTH_LONG).show();
                                }
                            });
                            startActivity(new Intent(mContext, LoginEaseActivity.class));
                            finish();
                        }

                        @Override
                        public void onProgress(int progress, String status) {

                        }

                        @Override
                        public void onError(int code, String error) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext.getApplicationContext(), "退出失败", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                } else {
                    Toast.makeText(mContext.getApplicationContext(), "尚未登录", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
