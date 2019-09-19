package com.taskapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class FormActivity extends AppCompatActivity {

    private EditText editTitle;
    private EditText editDesc;
    private Task task;
    String title;
    String desc;
    Button button_save;
    boolean onList = false;
    public static String RESULT_KEY = "task";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        editTitle = findViewById(R.id.editTitle);
        editDesc = findViewById(R.id.editDesc);
        button_save = findViewById(R.id.button_save);
        task = (Task) intent.getSerializableExtra(RESULT_KEY);
        if (task != null) {
            editTitle.setText(task.getTitle());
            editDesc.setText(task.getDesc());
            onList = true;
            button_save.setText("Редактировать");
        }
    }
    public void onSave(View view) {
        title = editTitle.getText().toString().trim();
        desc = editDesc.getText().toString().trim();
        if (task == null) {
            task = new Task();
        }
        task.setTitle(title);
        task.setDesc(desc);
        if (onList) {
            App.getDataBase().taskDao().update(task);
           // task = new Task(title, desc);
        } else {
            if (title.matches("")){
                Toast.makeText(this, "Введите данные", Toast.LENGTH_SHORT).show();
                YoYo.with(Techniques.Tada)
                        .duration(500)
                        .repeat(3)
                        .playOn(findViewById(R.id.editTitle));
                YoYo.with(Techniques.Tada)
                        .duration(500)
                        .repeat(3)
                        .playOn(findViewById(R.id.editDesc));
                return;
            }else {
                App.getDataBase().taskDao().insert(task);
            }
        }

//        if (title.matches("")){
//            Toast.makeText(this, "Введите данные", Toast.LENGTH_SHORT).show();
//            return;
//        }
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        preferences.edit().clear().commit();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    //    @Override
//    protected void onPause() {
//        super.onPause();
//        SharedPreferences.Editor settings = getSharedPreferences("preferences", MODE_PRIVATE).edit();
//        settings.putString("title", editTitle.getText().toString());
//        settings.putString("desc", editDesc.getText().toString());
//        settings.apply();
//    }
//    @Override
//    protected void onResume() {
//        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
//        editTitle.setText(sharedPreferences.getString("title", ""));
//        String desc = sharedPreferences.getString("desc", "");
//        editDesc.setText(desc);
//        super.onResume();
//    }
}

