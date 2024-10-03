package com.health.personaltracker.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Fasting {

    @PrimaryKey
    public Long uid;

    @ColumnInfo(name = "start_datetime")
    public String start_datetime;

    @ColumnInfo(name = "end_datetime")
    public String end_datetime;

    @ColumnInfo(name = "active")
    public boolean active;

    @ColumnInfo(name = "hours")
    public int hours;

    public Long getUid() {
        return uid;
    }
}
