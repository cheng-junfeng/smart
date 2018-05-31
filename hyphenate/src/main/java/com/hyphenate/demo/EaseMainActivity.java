package com.hyphenate.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;


public class EaseMainActivity extends EaseBaseActivity {

    private final static String TAG = "EaseMainActivity";

    AutoCompleteTextView user;
    AutoCompleteTextView pwd;

    AutoCompleteTextView groupId;

    private Button logoutButton;
    private Button chatButton;
    private Button chatButton2;
    private Button loginButton;

    private Button logoutOnlyButton;

    private Context mContext;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mContext = this;
        setContentView(R.layout.activity_main_ease);

        user = (AutoCompleteTextView)findViewById(R.id.main_user);
        pwd = (AutoCompleteTextView)findViewById(R.id.main_pwd);
        groupId = (AutoCompleteTextView)findViewById(R.id.main_group);

        loginButton = (Button) findViewById(R.id.main_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EMClient.getInstance().isLoggedInBefore()) {
                    Toast.makeText(mContext, "已经登录", Toast.LENGTH_LONG).show();
                } else {
                    String username = user.getText().toString();
                    String password = pwd.getText().toString();
                    EMClient.getInstance().login(username, password, new EMCallBack() {
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
                String group  = groupId.getText().toString();
                bundle.putString(EaseConstant.EXTRA_USER_ID, group);
                bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        chatButton2 = (Button) findViewById(R.id.main_chat2);
        chatButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String group  = groupId.getText().toString();
                bundle.putString(EaseConstant.EXTRA_USER_ID, group);
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

        logoutOnlyButton = (Button) findViewById(R.id.main_logout_only);
        logoutOnlyButton.setOnClickListener(new View.OnClickListener() {

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

        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
    }


    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onresume"+EMClient.getInstance().isConnected());
    }

    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
            Log.d(TAG, "onConnected");
        }
        @Override
        public void onDisconnected(final int error) {
            Log.d(TAG, "onDisconnected");
        }
    }
}

