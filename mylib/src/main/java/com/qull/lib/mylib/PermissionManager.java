package com.qull.lib.mylib;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.checker.DoubleChecker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 Dangerous permissions and permission groups.

PermissionGroup	    Permissions
CALENDAR            READ_CALENDAR, WRITE_CALENDAR
CAMERA              CAMERA
CONTACTS            READ_CONTACTS, WRITE_CONTACTS, GET_ACCOUNTS
LOCATION            ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION
MICROPHONE          RECORD_AUDIO
PHONE               READ_PHONE_STATE, READ_PHONE_NUMBERS, CALL_PHONE, ANSWER_PHONE_CALLS,
                    READ_CALL_LOG, WRITE_CALL_LOG, ADD_VOICEMAIL, USE_SIP, PROCESS_OUTGOING_CALLS
SENSORS             BODY_SENSORS
SMS                 SEND_SMS, RECEIVE_SMS, READ_SMS, RECEIVE_WAP_PUSH, RECEIVE_MMS
STORAGE             READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE
 */

/**
 * Copyright (c) 2018, 数字多⽹网络技术有限公司 All rights reserved.
 * File Name:PermissionManager
 * Version:V1.0
 * Author:qulonglong
 * Date:2018/12/5
 */



//权限申请管理
public class PermissionManager {
   //9组危险权限
   public final static int RequestCodeContacts = 101;//CONTACTS
   public final static int RequestCodePhone = 102;//PHONE
   public final static int RequestCodeCalendar = 103;//CALENDAR
   public final static int RequestCodeCAMERA = 104;//CAMERA
   public final static int RequestCodeSensors = 105;//SENSORS
   public final static int RequestCodeLocation = 106;//LOCATION
   public final static int RequestCodeStorage = 107;//STORAGE
   public final static int RequestCodeMicrophone = 108;//MICROPHONE
   public final static int RequestCodeSMS = 109;//SMS
   public final static int RequestCodeInstallPackage = 200;//INSTALLPACKAGE

   static private PermissionManager mInstance;
   private ArrayList<ListenerInfo> listeners = new ArrayList<>();
   private DoubleChecker doubleChecker = new DoubleChecker();


   static public PermissionManager sharedInstance() {
       if (mInstance == null) {
           mInstance = new PermissionManager();
       }

       return mInstance;
   }

