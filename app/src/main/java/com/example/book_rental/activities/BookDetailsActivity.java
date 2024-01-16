package com.example.book_rental.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.book_rental.R;
import com.squareup.picasso.Picasso;

public class BookDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_BOOK_TITLE = "pb.edu.pl.BOOK_TITLE";
    public static final String EXTRA_BOOK_AUTHOR = "pb.edu.pl.BOOK_AUTHOR";
    public static final String EXTRA_BOOK_ID = "pb.edu.pl.BOOK_ID";
    public static final String EXTRA_BOOK_ISBN = "pb.edu.pl.BOOK_ISBN";
    public static final String EXTRA_BOOK_PICTURE = "pb.edu.pl.BOOK_PICTURE";
    public static final String EXTRA_BOOK_AMOUNT = "pb.edu.pl.BOOK_AMOUNT";
    public static final String EXTRA_PREVIEW = "pb.edu.pl.PREVIEW";
    public static final String EXTRA_USER_ID = "pb.edu.pl.USER_ID";
    public static final String EXTRA_USER_ROLE = "pb.edu.pl.USER_ROLE";
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        titleTextView = findViewById(R.id.book_title);
        authorTextView = findViewById(R.id.book_author);
        amountTextView = findViewById(R.id.book_amount);
        ISBNTextView = findViewById(R.id.book_ISBN);
        coverImage = findViewById(R.id.book_cover);
        rentButton = findViewById(R.id.button_rent);

        Intent intent = getIntent();

        titleTextView.setText(intent.getStringExtra(EXTRA_BOOK_TITLE));
        authorTextView.setText(intent.getStringExtra(EXTRA_BOOK_AUTHOR));
        Picasso.get().load(intent.getStringExtra(EXTRA_BOOK_PICTURE))
                .resize(0,1000)
                .placeholder(R.drawable.baseline_image_24)
                .into(coverImage);
        ISBNTextView.setText(intent.getStringExtra(EXTRA_BOOK_ISBN));
        amountTextView.setText(intent.getStringExtra(EXTRA_BOOK_AMOUNT));

        if (intent.getBooleanExtra(EXTRA_PREVIEW,false))
            rentButton.setVisibility(View.GONE);

        if (Integer.parseInt(intent.getStringExtra(EXTRA_BOOK_AMOUNT)) == 0) {
            rentButton.setBackgroundColor(Color.GRAY);
        }
        else {
            rentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent replyIntent = new Intent();
                    if (intent.hasExtra(EXTRA_BOOK_ID)) {
                        replyIntent.putExtra(EXTRA_BOOK_ID, intent.getIntExtra(EXTRA_BOOK_ID, 0));
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