package com.xlx.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.xlx.room.dao.WorkDao;
import com.xlx.room.entities.WorkTime;

@Database(entities = {WorkTime.class}, version = 1)
public abstract class MyDataBase extends RoomDatabase {

    public abstract WorkDao workDao();
}
