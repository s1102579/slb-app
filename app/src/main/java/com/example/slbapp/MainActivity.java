package com.example.slbapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.slbapp.database.DatabaseHelper;
import com.example.slbapp.database.DatabaseInfo;
import com.example.slbapp.models.Course;
import com.example.slbapp.ui.main.ItemFragment;
import com.example.slbapp.ui.main.MainFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

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


//        Course course = new Course("2016", "1","IKPMD", "3",true, "7", "android ontwikkeling");
//        addCourseToDatabase(course);

        getFromDatabase();
    }

    public void addCourseToDatabase(Course course) {

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

    public void navigateToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, null)
                .setReorderingAllowed(true)
                .addToBackStack("name") // name can be null
                .commit();

    }

    public ArrayList<String> getCoursesFromDatabase() {
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(this);

        Cursor rs = dbHelper.query(DatabaseInfo.CourseTables.COURSETABLE, new String[]{"*"},
                null, null, null, null, null);

        ArrayList<String> coursenames = new ArrayList<>();
        if(rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                String name = (String) rs.getString(rs.getColumnIndex("name"));
                coursenames.add(name);
                rs.moveToNext();
            }
        }

        String rows_amount = String.valueOf(rs.getCount());
        Log.d("aantal rijen: ", rows_amount);
        Snackbar.make(this.findViewById(android.R.id.content), "total courses: " + rows_amount,
                Snackbar.LENGTH_LONG).setAction("", null).show();

        return coursenames;
    }

}