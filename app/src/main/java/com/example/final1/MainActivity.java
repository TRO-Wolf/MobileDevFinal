package com.example.final1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText editUsername, editPassword;
    Button btnLogin;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        editUsername = findViewById(R.id.username);
        editPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String current_user = editUsername.getText().toString();
                String password = editPassword.getText().toString();

                if (current_user.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter", Toast.LENGTH_SHORT).show();
                } else if (dbHelper.checkUserExists(current_user, password)) {
                        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                        intent.putExtra("USERNAME", current_user);
                        startActivity(intent);
                    } else {
                    Toast.makeText(MainActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}