package com.health.personaltracker.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fitness_records")
public class FitnessRecord {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int age;
    private float weight;
    private int steps;
    private boolean exercise;
    private long timestamp; // Stores Unix timestamp (milliseconds since epoch)
    private int dayOfWeek; // e.g., "Monday", or use int (1-7)

    // Constructor (Room will use this)
    public FitnessRecord(int age, float weight, int steps, boolean exercise, long timestamp, int dayOfWeek) {
        this.age = age;
        this.weight = weight;
        this.steps = steps;
        this.exercise = exercise;
        this.timestamp = timestamp;
        this.dayOfWeek = dayOfWeek;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public boolean isExercise() {
        return exercise;
    }

    public void setExercise(boolean exercise) {
        this.exercise = exercise;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}