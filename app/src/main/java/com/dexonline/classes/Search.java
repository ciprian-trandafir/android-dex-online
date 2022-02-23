package com.dexonline.classes;

public class Search {
    private String text, date;

    public Search() {
        this.text = "";
        this.date = "";
    }

    public Search(String text, String date) {
        this.text = text;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }
}
