package com.example.expense_management.ui.expense.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.example.expense_management.R;
import com.example.expense_management.models.Expense;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> implements Filterable {
    protected ArrayList<Expense> _originalList;
    protected ArrayList<Expense> _filteredList;
    protected ExpenseAdapter.ItemFilter _itemFilter = new ExpenseAdapter.ItemFilter();

    public ExpenseAdapter(ArrayList<Expense> list) {
        _originalList = list;
        _filteredList = list;
    }

    public void updateList(ArrayList<Expense> list) {
        _originalList = list;
        _filteredList = list;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_expense, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense expense = _filteredList.get(position);

        holder.listItemExpenseType.setText(expense.getExpenseType());
        holder.listItemExpenseDate.setText(expense.getDate());
        holder.listItemExpenseTime.setText(expense.getTime());
        holder.listItemExpenseAmount.setText(Integer.toString(expense.getAmount()));
        holder.listItemExpenseComment.setText(expense.getComment());
    }

    @Override
    public int getItemCount() {
        return _filteredList == null ? 0 : _filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return _itemFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView listItemExpenseType, listItemExpenseDate, listItemExpenseTime, listItemExpenseAmount, listItemExpenseComment;

        public ViewHolder(View itemView) {
            super(itemView);

            listItemExpenseDate = itemView.findViewById(R.id.listItemExpenseDate);
            listItemExpenseTime = itemView.findViewById(R.id.listItemExpenseTime);
            listItemExpenseType = itemView.findViewById(R.id.listItemExpenseType);
            listItemExpenseAmount = itemView.findViewById(R.id.listItemExpenseAmount);
            listItemExpenseComment = itemView.findViewById(R.id.listItemExpenseComment);
        }
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final ArrayList<Expense> list = _originalList;
            final ArrayList<Expense> nlist = new ArrayList<>(list.size());

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            for (Expense expense : list) {
                String filterableString = expense.toString();

                if (filterableString.toLowerCase().contains(filterString))
                    nlist.add(expense);
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            _filteredList = (ArrayList<Expense>) results.values;
            notifyDataSetChanged();
        }
    }
}