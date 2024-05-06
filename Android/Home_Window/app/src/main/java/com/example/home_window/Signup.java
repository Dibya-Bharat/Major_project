package com.example.home_window;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {
    private EditText editTextName, editTextPhone;
    private Button buttonSignup;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Initialize Views
        editTextName = findViewById(R.id.editTextText);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonSignup = findViewById(R.id.button2);

        // Set onClickListener for Signup button
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call method to store user information in Firebase Database
                signupUser();
            }
        });
    }
    private void signupUser() {
        // Get user input from EditText fields
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        // Check if any field is empty
        if (name.isEmpty()) {
            editTextName.setError("Name is required");
            editTextName.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            editTextPhone.setError("Phone number is required");
            editTextPhone.requestFocus();
            return;
        }

        // Generate a unique key for the user
        String userId = databaseReference.push().getKey();

        // Create User object, create class for the user and replace User with user class name
        User user = new User(userId, name, phone);

        // Store user information in Firebase Database
        databaseReference.child(userId).setValue(user);

        // Return to MainActivity
        finish();
    }
}