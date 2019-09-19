package com.taskapp2;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.taskapp2.room.AppDataBase;

public class App extends Application {

    private static AppDataBase database;
    volatile  static Context context;



    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        database = Room.databaseBuilder(this, AppDataBase.class, "database")
                .allowMainThreadQueries()
                .build();
    }

    public static AppDataBase getDataBase() {
        return database;
    }
}
