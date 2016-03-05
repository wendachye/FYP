package com.example.wenda.tarucnfc.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Activitys.FoodDetailsActivity;
import com.example.wenda.tarucnfc.Activitys.FoodMenuActivity;
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
    private String quantity;
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
            final String foodMenuID;
            foodMenu.setFoodMenuID(items.get(getAdapterPosition()).getFoodMenuID());
            foodMenuID = foodMenu.getFoodMenuID();

            // pass data to new activity
            Intent intent = new Intent(context, FoodDetailsActivity.class);
            intent.putExtra("FoodMenuID", foodMenuID);
            context.startActivity(intent);

            // display dialog box
            MaterialDialog dialog = new MaterialDialog.Builder(context)
                    .title(R.string.quantity)
                    .customView(R.layout.dialog_custom_view, true)
                    .positiveText(R.string.confirm)
                    .negativeText(android.R.string.cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            new BaseActivity().shortToast(context, "Added to cart.");
                            // run php to add order line
                            ((FoodMenuActivity) context).AddOrder(foodMenuID, quantity);
                        }
                    }).build();

            final Spinner mSpinner = (Spinner) dialog.getCustomView().findViewById(R.id.spinner_quantity);
            mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (mSpinner.getSelectedItemPosition()){
                        case 0:
                            quantity = "1";
                            break;

                        case 1:
                            quantity = "2";
                            break;

                        case 2:
                            quantity = "3";
                            break;

                        case 3:
                            quantity = "4";
                            break;

                        case 4:
                            quantity = "5";
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            mSpinner.setSelection(0);

            dialog.show();
        }
    }

    public interface AdapterCallBack {
        void adapterOnClick(int adapterPosition);
    }
}
