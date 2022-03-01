package com.dexonline.classes;

public class Definition {
    private String id, htmlRep, userNick, sourceName, createDate, modDate;
    private boolean bookmarked;

    public Definition(String id, String htmlRep, String userNick, String sourceName, String createDate, String modDate, boolean bookmarked) {
        this.id = id;
        this.htmlRep = htmlRep;
        this.userNick = userNick;
        this.sourceName = sourceName;
        this.createDate = createDate;
        this.modDate = modDate;
        this.bookmarked = bookmarked;
    }

    public String getId() {
        return id;
    }

    public String getHtmlRep() {
        return htmlRep;
    }

    public String getUserNick() {
        return userNick;
    }

    public String getSourceName() {
        return sourceName;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }
}
