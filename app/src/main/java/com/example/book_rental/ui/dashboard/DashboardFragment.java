package com.example.book_rental.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_rental.BookAndUserForBorrow;
import com.example.book_rental.BookViewModel;
import com.example.book_rental.BorrowViewModel;
import com.example.book_rental.R;
import com.example.book_rental.databinding.FragmentDashboardBinding;
import com.example.book_rental.databinding.FragmentHomeBinding;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DashboardFragment extends Fragment {
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_ROLE_KEY = "user_role_key";
    SharedPreferences sharedpreferences;
    private FragmentDashboardBinding binding;
    View view;
    private BorrowViewModel borrowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedpreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        int userId = sharedpreferences.getInt(USER_ID_KEY, 0);
        String role = sharedpreferences.getString(USER_ROLE_KEY, null);
        Log.d("USER_INTENT", "userId: "+userId+"role: "+role);

        view =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        final BorrowAdapter adapter = new BorrowAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        borrowViewModel = new ViewModelProvider(requireActivity()).get(BorrowViewModel.class);

        borrowViewModel.findAllBooksAndBorrows().observe(getViewLifecycleOwner(), borrows -> {
            adapter.setBorrows(borrows);
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class BorrowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView bookTitleTextView;
        private TextView borrowStatusTextView;
        private TextView borrowDateTextView;
        private TextView userEmailTextView;
        private BookAndUserForBorrow bookAndUserForBorrow;

        public BorrowHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.borrow_list_item, parent, false));
            itemView.setOnClickListener(this);

            bookTitleTextView = itemView.findViewById(R.id.book_ISBN);
            borrowStatusTextView = itemView.findViewById(R.id.borrow_status);
            borrowDateTextView = itemView.findViewById(R.id.borrow_date);
            userEmailTextView = itemView.findViewById(R.id.user_email);

        }

        public void bind(BookAndUserForBorrow bookAndUserForBorrow) {
            bookTitleTextView.setText(bookAndUserForBorrow.book.getISBN());
            borrowStatusTextView.setText(bookAndUserForBorrow.borrow.getStatus());
            String pattern = "MM/dd/yyyy HH:mm:ss";
            DateFormat df = new SimpleDateFormat(pattern);
            Date today = Calendar.getInstance().getTime();
            String todayAsString = df.format(bookAndUserForBorrow.borrow.getDate());
            borrowDateTextView.setText(todayAsString);
            userEmailTextView.setText(bookAndUserForBorrow.user.getEmail());
            this.bookAndUserForBorrow = bookAndUserForBorrow;
        }

        @Override
        public void onClick(View v) {

        }
    }

    private class BorrowAdapter extends RecyclerView.Adapter<BorrowHolder> {
        private List<BookAndUserForBorrow> bookAndUserForBorrows;

        @NonNull
        @Override
        public BorrowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BorrowHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BorrowHolder holder, int position) {
            if (bookAndUserForBorrows != null) {
                BookAndUserForBorrow bookAndUserForBorrow = bookAndUserForBorrows.get(position);
                holder.bind(bookAndUserForBorrow);
            }
            else
                Log.d("MainActivity", "No borrows");
        }

        @Override
        public int getItemCount() {
            if (bookAndUserForBorrows != null)
                return bookAndUserForBorrows.size();
            return 0;
        }

        void setBorrows(List<BookAndUserForBorrow> bookAndUserForBorrows) {
            this.bookAndUserForBorrows = bookAndUserForBorrows;
            notifyDataSetChanged();
        }
    }
}