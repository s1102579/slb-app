package com.example.slbapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static SQLiteDatabase mSQLDB;
    private static com.example.slbapp.database.DatabaseHelper mInstance;
    public static final String dbName = "course.db";
    public static final int dbVersion = 2;		// Versie nr van je db.

    private DatabaseHelper(Context ctx) {
        super(ctx, dbName, null, dbVersion);	// gebruik de super constructor.
    }

    // synchronized … dit zorgt voor . . . . (?)
    // welk design pattern is dit ??  ==> Dit is een Singleton Design Pattern
    public static synchronized com.example.slbapp.database.DatabaseHelper getHelper (Context ctx){
        if (mInstance == null){
            mInstance = new com.example.slbapp.database.DatabaseHelper(ctx);
            mSQLDB = mInstance.getWritableDatabase();
        }
        return mInstance;
    }

    @Override										// Maak je tabel met deze kolommen
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + com.example.slbapp.database.DatabaseInfo.CourseTables.COURSETABLE + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                com.example.slbapp.database.DatabaseInfo.CourseColumn.YEAR + " TEXT," +
                com.example.slbapp.database.DatabaseInfo.CourseColumn.PERIOD + " TEXT," +
                com.example.slbapp.database.DatabaseInfo.CourseColumn.NAME + " TEXT," +
                com.example.slbapp.database.DatabaseInfo.CourseColumn.ECTS + " TEXT," +
                com.example.slbapp.database.DatabaseInfo.CourseColumn.ISOPTIONAL + " INTEGER," +
                com.example.slbapp.database.DatabaseInfo.CourseColumn.GRADE + " TEXT," +
                com.example.slbapp.database.DatabaseInfo.CourseColumn.NOTES + " TEXT);"
        );
    }
    // CREATE TABLE CarTable (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, ects TEXT, grade TEXT);

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ com.example.slbapp.database.DatabaseInfo.CourseTables.COURSETABLE);
        onCreate(db);
    }

    public void dropCourses(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS "+ com.example.slbapp.database.DatabaseInfo.CourseTables.COURSETABLE);
        onCreate(db);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version ){
        super(context,name,factory, version);
    }

    public void insert(String table, String nullColumnHack, ContentValues values){
        mSQLDB.insert(table, nullColumnHack, values);
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectArgs, String groupBy, String having, String orderBy){
        return mSQLDB.query(table, columns, selection, selectArgs, groupBy, having, orderBy);
    }

}//end class





