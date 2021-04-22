package com.example.slbapp.database;

public class DatabaseInfo {

    public class CourseTables {
        public static final String COURSETABLE = "CourseTable";   // NAAM VAN JE TABEL
    }

    public class CourseColumn {
        public static final String YEAR = "year";
        public static final String PERIOD  = "period";	// VASTE WAARDES
        public static final String NAME = "name";	// NAAM VAN DE KOLOMMEN
        public static final String ECTS = "ects";	// FINAL !
        public static final String GRADE = "grade";	//
        public static final String ISOPTIONAL = "isOptional";
        public static final String NOTES = "notes";
    }

    public class DateTables {
        public static final String DATETABLE = "DateTable";
    }

    public class DateColumn {
        public static final String DATE_UPDATED = "DateUpdated";
    }
}
