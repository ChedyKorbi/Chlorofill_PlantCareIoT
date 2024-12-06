package com.example.plantiotapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextView temperatureTextView;
    private TextView humidityTextView;
    private TextView soilMoistureTextView;
    private TextView lightLevelTextView,waterTextView;
    private TextView txtDateajout, txtPlantName, txtPlantType, txtPlantAge;
    private Button btntips;
    private Button buttonToggleRelay, btnDetails;

    // Firebase Database references
    private DatabaseReference sensorDataReference;
    private DatabaseReference relayCommandReference;

    private boolean relayState = false;

    public float waterNeeded = 0.0f; // New attribute for water needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TextViews for sensor data
        temperatureTextView = findViewById(R.id.temperatureTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        soilMoistureTextView = findViewById(R.id.soilMoistureTextView);
        lightLevelTextView = findViewById(R.id.lightLevelTextView);
        btntips = findViewById(R.id.tipsbtn);
        waterTextView = findViewById(R.id.waterTextView);

        // Initialize the relay toggle button
        buttonToggleRelay = findViewById(R.id.buttonToggleRelay);

        // Initialize Firebase Database references
        sensorDataReference = FirebaseDatabase.getInstance().getReference("sensorData");
        relayCommandReference = FirebaseDatabase.getInstance().getReference("sensorData/relayCommand");

        // Retrieve plant details passed from AddPlant activity (if available)
        Intent intent = getIntent();
        String plantName = intent.getStringExtra("plantName");
        String plantType = intent.getStringExtra("plantType");
        String plantAge = intent.getStringExtra("plantAge");
        String dateAjout = intent.getStringExtra("dateAjout");

        // Initialize the TextViews
        txtPlantType = findViewById(R.id.plantType);
        txtPlantName = findViewById(R.id.plantName);
        txtPlantAge = findViewById(R.id.plantAge);
        txtDateajout = findViewById(R.id.txtDateajout);

        // Set the data to the TextViews if available
        if (plantType != null) txtPlantType.setText(plantType);
        if (plantName != null) txtPlantName.setText(plantName);
        if (plantAge != null) txtPlantAge.setText(plantAge);
        if (dateAjout != null) txtDateajout.setText(dateAjout);

        // Attach a listener to read the sensor data
        sensorDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get values from Firebase Database
                Float temperature = dataSnapshot.child("temperature").getValue(Float.class);
                Float humidity = dataSnapshot.child("humidity").getValue(Float.class);
                Integer soilMoisture = dataSnapshot.child("soilMoisture").getValue(Integer.class);
                Integer lightLevel = dataSnapshot.child("lightLevel").getValue(Integer.class);

                // Optimal soil moisture level (example: 60%)
                int optimalSoilMoisture = 60;

                // Water needed in milliliters calculation
                if (soilMoisture != null) {
                    if (soilMoisture < optimalSoilMoisture) {
                        // Calculate deficit percentage
                        int deficit = optimalSoilMoisture - soilMoisture;
                        // Estimate water needed based on deficit (e.g., 10ml per 1% deficit)
                        waterNeeded = deficit * 10.0f;
                    } else {
                        waterNeeded = 0.0f; // No water needed if soil moisture is sufficient
                    }
                }

                // Update UI elements with retrieved data
                if (temperature != null) temperatureTextView.setText(temperature + " °C");
                if (humidity != null) humidityTextView.setText(humidity + " %");
                if (soilMoisture != null) soilMoistureTextView.setText(soilMoisture + " %");
                if (lightLevel != null) lightLevelTextView.setText(lightLevel + " lux");
               waterTextView.setText(String.format(waterNeeded+" ml"));
                // Log data for debugging purposes
                Log.d("FirebaseData", "Temperature: " + temperature);
                Log.d("FirebaseData", "Humidity: " + humidity);
                Log.d("FirebaseData", "Soil Moisture: " + soilMoisture);
                Log.d("FirebaseData", "Light Level: " + lightLevel);
                Log.d("FirebaseData", "Water Needed: " + waterNeeded + " ml");
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseData", "Database error: " + databaseError.getMessage());
            }
        });

        // Set up the relay toggle button
        buttonToggleRelay.setOnClickListener(v -> {
            relayState = !relayState; // Toggle relay state
            relayCommandReference.setValue(relayState ? 1 : 0); // Update Firebase with relay state
        });

        // Tips button to navigate to the AdvicesActivity
        btntips.setOnClickListener(v -> {
            Intent tipsIntent = new Intent(MainActivity.this, AdvicesActivity.class);
            startActivity(tipsIntent);
        });

        // Initialize btnDetails and set up its onClickListener
        btnDetails = findViewById(R.id.btnDetails);
        if (btnDetails == null) {
            Log.e("MainActivity", "btnDetails is null. Check activity_main.xml.");
            return;
        }

        btnDetails.setOnClickListener(v -> {
            // Get current values from the TextViews to pass to PlantDetails
            float temperature = Float.parseFloat(temperatureTextView.getText().toString().replace(" °C", ""));
            float humidity = Float.parseFloat(humidityTextView.getText().toString().replace(" %", ""));
            float soilMoisture = Float.parseFloat(soilMoistureTextView.getText().toString().replace(" %", ""));
            float lightLevel = Float.parseFloat(lightLevelTextView.getText().toString().replace(" lux", ""));

            // Create an Intent to start PlantDetails activity
            Intent intent1 = new Intent(MainActivity.this, PlantDetails.class);

            // Pass the data to the new activity via Intent extras
            intent1.putExtra("temperature", temperature);
            intent1.putExtra("humidity", humidity);
            intent1.putExtra("soilMoisture", soilMoisture);
            intent1.putExtra("lightLevel", lightLevel);
            intent1.putExtra("waterLevel", waterNeeded);

            // Also pass the plant data to the new activity
            intent1.putExtra("plantName", plantName);
            intent1.putExtra("plantType", plantType);
            intent1.putExtra("plantAge", plantAge);
            intent1.putExtra("dateAjout", dateAjout);

            // Start the PlantDetails activity
            startActivity(intent1);
        });
    }
}