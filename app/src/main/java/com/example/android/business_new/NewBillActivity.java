package com.example.android.business_new;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Ref;

public class NewBillActivity extends AppCompatActivity {
    private EditText billNumber;
    private EditText billDate;
    private EditText rateperitem;
    private EditText totAmt;
    private EditText dueamt;

    private String billreceivedUid;
    private String receivedUid;
    private String receivedBillNum;
    private String receivedBillDate;
    private String receivedBillRate;
    private double receivedTotamt;
    private double receivedDueamt;

    private int flag;

    private CollectionReference shopListRef=FirebaseFirestore.getInstance().collection("shops");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bill);

        setTitle("Add Bill");

        billNumber = findViewById(R.id.edit_bill_number);
        billDate = findViewById(R.id.edit_bill_date);
        rateperitem = findViewById(R.id.RateperItem);
        totAmt = findViewById(R.id.totamt_editText);
        dueamt =findViewById(R.id.dueamt_editText);

        Bundle extras = getIntent().getExtras();
        receivedUid = extras.getString("mUid");
        receivedBillNum = extras.getString("billnumber");
        if(receivedBillNum.equalsIgnoreCase("x")){
            flag =0;
            billNumber.setText(null);
            billDate.setText(null);
            rateperitem.setText(null);
            totAmt.setText(null);
            dueamt.setText(null);
        }else{
            flag = 1;
            billreceivedUid = extras.getString("mUid2");
            receivedBillDate = extras.getString("billdate");
            receivedBillRate = extras.getString("rateperitem");
            receivedTotamt = extras.getDouble("totalamt");
            receivedDueamt = extras.getDouble("dueamt");
            billNumber.setText(receivedBillNum);
            billDate.setText(receivedBillDate);
            rateperitem.setText(receivedBillRate);
            totAmt.setText(String.valueOf(receivedTotamt));
            dueamt.setText(String.valueOf(receivedDueamt));

        }
//        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("INTENT_NAME"));
    }

//    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//           receivedUid = intent.getStringExtra("mUid");
//        }
//    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                if(flag == 0) {
                    saveNote();
                }else{
                    update();
                }
               return true;
            case R.id.close_page:
                Intent intent2 = new Intent(NewBillActivity.this,Bill_Store_Activity.class);
//                if(flag == 0) {
                    intent2.putExtra("mUid", receivedUid);
//                }else {
//                    intent2.putExtra("mUid2",billreceivedUid);
//                }
                startActivity(intent2);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {
        String billnumber = billNumber.getText().toString();
        String billdate = billDate.getText().toString();
        String rateperItem = rateperitem.getText().toString();
        double totalamt = Double.parseDouble(totAmt.getText().toString());
        double dueamount = Double.parseDouble(dueamt.getText().toString());

        if (billnumber.trim().isEmpty() || billdate.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a valid bill", Toast.LENGTH_SHORT).show();
            return;
        }
          Bill bill = new Bill(billnumber,billdate,rateperItem,totalamt,dueamount);
//        shopListRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
//
//                    Note note = documentSnapshot.toObject(Note.class);
//                    mUid = String.valueOf(documentSnapshot.getId());
//                   Log.d("NewBillAct", "the mUId is "+mUid);
//
//                }
//                Toast.makeText(NewBillActivity.this, "Bill added to Cloud", Toast.LENGTH_SHORT).show();
//            }
//        });
        shopListRef.document(receivedUid)
                .collection("bills").add(bill);
        Toast.makeText(NewBillActivity.this, "Bill added to Cloud", Toast.LENGTH_SHORT).show();
      Intent intent = new Intent(NewBillActivity.this,Bill_Store_Activity.class);
       intent.putExtra("mUid",receivedUid);
        startActivity(intent);
        finish();
//        CollectionReference billlistRef = FirebaseFirestore.getInstance()
//                .collection("bills");

//        billlistRef.add(new Bill(billnumber,billdate,rateperItem,totalamt,dueamount));


    }

    private void update(){

        DocumentReference billRef = shopListRef.document(receivedUid).collection("bills").document(billreceivedUid);
        receivedBillNum = billNumber.getText().toString();
        receivedBillDate = billDate.getText().toString();
        receivedBillRate = rateperitem.getText().toString();
        receivedTotamt = Double.parseDouble(totAmt.getText().toString());
        receivedDueamt = Double.parseDouble(dueamt.getText().toString());

        billRef.update(
                "billNumber", receivedBillNum,
                "billDate", receivedBillDate,
                "goods", receivedBillRate,
                "totamt", receivedTotamt,
                "dueamt", receivedDueamt
        ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(NewBillActivity.this, "Bill Updated",Toast.LENGTH_SHORT).show();
                    Intent backIntent = new Intent(NewBillActivity.this, Bill_Store_Activity.class);
                    backIntent.putExtra("mUid",receivedUid);
                    startActivity(backIntent);
                    finish();
                }else {
                    Toast.makeText(NewBillActivity.this, "Failed! Check Internet or Log",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}
