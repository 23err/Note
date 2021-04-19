package com.example.note.repo;

import android.util.Log;

import com.example.note.beans.Note;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;

public class LocalWithCloudRepository extends LocalNoteRepository {

    public static final String TAG = "FireStore_Repo";
    public static final String NAME = "name";
    public static final String BODY = "body";
    public static final String DATE = "date";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private OnInitListener initListener;

    public LocalWithCloudRepository(){
        initialize();
    }

    private void initialize(){
        read();
    }

    private void read(){
        db.collection("notes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "successfull load data");
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String name = document.getString(NAME);
                            String body = document.getString(BODY);
                            Date date = document.getDate(DATE);
                            super.insert(new Note(id, name, body, date));
                        }
                        initListener.onInit();
                        Log.d(TAG, "init finished");

                    } else {
                        Log.w("loading", "load data failed");
                    }
                });
    }

    @Override
    public void init(OnInitListener listener) {
        this.initListener = listener;
    }
}
