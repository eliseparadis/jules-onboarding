package com.example.moodtracker.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodtracker.R;

public class HistoryActivity extends AppCompatActivity {

    private HistoryViewModel historyViewModel;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RecyclerView recyclerView = findViewById(R.id.historyRecyclerView);
        adapter = new HistoryAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        historyViewModel.getAllLogs().observe(this, dailyLogs -> {
            // This is a simplification. A better approach would be to have a combined object
            // with DailyLog and its MoodEntries.
            // For now, we just pass the daily logs to the adapter.
            // The adapter will then have to query the mood entries for each log.
            // This is not efficient, but it's a start.
            adapter.setDailyLogs(dailyLogs);
        });
    }
}
