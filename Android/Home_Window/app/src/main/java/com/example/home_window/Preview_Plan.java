package com.example.home_window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
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
        data6 = intent.getStringExtra("KEY6").split(","); // Assuming KEY6 contains comma-separated values

        state.setText(data1);
        category.setText(data2);
        rating.setText(data3);
        accommodation.setText(data4);
        travel_mode.setText(data5);
        place1.setText(data6[0]);
        place2.setText(data6[1]);
        place3.setText(data6[2]);

        place1.setOnClickListener(v -> {
            // Handle click for place1
        });

        place2.setOnClickListener(v -> {
            // Handle click for place2
        });

        place3.setOnClickListener(v -> {
            // Handle click for place3
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
