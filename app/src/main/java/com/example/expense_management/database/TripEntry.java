package com.example.expense_management.database;

public class TripEntry {
    public static final String TABLE_NAME = "trip";
    public static final String COL_ID = "id";
    public static final String COL_TRIP_NAME = "trip_name";
    public static final String COL_DESTINATION = "destination";
    public static final String COL_DEPARTURE_DATE = "departure_date";
    public static final String COL_DEPARTURE_TIME = "departure_time";
    public static final String COL_PURPOSE = "purpose";
    public static final String COL_RISK_ASSESSMENT = "risk_assessment";
    public static final String COL_DESCRIPTION = "description";

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY," +
                    COL_TRIP_NAME + " TEXT NOT NULL," +
                    COL_DESTINATION + " TEXT NOT NULL," +
                    COL_DEPARTURE_DATE + " TEXT NOT NULL," +
                    COL_DEPARTURE_TIME + " TEXT NOT NULL," +
                    COL_PURPOSE + " TEXT NOT NULL," +
                    COL_RISK_ASSESSMENT + " INTEGER NULL," +
                    COL_DESCRIPTION + " TEXT NULL)" ;

    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}