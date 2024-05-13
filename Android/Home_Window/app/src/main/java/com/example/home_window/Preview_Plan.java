package com.example.home_window;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class Preview_Plan extends AppCompatActivity {

    private TextView state, category, rating, place1, place2, place3, accommodation, travel_mode;
    private Intent intent;
    private String data1,data2,data3,data4,data5;
    private String data6[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_plan);
        intent = getIntent();
        state = findViewById(R.id.State);
        category = findViewById(R.id.Category);
        rating = findViewById(R.id.Rating);
        place1 = findViewById(R.id.Place1);
        place2 = findViewById(R.id.Place2);
        place3 = findViewById(R.id.Place3);
        accommodation = findViewById(R.id.Accommodation);
        travel_mode = findViewById(R.id.Travel_Mode);
        data1=intent.getStringExtra("KEY1");
        data2=intent.getStringExtra("KEY2");
        data3=intent.getStringExtra("KEY3");
        data4=intent.getStringExtra("KEY4");
        data5=intent.getStringExtra("KEY5");
        data6= new String[]{intent.getStringExtra("KEY6")};
        state.setText(data1);
        category.setText(data2);
        rating.setText(data3);
        accommodation.setText(data4);
        travel_mode.setText(data5);
        place1.setText(data6[0]);
        place2.setText(data6[1]);
        place3.setText(data6[2]);


    }
}