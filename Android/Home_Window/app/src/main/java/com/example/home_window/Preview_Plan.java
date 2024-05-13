package com.example.home_window;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Preview_Plan extends AppCompatActivity {

    private TextView state, category, rating, place1, place2, place3, accommodation, travel_mode;
    private Intent intent;
    private String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_plan);
        Intent intent = getIntent();

    }
}