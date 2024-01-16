package com.example.book_rental.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_rental.models.Book;
import com.example.book_rental.viewModels.BookViewModel;
import com.example.book_rental.models.Borrow;
import com.example.book_rental.viewModels.BorrowViewModel;
import com.example.book_rental.utils.Const;
import com.example.book_rental.R;
import com.example.book_rental.activities.AddBookActivity;
import com.example.book_rental.activities.BookDetailsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BooksFragment extends Fragment {

    public static final int NEW_BOOK_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_BOOK_ACTIVITY_REQUEST_CODE = 2;
    public static final int BOOK_DETAILS_ACTIVITY_REQUEST_CODE = 3;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_ROLE_KEY = "user_role_key";
    private BookViewModel bookViewModel;
    private BorrowViewModel borrowViewModel;
    private BookAdapter bookAdapter;
    private SharedPreferences sharedpreferences;
    private View view;
    private List<Book> books;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_books, container, false);

        setHasOptionsMenu(true);

        sharedpreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String role = sharedpreferences.getString(USER_ROLE_KEY, null);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        bookAdapter = new BookAdapter();
        recyclerView.setAdapter(bookAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        borrowViewModel = new ViewModelProvider(requireActivity()).get(BorrowViewModel.class);

        bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);
        bookViewModel.findAll().observe(getViewLifecycleOwner(), books -> {
            bookAdapter.setBooks(books); this.books = books;
        });

        FloatingActionButton addBookButton = view.findViewById(R.id.add_button);

        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddBookActivity.class);
                startActivityForResult(intent, NEW_BOOK_ACTIVITY_REQUEST_CODE);
            }
        });

        if (role.equals(Const.roleUser))
            addBookButton.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater menuInflater = inflater;
        menuInflater.inflate(R.menu.book_search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String title) {
                filterBooksByTitle(title);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_clear) {
            bookAdapter.setBooks(books);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void filterBooksByTitle(String title) {
        List<Book> tempBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                tempBooks.add(book);
            }
        }
        bookAdapter.setBooks(tempBooks);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
            Toast.makeText(getActivity(),R.string.book_added,Toast.LENGTH_SHORT).show();
        }
        else if (resultCode == RESULT_OK && requestCode == EDIT_BOOK_ACTIVITY_REQUEST_CODE) {
            Book book = new Book(data.getStringExtra(AddBookActivity.EXTRA_EDIT_BOOK_TITLE),
                    data.getStringExtra(AddBookActivity.EXTRA_EDIT_BOOK_AUTHOR),
                    data.getStringExtra(AddBookActivity.EXTRA_EDIT_BOOK_ISBN),
                    data.getStringExtra(AddBookActivity.EXTRA_EDIT_BOOK_PICTURE),
                    Integer.parseInt(data.getStringExtra(AddBookActivity.EXTRA_EDIT_BOOK_AMOUNT)));
            book.setId(data.getIntExtra(AddBookActivity.EXTRA_EDIT_BOOK_ID,0));
            bookViewModel.update(book);
            Toast.makeText(getActivity(),R.string.book_edited,Toast.LENGTH_SHORT).show();
        }
        else if (resultCode == RESULT_OK && requestCode == BOOK_DETAILS_ACTIVITY_REQUEST_CODE) {
            int bookId = data.getIntExtra(BookDetailsActivity.EXTRA_BOOK_ID,0);
            long mills = System.currentTimeMillis();
            Date date =  date = new Date(mills);
            sharedpreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            int userId = sharedpreferences.getInt(USER_ID_KEY, 0);
            Borrow borrow = new Borrow(bookId, userId, Const.statusPending,mills);
            Book book = bookViewModel.findById(bookId);
            int amount = book.getAmount()-1;
            book.setAmount(amount);
            bookViewModel.update(book);
            borrowViewModel.insert(borrow);
        }
        else if (requestCode == NEW_BOOK_ACTIVITY_REQUEST_CODE || requestCode == EDIT_BOOK_ACTIVITY_REQUEST_CODE){
            Toast.makeText(getActivity(),R.string.empty_not_saved,Toast.LENGTH_SHORT).show();
        }
    }

    private class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView bookTitleTextView;
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
                                    BooksFragment.this.bookViewModel.delete(book);
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

            sharedpreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            String role = sharedpreferences.getString(USER_ROLE_KEY, null);

            if (role.equals(Const.roleUser)) {
                bookEditButton.setVisibility(View.GONE);
                bookDeleteButton.setVisibility(View.GONE);
            }

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
            intent.putExtra(BookDetailsActivity.EXTRA_BOOK_ID, book.getId());
            intent.putExtra(BookDetailsActivity.EXTRA_BOOK_TITLE, book.getTitle());
            intent.putExtra(BookDetailsActivity.EXTRA_BOOK_AUTHOR, book.getAuthor());
            intent.putExtra(BookDetailsActivity.EXTRA_BOOK_ISBN, book.getISBN());
            intent.putExtra(BookDetailsActivity.EXTRA_BOOK_PICTURE, book.getImage());
            intent.putExtra(BookDetailsActivity.EXTRA_BOOK_AMOUNT, String.valueOf(book.getAmount()));
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