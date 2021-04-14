package com.example.note.observe;

import com.example.note.beans.Note;

import java.util.ArrayList;
import java.util.List;

public class Publisher {
    private List<Observer> observers = new ArrayList<>();

    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    public void unsubscribe(Observer observer) {
        int ind = observers.indexOf(observer);
        if (ind != -1) {
            observers.remove(ind);
        }
    }

    public void notifySingle(Note note, boolean isNew) {
        for (Observer observer : observers) {
            observer.insertNote(note, isNew);
            unsubscribe(observer);
        }

    }
}
