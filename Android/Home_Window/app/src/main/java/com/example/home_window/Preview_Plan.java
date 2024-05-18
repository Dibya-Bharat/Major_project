package com.example.home_window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Preview_Plan extends AppCompatActivity {

    private TextView state, category, rating, place1, place2, place3, accommodation, travel_mode;
    private Button savebtn;
    private Intent intent;
    private String data1, data2, data3, data4, data5;
    private String[] data6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_plan);
        Toolbar toolbar = findViewById(R.id.plantoolbar);
        setSupportActionBar(toolbar);

        intent = getIntent();
        state = findViewById(R.id.State);
        category = findViewById(R.id.Category);
        rating = findViewById(R.id.Rating);
        place1 = findViewById(R.id.Place1);
        place2 = findViewById(R.id.Place2);
        place3 = findViewById(R.id.Place3);
        accommodation = findViewById(R.id.Accommodation);
        travel_mode = findViewById(R.id.Travel_Mode);
        savebtn = findViewById(R.id.savebutton);

        data1 = intent.getStringExtra("KEY1");
        data2 = intent.getStringExtra("KEY2");
        data3 = intent.getStringExtra("KEY3");
        data4 = intent.getStringExtra("KEY4");
        data5 = intent.getStringExtra("KEY5");
        data6 = intent.getStringArrayExtra("KEY6"); // Assuming KEY6 contains comma-separated values

        state.setText(String.format("State: %s", data1));
        category.setText(String.format("Category: %s", data2));
        rating.setText(String.format("Rating: %s", data3));
        accommodation.setText(String.format("Accommodation: %s", data4));
        travel_mode.setText(String.format("Travel Mode: %s", data5));
        place1.setText(data6[0]);
        place2.setText(data6[1]);
        place3.setText(data6[2]);

        place1.setOnClickListener(v -> {
            navigateToPlace(data6[0],data1);
        });

        place2.setOnClickListener(v -> {
            navigateToPlace(data6[1],data1);
        });

        place3.setOnClickListener(v -> {
            navigateToPlace(data6[2],data1);
        });


        savebtn.setOnClickListener(v -> saveDataToFirebase());
    }

    private void saveDataToFirebase() {
        // Get Firebase Database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("plans");

        // Create a unique ID for the new plan
        String planId = myRef.push().getKey();

        // Create a Plan object
        Plan plan = new Plan(data1, data2, data3, data4, data5, data6[0], data6[1], data6[2]);

        // Save the plan to Firebase under the generated unique ID
        if (planId != null) {
            myRef.child(planId).setValue(plan).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Data saved successfully
                    Toast.makeText(Preview_Plan.this, "Plan saved successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Failed to save data
                    Toast.makeText(Preview_Plan.this, "Failed to save plan", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void navigateToPlace(String place,String state) {
        String travelMode = data5.toLowerCase();
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

    // Plan class to hold the plan data
    public static class Plan {
        public String state;
        public String category;
        public String rating;
        public String accommodation;
        public String travel_mode;
        public String place1;
        public String place2;
        public String place3;

        public Plan(String state, String category, String rating, String accommodation, String travel_mode, String place1, String place2, String place3) {
            this.state = state;
            this.category = category;
            this.rating = rating;
            this.accommodation = accommodation;
            this.travel_mode = travel_mode;
            this.place1 = place1;
            this.place2 = place2;
            this.place3 = place3;
        }
    }
}
