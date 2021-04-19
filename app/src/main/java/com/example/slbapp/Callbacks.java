package com.example.slbapp;

import com.example.slbapp.models.Course;

import java.util.List;

interface CoursesCallback {
    void onCallback(List<Course> courses);
}

interface DateCallback {
    void onCallback(long dateUpdated);
}
