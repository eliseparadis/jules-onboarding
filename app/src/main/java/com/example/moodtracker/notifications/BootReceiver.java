package com.example.moodtracker.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // In a real app, you would re-schedule your alarms here.
            // For now, we will just create the notification channel.
            // The alarms will be scheduled when the MainActivity is opened.
            NotificationHelper.createNotificationChannel(context);
        }
    }
}
