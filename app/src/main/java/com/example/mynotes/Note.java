package com.example.mynotes;

public class Note {

    private String title;
    private String note;
    private String date;
    private String password;

    public Note(String title, String note, String date, String password) {
        this.title = title;
        this.note = note;
        this.date = date;
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
