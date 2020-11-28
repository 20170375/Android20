package com.cookandroid.dietmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Button btnDietActivity, btnCameraActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("참신한 어플 이름");

        btnDietActivity = (Button) findViewById(R.id.btnDietActivity);
        btnCameraActivity = (Button) findViewById(R.id.btnCameraActivity);

        btnDietActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DietActivity.class);
                startActivity(intent);
            }
        });

        btnCameraActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);
            }
        });

        // AVD에 이미지 샘플 넣어주기
        sampleLoad();
    }

    // AVD에 이미지 샘플 넣어주기
    private void sampleLoad(){
        Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.food);

        OutputStream fos;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "sample.jpg");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.
                        DIRECTORY_PICTURES);
                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues);
                fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
            } else {
                String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.
                        DIRECTORY_PICTURES).toString();
                File image = new File(imagesDir, "sample.jpg");
                fos = new FileOutputStream(image);
            }
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Objects.requireNonNull(fos).close();
            Toast.makeText(this,"샘플 이미지를 불러왔습니다." +
                    "\n/Pictures/sample.jpg",Toast.LENGTH_LONG).show();
        } catch (Exception e){
            Toast.makeText(this,"Fail to load sample.jpg",Toast.LENGTH_LONG).show();
        }
    }
}