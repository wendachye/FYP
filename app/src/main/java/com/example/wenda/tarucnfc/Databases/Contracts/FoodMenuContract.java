package com.example.wenda.tarucnfc.Databases.Contracts;


import android.provider.BaseColumns;

public class FoodMenuContract {

    public FoodMenuContract(){
    }

    public static abstract class FoodMenuRecord implements BaseColumns {
        public static final String FOOD_MENU_TABLE = "FoodMenus";
        public static final String COLUMN_FOOD_MENU_ID = "FoodMenuID";
        public static final String COLUMN_FOOD_STALL_ID = "FoodStallID";
        public static final String COLUMN_FOOD_CATEGORY = "FoodCategory";
        public static final String COLUMN_FOOD_NAME = "FoodName";
        public static final String COLUMN_FOOD_PRICE = "FoodPrice";
        public static final String COLUMN_FOOD_GST_PRICE = "FoodGSTPrice";
        public static final String COLUMN_FOOD_MENU_IMAGE_PATH = "FoodMenuImagePath";
    }
}
