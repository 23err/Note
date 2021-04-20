package com.example.note.repo;

import com.example.note.beans.Note;
import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NoteConverter {

    public static final String NAME = "name";
    public static final String BODY = "body";
    public static final String DATE = "date";

    public static Map<String, Object> convertToMap(Note note) {
        Map<String, Object> mapNote = new HashMap<>();
        mapNote.put(NAME, note.getName());
        mapNote.put(BODY, note.getBody());
        mapNote.put(DATE, new Timestamp(note.getDate()));
        return mapNote;
    }

    public static Note convertToNote(String id, Map<String, Object> mapNote){
        String name = (String) mapNote.get(NAME);
        String body = (String) mapNote.get(BODY);
        Date date = ((Timestamp) mapNote.get(DATE)).toDate();
        return new Note(id, name, body, date);
    }
}
