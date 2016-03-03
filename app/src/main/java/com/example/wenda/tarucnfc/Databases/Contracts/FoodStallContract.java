package com.example.wenda.tarucnfc.Databases.Contracts;


import android.provider.BaseColumns;

public class FoodStallContract {

    public FoodStallContract(){
    }

    public static abstract class FoodStallRecord implements BaseColumns {
        public static final String FOOD_STALL_TABLE = "FoodStalls";
        public static final String COLUMN_FOOD_STALL_ID = "FoodStallID";
        public static final String COLUMN_ACCOUNT_ID = "AccountID";
        public static final String COLUMN_STALL_NAME = "StallName";
        public static final String COLUMN_LOCATION = "Location";
        public static final String COLUMN_JOINED_DATE = "JoinedDate";
        public static final String COLUMN_FOOD_STALL_IMAGE_PATH = "FoodStallImagePath";
        public static final String COLUMN_STATUS = "Status";
    }

}
