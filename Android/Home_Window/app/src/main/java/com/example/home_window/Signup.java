package com.example.home_window;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signup extends AppCompatActivity {
    private EditText editTextName, editTextPhone;
    private Button buttonSignup;
    private DatabaseReference databaseReference;
    private ValueEventListener userListener; // Listener to check if user already exists

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
                // Call method to check if user already exists
                checkUserExists();
            }
        });
    }

    private void checkUserExists() {
        final String name = editTextName.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();

        // Add a ValueEventListener to check if the user already exists
        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean userExists = false;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null && user.getPhone().equals(phone)) {
                        userExists = true;
                        break;
                    }
                }
                if (userExists) {
                    // If user already exists, show a toast message
                    Toast.makeText(Signup.this, "You are already registered, go to login page", Toast.LENGTH_SHORT).show();
                } else {
                    // If user doesn't exist, proceed to signup
                    signupUser();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(Signup.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        // Attach the listener to the database reference
        databaseReference.addListenerForSingleValueEvent(userListener);
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

        // Check if userId is null
        if (userId == null) {
            // Handle the case where userId is null
            // For example, display an error message
            Toast.makeText(this, "Failed to create user. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create User object, create class for the user and replace User with user class name
        User user = new User(userId, name, phone);

        // Store user information in Firebase Database
        databaseReference.child(userId).setValue(user);

        // Return to MainActivity
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the ValueEventListener to prevent memory leaks
        if (userListener != null) {
            databaseReference.removeEventListener(userListener);
        }
    }
}
