package com.example.wenda.tarucnfc.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wenda.tarucnfc.Domains.FoodMenu;
import com.example.wenda.tarucnfc.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

public class AdapterFoodMenu extends RecyclerView.Adapter<AdapterFoodMenu.ViewHolder>{

    Context context;
    List<FoodMenu> items;
    int itemLayout;
    private DisplayImageOptions options;
    AdapterCallBack adapterCallBack;

    public AdapterFoodMenu(Context context, List<FoodMenu> items, int itemLayout, AdapterCallBack adapterCallBack) {
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

        holder.foodName.setText(items.get(position).getFoodName());
        holder.foodPrice.setText("\n" + items.get(position).getFoodPrice());
        ImageLoader.getInstance().displayImage(items.get(position).getFoodMenuImagePath(), holder.foodMenuImagePath, options);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView foodName, foodPrice;
        ImageView foodMenuImagePath;

        public ViewHolder(final View itemView) {
            super(itemView);
            foodName = (TextView) itemView.findViewById(R.id.text_view_foodName);
            foodPrice = (TextView) itemView.findViewById(R.id.text_view_foodPrice);
            foodMenuImagePath = (ImageView) itemView.findViewById(R.id.image_food_menu);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            adapterCallBack.adapterOnClick(getAdapterPosition());
            FoodMenu foodMenu = new FoodMenu();
            String foodMenuID;
            foodMenu.setFoodMenuID(items.get(getAdapterPosition()).getFoodMenuID());
            foodMenuID = foodMenu.getFoodMenuID();

            // display dialog box

        }
    }

    public interface AdapterCallBack {
        void adapterOnClick(int adapterPosition);
    }
}
