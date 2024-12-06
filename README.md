

# Chlorofill: IoT-Enabled Plant Care System

**Chlorofill** is an innovative IoT-based plant health monitoring and management system. This project leverages advanced Internet of Things (IoT) technology and a mobile application to track and provide real-time insights on plant health. By utilizing environmental data like temperature, humidity, light levels, and soil moisture, Chlorofill provides users with personalized care recommendations for their plants.

This system consists of an IoT device (based on Arduino) that collects data from various sensors and sends it to a cloud-based database (Firebase). The mobile application connects to Firebase, retrieves the data, and provides actionable insights to the user.

---

## Features

- **Real-time Data Collection**: Continuously monitors environmental factors such as temperature, humidity, light, and soil moisture.
- **Firebase Integration**: Sends real-time data to Firebase for cloud storage and retrieval.
- **Automated Relay Control**: A relay is used to control devices like a water pump or light based on sensor readings, which can be managed through Firebase.
- **Mobile Application**: Provides users with a mobile interface to monitor their plant health, receive notifications, and access personalized care recommendations.
- **Personalized Recommendations**: Based on real-time data, the app offers specific care tips for each plant.
- **Wi-Fi Connectivity**: The IoT device connects to Wi-Fi to send data to Firebase.

---

## Architecture

1. **IoT Device (Arduino-based)**:
   - **Sensors**:
     - DHT11 for temperature and humidity
     - Soil moisture sensor
     - Light sensor
   - **Relay**: Controls plant care systems (e.g., watering, lighting) based on sensor readings.
   - **Wi-Fi**: Connects the device to the internet for sending data to Firebase.
   - **Firebase**: Firebase Real-Time Database stores sensor data, including temperature, humidity, light levels, and soil moisture.

2. **Mobile Application**:
   - Displays sensor data and provides feedback to the user.
   - Firebase integration for real-time data updates.
   - Provides push notifications and personalized care recommendations based on the plant's environment.

---

## Technologies Used

- **IoT**: Arduino, Wi-Fi Module (ESP32/ESP8266)
- **Mobile Application**: Android (Java, XML)
- **Firebase**: Firebase Realtime Database
- **Sensors**:
  - DHT11 for temperature and humidity.
  - Soil moisture sensor for soil moisture levels.
  - Light sensor for ambient light levels.
- **Cloud**: Firebase for data storage, real-time updates, and relay control.

---

## Hardware Requirements

- **Arduino/ESP32/ESP8266 Board**
- **DHT11 Temperature and Humidity Sensor**
- **Soil Moisture Sensor**
- **Light Sensor**
- **Relay Module**
- **Wi-Fi Router for Connectivity**
- **Power Supply for Arduino/ESP32**

---

## Setup Instructions

### IoT Device Setup (Arduino)

1. **Install Arduino IDE**: Download and install the [Arduino IDE](https://www.arduino.cc/en/software).
2. **Install Required Libraries**:
   - `WiFi.h` for Wi-Fi connectivity.
   - `HTTPClient.h` for making HTTP requests to Firebase.
   - `DHT.h` for reading temperature and humidity data.
   - `ArduinoJson.h` for handling JSON data.
3. **Upload the Code**: Connect your Arduino/ESP32 to your computer and upload the provided code to the device.
4. **Configure Wi-Fi**: Replace the Wi-Fi credentials in the code with your network details (`ssid` and `password`).
5. **Firebase Setup**:
   - Set up a Firebase project at [Firebase Console](https://console.firebase.google.com/).
   - Create a Realtime Database and set the rules for read and write access.
   - Copy your Firebase database URL and authentication secret and update them in the code.
6. **Connect the Sensors**: Wire the sensors to the appropriate pins on your Arduino. The soil moisture sensor, light sensor, and DHT11 sensor should be connected as per the pin definitions in the code.

### Mobile App Setup

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-repository-url.git
   ```
2. **Open the Project in Android Studio**: Open the cloned project in Android Studio.
3. **Add Firebase to Your Android App**:
   - Go to the [Firebase Console](https://console.firebase.google.com/), create a new project, and add Firebase to your Android app.
   - Add the `google-services.json` file to your project (under the `app` directory).
   - Make sure to enable Firebase Realtime Database in your Firebase project settings.
4. **Run the App**: Build and run the app on your Android device. The app will automatically connect to Firebase and display real-time sensor data.

---

## Functions of the Code

### Arduino Code

- **Sensor Data Collection**: The code continuously reads data from the DHT11 (temperature and humidity), soil moisture sensor, and light sensor.
- **Sending Data to Firebase**: The sensor data is sent to Firebase using HTTP requests in JSON format.
- **Relay Control**: The code periodically checks for commands from Firebase to control the relay (e.g., turning a watering system on or off).
- **Error Handling**: It checks for errors in sensor readings and Wi-Fi connectivity, providing feedback on the serial monitor.

### Mobile Application

- **Real-Time Data Display**: The app fetches and displays the data from Firebase in real time.
- **User Notifications**: Sends notifications to users when plants need care (e.g., low moisture or light).
- **Firebase Integration**: Uses Firebase SDK for real-time database operations and cloud storage.
- **Personalized Care Tips**: Based on the data, the app gives suggestions such as optimal watering times and light conditions.

---

## Firebase Database Structure

The Firebase Realtime Database follows the structure below:

```
sensorData: {
  temperature: number,
  humidity: number,
  soilMoisture: number,
  lightLevel: number,
  relayCommand: number (0 for OFF, 1 for ON)
}
```

The **relayCommand** is used by the mobile app to control devices like water pumps or grow lights.

---

## Contributions

Contributions are welcome! Please fork the repository, make changes, and submit a pull request. If you want to report issues, use GitHub Issues.

---

---

## Acknowledgments

- Thanks to [Firebase](https://firebase.google.com/) for providing the database and real-time functionality.
- Thanks to [Arduino](https://www.arduino.cc/) for their open-source platform.
- Thanks to the open-source community for the libraries and resources used in this project.

---

