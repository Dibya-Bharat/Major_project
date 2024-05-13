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
import org.tensorflow.lite.Interpreter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Generate_Plan_form extends AppCompatActivity {

    private RadioButton radioButton1, radioButton2;
    private Spinner spinner1, spinner2, spinner3, spinner4;
    private FirebaseAuth firebaseAuth;
    private Interpreter interpreter;
    private TensorFlowModelHelper modelHelper;

    private FirebaseDatabase db;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_plan_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize the TensorFlow Lite interpreter with your model
        //interpreter = new Interpreter(modelfile);
        modelHelper = new TensorFlowModelHelper(interpreter);

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
        float floatValue = Float.parseFloat(spinner2Value);
        String spinner3Value = spinner3.getSelectedItem().toString();
        String spinner4Value = spinner4.getSelectedItem().toString();

        // Prepare input data
        float[] inputData = {
                // Define your input data here based on the UI components
        };

        // Send data to the TensorFlow model and get results
        float[] results = modelHelper.runInference(inputData);

        // Create an Intent object to navigate to the next activity
        Intent intent = new Intent(Generate_Plan_form.this, Preview_Plan.class);

        // Put the data you want to pass into the Intent using a key-value pair
        intent.putExtra("KEY1", radioValue);
        intent.putExtra("KEY2", spinner1Value);
        intent.putExtra("KEY3", floatValue);
        intent.putExtra("KEY4", spinner3Value);
        intent.putExtra("KEY5", spinner4Value);
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
