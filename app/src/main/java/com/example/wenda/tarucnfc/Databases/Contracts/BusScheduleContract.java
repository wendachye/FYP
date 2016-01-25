package com.example.wenda.tarucnfc.Databases.Contracts;

import android.provider.BaseColumns;


public class BusScheduleContract {

    public BusScheduleContract(){
    }

    public static abstract class BusScheduleRecord implements BaseColumns {
        public static final String BUS_SCHEDULE_TABLE = "BusSchedules";
        public static final String COLUMN_BUS_SCHEDULE_ID = "busScheduleID";
        public static final String COLUMN_DEPARTURE = "departure";
        public static final String COLUMN_DESTINATION = "destination";
        public static final String COLUMN_ROUTE_TIME = "routeTime";
        public static final String COLUMN_ROUTE_DAY = "routeDay";
        public static final String COLUMN_STATUS = "status";
    }
}
