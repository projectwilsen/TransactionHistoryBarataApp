package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransactionHistoryRecyclerViewAdapter extends RecyclerView.Adapter<TransactionHistoryRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<TransactionHistoryModel> transactionHistoryModels;
    public TransactionHistoryRecyclerViewAdapter(Context context, ArrayList<TransactionHistoryModel> transactionHistoryModels){
        this.context = context;
        this.transactionHistoryModels = transactionHistoryModels;
    }

    @NonNull
    @Override
    public TransactionHistoryRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.transaction_history_list, parent, false);
        return new TransactionHistoryRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHistoryRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tvtransactiondate.setText(transactionHistoryModels.get(position).getTransactionHistoryDate());
        holder.tvtransactionstatus.setText(transactionHistoryModels.get(position).getTransactionHistoryStatus());
        holder.tvcsname.setText(transactionHistoryModels.get(position).getTransactionHistoryCSName());
        holder.tvcslocation.setText(transactionHistoryModels.get(position).getTransactionHistoryCSLocation());
        holder.tvtransactionamount.setText(transactionHistoryModels.get(position).getTransactionHistoryAmount());

    }

    @Override
    public int getItemCount() {
        return transactionHistoryModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //ImageView imageView
        TextView tvtransactiondate, tvtransactionstatus, tvcsname, tvcslocation, tvtransactionamount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //imageView = itemview.findViewById(R.id.imageview);
            tvtransactiondate = itemView.findViewById(R.id.tvtransactiondate);
            tvtransactionstatus = itemView.findViewById(R.id.tvtransactionstatus);
            tvcsname = itemView.findViewById(R.id.tvcsname);
            tvcslocation = itemView.findViewById(R.id.tvcslocation);
            tvtransactionamount = itemView.findViewById(R.id.tvtransactionamount);
        }
    }
}
