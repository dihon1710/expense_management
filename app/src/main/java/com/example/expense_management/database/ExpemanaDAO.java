package com.example.expense_management.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import com.example.expense_management.models.Expense;
import com.example.expense_management.models.Trip;

public class ExpemanaDAO {
    protected ExpemanaDbHelper expemanaDbHelper;
    protected SQLiteDatabase dbWrite, dbRead;

    public ExpemanaDAO(Context context) {
        expemanaDbHelper = new ExpemanaDbHelper(context);

        dbRead = expemanaDbHelper.getReadableDatabase();
        dbWrite = expemanaDbHelper.getWritableDatabase();
    }

    public void close() {
        dbRead.close();
        dbWrite.close();
    }

    public void reset() {
        expemanaDbHelper.onUpgrade(dbWrite, 0, 0);
    }

    // Trip.
    public long insertTrip(Trip trip) {
        ContentValues values = getTripValues(trip);

        return dbWrite.insert(TripEntry.TABLE_NAME, null, values);
    }

    public ArrayList<Trip> getTripList(Trip trip, String orderByColumn, boolean isDesc) {
        String orderBy = getOrderByString(orderByColumn, isDesc);

        String selection = null;
        String[] selectionArgs = null;

        if (null != trip) {
            selection = "";
            ArrayList<String> conditionList = new ArrayList<String>();

            if (trip.getTripName() != null && !trip.getTripName().trim().isEmpty()) {
                selection += " AND " + TripEntry.COL_TRIP_NAME + " LIKE ?";
                conditionList.add("%" + trip.getTripName() + "%");
            }
            if (trip.getDestination() !=null && !trip.getDestination().trim().isEmpty()) {
                selection += " AND " + TripEntry.COL_DESTINATION + " = ?";
                conditionList.add(trip.getDestination());
            }
            if (trip.getDepartureDate() != null && !trip.getDepartureDate().trim().isEmpty()) {
                selection += " AND " + TripEntry.COL_DEPARTURE_DATE + " = ?";
                conditionList.add(trip.getDepartureDate());
            }
            if (trip.getDepartureTime() != null && !trip.getDepartureTime().trim().isEmpty()) {
                selection += " AND " + TripEntry.COL_DEPARTURE_TIME + " = ?";
                conditionList.add(trip.getDepartureTime());
            }
            if (trip.getPurpose() != null && !trip.getPurpose().trim().isEmpty()) {
                selection += " AND " + TripEntry.COL_PURPOSE + " = ?";
                conditionList.add(trip.getPurpose());
            }
            if (trip.getRiskAssessment() != -1) {
                selection += " AND " + TripEntry.COL_RISK_ASSESSMENT + " = ?";
                conditionList.add(String.valueOf(trip.getRiskAssessment()));
            }
            if (trip.getDescription() != null && !trip.getDescription().trim().isEmpty()) {
                selection += " AND " + TripEntry.COL_DESCRIPTION + " = ?";
                conditionList.add(trip.getDescription());
            }
            if (!selection.trim().isEmpty()) {
                selection = selection.substring(5);
            }
            selectionArgs = conditionList.toArray(new String[conditionList.size()]);
        }
        return getTripFromDB(null, selection, selectionArgs, null, null, orderBy);
    }

    public Trip getTripById(long id) {
        String selection = TripEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return getTripFromDB(null, selection, selectionArgs, null, null, null).get(0);
    }

    public long updateTrip(Trip trip) {
        ContentValues values = getTripValues(trip);

        String selection = TripEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(trip.getId())};

