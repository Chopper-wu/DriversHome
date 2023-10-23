package com.blue.corelib.encryption;


import java.security.Key;

/**
 * Author: Chopper
 * Date: 2016/7/27 18:08
 * <p>
 * Description: RSA 工具类
 */
@SuppressWarnings("unused")
public class RSAUtil {
    ///////////////////////////////////////////////////////////////////////////
    // RSA + Base64 加解密
    ///////////////////////////////////////////////////////////////////////////
    public static String Base64Encrypt(Key encryptKey, String data) {
        try {
            return Base64.encode(RSA.encrypt(encryptKey, data.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    public static String Base64Decrypt(Key decryptKey, String data) {
        try {
            byte[] base64Decrypt = RSA.decrypt(decryptKey, Base64.decode(data));
            if (null == base64Decrypt) {
                return data;
            } else {
                return new String(base64Decrypt);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // RSA + BCD 加解密
    ///////////////////////////////////////////////////////////////////////////
    public static String BCDEncrypt(Key encryptKey, String data) {
        try {
            return BCD.encode(RSA.encrypt(encryptKey, data.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    public static String BCDDecrypt(Key decryptKey, String data) {
        try {
            byte[] bcdDecrypt = RSA.decrypt(decryptKey, BCD.decode(data));
            if (null == bcdDecrypt) {
                return data;
            } else {
                return new String(bcdDecrypt);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    //设置加密+base64
    public static String getKeyBase64(String data) {
        try {
            final String PEM_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWoNzULbKSEPO53N9+OhKhOpbjEuv+vtJEF8wmqWJSGGXOBRBMLYUfUvAv7fZ8WAGc5MFyEQocGouB3ua2hpUBTdGt7OHlfGorAvkBrCuidVAd1g9p9A3oJOihhjerz07hDR/ljiF9vGzPq2slVjgemBsHlQ2glsuHDp+gbxdhRwIDAQAB";
            /** 公钥加密，私钥解密 */
            // PEM
            // 加密密钥
            Key encryptKey = RSA.getKey(RSA.MODE.PEM_STRING, RSA.TYPE_PUBLIC, PEM_PUBLIC);
            String encrypt_base64 = Base64Encrypt(encryptKey, data);
            System.out.println("Base64加密后: " + encrypt_base64);
            return encrypt_base64;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    ///////////////////////////////////////////////////////////////////////////
    // 测试方法
    ///////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws Exception {
        /** 初始化 */
        /*
        // 模数
        final String MODULUS = "107313014787453253829513673985053918726593068988221496468690790790802075050722805681779868076020008707138640388406561" +
                "068474396154327072223704660330307918588647384313038984599338136855776599433735821877968427797411779108082005780044235264787430800389" +
                "781402472437269446693342945094190149098376175320612555505261";
        // 公钥指数
        final String EXPONENT_PUBLIC = "65537";
        // 私钥指数
        final String EXPONENT_PRIVATE = "825630839314123009077580369817719561474018603922075208652261631330976735005805164729344417371726029423279143" +
                "943762397148880174999386550446867629457373067125297824799565849115055482890092858954479969385576382410003319025033705676588412117911" +
                "87830923546955842844428915263387790024344591831863514724691718478017";
        */

        final String PEM_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWoNzULbKSEPO53N9+OhKhOpbjEuv+vtJEF8wmqWJSGGXOBRBMLYUfUvAv7fZ8WAGc5MFyEQocGouB3ua2hpUBTdGt7OHlfGorAvkBrCuidVAd1g9p9A3oJOihhjerz07hDR/ljiF9vGzPq2slVjgemBsHlQ2glsuHDp+gbxdhRwIDAQAB";
        final String PEM_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJag3NQtspIQ87nc3346EqE6luMS6/6+0kQXzCapYlIYZc4FEEwthR9S8C/t9nxYAZzkwXIRChwai4He5raGlQFN0a3s4eV8aisC+QGsK6J1UB3WD2n0Degk6KGGN6vPTuENH+WOIX28bM+rayVWOB6YGweVDaCWy4cOn6BvF2FHAgMBAAECgYA29NP9Gfsn7cVuz5Y/MKLxbjX0/UdbN3Xx4BjdFjnIKByksRZEJOuIBTOa2SBwN4/HFXM60dq1bD3wEUpt1+Zu9wIlu2yS+/NgnJSf3UDDRLr+S0MAf4F12q3ccR3tzLNFYtU1OSQWtyVTrkJPXKHCgVwjntqBzHA8BAhi7+XQOQJBAMoyKgHAwNdLawbszDH/m89NAia/8ZFaXCWCnXW1zq3Zto5JC5vUohTNPsfg/+XzAk2tc4fD7vd5lM4/YT/BNbUCQQC+tdoAlO2bz/onGHEHAe8DpbHe+5PlPpuo2ev29TnPGoDnzFOOcEa3SUkCvPO4AxU2aTMbFkZd1DHzlehPdViLAkAZ2qg8aSU/YklhLU4+5mxJ6ZLo2YpOB7vYoAb6UjEHUdth/j0Zw+qltkjczgxqntQgkpk/NvKLemRoqvoIaPAxAkA/U596RfnInX0RclpF22yEp4ay5pMrsmh53zMtpCx4CvL3BbBRQhMZVap60EeVuOBYWwJvYiwniAi2O/cSO4MVAkEAsSAFxq3XIPCn/0o7o5WBeGrogHcsSI1U9kEY9/X9zXm2UtP1QhoMU+r+pOzsnq/j6OOaahEL+4DRyngFtOF+MA==";
        // 加密密钥
        Key encryptKey;
        // 解密密钥
        Key decryptKey;
        /** 公钥加密，私钥解密 */
        // PEM
        encryptKey = RSA.getKey(RSA.MODE.PEM_STRING, RSA.TYPE_PUBLIC, PEM_PUBLIC);
        decryptKey = RSA.getKey(RSA.MODE.PEM_STRING, RSA.TYPE_PRIVATE, PEM_PRIVATE);
        // 模数指数
        // encryptKey = RSA.getKey(MODE.MODULUS_EXPONENT, RSA.TYPE_PUBLIC, MODULUS, EXPONENT_PUBLIC);
        // decryptKey = RSA.getKey(MODE.MODULUS_EXPONENT, RSA.TYPE_PRIVATE, MODULUS, EXPONENT_PRIVATE);

        /** 私钥加密，公钥解密 */
        // PEM
        // encryptKey = RSA.getKey(MODE.PEM_STRING, RSA.TYPE_PRIVATE, PEM_PRIVATE);
        // decryptKey = RSA.getKey(MODE.PEM_STRING, RSA.TYPE_PUBLIC, PEM_PUBLIC);
        // 模数指数
        // encryptKey = RSA.getKey(MODE.MODULUS_EXPONENT, RSA.TYPE_PRIVATE, MODULUS, EXPONENT_PRIVATE);
        // decryptKey = RSA.getKey(MODE.MODULUS_EXPONENT, RSA.TYPE_PUBLIC, MODULUS, EXPONENT_PUBLIC);

        System.out.println("\n-------------------- content --------------------");
        // 待加密字符串
        String data = "";
        for (int i = 0; i < 8; i++) {
            data += (int) (Math.random() * 10);
        }
        System.out.println("待加密内容: " + data);

        System.out.println("\n-------------------- encrypt --------------------");
        // "RSA加密"后再进行"Base64"加密
        String encrypt_base64 = Base64Encrypt(encryptKey, data);
        System.out.println("Base64加密后: " + encrypt_base64);
        // "RSA加密"后再进行"BCD"加密
        String encrypt_bcd = BCDEncrypt(encryptKey, data);
        System.out.println("BCD加密后: " + encrypt_bcd);

        System.out.println("\n-------------------- decrypt --------------------");
        // "Base64"解密后再进行"RSA解密"
        System.out.println("Base64解密后: " + Base64Decrypt(decryptKey, encrypt_base64));
        // "BCD"解密后再进行"RSA解密"
        System.out.println("BCD解密后: " + BCDDecrypt(decryptKey, encrypt_bcd));

        System.out.println("\n-------------------- test --------------------");
        // 待解密字符串
        String dec = "";
        System.out.println("解密结果: " + BCDDecrypt(decryptKey, dec));
    }
}
