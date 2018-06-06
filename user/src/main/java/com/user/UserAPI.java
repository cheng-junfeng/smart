package com.user;


import android.content.Context;

import com.thirdlogin.ThirdLoginAPI;
import com.user.db.control.UserDbManager;

public class UserAPI {
    public static void init(Context context){
        UserDbManager.init(context);
        ThirdLoginAPI.init(context);
    }
}
