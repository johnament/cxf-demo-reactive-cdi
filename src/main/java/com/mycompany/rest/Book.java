package com.mycompany.rest;

public class Book {
    private final String s;
    private final int eventId;

    public Book(String s, int eventId) {

        this.s = s;
        this.eventId = eventId;
    }

    public String getS() {
        return s;
    }

    public int getEventId() {
        return eventId;
    }
}
