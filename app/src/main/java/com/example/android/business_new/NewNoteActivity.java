package com.example.android.business_new;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class NewNoteActivity extends AppCompatActivity{
    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;


    private CollectionReference shopListRef=FirebaseFirestore.getInstance().collection("shops");
    private PhoneCallerListener innclass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
//        innclass = new PhoneCallerListener(this);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Shop");

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);

        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);
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

    private class PhoneCallerListener extends PhoneStateListener{
        private boolean isPhoneCalling = false;

        public PhoneCallerListener(NewNoteActivity newNoteActivity) {

        }

        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            if(TelephonyManager.CALL_STATE_RINGING == state){
                Log.i("NewNoteAct ", "Ringing number is "+phoneNumber);

            }
            if (TelephonyManager.CALL_STATE_OFFHOOK == state){
                isPhoneCalling =true;
            }
            if(TelephonyManager.CALL_STATE_IDLE == state){
                Log.i("NewNoteAct", "IDLE");
                if(isPhoneCalling){
                    Log.i("NewNoteAct","Restartimg App");

                    Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(
                             getBaseContext().getPackageName()
                    );
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                    isPhoneCalling = false;
                }
            }
        }
    }


    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();

        if (title.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a valid Shop name", Toast.LENGTH_SHORT).show();
            return;
        }
        CollectionReference notebookRef = FirebaseFirestore.getInstance()
                .collection("shops");
        notebookRef.add(new Note(title, description, priority));
        Toast.makeText(this, "Shop added to Cloud", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(NewNoteActivity.this, MainActivity.class));
        finish();
//        Note note = new Note(title,description, priority);
//        notebookRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
//
//                    Note note = documentSnapshot.toObject(Note.class);
//                    mUid = String.valueOf(documentSnapshot.getId());
//                    Log.d("NewBillAct", "the mUId is "+mUid);
//
//                }
//
//            }
//        });



    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(NewNoteActivity.this, MainActivity.class));
        finish();
    }
}
