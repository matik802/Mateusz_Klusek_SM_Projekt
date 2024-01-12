package com.example.book_rental.ui.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_rental.AddBookActivity;
import com.example.book_rental.Book;
import com.example.book_rental.BookAndBorrow;
import com.example.book_rental.BookDetailsActivity;
import com.example.book_rental.BookViewModel;
import com.example.book_rental.Borrow;
import com.example.book_rental.BorrowViewModel;
import com.example.book_rental.R;
import com.example.book_rental.databinding.FragmentDashboardBinding;
import com.example.book_rental.databinding.FragmentHomeBinding;
import com.example.book_rental.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DashboardFragment extends Fragment {

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

//        bookViewModel.findAll().observe((LifecycleOwner) getContext(), adapter::setBooks);
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
        private BookAndBorrow bookAndBorrow;

        public BorrowHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.borrow_list_item, parent, false));
            itemView.setOnClickListener(this);

            bookTitleTextView = itemView.findViewById(R.id.book_ISBN);
            borrowStatusTextView = itemView.findViewById(R.id.borrow_status);
            borrowDateTextView = itemView.findViewById(R.id.borrow_date);
            userEmailTextView = itemView.findViewById(R.id.user_email);
        }

        public void bind(BookAndBorrow bookAndBorrow) {
            bookTitleTextView.setText(bookAndBorrow.book.getISBN());
            borrowStatusTextView.setText(bookAndBorrow.borrow.getStatus());
            String pattern = "MM/dd/yyyy HH:mm:ss";
            DateFormat df = new SimpleDateFormat(pattern);
            Date today = Calendar.getInstance().getTime();
            String todayAsString = df.format(bookAndBorrow.borrow.getDate());
            borrowDateTextView.setText(todayAsString);
            userEmailTextView.setText("janusz123456@gmial.com");
            this.bookAndBorrow = bookAndBorrow;
        }

        @Override
        public void onClick(View v) {

        }
    }

    private class BorrowAdapter extends RecyclerView.Adapter<BorrowHolder> {
        private List<BookAndBorrow> bookAndBorrows;

        @NonNull
        @Override
        public BorrowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BorrowHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BorrowHolder holder, int position) {
            if (bookAndBorrows != null) {
                BookAndBorrow bookAndBorrow = bookAndBorrows.get(position);
                holder.bind(bookAndBorrow);
            }
            else
                Log.d("MainActivity", "No borrows");
        }

        @Override
        public int getItemCount() {
            if (bookAndBorrows != null)
                return bookAndBorrows.size();
            return 0;
        }

        void setBorrows(List<BookAndBorrow> bookAndBorrows) {
            this.bookAndBorrows = bookAndBorrows;
            notifyDataSetChanged();
        }
    }
}