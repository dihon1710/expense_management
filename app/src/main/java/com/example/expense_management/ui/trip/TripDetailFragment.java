package com.example.expense_management.ui.trip;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.Navigation;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.example.expense_management.R;
import com.example.expense_management.database.ExpemanaDAO;
import com.example.expense_management.models.Expense;
import com.example.expense_management.models.Trip;
import com.example.expense_management.ui.dialog.DeleteConfirmFragment;
import com.example.expense_management.ui.expense.ExpenseCreateFragment;
import com.example.expense_management.ui.expense.list.ExpenseListFragment;

public class TripDetailFragment extends Fragment
        implements DeleteConfirmFragment.FragmentListener, ExpenseCreateFragment.FragmentListener {
    public static final String ARG_PARAM_TRIP = "trip";

    protected ExpemanaDAO _db;
    protected Trip _trip;
    protected Button fmTripDetailExpenseButton;
    protected BottomAppBar fmTripDetailBottomAppBar;
    protected FragmentContainerView fmTripDetailExpenseList;
    protected TextView fmTripDetailTripName, fmTripDetailDestination, fmTripDetailDepartureDate, fmTripDetailDepartureTime,
                       fmTripDetailPurpose, fmTripDetailRiskAssessment, fmTripDetailDescription;

    public TripDetailFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new ExpemanaDAO(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_detail, container, false);

        fmTripDetailTripName = view.findViewById(R.id.fmTripDetailTripName);
        fmTripDetailDestination = view.findViewById(R.id.fmTripDetailDestination);
        fmTripDetailDepartureDate = view.findViewById(R.id.fmTripDetailDepartureDate);
        fmTripDetailDepartureTime = view.findViewById(R.id.fmTripDetailDepartureTime);
        fmTripDetailPurpose = view.findViewById(R.id.fmTripDetailPurpose);
        fmTripDetailRiskAssessment = view.findViewById(R.id.fmTripDetailRiskAssessment);
        fmTripDetailDescription = view.findViewById(R.id.fmTripDetailDescription);
        fmTripDetailBottomAppBar = view.findViewById(R.id.fmTripDetailBottomAppBar);
        fmTripDetailExpenseButton = view.findViewById(R.id.fmTripDetailExpenseButton);
        fmTripDetailExpenseList = view.findViewById(R.id.fmTripDetailExpenseList);

        fmTripDetailBottomAppBar.setOnMenuItemClickListener(item -> menuItemSelected(item));
        fmTripDetailExpenseButton.setOnClickListener(v -> showAddExpenseFragment());

        showDetails();
        showExpenseList();

        return view;
    }

    protected void showDetails() {
        String tripName = getString(R.string.error_not_found);
        String destination = getString(R.string.error_not_found);
        String departureDate = getString(R.string.error_not_found);
        String departureTime = getString(R.string.error_not_found);
        String purpose = getString(R.string.error_not_found);
        String riskAssessment = getString(R.string.error_not_found);
        String description = getString(R.string.error_not_found);

        if (getArguments() != null) {
            _trip = (Trip) getArguments().getSerializable(ARG_PARAM_TRIP);
            _trip = _db.getTripById(_trip.getId()); // Retrieve data from Database.

            tripName = _trip.getTripName();
            destination = _trip.getDestination();
            departureDate = _trip.getDepartureDate();
            departureTime = _trip.getDepartureTime();
            purpose = _trip.getPurpose();
            riskAssessment = _trip.getRiskAssessment() == 1 ? getString(R.string.label_req_risk_assessment) : getString(R.string.label_not_req_risk_assessment);
            description = _trip.getDescription();
        }

        fmTripDetailTripName.setText(tripName);
        fmTripDetailDestination.setText(destination);
        fmTripDetailDepartureDate.setText(departureDate);
        fmTripDetailDepartureTime.setText(departureTime);
        fmTripDetailPurpose.setText(purpose);
        fmTripDetailRiskAssessment.setText(riskAssessment);
        fmTripDetailDescription.setText(description);
    }

    protected void showExpenseList() {
        if (getArguments() != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ExpenseListFragment.ARG_PARAM_TRIP_ID, _trip.getId());

            // Send arguments (trip  id) to ExpenseListFragment.
            getChildFragmentManager().getFragments().get(0).setArguments(bundle);
        }
    }

    protected boolean menuItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tripUpdateFragment:
                showUpdateFragment();
                return true;

            case R.id.tripDeleteFragment:
                showDeleteConfirmFragment();
                return true;
        }
        return true;
    }

    protected void showUpdateFragment() {
        Bundle bundle = null;

        if (_trip != null) {
            bundle = new Bundle();
            bundle.putSerializable(TripUpdateFragment.ARG_PARAM_TRIP, _trip);
        }
        Navigation.findNavController(getView()).navigate(R.id.tripUpdateFragment, bundle);
    }

    protected void showDeleteConfirmFragment() {
        new DeleteConfirmFragment(getString(R.string.notification_delete_confirm)).show(getChildFragmentManager(), null);
    }

    protected void showAddExpenseFragment() {
        new ExpenseCreateFragment(_trip.getId()).show(getChildFragmentManager(), null);
    }

    @Override
    public void sendFromDeleteConfirmFragment(int status) {
        if (status == 1 && _trip != null) {
            long numOfDeletedRows = _db.deleteTrip(_trip.getId());

            if (numOfDeletedRows > 0) {
                Toast.makeText(getContext(), R.string.notification_delete_success, Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getView()).navigateUp();

                return;
            }
        }
        Toast.makeText(getContext(), R.string.notification_delete_fail, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendFromExpenseCreateFragment(Expense expense) {
        if (expense != null) {
            expense.setTripId(_trip.getId());

            long id = _db.insertExpense(expense);

            Toast.makeText(getContext(), id == -1 ? R.string.notification_create_fail : R.string.notification_create_success, Toast.LENGTH_SHORT).show();

            reloadExpenseList();
        }
    }

    protected void reloadExpenseList() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ExpenseListFragment.ARG_PARAM_TRIP_ID, _trip.getId());

        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fmTripDetailExpenseList, ExpenseListFragment.class, bundle)
                .commit();
    }
}