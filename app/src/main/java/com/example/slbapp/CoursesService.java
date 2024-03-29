package com.example.slbapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.slbapp.database.DatabaseHelper;
import com.example.slbapp.database.DatabaseInfo;
import com.example.slbapp.models.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CoursesService {

    private List<Course> allCourses = new ArrayList<>();
    private List<Course> firebaseCourses = new ArrayList<>();
    private List<Course> filteredCourses = new ArrayList<>();
    private Context context;
    private CoursesCallback coursesCallback;
    private DatabaseReference myRef;
    private DateService dateService;
    private int teller = 0;
    private boolean databaseIsEmpty = false;

    public CoursesService() {}

    public CoursesService(Context context, CoursesCallback coursesCallback) {
        setupFirebaseDatabase();
        initPrivateVariables(context, coursesCallback);
        handleEmptyDatabase();
        setupDateService();
    }

    private void initPrivateVariables(Context context, CoursesCallback coursesCallback) {
        this.context = context;
        this.coursesCallback = coursesCallback;
        allCourses = getCoursesFromDatabase();
        filteredCourses = allCourses;
    }

    private void setupFirebaseDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://slb-app-2a31b-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference("courses");
    }

    private void handleEmptyDatabase() {
        Log.d("handleEmptyDatabase", "start");
        if(allCourses.size() == 0) {

            Log.d("handleEmptyDatabase", "database is empty");
            databaseIsEmpty = true;

           getCoursesFromFirebase(new CoursesCallback() {
                @Override
                public void onCallback(List<Course> courses) {
                    addAllcoursesToDatabase(firebaseCourses);
                    setFilteredCourses(firebaseCourses);
                    allCourses = getCoursesFromDatabase();
                    dateService.setDateInDatabase();
                    coursesCallback.onCallback(firebaseCourses);
                }
            });
        }
        else {
            coursesCallback.onCallback(allCourses);
        }
    }

    private void addAllcoursesToDatabase(List<Course> courses) {
        for (Course course: courses) {
            addCourseToDatabase(course, new Date().getTime());
        }
    }

    private void setupDateService() {
        dateService = new DateService(context, new DateCallback() {
            @Override
            public void onCallback(long dateUpdated) {
                handleOutdatedLocalDatabase();
            }
        });
    }

    public void handleOutdatedLocalDatabase() {
        if (dateService.isDatabaseOutdated() && !databaseIsEmpty) {
            Log.d("isDatabaseOutdated", "true");

            DatabaseHelper dbhelper = DatabaseHelper.getHelper(context);

            dbhelper.resetCourses();
            getCoursesFromFirebase(new CoursesCallback() {
                @Override
                public void onCallback(List<Course> courses) {
                    addAllcoursesToDatabase(firebaseCourses);
                    setFilteredCourses(firebaseCourses);
                    allCourses = getCoursesFromDatabase();
                    dateService.updateDateInDatabase(dateService.getDateUpdated());
                }
            });
        } else {
            Log.d("isDatabaseOutdated", "false");
        }
    }

    private void getCoursesFromFirebase(CoursesCallback callback) {
        ArrayList<Course> courses = new ArrayList<Course>();
        myRef.addValueEventListener(new ValueEventListener() {

            // LETOP DIT MOET WACHTEN OP DE DATA VAN FIREBASE EN IS ASYNCHRONOUS
            // https://stackoverflow.com/questions/47847694/how-to-return-datasnapshot-value-as-a-result-of-a-method/47853774
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (teller <= 0) {
                    for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                        Course course = courseSnapshot.getValue(Course.class);
                        courses.add(course);
                        Log.d("read firebase", "course is: " + course.getName());
                    }
                    teller++;
                    firebaseCourses = courses;
                    callback.onCallback(courses);
                    coursesCallback.onCallback(courses);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("read fireBase failed", "Failed to read value.", error.toException());
            }
        });
    }

    public void addCourseToFireBase(Course course, long date) {
        DatabaseReference coursesRef = myRef.child(course.getName());
        coursesRef.setValue(course);

        dateService.setDateUpdatedInFirebase(date);
    }

    public ArrayList<String> getAllCourseNames() {
        ArrayList<String> allCourseNames = new ArrayList<>();

        for(int i = 0; i < allCourses.size(); i++) {
            allCourseNames.add(allCourses.get(i).getName());
        }

        return allCourseNames;
    }

    public void setAllCourses(ArrayList<Course> courses) {
        this.allCourses = courses;
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

    public int getPointsFromFinishedCourses() {
        int punten = 0;

        for (Course course : allCourses) {

            int coursePoints = Integer.parseInt(course.getEcts());
            try {
                double num = Double.parseDouble(course.getGrade());

                if(num >= 5.5) {

                    punten += coursePoints;
                }

            } catch (NumberFormatException e) {
                if (course.getGrade().equals("V")) {
                    punten += coursePoints;
                }
            }
        }
        return punten;
    }

    public void addCourseToDatabase(Course course, long date) {
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(context);

        ContentValues values = new ContentValues();
        values.put(DatabaseInfo.CourseColumn.YEAR, course.getYear());
        values.put(DatabaseInfo.CourseColumn.PERIOD, course.getPeriod());
        values.put(DatabaseInfo.CourseColumn.NAME, course.getName());
        values.put(DatabaseInfo.CourseColumn.ECTS, course.getEcts());
        values.put(DatabaseInfo.CourseColumn.ISOPTIONAL, course.isOptional());
        values.put(DatabaseInfo.CourseColumn.GRADE, course.getGrade());
        values.put(DatabaseInfo.CourseColumn.NOTES, course.getNotes());

        dbHelper.insert(DatabaseInfo.CourseTables.COURSETABLE, null, values);

        this.allCourses.add(course);
        this.filteredCourses.add(course);

        dateService.updateDateInDatabase(date);
    }

    public void updateCourseToDatabase(Course course, long date) {
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(context);

        ContentValues values = new ContentValues();
        values.put(DatabaseInfo.CourseColumn.YEAR, course.getYear());
        values.put(DatabaseInfo.CourseColumn.PERIOD, course.getPeriod());
        values.put(DatabaseInfo.CourseColumn.NAME, course.getName());
        values.put(DatabaseInfo.CourseColumn.ECTS, course.getEcts());
        values.put(DatabaseInfo.CourseColumn.ISOPTIONAL, course.isOptional());
        values.put(DatabaseInfo.CourseColumn.GRADE, course.getGrade());
        values.put(DatabaseInfo.CourseColumn.NOTES, course.getNotes());

        Log.d("name_value", values.get("name").toString());

        dbHelper.update(DatabaseInfo.CourseTables.COURSETABLE, values);

        setAllCourses(getCoursesFromDatabase());
        setFilteredCourses(allCourses);

        dateService.updateDateInDatabase(date);
    }

    private boolean convertIntToBoolean (int number) {
        return number != 0;
    }

    public ArrayList<Course> getCoursesFromDatabase() {
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(context);

        Cursor rs = dbHelper.query(DatabaseInfo.CourseTables.COURSETABLE, new String[]{"*"},
                null, null, null, null, null);

        ArrayList<Course> courses = new ArrayList<>();
        if (rs.moveToFirst()) {
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

            }
        }
        String aantal_rijen = String.valueOf(rs.getCount());
        Log.d("aantal rijen: ", aantal_rijen);

        return courses;
    }
}
