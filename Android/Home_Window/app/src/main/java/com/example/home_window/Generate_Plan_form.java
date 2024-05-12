package com.example.home_window;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import android.util.Log;


public class Generate_Plan_form extends AppCompatActivity {

    private RadioButton radioButton1, radioButton2;
    private Spinner spinner1, spinner2, spinner3, spinner4;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_plan_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();

        radioButton1 = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);
        spinner1 = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);
        spinner4 = findViewById(R.id.spinner4);
        Button generateButton = findViewById(R.id.button3);

        generateButton.setOnClickListener(v -> generatePlan());
    }

    private void generatePlan() {
        // Retrieve data from UI components
        String radioValue = radioButton1.isChecked() ? radioButton1.getText().toString() : radioButton2.getText().toString();
        String spinner1Value = spinner1.getSelectedItem().toString();
        String spinner2Value = spinner2.getSelectedItem().toString();
        String spinner3Value = spinner3.getSelectedItem().toString();
        String spinner4Value = spinner4.getSelectedItem().toString();

        // Create a data object
        Map<String, Object> data = new HashMap<>();
        data.put("Location", radioValue);
        data.put("Category", spinner1Value);
        data.put("Rating", spinner2Value);
        data.put("Accommodation", spinner3Value);
        data.put("TravelMode", spinner4Value);

        // Get database reference
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        if (databaseRef == null) {
            Log.e("Firebase", "Database reference is null");
            Toast.makeText(getApplicationContext(), "Failed to generate plan: Database reference is null", Toast.LENGTH_SHORT).show();
            return;
        }

        // Store data in Firebase database
        databaseRef.child("generated_plans").push().setValue(data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Plan generated and saved successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to generate plan", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Log.e("Firebase", "Failed to generate plan: " + e.getMessage(), e);
                    Toast.makeText(getApplicationContext(), "Failed to generate plan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(Generate_Plan_form.this, MainActivity.class);
        startActivity(intent);
    }
}