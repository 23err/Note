package com.example.note.repo;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.note.beans.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LocalNoteRepository implements NoteRepository {
    List<Note> noteList;

    public LocalNoteRepository() {
        noteList = new ArrayList<>();
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
