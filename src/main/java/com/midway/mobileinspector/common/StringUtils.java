package com.midway.mobileinspector.common;

/**
 * Created by ilya on 22.02.16.
 */
public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNo0tEmpty(String str) {
        return !isEmpty(str);
    }
}
