package com.example.book_rental;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.book_rental.R;

public class AddBookActivity extends AppCompatActivity {
    public static final String EXTRA_EDIT_BOOK_TITLE = "pb.edu.pl.EDIT_BOOK_TITLE";
    public static final String EXTRA_EDIT_BOOK_AUTHOR = "pb.edu.pl.EDIT_BOOK_AUTHOR";
    public static final String EXTRA_EDIT_BOOK_ID = "pb.edu.pl.EDIT_BOOK_ID";
    public static final String EXTRA_EDIT_BOOK_ISBN = "pb.edu.pl.EDIT_BOOK_ISBN";
    public static final String EXTRA_EDIT_BOOK_PICTURE = "pb.edu.pl.EDIT_BOOK_PICTURE";
    public static final String EXTRA_EDIT_BOOK_AMOUNT = "pb.edu.pl.EDIT_BOOK_AMOUNT";
    private EditText editTitleEditText;
    private EditText editAuthorEditText;
    private EditText editISBNEditText;
    private EditText editPictureEditText;
    private EditText editAmountEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        editTitleEditText = findViewById(R.id.edit_book_title);
        editAuthorEditText = findViewById(R.id.edit_book_author);
        editISBNEditText = findViewById(R.id.edit_book_ISBN);
        editPictureEditText = findViewById(R.id.edit_book_picture);
        editAmountEditText = findViewById(R.id.edit_book_amount);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_EDIT_BOOK_TITLE))
        {
            editTitleEditText.setText(intent.getStringExtra(EXTRA_EDIT_BOOK_TITLE));
            editAuthorEditText.setText(intent.getStringExtra(EXTRA_EDIT_BOOK_AUTHOR));
            editISBNEditText.setText(intent.getStringExtra(EXTRA_EDIT_BOOK_ISBN));
            editPictureEditText.setText(intent.getStringExtra(EXTRA_EDIT_BOOK_PICTURE));
            editAmountEditText.setText(intent.getStringExtra(EXTRA_EDIT_BOOK_AMOUNT));
        }
        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();
                if (editTitleEditText.getText().toString().isEmpty()
                        || editAuthorEditText.getText().toString().isEmpty()
                        || editISBNEditText.getText().toString().isEmpty()
                        || editPictureEditText.getText().toString().isEmpty()
                        || editAmountEditText.getText().toString().isEmpty()
                    ) {
                    setResult(RESULT_CANCELED, replyIntent);
                }
                else {
                    String title = editTitleEditText.getText().toString();
                    replyIntent.putExtra(EXTRA_EDIT_BOOK_TITLE, title);
                    String author = editAuthorEditText.getText().toString();
                    replyIntent.putExtra(EXTRA_EDIT_BOOK_AUTHOR, author);
                    String ISBN = editISBNEditText.getText().toString();
                    replyIntent.putExtra(EXTRA_EDIT_BOOK_ISBN, ISBN);
                    String picture = editPictureEditText.getText().toString();
                    replyIntent.putExtra(EXTRA_EDIT_BOOK_PICTURE, picture);
                    String amount = editAmountEditText.getText().toString();
                    replyIntent.putExtra(EXTRA_EDIT_BOOK_AMOUNT, amount);
                    if (intent.hasExtra(EXTRA_EDIT_BOOK_ID)) {
                        replyIntent.putExtra(EXTRA_EDIT_BOOK_ID, intent.getIntExtra(EXTRA_EDIT_BOOK_ID, 0));
                    }
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}