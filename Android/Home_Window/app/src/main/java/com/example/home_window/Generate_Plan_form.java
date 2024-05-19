package com.example.home_window;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Generate_Plan_form extends AppCompatActivity {

    private RadioButton radioButton1, radioButton2;
    private Spinner spinner1, spinner2, spinner3, spinner4;
    private FirebaseAuth firebaseAuth;
    private Interpreter interpreter;
    private TensorFlowModelHelper modelHelper;
    private String user_name;
    private FirebaseDatabase db;
    private DatabaseReference reference;
    private Button generateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_plan_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();

        user_name = getIntent().getStringExtra("USER_NAME");
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

        try {
            interpreter = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(getAssets().openFd("my_model.tflite").getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = getAssets().openFd("my_model.tflite").getStartOffset();
        long declaredLength = getAssets().openFd("my_model.tflite").getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }



    private void updateCategories(int arrayResId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayResId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
    }

    private void generatePlan() {

        final String[] place1 = {"Akshardham Temple", "Ambience Mall", "Appu Ghar Express", "Askot Sanctuary",
                "Auli", "Badrinath", "Bangla Sahib Market", "Bhimtal lake", "Bhulla Lake", "Chakrata", "Chamoli",
                "Chandni Chowk", "Chhatarpur Temple", "Chopta", "Connaught Place", "Corbett National Park", "DLF CyberHub",
                "DLF Emporio", "Dehradun", "Delhi Haunted House", "Delhi Ridge Forest", "Dilli Haat INA", "Dronagiri",
                "Gandhi Smriti", "George Everest Peak", "Gurudwara Bangla Sahib", "Haldwani", "Haridwar", "Hauz Khas Village",
                "Humayun’s Tomb", "ISKCON Temple", "India Gate", "India Habitat Centre", "India International Centre", "Jageshwar",
                "Jama Masjid", "Janpath Market", "Jantar Mantar", "Joshimath", "Kafni Glacier", "Kainchi Dham", "Kausani",
                "Kedarnath", "Khari Baoli", "Lakshman Jhula", "Lansdowne", "Lotus Temple", "Madame Tussauds", "Mughal Gardens",
                "Mukteshwar", "Munsiyari", "Mussoorie", "Nag Tibba", "Nainital", "National Gallery of Modern Art",
                "National Museum", "National Zoological Park", "Neelkanth Mahadev Temple", "Neer Garh Waterfall",
                "Nizamuddin Dargah", "Palika Bazaar", "Panch Kedar", "Pauri", "Pindari Glacier", "Pithoragarh",
                "Purana Qila", "Qutub Minar", "Raj Ghat", "Rajaji Wildlife Sanctuary", "Rajouri Garden Market",
                "Rani Jheel", "Red Fort", "Roopkund", "Sacred Heart Cathedral", "Sahastradhara", "Sarojini Nagar Market",
                "Sattal", "Select Citywalk", "Talkatora Garden", "Tapovan", "Tehri", "Tughlaqabad Fort",
                "Valley of Flowers National Park"};

        final String[] place2 = {
                "Akshardham Temple", "Appu Ghar Express", "Badrinath", "Bangla Sahib Market",
                "Bhavishya Badri", "Bhimtal lake", "Bhulla Lake", "Binsar", "Chakrata", "Chamoli",
                "Chhatarpur Temple", "Connaught Place", "DLF Emporio", "Dehradun",
                "Delhi Ridge Biodiversity Park", "Delhi Ridge Forest", "Delhi-6", "Devprayag",
                "Dhanaulti", "Dilli Haat INA", "Dronagiri", "Gangotri", "George Everest Peak",
                "Gomukh", "Gurudwara Bangla Sahib", "Hanuman Temple", "ISKCON Temple", "Jageshwar",
                "Janpath Market", "Jharipani Falls", "Kafni Glacier", "Kainchi Dham", "Kanatal",
                "Kedarnath", "Khan Market", "Khirsu", "Kingdom of Dreams", "Lakshman Jhula",
                "Lansdowne", "Laxminarayan Temple", "Mukteshwar", "Munsiyari", "Mussoorie",
                "Nag Tibba", "National Gallery of Modern Art", "National Rail Museum",
                "Neelkanth Mahadev Temple", "Neer Garh Waterfall", "Pacific Mall", "Palika Bazaar",
                "Panch Kedar", "Pangot And Kilbury Bird Sanctuary", "Pauri", "Pithoragarh",
                "Rajaji Wildlife Sanctuary", "Rajouri Garden Market", "Rani Jheel", "Ranikhet",
                "Roopkund", "Roorkee", "Sacred Heart Cathedral", "Sahastradhara",
                "Sarojini Nagar Market", "Sattal", "Select Citywalk", "St. James’ Church",
                "Talkatora Garden", "Tapovan", "Tungnath Temple", "Valley of Flowers National Park",
                "Art Spice Gallery", "Delhi Haunted House", "Feroz Shah Kotla Fort",
                "Fun N Food Village", "Humayun’s Tomb", "India Gate", "Lodi Art District",
                "Madame Tussauds", "Nehru Planetarium", "Okhla Bird Sanctuary", "Purana Qila",
                "Qutub Minar", "Raj Ghat", "Rashtrapati Bhavan", "Rishikesh", "Tughlaqabad Fort"
        };



    final String[] place3 = {
            "Raj Ghat", "Almora", "Badrinath", "Bhavishya Badri", "Bhim Ghasutri", "Bhowali", "Bhulla Lake",
            "Binsar", "Binsar Wildlife Sanctuary", "Chakrata", "Chamoli", "Connaught Place", "Crafts Museum",
            "DLF Emporio", "Dayara Bugyal", "Delhi Ridge Biodiversity Park", "Delhi-6", "Devprayag", "Dhanaulti",
            "Dharali", "Dronagiri", "Ganga Aarti at Triveni Ghat", "Gangotri", "Gomukh", "Greater Kailash M-Block Market",
            "Gurudwara Bangla Sahib", "Gurudwara Sis Ganj Sahib", "Gwaldam", "Hanuman Temple", "Hemkund Sahib",
            "ISKCON Temple", "Jageshwar", "Janpath Market", "Jhandewalan Temple", "Jharipani Falls", "Kafni Glacier", "Kainchi Dham",
            "Kanatal", "Karnaprayag", "Karol Bagh Market", "Kausani", "Kempty Falls", "Khan Market", "Khirsu",
            "Kingdom of Dreams", "Kunjapuri Temple", "Lakshman Jhula", "Lansdowne", "Laxminarayan Temple", "Lodhi Gardens",
            "Munsiyari", "Nag Tibba", "National Rail Museum", "Neer Garh Waterfall", "Pacific Mall", "Palika Bazaar",
            "Panch Kedar", "Pangot And Kilbury Bird Sanctuary", "Raj Ghat", "Rani Jheel", "Roopkund",
            "Roorkee", "Sahastradhara", "Sarojini Nagar Market", "Satopanth Lake", "Sattal", "St. James’ Church",
            "St. Stephen’s Church", "Surkanda Devi Temple", "Swarg Ashram", "Talkatora Garden", "Tapkeshwar Temple",
            "Tapovan", "The Great India Place", "Tungnath Temple", "Vashishta Gufa", "Worlds of Wonder", "Adventure Island",
            "Agrasen ki Baoli", "Benog Wildlife Sanctuary", "Delhi Haunted House", "Delhi War Cemetery", "Feroz Shah Kotla Fort",
            "Fun N Food Village", "Gaumukh Glacier", "Kathputli Colony", "Purana Qila", "Qutub Minar", "Rashtrapati Bhavan",
            "Shankar’s International Dolls Museum", "Surajkund Mela", "Tughlaqabad Fort", "Yamuna biodiversity Park"
    };

                // Retrieve data from UI components
        String State = radioButton1.isChecked() ? radioButton1.getText().toString() : radioButton2.getText().toString();
        String Category = spinner1.getSelectedItem().toString();
        String spinner2Value = spinner2.getSelectedItem().toString();
        float Rating = Float.parseFloat(spinner2Value);
        String Accomod = spinner3.getSelectedItem().toString();
        String TravelPf = spinner4.getSelectedItem().toString();


        int encodedState = encodeState(State);
        int encodedCategory = encodeCategory(Category);
        int encodedRating = encodeRating(Rating);

        float[][] input = new float[1][3];
        input[0][0] = encodedState;
        input[0][1] = encodedCategory;
        input[0][2] = encodedRating;



        float[][] output1 = new float[1][83];
        float[][] output2 = new float[1][86];
        float[][] output3 = new float[1][93];

        Map<Integer, Object> outputs = new HashMap<>();
        outputs.put(0, output1);
        outputs.put(1, output2);
        outputs.put(2, output3);

        interpreter.runForMultipleInputsOutputs(new Object[]{input}, outputs);

        String place1Name = decodePlace(place1, output1[0]);
        String place2Name = decodePlace(place2, output2[0]);
        String place3Name = decodePlace(place3, output3[0]);



        // Show the results in a Toast
        //String message = "Place 1: " + place1Name + "\nPlace 2: " + place2Name + "\nPlace 3: " + place3Name;
        String[] results = {place1Name,place2Name,place3Name};
        //Toast.makeText(Generate_Plan_form.this, message, Toast.LENGTH_LONG).show();

//
        Intent intent = new Intent(Generate_Plan_form.this, Preview_Plan.class);

        // Put the data you want to pass into the Intent using a key-value pair
        intent.putExtra("KEY1", State);
        intent.putExtra("KEY2", Category);
        intent.putExtra("KEY3", spinner2Value);
        intent.putExtra("KEY4", Accomod);
        intent.putExtra("KEY5", TravelPf);
        intent.putExtra("KEY6", results);
        intent.putExtra("USER_NAME",user_name);

        // Start the next activity
        startActivity(intent);
    }
    private String decodePlace(String[] places, float[] output) {
        int maxIndex = 0;
        for (int i = 1; i < output.length; i++) {
            if (output[i] > output[maxIndex]) {
                maxIndex = i;
            }
        }
        return places[maxIndex];
    }
    public static int encodeState(String state) {
            switch (state) {
                case "Delhi":
                    return 0;
                case "Uttarakhand":
                    return 1;
            }
            return 0;
    }

        public static int encodeRating(float rating) {
            if (rating == 4.0f) {
                return 0;
            } else if (rating == 4.5f) {
                return 1;
            } else {
                return 0;
            }
        }

        public static int encodeCategory(String category) {
            switch (category) {
                case "Adventure sports":
                    return 0;
                case "Culture":
                    return 1;
                case "Entertainment":
                    return 2;
                case "Hill Station":
                    return 3;
                case "Historical":
                    return 4;
                case "Mall":
                    return 5;
                case "Market":
                    return 6;
                case "Museum":
                    return 7;
                case "Natural Beauty":
                    return 8;
                case "Religious":
                    return 9;
                case "Wildlife":
                    return 10;
            }
            return 0;
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
        finish();
    }
}
