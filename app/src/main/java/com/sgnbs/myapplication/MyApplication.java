package com.sgnbs.myapplication;

import android.app.Application;

import androidx.room.Room;

import com.xlx.room.MyDataBase;

public class MyApplication extends Application {

    private static MyDataBase dataBase;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public MyDataBase getDataBase() {
        if (dataBase == null) {
            dataBase = Room.databaseBuilder(getApplicationContext(), MyDataBase.class, "work")
                    .build();
        }
        return dataBase;
    }
}
