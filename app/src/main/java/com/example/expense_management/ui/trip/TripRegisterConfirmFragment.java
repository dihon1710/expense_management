package com.example.expense_management.ui.trip;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.expense_management.R;
import com.example.expense_management.database.ExpemanaDAO;
import com.example.expense_management.models.Trip;

public class TripRegisterConfirmFragment extends DialogFragment {
    protected ExpemanaDAO _db;
    protected Trip _trip;
    protected Button fmTripRegisterConfirmButtonConfirm, fmTripRegisterConfirmButtonCancel;
    protected TextView fmTripRegisterConfirmTripName, fmTripRegisterConfirmDestination, fmTripRegisterConfirmDepartureDate, fmTripRegisterConfirmDepartureTime,
                       fmTripRegisterConfirmPurpose,fmTripRegisterConfirmRiskAssessment, fmTripRegisterConfirmDescription;

    public TripRegisterConfirmFragment() {
        _trip = new Trip();
    }

    public TripRegisterConfirmFragment(Trip trip) {
        _trip = trip;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new ExpemanaDAO(getContext());
    }

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
        View view = inflater.inflate(R.layout.fragment_trip_register_confirm, container, false);

        String tripName = getString(R.string.error_no_info);
        String destination = getString(R.string.error_no_info);
        String departureDate = getString(R.string.error_no_info);
        String departureTime = getString(R.string.error_no_info);
        String purpose = getString(R.string.error_no_info);
        String riskAssessment = getString(R.string.error_no_info);
        String description = getString(R.string.error_no_info);

        fmTripRegisterConfirmTripName = view.findViewById(R.id.fmTripRegisterConfirmTripName);
        fmTripRegisterConfirmDestination = view.findViewById(R.id.fmTripRegisterConfirmDestination);
        fmTripRegisterConfirmDepartureDate = view.findViewById(R.id.fmTripRegisterConfirmDepartureDate);
        fmTripRegisterConfirmDepartureTime = view.findViewById(R.id.fmTripRegisterConfirmDepartureTime);
        fmTripRegisterConfirmPurpose = view.findViewById(R.id.fmTripRegisterConfirmPurpose);
        fmTripRegisterConfirmRiskAssessment = view.findViewById(R.id.fmTripRegisterConfirmRiskAssessment);
        fmTripRegisterConfirmDescription = view.findViewById(R.id.fmTripRegisterConfirmDescription);
        fmTripRegisterConfirmButtonCancel = view.findViewById(R.id.fmTripRegisterConfirmButtonCancel);
        fmTripRegisterConfirmButtonConfirm = view.findViewById(R.id.fmTripRegisterConfirmButtonConfirm);

        if (_trip.getTripName() != null && !_trip.getTripName().trim().isEmpty()) {
            tripName = _trip.getTripName();
        }
        if(_trip.getDestination() != null && !_trip.getDestination().trim().isEmpty()) {
            destination = _trip.getDestination();
        }
        if (_trip.getDepartureDate() != null && !_trip.getDepartureDate().trim().isEmpty()) {
            departureDate = _trip.getDepartureDate();
        }
        if (_trip.getDepartureTime() != null && !_trip.getDepartureTime().trim().isEmpty()) {
            departureTime = _trip.getDepartureTime();
        }
        if (_trip.getPurpose() != null && !_trip.getPurpose().trim().isEmpty()) {
            purpose = _trip.getPurpose();
        }
        if (_trip.getRiskAssessment() != -1) {
            riskAssessment = _trip.getRiskAssessment() == 1 ? getString(R.string.label_req_risk_assessment) : getString(R.string.label_not_req_risk_assessment);
        }
        if (_trip.getDescription() != null && !_trip.getDescription().trim().isEmpty()) {
            description = _trip.getDescription();
        }

        fmTripRegisterConfirmTripName.setText(tripName);
        fmTripRegisterConfirmDestination.setText(destination);
        fmTripRegisterConfirmDepartureDate.setText(departureDate);
        fmTripRegisterConfirmDepartureTime.setText(departureTime);
        fmTripRegisterConfirmPurpose.setText(purpose);
        fmTripRegisterConfirmRiskAssessment.setText(riskAssessment);
        fmTripRegisterConfirmDescription.setText(description);

        fmTripRegisterConfirmButtonCancel.setOnClickListener(v -> dismiss());
        fmTripRegisterConfirmButtonConfirm.setOnClickListener(v -> confirm());

        return view;
    }

    protected void confirm() {
        long status = _db.insertTrip(_trip);

        FragmentListener listener = (FragmentListener) getParentFragment();
        listener.sendFromTripRegisterConfirmFragment(status);

        dismiss();
    }

    public interface FragmentListener {
        void sendFromTripRegisterConfirmFragment(long status);
    }
}