package com.example.moodtracker.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.moodtracker.data.DailyLog;
import com.example.moodtracker.data.MoodDao;
import com.example.moodtracker.data.MoodDatabase;
import com.example.moodtracker.data.MoodEntry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainViewModel extends AndroidViewModel {

    private MoodDao moodDao;
    private ExecutorService executorService;

    public MainViewModel(@NonNull Application application) {
        super(application);
        MoodDatabase db = MoodDatabase.getDatabase(application);
        moodDao = db.moodDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<DailyLog> getDailyLog(String date) {
        return moodDao.getDailyLog(date);
    }

    public LiveData<List<MoodEntry>> getMoodEntries(String date) {
        return moodDao.getMoodEntries(date);
    }

    public void insertMoodEntry(int mood) {
        executorService.execute(() -> {
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            long timestamp = System.currentTimeMillis();
            MoodEntry moodEntry = new MoodEntry(timestamp, mood, date);
            moodDao.insertMoodEntry(moodEntry);

            // Also ensure a daily log exists for this date
            if (moodDao.getDailyLogSync(date) == null) {
                moodDao.insertDailyLog(new DailyLog(date, false, false, 0));
            }
        });
    }

    public void updateDailyLog(DailyLog dailyLog) {
        executorService.execute(() -> moodDao.updateDailyLog(dailyLog));
    }
}
