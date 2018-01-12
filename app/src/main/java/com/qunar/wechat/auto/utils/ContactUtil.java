package com.qunar.wechat.auto.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Created by lihaibin.li on 2017/11/21.
 */

public class ContactUtil {
    public static void insertContact(Context context, String contacts) {
        if (contacts == null) return;
        String[] phoneNumbers = contacts.split("\n");
        for (String phoneNumber : phoneNumbers) {
            // 创建一个空的ContentValues
            ContentValues values = new ContentValues();

            // 向RawContacts.CONTENT_URI空值插入，
            // 先获取Android系统返回的rawContactId
            // 后面要基于此id插入值
            Uri rawContactUri = context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
            long rawContactId = ContentUris.parseId(rawContactUri);
            values.clear();

//        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
//        // 内容类型
//        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
//        // 联系人名字
//        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
//        // 向联系人URI添加联系人名字
//        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
//        values.clear();

            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            // 联系人的电话号码
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
            // 电话类型
            values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            // 向联系人电话号码URI添加电话号码
            context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
            values.clear();
        }


    }
}
