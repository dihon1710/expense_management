package com.example.expense_management.models;

import java.io.Serializable;

public class Expense implements Serializable {
    protected long _id;
    protected String _expenseType;
    protected int _amount;
    protected String _comment;
    protected String _date;
    protected String _time;
    protected long _tripId;

    public Expense() {
        _id = -1;
        _expenseType = null;
        _amount = -1;
        _date = null;
        _time = null;
        _comment = null;
        _tripId = -1;
    }

    public Expense(long id, String expenseType, int amount, String date, String time, String comment, long tripId) {
        _id = id;
        _expenseType = expenseType;
        _amount = amount;
        _date = date;
        _time = time;
        _comment = comment;
        _tripId = tripId;
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        _id = id;
    }

    public String getExpenseType() {
        return _expenseType;
    }

    public void setExpenseType(String expenseType) {
        _expenseType = expenseType;
    }

    public int getAmount() { return _amount; }

    public void setAmount(int amount) { _amount = amount; }

    public String getDate() { return _date; }

    public void setDate(String date) { _date = date; }

    public String getTime() { return _time; }

    public void setTime(String time) { _time = time; }

    public String getComment() { return _comment; }

    public void setComment(String comment) { _comment = comment; }

    public long getTripId() { return _tripId; }

    public void setTripId(long tripId) { _tripId = tripId; }

    public boolean isEmpty() {
        if (-1 == _id && null == _expenseType && -1 == _amount && null == _date && null == _time && null == _comment && -1 == _tripId)
            return true;

        return false;
    }

    @Override
    public String toString() {
        return "[" + _expenseType + "][" + _amount + "][" + _date + "] " + _comment;
    }
}