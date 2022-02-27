package com.dexonline.classes;

import java.util.Calendar;

public class Error {
    private String type, text, date, function;

    public Error(String type, String text, String function) {
        this.type = type;
        this.text = text;
        this.function = function;
        this.date = String.valueOf(Calendar.getInstance().getTime());
    }
}
