package com.example.note.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note.MainActivity;
import com.example.note.Navigation;
import com.example.note.R;
import com.example.note.beans.Note;
import com.example.note.observe.Publisher;
import com.example.note.repo.NoteRepository;
import com.example.note.repo.NoteRepositoryFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListNotesFragment extends Fragment {
    public static final String VIEWTYPE = "viewtype";
    public static final String IS_CARD_VIEW_IN_RV = "isCardViewInRV";
    private FloatingActionButton btnAdd;
    private RecyclerView rvNotes;
    private NoteRepository repo;
    private NotesListRVAdapter adapter;
    private List<Note> noteList;
    private SearchView searchView;
    private static int lastOpenedNote = -1;
    private boolean isLandscape;
    private boolean isCardViewRV;
    private Publisher publisher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isLandscape = getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        setHasOptionsMenu(true);
        publisher = ((MainActivity) requireActivity()).getPublisher();
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
        readViewTypeSP();
        setAdapter();
        btnSetOnClick();
    }

    private void setAdapter() {
        repo = NoteRepositoryFactory.getInstance();
        noteList = repo.getList();
        adapter = new NotesListRVAdapter(getContext(), noteList, this);
        adapter.setCardView(isCardViewRV);
        rvNotes.setAdapter(adapter);
        adapterSetOnItemClick();
        repoInitListeners();
    }

    private void repoInitListeners() {
        repo
                .setOnInitListener(adapter::notifyDataSetChanged)
                .setOnInsertListener(adapter::notifyItemInserted)
                .setOnUpdateListener(adapter::notifyItemChanged)
                .setOnRemoveListener(index -> {
                    adapter.notifyItemRemoved(index);
                    if (index == lastOpenedNote) {
                        lastOpenedNote = -1;
                    }
                })
                .init();
    }

    private void adapterSetOnItemClick() {
        adapter.setOnItemClickListener((view1, note) -> {
            NoteFragment fragment = NoteFragment.getInstance(note);
            showFragment(fragment);
            subscribeToFragment();
        });
    }

    private void setSearchViewTextListener(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                repo.search(newText);
                return true;
            }
        });
    }


    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.context_menu_recycler_view, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int resId = item.getItemId();
        int position = adapter.getPosition();
        switch (resId) {
            case R.id.itemRemove:
                repo.remove(position);
                return true;
        }
        return super.onContextItemSelected(item);
    }


    private void subscribeToFragment() {
        publisher.subscribe((note, isNew) -> {
            if (isNew) {
                repo.insert(note);
                lastOpenedNote = repo.getSize() - 1;
            } else {
                repo.update(note);
                lastOpenedNote = repo.getIndex(note);
            }
        });

    }

    public static int getLastOpenedNote() {
        return lastOpenedNote;
    }


    private void showFragment(Fragment fragment) {
        new Navigation(requireActivity()).setCustomAnimation(true).showFragment(fragment, !isLandscape);
    }


    private void btnSetOnClick() {
        btnAdd.setOnClickListener(view1 -> {
            NoteFragment fragment = NoteFragment.getInstance();
            subscribeToFragment();
            showFragment(fragment);
        });
    }

    private void findViews(@NonNull View view) {
        btnAdd = view.findViewById(R.id.btnAdd);
        rvNotes = view.findViewById(R.id.rvNotes);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.note_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) menuItem.getActionView();
        setSearchViewTextListener(searchView);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int resId = item.getItemId();
        if (resId == R.id.view) {
            isCardViewRV = !isCardViewRV;
            saveViewType();
            adapter.setCardView(isCardViewRV);
            rvNotes.setAdapter(adapter);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveViewType() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(VIEWTYPE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(IS_CARD_VIEW_IN_RV, isCardViewRV);
        edit.apply();
    }

    private void readViewTypeSP() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(VIEWTYPE, Context.MODE_PRIVATE);
        isCardViewRV = sharedPreferences.getBoolean(IS_CARD_VIEW_IN_RV, false);
    }
}
