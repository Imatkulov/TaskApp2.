package com.taskapp2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private final int REQUEST_GALLERY = 100;
    private Uri uri;
    EditText editText;
    EditText editPhone;
    ProgressBar progressBar;
    Button btnSave;
    String APP_PREFERENCES = "settings";
    SharedPreferences settings;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if (getSupportActionBar()!=null)getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        editText = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        btnSave = findViewById(R.id.btnSave);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        imageView = findViewById(R.id.imageView);
        getData();
    }

    private void getData() {
        String avatar = getSharedPreferences("preferences", MODE_PRIVATE).getString("avatar","");
        if (avatar != "")
        {
            showAvatar(avatar);
        }
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            String name = task.getResult().getString("name");
                            editText.setText(name);
//                            String phone = task.getResult().getString("phone");
//                            editText.setText(phone);
                            String avatar = task.getResult().getString("avatar");
                            showAvatar(avatar);
                        }
                    }
                });
    }

    private void showAvatar(String avatar) {
        Glide.with(this).load(avatar).apply(RequestOptions.circleCropTransform()).into(imageView);

    }

    public void onSave(View view) {
        uploadImage();
    }
    private void saveUser(Uri downloadUri){
        progressBar.setVisibility(View.VISIBLE);
        String name = editText.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        SharedPreferences.Editor settings = getSharedPreferences("preferences", MODE_PRIVATE).edit();
        settings.putString("name", editText.getText().toString());
        settings.putString("avatar",downloadUri.toString());
        settings.putString("phone", editPhone.getText().toString());
        settings.apply();
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("phone", phone);
        map.put("email", "rustam180492@gmail.com");
        map.put("avatar", downloadUri.toString());
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toaster.show("Успешно");
                        } else {
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

    public void onClickImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_GALLERY) {
            uri = data.getData();
            imageView.setImageURI(uri);


        }
    }

    private void uploadImage() {
        String name = "avatars/"+SystemClock.currentThreadTimeMillis()+".jpg";
        final StorageReference ref = FirebaseStorage.getInstance().getReference().child(name);
        Task<Uri> task = ref.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (task.isSuccessful()){
                    return ref.getDownloadUrl();
                }
                return null;
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.e("TAG", "uri = " + downloadUri );
                    saveUser(downloadUri);
                } else {
                    Toaster.show("Ошибка");
                }
            }
        });
     }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
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
