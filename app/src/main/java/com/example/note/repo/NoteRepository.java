package com.example.note.repo;

import com.example.note.beans.Note;

import java.util.List;

public interface NoteRepository {

    int getIndex(Note note);

    void insertNote(Note note);

    Note getNote(int index);

    List<Note> getNoteList();

    int getSize();

    void removeNote(int index);

    void removeNote(Note note);
}
