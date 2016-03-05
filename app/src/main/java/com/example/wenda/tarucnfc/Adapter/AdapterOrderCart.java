package com.example.wenda.tarucnfc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wenda.tarucnfc.Domains.FoodOrder;
import com.example.wenda.tarucnfc.R;

import org.w3c.dom.Text;

import java.util.List;

public class AdapterOrderCart extends RecyclerView.Adapter<AdapterOrderCart.ViewHolder>{

    Context context;
    List<FoodOrder> items;
    int itemLayout;

    public AdapterOrderCart(Context context, List<FoodOrder> items, int itemLayout) {
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

        holder.foodName.setText(items.get(position).getFoodName());
        holder.quantity.setText(items.get(position).getItemQuantity());
        holder.foodPrice.setText("RM " + items.get(position).getFoodPrice());
        holder.subTotal.setText("RM " +  items.get(position).getSubTotal());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView foodName, foodPrice, quantity, subTotal, totalPrice, GSTPrice, grandTotal;

        public ViewHolder(final View itemView) {
            super(itemView);
            foodName = (TextView) itemView.findViewById(R.id.text_view_foodName);
            quantity = (TextView) itemView.findViewById(R.id.text_view_quantity);
            foodPrice = (TextView) itemView.findViewById(R.id.text_view_foodPrice);
            subTotal = (TextView) itemView.findViewById(R.id.text_view_subTotal);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
