package com.dexonline.classes;

public class Notification {
    private boolean active, definition, reason;
    private String time;

    public Notification() {
        this.active = false;
        this.definition = true;
        this.reason = true;
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

    public boolean isReason() {
        return reason;
    }

    public void setReason(boolean reason) {
        this.reason = reason;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