   protected PermissionManager() {

   }

   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                          int[] grantResults) {
       boolean granted = true;
       for (int i = 0; i < grantResults.length; i++) {
           granted &= (grantResults[i] == PackageManager.PERMISSION_GRANTED);
       }

       if (granted) {
           this.getListener(requestCode).onGranted(requestCode);//都通过才算通过
           this.removeListener(requestCode);
       } else {
           this.getListener(requestCode).onDenied(requestCode);//拒绝
           this.removeListener(requestCode);
       }
   }

   public void requestPermissions(final Activity activity, String[] permissions, final int requestCode, final Listener listener) {
       this.addListener(requestCode, listener);

       if (activity != null) {
           AndPermission.with(activity)
                   .permission(permissions)
                   .onGranted(new Action() {
                       @Override
                       public void onAction(List<String> permissions) {
                           checkPermissions(activity, permissions, requestCode, listener);
                       }
                   }).onDenied(new Action() {
               @Override
               public void onAction(List<String> permissions) {
                   checkPermissions(activity, permissions, requestCode, listener);
               }
           }).rationale(new Rationale() {
               @Override
               public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {
                   System.out.println("rationale permissions: " + permissions.toString());
                   showRationaleDialog(activity, permissions, executor);
               }
           }).start();
       }
   }

   //为了兼容国内手机，需要二次检查
   private void checkPermissions(final Activity activity, List<String> permissions, final int requestCode, Listener listener) {
       if (doubleChecker.hasPermission(activity, permissions)) {
           PermissionManager.this.getListener(requestCode).onGranted(requestCode);//通过
           PermissionManager.this.removeListener(requestCode);
           System.out.println("grant permissions: " + permissions.toString());
       } else {
           if (AndPermission.hasAlwaysDeniedPermission(activity, permissions)) {
               PermissionManager.this.getListener(requestCode).onAlwaysDenied(requestCode, permissions);//拒绝
               PermissionManager.this.removeListener(requestCode);
               System.out.println("alwaysDenied permissions: " + permissions.toString());
           } else {
               PermissionManager.this.getListener(requestCode).onDenied(requestCode);//拒绝
               PermissionManager.this.removeListener(requestCode);
               System.out.println("deny permissions: " + permissions.toString());
           }
       }
   }

   //永久拒绝，打开设置
   public void showAlwaysDeniedDialog(final Activity activity, List<String> permissions) {
       new AlertDialog.Builder(activity)
               .setMessage("您拒绝了" + getDescription(permissions) + "权限，此功能需要授权才可以使用")
               .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       SettingsUtils.gotoPermissionSetting(activity.getApplication(),activity);
                   }
               }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {

           }
       }).show();
   }

   //非永久拒绝，重新申请
   private void showRationaleDialog(final Activity activity, List<String> permissions, final RequestExecutor executor) {
       new AlertDialog.Builder(activity)
               .setMessage("您拒绝了" + getDescription(permissions) + "权限，此功能需要授权才可以使用")
               .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       executor.execute();
                   }
               }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               executor.cancel();
           }
       }).show();
   }

   private String getDescription(final List<String> permissions) {
       String desc = "相关";
       Set<String> nameSet = new HashSet<>();

       for (String permission : permissions) {
           if (permission.equalsIgnoreCase(Manifest.permission.READ_CONTACTS) ||
                   permission.equalsIgnoreCase(Manifest.permission.WRITE_CONTACTS) ||
                   permission.equalsIgnoreCase(Manifest.permission.GET_ACCOUNTS)) {
               nameSet.add("通讯录");
           } else if (permission.equalsIgnoreCase(Manifest.permission.READ_PHONE_STATE) ||
                   permission.equalsIgnoreCase(Manifest.permission.CALL_PHONE) ||
                   permission.equalsIgnoreCase(Manifest.permission.READ_CALL_LOG) ||
                   permission.equalsIgnoreCase(Manifest.permission.WRITE_CALL_LOG) ||
                   permission.equalsIgnoreCase(Manifest.permission.ADD_VOICEMAIL) ||
                   permission.equalsIgnoreCase(Manifest.permission.USE_SIP) ||
                   permission.equalsIgnoreCase(Manifest.permission.PROCESS_OUTGOING_CALLS)) {
               nameSet.add("电话");
           } else if (permission.equalsIgnoreCase(Manifest.permission.RECORD_AUDIO)) {
               nameSet.add("麦克风");
           } else if (permission.equalsIgnoreCase(Manifest.permission.BODY_SENSORS)) {
               nameSet.add("传感器");
           } else if (permission.equalsIgnoreCase(Manifest.permission.CAMERA)) {
               nameSet.add("相机");
           } else if (permission.equalsIgnoreCase(Manifest.permission.READ_CALENDAR) ||
                   permission.equalsIgnoreCase(Manifest.permission.WRITE_CALENDAR)) {
               nameSet.add("日历");
           } else if (permission.equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION) ||
                   permission.equalsIgnoreCase(Manifest.permission.ACCESS_COARSE_LOCATION)) {
               nameSet.add("定位");
           } else if (permission.equalsIgnoreCase(Manifest.permission.READ_EXTERNAL_STORAGE) ||
                   permission.equalsIgnoreCase(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
               nameSet.add("存储");
           } else if (permission.equalsIgnoreCase(Manifest.permission.SEND_SMS) ||
                   permission.equalsIgnoreCase(Manifest.permission.RECEIVE_SMS) ||
                   permission.equalsIgnoreCase(Manifest.permission.READ_SMS) ||
                   permission.equalsIgnoreCase(Manifest.permission.RECEIVE_WAP_PUSH) ||
                   permission.equalsIgnoreCase(Manifest.permission.RECEIVE_MMS)) {
               nameSet.add("短信");
           } else if (permission.equalsIgnoreCase(Manifest.permission.REQUEST_INSTALL_PACKAGES)) {
               nameSet.add("未知来源安装");
           }
       }

       if (nameSet.isEmpty()) {
           nameSet.add(desc);
       }

       String[] names = new String[nameSet.size()];

       int index = 0;
       for (Object name : nameSet) {
           names[index++] = name.toString();
       }

       desc = join(names, "、");
       return desc;
   }

   private void addListener(int requestCode, Listener listener) {
       ListenerInfo listenerInfo = new ListenerInfo();
       listenerInfo.requestCode = requestCode;
       listenerInfo.listener = listener;
       this.listeners.add(listenerInfo);
   }

   private void removeListener(int requestCode) {
       ListenerInfo listener = getListenerInfo(requestCode);
       this.listeners.remove(listener);
   }

   private ListenerInfo getListenerInfo(int requestCode) {
       ListenerInfo listener = null;
       for (ListenerInfo item : this.listeners) {
           if (item.requestCode == requestCode) {
               listener = item;
               break;
           }
       }
       return listener;
   }
   private Listener getListener(int requestCode) {
       Listener listener = null;
       for (ListenerInfo item : this.listeners) {
           if (item.requestCode == requestCode) {
               listener = item.listener;
               break;
           }
       }
       return listener;
   }

   public interface Listener {
       //通过
       void onGranted(int requestCode);

       //拒绝
       void onDenied(int requestCode);

       //永久拒绝
       void onAlwaysDenied(int requestCode, List<String> permissions);
   }

   private class ListenerInfo {
       public int requestCode;
       public Listener listener;
   }

   private String join(String[] array, String separator) {
       if (array == null || array.length <= 0) {
           return "";
       }

       if (TextUtils.isEmpty(separator)) {
           separator = "";
       }

       StringBuffer sb = new StringBuffer();

       for (int i = 0; i < array.length; i++) {

           sb.append(array[i].trim() + separator);
       }
       String content = sb.toString();
       content = content.substring(0, content.length() - separator.length());

       return content;
   }
}