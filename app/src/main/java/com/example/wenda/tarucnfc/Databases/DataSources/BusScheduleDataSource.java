package com.example.wenda.tarucnfc.Databases.DataSources;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.wenda.tarucnfc.Databases.Contracts.BusScheduleContract.BusScheduleRecord;
import com.example.wenda.tarucnfc.Databases.DatabaseSQLHelper;
import com.example.wenda.tarucnfc.Domains.BusSchedule;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BusScheduleDataSource {

    private SQLiteDatabase database;
    private DatabaseSQLHelper dbHelper;
    private String[] allColumn = {
            BusScheduleRecord.COLUMN_BUS_SCHEDULE_ID,
            BusScheduleRecord.COLUMN_DEPARTURE,
            BusScheduleRecord.COLUMN_DESTINATION,
            BusScheduleRecord.COLUMN_ROUTE_TIME,
            BusScheduleRecord.COLUMN_ROUTE_DAY,
            BusScheduleRecord.COLUMN_STATUS
    };

    public BusScheduleDataSource(Context context) {
        dbHelper = new DatabaseSQLHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insertBusSchedules(BusSchedule busSchedule) {
        ContentValues values = new ContentValues();

        values.put(BusScheduleRecord.COLUMN_BUS_SCHEDULE_ID, busSchedule.getBusScheduleID());
        values.put(BusScheduleRecord.COLUMN_DEPARTURE, busSchedule.getDeparture());
        values.put(BusScheduleRecord.COLUMN_DESTINATION, busSchedule.getDestination());
        values.put(BusScheduleRecord.COLUMN_ROUTE_TIME, busSchedule.getRouteTime());
        values.put(BusScheduleRecord.COLUMN_ROUTE_DAY, busSchedule.getRouteDay());
        values.put(BusScheduleRecord.COLUMN_STATUS, busSchedule.getStatus());


        database = dbHelper.getWritableDatabase();
        long ScheduleId = database.insert(BusScheduleRecord.BUS_SCHEDULE_TABLE, null, values);
        //  = database.insert(StudentScheduleRecord.STUDENT_SCHEDULE_TABLE, null, values);

        Log.d("BusScheduleDataSource:", "Successfully insert id " + ScheduleId);
        //  return ScheduleId;
    }

    public List<BusSchedule> getAllBusScheduleByDestination(String destination){
        List<BusSchedule> busScheduleList = new ArrayList<>();


        String whereClause = BusScheduleRecord.COLUMN_DESTINATION + " = '" + destination + "'";
        Cursor cursor = database.query(BusScheduleRecord.BUS_SCHEDULE_TABLE, allColumn, whereClause, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            BusSchedule busSchedule = new BusSchedule();
            busSchedule.setBusScheduleID(cursor.getInt(0));
            busSchedule.setDeparture(cursor.getString(1));
            busSchedule.setDestination(cursor.getString(2));
            busSchedule.setRouteTime(cursor.getString(3));
            busSchedule.setRouteDay(cursor.getString(4));
            busSchedule.setStatus(cursor.getString(5));

            busScheduleList.add(busSchedule);
            cursor.moveToNext();
        }

        cursor.close();
        Log.d("BusScheduleDataSource", "Bus schedule list size : " + busScheduleList.size());
        return busScheduleList;
    }

    public BusSchedule getBusScheduleByDestination(String destination, String day) {

        String filterDay = "";

        switch (day) {
            case "1":
                filterDay = "Sunday";
                break;
            case "2":
                filterDay = "Monday to Thursday";
                break;
            case "3":
                filterDay = "Monday to Thursday";

                break;
            case "4":
                filterDay = "Monday to Thursday";

                break;
            case "5":
                filterDay = "Monday to Thursday";

                break;
            case "6":
                filterDay = "Monday to Thursday";

                break;
            case "7":
                filterDay = "Saturday";

                break;

        }


        BusSchedule busSchedule = new BusSchedule();

        Log.d("BusScheduleDataSource", "Whereclause  : " + destination + " " + filterDay);
        String whereClause = BusScheduleRecord.COLUMN_DESTINATION + " = '" + destination + "' AND " +
                BusScheduleRecord.COLUMN_ROUTE_DAY + " = '" + filterDay + "'" + " AND " +
                BusScheduleRecord.COLUMN_STATUS + " = 'Active'";
        Cursor cursor = database.query(BusScheduleRecord.BUS_SCHEDULE_TABLE, allColumn, whereClause, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            busSchedule.setBusScheduleID(cursor.getInt(0));
            busSchedule.setDeparture(cursor.getString(1));
            busSchedule.setDestination(cursor.getString(2));
            busSchedule.setRouteTime(cursor.getString(3));
            busSchedule.setRouteDay(cursor.getString(4));
            busSchedule.setStatus(cursor.getString(5));

            cursor.moveToNext();
        }

        cursor.close();
        Log.d("BusScheduleDataSource", "Day : " + busSchedule.getRouteDay());
        return busSchedule;
    }

    public void deleteBusSchedules() {

        database.execSQL("DELETE FROM " + BusScheduleRecord.BUS_SCHEDULE_TABLE);
    }

    public boolean isEmptyBusSchedule() {

        database = dbHelper.getWritableDatabase();
        String count = "SELECT count(*) FROM " + BusScheduleRecord.BUS_SCHEDULE_TABLE;
        Cursor cursor = database.rawQuery(count, null);
        cursor.moveToFirst();
        int icount = cursor.getInt(0);
        if (icount > 0) {
            Log.d("BusScheduleDataSource:", "Bus schedule not empty");
            return false;
        } else {
            Log.d("BusScheduleDataSource:", "Bus schedule is empty");
            return true;
        }

    }
}

