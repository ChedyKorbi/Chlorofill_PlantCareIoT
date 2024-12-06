package com.example.plantiotapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PlantTypes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_types); // Assuming your layout is named activity_plant_types.xml
    }

    public void onPlantSelected(View view) {
        String plantType;

        // Check for CardView IDs, not ImageView IDs
        if (view.getId() == R.id.plantACardView) {
            plantType = "Ficus Ginseng";
        } else if (view.getId() == R.id.plantBCardView) {
            plantType = "Schefflera";
        } else if (view.getId() == R.id.plantCCardView) {
            plantType = "Géranium Dwarf";
        } else if (view.getId() == R.id.plantDCardView) {
            plantType = "Snake Plant";
        } else if (view.getId() == R.id.plantECardView) {
            plantType = "Fiddle Leaf Fig";
        } else if (view.getId() == R.id.plantFCardView) {
            plantType = "Aloe Vera";
        } else {
            Toast.makeText(this, "Unknown plant selected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Pass the selected plant type to AddPlant activity
        Intent intent = new Intent(this, AddPlant.class);
        intent.putExtra("selectedPlantType", plantType);
        startActivity(intent);
    }

}
