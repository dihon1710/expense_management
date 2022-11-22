package com.example.expense_management.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class ExpemanaDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Expemana";
    public static final int DATABASE_VERSION = 1;

    public ExpemanaDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TripEntry.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(ExpenseEntry.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(ExpenseEntry.SQL_DELETE_TABLE);
        sqLiteDatabase.execSQL(TripEntry.SQL_DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }
}