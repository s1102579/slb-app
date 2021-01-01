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

    public void setPeriod(String period) {
        this.period = period;
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

    public void setEcts(String ects) {
        this.ects = ects;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
