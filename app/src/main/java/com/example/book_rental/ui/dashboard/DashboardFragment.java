package com.example.book_rental.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_rental.BookAndUserForBorrow;
import com.example.book_rental.BookDetailsActivity;
import com.example.book_rental.BookViewModel;
import com.example.book_rental.Borrow;
import com.example.book_rental.BorrowViewModel;
import com.example.book_rental.Const;
import com.example.book_rental.OnSwipeTouchListener;
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
        view =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        final BorrowAdapter adapter = new BorrowAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        borrowViewModel = new ViewModelProvider(requireActivity()).get(BorrowViewModel.class);

        sharedpreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        int userId = sharedpreferences.getInt(USER_ID_KEY, 0);
        String role = sharedpreferences.getString(USER_ROLE_KEY, null);

        if (role.equals(Const.roleUser)) {
            borrowViewModel.findBooksAndUsersForBorrowsForUser(userId).observe(getViewLifecycleOwner(), borrows -> {
                adapter.setBorrows(borrows);
            });
        }
        else {
            borrowViewModel.findBooksAndUsersForBorrows().observe(getViewLifecycleOwner(), borrows -> {
                adapter.setBorrows(borrows);
        });

        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class BorrowHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView bookTitleTextView;
        private TextView borrowStatusTextView;
        private TextView borrowDateTextView;
        private TextView userEmailTextView;
        private BookAndUserForBorrow bookAndUserForBorrow;

        public BorrowHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.borrow_list_item, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            bookTitleTextView = itemView.findViewById(R.id.book_ISBN);
            borrowStatusTextView = itemView.findViewById(R.id.borrow_status);
            borrowDateTextView = itemView.findViewById(R.id.borrow_date);
            userEmailTextView = itemView.findViewById(R.id.user_email);

            sharedpreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            String role = sharedpreferences.getString(USER_ROLE_KEY, null);
            if (role.equals(Const.roleAdmin)) {
                borrowStatusTextView.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
                    public void onSwipeRight() {
                        if (borrowStatusTextView.getText().toString().equals(Const.statusPending)) {
                            borrowStatusTextView.setText(Const.statusReturned);
                            borrowStatusTextView.setTextColor(Color.GREEN);
                        } else if (borrowStatusTextView.getText().toString().equals(Const.statusBorrowed)) {
                            borrowStatusTextView.setText(Const.statusPending);
                            borrowStatusTextView.setTextColor(Color.GREEN);
                        }
                    }

                    public void onSwipeLeft() {
                        if (borrowStatusTextView.getText().toString().equals(Const.statusPending)) {
                            borrowStatusTextView.setText(Const.statusBorrowed);
                            borrowStatusTextView.setTextColor(Color.GREEN);
                        } else if (borrowStatusTextView.getText().toString().equals(Const.statusReturned)) {
                            borrowStatusTextView.setText(Const.statusPending);
                            borrowStatusTextView.setTextColor(Color.GREEN);
                        }
                    }
                });
            }
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
            Intent intent = new Intent(getContext(), BookDetailsActivity.class);
            intent.putExtra(BookDetailsActivity.EXTRA_EDIT_BOOK_ID, bookAndUserForBorrow.book.getId());
            intent.putExtra(BookDetailsActivity.EXTRA_EDIT_BOOK_TITLE, bookAndUserForBorrow.book.getTitle());
            intent.putExtra(BookDetailsActivity.EXTRA_EDIT_BOOK_AUTHOR, bookAndUserForBorrow.book.getAuthor());
            intent.putExtra(BookDetailsActivity.EXTRA_EDIT_BOOK_ISBN, bookAndUserForBorrow.book.getISBN());
            intent.putExtra(BookDetailsActivity.EXTRA_EDIT_BOOK_PICTURE, bookAndUserForBorrow.book.getImage());
            intent.putExtra(BookDetailsActivity.EXTRA_EDIT_BOOK_AMOUNT, String.valueOf(bookAndUserForBorrow.book.getAmount()));
            startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            Borrow borrow = bookAndUserForBorrow.borrow;
            borrow.setStatus(borrowStatusTextView.getText().toString());
            long mills = System.currentTimeMillis();
            borrow.setDate(mills);
            borrowViewModel.update(borrow);
            borrowStatusTextView.setTextColor(Color.BLACK);
            return true;
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