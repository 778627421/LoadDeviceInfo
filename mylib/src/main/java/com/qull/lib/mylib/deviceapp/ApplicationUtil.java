package com.qull.lib.mylib.deviceapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * File Name:
 * Version:V1.0
 * Author:qulonglong
 * Date:2019/1/15
 * 获取所有软件名字
 */

public class ApplicationUtil {
    private static PackageManager pm;
    static Context context;

    private ApplicationUtil(Context context) {

        ApplicationUtil.context = context;
        pm = context.getPackageManager();
    }

    public static ApplicationUtil newInstance(Context context) {

        return new ApplicationUtil(context);
    }

    /**
     * 获取所有软件名字 包名 首页名字
     **/
    public List<AppInfo> loadAllApp() {
        List<AppInfo> apps = new ArrayList<AppInfo>();
        Intent main = new Intent(Intent.ACTION_MAIN, null);
        main.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolves = pm.queryIntentActivities(main, 0);
        Collections.sort(resolves, new ResolveInfo.DisplayNameComparator(pm));
        for (int i = 0; i < resolves.size(); i++) {
            AppInfo vo = new AppInfo();
            ResolveInfo info = resolves.get(i);
            String label = info.loadLabel(pm).toString().trim();//应用名字，
//            Drawable icon = info.activityInfo.loadIcon(pm);//应用图标
            String pkgName = info.activityInfo.applicationInfo.packageName;//应用包名
            String activityName = info.activityInfo.name;//应用首页名字
            vo.activityName = activityName;
            vo.appName = label;
            vo.appPackage = pkgName;
            apps.add(vo);
//            Intent intent = new Intent();
//            intent.setClassName(pkgName, activityName);
//            apps.add(app);
//            app.setLabel(label);
//            app.setIcon(icon);
//            app.setClassName(activityName);
//            app.setPackageName(pkgName);
//            app.setIntent(intent);
//            app.setChecked(false);
            System.out.println(label + "/" + pkgName + "/" + activityName);
        }
        return apps;
    }

    /**
     * 包名跳转
     **/
    public void startApp(String pkgName) {
        Intent intent = pm.getLaunchIntentForPackage(pkgName);
        if (intent != null) {
            context.startActivity(intent);
        }
    }
}
