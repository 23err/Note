package com.example.note.repo;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.note.beans.Note;
import com.example.note.repo.listeners.OnInitListener;
import com.example.note.repo.listeners.OnInsertListener;
import com.example.note.repo.listeners.OnRemoveListener;
import com.example.note.repo.listeners.OnUpdateListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;

public class LocalWithCloudRepository extends LocalNoteRepository {

    public static final String TAG = "FireStore_Repo";
    public static final String NOTES = "notes";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notesDB = db.collection(NOTES);

    private OnRemoveListener onRemoveListener;
    private OnUpdateListener onUpdateListener;
    private OnInsertListener onInsertListener;
    private OnInitListener onInitListener;

    public LocalWithCloudRepository() {
        read();
    }

    private void read() {
        notesDB.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "successful load data");
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            Note note = NoteConverter.convertToNote(id, document.getData());
                            super.insert(note);
                        }
                        updateSuperList();
                        onInit();
                        Log.d(TAG, "init finished");
                    } else {
                        Log.w(TAG, "load data failed");
                    }
                });
    }

    @Override
    public void insert(Note note) {
        super.insert(note);
        updateSuperList();
        onInsert();
        notesDB.add(NoteConverter.convertToMap(note))
                .addOnSuccessListener(documentReference -> {
                    note.setId(documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    int index = super.getIndex(note);
                    super.remove(note);
                    onRemove(index);
                });
    }

    @Override
    public void update(Note note) {
        super.update(note);
        int index = super.getIndex(note);
        onUpdate(index);
        notesDB.document(note.getId()).update(NoteConverter.convertToMap(note))
                .addOnSuccessListener(runnable -> Log.d(TAG, String.format("Update note with id %s successful", note.getId())))
                .addOnFailureListener(e -> Log.e(TAG, e.getMessage()));
    }


    @Override
    public void remove(int index) {
        Note note = super.get(index);
        super.remove(index);
        remove(note, index);
    }

    @Override
    public void remove(Note note) {
        int index = super.getIndex(note);
        super.remove(note);
        remove(note, index);
    }

    private void remove(Note note, int index) {
        updateSuperList();
        onRemove(index);
        String message = String.format("Remove item with id %s ", note.getId());
        notesDB.document(note.getId())
                .delete()
                .addOnSuccessListener(runnable -> Log.d(TAG, message + "successful"))
                .addOnFailureListener(e -> {
                    Log.e(TAG, message + "failed");
                    Log.e(TAG, e.getMessage());
                    super.insert(note);
                    onInsert();
                });
    }

    @Override
    public NoteRepository init() {
        onInit();
        return this;
    }

    @Override
    public NoteRepository setOnInitListener(OnInitListener listener) {
        onInitListener = listener;
        return this;
    }

    @Override
    public NoteRepository setOnRemoveListener(OnRemoveListener listener) {
        onRemoveListener = listener;
        return this;
    }

    @Override
    public NoteRepository setOnUpdateListener(OnUpdateListener listener) {
        onUpdateListener = listener;
        return this;
    }

    @Override
    public NoteRepository setOnInsertListener(OnInsertListener listener) {
        onInsertListener = listener;
        return this;
    }

    private void onInit() {
        if (onInitListener != null) {
            onInitListener.onInit();
        }
    }

    private void onInsert() {
        if (onInsertListener != null) {
            onInsertListener.onInsert(super.getSize() - 1);
        }
    }

    private void onUpdate(int index) {
        if (onUpdateListener != null) {
            onUpdateListener.onUpdate(index);
        }
    }

    private void onRemove(int index) {
        if (onRemoveListener != null) {
            onRemoveListener.onRemove(index);
        }
    }

    private void updateSuperList() {
        super.updateList();
    }

}
