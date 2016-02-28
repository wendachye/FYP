package com.example.wenda.tarucnfc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wenda.tarucnfc.Domains.Transaction;

import java.util.List;

public class AdapterTransaction extends RecyclerView.Adapter<AdapterTransaction.ViewHolder>{

    Context context;
    List<Transaction> items;
    int itemLayout;

    public AdapterTransaction(Context context, List<Transaction> items, int itemLayout) {
        this.context = context;
        this.items = items;
        this.itemLayout = itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.senderID.setText(items.get(position).getSenderID());
        holder.recipientID.setText(items.get(position).getRecipientID());
        holder.transactionType.setText(items.get(position).getTransactionType());
        holder.amount.setText("RM " + items.get(position).getAmount());
        holder.dateTime.setText(items.get(position).getDateTime());
        holder.status.setText(items.get(position).getStatus());
        holder.remark.setText(items.get(position).getRemark());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView senderID, recipientID, transactionType, amount, dateTime, status, remark;

        public ViewHolder(final View itemView) {
            super(itemView);
            senderID = (TextView) itemView.findViewById(R.id.senderID);
            recipientID = (TextView) itemView.findViewById(R.id.recipientID);
            transactionType = (TextView) itemView.findViewById(R.id.transactionType);
            amount = (TextView) itemView.findViewById(R.id.amount);
            dateTime = (TextView) itemView.findViewById(R.id.dateTime);
            status = (TextView) itemView.findViewById(R.id.status);
            remark = (TextView) itemView.findViewById(R.id.remark);
        }

        @Override
        public void onClick(View view) {


        }
    }

}

