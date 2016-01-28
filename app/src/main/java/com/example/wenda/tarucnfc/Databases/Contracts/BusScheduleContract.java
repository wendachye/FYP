package com.example.wenda.tarucnfc.Databases.Contracts;

import android.provider.BaseColumns;


public class BusScheduleContract {

    public BusScheduleContract(){
    }

    public static abstract class BusScheduleRecord implements BaseColumns {
        public static final String BUS_SCHEDULE_TABLE = "BusSchedules";
        public static final String COLUMN_BUS_SCHEDULE_ID = "BusScheduleID";
        public static final String COLUMN_DEPARTURE = "Departure";
        public static final String COLUMN_DESTINATION = "Destination";
        public static final String COLUMN_ROUTE_TIME = "RouteTime";
        public static final String COLUMN_ROUTE_DAY = "RouteDay";
        public static final String COLUMN_STATUS = "Status";
    }
}
