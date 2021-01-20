package com.example.slbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.slbapp.database.DatabaseHelper;
import com.example.slbapp.database.DatabaseInfo;
import com.example.slbapp.models.Course;
import com.example.slbapp.ui.main.ItemFragment;
import com.example.slbapp.ui.main.MainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static SQLiteDatabase mSQLDB;
    public ArrayList<Course> allCourses = new ArrayList<>();
    public List<Course> filteredCourses = new ArrayList<>();
    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, MainFragment.newInstance())
                    .commitNow();
            instance = this;
        }

        allCourses = getCoursesFromDatabase();
        setFilteredCourses(allCourses);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

// verwijdert Courses tabel uit database
//        DatabaseHelper dbHelper = DatabaseHelper.getHelper(this);
//        dbHelper.dropCourses(mSQLDB);


//        Course course = new Course("2016", "1","IOPR1", "3",true, "7", "android ontwikkeling");
//        addCourseToDatabase(course);

//        getFromDatabase();
    }

    public static MainActivity getInstance() {
        return instance;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch(item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new MainFragment();
                            break;
                        case R.id.nav_courses:
                            selectedFragment = new ItemFragment();
                            break;
                    }

                    navigateToFragment(selectedFragment);

                    return true;
                }
            };

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

    public ArrayList<Course> getCourses() {
        return allCourses;
    }

    public Course getCourse(int position) {
        return getCourses().get(position);
    }

    public void setFilteredCourses(List<Course> filteredCourses) {
        this.filteredCourses = filteredCourses;
    }

    public List<Course> getFilteredCourses() {
        return filteredCourses;
    }

    public Course getFilteredCourse(int position) {
        return getFilteredCourses().get(position);
    }

    //    public Course getCourse(int position) {
//        return getCoursesFromDatabase().get(position);
//    }

    public void navigateToFragment(Fragment fragment) {

        // simpele fragment navigation
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, null)
                .setReorderingAllowed(true)
                .addToBackStack("name") // name can be null
                .commit();


        // navigation component manier NOG NIET WERKEND
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        NavHostFragment navHostFragment =
//                (NavHostFragment) fragmentManager.findFragmentById(fragment.getId());
//        NavController navController = navHostFragment.getNavController();

    }



    public ArrayList<String> getCourseNamesFromDatabase() {
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

    private boolean convertIntToBoolean (int number) {
        return number != 0;
    }

    public ArrayList<Course> getCoursesFromDatabase() {
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(this);

        Cursor rs = dbHelper.query(DatabaseInfo.CourseTables.COURSETABLE, new String[]{"*"},
                null, null, null, null, null);

        ArrayList<Course> courses = new ArrayList<>();
        if(rs.moveToFirst()) {
            while (!rs.isAfterLast()) {
                String year = (String) rs.getString(rs.getColumnIndex("year"));
                String period = (String) rs.getString(rs.getColumnIndex("period"));
                String name = (String) rs.getString(rs.getColumnIndex("name"));
                boolean isOptional = convertIntToBoolean((int) rs.getInt(rs.getColumnIndex("isOptional")));
                String ects = (String) rs.getString(rs.getColumnIndex("ects"));
                String grade = (String) rs.getString(rs.getColumnIndex("grade"));
                String notes = (String) rs.getString(rs.getColumnIndex("notes"));

                Course course = new Course(year, period, name, ects, isOptional, grade, notes);
                courses.add(course);
                rs.moveToNext();

                Log.d("boolean: ", String.valueOf(isOptional));
                Log.d("year: ", year);
            }
        }

        String aantal_rijen = String.valueOf(rs.getCount());
        Log.d("aantal rijen: ", aantal_rijen);
//        Snackbar.make(this.findViewById(android.R.id.content), "total courses: " + aantal_rijen,
//                Snackbar.LENGTH_LONG).setAction("", null).show();

        return courses;
    }


    public void setupTextViewsCourseFragment(Course course) {

        Log.d("course name: ", course.getName());

        TextView courseName = (TextView) findViewById(R.id.tv_courseName);
        TextView year = (TextView) findViewById(R.id.tv_year);
        TextView ects = (TextView) findViewById(R.id.tv_ects);
        TextView isOptional = (TextView) findViewById(R.id.tv_isOptional);
        TextView period = (TextView) findViewById(R.id.tv_period);
        TextView notes = (TextView) findViewById(R.id.tv_notes);

        courseName.setText(course.getName());
        year.setText(course.getYear());
        ects.setText(course.getEcts());
        period.setText(course.getPeriod());
        notes.setText(course.getNotes());

        if (course.isOptional()) {
            isOptional.setText("keuzevak");
        } else {
            isOptional.setText("verplicht vak");
        }
    }

}