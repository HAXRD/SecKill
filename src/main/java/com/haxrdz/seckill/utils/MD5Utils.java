package com.haxrdz.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class MD5Utils {
    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    public static String inputStr2ServerStr(String src) {
        return md5(src + salt.charAt(3) + salt.charAt(2) + salt.charAt(5));
    }

    public static String serverStr2DBStr(String src, String salt) {
        return md5(salt.charAt(5) + salt.charAt(3) + salt.charAt(2) + src);
    }

    public static String inputStr2DBStr(String src, String salt) {
        String serverStr = inputStr2ServerStr(src);
        String dbStr = serverStr2DBStr(serverStr, salt);
        return dbStr;
    }

    public static void main(String[] args) {
        System.out.println(inputStr2ServerStr("123456"));
        System.out.println(serverStr2DBStr("3288306b519f4822dc4a04db6f92b20f", "1a2b3c4d"));
        System.out.println(inputStr2DBStr("123456", "1a2b3c4d"));
    }
}
