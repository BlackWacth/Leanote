package com.bruce.leanote.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * 日志工具类
 * @author hzw
 *
 */
public class L {

    public static String customTagPrefix = "";
    public static boolean isShow = true;

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(L:%d)";
        String callerClazzName = getClassName(caller);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }
    
    private static String getClassName(StackTraceElement caller) {
    	String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        return callerClazzName;
    }

    public static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    public static void i(String text) {
        if (isShow) {
            StackTraceElement caller = getCallerStackTraceElement();
            Log.i(getClassName(caller), generateTag(caller) + " : " + text);
        }
    }

    public static void i(String tag, String text) {
        if (isShow) {
            StackTraceElement caller = getCallerStackTraceElement();
            Log.i(tag, generateTag(caller) + " : " + text);
        }
    }

    public static void w(String text) {
        if (isShow) {
            StackTraceElement caller = getCallerStackTraceElement();
            Log.w(getClassName(caller), generateTag(caller) + " : " + text);
        }
    }

    public static void w(String tag, String text) {
        if (isShow) {
            StackTraceElement caller = getCallerStackTraceElement();
            Log.w(tag, generateTag(caller) + " : " + text);
        }
    }


    public static void e(String text) {
        if (isShow) {
            StackTraceElement caller = getCallerStackTraceElement();
            Log.e(getClassName(caller), generateTag(caller) + " : " + text);
        }
    }

    public static void e(String tag, String text) {
        if (isShow) {
            StackTraceElement caller = getCallerStackTraceElement();
            Log.e(tag, generateTag(caller) + " : " + text);
        }
    }
}
