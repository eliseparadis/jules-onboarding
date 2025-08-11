# MoodTracker

MoodTracker is a simple Android application that helps you track your mood and daily activities to better understand your well-being.

## Features

*   **Mood Logging:** Log your mood up to three times a day (😄, 😐, 😢).
*   **Daily Activity Tracking:**
    *   Track whether you have exercised.
    *   Track if you have eaten enough.
*   **Step Counter:** Monitors your daily steps using the phone's built-in sensor.
*   **History View:** See a history of your mood entries over time.
*   **Notifications:** Receive reminders to log your mood at 9 AM, 3 PM, and 9 PM.

## Building and Running the App

To build and run the app, you will need Android Studio.

1.  Clone the repository:
    ```bash
    git clone <repository-url>
    ```
2.  Open the project in Android Studio.
3.  Let Gradle sync and download the required dependencies.
4.  Run the app on an Android emulator or a physical device.

## Permissions

The app requires the following permissions:

*   `android.permission.ACTIVITY_RECOGNITION`: To use the step counter sensor and track your daily steps.
*   `android.permission.POST_NOTIFICATIONS`: To send you reminders to log your mood.
*   `android.permission.RECEIVE_BOOT_COMPLETED`: To reschedule notifications after the device reboots.

## Technologies Used

*   **Language:** Java
*   **UI:** Android XML
*   **Database:** Room Persistence Library for local data storage.
*   **Architecture:** Model-View-ViewModel (MVVM)
    *   **ViewModel:** To manage UI-related data in a lifecycle-conscious way.
    *   **LiveData:** To build data objects that notify views when the underlying database changes.
*   **Dependencies:**
    *   AndroidX AppCompat and Material Design for UI components.
    *   JUnit and Mockito for testing.
