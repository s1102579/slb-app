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
        setupFragment(savedInstanceState);
    }

    public static MainActivity getInstance() {
        return instance;
    }

    private void setupFragment(Bundle savedInstanceState) {
        setContentView(R.layout.main_activity);

        if (savedInstanceState == null) {
            setupCoursesStore();
            setupBottomNavigation();
        }
    }

    private void setupCoursesStore() {
        coursesStore = new CoursesStore(this, new CoursesCallback() {
            @Override
            public void onCallback(List<Course> courses) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, MainFragment.newInstance())
                        .commitNow();
                instance = getInstance();
            }
        });
    }

    private void setupBottomNavigation() {

        BottomNavigationView.OnNavigationItemSelectedListener navListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;

                        switch(item.getItemId()) {
                            case R.id.nav_home:
                                selectedFragment = MainFragment.newInstance();
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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
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