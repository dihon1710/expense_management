package com.example.expense_management.models;

import java.io.Serializable;

public class Trip implements Serializable {
    protected long _id;
    protected String _tripName;
    protected String _destination;
    protected String _departureDate;
    protected String _departureTime;
    protected String _purpose;
    protected int _riskAssessment;
    protected String _description;

    public Trip() {
        _id = -1;
        _tripName = null;
        _destination = null;
        _departureDate = null;
        _departureTime = null;
        _purpose = null;
        _riskAssessment = -1;
        _description = null;
    }

    public Trip(long id, String tripName, String destination, String departureDate, String departureTime,
                String purpose, int riskAssessment, String description) {
        _id = id;
        _tripName = tripName;
        _destination = destination;
        _departureDate = departureDate;
        _departureTime = departureTime;
        _purpose = purpose;
        _riskAssessment = riskAssessment;
        _description = description;
    }

    public long getId() { return _id; }
    public void setId(long id) {
        _id = id;
    }

    public String getTripName() {
        return _tripName;
    }
    public void setTripName(String tripName) {
        _tripName = tripName;
    }

    public String getDestination() { return _destination; }
    public void setDestination(String destination) { _destination = destination; }

    public String getDepartureDate() {
        return _departureDate;
    }
    public void setDepartureDate(String departureDate) {
        _departureDate = departureDate;
    }

    public String getDepartureTime() { return _departureTime; }
    public void setDepartureTime(String departureTime) { _departureTime = departureTime; }

    public String getPurpose() { return _purpose; }
    public void setPurpose(String purpose) { _purpose = purpose; }

    public int getRiskAssessment() { return _riskAssessment; }
    public void setRiskAssessment(int riskAssessment) { _riskAssessment = riskAssessment; }

    public String getDescription() { return _description; }
    public void setDescription(String description) { _description = description; }

    public boolean isEmpty() {
        if (-1 == _id && null == _tripName && null == _destination && null == _departureDate && null == _departureTime
                                && -1 == _riskAssessment && null == _description  )
            return true;

        return false;
    }

    @Override
    public String toString() {
        return "[" + _departureDate + "] " + _tripName;
    }
}