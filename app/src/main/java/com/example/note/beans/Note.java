package com.example.note.beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {
    private String name, body;
    private Date date;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

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


    public Note(String name, String body, Date date) {
        this.name = name.trim();
        this.body = body.trim();
        this.date = date;
    }

    public Note(String name, String body) {
        this(name, body, new Date());
    }


    @Override
    public String toString(){
        String result;
        if (name.trim().length()>0){
            result = name;
        } else{
            result = body.length() > 50 ? body.substring(0,50) : body;
        }
        return result;
    }


}
