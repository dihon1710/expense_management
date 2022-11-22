package com.example.expense_management.ui.expense;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.example.expense_management.R;
import com.example.expense_management.models.Expense;
import com.example.expense_management.ui.dialog.DatePickerFragment;
import com.example.expense_management.ui.dialog.TimePickerFragment;

public class ExpenseCreateFragment extends DialogFragment
        implements DatePickerFragment.FragmentListener, TimePickerFragment.FragmentListener {
    protected long _tripId;

    protected EditText fmExpenseCreateDate, fmExpenseCreateTime, fmExpenseCreateComment, fmExpenseCreateAmount;
    protected Button fmExpenseCreateButtonCancel, fmExpenseCreateButtonAdd;
    protected Spinner fmExpenseCreateType;

    public ExpenseCreateFragment() {
        _tripId = -1;
    }

    public ExpenseCreateFragment(long tripId) {
        _tripId = tripId;
    }

    @Override
    public void sendFromDatePickerFragment(String date) {
        fmExpenseCreateDate.setText(date);
    }

    @Override
    public void sendFromTimePickerFragment(String time) { fmExpenseCreateTime.setText(time); }

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
        View view = inflater.inflate(R.layout.fragment_expense_create, container, false);

        fmExpenseCreateType = view.findViewById(R.id.fmExpenseCreateType);
        fmExpenseCreateDate = view.findViewById(R.id.fmExpenseCreateDate);
        fmExpenseCreateTime = view.findViewById(R.id.fmExpenseCreateTime);
        fmExpenseCreateAmount = view.findViewById(R.id.fmExpenseCreateAmount);
        fmExpenseCreateButtonCancel = view.findViewById(R.id.fmExpenseCreateButtonCancel);
        fmExpenseCreateButtonAdd = view.findViewById(R.id.fmExpenseCreateButtonAdd);
        fmExpenseCreateComment = view.findViewById(R.id.fmExpenseCreateComment);

        fmExpenseCreateButtonCancel.setOnClickListener(v -> dismiss());
        fmExpenseCreateButtonAdd.setOnClickListener(v -> createExpense());

        fmExpenseCreateDate.setOnTouchListener((v, motionEvent) -> showDateDialog(motionEvent));
        fmExpenseCreateTime.setOnTouchListener((v, motionEvent) -> showTimeDialog(motionEvent));

        setTypeSpinner();

        return view;
    }

    protected void setTypeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.expense_type,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fmExpenseCreateType.setAdapter(adapter);
    }

    protected boolean showDateDialog(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new DatePickerFragment().show(getChildFragmentManager(), null);
            return true;
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

    protected void createExpense() {
        Expense expense = new Expense();

        expense.setExpenseType(fmExpenseCreateType.getSelectedItem().toString());
        expense.setAmount(Integer.parseInt(fmExpenseCreateAmount.getText().toString()));
        expense.setTime(fmExpenseCreateTime.getText().toString());
        expense.setDate(fmExpenseCreateDate.getText().toString());
        expense.setComment(fmExpenseCreateComment.getText().toString());

        FragmentListener listener = (FragmentListener) getParentFragment();
        listener.sendFromExpenseCreateFragment(expense);

        dismiss();
    }

    public interface FragmentListener {
        void sendFromExpenseCreateFragment(Expense expense);
    }
}