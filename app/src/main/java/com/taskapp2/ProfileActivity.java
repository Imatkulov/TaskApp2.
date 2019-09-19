package com.taskapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    EditText editText;
    EditText editPhone;
    ProgressBar progressBar;
    Button btnSave;
    String APP_PREFERENCES = "settings";
    SharedPreferences settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        settings =getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        editText = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        btnSave = findViewById(R.id.btnSave);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }
    private void getData(){

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() !=null){
                            String name = task.getResult().getString("name");
                            String phone = task.getResult().getString("phone");
                            editText.setText(name);
                            editText.setText(phone);
                        }
                    }
                });
    }

    public void onSave(View view) {
        progressBar.setVisibility(View.VISIBLE);
        String name = editText.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
//        editText.setText(name);
//        editPhone.setText(phone);
        SharedPreferences.Editor settings = getSharedPreferences("preferences", MODE_PRIVATE).edit();
        settings.putString("name", editText.getText().toString());
        settings.putString("phone", editPhone.getText().toString());
        settings.apply();
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("phone", phone);
        map.put("email", "rustam180492@gmail.com");
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressBar.setVisibility(View.INVISIBLE);
                            Toaster.show("Успешно");
                        }else{
                            Toaster.show("Ошибка");
                        }

                    }
                });

            }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        editText.setText(sharedPreferences.getString("name", ""));
        String desc = sharedPreferences.getString("phone", "");
        editPhone.setText(desc);

    }
//        SharedPreferences sharedPreferences = getSharedPreferences("preferences",   MODE_PRIVATE);
//        editText.setText(sharedPreferences.getString("name", ""));
//        String phone = sharedPreferences.getString("phone","");
//        editPhone.setText(phone);
//
//        if (settings.contains(APP_PREFERENCES)){
//            editText.setText("name");
//            editPhone.setText("phone");




//    @Override
//    protected void onPause() {
//        super.onPause();
////        SharedPreferences.Editor settings = (SharedPreferences.Editor) getSharedPreferences("preferences", MODE_PRIVATE);
////        settings.putString("name",editText.getText().toString());
////        settings.putString("name",editPhone.getText().toString());
//
//        SharedPreferences.Editor editor =settings.edit();
//        editor.putInt(APP_PREFERENCES,0);
//        editor.apply();
//    }
}
