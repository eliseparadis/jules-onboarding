package com.example.moodtracker.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DailyLog.class, MoodEntry.class}, version = 1, exportSchema = false)
public abstract class MoodDatabase extends RoomDatabase {

    public abstract MoodDao moodDao();

    private static volatile MoodDatabase INSTANCE;

    public static MoodDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MoodDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MoodDatabase.class, "mood_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
