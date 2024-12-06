#include <WiFi.h>
#include <HTTPClient.h>  // For HTTP requests
#include <DHT.h>         // For DHT sensor
#include <ArduinoJson.h> // For JSON handling

// Wi-Fi credentials
const char* ssid = "iPhoneAziz";
const char* password = "12345678910111213141516171819202122";

// Firebase settings
const char* firebaseHost = "https://plante-7b6f6-default-rtdb.europe-west1.firebasedatabase.app/";
const char* firebaseAuth = "6hI1hMDAwaIVdV7jMvDFt2wjR1wfkkUr8oWw6YkD"; // Firebase Database Secret

// DHT sensor settings
#define DHTPIN 5            // GPIO connected to DHT11 data pin
#define DHTTYPE DHT11       // Sensor type DHT11
DHT dht(DHTPIN, DHTTYPE);

// Sensor and relay pins
#define SOIL_SENSOR_PIN 35  // GPIO for soil moisture sensor
#define LIGHT_SENSOR_PIN 34 // GPIO for light sensor
#define RELAY_PIN 13        // GPIO pin connected to relay module
#define wifi 4
// Calibration values for soil moisture sensor (adjust based on your sensor readings)
const int dryValue = 4095;   // Value for completely dry soil
const int wetValue = 1000;   // Value for fully saturated soil

void setup() {
  Serial.begin(115200);
  
  // Initialize DHT sensor
  dht.begin();

  // Set sensor and relay pins 
  pinMode(SOIL_SENSOR_PIN, INPUT);
  pinMode(LIGHT_SENSOR_PIN, INPUT);
  pinMode(RELAY_PIN, OUTPUT);
  digitalWrite(RELAY_PIN, LOW); // Start with relay OFF
  digitalWrite(wifi, LOW);
  // Connect to Wi-Fi
  Serial.print("Connecting to Wi-Fi...");
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nConnected to Wi-Fi");
  digitalWrite(wifi, HIGH);
}

void loop() {
  // Read temperature and humidity from DHT11 sensor
  float humidity = dht.readHumidity();
  float temperature = dht.readTemperature();

  // Check if readings are valid
  if (isnan(humidity) || isnan(temperature)) {
    Serial.println("Failed to read from DHT sensor!");
    return;
  }

  // Read soil moisture and light level values
  int soilMoistureRaw = analogRead(SOIL_SENSOR_PIN);
  int lightLevel = analogRead(LIGHT_SENSOR_PIN);

  // Convert soil moisture raw value to percentage
  int moisturePercentage = map(soilMoistureRaw, dryValue, wetValue, 0, 100);
  moisturePercentage = constrain(moisturePercentage, 0, 100);  // Ensure within 0-100%

  // Display sensor data on Serial Monitor
  Serial.print("Temperature: ");
  Serial.print(temperature);
  Serial.print(" °C, Humidity: ");
  Serial.print(humidity);
  Serial.print(" %, Light Level: ");
  Serial.print(lightLevel);
  Serial.print(", Moisture Percentage: ");
  Serial.print(moisturePercentage);
  Serial.println(" %");

  // Send sensor data to Firebase
  sendToFirebase(temperature, humidity, moisturePercentage, lightLevel);

  // Check relay command from Firebase
  checkRelayCommand();

  delay(1000); // Wait 1 second before the next cycle
}

// Function to send sensor data to Firebase using PATCH
void sendToFirebase(float temperature, float humidity, int moisturePercentage, int lightLevel) {
  if (WiFi.status() == WL_CONNECTED) {
    HTTPClient http;
    String url = String(firebaseHost) + "/sensorData.json?auth=" + firebaseAuth;

    // Create JSON payload
    StaticJsonDocument<256> jsonDoc;
    jsonDoc["temperature"] = temperature;
    jsonDoc["humidity"] = humidity;
    jsonDoc["soilMoisture"] = moisturePercentage;  
    jsonDoc["lightLevel"] = lightLevel;

    String jsonData;
    serializeJson(jsonDoc, jsonData);

    // Send HTTP PATCH request
    http.begin(url);
    http.addHeader("Content-Type", "application/json");

    int httpResponseCode = http.PATCH(jsonData); // Using PATCH to only update fields in jsonDoc

    if (httpResponseCode > 0) {
      Serial.print("Data sent to Firebase successfully, response code: ");
      Serial.println(httpResponseCode);
    } else {
      Serial.print("Failed to send data, error: ");
      Serial.println(http.errorToString(httpResponseCode));
    }

    http.end(); // End the HTTP connection
  } else {
    Serial.println("Wi-Fi is not connected");
  }
}

// Function to check relay command in Firebase
void checkRelayCommand() {
  if (WiFi.status() == WL_CONNECTED) {
    HTTPClient http;
    String url = String(firebaseHost) + "/sensorData/relayCommand.json?auth=" + firebaseAuth;

    http.begin(url);
    int httpResponseCode = http.GET();

    if (httpResponseCode > 0) {
      String payload = http.getString();
      int relayCommand = payload.toInt();

      // Update relay state based on relayCommand value
      if (relayCommand == 1) {
        digitalWrite(RELAY_PIN, HIGH); // Turn relay ON
        Serial.println("Relay is ON");
      } else {
        digitalWrite(RELAY_PIN, LOW);  // Turn relay OFF
        Serial.println("Relay is OFF");
      }
    } else {
      Serial.print("Error getting relay command: ");
      Serial.println(http.errorToString(httpResponseCode));
    }

    http.end(); // End the HTTP connection
  }
}
