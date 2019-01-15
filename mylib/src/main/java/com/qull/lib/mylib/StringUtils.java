package com.qull.lib.mylib;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Copyright (c) 2018, 数字多⽹网络技术有限公司 All rights reserved.
 * File Name:StringUtils
 * Version:V1.0
 * Author:qulonglong
 * Date:2018/12/5
 */


public class StringUtils {
   //只检测位数
   public static boolean isValidUserName(String username) {
       if (username == null || username.length() < 5) {
           return false;
       }

       return true;
//        //5-25位字母数字组合
//        String usernamePattern = "(?!^\\d+$)^\\w{5,25}$";
//        Pattern pattern = Pattern.compile(usernamePattern);
//        boolean ret = pattern.matcher(username).matches();
//        return ret;
   }

   //只检测位数
   public static boolean isValidPassword(String password) {
       password = password.trim();
       if (password == null || password.length() < 6) {
           return false;
       }

       return true;

       //6-20位字母数字组合
//        String pwdPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]{6,20})$";
//        Pattern pattern = Pattern.compile(pwdPattern);
//        boolean ret = pattern.matcher(password).matches();
//
//        return ret;
   }

   public static boolean isValidPhoneNum(String phonenum) {
       //1开头11位数字

       String phonenumPattern = "^1\\d{10}$";
       Pattern pattern = Pattern.compile(phonenumPattern);
       boolean ret = pattern.matcher(phonenum).matches();

       return ret;
   }

   public static boolean isValidMailaddress(String mailaddress) {

       String mailPattern = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
       Pattern pattern = Pattern.compile(mailPattern);
       boolean ret = pattern.matcher(mailaddress).matches();

       return ret;
   }

   //是否正确的钱数, 只允许保留小数点两位
   public static boolean isValidMoneyNumber(String money) {

       String mailPattern = "^[0-9]+(.[0-9]{0,2})?$";
       Pattern pattern = Pattern.compile(mailPattern);
       boolean ret = pattern.matcher(money).matches();

       return ret;
   }

   public static boolean isValidVerifyCode(String verifyCode) {
       //验证码6位数字

       String verifyCodePattern = "^\\d{6}$";
       Pattern pattern = Pattern.compile(verifyCodePattern);
       boolean ret = pattern.matcher(verifyCode).matches();

       return ret;
   }

   public static boolean isValidPicVerifyCode(String verifyCode) {
       //图形验证码5位数字

       String verifyCodePattern = "^\\d{5}$";
       Pattern pattern = Pattern.compile(verifyCodePattern);
       boolean ret = pattern.matcher(verifyCode).matches();

       return ret;
   }

   public static boolean isValidFeedback(String feedback) {
       String verifyCodePattern = "^([\u4E00-\u9FA5，。？：；’‘！“、～｀]|[A-Za-z0-9_,.:;'\"?!~`@]|[\\s])+$";
       Pattern pattern = Pattern.compile(verifyCodePattern);
       boolean ret = pattern.matcher(feedback).matches();

       return ret;
   }

   // 万
   public static String stringForMoney(String money) {
       String formatMoney;
       double value = StringUtils.valueOf(money);
       if (value >= 10000) {
           NumberFormat nf = new DecimalFormat("0.##万元");
           nf.setRoundingMode(RoundingMode.FLOOR);
           formatMoney = nf.format(value / 10000);
       } else {
           NumberFormat nf = new DecimalFormat("0.##元");
           formatMoney = nf.format(value);
       }
       return formatMoney;
   }

   // 万  (元改分后的调用公式) money 分
   public static String string4Money(int money) {
       double value = money / 100.00;
       return stringForMoney(String.valueOf(value));
   }

   // 万  (元改分后的调用公式)
   public static String str4Money(String money) {
       double value = 0.0;
       try {
           value = Integer.valueOf(money) / 100.00;
       } catch (Exception e) {

       }

       return stringForMoney(String.valueOf(value));
   }

   // 万  强制保留两位小数
   public static String stringForMoney2Point(String money) {
       String formatMoney;
       double value = StringUtils.valueOf(money);
       if (value >= 10000) {
           NumberFormat nf = new DecimalFormat("0.00万元");
           formatMoney = nf.format(value / 10000);
       } else {
           NumberFormat nf = new DecimalFormat("0.00元");
           formatMoney = nf.format(value);
       }
       return formatMoney;
   }

   // 1,245.00元
   public static String stringForMoneyYuan(String money) {
       String formatMoney;
       double value = StringUtils.valueOf(money);

       NumberFormat nf = new DecimalFormat("#,##0.0#");
       formatMoney = nf.format(value);

       return formatMoney + "元";
   }

