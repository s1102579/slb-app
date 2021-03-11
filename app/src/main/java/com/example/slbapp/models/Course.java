package com.example.slbapp.models;

public class Course {
    private String year;
    private String period;
    private String name;
    private String ects;
    private boolean isOptional;
    private String grade;
    private String notes;

    public Course(String year, String period, String courseName, String ects, boolean isOptional, String grade, String notes) {
        this.year = year;
        this.isOptional = isOptional;
        this.notes = notes;
        this.name = courseName;
        this.ects = ects;
        this.grade = grade;
        this.period = period;
    }

    public Course(String year, String period, String courseName, String ects, boolean isOptional, String grade) {
        this.year = year;
        this.isOptional = isOptional;
        this.name = courseName;
        this.ects = ects;
        this.grade = grade;
        this.period = period;
    }

    public Course() {}

    public String getPeriod() {
        return period;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEcts() {
        return ects;
    }

    public String getGrade() {
        return grade;
    }

    public String getYear() {
        return year;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean optional) {
        isOptional = optional;
    }

    public String getNotes() {
        return notes;
    }


}
