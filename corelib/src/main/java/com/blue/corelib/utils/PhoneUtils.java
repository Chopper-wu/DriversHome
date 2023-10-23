package com.blue.corelib.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Chopper on 2021/2/25
 * desc : 手机号码处理工具类
 */
public class PhoneUtils {
    private PhoneUtils() {
    }

    /**
     * 手机号格式校验正则
     */
    public static final String PHONE_REGEX = "^1(3[0-9]|4[57]|5[0-35-9]|7[0135678]|8[0-9])\\d{8}$";

    /**
     * 手机号脱敏筛选正则
     */
    public static final String PHONE_BLUR_REGEX = "(\\d{3})\\d{4}(\\d{4})";

    /**
     * 手机号脱敏替换正则
     */
    public static final String PHONE_BLUR_REPLACE_REGEX = "$1****$2";

    /**
     * 手机号格式校验
     * @param phone
     * @return
     */
    public static final boolean checkPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        //return phone.matches(PHONE_REGEX);
        return true;
    }

    /**
     * 手机号脱敏处理
     * @param phone
     * @return
     */
    public static final String blurPhone(String phone) {
        boolean checkFlag = checkPhone(phone);
        if (!checkFlag) {
            //手机号格式不正确!
            return "0";
        }
        return phone.replaceAll(PHONE_BLUR_REGEX, PHONE_BLUR_REPLACE_REGEX);
    }
    /**
     * 验证手机格式
     */
    public static boolean isPhone(String phone) {
        /*
         * 电信
         * 中国电信手机号码开头数字
         * 2G/3G号段（CDMA2000网络）133、153、180、181、189
         * 4G号段 177
         *
         * 联通
         * 中国联通手机号码开头数字
         * 2G号段（GSM网络）130、131、132、155、156
         * 3G上网卡145
         * 3G号段（WCDMA网络）185、186
         * 4G号段 176、185[1]
         *
         * 移动
         * 中国移动手机号码开头数字
         * 2G号段（GSM网络）有134x（0-8）、135、136、137、138、139、150、151、152、158、159、182、183、184。
         * 3G号段（TD-SCDMA网络）有157、187、188
         * 3G上网卡 147
         * 4G号段 178
         * 补充
         *
         * 14号段以前为上网卡专属号段，如中国联通的是145，中国移动的是147等等。
         * 170号段为虚拟运营商专属号段，170号段的 11 位手机号前四位来区分基础运营商，其中 “1700” 为中国电信的转售号码标识，“1705” 为中国移动，“1709” 为中国联通。
         */
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        phone = phone.replaceAll("\\s*", "");
        Pattern regex   = Pattern.compile("^((1[358][0-9])|(14[57])|(17[03678]))\\d{8}$");
        Matcher matcher = regex.matcher(phone);
        return matcher.matches();
    }
    /***
     * 验证手机长度
     */
    public static boolean isPhoneLength(String phone){
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        return phone.length() == 11;
    }
}
