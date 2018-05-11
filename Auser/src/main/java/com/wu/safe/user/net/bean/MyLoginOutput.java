package com.wu.safe.user.net.bean;


public class MyLoginOutput {
    public String accessToken;
    public String encryptedAccessToken;
    public int expireInSeconds;
    public boolean shouldResetPassword;
    public Object passwordResetCode;
    public int userId;
    public boolean requiresTwoFactorVerification;
    public Object twoFactorAuthProviders;
    public Object twoFactorRememberClientToken;
    public Object returnUrl;
    public String code;
    public boolean loginSuccess;
}
