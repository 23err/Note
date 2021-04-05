package com.example.note.repo;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.note.beans.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class LocalNoteRepository implements NoteRepository {
    private List<Note> noteList;

    public LocalNoteRepository() {
        noteList = new ArrayList<>();
        fillNoteList();
    }

    private void fillNoteList() {
        this.insertNote(new Note("first note", "aklsjhfkkhsdf askdjfhka", new Date()));
        this.insertNote(new Note("second note", "aklsjhfkkhsdf askdjfhka", new Date()));
        this.insertNote(new Note("", "third note in body", new Date()));
        this.insertNote(new Note("ajsd", "aklsjhfkkhsdf askdjfhka", new Date()));
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Note> findNotes(String text) {
        final String finalText = text.toLowerCase().trim();
        List<Note> list = noteList.stream()
                .filter(note -> {
                    return (note.getName().toLowerCase().contains(finalText) || note.getBody().toLowerCase().contains(finalText));
                })
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public void updateNote(Note note) {
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
    public void insertOrUpdateNote(Note note) {
        int noteIndex = getIndex(note);
        if (noteIndex >= 0) {
            if (note.isEmpty()) {
                removeNote(noteIndex);
            } else {
                updateNote(note);
            }
        } else {
            if (!note.isEmpty()) {
                insertNote(note);
            }
        }
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
        return noteList.size();
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
