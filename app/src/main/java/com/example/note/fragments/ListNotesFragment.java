package com.example.note.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note.R;
import com.example.note.beans.Note;
import com.example.note.repo.NoteRepository;
import com.example.note.repo.NoteRepositoryFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListNotesFragment extends Fragment {
    private FloatingActionButton btnAdd;
    private RecyclerView rvNotes;
    private NoteRepository repo;
    private NotesListRVAdapter adapter;
    private List<Note> noteList;
    private SearchView searchView;

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

        repo = NoteRepositoryFactory.getInstance();
        noteList = new ArrayList<Note>();
        setAllNotesToList();

        adapter = new NotesListRVAdapter(getContext(), noteList);
        adapterSetOnItemClick();
        rvNotes.setAdapter(adapter);

        btnSetOnClick();
        searchChange();
    }

    private void setAllNotesToList() {
        noteList.clear();
        noteList.addAll(repo.getNoteList());
    }

    private void adapterSetOnItemClick() {
        adapter.setOnItemClickListener((view1, note) -> {
            NoteFragment fragment = NoteFragment.getInstance(note);
            setFragmentUpdateListener(fragment);
            showFragment(fragment);
        });
    }

    private void searchChange() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().length() == 0) {
                    setAllNotesToList();
                } else {
                    noteList.clear();
                    noteList.addAll(repo.findNotes(newText));
                }
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void setFragmentUpdateListener(NoteFragment fragment) {
        fragment.setNoteUpdateListener(note1 -> {
//            adapter.notifyItemChanged(repo.getIndex(note1));
            repo.insertOrUpdateNote(note1);
            adapter.notifyDataSetChanged();
        });
    }


    private void showFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .addToBackStack("note")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }




    private void btnSetOnClick() {
        btnAdd.setOnClickListener(view1 -> {
            Note note = new Note();
            NoteFragment fragment = NoteFragment.getInstance(note);
            setFragmentUpdateListener(fragment);
            showFragment(fragment);
        });
    }

    private void findViews(@NonNull View view) {
        btnAdd = view.findViewById(R.id.btnAdd);
        rvNotes = view.findViewById(R.id.rvNotes);
        searchView = view.findViewById(R.id.searchView);

    }


}
