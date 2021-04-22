package com.example.slbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.slbapp.models.Course;
import com.example.slbapp.ui.main.ItemFragment;
import com.example.slbapp.ui.main.MainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public CoursesService coursesService;
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
        coursesService = new CoursesService(this, new CoursesCallback() {
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }
}