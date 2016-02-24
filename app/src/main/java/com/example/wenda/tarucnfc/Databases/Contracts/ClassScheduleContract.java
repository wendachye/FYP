package com.example.wenda.tarucnfc.Databases.Contracts;


import android.provider.BaseColumns;

public class ClassScheduleContract {

    public ClassScheduleContract(){
    }

    public static abstract class ClassScheduleRecord implements BaseColumns {
        public static final String CLASS_SCHEDULE_TABLE = "ClassSchedules";
        public static final String COLUMN_CLASS_SCHEDULE_ID = "ClassScheduleID";
        public static final String COLUMN_BACKEND_ID = "BackendID";
        public static final String COLUMN_FACULTY = "Faculty";
        public static final String COLUMN_PROGRAMME = "Programme";
        public static final String COLUMN_GROUP_No = "GroupNo";
        public static final String COLUMN_SUBJECT = "Subject";
        public static final String COLUMN_TUTORLECTURER = "TutorLecturer";
        public static final String COLUMN_LOCATION = "Location";
        public static final String COLUMN_DAY = "Day";
        public static final String COLUMN_START_TIME = "StartTime";
        public static final String COLUMN_END_TIME = "EndTime";
        public static final String COLUMN_STATUS = "Status";
    }

}
