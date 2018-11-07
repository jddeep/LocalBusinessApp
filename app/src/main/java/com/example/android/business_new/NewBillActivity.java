package com.example.android.business_new;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Ref;

public class NewBillActivity extends AppCompatActivity {
    private EditText billNumber;
    private EditText billDate;
    private EditText rateperitem;
    private EditText totAmt;
    private EditText dueamt;

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
                saveNote();
                return true;
            case R.id.close_page:
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

        CollectionReference billlistRef = FirebaseFirestore.getInstance()
                .collection("bills");
//        Log.d("New", "Hey "+billnumber);
        billlistRef.add(new Bill(billnumber,billdate,rateperItem,totalamt,dueamount));
        Toast.makeText(this, "Bill added to Cloud", Toast.LENGTH_SHORT).show();
        finish();
    }

}
