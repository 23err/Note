package com.example.note.repo;

import com.example.note.beans.Note;

import java.util.ArrayList;
import java.util.List;

public class LocalNoteRepository implements NoteRepository {
    List<Note> noteList;

    public LocalNoteRepository() {
        noteList = new ArrayList<>();
    }


    @Override
    public int getIndex(Note note) {
        return noteList.indexOf(note);
    }

    @Override
    public void insertNote(Note note) {
        noteList.add(note);
    }

    @Override
    public Note getNote(int index) {
        return noteList.get(index);
    }

    @Override
    public List<Note> getNoteList() {
        return noteList;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void removeNote(int index) {
        noteList.remove(index);
    }

    @Override
    public void removeNote(Note note) {
        int index = noteList.indexOf(note);
        if (index >= 0) {
            noteList.remove(index);
        }
    }
}
