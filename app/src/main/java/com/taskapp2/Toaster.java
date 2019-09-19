package com.taskapp2;

import android.widget.Toast;

public class Toaster {
    public static void show(String message){
        Toast.makeText(App.context,message,Toast.LENGTH_SHORT).show();
    }
}
