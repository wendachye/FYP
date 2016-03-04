package com.example.wenda.tarucnfc.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wenda.tarucnfc.Activitys.FoodMenuActivity;
import com.example.wenda.tarucnfc.Domains.FoodStall;
import com.example.wenda.tarucnfc.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

public class AdapterFoodOrder extends RecyclerView.Adapter<AdapterFoodOrder.ViewHolder>{

    Context context;
    List<FoodStall> items;
    int itemLayout;
    private DisplayImageOptions options;
    AdapterCallBack adapterCallBack;

    public AdapterFoodOrder(Context context, List<FoodStall> items, int itemLayout, AdapterCallBack adapterCallBack) {
        this.context = context;
        this.items = items;
        this.itemLayout = itemLayout;
        this.adapterCallBack = adapterCallBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);

        options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .build();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.stallName.setText(items.get(position).getStallName());
        holder.location.setText("\n" + items.get(position).getLocation());
        ImageLoader.getInstance().displayImage(items.get(position).getFoodStallImagePath(), holder.foodStallImagePath, options);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView stallName, location;
        ImageView foodStallImagePath;

        public ViewHolder(final View itemView) {
            super(itemView);
            stallName = (TextView) itemView.findViewById(R.id.text_view_stallName);
            location = (TextView) itemView.findViewById(R.id.text_view_location);
            foodStallImagePath = (ImageView) itemView.findViewById(R.id.image_food_stall);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            adapterCallBack.adapterOnClick(getAdapterPosition());
            FoodStall foodStall = new FoodStall();
            String foodStallID;
            foodStall.setFoodStallID(items.get(getAdapterPosition()).getFoodStallID());
            foodStallID = foodStall.getFoodStallID();

            // pass news data to new activity
            Intent intent = new Intent(context, FoodMenuActivity.class);
            intent.putExtra("FoodStallID", foodStallID);
            context.startActivity(intent);
        }
    }

    public interface AdapterCallBack {
        void adapterOnClick(int adapterPosition);
    }
}
