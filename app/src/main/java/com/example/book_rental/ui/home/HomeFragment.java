package com.example.book_rental.ui.home;

import static android.app.Activity.RESULT_OK;

import static com.example.book_rental.MainActivity.EXTRA_USER_ID;
import static com.example.book_rental.MainActivity.EXTRA_USER_ROLE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_rental.AddBookActivity;
import com.example.book_rental.Book;
import com.example.book_rental.BookDetailsActivity;
import com.example.book_rental.BookViewModel;
import com.example.book_rental.Borrow;
import com.example.book_rental.BorrowViewModel;
import com.example.book_rental.Const;
import com.example.book_rental.MainActivity;
import com.example.book_rental.R;
import com.example.book_rental.databinding.FragmentHomeBinding;
import com.example.book_rental.ui.dashboard.DashboardFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    public static final int NEW_BOOK_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_BOOK_ACTIVITY_REQUEST_CODE = 2;
    public static final int BOOK_DETAILS_ACTIVITY_REQUEST_CODE = 3;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_ROLE_KEY = "user_role_key";
    private FragmentHomeBinding binding;
    private BookViewModel bookViewModel;
    private BorrowViewModel borrowViewModel;
    SharedPreferences sharedpreferences;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedpreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        int userId = sharedpreferences.getInt(USER_ID_KEY, 0);
        String role = sharedpreferences.getString(USER_ROLE_KEY, null);
        Log.d("USER_INTENT", "userId: "+userId+"role: "+role);

        view =  inflater.inflate(R.layout.fragment_home, container, false);

//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        //textView.setText("abc");

        //setContentView(R.layout.activity_main);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        final BookAdapter adapter = new BookAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        borrowViewModel = new ViewModelProvider(requireActivity()).get(BorrowViewModel.class);

        bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);
