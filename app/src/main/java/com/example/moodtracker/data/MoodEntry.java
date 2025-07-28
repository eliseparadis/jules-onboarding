package com.example.moodtracker.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "mood_entries",
        foreignKeys = @ForeignKey(entity = DailyLog.class,
                                  parentColumns = "date",
                                  childColumns = "date",
                                  onDelete = ForeignKey.CASCADE))
public class MoodEntry {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long timestamp;
    private int mood;
    private String date;

    public MoodEntry(long timestamp, int mood, String date) {
        this.timestamp = timestamp;
        this.mood = mood;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getMood() {
        return mood;
    }

    public String getDate() {
        return date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
