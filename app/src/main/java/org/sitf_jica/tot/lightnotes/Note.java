package org.sitf_jica.tot.lightnotes;

import java.util.Comparator;

/**
 * Created by Asus on 11/3/2017.
 */

public class Note{
    public int id;
    public String title;
    public String note;
    public String time;
    public int color;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Note(){}

    public Note(int id, String title, String note, String time, int color) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.time = time;
        this.color = color;
    }

    public Note(String title, String note, String time, int color) {
        this.title = title;
        this.note = note;
        this.time = time;
        this.color = color;
    }

}
