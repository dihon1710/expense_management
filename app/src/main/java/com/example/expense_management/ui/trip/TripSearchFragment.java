package com.example.expense_management.ui.trip;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.expense_management.R;
import com.example.expense_management.models.Trip;
import com.example.expense_management.ui.dialog.CalendarFragment;
import com.example.expense_management.ui.dialog.TimePickerFragment;

public class TripSearchFragment extends DialogFragment implements CalendarFragment.FragmentListener, TimePickerFragment.FragmentListener {
    protected EditText fmTripSearchTripName, fmTripSearchDestination, fmTripSearchDepartureDate,
                       fmTripSearchDepartureTime, fmTripSearchPurpose;
    protected Button fmTripSearchButtonCancel, fmTripSearchButtonSearch;

    public TripSearchFragment() {}

    @Override
    public void sendFromCalendarFragment(String date) { fmTripSearchDepartureDate.setText(date); }

    @Override
    public void sendFromTimePickerFragment(String time) { fmTripSearchDepartureTime.setText(time); }

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_search, container, false);

        fmTripSearchTripName = view.findViewById(R.id.fmTripSearchTripName);
        fmTripSearchDestination = view.findViewById(R.id.fmTripSearchDestination);
        fmTripSearchDepartureDate = view.findViewById(R.id.fmTripSearchDepartureDate);
        fmTripSearchDepartureTime = view.findViewById(R.id.fmTripSearchDepartureTime);
        fmTripSearchPurpose = view.findViewById(R.id.fmTripSearchPurpose);
        fmTripSearchButtonCancel = view.findViewById(R.id.fmTripSearchButtonCancel);
        fmTripSearchButtonSearch = view.findViewById(R.id.fmTripSearchButtonSearch);

        fmTripSearchButtonSearch.setOnClickListener(v -> search());
        fmTripSearchButtonCancel.setOnClickListener(v -> dismiss());

        fmTripSearchDepartureDate.setOnTouchListener((v, motionEvent) -> showCalendar(motionEvent));
        fmTripSearchDepartureTime.setOnTouchListener((v, motionEvent) -> showTimeDialog(motionEvent));

        return view;
    }

    protected void search() {
        Trip _trip = new Trip();

        String tripName = fmTripSearchTripName.getText().toString();
        String destination = fmTripSearchDestination.getText().toString();
        String departureDate = fmTripSearchDepartureDate.getText().toString();
        String departureTime = fmTripSearchDepartureTime.getText().toString();
        String purpose = fmTripSearchPurpose.getText().toString();

        if (tripName != null && !tripName.trim().isEmpty())
            _trip.setTripName(tripName);

        if (destination != null && !destination.trim().isEmpty())
            _trip.setDestination(destination);

        if (departureDate != null && !departureDate.trim().isEmpty())
            _trip.setDepartureDate(departureDate);

        if (departureTime != null && !departureTime.trim().isEmpty())
            _trip.setDepartureTime(departureTime);

        if (purpose != null && !purpose.trim().isEmpty())
            _trip.setPurpose(purpose);

        FragmentListener listener = (FragmentListener) getParentFragment();
        listener.sendFromTripSearchFragment(_trip);

        dismiss();
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

    public interface FragmentListener {
        void sendFromTripSearchFragment(Trip trip);
    }
}