   // 1,245.00元  (传入的值是分为单位)
   public static String fenForMoneyYuan(String money) {
       String formatMoney;
       double value = StringUtils.intToDouble(money);

       NumberFormat nf = new DecimalFormat("#,##0.00");
       formatMoney = nf.format(value);

       return formatMoney + "元";
   }

   // 1,245.00元  (传入的值是分为单位)
   public static String fenForMoneyYuan2(String money) {
       String formatMoney;
       double value = StringUtils.intToDouble(money);

       NumberFormat nf = new DecimalFormat("#,##0.##");
       formatMoney = nf.format(value);

       return formatMoney + "元";
   }

   //保留两位小数
   public static String double2Point(double money) {
       String formatMoney;
       NumberFormat nf = new DecimalFormat("##0.00");
       formatMoney = nf.format(money);

       return formatMoney;
   }

   public static String String2Point(String money) {
       String formatMoney;
       NumberFormat nf = new DecimalFormat("##0.00");
       formatMoney = nf.format(money);

       return formatMoney;
   }

   public static String forMatPercent(String money) {
       String formatMoney;
       double value = StringUtils.valueOf(money);

       NumberFormat nf = new DecimalFormat("##0.##");
       formatMoney = nf.format(value);

       return formatMoney + "%";
   }

   //23,423,459
   public static String forMatNumber(String money) {
       String formatMoney;
       double value = StringUtils.valueOf(money);

       NumberFormat nf = new DecimalFormat("#,##0.##");
       formatMoney = nf.format(value);

       return formatMoney;
   }

   //传入后台的钱 格式化为两位小数，例如 提现，充值，
   public static String forMatMoney(String money) {
       String formatMoney;
       double value = StringUtils.valueOf(money);

       DecimalFormat nf = new DecimalFormat();
       nf.setMaximumFractionDigits(2);
       nf.setMinimumFractionDigits(2);
       nf.setGroupingSize(0);
       nf.setRoundingMode(RoundingMode.FLOOR);
       formatMoney = nf.format(value);

       return formatMoney;
   }

   // ¥ 1,245.00
   public static String stringForFormatMoney(String money) {
       String formatMoney;
       double value = StringUtils.valueOf(money);

       NumberFormat nf = new DecimalFormat("#,##0.0#");
       formatMoney = nf.format(value);

       return "￥" + formatMoney;
   }

   public static String stringFormatMoney2(String money) {
       String formatMoney;
       double value = StringUtils.valueOf(money);

       NumberFormat nf = new DecimalFormat("#,##0.0#");
       formatMoney = nf.format(value);

       return formatMoney;
   }

   //单位是分-- 转元
   public static String fenFormatMoney(String money) {
       String formatMoney;
       double value = StringUtils.intToDouble(money);

       NumberFormat nf = new DecimalFormat("#,##0.##");
       formatMoney = nf.format(value);

       return formatMoney;
   }

   //分转元  保留小数点
   public static String fen2yuan2point(String money) {
       String formatMoney;
       double value = StringUtils.intToDouble(money);

       NumberFormat nf = new DecimalFormat("#,##0.00");
       formatMoney = nf.format(value);

       return formatMoney;
   }

   //123,435,23.00
   // 只能用作展示不能用作计算(精度会损失)
   public static String FormatDouble(double money) {
       String formatMoney;
       double value = StringUtils.valueOfToPoint(money + 0.00000001);

       NumberFormat nf = new DecimalFormat("#,##0.##");
       formatMoney = nf.format(value);

       return formatMoney;
   }

   //单位是分-- 转元
   public static String fenIntFormatMoney(int money) {
       String formatMoney;
       double value1 = 0;
       try {
           value1 = money / 100.00;
       } catch (Exception e) {
       }
       double value2 = valueOfToPoint(value1 + 0.00000001);
       NumberFormat nf = new DecimalFormat("#,##0.0#");
       formatMoney = nf.format(value2);

       return formatMoney;
   }

   //分--元  123243  格式
   public static String formatFenMoney2(String money) {
       String formatMoney;
       double value = StringUtils.intToDouble(money);

       NumberFormat nf = new DecimalFormat("0.##");
       formatMoney = nf.format(value);

       return formatMoney;
   }

   public static String formatMoney2(String money) {
       String formatMoney;
       double value = StringUtils.valueOf(money);

       NumberFormat nf = new DecimalFormat("0.##");
       formatMoney = nf.format(value);

       return formatMoney;
   }

   public static String formatDoubleMoney(Double money) {
       String formatMoney;
       NumberFormat nf = new DecimalFormat("0.##");
       formatMoney = nf.format(money);

       return formatMoney;
   }

   public static String stringFormatMoneyPoint(String money) {
       String formatMoney;
       double value = StringUtils.valueOf(money);

       NumberFormat nf = new DecimalFormat("#,##0.0#");
       formatMoney = nf.format(value);

       return formatMoney;
   }

