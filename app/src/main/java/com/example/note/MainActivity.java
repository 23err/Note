package com.example.note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.res.Configuration;
import android.os.Bundle;

import com.example.note.beans.Note;
import com.example.note.fragments.ListNotesFragment;
import com.example.note.fragments.NoteFragment;
import com.example.note.repo.NoteRepository;
import com.example.note.repo.NoteRepositoryFactory;

public class MainActivity extends AppCompatActivity {

    protected boolean isLandscapeOrientation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isLandscapeOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        Fragment fragment;
        if (isLandscapeOrientation) {
            NoteRepository repo = NoteRepositoryFactory.getInstance();
            Note note;
            int lastOpenedNote = ListNotesFragment.getLastOpenedNote();
            if (repo.getSize() > 0 && lastOpenedNote != -1) {
                note = repo.getNote(lastOpenedNote);
            }else {
                note = new Note();
            }
            fragment = NoteFragment.getInstance(note);
        } else {
            fragment =  new ListNotesFragment();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }


}