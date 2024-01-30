package com.example.book_rental.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.book_rental.utils.Const;
import com.example.book_rental.R;
import com.example.book_rental.models.User;
import com.example.book_rental.viewModels.UserViewModel;

public class LoginActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String USER_ID_KEY = "user_id_key";
    public static final String USER_ROLE_KEY = "user_role_key";
    private SharedPreferences sharedpreferences;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginbutton;
    private Button registerButton;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.input_email);
        passwordEditText = findViewById(R.id.input_password);
        loginbutton = findViewById(R.id.button_login);
        registerButton = findViewById(R.id.button_register);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this,R.string.email_empty,Toast.LENGTH_SHORT).show();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(LoginActivity.this,R.string.email_not_correct,Toast.LENGTH_SHORT).show();
                }
                else if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this,R.string.password_empty,Toast.LENGTH_SHORT).show();
                }
                else {
                    User user = userViewModel.findByEmail(email);
                    if (user == null) {
                        Toast.makeText(LoginActivity.this,R.string.wrong_password_or_username,Toast.LENGTH_SHORT).show();
                    }
                    else if (!password.equals(user.getPassword())) {
                        Toast.makeText(LoginActivity.this,R.string.wrong_password_or_username,Toast.LENGTH_SHORT).show();
                    }
                    else {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putInt(USER_ID_KEY, user.getId());
                        if (user.getEmail().equals(Const.adminEmail))
                            editor.putString(USER_ROLE_KEY, Const.roleAdmin);
                        else
                            editor.putString(USER_ROLE_KEY, Const.roleUser);
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setTitle(R.string.login_activity);
    }

}
