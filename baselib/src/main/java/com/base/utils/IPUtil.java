package com.base.utils;

import com.blankj.utilcode.util.EmptyUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPUtil {

    private static final String REGEX = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
    private static final String REGEX_ADDRESS = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";

    public static boolean isValidIp(String address) {
        if (EmptyUtils.isEmpty(address)){
            return false;
        }
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(address);
        return m.matches();
    }

    public static boolean isValidAddress(String address) {
        if (EmptyUtils.isEmpty(address)){
            return false;
        }
        Pattern p = Pattern.compile(REGEX_ADDRESS);
        Matcher m = p.matcher(address);
        return m.matches();
    }
}
