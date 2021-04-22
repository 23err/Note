package com.example.note.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.note.MainActivity;
import com.example.note.R;
import com.example.note.beans.Note;
import com.example.note.observe.Publisher;
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;
import com.google.android.material.textview.MaterialTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteFragment extends Fragment {

    public static final String NOTE_PARCELABLE_KEY = "Note";
    private MaterialTextView tvDate;
    private EditText etBody, etName;
    private Note note;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private boolean isNew = false;
    private Publisher publisher;



    public static NoteFragment getInstance() {
        NoteFragment noteFragment = new NoteFragment();
        return noteFragment;
    }

    public static NoteFragment getInstance(Note note) {
        NoteFragment noteFragment = getInstance();
        Bundle args = new Bundle();
        args.putParcelable(NOTE_PARCELABLE_KEY, note);
        noteFragment.setArguments(args);
        return noteFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            note = args.getParcelable(NOTE_PARCELABLE_KEY);
        } else {
            note = new Note("", "");
            isNew = true;
        }
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity)getActivity();
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        publisher = null;

    }



    private void populateNote() {
        note.setBody(etBody.getText().toString());
        note.setName(etName.getText().toString());
        tvDate.getText().toString();
        try {
            note.setDate(dateFormat.parse(tvDate.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("parse", "parce date in note exception");
        }
    }

    @Override
    public void onStop() {
        populateNote();
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (publisher != null) {
            publisher.notifySingle(note, isNew);
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
                    (datePicker, i, i1, i2) -> {
                        String stringDate = String.format("%02d.%02d.%4d", i2, i1, i);
                        tvDate.setText(stringDate);
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

