package com.qull.lib.mylib.phonesms;

import java.util.ArrayList;


public class PhoneBean {

    public int status;//0:同意,1:拒绝
    public ArrayList<ContactVO> contacts;

    public static class ContactVO {
        public String name;
        public String phone;
    }
}
