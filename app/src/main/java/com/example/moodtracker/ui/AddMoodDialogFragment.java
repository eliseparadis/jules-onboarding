package com.example.moodtracker.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.moodtracker.R;

public class AddMoodDialogFragment extends DialogFragment {

    public static final int MOOD_HAPPY = 1;
    public static final int MOOD_NEUTRAL = 2;
    public static final int MOOD_SAD = 3;

    public interface AddMoodDialogListener {
        void onMoodSelected(int mood);
    }

    private AddMoodDialogListener listener;

    public void setListener(AddMoodDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_mood, null);
        builder.setView(view);

        Button happyButton = view.findViewById(R.id.happyButton);
        Button neutralButton = view.findViewById(R.id.neutralButton);
        Button sadButton = view.findViewById(R.id.sadButton);

        happyButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMoodSelected(MOOD_HAPPY);
            }
            dismiss();
        });

        neutralButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMoodSelected(MOOD_NEUTRAL);
            }
            dismiss();
        });

        sadButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMoodSelected(MOOD_SAD);
            }
            dismiss();
        });

        return builder.create();
    }
}
