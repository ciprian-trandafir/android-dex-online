package com.dexonline.classes;

public class Notification {
    private boolean active, definition;
    private String time;

    public Notification() {
        this.active = false;
        this.definition = true;
        this.time = "10:00";
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDefinition() {
        return definition;
    }

    public void setDefinition(boolean definition) {
        this.definition = definition;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
