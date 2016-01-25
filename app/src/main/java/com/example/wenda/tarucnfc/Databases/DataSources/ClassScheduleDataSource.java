package com.example.wenda.tarucnfc.Databases.DataSources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.wenda.tarucnfc.Databases.Contracts.ClassScheduleContract.ClassScheduleRecord;
import com.example.wenda.tarucnfc.Databases.DatabaseSQLHelper;
import com.example.wenda.tarucnfc.Domains.ClassSchedule;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ClassScheduleDataSource {
    private SQLiteDatabase database;
    private DatabaseSQLHelper dbHelper;
    private SimpleDateFormat timeFormat, dateFormat, dayFormat, dateTimeFormat;
    private Calendar calendar;
    private String[] allColumn = {
            ClassScheduleRecord.COLUMN_CLASS_SCHEDULE_ID,
            ClassScheduleRecord.COLUMN_FACULTY,
            ClassScheduleRecord.COLUMN_PROGRAMME,
            ClassScheduleRecord.COLUMN_GROUP,
            ClassScheduleRecord.COLUMN_SUBJECT,
            ClassScheduleRecord.COLUMN_TUTORLECTURER,
            ClassScheduleRecord.COLUMN_LOCATION,
            ClassScheduleRecord.COLUMN_DAY,
            ClassScheduleRecord.COLUMN_START_TIME,
            ClassScheduleRecord.COLUMN_END_TIME
    };

    public ClassScheduleDataSource(Context context) {
        dbHelper = new DatabaseSQLHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insertBusSchedules(ClassSchedule classSchedule) {
        ContentValues values = new ContentValues();

        values.put(ClassScheduleRecord.COLUMN_CLASS_SCHEDULE_ID, classSchedule.getClassScheduleID());
        values.put(ClassScheduleRecord.COLUMN_FACULTY, classSchedule.getFaculty());
        values.put(ClassScheduleRecord.COLUMN_PROGRAMME, classSchedule.getProgramme());
        values.put(ClassScheduleRecord.COLUMN_GROUP, classSchedule.getGroup());
        values.put(ClassScheduleRecord.COLUMN_SUBJECT, classSchedule.getSubject());
        values.put(ClassScheduleRecord.COLUMN_TUTORLECTURER, classSchedule.getTutorlecturer());
        values.put(ClassScheduleRecord.COLUMN_LOCATION, classSchedule.getLocation());
        values.put(ClassScheduleRecord.COLUMN_DAY, classSchedule.getDay());
        values.put(ClassScheduleRecord.COLUMN_START_TIME, classSchedule.getStartTime());
        values.put(ClassScheduleRecord.COLUMN_END_TIME, classSchedule.getEndTime());

        database = dbHelper.getWritableDatabase();
        long ScheduleId = database.insert(ClassScheduleRecord.CLASS_SCHEDULE_TABLE, null, values);
        //  = database.insert(StudentScheduleRecord.STUDENT_SCHEDULE_TABLE, null, values);

        Log.d("ClassScheduleDataSource", "Successfully insert " + ScheduleId);
        //  return ScheduleId;
    }

    public List<ClassSchedule> getAllClassSchedule(){
        List<ClassSchedule> classScheduleList = new ArrayList<>();

        Cursor cursor = database.query(ClassScheduleRecord.CLASS_SCHEDULE_TABLE, allColumn, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            ClassSchedule classSchedule = new ClassSchedule();
            classSchedule.setClassScheduleID(cursor.getInt(0));
            classSchedule.setFaculty(cursor.getString(1));
            classSchedule.setProgramme(cursor.getString(2));
            classSchedule.setGroup(cursor.getString(3));
            classSchedule.setSubject(cursor.getString(4));
            classSchedule.setTutorlecturer(cursor.getString(5));
            classSchedule.setLocation(cursor.getString(6));
            classSchedule.setDay(cursor.getString(7));
            classSchedule.setStartTime(cursor.getString(8));
            classSchedule.setEndTime(cursor.getString(9));

            classScheduleList.add(classSchedule);
            cursor.moveToNext();
        }

        cursor.close();
        Log.d("ClassScheduleDataSource", "Class schedule list size : " + classScheduleList.size());
        return classScheduleList;
    }

    public void deleteClassSchedules() {

        database.execSQL("DELETE FROM " + ClassScheduleRecord.CLASS_SCHEDULE_TABLE);
    }

    public boolean isEmptyClassSchedule() {

        database = dbHelper.getWritableDatabase();
        String count = "SELECT count(*) FROM " + ClassScheduleRecord.CLASS_SCHEDULE_TABLE;
        Cursor cursor = database.rawQuery(count, null);
        cursor.moveToFirst();
        int icount = cursor.getInt(0);
        if (icount > 0) {
            Log.d("ClassScheduleDataSource", "Class schedule not empty");
            return false;
        } else {
            Log.d("ClassScheduleDataSource", "Class schedule is empty");
            return true;
        }

    }

}
