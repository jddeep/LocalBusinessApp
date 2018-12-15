package com.example.android.business_new;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.sql.Ref;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewBillActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private EditText billNumber;
    private EditText billDate;
    private EditText rateperitem;
    private EditText totAmt;
    private EditText dueamt;

    DocumentReference billRef;

    private FloatingActionButton datepicker;
    private LinearLayout paymentsLayout;

    private int x = 0;
    private int i=0;


    private String billreceivedUid;
    private String receivedUid;
    private String receivedBillNum;
    private String receivedBillDate;
    private String receivedBillRate;
    private double receivedTotamt;
    private double receivedDueamt;
    private List<String> receivedPays;
    private List<String> receivedPayDates;

    private double[] doublepaysarray;
    private double paymentsum=0.0;
    private double initDueAmt;

    private List<String> pays;
    private List<String> paydates;

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

       pays = new ArrayList<String>();
       paydates = new ArrayList<String>();

       doublepaysarray = new double[0];


        paymentsLayout = findViewById(R.id.payments_linear_layout);

//       payments.setOnKeyListener(new View.OnKeyListener() {
//           @Override
//           public boolean onKey(View v, int keyCode, KeyEvent event) {
//               if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode == KeyEvent.KEYCODE_ENTER)){
//                   Log.d("NewBillAct", "Entered");
//                   payments.setText("Rs. ");
//                   return true;
//
//               }
//               return false;
//           }
//       });


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
//            payments.setText(null);
        }else{
            flag = 1;
            billreceivedUid = extras.getString("mUid2");
            receivedBillDate = extras.getString("billdate");
            receivedBillRate = extras.getString("rateperitem");
            receivedTotamt = extras.getDouble("totalamt");
            receivedDueamt = extras.getDouble("dueamt");
            receivedPays = extras.getStringArrayList("pays");
            receivedPayDates = extras.getStringArrayList("paydates");

            billNumber.setText(receivedBillNum);
            billDate.setText(receivedBillDate);
            rateperitem.setText(receivedBillRate);
            totAmt.setText(String.valueOf(receivedTotamt));
            dueamt.setText(String.valueOf(receivedDueamt));
          if(receivedPays!=null) {



              for (int index = 0; index < receivedPays.size(); index++) {
                  LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                  View new_payment = layoutInflater.inflate(R.layout.new_payment_field, null);
                  paymentsLayout.addView(new_payment, paymentsLayout.getChildCount() - 1);


              }
              for (int shit = 0; shit < paymentsLayout.getChildCount(); shit++) {
                  View hitview = paymentsLayout.getChildAt(shit);
                  ViewGroup HitView = (ViewGroup) hitview;
                  EditText childshitEditview = (EditText) HitView.getChildAt(0);
                  TextView childshitTextview = (TextView) HitView.getChildAt(1);
                  Log.d("NewBillAct", shit + "RPAY" + receivedPays.get(shit));
                  childshitEditview.setText(receivedPays.get(shit));
                  childshitTextview.setText(receivedPayDates.get(shit));
              }
          }



        }
//        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("INTENT_NAME"));
//    addpaybtn.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Add_Payment();
//        }
//    });






    }

    public void openCalendar(View view){


                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
                x = paymentsLayout.getChildCount()-1;



    }




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

        initDueAmt = dueamount;
        Log.d("NewBillAct", "Hithere"+initDueAmt);

       for(int k=0; k<paymentsLayout.getChildCount();k++){
           View mView = paymentsLayout.getChildAt(k);
           ViewGroup mView2 = (ViewGroup)mView;
           View mview = mView2.getChildAt(0);
           EditText editText = (EditText)mview;
           pays.add(k, editText.getText().toString());
       }

        if (billnumber.trim().isEmpty() || billdate.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a valid bill", Toast.LENGTH_SHORT).show();
            return;
        }
          Bill bill = new Bill(billnumber,billdate,rateperItem,totalamt,dueamount, pays, paydates);

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
//        finish();

    }


    private void update(){

        billRef = shopListRef.document(receivedUid).collection("bills").document(billreceivedUid);
        receivedBillNum = billNumber.getText().toString();
        receivedBillDate = billDate.getText().toString();
        receivedBillRate = rateperitem.getText().toString();
        receivedTotamt = Double.parseDouble(totAmt.getText().toString());

        doublepaysarray = new double[0];
        paymentsum=0.0;
        if(paymentsLayout.getChildCount()!=0) {
            receivedPays=new ArrayList<String>();
            receivedPayDates=new ArrayList<String>();

            for (int shit = 0; shit < paymentsLayout.getChildCount(); shit++) {
                View hitview = paymentsLayout.getChildAt(shit);
                ViewGroup HitView = (ViewGroup) hitview;
                EditText childshitEditview = (EditText) HitView.getChildAt(0);
                TextView childshitTextview = (TextView) HitView.getChildAt(1);
//                Log.d("NewBillAct", shit + "RPAY" + receivedPays.get(shit));
                receivedPays.add(shit, childshitEditview.getText().toString());
                doublepaysarray=new double[receivedPays.size()];
                doublepaysarray[shit] = Double.parseDouble(receivedPays.get(shit));
                paymentsum=paymentsum+doublepaysarray[shit];
                receivedPayDates.add(shit, childshitTextview.getText().toString());
//                paymentsLayout.removeView((View) hitview);
            }
        }else{
            receivedPays=new ArrayList<String>();
            receivedPayDates=new ArrayList<String>();
        }

        Log.d("NewBillAct", "Hithere"+initDueAmt);
        double tempDue =initDueAmt-paymentsum;
        dueamt.setText(String.valueOf(tempDue));
        receivedDueamt = Double.parseDouble(dueamt.getText().toString());

        billRef.update(
                "billNumber", receivedBillNum,
                "billDate", receivedBillDate,
                "goods", receivedBillRate,
                "totamt", receivedTotamt,
                "dueamt", receivedDueamt,
                "pays", receivedPays,
                "paydates", receivedPayDates

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backintent = new Intent(NewBillActivity.this, Bill_Store_Activity.class);
      backintent.putExtra("mUid",receivedUid);
        startActivity(backintent);
      finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currdateString = DateFormat.getDateInstance(DateFormat.DEFAULT).format(c.getTime());

        if(x!=0){
            x=x-1;
        }
        View mView = paymentsLayout.getChildAt(x);
        ViewGroup mView2 = (ViewGroup)mView;
        View mview = mView2.getChildAt(1);

        Log.d("NewAct","Hey "+x);
        if(mview instanceof TextView) {
            TextView textView = (TextView)mview;
            textView.setText(currdateString);
            paydates.add(currdateString);
            i++;
        }



    }

    public void onAddField(View view){
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View new_payment = layoutInflater.inflate(R.layout.new_payment_field, null);
        paymentsLayout.addView(new_payment, paymentsLayout.getChildCount()-1);


    }

    public void onDelete(View view){
        paymentsLayout.removeView((View) view.getParent());
        i--;

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}

