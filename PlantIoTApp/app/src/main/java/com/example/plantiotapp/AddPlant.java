package com.example.plantiotapp;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddPlant extends AppCompatActivity {

    private EditText plantNameEditText, plantTypeEditText, plantAgeEditText, dateAjoutEditText;
    private ImageView plantImageView;
    private Calendar calendar;
    private DatabaseHelper databaseHelper;
    private Button submitButton, selectImageButton;
    private ImageButton homebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        // Initialize views
        initializeViews();

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Handle plant type passed via Intent
        handleIncomingIntent();

        // Disable typing in date field and set up date picker
        setupDatePicker();

        // Set up image selection functionality
        setupImageSelection();

        submitButton.setOnClickListener(v -> {
            // Collect data from the form
            String plantName = plantNameEditText.getText().toString();
            String plantType = plantTypeEditText.getText().toString();
            String plantAge = plantAgeEditText.getText().toString();
            String dateAjout = dateAjoutEditText.getText().toString();

            // Validate input
            if (validateInput(plantName, plantType, plantAge, dateAjout)) {
                // Insert data into the database
                long result = addPlantToDatabase(plantName, plantType, plantAge, dateAjout);
                if (result != -1) {
                    navigateToMainActivity(plantName, plantType, plantAge, dateAjout);
                } else {
                    Toast.makeText(this, "Error adding plant to the database", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * Initializes views and UI elements.
     */
    private void initializeViews() {
        plantNameEditText = findViewById(R.id.plantName);
        plantTypeEditText = findViewById(R.id.plantType);
        plantAgeEditText = findViewById(R.id.plantAge);
        dateAjoutEditText = findViewById(R.id.dateajout);
        plantImageView = findViewById(R.id.plantImage);
        selectImageButton = findViewById(R.id.selectPlantImageButton);
        submitButton = findViewById(R.id.submitBtn);
        calendar = Calendar.getInstance();
    }

    /**
     * Handles incoming data from other Activities via Intent.
     */
    private void handleIncomingIntent() {
        Intent intent = getIntent();
        String selectedPlantType = intent.getStringExtra("selectedPlantType");
        if (selectedPlantType != null && !selectedPlantType.isEmpty()) {
            plantTypeEditText.setText(selectedPlantType);
        }
    }

    /**
     * Sets up the DatePicker dialog for the date field.
     */
    private void setupDatePicker() {
        updateDateInView();
        dateAjoutEditText.setFocusable(false);
        dateAjoutEditText.setOnClickListener(v -> showDatePickerDialog());
    }

    /**
     * Shows a DatePickerDialog to select a date.
     */
    private void showDatePickerDialog() {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateInView();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * Updates the date field with the selected date in "yyyy-MM-dd" format.
     */
    private void updateDateInView() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateAjoutEditText.setText(sdf.format(calendar.getTime()));
    }

    /**
     * Sets up image selection functionality.
     */
    private void setupImageSelection() {
        selectImageButton.setOnClickListener(v -> {
            // Placeholder for actual image selection functionality
            Glide.with(this)
                    .load("file:///android_asset/placeholder.png") // Placeholder image
                    .into(plantImageView);
            Toast.makeText(this, "Image functionality not yet implemented", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Sets up the submit button and its functionality.
     */


    /**
     * Validates input fields for completeness.
     */
    private boolean validateInput(String name, String type, String age, String dateAdded) {
        if (name.isEmpty() || type.isEmpty() || age.isEmpty() || dateAdded.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Navigates to MainActivity with the new plant details.
     */
    private void navigateToMainActivity(String name, String type, String age, String dateAdded) {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra("plantName", name);
        mainIntent.putExtra("plantType", type);
        mainIntent.putExtra("plantAge", age);
        mainIntent.putExtra("dateAjout", dateAdded);
        startActivity(mainIntent);
        Toast.makeText(this, "Plant submitted successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * Adds the plant details to the database.
     */
    private long addPlantToDatabase(String name, String type, String age, String dateAdded) {
        SQLiteDatabase db = null;
        long rowId = -1;
        try {
            db = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("type", type);
            values.put("age", Integer.parseInt(age)); // Ensures age is a valid integer
            values.put("date_added", dateAdded);

            rowId = db.insert("Plants", null, values); // Insert into the database
        } catch (Exception e) {
            Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return rowId;
    }
}
