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
        insert(new Note("first note", "aklsjhfkkhsdf askdjfhka"));
        insert(new Note("second note", "aklsjhfkkhsdf askdjfhka"));
        insert(new Note("", "third note in body"));
        insert(new Note("ajsd", "aklsjhfkkhsdf askdjfhka"));
//        for (int i = 0; i < 30; i++) {
//            insertNote(new Note("note test", "hello"));
//
//        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Note> find(String text) {
        final String finalText = text.toLowerCase().trim();
        if (finalText.length()==0) {
            return noteList;
        }
        List<Note> list = noteList.stream()
                .filter(note -> {
                    return (note.getName().toLowerCase().contains(finalText) || note.getBody().toLowerCase().contains(finalText));
                })
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public void update(Note note) {
        int ind = noteList.indexOf(note);
        if (ind!=-1) {
            noteList.set(ind, note);
        }
    }

    @Override
    public int getIndex(Note note) {
        return noteList.indexOf(note);
    }

    @Override
    public void insert(Note note) {
        if (!note.isEmpty())
            noteList.add(note);
    }



    @Override
    public Note get(int index) {
        return noteList.get(index);
    }

    @Override
    public List<Note> getList() {
        return noteList;
    }

    @Override
    public int getSize() {
        return noteList.size();
    }

    @Override
    public void remove(int index) {
        noteList.remove(index);
    }

    @Override
    public void remove(Note note) {
        int index = noteList.indexOf(note);
        if (index >= 0) {
            noteList.remove(index);
        }
    }
}
