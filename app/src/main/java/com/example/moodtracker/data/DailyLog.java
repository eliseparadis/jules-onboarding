package com.example.moodtracker.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "daily_logs")
public class DailyLog {

    @PrimaryKey
    @NonNull
    private String date;

    private boolean exercised;
    private boolean ateEnough;
    private int steps;

    public DailyLog(@NonNull String date, boolean exercised, boolean ateEnough, int steps) {
        this.date = date;
        this.exercised = exercised;
        this.ateEnough = ateEnough;
        this.steps = steps;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public boolean isExercised() {
        return exercised;
    }

    public boolean isAteEnough() {
        return ateEnough;
    }

    public int getSteps() {
        return steps;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public void setExercised(boolean exercised) {
        this.exercised = exercised;
    }

    public void setAteEnough(boolean ateEnough) {
        this.ateEnough = ateEnough;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}
