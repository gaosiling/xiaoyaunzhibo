package com.beicai.xiaoyuanzhibo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaoyuan on 17/3/15.
 */

public class CommonUtils {
    public static boolean checkPhoneNumber(String number) {
        number = number.replace(" ", "");
        Pattern p = Pattern.compile("1\\d{10}");
        Matcher m = p.matcher(number);
        return m.matches() && (number.length() == 11);
    }
}
