package com.blue.corelib.view;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetectedDataValidation {
    private static String[] filterStr = new String[]{"[", "]", "\\", "_"};

    /**
     * 判断字符串中是否包含表情符
     *
     * @param str
     * @return true 包含  false 不包含
     */
    public static boolean verifyValidStr(String str) {
        return Pattern.compile("(?:[\uD83C\uDF00-\uD83D\uDDFF]|[\uD83E\uDD00-\uD83E\uDDFF]" +
                "|[\uD83D\uDE00-\uD83D\uDE4F]|[\uD83D\uDE80-\uD83D\uDEFF]|[\u2600-\u26FF]\uFE0F?" +
                "|[\u2700-\u27BF]\uFE0F?|\u24C2\uFE0F?|[\uD83C\uDDE6-\uD83C\uDDFF]{1,2}" +
                "|[\uD83C\uDD70\uD83C\uDD71\uD83C\uDD7E\uD83C\uDD7F\uD83C\uDD8E\uD83C\uDD91-\uD83C\uDD9A]\uFE0F?" +
                "|[\u0023\u002A\u0030-\u0039]\uFE0F?\u20E3|[\u2194-\u2199\u21A9-\u21AA]\uFE0F?" +
                "|[\u2B05-\u2B07\u2B1B\u2B1C\u2B50\u2B55]\uFE0F?|[\u2934\u2935]\uFE0F?|[\u3030\u303D]\uFE0F?" +
                "|[\u3297\u3299]\uFE0F?|[\uD83C\uDE01\uD83C\uDE02\uD83C\uDE1A\uD83C\uDE2F\uD83C\uDE32-\uD83C\uDE3A\uD83C\uDE50\uD83C\uDE51]\uFE0F?" +
                "|[\u203C\u2049]\uFE0F?|[\u25AA\u25AB\u25B6\u25C0\u25FB-\u25FE]\uFE0F?|[\u00A9\u00AE]\uFE0F?" +
                "|[\u2122\u2139]\uFE0F?|\uD83C\uDC04\uFE0F?|\uD83C\uDCCF\uFE0F?" +
                "|[\u231A\u231B\u2328\u23CF\u23E9-\u23F3\u23F8-\u23FA]\uFE0F?)").matcher(str).find();//|| str.contains(" ")
    }

    /**
     * 判断是否为中字符
     *
     * @param str
     * @return
     */
    public static boolean isChina(String str) {
        boolean b = Pattern.compile("[\\u4e00-\\u9fa5]+").matcher(str).find();
        return b;
    }

    public static boolean VerifyMobileNum(String mobiles) {
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        }
        //  Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(14[57])|(17[03678])|(18[0-9]))\\d{8}$");
        Pattern p = Pattern.compile("^1\\d{10}$");
        //匹配国内运营商号段
        //中国移动：134、135、136、137、138、139、147、150、151、157、158、159、182、183、184、187、188、178、170[5]（虚拟运营商转售号段）
        //中国联通：130、131、132、145、155、156、185、186、176、170[9]（虚拟运营商转售号段）
        //中国电信：133、153、180、181、189、177、170[0]（虚拟运营商转售号段）
        //新增号段 173
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean VerifyEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        boolean ret = false;
        if (!email.contains("@")) {
            ret = false;
        }
        String[] email_frag = email.split("@");
        if (email_frag.length != 2) {
            ret = false;
        } else if (email_frag[0].length() <= 0) {
            ret = false;
        } else if (email_frag[1].length() <= 0) {
            ret = false;
        } else if (email.length() > 64) {
            ret = false;
        } else {
            String[] domain_frag = email_frag[1].split("\\.");
            if (domain_frag.length < 2) {
                ret = false;
            } else if (hasEmptyDomain(domain_frag)) {
                ret = false;
            } else {
                Pattern p = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+\\\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");
                //Pattern p = Pattern.compile("^[A-Za-z0-9]((\\.|-)?[A-Za-z0-9]+)*@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z]{2,}$");
                Matcher m = p.matcher(email);
                ret = m.matches();
            }
        }
        return ret;
    }

    /**
     * 检查邮箱域名地址是否包含空域名。避免在数据过长时交由正则表达式处理耗时过多
     */
    private static boolean hasEmptyDomain(String[] domains) {
        boolean result = false;
        for (String domain : domains) {
            if (domain.isEmpty()) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static boolean VerifyAccount(String account) {
        return VerifyMobileNum(account) || VerifyEmail(account);
    }

    /**
     * 验证码
     *
     * @param va_code
     * @return
     */
    public static boolean VerifyVaCode(String va_code) {
        if (TextUtils.isEmpty(va_code)) {
            return false;
        }
        Pattern p = Pattern.compile("^[\\d]{6}$");
        Matcher m = p.matcher(va_code);
        return m.matches();
    }

    public static boolean isContainNum(String str) {
        return str.matches(".*[0-9].*");
    }

    public static boolean isContainLetter(String str) {
        return str.matches(".*[a-zA-z].*");
    }

    /**
     * 是否为中文、数字、字母
     *
     * @param str
     * @return
     */
    public static boolean isOnlyContainChineseNUMLetter(String str) {
        return str.matches("^^[a-zA-Z0-9\\u4e00-\\u9fa5]*$");
    }
}
