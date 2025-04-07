package com.health.personaltracker.model;

public class HistoryItem {
    private final String label;
    private final long id;

    public HistoryItem(long id, String label) {
        this.label = label;
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public long getId() {
        return id;
    }

}
