package com.example.android.business_new;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Bill_Store_Activity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference billlistRef;
    private BillAdapter adapter;
    String mUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill__store);

        setTitle("Biils List");

        Bundle extras = getIntent().getExtras();
        mUid = extras.getString("mUid");
        billlistRef = db.collection("shops").document(mUid).collection("bills");

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_bill);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               JustforOnClickIntent();
            }
        });

        setUpRecyclerView();
    }

    private void JustforOnClickIntent(){
        Intent intent = new Intent(Bill_Store_Activity.this, NewBillActivity.class);
        intent.putExtra("mUid",mUid);
        intent.putExtra("billnumber","x");
        startActivity(intent);
    }

    private void setUpRecyclerView() {
        Query query = billlistRef.orderBy("dueamt", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<Bill> options = new FirestoreRecyclerOptions.Builder<Bill>()
                .setQuery(query, Bill.class)
                .build();

        adapter = new BillAdapter(options);


        RecyclerView recyclerView = findViewById(R.id.recycler_view_2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);


        adapter.setOnItemClickListener(new BillAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    Bill bill = documentSnapshot.toObject(Bill.class);
                    String id2 = documentSnapshot.getId();
                    Intent intent = new Intent(Bill_Store_Activity.this, NewBillActivity.class);
                    intent.putExtra("mUid",mUid);
                    intent.putExtra("mUid2",id2);
                    intent.putExtra("billnumber",bill.getBillNumber());
                    intent.putExtra("billdate",bill.getBillDate());
                    intent.putExtra("rateperitem",bill.getGoods());
                    intent.putExtra("totalamt",bill.getTotamt());
                    intent.putExtra("dueamt",bill.getDueamt());
                    startActivity(intent);
            }
        });
//        adapter.setOnItemClickListener(new NodeAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
//                Note note = documentSnapshot.toObject(Note.class);
//                String id = documentSnapshot.getId();
//                String path = documentSnapshot.getReference().getPath();
//                startActivity(new Intent(Bill_Store_Activity.this, MainActivity.class));
//                finish();
////                Toast.makeText(MainActivity.this,
////                        "Position: " + position + " ID: " + id, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Bill_Store_Activity.this, MainActivity.class));
    }
}