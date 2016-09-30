//package com.joecorelibrary.util;
//
//import android.util.Log;
//
//
///**
// * 控制台日志输出
// */
//public class LogUtil {
//    public static String TAG = "NHS";
//
//    public static void v(String msg) {
//        if (BuildConfig.DEBUG) {
//            Log.v(TAG, msg);
//        }
//    }
//
//    public static void v(String tag, String msg) {
//        if (BuildConfig.DEBUG) {
//            Log.v(tag, msg);
//        }
//    }
//
//    public static void d(String msg) {
//        if (BuildConfig.DEBUG) {
//            Log.d(TAG, msg);
//        }
//    }
//
//    public static void d(String tag, String msg) {
//        if (BuildConfig.DEBUG) {
//            Log.d(tag, msg);
//        }
//    }
//
//    public static void i(String msg) {
//        if (BuildConfig.DEBUG) {
//            Log.i(TAG, msg);
//        }
//    }
//
//    public static void i(String tag, String msg) {
//        if (BuildConfig.DEBUG) {
//            Log.i(tag, msg);
//        }
//    }
//
//    public static void w(String msg) {
//        if (BuildConfig.DEBUG) {
//            Log.w(TAG, msg);
//        }
//    }
//
//    public static void w(String tag, String msg) {
//        if (BuildConfig.DEBUG) {
//            Log.w(tag, msg);
//        }
//    }
//
//    public static void e(String msg, Throwable e) {
//        Log.e(TAG, msg, e);
//    }
//
//    public static void e(String tag, String msg) {
//        Log.e(tag, msg);
//    }
//
//    public static void e(String tag, String msg, Throwable e) {
//        Log.e(tag, msg, e);
//    }
//
//    public static void handleException(Exception e) {
//        e(TAG, "======Exception======", e);
//    }
//
//}