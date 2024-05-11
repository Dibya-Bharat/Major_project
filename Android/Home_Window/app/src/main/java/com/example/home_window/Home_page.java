package com.example.home_window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Home_page extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private TextView emptyTextView;
    private Button bt1, livesupport, allplans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        emptyTextView = findViewById(R.id.emptyTextView);
        checkForPlans();
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        allplans = findViewById(R.id.btnAllPlans);
        bt1 = findViewById(R.id.btnRecomGen);
        livesupport = findViewById(R.id.btnLiveSupport);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_page.this, Generate_Plan_form.class);
                startActivity(intent);
            }
        });
        livesupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_page.this, Live_Support.class);
                startActivity(intent);
            }
        });
        allplans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_page.this, Allplans.class);
                startActivity(intent);
            }
        });

    }
    private void checkForPlans() {
        // Get a reference to the Firebase database
        DatabaseReference plansRef = FirebaseDatabase.getInstance().getReference("plans");

        // Query to check if any plans exist
        Query plansQuery = plansRef.limitToFirst(1); // Limit to the first plan (if any)

        plansQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Plans exist, hide the empty text and show bottom navigation
                    emptyTextView.setVisibility(View.GONE);

                } else {
                    // No plans exist, show the empty text and hide bottom navigation
                    emptyTextView.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(Home_page.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.menu_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout(){
        firebaseAuth.signOut();
        Intent intent = new Intent(Home_page.this, MainActivity.class);
        startActivity(intent);
    }
}