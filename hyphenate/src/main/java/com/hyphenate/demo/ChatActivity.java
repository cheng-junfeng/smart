package com.hyphenate.demo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.hyphenate.demo.Fragment.ChatFragment;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.ui.EaseBaseActivity;

public class ChatActivity extends EaseBaseActivity {
    private ChatFragment chatFragment;
    String userId;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_ease_chat);
        userId = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        setChatFragment(userId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // cancel the notification
        EaseUI.getInstance().getNotifier().reset();
    }

    private void setChatFragment(String groupId) {
        if (!TextUtils.isEmpty(groupId)){
            chatFragment=new ChatFragment();
            Bundle bundle=new Bundle();
            bundle.putString(EaseConstant.EXTRA_USER_ID, groupId);
            bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
            chatFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
        }else {
            finish();
        }
    }
}