//        bookViewModel.findAll().observe((LifecycleOwner) getContext(), adapter::setBooks);
        bookViewModel.findAll().observe(getViewLifecycleOwner(), books -> {
            adapter.setBooks(books);
        });

        FloatingActionButton addBookButton = view.findViewById(R.id.add_button);

        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddBookActivity.class);
                startActivityForResult(intent, NEW_BOOK_ACTIVITY_REQUEST_CODE);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == NEW_BOOK_ACTIVITY_REQUEST_CODE) {
            Book book = new Book(data.getStringExtra(AddBookActivity.EXTRA_EDIT_BOOK_TITLE),
                    data.getStringExtra(AddBookActivity.EXTRA_EDIT_BOOK_AUTHOR),
                    data.getStringExtra(AddBookActivity.EXTRA_EDIT_BOOK_ISBN),
                    data.getStringExtra(AddBookActivity.EXTRA_EDIT_BOOK_PICTURE),
                    Integer.parseInt(data.getStringExtra(AddBookActivity.EXTRA_EDIT_BOOK_AMOUNT)));
            bookViewModel.insert(book);
            Snackbar.make(view.findViewById(R.id.home_layout),
                    getString(R.string.book_added),
                    Snackbar.LENGTH_LONG).show();
        }
        else if (resultCode == RESULT_OK && requestCode == EDIT_BOOK_ACTIVITY_REQUEST_CODE) {
            Book book = new Book(data.getStringExtra(AddBookActivity.EXTRA_EDIT_BOOK_TITLE),
                    data.getStringExtra(AddBookActivity.EXTRA_EDIT_BOOK_AUTHOR),
                    data.getStringExtra(AddBookActivity.EXTRA_EDIT_BOOK_ISBN),
                    data.getStringExtra(AddBookActivity.EXTRA_EDIT_BOOK_PICTURE),
                    Integer.parseInt(data.getStringExtra(AddBookActivity.EXTRA_EDIT_BOOK_AMOUNT)));
            book.setId(data.getIntExtra(AddBookActivity.EXTRA_EDIT_BOOK_ID,0));
            // Book book = bookViewModel.findById(data.getIntExtra(EditBookActivity.EXTRA_EDIT_BOOK_ID,0));
//            Book book = bookViewModel.findBookWithTitle(data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_TITLE)).get(0);
            //Log.e("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",String.valueOf(book.getId()));
            bookViewModel.update(book);
            Snackbar.make(view.findViewById(R.id.home_layout),
                    getString(R.string.book_edited),
                    Snackbar.LENGTH_LONG).show();
        }
        else if (resultCode == RESULT_OK && requestCode == BOOK_DETAILS_ACTIVITY_REQUEST_CODE) {
            Log.d("aaaaaaaaaaaaaaaaaaa",String.valueOf(data.getIntExtra(AddBookActivity.EXTRA_EDIT_BOOK_ID,0)));
            int bookId = data.getIntExtra(AddBookActivity.EXTRA_EDIT_BOOK_ID,0);
            long mills = System.currentTimeMillis();
            Date date =  date = new Date(mills);

            String pattern = "MM/dd/yyyy HH:mm:ss";
            DateFormat df = new SimpleDateFormat(pattern);
            Date today = Calendar.getInstance().getTime();
            String todayAsString = df.format(date);
            Log.d("bbbbbbbbbbbbbbbb",todayAsString);

            sharedpreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            int userId = sharedpreferences.getInt(USER_ID_KEY, 0);
            Borrow borrow = new Borrow(bookId, userId, Const.statusPending,mills);
            Book book = bookViewModel.findById(bookId);
            int amount = book.getAmount()-1;
            book.setAmount(amount);
            bookViewModel.update(book);
            borrowViewModel.insert(borrow);
        }
        else {
            Snackbar.make(view.findViewById(R.id.home_layout),
                    getString(R.string.empty_not_saved),
                    Snackbar.LENGTH_LONG).show();
        }
    }

    private class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView bookTitleTextView;
        private TextView bookAuthorTextView;
        private ImageView bookCoverImage;
        private Button bookEditButton;
        private Button bookDeleteButton;
        private Book book;

        public BookHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.book_list_item, parent, false));
            itemView.setOnClickListener(this);

            bookTitleTextView = itemView.findViewById(R.id.book_title);
            bookCoverImage = itemView.findViewById(R.id.book_cover);

            bookEditButton = itemView.findViewById(R.id.button_edit);
            bookDeleteButton = itemView.findViewById(R.id.button_delete);

            bookEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), AddBookActivity.class);
                    intent.putExtra(AddBookActivity.EXTRA_EDIT_BOOK_ID, book.getId());
                    intent.putExtra(AddBookActivity.EXTRA_EDIT_BOOK_TITLE, book.getTitle());
                    intent.putExtra(AddBookActivity.EXTRA_EDIT_BOOK_AUTHOR, book.getAuthor());
                    intent.putExtra(AddBookActivity.EXTRA_EDIT_BOOK_ISBN, book.getISBN());
                    intent.putExtra(AddBookActivity.EXTRA_EDIT_BOOK_PICTURE, book.getImage());
                    intent.putExtra(AddBookActivity.EXTRA_EDIT_BOOK_AMOUNT, String.valueOf(book.getAmount()));
                    startActivityForResult(intent, EDIT_BOOK_ACTIVITY_REQUEST_CODE);
                }
            });

            bookDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(true);
                    builder.setTitle(R.string.delete_pop_up_title);
                    builder.setMessage(R.string.delete_pop_up_content);
                    builder.setPositiveButton(R.string.delete_pop_up_confirmation,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    HomeFragment.this.bookViewModel.delete(book);
                                }
                            });
                    builder.setNegativeButton(R.string.delete_pop_up_cancelation, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }

        public void bind(Book book) {
            bookTitleTextView.setText(book.getTitle());
            Picasso.get().load(book.getImage())
                    .resize(0,1000)
                    .placeholder(R.drawable.baseline_image_24)
                    .into(bookCoverImage);
            this.book = book;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), BookDetailsActivity.class);
            intent.putExtra(BookDetailsActivity.EXTRA_EDIT_BOOK_ID, book.getId());
            intent.putExtra(BookDetailsActivity.EXTRA_EDIT_BOOK_TITLE, book.getTitle());
            intent.putExtra(BookDetailsActivity.EXTRA_EDIT_BOOK_AUTHOR, book.getAuthor());
            intent.putExtra(BookDetailsActivity.EXTRA_EDIT_BOOK_ISBN, book.getISBN());
            intent.putExtra(BookDetailsActivity.EXTRA_EDIT_BOOK_PICTURE, book.getImage());
            intent.putExtra(BookDetailsActivity.EXTRA_EDIT_BOOK_AMOUNT, String.valueOf(book.getAmount()));
            startActivityForResult(intent, BOOK_DETAILS_ACTIVITY_REQUEST_CODE);
        }
    }

    private class BookAdapter extends RecyclerView.Adapter<BookHolder> {
        private List<Book> books;

        @NonNull
        @Override
        public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BookHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BookHolder holder, int position) {
            if (books != null) {
                Book book = books.get(position);
                holder.bind(book);
            }
            else
                Log.d("MainActivity", "No books");
        }

        @Override
        public int getItemCount() {
            if (books != null)
                return books.size();
            return 0;
        }

        void setBooks(List<Book> books) {
            this.books = books;
            notifyDataSetChanged();
        }
    }
}