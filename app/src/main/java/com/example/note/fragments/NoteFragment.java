package com.example.note.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.note.R;
import com.example.note.beans.Note;
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;
import com.google.android.material.textview.MaterialTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteFragment extends Fragment {

    private MaterialTextView tvDate;
    private EditText etBody, etName;
    private Note note;
    private OnNoteUpdateListener noteUpdateListener;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public interface OnNoteUpdateListener {
        void onUpdateNote(Note note);
    };

    public static NoteFragment getInstance(Note note) {
        NoteFragment noteFragment = new NoteFragment();
        noteFragment.note = note;
        return noteFragment;
    }

    public NoteFragment(){
        note = new Note("","");
    }


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
        setValues();
        setDatePickerDialog();
    }

    @Override
    public void onPause() {
        super.onPause();
        note.setBody(etBody.getText().toString());
        note.setName(etName.getText().toString());
        tvDate.getText().toString();
        try {
            note.setDate(dateFormat.parse(tvDate.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("parse", "parce date in note exception");
        }
        notifyNoteUpdate();
    }

    public void setNoteUpdateListener(OnNoteUpdateListener listener) {
        noteUpdateListener = listener;
    }

    private void notifyNoteUpdate() {
        if (noteUpdateListener != null) {
            noteUpdateListener.onUpdateNote(note);
        }
    }

    private void setValues() {
        etName.setText(note.getName());
        etBody.setText(note.getBody());
        tvDate.setText(dateFormat.format(note.getDate()));
    }

    @SuppressLint("RestrictedApi")
    private void setDatePickerDialog() {
        tvDate.setOnClickListener(view1 -> {
            Calendar calendar = Calendar.getInstance();
            new MaterialStyledDatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            String stringDate = String.format("%02d.%02d.%4d", i2, i1, i);
                            tvDate.setText(stringDate);
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
        etBody = view.findViewById(R.id.etBody);
        etName = view.findViewById(R.id.etName);

    }
}

