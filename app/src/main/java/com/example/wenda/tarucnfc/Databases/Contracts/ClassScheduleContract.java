package com.example.wenda.tarucnfc.Databases.Contracts;


import android.provider.BaseColumns;

public class ClassScheduleContract {

    public ClassScheduleContract(){
    }

    public static abstract class ClassScheduleRecord implements BaseColumns {
        public static final String CLASS_SCHEDULE_TABLE = "ClassSchedules";
        public static final String COLUMN_CLASS_SCHEDULE_ID = "classScheduleID";
        public static final String COLUMN_FACULTY = "faculty";
        public static final String COLUMN_PROGRAMME = "programme";
        public static final String COLUMN_GROUP = "group";
        public static final String COLUMN_SUBJECT = "subject";
        public static final String COLUMN_TUTORLECTURER = "tutorlecturer";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_DAY = "day";
        public static final String COLUMN_START_TIME = "startTime";
        public static final String COLUMN_END_TIME = "endTime";
    }

}
