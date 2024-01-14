package com.example.book_rental;

import static com.example.book_rental.MainActivity.EXTRA_USER_ID;
import static com.example.book_rental.MainActivity.EXTRA_USER_ROLE;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.book_rental.R;
import com.squareup.picasso.Picasso;

public class BookDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_EDIT_BOOK_TITLE = "pb.edu.pl.EDIT_BOOK_TITLE";
    public static final String EXTRA_EDIT_BOOK_AUTHOR = "pb.edu.pl.EDIT_BOOK_AUTHOR";
    public static final String EXTRA_EDIT_BOOK_ID = "pb.edu.pl.EDIT_BOOK_ID";
    public static final String EXTRA_EDIT_BOOK_ISBN = "pb.edu.pl.EDIT_BOOK_ISBN";
    public static final String EXTRA_EDIT_BOOK_PICTURE = "pb.edu.pl.EDIT_BOOK_PICTURE";
    public static final String EXTRA_EDIT_BOOK_AMOUNT = "pb.edu.pl.EDIT_BOOK_AMOUNT";
    public static final String EXTRA_USER_ID = "pb.edu.pl.EXTRA_USER_ID";
    public static final String EXTRA_USER_ROLE = "pb.edu.pl.EXTRA_USER_ROLE";
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_ROLE_KEY = "user_role_key";
    SharedPreferences sharedpreferences;
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView amountTextView;
    private TextView ISBNTextView;
    private ImageView coverImage;
    private Button rentButton;
    private TextView _authorTextView;
    private TextView _amountTextView;
    private TextView _ISBNTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        titleTextView = findViewById(R.id.book_title);
        _authorTextView = findViewById(R.id._book_author);
        authorTextView = findViewById(R.id.book_author);
        _amountTextView = findViewById(R.id._book_amount);
        amountTextView = findViewById(R.id.book_amount);
        _ISBNTextView = findViewById(R.id._book_ISBN);
        ISBNTextView = findViewById(R.id.book_ISBN);
        coverImage = findViewById(R.id.book_cover);
        rentButton = findViewById(R.id.button_rent);

        Intent intent = getIntent();

        titleTextView.setText(intent.getStringExtra(EXTRA_EDIT_BOOK_TITLE));
        _authorTextView.setText(R.string.book_author);
        authorTextView.setText(intent.getStringExtra(EXTRA_EDIT_BOOK_AUTHOR));
        Picasso.get().load(intent.getStringExtra(EXTRA_EDIT_BOOK_PICTURE))
                .resize(0,1000)
                .placeholder(R.drawable.baseline_image_24)
                .into(coverImage);
        _ISBNTextView.setText(R.string.book_ISBN);
        ISBNTextView.setText(intent.getStringExtra(EXTRA_EDIT_BOOK_ISBN));
        _amountTextView.setText(R.string.book_amount);
        amountTextView.setText(intent.getStringExtra(EXTRA_EDIT_BOOK_AMOUNT));

        if (Integer.parseInt(intent.getStringExtra(EXTRA_EDIT_BOOK_AMOUNT)) == 0) {
            rentButton.setBackgroundColor(Color.GRAY);
        }
        else {
            rentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent replyIntent = new Intent();
                    if (intent.hasExtra(EXTRA_EDIT_BOOK_ID)) {
                        Intent userIntent = getIntent();
                        //
                        replyIntent.putExtra(EXTRA_EDIT_BOOK_ID, intent.getIntExtra(EXTRA_EDIT_BOOK_ID, 0));
                        setResult(RESULT_OK, replyIntent);
                    }
                    else {
                        setResult(RESULT_CANCELED, replyIntent);
                    }
                    finish();
                }
            });
        }
    }
}