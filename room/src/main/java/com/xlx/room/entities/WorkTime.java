package com.xlx.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WorkTime {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="on_time")
    public String onTime;

    @ColumnInfo(name="off_time")
    public String offTime;

    @ColumnInfo(name="work_hour")
    public float workHour;

    @ColumnInfo(name="diff_hour")
    public float diffHour;

    @ColumnInfo(name="day")
    public int day;

    @ColumnInfo(name="month")
    public int month;

    @ColumnInfo(name="year")
    public int year;


}
