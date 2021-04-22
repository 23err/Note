package com.example.note.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Note implements Parcelable {

    private String id, name, body;
    private Date date;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    protected Note(Parcel in) {
        id = in.readString();
        name = in.readString();
        body = in.readString();
        date = new Date(in.readLong());
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public String getBody() {
        return body.trim();
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public String getFormatDate() {
        return dateFormat.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public Note(String id, String name, String body, Date date) {
        this(name, body, date);
        this.id = id;
    }

    public Note(String name, String body, Date date) {
        this.name = name.trim();
        this.body = body.trim();
        this.date = date;
    }

    public Note(String name, String body) {
        this(name, body, new Date());
    }

    public boolean isEmpty() {
        return stringLength(name) == 0 && stringLength(body) == 0;
    }

    private int stringLength(String text) {
        return text.trim().length();
    }

    public Note(){
        this("", "");
    }


    @Override
    public String toString(){
        String result = body.replaceAll("\\n", " ");
        if (stringLength(name)>0){
            result = name;
        } else{
            result = stringLength(result) > 50 ? result.substring(0,50) : result;
        }
        return result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(body);
        parcel.writeLong(date.getTime());
    }

}
