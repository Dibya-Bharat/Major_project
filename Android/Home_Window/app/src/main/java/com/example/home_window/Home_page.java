package com.example.home_window;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
    private TextView state, category, rating, place1, place2, place3, accommodation, travel_mode;
    private String user_name;
    private TextView  un;
    private Button gen_plan_form, livesupport, allplans, userprofile;
    private Generate_plans plan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        state = findViewById(R.id.State);
        category = findViewById(R.id.Category);
        rating = findViewById(R.id.Rating);
        place1 = findViewById(R.id.Place1);
        place2 = findViewById(R.id.Place2);
        place3 = findViewById(R.id.Place3);
        accommodation = findViewById(R.id.Accommodation);
        travel_mode = findViewById(R.id.Travel_Mode);
        //allplans = findViewById(R.id.btnAllPlans);
        gen_plan_form = findViewById(R.id.btnRecomGen);
        livesupport = findViewById(R.id.btnLiveSupport);
        userprofile = findViewById(R.id.btnUserProfile);
        un = findViewById(R.id.user_name);
        user_name = getIntent().getStringExtra("USER_NAME");
        if (user_name != null) {
            // Update your UI with the user's name

            un.setText("Welcome, " + user_name + "!");
        }
        else {
            Log.e(TAG, "user_name is null in Home_page");
            Toast.makeText(this, "Error: No user found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        fetchLatestPlan();
        gen_plan_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_page.this, Generate_Plan_form.class);
                intent.putExtra("USER_NAME",user_name);
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
        /*allplans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_page.this, Allplans.class);
                intent.putExtra("USER_NAME",user_name);
                startActivity(intent);
            }
        });*/
        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_page.this, User_Profile.class);
                intent.putExtra("USER_NAME",user_name);
                startActivity(intent);
            }
        });
        place1.setOnClickListener(v -> {
            navigateToPlace(place1.getText().toString(), state.getText().toString());
        });

        place2.setOnClickListener(v -> {
            navigateToPlace(place2.getText().toString(), state.getText().toString());
        });

        place3.setOnClickListener(v -> {
            navigateToPlace(place3.getText().toString(), state.getText().toString());
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
        finish();
    }

    private void fetchLatestPlan() {
        DatabaseReference plansRef = FirebaseDatabase.getInstance().getReference("plans").child(user_name);
        plansRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Generate_plans plan = snapshot.getValue(Generate_plans.class);
                        if (plan != null) {
                            state.setText(String.format("State: %s", plan.getState()));
                            category.setText(String.format("Category: %s", plan.getCategory()));
                            rating.setText(String.format("Rating: %s", plan.getRating()));
                            place1.setText(String.format("Place 1: %s", plan.getPlace1()));
                            place2.setText(String.format("Place 2: %s", plan.getPlace2()));
                            place3.setText(String.format("Place 3: %s", plan.getPlace3()));
                            accommodation.setText(String.format("Accommodation: %s", plan.getAccommodation()));
                            travel_mode.setText(String.format("Hotels: %s", plan.getTravelMode()));
                        }
                    }
                } else {
                    Toast.makeText(Home_page.this, "No plans found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "fetchLatestPlan:onCancelled", databaseError.toException());
                Toast.makeText(Home_page.this, "Failed to load your last plan.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void navigateToPlace(String place,String state) {
        String travelMode = travel_mode.getText().toString();
        String mode = "d";
        switch (travelMode) {
            case "bus":
                mode = "transit";
                break;
            case "car":
                mode = "driving";
                break;
            case "train":
                mode = "transit";
                break;
        }
        Log.d("travel preference",travelMode);
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Uri.encode(place+","+state+",India") + "&mode=" + mode);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
}