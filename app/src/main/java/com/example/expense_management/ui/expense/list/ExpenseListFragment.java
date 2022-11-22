package com.example.expense_management.ui.expense.list;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import com.example.expense_management.R;
import com.example.expense_management.database.ExpemanaDAO;
import com.example.expense_management.models.Expense;

public class ExpenseListFragment extends Fragment {
    public static final String ARG_PARAM_TRIP_ID = "trip_id";

    protected ArrayList<Expense> _expenseList = new ArrayList<>();

    protected ExpemanaDAO _db;
    protected TextView fmExpenseListEmptyNotice;
    protected RecyclerView fmExpenseListRecylerView;

    public ExpenseListFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        _db = new ExpemanaDAO(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_list, container, false);

        if (getArguments() != null) {
            Expense expense = new Expense();
            expense.setTripId(getArguments().getLong(ARG_PARAM_TRIP_ID));

            _expenseList = _db.getExpenseList(expense, null, false);
        }

        fmExpenseListRecylerView = view.findViewById(R.id.fmExpenseListRecylerView);
        fmExpenseListEmptyNotice = view.findViewById(R.id.fmExpenseListEmptyNotice);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());

        fmExpenseListRecylerView.addItemDecoration(dividerItemDecoration);
        fmExpenseListRecylerView.setAdapter(new ExpenseAdapter(_expenseList));
        fmExpenseListRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Show "No Expense." message.
        fmExpenseListEmptyNotice.setVisibility(_expenseList.isEmpty() ? View.VISIBLE : View.GONE);

        return view;
    }
}