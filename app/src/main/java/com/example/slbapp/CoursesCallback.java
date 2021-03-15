package com.example.slbapp;

import com.example.slbapp.models.Course;

import java.util.ArrayList;

public interface CoursesCallback {
    void onCallback(ArrayList<Course> courses);
}
