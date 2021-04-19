package com.example.note.repo;

import com.example.note.beans.Note;

import java.util.List;

public interface NoteRepository {

    void init(OnInitListener listener);

    List<Note> find(String text);

    int getIndex(Note note);

    void insert(Note note);

    void update(Note note);

    Note get(int index);

    List<Note> getList();

    int getSize();

    void remove(int index);

    void remove(Note note);
}
