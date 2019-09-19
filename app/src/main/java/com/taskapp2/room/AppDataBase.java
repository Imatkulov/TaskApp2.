package com.taskapp2.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.taskapp2.Task;

@Database(entities = {Task.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public abstract TaskDao taskDao();
}
