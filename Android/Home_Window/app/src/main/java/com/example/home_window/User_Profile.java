package com.example.home_window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User_Profile extends AppCompatActivity {

    private TextView user_name, Phone;
    private Button chng1, chng2;
    private String un;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        user_name = findViewById(R.id.name);
        Phone = findViewById(R.id.number);
        chng1 = findViewById(R.id.changename);
        chng2 = findViewById(R.id.changenum);
        Intent intent = getIntent();
        un = intent.getStringExtra("USER_NAME");
        if (un != null) {
            user_name.setText(un);
            fetchPhoneNumber(un);
        }
        chng1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeNameDialog();
            }
        });
        chng2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePhoneDialog();
            }
        });
    }
    private void fetchPhoneNumber(String userName) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.orderByChild("name").equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String phone = snapshot.child("phone").getValue(String.class);
                        if (phone != null) {
                            Phone.setText(phone);
                        } else {
                            Toast.makeText(User_Profile.this, "Phone number not found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(User_Profile.this, "User not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(User_Profile.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showChangeNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Name");

        final EditText input = new EditText(this);
        input.setHint("Enter new name");
        builder.setView(input);

        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = input.getText().toString().trim();
                if (!TextUtils.isEmpty(newName)) {
                    updateName(newName);
                } else {
                    Toast.makeText(User_Profile.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showChangePhoneDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Phone Number");

        final EditText input = new EditText(this);
        input.setHint("Enter new phone number");
        builder.setView(input);

        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPhone = input.getText().toString().trim();
                if (!TextUtils.isEmpty(newPhone)) {
                    updatePhoneNumber(newPhone);
                } else {
                    Toast.makeText(User_Profile.this, "Phone number cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void updateName(final String newName) {
        final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.orderByChild("name").equalTo(un).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().child("name").setValue(newName);
                        user_name.setText(newName);
                        un = newName;
                        Toast.makeText(User_Profile.this, "Name updated successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(User_Profile.this, "User not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(User_Profile.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePhoneNumber(final String newPhone) {
        final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.orderByChild("name").equalTo(un).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().child("phone").setValue(newPhone);
                        Phone.setText(newPhone);
                        Toast.makeText(User_Profile.this, "Phone number updated successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(User_Profile.this, "User not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(User_Profile.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}