        return dbWrite.update(TripEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public long deleteTrip(long id) {
        String selection = TripEntry.COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return dbWrite.delete(TripEntry.TABLE_NAME, selection, selectionArgs);
    }

    protected String getOrderByString(String orderByColumn, boolean isDesc) {
        if (orderByColumn == null || orderByColumn.trim().isEmpty())
            return null;

        if (isDesc)
            return orderByColumn.trim() + " DESC";
        return orderByColumn.trim();
    }

    protected ContentValues getTripValues(Trip trip) {
        ContentValues values = new ContentValues();

        values.put(TripEntry.COL_TRIP_NAME, trip.getTripName());
        values.put(TripEntry.COL_DESTINATION, trip.getDestination());
        values.put(TripEntry.COL_DEPARTURE_DATE, trip.getDepartureDate());
        values.put(TripEntry.COL_DEPARTURE_TIME, trip.getDepartureTime());
        values.put(TripEntry.COL_PURPOSE, trip.getPurpose());
        values.put(TripEntry.COL_RISK_ASSESSMENT, trip.getRiskAssessment());
        values.put(TripEntry.COL_DESCRIPTION, trip.getDescription());

        return values;
    }

    protected ArrayList<Trip> getTripFromDB(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<Trip> list = new ArrayList<>();

        Cursor cursor = dbRead.query(TripEntry.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);

        while (cursor.moveToNext()) {
            Trip tripItem = new Trip();

            tripItem.setId(cursor.getLong(cursor.getColumnIndexOrThrow(TripEntry.COL_ID)));
            tripItem.setTripName(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COL_TRIP_NAME)));
            tripItem.setDestination(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COL_DESTINATION)));
            tripItem.setDepartureDate(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COL_DEPARTURE_DATE)));
            tripItem.setDepartureTime(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COL_DEPARTURE_TIME)));
            tripItem.setPurpose(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COL_PURPOSE)));
            tripItem.setRiskAssessment(cursor.getInt(cursor.getColumnIndexOrThrow(TripEntry.COL_RISK_ASSESSMENT)));
            tripItem.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(TripEntry.COL_DESCRIPTION)));

            list.add(tripItem);
        }
        cursor.close();
        return list;
    }

    // Expense.
    public long insertExpense(Expense expense) {
        ContentValues values = getExpenseValues(expense);

        return dbWrite.insert(ExpenseEntry.TABLE_NAME, null, values);
    }

    public ArrayList<Expense> getExpenseList(Expense expense, String orderByColumn, boolean isDesc) {
        String orderBy = getOrderByString(orderByColumn, isDesc);

        String selection = null;
        String[] selectionArgs = null;

        if (expense != null) {
            selection = "";
            ArrayList<String> conditionList = new ArrayList<String>();

            if (expense.getComment() != null && !expense.getComment().trim().isEmpty()) {
                selection += " AND " + ExpenseEntry.COL_COMMENT + " LIKE ?";
                conditionList.add("%" + expense.getComment() + "%");
            }
            if (expense.getDate() != null && !expense.getDate().trim().isEmpty()) {
                selection += " AND " + ExpenseEntry.COL_DATE + " = ?";
                conditionList.add(expense.getDate());
            }
            if (expense.getTime() != null && !expense.getTime().trim().isEmpty()) {
                selection += " AND " + ExpenseEntry.COL_TIME + " = ?";
                conditionList.add(expense.getTime());
            }
                if (expense.getExpenseType() != null && !expense.getExpenseType().trim().isEmpty()) {
                selection += " AND " + ExpenseEntry.COL_EXPENSE_TYPE + " = ?";
                conditionList.add(expense.getExpenseType());
            }
            if (expense.getAmount() != -1) {
                selection += " AND " + ExpenseEntry.COL_AMOUNT + " = ?";
                conditionList.add(String.valueOf(expense.getAmount()));
            }
            if (expense.getTripId() != -1) {
                selection += " AND " + ExpenseEntry.COL_TRIP_ID + " = ?";
                conditionList.add(String.valueOf(expense.getTripId()));
            }
            if (!selection.trim().isEmpty()) {
                selection = selection.substring(5);
            }
            selectionArgs = conditionList.toArray(new String[conditionList.size()]);
        }
        return getExpenseFromDB(null, selection, selectionArgs, null, null, orderBy);
    }

    protected ContentValues getExpenseValues(Expense expense) {
        ContentValues values = new ContentValues();

        values.put(ExpenseEntry.COL_COMMENT, expense.getComment());
        values.put(ExpenseEntry.COL_DATE, expense.getDate());
        values.put(ExpenseEntry.COL_TIME, expense.getTime());
        values.put(ExpenseEntry.COL_EXPENSE_TYPE, expense.getExpenseType());
        values.put(ExpenseEntry.COL_AMOUNT, expense.getAmount());
        values.put(ExpenseEntry.COL_TRIP_ID, expense.getTripId());

        return values;
    }

    protected ArrayList<Expense> getExpenseFromDB(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        ArrayList<Expense> list = new ArrayList<>();

        Cursor cursor = dbRead.query(ExpenseEntry.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);

        while (cursor.moveToNext()) {
            Expense requestItem = new Expense();

            requestItem.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ExpenseEntry.COL_ID)));
            requestItem.setComment(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseEntry.COL_COMMENT)));
            requestItem.setDate(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseEntry.COL_DATE)));
            requestItem.setTime(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseEntry.COL_TIME)));
            requestItem.setExpenseType(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseEntry.COL_EXPENSE_TYPE)));
            requestItem.setAmount(cursor.getInt(cursor.getColumnIndexOrThrow(ExpenseEntry.COL_AMOUNT)));
            requestItem.setTripId(cursor.getLong(cursor.getColumnIndexOrThrow(ExpenseEntry.COL_TRIP_ID)));

            list.add(requestItem);
        }
        cursor.close();
        return list;
    }
}