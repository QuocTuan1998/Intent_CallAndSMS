package com.example.quoctuan.intent_callandsms;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.quoctuan.intent_callandsms.model.Contact;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText editName, editPhone;
    Button btnSave;
    ListView lvContact;
    ArrayList<Contact> listContact = new ArrayList<>();
    ArrayAdapter<Contact> adapterContact = null;
    Contact contact = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvents();
    }

    private void addControls() {
        editName = (EditText) findViewById(R.id.editName);
        editPhone = (EditText) findViewById(R.id.editPhone);
        btnSave = (Button) findViewById(R.id.btnSave);
        lvContact = (ListView) findViewById(R.id.lvContact);
        adapterContact = new ArrayAdapter<Contact>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                listContact);
        lvContact.setAdapter(adapterContact);
//        setup Contextmenu for ListView
        registerForContextMenu(lvContact);
    }

    private void addEvents() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSaveContact();
            }
        });
        lvContact.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                contact = listContact.get(position);
                return false;
            }
        });

    }

    private void handleSaveContact() {
        Contact contact = new Contact();
        contact.setName(editName.getText() + "");
        contact.setPhone(editPhone.getText() + "");
        listContact.add(contact);
        adapterContact.notifyDataSetChanged();
        editName.setText("");
        editPhone.setText("");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.phonecontextmenu, menu);
        menu.setHeaderTitle("Call - SMS");
        menu.getItem(0).setTitle("Call to " + contact.getPhone());
        menu.getItem(1).setTitle("Send Sms to " + contact.getPhone());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemCall:
                doMakeCall();
                break;
            case R.id.itemSend:
                doMakeSms();
                break;
            case R.id.itemRemove:
                doMakeRemove();
        }
        return super.onContextItemSelected(item);
    }

    private void doMakeCall() {
        Uri uri = Uri.parse("tel:" + contact.getPhone());
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    private void doMakeSms() {
        Intent intent = new Intent(this,MySMSActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("CONTACT",contact);
        intent.putExtra("DATA",b);
        startActivity(intent);

    }

    private void doMakeRemove() {
        listContact.remove(contact);
        adapterContact.notifyDataSetChanged();
    }
}
