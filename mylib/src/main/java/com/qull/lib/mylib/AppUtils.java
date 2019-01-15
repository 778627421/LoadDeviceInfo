package com.qull.lib.mylib;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.RequiresPermission;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2017/11/09
 *    desc   : 工具类: APP 相关
 * </pre>
 */
public class AppUtils {

    private AppUtils() {
    }

    /**
     * 判断服务是否正在运行
     *
     * @param serviceClassName 指定的服务类名
     * @return true 为正在运行; 其他为 false
     */
    public static boolean isServiceRunning(Application application, String serviceClassName) {
        ActivityManager activityManager = (ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前 APP 是否正在前台运行<br/>
     * 注意: 原理是判断 APP 是否处于栈顶, 屏幕是否关闭不会影响结果
     *
     * @return true 为是, false 为否
     */
    @RequiresPermission(Manifest.permission.GET_TASKS)
    public static boolean isAppOnForeground(Application application) {
        return isAppOnForeground(application,getPackageName(application));
    }

    /**
     * 判断某应用程序(App)是否正在前台运行
     * 注意: 原理是判断 APP 是否处于栈顶, 屏幕是否关闭不会影响结果
     *
     * @param packageName 应用程序包名
     * @return true 为是, false 为否
     */
    @RequiresPermission(Manifest.permission.GET_TASKS)
    public static boolean isAppOnForeground(Application application,String packageName) {
        ActivityManager activityManager = (ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            // 应用程序位于堆栈的顶层
            if (tasksInfo.get(0).topActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取栈顶 Activity 的名称
     *
     * @return 栈顶 Activity 的名称
     */
    public static String getTopActivityName(Application application) {
        ActivityManager activityManager = (ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            return activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        }
        return null;
    }

    /**
     * 获取当前 APP 的包名
     *
     * @return 当前 APP 的包名
     */
    public static String getPackageName(Application application) {
        return application.getPackageName();
    }

    /**
     * 获取当前 APP 的版本名称
     *
     * @return 当前 APP 的版本名称
     */
    public static String getVersionName(Application application) {
        PackageManager packageManager = application.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(application), 0);
            // 获取版本名称
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取当前 APP 的版本号
     *
     * @return 当前 APP 的版本号
     */
    public static int getVersionCode(Application application) {
        PackageManager packageManager = application.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(application), 0);
            // 获取版本名称
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当前 APP 的应用名称
     *
     * @return 当前 APP 的应用名称
     */
    public String getAppName(Application application) {
        PackageManager pm = application.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(getPackageName(application), 0);
            return info.loadLabel(pm).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前 APP 的应用签名
     *
     * @return 当前 APP 的应用签名
     */
    public static String getAppSignature(Application application) {
        PackageManager pm = application.getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(getPackageName(application), PackageManager.GET_SIGNATURES);
            return packinfo.signatures[0].toCharsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断指定的应用程序是否已安装
     *
     * @param packageName 指定的 APP 的包名
     * @return true 为已安装; false 为未安装
     */
    public static boolean isAppInstalled(Application application,String packageName) {
        try {
            application.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    /**
     * 判断当前 APP 是否为 Debug 版本
     *
     * @return true 为 Debug 版本; 其他为 false
     */
    public static boolean isAppDebug(Application application) {
        return isAppDebug(application,getPackageName(application));
    }

    /**
     * 判断指定的 APP 是否为 Debug 版本
     *
     * @param packageName 指定的 APP 的包名
     * @return true 为 Debug 版本; 其他为 false
     */
    public static boolean isAppDebug(Application application,String packageName) {
        try {
            PackageManager pm = application.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 杀死当前进程, 并退出 APP
     */
    public static void killCurrentProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
