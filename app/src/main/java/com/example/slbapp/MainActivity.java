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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static SQLiteDatabase mSQLDB;
    public CoursesStore coursesStore;
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

        coursesStore = new CoursesStore(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        setupFirebaseDatabase();

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
                        case R.id.nav_addCourse:
                            selectedFragment = new CourseFragment();
                            break;
                    }

                    navigateToFragment(selectedFragment);

                    return true;
                }
            };

    private void setupFirebaseDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://slb-app-2a31b-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("courses");

        // read courses from fireBase
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayList<Course> courses = new ArrayList<>();
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    Course course = courseSnapshot.getValue(Course.class);
                    courses.add(course);
                }
                String firstCourse = courses.get(0).getName();
                Log.d("read firebase", "course is: " + firstCourse);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("read fireBase failed", "Failed to read value.", error.toException());
            }
        });

    }

    public void navigateToFragment(Fragment fragment) {

        // simpele fragment navigation
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, null)
                .setReorderingAllowed(true)
                .addToBackStack("name") // name can be null
                .commit();

    }




}