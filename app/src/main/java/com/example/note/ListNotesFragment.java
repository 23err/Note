package com.example.note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ListNotesFragment extends Fragment {
    private ImageButton btnAdd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);

        btnAdd.setOnClickListener(view1 -> {
            Fragment fragment = new NoteFragment();
            requireFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .addToBackStack("note")
                    .commit();
        });
    }

    private void findViews(@NonNull View view) {
        btnAdd = view.findViewById(R.id.btnAdd);
    }


}
