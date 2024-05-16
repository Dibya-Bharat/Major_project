package com.example.home_window;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import org.tensorflow.lite.Interpreter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;



public class Generate_Plan_form extends AppCompatActivity {

    private RadioButton radioButton1, radioButton2;
    private Spinner spinner1, spinner2, spinner3, spinner4;
    private FirebaseAuth firebaseAuth;
    private Interpreter interpreter;
    private TensorFlowModelHelper modelHelper;

    private FirebaseDatabase db;
    private DatabaseReference reference;
    private Button generateButton;

    private static final String MODEL_PATH = "my_model.tflite";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_plan_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();

        try {
            interpreter = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize the TensorFlow Lite interpreter with your model
        //modelHelper = new TensorFlowModelHelper(interpreter);

        radioButton1 = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);
        spinner1 = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);
        spinner4 = findViewById(R.id.spinner4);
        generateButton = findViewById(R.id.button3);

        radioButton1.setOnClickListener(v -> updateCategories(R.array.categories1));
        radioButton2.setOnClickListener(v -> updateCategories(R.array.categories2));
        generateButton.setOnClickListener(v -> generatePlan());
    }

    private void updateCategories(int arrayResId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayResId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        FileInputStream inputStream = new FileInputStream(getAssets().open("my_model.tflite"));
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = 0;
        long declaredLength = fileChannel.size();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void generatePlan() {
        // Retrieve data from UI components
        String State = radioButton1.isChecked() ? radioButton1.getText().toString() : radioButton2.getText().toString();
        String Category = spinner1.getSelectedItem().toString();
        String spinner2Value = spinner2.getSelectedItem().toString();
        float Rating = Float.parseFloat(spinner2Value);
        String Accomod = spinner3.getSelectedItem().toString();
        String TravelPf = spinner4.getSelectedItem().toString();


        

        String results = "";



        Intent intent = new Intent(Generate_Plan_form.this, Preview_Plan.class);

        // Put the data you want to pass into the Intent using a key-value pair
        intent.putExtra("KEY1", State);
        intent.putExtra("KEY2", Category);
        intent.putExtra("KEY3", Rating);
        intent.putExtra("KEY4", Accomod);
        intent.putExtra("KEY5", TravelPf);
        intent.putExtra("KEY6", results);

        // Start the next activity
        startActivity(intent);
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
