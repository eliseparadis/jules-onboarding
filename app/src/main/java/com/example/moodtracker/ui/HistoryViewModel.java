package com.example.moodtracker.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.moodtracker.data.DailyLog;
import com.example.moodtracker.data.MoodDao;
import com.example.moodtracker.data.MoodDatabase;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private MoodDao moodDao;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        MoodDatabase db = MoodDatabase.getDatabase(application);
        moodDao = db.moodDao();
    }

    public LiveData<List<DailyLog>> getAllLogs() {
        return moodDao.getAllDailyLogs();
    }
}
