package com.example.plantiotapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        // Get the reference to the 'Add Your Plant' button
        Button addPlantButton = findViewById(R.id.addPlantButton);

        // Set a click listener to handle button click
        addPlantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the PantTypesActivity
                Intent intent = new Intent(HomePage.this, PlantTypes.class);
                startActivity(intent);
            }
        });
    }
    }
