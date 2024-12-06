package com.example.plantiotapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class PlantDetails extends AppCompatActivity {

    private TextView plantDetailsTitle, lightTitle, currentLight, lightRecommendation;
    private TextView waterTitle, currentWater, waterRecommendation;
    private TextView temperatureTitle, currentTemperature, temperatureRecommendation;
    private TextView humidityTitle, currentHumidity, HumidityRecommendation;
    private TextView soilMoisturetitle, soilMoisture, SoilMRecommendation;
    private TextView explanationTitle, explanationText;
    private ImageButton settingsButton;
    private LinearLayout scrollableContent;
    private CardView bilanCard;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);

        // Initialize views
        initializeViews();

        // Initialize the database helper
        databaseHelper = new DatabaseHelper(this);

        // Set up plant details
        setPlantDetails();

        // Settings button functionality
        settingsButton.setOnClickListener(v -> {
            // Open settings or perform your desired action
        });
    }

    private void initializeViews() {
        plantDetailsTitle = findViewById(R.id.plantDetailsTitle);
        lightTitle = findViewById(R.id.lightTitle);
        currentLight = findViewById(R.id.currentLight);
        lightRecommendation = findViewById(R.id.lightRecommendation);

        waterTitle = findViewById(R.id.waterTitle);
        currentWater = findViewById(R.id.currentWater);
        waterRecommendation = findViewById(R.id.waterRecommendation);

        temperatureTitle = findViewById(R.id.temperatureTitle);
        currentTemperature = findViewById(R.id.currentTemperature);
        temperatureRecommendation = findViewById(R.id.temperatureRecommendation);

        humidityTitle = findViewById(R.id.HumidityTitle);
        currentHumidity = findViewById(R.id.currentHumidity);
        HumidityRecommendation = findViewById(R.id.HumidityRecommendation);

        soilMoisturetitle = findViewById(R.id.soilMoisturetitle);
        soilMoisture = findViewById(R.id.soilMoisture);
        SoilMRecommendation = findViewById(R.id.SoilMRecommendation);

        explanationTitle = findViewById(R.id.explanationTitle);
        explanationText = findViewById(R.id.explanationText);

        settingsButton = findViewById(R.id.settingsButton);
        bilanCard = findViewById(R.id.bilanCard);
    }

    private void setPlantDetails() {
        Intent intent1 = getIntent();
        String plantType = intent1.getStringExtra("plantType");
        float currentLightValue = intent1.getFloatExtra("lightLevel", 0.0f);
        float currentTemperatureValue = intent1.getFloatExtra("temperature", 0.0f);
        float currentSoilMoistureValue = intent1.getFloatExtra("soilMoisture", 0.0f);
        float currentHumidityValue = intent1.getFloatExtra("humidity", 0.0f);
        float currentWaterValue = intent1.getFloatExtra("waterLevel", 0.0f);

        OptimalCondition optimalConditions = getOptimalConditionsFromDb(plantType);

        if (optimalConditions != null) {
            int lightScore = calculateAttributeScore(currentLightValue, optimalConditions.getMinLight(), optimalConditions.getMaxLight());
            int waterScore = calculateAttributeScore(currentWaterValue, optimalConditions.getMinWater(), optimalConditions.getMaxWater());
            int temperatureScore = calculateAttributeScore(currentTemperatureValue, optimalConditions.getMinTemperature(), optimalConditions.getMaxTemperature());
            int soilMoistureScore = calculateAttributeScore(currentSoilMoistureValue, optimalConditions.getMinSoilHumidity(), optimalConditions.getMaxSoilHumidity());
            int humidityScore = calculateAttributeScore(currentHumidityValue, optimalConditions.getMinAmbientHumidity(), optimalConditions.getMaxAmbientHumidity());

            int overallScore = (lightScore + waterScore + temperatureScore + soilMoistureScore + humidityScore) / 5;
            // Update titles dynamically based on status
            lightTitle.setText("Light (" + getStatus(currentLightValue, optimalConditions.getMinLight(), optimalConditions.getMaxLight()) + ")");
            waterTitle.setText("Water (" + getStatus(currentWaterValue, optimalConditions.getMinWater(), optimalConditions.getMaxWater()) + ")");
            temperatureTitle.setText("Temperature (" + getStatus(currentTemperatureValue, optimalConditions.getMinTemperature(), optimalConditions.getMaxTemperature()) + ")");
            humidityTitle.setText("Humidity (" + getStatus(currentHumidityValue, optimalConditions.getMinAmbientHumidity(), optimalConditions.getMaxAmbientHumidity()) + ")");
            soilMoisturetitle.setText("Soil Moisture (" + getStatus(currentSoilMoistureValue, optimalConditions.getMinSoilHumidity(), optimalConditions.getMaxSoilHumidity()) + ")");

            currentLight.setText("Light: " + currentLightValue + " Lux (" + lightScore + "/100) " + getEmoji(lightScore));
            lightRecommendation.setText("Recommendation: " + getLightRecommendation(getStatus(currentLightValue, optimalConditions.getMinLight(), optimalConditions.getMaxLight())));

            currentWater.setText("Water: " + currentWaterValue + " ml (" + waterScore + "/100) " + getEmoji(waterScore));
            waterRecommendation.setText("Recommendation: " + getWaterRecommendation(getStatus(currentWaterValue, optimalConditions.getMinWater(), optimalConditions.getMaxWater())));

            currentTemperature.setText("Temperature: " + currentTemperatureValue + " °C (" + temperatureScore + "/100) " + getEmoji(temperatureScore));
            temperatureRecommendation.setText("Recommendation: " + getTemperatureRecommendation(getStatus(currentTemperatureValue, optimalConditions.getMinTemperature(), optimalConditions.getMaxTemperature())));

            soilMoisture.setText("Soil Moisture: " + currentSoilMoistureValue + "% (" + soilMoistureScore + "/100) " + getEmoji(soilMoistureScore));
            SoilMRecommendation.setText("Recommendation: " + getSoilMoistureRecommendation(getStatus(currentSoilMoistureValue, optimalConditions.getMinSoilHumidity(), optimalConditions.getMaxSoilHumidity())));

            currentHumidity.setText("Humidity: " + currentHumidityValue + "% (" + humidityScore + "/100) " + getEmoji(humidityScore));
            HumidityRecommendation.setText("Recommendation: " + getHumidityRecommendation(getStatus(currentHumidityValue, optimalConditions.getMinAmbientHumidity(), optimalConditions.getMaxAmbientHumidity())));

            explanationTitle.setText("BILAN");
            explanationText.setText(getBilanSummary(overallScore));
        } else {
            explanationTitle.setText("Error");
            explanationText.setText("Optimal conditions not found for the plant type: " + plantType);
        }
    }

    private String getHumidityRecommendation(String humidityStatus) {
        switch (humidityStatus) {
            case "Too Low":
                return "Increase ambient humidity using a humidifier or misting. 🌫️";
            case "Too High":
                return "Reduce humidity by improving air circulation or moving the plant. 💨";
            default:
                return "Humidity levels are optimal. 👍";
        }
    }


    private int calculateAttributeScore(float currentValue, float minValue, float maxValue) {
        float range = maxValue - minValue;

        // If the value is out of range, apply a gradual penalty
        if (currentValue < minValue) {
            // Penalize progressively as the value moves farther from the range
            return Math.max(10, (int) (50 - ((minValue - currentValue) / range) * 50));
        } else if (currentValue > maxValue) {
            // Penalize progressively as the value moves farther from the range
            return Math.max(10, (int) (50 - ((currentValue - maxValue) / range) * 50));
        } else {
            // Normal calculation for in-range values
            float midpoint = (minValue + maxValue) / 2;
            float closeness = 1 - Math.abs(currentValue - midpoint) / (range / 2);
            return (int) (50 + closeness * 50);  // Scores between 50-100
        }


        }

        private String getBilanSummary(int overallScore) {
            if (overallScore >= 81) {
                return "🎉 Excellent! Your plant is thriving with lush leaves and vibrant growth. Keep doing what you're doing 🌟!";
            } else if (overallScore >= 61) {
                return "🌱 Good! Your plant is happy and healthy, but a few tweaks could make it even better. You're almost a pro! 💪";
            } else if (overallScore >= 41) {
                return "🤔 Moderate. Your plant is doing okay, but there are areas that need improvement. Pay closer attention to its needs. 🌿";
            } else if (overallScore >= 21) {
                return "⚠️ Weak. Your plant needs significant care and adjustments to thrive. Don't lose hope—small changes can make a big difference! 💚";
            } else {
                return "🌪️ Critical! Your plant is struggling and needs urgent attention. Act fast to save your leafy friend! 😟";
            }
        }

        private String getStatus(float currentValue, float minValue, float maxValue) {
            if (currentValue < minValue) {
                return "Too Low";
            } else if (currentValue > maxValue) {
                return "Too High";
            } else {
                return "Optimal";
            }
        }

        private String getEmoji(int score) {
            if (score >= 81) return "🌟";
            if (score >= 61) return "🙂";
            if (score >= 41) return "😕";
            if (score >= 21) return "☹️";
            return "❌";
        }

        private String getLightRecommendation(String lightStatus) {
            switch (lightStatus) {
                case "Too Low":
                    return "Your plant needs more light! Move it closer to a window 🌞.";
                case "Too High":
                    return "Your plant is feeling scorched 🔥. Move it to indirect light.";
                default:
                    return "The light is perfect—keep it shining! 😎";
            }
        }

        private String getWaterRecommendation(String waterStatus) {
            switch (waterStatus) {
                case "Too Low":
                    return "Your plant is thirsty 💧. Water it gradually.";
                case "Too High":
                    return "Too much water! Let the soil dry before watering again 🌊.";
                default:
                    return "Watering is perfect—keep it up! 🪴";
            }
        }


        private String getTemperatureRecommendation(String temperatureStatus) {
            switch (temperatureStatus) {
                case "Too Low":
                    return "It's too cold 🥶. Move your plant to a warmer spot.";
                case "Too High":
                    return "It's too hot! Cool down the environment 🥵.";
                default:
                    return "Temperature is spot on 🌡️!";
            }
        }


        private String getSoilMoistureRecommendation(String soilMoistureStatus) {
            switch (soilMoistureStatus) {
                case "Too Low":
                    return "Soil is dry 🌵. Water it gently.";
                case "Too High":
                    return "Soil is too wet 🌧️. Let it dry out.";
                default:
                    return "Soil moisture is perfect 🌱.";
            }
        }

        private OptimalCondition getOptimalConditionsFromDb(String plantType) {
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            OptimalCondition optimalCondition = null;

            String query = "SELECT * FROM OptimalConditions WHERE type = ?";
            Cursor cursor = db.rawQuery(query, new String[]{plantType});

            if (cursor != null && cursor.moveToFirst()) {
                optimalCondition = new OptimalCondition(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("type")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("min_age")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("max_age")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("min_light")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("max_light")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("min_water")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("max_water")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("min_temperature")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("max_temperature")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("min_soil_humidity")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("max_soil_humidity")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("min_ambient_humidity")),
                        cursor.getFloat(cursor.getColumnIndexOrThrow("max_ambient_humidity"))
                );
            }

            if (cursor != null) {
                cursor.close();
            }
            db.close();
            return optimalCondition;
        }
    }
