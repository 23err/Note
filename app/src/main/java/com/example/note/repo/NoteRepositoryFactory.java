package com.example.note.repo;

public class NoteRepositoryFactory {

    private static NoteRepository repo = null;

    public static NoteRepository getInstance() {
        if (repo == null) {
            repo = new LocalNoteRepository();
        }
        return repo;
    }
}