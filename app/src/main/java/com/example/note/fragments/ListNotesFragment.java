package com.example.note.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.List;

public class ListNotesFragment extends Fragment {
    private FloatingActionButton btnAdd;
    private RecyclerView rvNotes;
    private NoteRepository repo;
    private NotesListRVAdapter adapter;
    private List<Note> noteList;
    private SearchView searchView;
    private static int lastOpenedNote = -1;
    private boolean isLandscape;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isLandscape = getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
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
            repo.insertOrUpdateNote(note1);
            int index = repo.getIndex(note1);
            lastOpenedNote = index;

            setAllNotesToList();
            adapter.notifyDataSetChanged();
        });
    }

    public static int getLastOpenedNote() {
        return lastOpenedNote;
    }


    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment);
        if (!isLandscape) {
            transaction.addToBackStack("note");
        }
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
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
