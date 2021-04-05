package com.example.note.repo;

import com.example.note.beans.Note;

import java.util.List;

public interface NoteRepository {

    List<Note> findNotes(String text);

    void updateNote(Note note);

    int getIndex(Note note);

    void insertNote(Note note);

    void insertOrUpdateNote(Note note);

    Note getNote(int index);

    List<Note> getNoteList();

    int getSize();

    void removeNote(int index);

    void removeNote(Note note);
}
