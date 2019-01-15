package com.qull.lib.mylib.phonesms;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Copyright (c) 2018, 数字多⽹网络技术有限公司 All rights reserved.
 * File Name:
 * Version:V1.0
 * Author:qulonglong
 * Date:2019/1/15
 * 读取短信
 */

public class Readsms {

    private Uri SMS= Uri.parse("content://sms/");
    public void getSmsFromPhone(Context context) {
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[] { "_id", "address", "person","body","date" ,"service_center"};//"_id", "address", "person",, "date", "type
        Cursor cur = cr.query(SMS, projection, null, null, "date desc");
        if (null == cur)
            return;

        System.out.print("#############################");
        while (cur.moveToNext()) {
            System.out.print(cur.toString());
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));
            String service_center = cur.getString(cur.getColumnIndex("service_center"));
            //这里我是要获取自己短信服务号码中的验证码~~
//            Pattern pattern = Pattern.compile(" [a-zA-Z0-9]{10}");
//            Matcher matcher = pattern.matcher(body);
//            if (matcher.find()) {
//                String res = matcher.group().substring(1, 11);
//                TextView.setText(res);
//            }
            System.out.println("ReadMsg>>"+number  +"/service_center"+service_center+ "/"+ name + "/" + body);
        }
        System.out.print("#############################");
        cur.close();
    }

}
