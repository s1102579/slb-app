package com.example.slbapp;

import com.example.slbapp.models.Course;

import java.util.ArrayList;

interface CoursesCallback {
    void onCallback(ArrayList<Course> courses);
}

interface DateCallback {
    void onCallback(long dateUpdated);
}
