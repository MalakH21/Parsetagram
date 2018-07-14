package com.codepath.parseinstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginBtn;
    private Button signupBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginbutton);
        signupBtn = findViewById(R.id.signupBtn);


        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null){
            final Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();

                login(username, password);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();

            }
        });
    }

    public void login(String username, String password){

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
               if (e == null){
                   Log.d("LoginActivity","Login successful!");
                   final Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                   startActivity(intent);
                   finish();
               }else{
                   Log.e("LoginActivity", "Login failure");
                   e.printStackTrace();
               }
            }
        });
    }

    private void signup(){
        ParseUser user = new ParseUser();

        user.setUsername(usernameInput.getText().toString());
        user.setPassword(passwordInput.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    Log.d("SignUpActivity","SignUp successful!");
                    final Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Log.e("SignUpActivity", "SignUp failure");
                    e.printStackTrace();
                }
            }

        });
    }
}
