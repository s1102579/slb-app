package com.example.slbapp;

import android.content.ContentValues;
import android.content.Context;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.slbapp.database.DatabaseHelper;
import com.example.slbapp.database.DatabaseInfo;
import com.example.slbapp.models.Course;

import java.util.Date;

public class DateService {

    private Context context;

    public DateService(Context context) {
        this.context = context;
    }

    public void setDateInDatabase() {
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(context);

        ContentValues values = new ContentValues();
        Date currentDate = new Date();

        values.put(DatabaseInfo.DateColumn.DATE_UPDATED, String.valueOf(currentDate));

        dbHelper.insert(DatabaseInfo.DateTables.DATETABLE, null, values);

    }

    public void updateDateInDatabase() {
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(context);

        ContentValues values = new ContentValues();
        Date currentDate = new Date();
        values.put(BaseColumns._ID, 1);
        values.put(DatabaseInfo.DateColumn.DATE_UPDATED, String.valueOf(currentDate));

        dbHelper.updateDate(DatabaseInfo.DateTables.DATETABLE, values);

    }

}
