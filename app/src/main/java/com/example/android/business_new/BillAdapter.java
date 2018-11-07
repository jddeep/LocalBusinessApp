package com.example.android.business_new;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class BillAdapter extends FirestoreRecyclerAdapter<Bill, BillAdapter.BillHolder> {


    public BillAdapter(@NonNull FirestoreRecyclerOptions<Bill> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BillHolder holder, int position, @NonNull Bill model) {

         holder.billNumber.setText(String.valueOf(model.getBillNumber()));
         holder.billDatetext.setText(String.valueOf(model.getBillDate()));
         holder.ratePerItem.setText(String.valueOf(model.getGoods())); //*these are hidden in layout
         holder.totamt.setText(String.valueOf((int)model.getTotamt()));// as are not required to see in the UI */
        holder.dueamt.setText(String.valueOf((int)model.getDueamt()));

    }

    @NonNull
    @Override
    public BillHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bill_item, viewGroup, false);
        return new BillHolder(v);
    }
    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class BillHolder extends RecyclerView.ViewHolder{
        TextView billNumber;
        TextView billDatetext;
        TextView ratePerItem;
        TextView totamt;
        TextView dueamt;

        public BillHolder(@NonNull View itemView) {
            super(itemView);
            billNumber = itemView.findViewById(R.id.text_view_billnumber);
            billDatetext = itemView.findViewById(R.id.text_view_billdate);
            ratePerItem = itemView.findViewById(R.id.text_view_rateperitem);
            totamt = itemView.findViewById(R.id.text_view_totAmt);
            dueamt = itemView.findViewById(R.id.text_view_dueAmt);

        }
    }
}
