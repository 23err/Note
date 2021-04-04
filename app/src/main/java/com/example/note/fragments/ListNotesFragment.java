package com.example.note.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note.R;
import com.example.note.beans.Note;
import com.example.note.repo.NoteRepository;
import com.example.note.repo.NoteRepositoryFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

public class ListNotesFragment extends Fragment {
    private FloatingActionButton btnAdd;
    private RecyclerView rvNotes;
    private NoteRepository repo;
    private NotesListRVAdapter adapter;
    private List<Note> noteList;

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

        btnSetOnClick();

        initRepo();

        noteList = repo.getNoteList();

        adapter = new NotesListRVAdapter(getContext(), repo.getNoteList());
        adapter.setOnItemClickListener((view1, note) -> {
            NoteFragment fragment = NoteFragment.getInstance(note);
            setFragmentUpdateListener(fragment);
            showFragment(fragment);
        });
        rvNotes.setAdapter(adapter);

    }

    private void setFragmentUpdateListener(NoteFragment fragment) {
        fragment.setNoteUpdateListener(note1 -> {
            adapter.notifyItemChanged(repo.getIndex(note1));
        });
    }


    private void showFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .addToBackStack("note")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }


    private void initRepo() {
        repo = NoteRepositoryFactory.getInstance();
        repo.insertNote(new Note("first note", "aklsjhfkkhsdf askdjfhka", new Date()));
        repo.insertNote(new Note("second note", "aklsjhfkkhsdf askdjfhka", new Date()));
        repo.insertNote(new Note("", "third note in body", new Date()));
        repo.insertNote(new Note("ajsd ajsdkfhlksjhdf sdjklfhaskjdfh sakjdfh alskdj fksdhf note", "aklsjhfkkhsdf askdjfhka", new Date()));
//        for (int i = 0; i < 30; i++) {
//            repo.insertNote(new Note("other note", "test text", new Date()));
//
//        }
    }


    private void btnSetOnClick() {
        btnAdd.setOnClickListener(view1 -> {
            Note note = new Note("", "");
            noteList.add(note);
            NoteFragment fragment = NoteFragment.getInstance(note);
            setFragmentUpdateListener(fragment);
            showFragment(fragment);
        });
    }

    private void findViews(@NonNull View view) {
        btnAdd = view.findViewById(R.id.btnAdd);
        rvNotes = view.findViewById(R.id.rvNotes);
    }


}
