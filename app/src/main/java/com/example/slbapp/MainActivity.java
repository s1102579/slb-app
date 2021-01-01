package com.example.slbapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.example.slbapp.database.DatabaseHelper;
import com.example.slbapp.database.DatabaseInfo;
import com.example.slbapp.models.Course;
import com.example.slbapp.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {
    public static SQLiteDatabase mSQLDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }

// verwijdert Courses tabel uit database
//        DatabaseHelper dbHelper = DatabaseHelper.getHelper(this);
//        dbHelper.dropCourses(mSQLDB);


        Course course = new Course("2016", "1","IKPMD", "3",true, "7", "android ontwikkeling");
        addCourseToDatabase(course);

        getFromDatabase();
    }

    private void addCourseToDatabase(Course course) {

        DatabaseHelper dbHelper = DatabaseHelper.getHelper(this);

        ContentValues values = new ContentValues();
        values.put(DatabaseInfo.CourseColumn.YEAR, course.getYear());
        values.put(DatabaseInfo.CourseColumn.PERIOD, course.getPeriod());
        values.put(DatabaseInfo.CourseColumn.NAME, course.getName());
        values.put(DatabaseInfo.CourseColumn.ECTS, course.getEcts());
        values.put(DatabaseInfo.CourseColumn.ISOPTIONAL, course.isOptional());
        values.put(DatabaseInfo.CourseColumn.GRADE, course.getGrade());
        values.put(DatabaseInfo.CourseColumn.NOTES, course.getNotes());

        dbHelper.insert(DatabaseInfo.CourseTables.COURSETABLE, null, values);

    }

    public void getFromDatabase() {
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(this);

        Cursor rs = dbHelper.query(DatabaseInfo.CourseTables.COURSETABLE, new String[]{"*"},
                null, null, null, null, null);

        rs.moveToFirst();
        String name = (String) rs.getString(rs.getColumnIndex("name"));
        Log.d("Tim heeft gevonden=", "deze: "+ name);
    }
}