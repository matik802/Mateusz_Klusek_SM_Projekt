package com.example.book_rental;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordConfrimationEditText;
    private Button registerButton;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.input_email);
        passwordEditText = findViewById(R.id.input_password);
        passwordConfrimationEditText = findViewById(R.id.input_password_confirmation);
        registerButton = findViewById(R.id.button_register);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String passwordConfirmation = passwordConfrimationEditText.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(RegisterActivity.this,R.string.email_empty,Toast.LENGTH_SHORT).show();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(RegisterActivity.this,R.string.email_not_correct,Toast.LENGTH_SHORT).show();
                }
                else if (password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this,R.string.password_empty,Toast.LENGTH_SHORT).show();
                }
                else if (!password.equals(passwordConfirmation)) {
                    Toast.makeText(RegisterActivity.this,R.string.passwords_not_match,Toast.LENGTH_SHORT).show();
                }
                else {
                    if (userViewModel.findByEmail(email) == null) {
                        User user = new User(email,password);
                        userViewModel.insert(user);
                        Toast.makeText(RegisterActivity.this,R.string.user_added,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(RegisterActivity.this,R.string.email_exists,Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

}
