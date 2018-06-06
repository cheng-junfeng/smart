package com.hyphenate;

import android.content.Context;

import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseAvatarOptions;


public class EaseUIAPI {
    public static void init(Context context){
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        if (EaseUI.getInstance().init(context, options)){
            EaseAvatarOptions easeAvatarOptions=new EaseAvatarOptions();
            //圆形头像
            easeAvatarOptions.setAvatarShape(1);
            EaseUI.getInstance().setAvatarOptions(easeAvatarOptions);
        }
    }
}
