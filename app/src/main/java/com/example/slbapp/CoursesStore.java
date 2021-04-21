

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

public class CoursesStore {

    private List<Course> allCourses = new ArrayList<>();
    private List<Course> firebaseCourses = new ArrayList<>();
    private List<Course> filteredCourses = new ArrayList<>();
    private Context context;
    private CoursesCallback coursesCallback;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DateService dateService;
    private int teller = 0;
    private boolean databaseIsEmpty = false;
//    private static CoursesStore instance;

    public CoursesStore() {

    }

    public CoursesStore(Context context, CoursesCallback coursesCallback) {
        setupFirebaseDatabase();
        initPrivateVariables(context, coursesCallback);
        handleEmptyDatabase();
        setupDateService();
    }

//    public static CoursesStore getInstance() {
//        if (instance == null) {
//            instance = new CoursesStore();
//        }
//        return instance;
//    }

    private void initPrivateVariables(Context context, CoursesCallback coursesCallback) {
        this.context = context;
        this.coursesCallback = coursesCallback;
        allCourses = getCoursesFromDatabase();
        filteredCourses = allCourses;
    }

    private void setupFirebaseDatabase() {
        database = FirebaseDatabase.getInstance("https://slb-app-2a31b-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference("courses");
    }

    public void getInstanceCoursesService() {

    }

    private void handleEmptyDatabase() {

        Log.d("handleEmptyDatabase", "start");

        if(allCourses.size() == 0) {

            Log.d("handleEmptyDatabase", "database is empty");
            databaseIsEmpty = true;

           getCoursesFromFirebase(new Date().getTime(), new CoursesCallback() {
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

//        CopyOnWriteArrayList<Course> list = new CopyOnWriteArrayList<>(courses);
//
//        Log.d("allCourses size", String.valueOf(list.size()));
//
//        for (Iterator<Course> iterator = list.iterator(); iterator.hasNext(); ) {
//
//            Course course = iterator.next();
//
//            Log.d("addAllcoursesToDatabase", "in loop");
//
//            addCourseToDatabase(course, new Date().getTime());
//
//            iterator.remove();

//        }

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
            getCoursesFromFirebase(new Date().getTime(), new CoursesCallback() {
                @Override
                public void onCallback(List<Course> courses) {
                    addAllcoursesToDatabase(firebaseCourses);
                    setFilteredCourses(firebaseCourses);
                    allCourses = getCoursesFromDatabase();
                    dateService.updateDateInDatabase(dateService.getDateUpdated());
                }
            });

        }
        else {
            Log.d("isDatabaseOutdated", "false");
        }

    }

    private void getCoursesFromFirebase(long date, CoursesCallback callback) {
        // read courses from fireBase
        ArrayList<Course> courses = new ArrayList<Course>();
        myRef.addValueEventListener(new ValueEventListener() {

            // LETOP DIT MOET WACHTEN OP DE DATA VAN FIREBASE EN IS ASYNCHRONOUS
            // https://stackoverflow.com/questions/47847694/how-to-return-datasnapshot-value-as-a-result-of-a-method/47853774
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
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
                    Log.d("test Async", "test");
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("read fireBase failed", "Failed to read value.", error.toException());
            }
        });
    }

    public void addCourseToFireBase(Course course, long date) {
        DatabaseReference coursesRef = myRef.child(course.getName());
        coursesRef.setValue(course);

        dateService.setDateUpdatedInFirebase(date);
    }

    private void addAllCoursesToFirebase(ArrayList<Course> courses, long date) {
        for (Course course: courses) {
            addCourseToFireBase(course, date);
        }
    }

    public List<Course> getCourses() {
        return allCourses;
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

    public Course getCourse(int position) {
        return getCourses().get(position);
    }

    public void setFilteredCourses(List<Course> filteredCourses) {
        this.filteredCourses = filteredCourses;
    }

    public void setCoursesCallback(List<Course> courses) {
        coursesCallback.onCallback(courses);
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
//        dateService.setDateInDatabase();

    }

    public void updateCourseToDatabase(Course course) {
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

        //TODO moet nog veranderen net zoals bij addCourseToDatabase
        setAllCourses(getCoursesFromDatabase());
        setFilteredCourses(allCourses);

    }

    public void getFromDatabase() {
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(context);

        Cursor rs = dbHelper.query(DatabaseInfo.CourseTables.COURSETABLE, new String[]{"*"},
                null, null, null, null, null);

        rs.moveToFirst();
        String name = (String) rs.getString(rs.getColumnIndex("name"));
        Log.d("Tim heeft gevonden=", "deze: "+ name);
    }

//    private void makeClassesFromJson() {
//        DatabaseHelper dbHelper = DatabaseHelper.getHelper(context);
//
////        JsonReader json = new JsonReader(new FileReader(readJSONFromAsset()));
//
////        dbHelper.dropCourses(mSQLDB);
//
//        // van bestand in project
//        String json = readJSONFromAsset("courses.json");
//
//        Log.d("json bestand", json);
//        Gson gson = new Gson();
//
//        Course[] courses = gson.fromJson(json, Course[].class);
//
//        for (Course cours : courses) {
//            addCourseToDatabase(cours);
//        }
//
//    }

    // haalt een json bestand van assets en zet die om in String
//    public String readJSONFromAsset(String filename) {
//        String json = null;
//        try {
//            InputStream is = getAssets().open(filename);
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        return json;
//    }

    public ArrayList<String> getCourseNamesFromDatabase() {
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(context);

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
//        Snackbar.make(findViewById(android.R.id.content), "total courses: " + rows_amount,
//                Snackbar.LENGTH_LONG).setAction("", null).show();

        return coursenames;
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
}
