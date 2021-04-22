package com.example.slbapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.slbapp.database.DatabaseHelper;
import com.example.slbapp.database.DatabaseInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class DateService {

    private final Context context;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private final DateCallback dateCallback;
    private long dateUpdated;

    public DateService(Context context, DateCallback dateCallback) {
        this.dateCallback = dateCallback;
        this.context = context;

        setupFirebaseDatabase();
        getDateUpdatedFromFirebase();
    }

    public void setDateInDatabase() {
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(context);

        ContentValues values = new ContentValues();
        long currentDate = new Date().getTime();

        values.put(DatabaseInfo.DateColumn.DATE_UPDATED, String.valueOf(currentDate));

        dbHelper.insert(DatabaseInfo.DateTables.DATETABLE, null, values);
    }

    public void updateDateInDatabase(long date) {
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(context);

        ContentValues values = new ContentValues();
        values.put(BaseColumns._ID, 1);
        values.put(DatabaseInfo.DateColumn.DATE_UPDATED, String.valueOf(date));

        dbHelper.updateDate(DatabaseInfo.DateTables.DATETABLE, values);
    }

    public void setDateUpdatedInFirebase(long date) {
        DatabaseReference dateRef = myRef.child("dateUpdated");
        dateRef.setValue(date);
    }

    public void getDateUpdatedFromFirebase() {

        myRef.addValueEventListener(new ValueEventListener() {
            long dateUpdatedTemp;

            // LETOP DIT MOET WACHTEN OP DE DATA VAN FIREBASE EN IS ASYNCHRONOUS
            // https://stackoverflow.com/questions/47847694/how-to-return-datasnapshot-value-as-a-result-of-a-method/47853774
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datesnapshot : dataSnapshot.getChildren()) {
                    dateUpdatedTemp = datesnapshot.getValue(Long.class);
                }
                dateUpdated = dateUpdatedTemp;
                Log.d("dateUpdated", String.valueOf(dateUpdated));
                dateCallback.onCallback(dateUpdated);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("read fireBase failed", "Failed to read value.", error.toException());
            }
        });
    }

    public long getDateUpdated() {
        return dateUpdated;
    }

    public long getDateUpdatedFromDatabase() {
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(context);

        Cursor rs = dbHelper.query(DatabaseInfo.DateTables.DATETABLE, new String[]{"*"},
                null, null, null, null, null);

        try {
            rs.moveToFirst();
            long dateUpdatedDatabase = Long.parseLong(rs.getString(rs.getColumnIndex("DateUpdated")));
            Log.d("dateUpdated_database", String.valueOf(dateUpdatedDatabase));

            return dateUpdatedDatabase;

        } catch (CursorIndexOutOfBoundsException e) {
            return 0;
        }

    }

    public boolean isDatabaseOutdated() {
        Log.d("dateUpdatedFirebase", String.valueOf(dateUpdated));
        long dateDatabase = getDateUpdatedFromDatabase();

        if (dateDatabase != 0) {
            return dateUpdated > dateDatabase;
        }
        else {
            return false;
        }
    }

    private void setupFirebaseDatabase() {
        database = FirebaseDatabase.getInstance("https://slb-app-2a31b-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference("dates");
    }
}