   public static String stringFormatMoney2Point(String money) {
       String formatMoney;
       double value = StringUtils.valueOf(money);

       NumberFormat nf = new DecimalFormat("#,##0.00");
       formatMoney = nf.format(value);

       return formatMoney;
   }

   public static String stringFormatMoneyPointYuan(String money) {
       String formatMoney;
       double value = StringUtils.valueOf(money);

       NumberFormat nf = new DecimalFormat("#,##0.0#");
       formatMoney = nf.format(value);

       return formatMoney + "元";
   }

   public static String stringFormatMoney2PointYuan(String money) {
       String formatMoney;
       double value = StringUtils.valueOf(money);

       NumberFormat nf = new DecimalFormat("#,##0.00");
       formatMoney = nf.format(value);

       return formatMoney + "元";
   }

   //单位 分
   public static int valueOfInt(String string) {
       int intValue = 0;
       try {
           intValue = Integer.valueOf(string);
       } catch (Exception e) {
       }

       return intValue;
   }

   //分->元
   public static double intToDouble(String string) {
       double value = 0;
       try {
           value = Integer.valueOf(string) / 100.00;
       } catch (Exception e) {
       }

       return valueOfToPoint(value + 0.00000001);
   }

   //分->元
   public static double intToDoubleNum(int string) {
       double value = 0;
       try {
           value = string / 100.00;
       } catch (Exception e) {
       }

       return valueOfToPoint(value + 0.00000001);
   }

   // 分->万元  强制保留两位小数
   public static double intToDoubleNum2(int money) {

       double value = 0;
       try {
           if (money > 10000000) {//10万以上显示万元
               value = money / 1000000.00;
           } else {
               value = money / 100.00;
           }
       } catch (Exception e) {
       }

       return valueOfToPoint(value + 0.00000001);
   }

   public static double valueOf(String string) {
       double doubleValue = 0.0;
       try {
           doubleValue = Double.valueOf(string);
       } catch (Exception e) {
       }

       return valueOfToPoint(doubleValue + 0.00000001);//加0.0000000001补偿精度损失
   }

   public static double truncateValue(double value) {
       BigDecimal bd = new BigDecimal(String.valueOf(value));
       BigDecimal scale = bd.setScale(2, bd.ROUND_DOWN);
       double result = scale.doubleValue();
       return result;
   }

   //保留两位小数点，不四舍五入
   public static double valueOfToPoint(double doub) {
       double doubleValue = 0.0;
       try {
           NumberFormat ddf1 = NumberFormat.getNumberInstance();
           ddf1.setMaximumFractionDigits(2);
           ddf1.setRoundingMode(RoundingMode.FLOOR);
           String strResult = ddf1.format(doub);
           strResult = strResult.replace(",", "");
           doubleValue = Double.parseDouble(strResult);
       } catch (Exception e) {
           e.printStackTrace();
       }
       return doubleValue;
   }

   public static String valueDetele_dot(String string) {
       if (TextUtils.isEmpty(string)) {
           return "";
       }

       int index = string.indexOf(".");
       if (index > 0) {
           try {
               return string.substring(0, index);
           } catch (Exception e) {
               e.printStackTrace();
           }
       }

       return string;
   }

   public static String dataFormateEnd(String dateString) {
       //2015-10-29 10:00:00
       if (TextUtils.isEmpty(dateString)) {
           return "";
       }
       // String s_nd = dateString.substring(0, 4) + "年"; // 年份
       // String s_yf = dateString.substring(5, 7) + "月"; // 月份
       // String s_rq = dateString.substring(8, 10) + "日"; // 日期
       String[] oriagal = dateString.split(" ");
       String[] data = oriagal[0].split("-");

       return data[1] + "月" + data[2] + "日";//10月29日
//        return s_yf + s_rq;//10月29日
   }

   public static String dataFormateBegin(String dateString) {
       //2015-10-29 10:00:00
       if (TextUtils.isEmpty(dateString)) {
           return "";
       }
       String[] oriagal = dateString.split(" ");
       String[] data = oriagal[0].split("-");

       return data[1] + "月" + data[2] + "日-" + oriagal[1];//10月29日－10:00:00

//        String s_yf = dateString.substring(5, 7) + "月"; // 月份
//        String s_rq = dateString.substring(8, 10) + "日"; // 日期
//        String s_hu = dateString.substring(11);
//        return s_yf + s_rq+"-"+s_hu;//10月29日－10:00:00
   }

   public static String getDataForMateDay() {
       //20161029
       SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
       Date curDate = new Date(System.currentTimeMillis());//获取当前时间
       return formatter.format(curDate);
   }

