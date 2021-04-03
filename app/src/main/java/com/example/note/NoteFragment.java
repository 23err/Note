package com.example.note;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;
import com.google.android.material.textview.MaterialTextView;

import java.util.Calendar;
import java.util.Date;

public class NoteFragment extends Fragment {

    private MaterialTextView tvDate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);

        setDatePickerDialog();

    }

    @SuppressLint("RestrictedApi")
    private void setDatePickerDialog() {
        tvDate.setOnClickListener(view1 -> {
            Calendar calendar = Calendar.getInstance();
            new MaterialStyledDatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            String date = String.format("%02d.%02d.%4d", i2, i1, i);
                            tvDate.setText(date);
                        }
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            )
            .show();
        });
    }

    private void findViews(@NonNull View view) {
        tvDate = view.findViewById(R.id.tvDate);
    }
}

