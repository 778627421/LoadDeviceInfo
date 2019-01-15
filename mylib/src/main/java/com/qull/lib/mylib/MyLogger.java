package com.qull.lib.mylib;

import android.util.Log;

import com.qull.lib.BuildConfig;

import java.util.Hashtable;

/**
 * Copyright (c) 2018, 数字多⽹网络技术有限公司 All rights reserved.
 * File Name:MyLogger
 * Version:V1.0
 * Author:qulonglong
 * Date:2018/12/5
 */


public class MyLogger {
   public boolean mIsLoggerEnable = BuildConfig.DEBUG;
   private final static String LOG_TAG = "shuziduo";
   private static Hashtable<String, MyLogger> sLoggerTable;
   private String mClassName;

   static {
       sLoggerTable = new Hashtable<String, MyLogger>();
   }

   public static MyLogger getLogger(String className) {
       MyLogger classLogger = (MyLogger) sLoggerTable.get(className);
       if (classLogger == null) {
           classLogger = new MyLogger(className);
           sLoggerTable.put(className, classLogger);
       }
       return classLogger;
   }

   private MyLogger(String name) {
       mClassName = name;
   }

   public void v(String log) {
       if (mIsLoggerEnable) {
           Log.v(LOG_TAG, "{Thread:" + Thread.currentThread().getName() + "}" + "[" + mClassName
                   + ":] " + log);
       }
   }

   public void d(String log) {
       if (mIsLoggerEnable) {
           Log.i(LOG_TAG, "****my Log{Thread:" + Thread.currentThread().getName() + "}" + "[" + mClassName
                   + ":] ****");
           int index = 0;
           int maxLength = 3072;//大约4k字符长度限制
           String sub;
           while (index < log.length()) {

               try {
                   if (log.length() <= index + maxLength) {
                       sub = log.substring(index);
                   } else {
                       sub = log.substring(index, index + maxLength);
                   }

                   index += maxLength;
                   Log.d(LOG_TAG, sub);
               } catch (Exception e) {
                   e.printStackTrace();
                   break;
               }
           }
       }
   }

   public void i(String log) {
       if (mIsLoggerEnable) {
           Log.i(LOG_TAG, "{Thread:" + Thread.currentThread().getName() + "}" + "[" + mClassName
                   + ":] " + log);
       }
   }

   public void i(String log, Throwable tr) {
       if (mIsLoggerEnable) {
           Log.i(LOG_TAG, "{Thread:" + Thread.currentThread().getName() + "}" + "[" + mClassName
                   + ":] " + log + "\n" + Log.getStackTraceString(tr));
       }
   }

   public void w(String log) {
       if (mIsLoggerEnable) {
           Log.w(LOG_TAG, "{Thread:" + Thread.currentThread().getName() + "}" + "[" + mClassName
                   + ":] " + log);
       }
   }

   public void w(String log, Throwable tr) {
       if (mIsLoggerEnable) {
           Log.w(LOG_TAG, "{Thread:" + Thread.currentThread().getName() + "}" + "[" + mClassName
                   + ":] " + log + "\n" + Log.getStackTraceString(tr));
       }
   }

   public void e(String log) {
       if (mIsLoggerEnable) {
           Log.e(LOG_TAG, "{Thread:" + Thread.currentThread().getName() + "}" + "[" + mClassName
                   + ":] " + log);
       }
   }

   public void e(String log, Throwable tr) {
       if (mIsLoggerEnable) {
           Log.e(LOG_TAG, "{Thread:" + Thread.currentThread().getName() + "}" + "[" + mClassName
                   + ":] " + log + "\n" + Log.getStackTraceString(tr));
       }
   }
}
