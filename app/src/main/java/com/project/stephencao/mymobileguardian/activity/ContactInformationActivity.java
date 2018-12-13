package com.project.stephencao.mymobileguardian.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.adapter.ContactPerson;
import com.project.stephencao.mymobileguardian.adapter.MyContactListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ContactInformationActivity extends AppCompatActivity {
    private ListView listView;
    private List<ContactPerson> personList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_information);
        initUI();
        initData();
        MyContactListViewAdapter myContactListViewAdapter = new MyContactListViewAdapter(personList, this);
        listView.setAdapter(myContactListViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               TextView phoneTextView = view.findViewById(R.id.tv_contact_phone);
                Intent intent = new Intent();
                intent.putExtra("phoneNumber",phoneTextView.getText().toString());
                setResult(2,intent);
                finish();
            }
        });
    }

    /**
     * Use contentResolver to get the contact info with phone numbers
     *
     * Some people might have mobile phone number and home phone number, so their name will appear twice in the list
     */
    private void initData() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                new String[]{ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME}, null, null, null);
        if (cursor != null) {
            personList = new ArrayList<>();
            while (cursor.moveToNext()) {
                int _id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                // contact person name
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Cursor cursor1 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                                ContactsContract.CommonDataKinds.Phone.TYPE},
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + _id, null,
                        null);
                if (cursor1 != null) {
                    while (cursor1.moveToNext()) {
                        //Only record info with phone numbers, name might be same, different phone numbers
                        ContactPerson contactPerson = new ContactPerson();
                        contactPerson.setName(name);
                        int type = cursor1.getInt(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        if (type == ContactsContract.CommonDataKinds.Phone.TYPE_HOME) {
                            contactPerson.setPhone(cursor1.getString(cursor1.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER)));
                            personList.add(contactPerson);
                        } else if (type == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                            contactPerson.setPhone(cursor1.getString(cursor1.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER)));
                            personList.add(contactPerson);
                        }
                    }
                }
                cursor1.close();
            }
        }
        cursor.close();
    }

    private void initUI() {
        listView = findViewById(R.id.lv_contact_number_information);
    }

}
