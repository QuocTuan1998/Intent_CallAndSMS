package com.example.quoctuan.intent_callandsms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quoctuan.intent_callandsms.model.Contact;

import java.util.ArrayList;

public class MySMSActivity extends Activity {
    EditText editWrite;
    TextView txtSendTo;
    Button btnSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sms);

        editWrite = (EditText) findViewById(R.id.editWrite);
        txtSendTo = (TextView) findViewById(R.id.txtSendTo);
        btnSend = (Button) findViewById(R.id.btnSend);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("DATA");
        final Contact c = (Contact) bundle.getSerializable("CONTACT");
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSend(c);
            }
        });
        txtSendTo.setText("send to : " + c.getPhone());
    }

    private void handleSend(Contact c){

        final SmsManager sms=SmsManager.getDefault();
        Intent intentSms=new Intent("ACTION_MSG_SENT");
        final PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,intentSms,0);
        registerReceiver(new BroadcastReceiver(){
            @Override
                    public void onReceive(Context context,Intent intent){
                int result=getResultCode();
                String msg="Đã gửi thành công";
                if(result!=Activity.RESULT_OK){
                    msg="Gửi tin thất bại";
                }
                Toast.makeText(MySMSActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        },new IntentFilter("ACTION_MSG_SENT"));

        sms.sendTextMessage(c.getPhone(),
                null,
                editWrite.getText()+"",
                pendingIntent,null
        );
        finish();
    }
}
