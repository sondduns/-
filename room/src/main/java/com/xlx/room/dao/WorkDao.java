package com.xlx.room.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.xlx.room.entities.WorkTime;

import java.util.List;

@Dao
public interface WorkDao {

    @Query("select * from worktime where month=(:month)")
    List<WorkTime> getOfMonth(int month);

    @Query("select * from worktime where year=(:year) and month=(:month) and day=(:day)")
    WorkTime getToday(int year, int month, int day);

    @Insert
    void insert(WorkTime workTime);

    @Update
    void update(WorkTime workTime);
}
