package com.example.note.repo;

import com.example.note.beans.Note;
import com.example.note.repo.listeners.OnInitListener;
import com.example.note.repo.listeners.OnInsertListener;
import com.example.note.repo.listeners.OnRemoveListener;
import com.example.note.repo.listeners.OnUpdateListener;

import java.util.List;

public interface NoteRepository {

    NoteRepository search(String text);

    int getIndex(Note note);

    void insert(Note note);

    void update(Note note);

    Note get(int index);

    List<Note> getList();

    int getSize();

    void remove(int index);

    void remove(Note note);

    NoteRepository init();

    NoteRepository setOnInitListener(OnInitListener listener);

    NoteRepository setOnRemoveListener(OnRemoveListener listener);

    NoteRepository setOnUpdateListener(OnUpdateListener listener);

    NoteRepository setOnInsertListener(OnInsertListener listener);

}
