package com.example.note.observe;

import com.example.note.beans.Note;

public interface Observer {
    void insertNote(Note note, boolean isNew);
}
