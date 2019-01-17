package com.qull.lib.mylib.deviceapp;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

/**
 * File Name:
 * Version:V1.0
 * Author:qulonglong
 * Date:2019/1/17
 * 获取手机基本信息(厂商、型号等参数)
 * 设备信息获取实现图
 * 获取手机设备 宽、高、IMEI 信息
 * 获取手机厂商名、产品名、手机品牌、手机型号、主板名、设备名
 * 获取手机硬件名、SDK版本、android版本 、语言支持、默认语言
 * 获取 SD 卡存储信息
 */

public class SystemUtil {
    /**
     * 获取当前手机屏幕宽高
     */
    public static String getDeviceWidth(Context ctx) {
        return ctx.getResources().getDisplayMetrics().toString();
    }

    /**
     * 获取当前手机系统版本号 8.0.0
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取当前手机系统sdk 26
     */
    public static String getSystemSDK_INT() {
        return "" + Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机主板
     */
    public static String getSystemBOARD() {
        return Build.BOARD;
    }

    /**
     * 获取手机厂商
     */
    public static String getDeviceMANUFACTURER() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取手机品牌
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * 需要“android.permission.READ_PHONE_STATE”权限
     * 硬件序列号
     */
    public static String getDeviceSerial() {
        return Build.getSerial();
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
    public static String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getDeviceId();
        }
        return null;
    }

    /**
     * 获取手机IMSI号
     */
    public static String getIMSI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = mTelephonyMgr.getSubscriberId();

        return imsi;
    }

    /**
     * 设备序列号
     */
    public static String getANDROID_ID(Context ctx) {
        String androidId = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);

        return androidId;
    }

    /**
     * SD 卡是否存在
     */
    public static boolean isSDCardMount() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取手机MAC地址
     * 从 Android M 开始，无法再通过第三方 API 获得本地设备 MAC 地址（例如，WLAN 和蓝牙）。
     * WifiInfo.getMacAddress() 方法和 BluetoothAdapter.getDefaultAdapter().getAddress()
     * 方法都会返回 02:00:00:00:00:00。
     * google 已经限制 无法得到真实Mac
     * https://developer.android.com/training/articles/user-data-ids
     * android.permission.ACCESS_WIFI_STATE
     */
    public static String getMacAddress(Context ctx) {
        String result = "";
        WifiManager wifiManager = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        result = wifiInfo.getMacAddress();
        return "手机macAdd:" + result;
    }

    /**
     * 手机CPU信息
     */
    public static String getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""}; //1-cpu型号 //2-cpu频率
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "CPU型号:" + cpuInfo[0] + "CPU频率：" + cpuInfo[1];
    }

    public static void showSystemParameterTest(Context ctx) {
        String TAG = "系统参数：";
        Log.e(TAG, "手机屏幕参数：" + SystemUtil.getDeviceWidth(ctx));
        Log.e(TAG, "手机厂商：" + SystemUtil.getDeviceMANUFACTURER());
        Log.e(TAG, "手机语言：" + SystemUtil.getSystemLanguage());
        Log.e(TAG, "手机品牌：" + SystemUtil.getDeviceBrand());
        Log.e(TAG, "手机型号：" + SystemUtil.getSystemModel());
        Log.e(TAG, "Android系统版本号：" + SystemUtil.getSystemVersion());
        Log.e(TAG, "系统SDK：" + getSystemSDK_INT());
        Log.e(TAG, "手机IMEI：" + SystemUtil.getIMEI(ctx));
        Log.e(TAG, "手机IMSI：" + SystemUtil.getIMSI(ctx));
        Log.e(TAG, "手机ANDROID_ID：" + SystemUtil.getANDROID_ID(ctx));
        Log.e(TAG, "手机MacAddress：Google限制了无法得到真实地址" + getMacAddress(ctx));
        Log.e(TAG, "手机Cpu：" + getCpuInfo());
        Log.e(TAG, "手机硬件序列号：" + getDeviceSerial());
        Log.e(TAG, "手机主板：" + getSystemBOARD());
    }
}
