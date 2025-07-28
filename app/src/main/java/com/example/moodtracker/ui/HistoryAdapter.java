package com.example.moodtracker.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodtracker.R;
import com.example.moodtracker.data.DailyLog;

import java.util.Collections;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<DailyLog> dailyLogs = Collections.emptyList();

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        DailyLog currentLog = dailyLogs.get(position);
        holder.dateTextView.setText(currentLog.getDate());
        holder.exerciseTextView.setText(currentLog.isExercised() ? "Exercised" : "Did not exercise");
        holder.foodTextView.setText(currentLog.isAteEnough() ? "Ate enough" : "Did not eat enough");
        holder.stepsTextView.setText("Steps: " + currentLog.getSteps());
        // Moods are not displayed for now, as it would require a more complex data structure
        // or inefficient queries.
    }

    @Override
    public int getItemCount() {
        return dailyLogs.size();
    }

    public void setDailyLogs(List<DailyLog> dailyLogs) {
        this.dailyLogs = dailyLogs;
        notifyDataSetChanged();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateTextView;
        private final TextView mood1TextView;
        private final TextView mood2TextView;
        private final TextView mood3TextView;
        private final TextView exerciseTextView;
        private final TextView foodTextView;
        private final TextView stepsTextView;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            mood1TextView = itemView.findViewById(R.id.mood1TextView);
            mood2TextView = itemView.findViewById(R.id.mood2TextView);
            mood3TextView = itemView.findViewById(R.id.mood3TextView);
            exerciseTextView = itemView.findViewById(R.id.exerciseTextView);
            foodTextView = itemView.findViewById(R.id.foodTextView);
            stepsTextView = itemView.findViewById(R.id.stepsHistoryTextView);
        }
    }
}
