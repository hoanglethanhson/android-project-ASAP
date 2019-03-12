package com.example.assignment.Entity;

public class LongTermNote {
    private int id;
    private String title;

    public LongTermNote() {
    }

    public LongTermNote(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LongTermNote(String title) {
        this.title = title;
    }


    @Override
    public String toString() {
        return  this.title;
    }
}
