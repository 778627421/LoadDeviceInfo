package com.qull.lib.mylib.deviceapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 原文：https://blog.csdn.net/u010248450/article/details/81503439
 * File Name:
 * Version:V1.0
 * Author:qulonglong
 * Date:2019/1/15
 */

public class CheckAppInstalledUtil {
//使用
//    if(CheckAppInstalledUtil.isInstalled(this,"此处填写你需要打开应用的包名如com.xxx.xxx")){//判断是否安装
//        CheckAppInstalledUtil.openApp(this, "此处填写你需要打开应用的包名如com.xxx.xxx")); //打开应用
//    }else{//没有安装
//        //TODO 去下载
//    }

    /**
     * 判断 APP 是否安装
     *
     * @param context     活动对应的上下文对象
     * @param packageName 需要检查的应用包名
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        try {
            Process process = Runtime.getRuntime().exec("pm list package -3");
            BufferedReader bis = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = bis.readLine()) != null) {
                System.out.println("MainActivity.runCommand, line=" + line.substring(8,line.length()));
                if (packageName.equals(line.substring(8,line.length()))) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("MainActivity.runCommand,e=" + e);
        }
        return false;
    }

    private void runCommand() {
        try {
            Process process = Runtime.getRuntime().exec("pm list package -3");
            BufferedReader bis = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = bis.readLine()) != null) {
                System.out.println("MainActivity.runCommand, line=" + line);
            }
        } catch (IOException e) {
            System.out.println("MainActivity.runCommand,e=" + e);
        }
    }

    /**
     * 打开或者去下载应用
     *
     * @param context     活动对应上下文对象
     * @param packagename 需要打开的应用包名
     */
    public static void openApp(Activity context, String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }
}
