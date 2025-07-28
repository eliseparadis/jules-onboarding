package com.example.moodtracker.ui;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.moodtracker.data.MoodDao;
import com.example.moodtracker.data.MoodDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private MoodDatabase moodDatabase;

    @Mock
    private MoodDao moodDao;

    @Mock
    private Application application;

    private MainViewModel mainViewModel;

    @Before
    public void setUp() {
        when(moodDatabase.moodDao()).thenReturn(moodDao);
        mainViewModel = new MainViewModel(application);
        // This is a hack to inject the mocked dao.
        // A better approach would be to use a dependency injection framework.
        try {
            java.lang.reflect.Field daoField = MainViewModel.class.getDeclaredField("moodDao");
            daoField.setAccessible(true);
            daoField.set(mainViewModel, moodDao);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertMoodEntry_insertsMoodEntryAndDailyLog() {
        mainViewModel.insertMoodEntry(1);
        // We need to wait for the executor to finish.
        // This is not ideal, a better way would be to use a TestCoroutineDispatcher.
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        verify(moodDao).insertMoodEntry(any());
        verify(moodDao).insertDailyLog(any());
    }
}
