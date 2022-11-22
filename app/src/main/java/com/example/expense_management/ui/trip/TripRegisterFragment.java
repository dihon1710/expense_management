package com.example.expense_management.ui.trip;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;;
import com.example.expense_management.R;
import com.example.expense_management.database.ExpemanaDAO;
import com.example.expense_management.models.Trip;
import com.example.expense_management.ui.dialog.CalendarFragment;
import com.example.expense_management.ui.dialog.TimePickerFragment;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class TripRegisterFragment extends Fragment
        implements TripRegisterConfirmFragment.FragmentListener, CalendarFragment.FragmentListener, TimePickerFragment.FragmentListener {
    public static final String ARG_PARAM_TRIP = "trip";

    protected EditText fmTripRegisterTripName, fmTripRegisterDestination, fmTripRegisterDepartureDate, fmTripRegisterDepartureTime, fmTripRegisterDescription;
    protected LinearLayout fmTripRegisterLinearLayout;
    protected SwitchMaterial fmTripRegisterRiskAssessment;
    protected TextView fmTripRegisterError;
    protected Button fmTripRegisterButton;
    protected RadioButton fmTripRegisterRadioButtonStudy, fmTripRegisterRadioButtonWork, fmTripRegisterRadioButtonTravel;

    protected ExpemanaDAO _db;

    public TripRegisterFragment() {}

    @Override
    public void sendFromCalendarFragment(String date) {
        fmTripRegisterDepartureDate.setText(date);
    }

    @Override
    public void sendFromTimePickerFragment(String time) { fmTripRegisterDepartureTime.setText(time); }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new ExpemanaDAO(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_register,   container, false);

        fmTripRegisterError = view.findViewById(R.id.fmTripRegisterError);
        fmTripRegisterRadioButtonStudy = view.findViewById(R.id.fmTripRegisterRadioButtonStudy);
        fmTripRegisterRadioButtonWork = view.findViewById(R.id.fmTripRegisterRadioButtonWork);
        fmTripRegisterRadioButtonTravel = view.findViewById(R.id.fmTripRegisterRadioButtonTravel);
        fmTripRegisterTripName = view.findViewById(R.id.fmTripRegisterTripName);
        fmTripRegisterDestination = view.findViewById(R.id.fmTripRegisterDestination);
        fmTripRegisterDepartureDate = view.findViewById(R.id.fmTripRegisterDepartureDate);
        fmTripRegisterDepartureTime = view.findViewById(R.id.fmTripRegisterConfirmDepartureTime);
        fmTripRegisterRiskAssessment = view.findViewById(R.id.fmTripRegisterRiskAssessment);
        fmTripRegisterDescription = view.findViewById(R.id.fmTripRegisterDescription);
        fmTripRegisterButton = view.findViewById(R.id.fmTripRegisterButton);
        fmTripRegisterLinearLayout = view.findViewById(R.id.fmTripRegisterLinearLayout);

        // Show Calendar for choosing a date.
        fmTripRegisterDepartureDate.setOnTouchListener((v, motionEvent) -> showCalendar(motionEvent));

        // Show Time Picker for choosing time.
        fmTripRegisterDepartureTime.setOnTouchListener((v, motionEvent) -> showTimeDialog(motionEvent));

        // Update current trip.
        if (getArguments() != null) {
            Trip trip = (Trip) getArguments().getSerializable(ARG_PARAM_TRIP);

            fmTripRegisterTripName.setText(trip.getTripName());
            fmTripRegisterDestination.setText(trip.getDestination());
            fmTripRegisterDepartureDate.setText(trip.getDepartureDate());
            fmTripRegisterDepartureTime.setText(trip.getDepartureTime());
            getPurposeValue();
            fmTripRegisterRiskAssessment.setChecked(trip.getRiskAssessment() == 1 ? true : false);
            fmTripRegisterDescription.setText(trip.getDescription());

            fmTripRegisterButton.setText(R.string.label_update);
            fmTripRegisterButton.setOnClickListener(v -> update(trip.getId()));

            return view;
        }

        // Create new trip.
        fmTripRegisterButton.setOnClickListener(v -> register());

        return view;
    }

    protected void register() {
        if (isValidForm()) {
            Trip trip = getTripFromInput(-1);

            new TripRegisterConfirmFragment(trip).show(getChildFragmentManager(), null);
            return;
        }
    }

    protected void update(long id) {
        if (isValidForm()) {
            Trip trip = getTripFromInput(id);
            long status = _db.updateTrip(trip);

            FragmentListener listener = (FragmentListener) getParentFragment();
            listener.sendFromTripRegisterFragment(status);
            return;
        }
    }

    protected boolean showCalendar(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new CalendarFragment().show(getChildFragmentManager(), null);
        }
        return false;
    }

    protected boolean showTimeDialog(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new TimePickerFragment().show(getChildFragmentManager(), null);
            return true;
        }
        return false;
    }

    protected String getPurposeValue() {
        String purpose = "";

        if(fmTripRegisterRadioButtonStudy.isChecked()){
            purpose = getString(R.string.label_study);
        } else if(fmTripRegisterRadioButtonWork.isChecked()){
            purpose = getString(R.string.label_work);
        } else if(fmTripRegisterRadioButtonTravel.isChecked()){
            purpose = getString(R.string.label_travel);
        }

        return purpose;
    }

    protected Trip getTripFromInput(long id) {
        String tripName = fmTripRegisterTripName.getText().toString();
        String destination = fmTripRegisterDestination.getText().toString();
        String departureDate = fmTripRegisterDepartureDate.getText().toString();
        String departureTime = fmTripRegisterDepartureTime.getText().toString();
        String purpose = getPurposeValue();
        int riskAssessment = fmTripRegisterRiskAssessment.isChecked() ? 1 : 0;
        String description = fmTripRegisterDescription.getText().toString();

        return new Trip(id, tripName, destination, departureDate, departureTime, purpose, riskAssessment, description);
    }

    protected boolean isValidForm() {
        boolean isValid = true;

        String error = "";
        String tripName = fmTripRegisterTripName.getText().toString();
        String destination = fmTripRegisterDestination.getText().toString();
        String departureDate = fmTripRegisterDepartureDate.getText().toString();
        String departureTime = fmTripRegisterDepartureTime.getText().toString();
        String purpose = getPurposeValue();

        if (tripName == null || tripName.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_name) + "\n";
            isValid = false;
        }
        if (destination == null || destination.trim().isEmpty()){
            error += "* " + getString(R.string.error_blank_destination) + "\n";
            isValid = false;
        }
        if (departureDate == null || departureDate.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_departure_date) + "\n";
            isValid = false;
        }
        if (departureTime == null || departureTime.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_departure_time) + "\n";
        }
        if (purpose == null || purpose.trim().isEmpty()){
            error += "* " + getString(R.string.error_blank_purpose) + "\n";
        }
        fmTripRegisterError.setText(error);
        return isValid;
    }

    @Override
    public void sendFromTripRegisterConfirmFragment(long status) {
        switch ((int) status) {
            case -1:
                Toast.makeText(getContext(), R.string.notification_create_fail, Toast.LENGTH_SHORT).show();
                return;
            default:
                Toast.makeText(getContext(), R.string.notification_create_success, Toast.LENGTH_SHORT).show();

                fmTripRegisterTripName.setText("");
                fmTripRegisterDestination.setText("");
                fmTripRegisterDepartureDate.setText("");
                fmTripRegisterDepartureTime.setText("");
                fmTripRegisterRiskAssessment.setChecked(false);

                fmTripRegisterRadioButtonStudy.setChecked(false);
                fmTripRegisterRadioButtonWork.setChecked(false);
                fmTripRegisterRadioButtonTravel.setChecked(false);

                fmTripRegisterDescription.setText("");

                fmTripRegisterTripName.requestFocus();
        }
    }

    public interface FragmentListener {
        void sendFromTripRegisterFragment(long status);
    }
}