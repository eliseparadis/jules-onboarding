package com.example.moodtracker;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.moodtracker.data.DailyLog;
import com.example.moodtracker.data.MoodEntry;
import com.example.moodtracker.notifications.NotificationHelper;
import com.example.moodtracker.notifications.NotificationReceiver;
import com.example.moodtracker.ui.AddMoodDialogFragment;
import com.example.moodtracker.ui.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AddMoodDialogFragment.AddMoodDialogListener, SensorEventListener {

    private static final int ACTIVITY_RECOGNITION_REQUEST_CODE = 100;
    private static final int POST_NOTIFICATIONS_REQUEST_CODE = 101;
    private MainViewModel mainViewModel;

    private TextView moodEntry1;
    private TextView moodEntry2;
    private TextView moodEntry3;
    private CheckBox exerciseCheckBox;
    private CheckBox foodCheckBox;
    private TextView stepsTextView;

    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private boolean isStepCounterSensorPresent;
    private int totalSteps = 0;
    private DailyLog currentDailyLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        moodEntry1 = findViewById(R.id.moodEntry1);
        moodEntry2 = findViewById(R.id.moodEntry2);
        moodEntry3 = findViewById(R.id.moodEntry3);
        exerciseCheckBox = findViewById(R.id.exerciseCheckBox);
        foodCheckBox = findViewById(R.id.foodCheckBox);
        stepsTextView = findViewById(R.id.stepsTextView);

        Button addMoodButton = findViewById(R.id.addMoodButton);
        addMoodButton.setOnClickListener(v -> {
            AddMoodDialogFragment dialog = new AddMoodDialogFragment();
            dialog.setListener(this);
            dialog.show(getSupportFragmentManager(), "AddMoodDialogFragment");
        });

        Button historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, com.example.moodtracker.ui.HistoryActivity.class);
            startActivity(intent);
        });

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        mainViewModel.getDailyLog(currentDate).observe(this, this::updateDailyLogUI);
        mainViewModel.getMoodEntries(currentDate).observe(this, this::updateMoodEntriesUI);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isStepCounterSensorPresent = true;
        } else {
            stepsTextView.setText("Not available");
            isStepCounterSensorPresent = false;
        }

        exerciseCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (currentDailyLog != null) {
                currentDailyLog.setExercised(isChecked);
                mainViewModel.updateDailyLog(currentDailyLog);
            }
        });

        foodCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (currentDailyLog != null) {
                currentDailyLog.setAteEnough(isChecked);
                mainViewModel.updateDailyLog(currentDailyLog);
            }
        });

        NotificationHelper.createNotificationChannel(this);
        requestNotificationPermission();
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, POST_NOTIFICATIONS_REQUEST_CODE);
            } else {
                scheduleNotifications();
            }
        } else {
            scheduleNotifications();
        }
    }

    private void scheduleNotifications() {
        scheduleNotification(9);
        scheduleNotification(15);
        scheduleNotification(21);
    }

    private void scheduleNotification(int hour) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, hour, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(isStepCounterSensorPresent) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                    == PackageManager.PERMISSION_GRANTED) {
                sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                        ACTIVITY_RECOGNITION_REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isStepCounterSensorPresent) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTIVITY_RECOGNITION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI);
            } else {
                Toast.makeText(this, "Permission denied. Step counter will not work.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == POST_NOTIFICATIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scheduleNotifications();
            } else {
                Toast.makeText(this, "Permission denied. Notifications will not work.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateDailyLogUI(DailyLog dailyLog) {
        this.currentDailyLog = dailyLog;
        if (dailyLog != null) {
            exerciseCheckBox.setChecked(dailyLog.isExercised());
            foodCheckBox.setChecked(dailyLog.isAteEnough());
            totalSteps = dailyLog.getSteps();
            stepsTextView.setText(String.valueOf(totalSteps));
        }
    }

    private void updateMoodEntriesUI(List<MoodEntry> moodEntries) {
        TextView[] moodTextViews = {moodEntry1, moodEntry2, moodEntry3};
        for (int i = 0; i < moodTextViews.length; i++) {
            if (i < moodEntries.size()) {
                moodTextViews[i].setText(getMoodString(moodEntries.get(i).getMood()));
            } else {
                moodTextViews[i].setText("-");
            }
        }
    }

    @Override
    public void onMoodSelected(int mood) {
        mainViewModel.insertMoodEntry(mood);
    }

    private String getMoodString(int mood) {
        switch (mood) {
            case AddMoodDialogFragment.MOOD_HAPPY:
                return "😄";
            case AddMoodDialogFragment.MOOD_NEUTRAL:
                return "😐";
            case AddMoodDialogFragment.MOOD_SAD:
                return "😢";
            default:
                return "?";
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int newSteps = (int) event.values[0];
            if (totalSteps != newSteps) {
                totalSteps = newSteps;
                stepsTextView.setText(String.valueOf(totalSteps));
                if (currentDailyLog != null) {
                    currentDailyLog.setSteps(totalSteps);
                    mainViewModel.updateDailyLog(currentDailyLog);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed for this app
    }
}
