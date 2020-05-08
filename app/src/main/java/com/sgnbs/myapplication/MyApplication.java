package com.sgnbs.myapplication;

import android.app.Application;

import androidx.room.Room;

import com.xlx.room.MyDataBase;

public class MyApplication extends Application {

    private static MyDataBase dataBase;

    @Override
    public void onCreate() {
        super.onCreate();
        dataBase = Room.databaseBuilder(getApplicationContext(), MyDataBase.class, "work")
                    .build();
    }

    public static MyDataBase getDataBase() {
        return dataBase;
    }
}
