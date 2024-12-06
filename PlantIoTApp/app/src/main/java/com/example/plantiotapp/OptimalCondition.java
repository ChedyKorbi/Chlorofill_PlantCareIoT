package com.example.plantiotapp;

public class OptimalCondition {
    private long id;
    private String type;
    private int minAge;
    private int maxAge;
    private float minLight;
    private float maxLight;
    private float minWater;
    private float maxWater;
    private float minTemperature;
    private float maxTemperature;

    private float minSoilHumidity;
    private float maxSoilHumidity;
    private float minAmbientHumidity;
    private float maxAmbientHumidity;

    // Constructor
    public OptimalCondition(long id, String type, int minAge, int maxAge, float minLight, float maxLight,
                            float minWater, float maxWater, float minTemperature, float maxTemperature
                            , float minSoilHumidity, float maxSoilHumidity,
                            float minAmbientHumidity, float maxAmbientHumidity) {
        this.id = id;
        this.type = type;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.minLight = minLight;
        this.maxLight = maxLight;
        this.minWater = minWater;
        this.maxWater = maxWater;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;

        this.minSoilHumidity = minSoilHumidity;
        this.maxSoilHumidity = maxSoilHumidity;
        this.minAmbientHumidity = minAmbientHumidity;
        this.maxAmbientHumidity = maxAmbientHumidity;
    }

    // Getter and Setter methods
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public float getMinLight() {
        return minLight;
    }

    public void setMinLight(float minLight) {
        this.minLight = minLight;
    }

    public float getMaxLight() {
        return maxLight;
    }

    public void setMaxLight(float maxLight) {
        this.maxLight = maxLight;
    }

    public float getMinWater() {
        return minWater;
    }

    public void setMinWater(float minWater) {
        this.minWater = minWater;
    }

    public float getMaxWater() {
        return maxWater;
    }

    public void setMaxWater(float maxWater) {
        this.maxWater = maxWater;
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(float minTemperature) {
        this.minTemperature = minTemperature;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(float maxTemperature) {
        this.maxTemperature = maxTemperature;
    }



    public float getMinSoilHumidity() {
        return minSoilHumidity;
    }

    public void setMinSoilHumidity(float minSoilHumidity) {
        this.minSoilHumidity = minSoilHumidity;
    }

    public float getMaxSoilHumidity() {
        return maxSoilHumidity;
    }

    public void setMaxSoilHumidity(float maxSoilHumidity) {
        this.maxSoilHumidity = maxSoilHumidity;
    }

    public float getMinAmbientHumidity() {
        return minAmbientHumidity;
    }

    public void setMinAmbientHumidity(float minAmbientHumidity) {
        this.minAmbientHumidity = minAmbientHumidity;
    }

    public float getMaxAmbientHumidity() {
        return maxAmbientHumidity;
    }

    public void setMaxAmbientHumidity(float maxAmbientHumidity) {
        this.maxAmbientHumidity = maxAmbientHumidity;
    }
}
