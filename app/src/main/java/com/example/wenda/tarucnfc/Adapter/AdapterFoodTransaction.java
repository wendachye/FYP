package com.example.wenda.tarucnfc.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wenda.tarucnfc.Domains.FoodOrder;
import com.example.wenda.tarucnfc.R;

import java.util.List;

public class AdapterFoodTransaction extends RecyclerView.Adapter<AdapterFoodTransaction.ViewHolder>{

    Context context;
    List<FoodOrder> items;
    int itemLayout;

    public AdapterFoodTransaction(Context context, List<FoodOrder> items, int itemLayout) {
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

        holder.paymentDateTime.setText(items.get(position).getPaymentDateTime());
        holder.totalAmount.setText("RM " + items.get(position).getTotalPrice());
        holder.status.setText(items.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView paymentDateTime, totalAmount, status;

        public ViewHolder(final View itemView) {
            super(itemView);
            paymentDateTime = (TextView) itemView.findViewById(R.id.paymentDateTime);
            totalAmount = (TextView) itemView.findViewById(R.id.totalAmount);
            status = (TextView) itemView.findViewById(R.id.status);
        }

        @Override
        public void onClick(View view) {


        }
    }
}
