package com.qull.lib.loaddeviceinfo;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.qull.lib.mylib.PermissionManager;
import com.qull.lib.mylib.phonesms.ContactInfo;
import com.qull.lib.mylib.phonesms.PhoneBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                List<AppInfo> lists= ApplicationUtil.newInstance(MainActivity.this).loadAllApp();
                PermissionManager.sharedInstance().requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.GET_ACCOUNTS}, PermissionManager.RequestCodeContacts, new PermissionManager.Listener() {
                    @Override
                    public void onGranted(int requestCode) {
//                        doGetContacts();
                        getSystemContactInfos(MainActivity.this);
//                        ReadPhoneNumberUtils.readContact(MainActivity.this);
                    }

                    @Override
                    public void onDenied(int requestCode) {

                    }

                    @Override
                    public void onAlwaysDenied(int requestCode, List<String> permissions) {

                    }
                });
//                PermissionManager.sharedInstance().requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, PermissionManager.RequestCodeSMS, new PermissionManager.Listener() {
//                    @Override
//                    public void onGranted(int requestCode) {
//                        Readsms readsms=new Readsms();
//                        readsms.getSmsFromPhone(MainActivity.this);
//                    }
//
//                    @Override
//                    public void onDenied(int requestCode) {
//
//                    }
//
//                    @Override
//                    public void onAlwaysDenied(int requestCode, List<String> permissions) {
//
//                    }
//                });

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private ArrayList<HashMap<String, String>> readContact() {
        // 首先,从raw_contacts中读取联系人的id("contact_id")
        // 其次, 根据contact_id从data表中查询出相应的电话号码和联系人名称
        // 然后,根据mimetype来区分哪个是联系人,哪个是电话号码

        Uri rawContactsUri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");

        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        // 从raw_contacts中读取所有联系人的id("contact_id")
        Cursor rawContactsCursor = getContentResolver().query(rawContactsUri,
                new String[]{"contact_id"}, null, null, null);
        System.out.print("***************MMMMMMMM********************");
        if (rawContactsCursor != null) {
            while (rawContactsCursor.moveToNext()) {
                String contactId = rawContactsCursor.getString(0);
                // System.out.println("得到的contact_id="+contactId);

                // 根据contact_id从data表中查询出相应的电话号码和联系人名称, 实际上查询的是视图view_data
                Cursor dataCursor = getContentResolver().query(dataUri,
                        new String[]{"data1", "mimetype"}, "contact_id=?",
                        new String[]{contactId}, null);

                if (dataCursor != null) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    while (dataCursor.moveToNext()) {
                        String data1 = dataCursor.getString(0);
                        String mimetype = dataCursor.getString(1);
                        System.out.print("data1==" + data1 + "==mimetype===" + mimetype);
                        if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {//手机号码
                            map.put("phone", data1);
                        } else if ("vnd.android.cursor.item/name".equals(mimetype)) {//联系人名字
                            map.put("name", data1);
                        }
                    }
                    list.add(map);
                    dataCursor.close();
                }
            }
            rawContactsCursor.close();
        }
        return list;
    }

    public List<ContactInfo> getSystemContactInfos(Context mContext) {
        List<ContactInfo> infos = new ArrayList<ContactInfo>();

        // 使用ContentResolver查找联系人数据
        Cursor cursor = mContext.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null,
                null, null);
        System.out.print("========getSystemContactInfos===================");
        if (cursor==null){
            return null;
        }
        // 遍历查询结果，获取系统中所有联系人
        while (cursor.moveToNext()) {
            ContactInfo info = new ContactInfo();
            // 获取联系人ID
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            // 获取联系人的名字
            String name = cursor.getString(cursor.getColumnIndex(
                    ContactsContract.Contacts.DISPLAY_NAME));
            info.setContactName(name);
            System.out.print(name);
            // 使用ContentResolver查找联系人的电话号码
            Cursor phones = mContext.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                            + " = " + contactId, null, null);
            if (phones != null) {
                // 遍历查询结果，获取该联系人的多个电话号码
                while (phones.moveToNext()) {
                    // 获取查询结果中电话号码列中数据。
                    String phoneNumber = phones.getString(phones
                            .getColumnIndex(ContactsContract
                                    .CommonDataKinds.Phone.NUMBER));
                    info.setPhoneNumber(phoneNumber);
                    System.out.print(phoneNumber);
                }
                phones.close();
            }
            infos.add(info);
            info = null;
        }
        cursor.close();

        return infos;
    }
    private void doGetContacts() {
        PhoneBean VO = new PhoneBean();
        VO.contacts = new ArrayList<>();
        VO.status = 0;
        Log.d("rttttt","contacts---:begin");
        //获取通讯录
        try {
            Uri contactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            Cursor cursor = this.getContentResolver().query(contactUri,
                    new String[]{"display_name", "sort_key", "contact_id", "data1"},
                    null, null, "sort_key");
            String contactName;
            String contactNumber;
            int contactId;

            while (cursor.moveToNext()) {
                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactId = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

                PhoneBean.ContactVO contact = new PhoneBean.ContactVO();
                VO.contacts.add(contact);
                contact.name = contactName;
                contact.phone = contactNumber;
            }
            cursor.close();//使用完后一定要将cursor关闭，不然会造成内存泄露等问题

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        Gson gson = new Gson();
        String content = gson.toJson(VO);
        Log.d("rttttt","contacts---:" + content);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
