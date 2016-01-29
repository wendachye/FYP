package com.example.wenda.tarucnfc.Databases;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wenda.tarucnfc.Databases.Contracts.BusScheduleContract.BusScheduleRecord;
import com.example.wenda.tarucnfc.Databases.Contracts.ClassScheduleContract.ClassScheduleRecord;

public class DatabaseSQLHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TARUCANDROID.db";

    private static final String SQL_CREATE_BUS_SCHEDULES =
            "CREATE TABLE "
                    + BusScheduleRecord.BUS_SCHEDULE_TABLE +" ("
                    + BusScheduleRecord.COLUMN_BUS_SCHEDULE_ID + " INTEGER PRIMARY KEY,"
                    + BusScheduleRecord.COLUMN_DEPARTURE + " TEXT,"
                    + BusScheduleRecord.COLUMN_DESTINATION + " TEXT,"
                    + BusScheduleRecord.COLUMN_ROUTE_TIME + " TEXT,"
                    + BusScheduleRecord.COLUMN_ROUTE_DAY + " TEXT,"
                    + BusScheduleRecord.COLUMN_STATUS + " TEXT)";

    private static final String SQL_CREATE_CLASS_SCHEDULES =
            "CREATE TABLE "
                    + ClassScheduleRecord.CLASS_SCHEDULE_TABLE +" ("
                    + ClassScheduleRecord.COLUMN_CLASS_SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ClassScheduleRecord.COLUMN_FACULTY + " TEXT,"
                    + ClassScheduleRecord.COLUMN_PROGRAMME + " TEXT,"
                    + ClassScheduleRecord.COLUMN_GROUP_No + " TEXT,"
                    + ClassScheduleRecord.COLUMN_SUBJECT + " TEXT,"
                    + ClassScheduleRecord.COLUMN_TUTORLECTURER + " TEXT,"
                    + ClassScheduleRecord.COLUMN_LOCATION + " TEXT,"
                    + ClassScheduleRecord.COLUMN_DAY + " TEXT,"
                    + ClassScheduleRecord.COLUMN_START_TIME + " TEXT,"
                    + ClassScheduleRecord.COLUMN_END_TIME + " TEXT)";


    public DatabaseSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // create SQLite database tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_BUS_SCHEDULES);
        //db.execSQL(SQL_CREATE_CLASS_SCHEDULES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
