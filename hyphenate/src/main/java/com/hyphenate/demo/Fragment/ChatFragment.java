package com.hyphenate.demo.Fragment;

import android.support.v4.content.ContextCompat;
import android.view.View;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.ui.EaseChatFragment;


public class ChatFragment extends EaseChatFragment {
     @Override
    protected void setUpView() {
         //主题颜色
        titleBar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        super.setUpView();
        titleBar.getRightLayout().setVisibility(View.GONE);
        //右边不可见
    }

    @Override
    protected void registerExtendMenuItem() {
        super.registerExtendMenuItem();
    }
}
