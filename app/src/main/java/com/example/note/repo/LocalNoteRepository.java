package com.example.note.repo;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.note.beans.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public abstract class LocalNoteRepository implements NoteRepository {
    private List<Note> resultNotes = new ArrayList<>();
    private List<Note> allNotes = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Note> find(String text) {
        final String finalText = text.toLowerCase().trim();
        if (finalText.length() == 0) {
            resultNotesInit(allNotes);
            return resultNotes;
        }

        List<Note> list = allNotes.stream()
                .filter(note -> {
                    return (note.getName().toLowerCase().contains(finalText) || note.getBody().toLowerCase().contains(finalText));
                })
                .collect(Collectors.toList());
        resultNotesInit(list);
        return resultNotes;
    }

    public void updateList() {
        resultNotesInit(allNotes);
    }

    @Override
    public void update(Note note) {
        int ind = allNotes.indexOf(note);
        if (ind != -1) {
            allNotes.set(ind, note);
        }
    }

    @Override
    public int getIndex(Note note) {
        return allNotes.indexOf(note);
    }

    @Override
    public void insert(Note note) {
        if (!note.isEmpty())
            allNotes.add(note);
    }

    @Override
    public Note get(int index) {
        return allNotes.get(index);
    }

    @Override
    public List<Note> getList() {
        return resultNotes;
    }

    @Override
    public int getSize() {
        return allNotes.size();
    }

    @Override
    public void remove(int index) {
        allNotes.remove(index);
    }

    @Override
    public void remove(Note note) {
        int index = allNotes.indexOf(note);
        if (index >= 0) {
            allNotes.remove(index);
        }
    }

    private void resultNotesInit(List<Note> list) {
        resultNotes.clear();
        resultNotes.addAll(list);
    }
}
