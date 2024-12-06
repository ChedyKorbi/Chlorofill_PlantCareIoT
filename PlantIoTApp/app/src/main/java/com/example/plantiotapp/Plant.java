package com.example.plantiotapp;

public class Plant {
    private int id;
    private String name;
    private String type;
    private int age;
    private String dateAdded;

    // Constructor
    public Plant(int id, String name, String type, int age, String dateAdded) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.age = age;
        this.dateAdded = dateAdded;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
}
