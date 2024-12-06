package com.example.plantiotapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LOCALPLANTDB";
    private static final int DATABASE_VERSION = 5; // Incremented for schema changes (make sure this is updated correctly)

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Plants table with constraints
        db.execSQL("CREATE TABLE IF NOT EXISTS Plants (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "type TEXT NOT NULL, " +
                "age INTEGER NOT NULL, " +
                "date_added TEXT NOT NULL, " +
                "UNIQUE(name, type) ON CONFLICT REPLACE)");  // Ensures unique combinations

        // Create OptimalConditions table with constraints
        db.execSQL("CREATE TABLE IF NOT EXISTS OptimalConditions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type TEXT NOT NULL, " +
                "min_age INTEGER NOT NULL, " +
                "max_age INTEGER NOT NULL, " +
                "min_light FLOAT NOT NULL, " +
                "max_light FLOAT NOT NULL, " +
                "min_water FLOAT NOT NULL, " +
                "max_water FLOAT NOT NULL, " +
                "min_temperature FLOAT NOT NULL, " +
                "max_temperature FLOAT NOT NULL, " +
                "min_soil_pH FLOAT NOT NULL, " +
                "max_soil_pH FLOAT NOT NULL, " +
                "min_soil_humidity FLOAT NOT NULL, " +
                "max_soil_humidity FLOAT NOT NULL, " +
                "min_ambient_humidity FLOAT NOT NULL, " +
                "max_ambient_humidity FLOAT NOT NULL, " +
                "UNIQUE(type) ON CONFLICT REPLACE)"); // Ensures type uniqueness in optimal conditions

        // Preload default optimal conditions
        preloadOptimalConditions(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {  // Handle version-specific upgrades
            // Migration logic for newer versions
            // You can add schema changes here, such as adding new columns, etc.

            // Example: Add a new column to the Plants table (if needed)
            // db.execSQL("ALTER TABLE Plants ADD COLUMN new_column INTEGER DEFAULT 0");

            // Don't drop tables unless absolutely necessary (use with caution)
            // db.execSQL("DROP TABLE IF EXISTS Plants");
            // db.execSQL("DROP TABLE IF EXISTS OptimalConditions");
        }
    }

    // Method to preload default optimal conditions
    private void preloadOptimalConditions(SQLiteDatabase db) {
        // Only insert the default conditions if the table is empty
        String checkQuery = "SELECT COUNT(*) FROM OptimalConditions";
        Cursor cursor = db.rawQuery(checkQuery, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count == 0) {
            String insertQuery = "INSERT INTO OptimalConditions (type, min_age, max_age, min_light, max_light, " +
                    "min_water, max_water, min_temperature, max_temperature, " +
                    "min_soil_pH, max_soil_pH, min_soil_humidity, max_soil_humidity, " +
                    "min_ambient_humidity, max_ambient_humidity) VALUES " +
                    "('Ficus Ginseng', 0, 120, 800, 1500, 60, 70, 18, 25, 6.0, 7.0, 40, 60, 50, 60), " +
                    "('Schefflera', 0, 120, 1000, 2000, 50, 60, 18, 27, 5.5, 6.5, 40, 50, 40, 50), " +
                    "('Géranium Dwarf', 0, 120, 2000, 4000, 40, 50, 16, 24, 6.0, 7.5, 30, 40, 30, 40), " +
                    "('Snake Plant', 0, 120, 300, 800, 20, 30, 18, 30, 4.5, 7.5, 20, 30, 30, 40), " +
                    "('Cuphea', 0, 120, 1000, 2000, 50, 70, 18, 25, 6.5, 7.5, 50, 70, 50, 60), " +
                    "('Croton', 0, 120, 1500, 3000, 50, 70, 20, 26, 5.5, 6.5, 40, 60, 40, 60);";
            db.execSQL(insertQuery);
        }
    }
}