   //判断是否下载了最新app
   public static boolean fileIsExists() {
       try {
           File f = new File(Environment.getExternalStorageDirectory() + "updata/tuotiansudai_upgrade.apk");
           if (!f.exists()) {
               return false;
           }

       } catch (Exception e) {
           return false;
       }
       return true;
   }


   public static String concatPath(String basePath, String path) {
       if (basePath.endsWith("/") && path.startsWith("/")) {
           return basePath + path.substring(1);
       } else if (!basePath.endsWith("/") && !path.startsWith("/")) {
           return basePath + "/" + path;
       }

       return basePath + path;
   }

   //拼接http地址
   public static String concatUrl(String baseUrl, String url) {
       String fullUrl;
       try {
           //
           if (url.startsWith("http")) {
               fullUrl = url;
           } else if (baseUrl.endsWith("/") &&
                   url.startsWith("/")) {
               fullUrl = baseUrl + url.substring(1, url.length());
           } else if (!baseUrl.endsWith("/") &&
                   !url.startsWith("/")) {
               fullUrl = baseUrl + "/" + url;
           } else {
               fullUrl = baseUrl + url;
           }
       } catch (Exception e) {
           fullUrl = "";
       }

       return fullUrl;
   }

   //拼接http地址
   public static String fullUrl(String baseUrl,String url) {
       String fullUrl;
       try {
           //
           if (url.startsWith("http")) {
               fullUrl = url;
           } else if (baseUrl.endsWith("/") &&
                   url.startsWith("/")) {
               fullUrl = baseUrl + url.substring(1, url.length());
           } else if (!baseUrl.endsWith("/") &&
                   !url.startsWith("/")) {
               fullUrl = baseUrl + "/" + url;
           } else {
               fullUrl = baseUrl + url;
           }
       } catch (Exception e) {
           fullUrl = "";
       }

       return fullUrl;
   }

   //拼接http地址
   public static String fullApiUrl(String baseUrl,String url) {
       String fullUrl;
       try {
           //
           if (url.startsWith("http")) {
               fullUrl = url;
           } else if (baseUrl.endsWith("/") &&
                   url.startsWith("/")) {
               fullUrl = baseUrl + url.substring(1, url.length());
           } else if (!baseUrl.endsWith("/") &&
                   !url.startsWith("/")) {
               fullUrl = baseUrl + "/" + url;
           } else {
               fullUrl = baseUrl + url;
           }
       } catch (Exception e) {
           fullUrl = "";
       }

       return fullUrl;
   }

   //字符串数组转字符串
   public static String join(List<String> list, String seperator) {
       StringBuilder sb = new StringBuilder();
       for (int i = 0; i < list.size() - 1; i++) {
           sb.append(list.get(i));
           sb.append(seperator);
       }
       if (list.size() - 1 >= 0) {
           sb.append(list.get(list.size() - 1));
       }

       return sb.toString();
   }

//    public static String addUserIdToSharedUrl(String url) {
//        if (!UserAccountVO.sharedInstance().isLogin()) {
//            return url;
//        }
//
//        url = addParamToUrl(url, "loginName", UserAccountVO.sharedInstance().getPersonalInfo().userId);
//        return url;
//    }

   public static String addParamToUrl(String url, String key, String value) {
       if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
           return url;
       }

       StringBuilder builder = new StringBuilder();
       builder.append(url);
       if (url.contains("?")) {
           builder.append("&");
       } else {
           builder.append("?");
       }

       builder.append(key);
       builder.append("=");
       builder.append(value);

       return builder.toString();
   }

   //防止暴力点击
   private static long lastClickTime = 0;

   public static boolean isFastDoubleClick() {
       long time = System.currentTimeMillis();
       long timeD = time - lastClickTime;
       if (0 < timeD && timeD < 2000) {
//            lastClickTime = time;
           return true;
       }
       lastClickTime = time;
       return false;
   }

   public static String toMD5String(String string) {
       char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
               , 'a', 'b', 'c', 'd', 'e', 'f'};
       try {
           byte[] arrBytes = string.getBytes();
           MessageDigest msgDigest = MessageDigest.getInstance("MD5");
           msgDigest.update(arrBytes);

           byte[] arrDigest = msgDigest.digest();
           char result[] = new char[arrDigest.length * 2];
           int k = 0;
           for (int i = 0; i < arrDigest.length; i++) {
               byte value = arrDigest[i];
               result[k++] = hexDigits[value >>> 4 & 0xf];
               result[k++] = hexDigits[value & 0xf];
           }
           return new String(result);
       } catch (Exception e) {
           return null;
       }
   }

   public static Boolean notEmpty(String text) {
       return !TextUtils.isEmpty(text);
   }

}
