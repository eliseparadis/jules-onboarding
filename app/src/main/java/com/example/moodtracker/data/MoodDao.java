package com.example.moodtracker.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDailyLog(DailyLog dailyLog);

    @Update
    void updateDailyLog(DailyLog dailyLog);

    @Query("SELECT * FROM daily_logs WHERE date = :date")
    LiveData<DailyLog> getDailyLog(String date);

    @Query("SELECT * FROM daily_logs WHERE date = :date")
    DailyLog getDailyLogSync(String date);

    @Insert
    void insertMoodEntry(MoodEntry moodEntry);

    @Query("SELECT * FROM mood_entries WHERE date = :date ORDER BY timestamp ASC")
    LiveData<List<MoodEntry>> getMoodEntries(String date);

    @Query("SELECT * FROM daily_logs ORDER BY date DESC")
    LiveData<List<DailyLog>> getAllDailyLogs();
}
