package com.cookandroid.dietmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Button btnDietActivity, btnCameraActivity;
    ImageButton btnEdit;
    View dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("YourDiet");

        btnDietActivity = (Button) findViewById(R.id.btnDietActivity);
        btnCameraActivity = (Button) findViewById(R.id.btnCameraActivity);
        btnEdit = (ImageButton) findViewById(R.id.btnEdit);

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

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str;
                String strs[] = {"23", "60"};

                // 사용자 정보 불러오기(내부 저장소 접근)
                try {
                    FileInputStream inFs = openFileInput("user_info.txt");
                    byte[] txt = new byte[20];
                    inFs.read(txt);
                    str = new String(txt);

                    // user_info.txt가 없거나 공백이면, 나이 23 몸무게 60으로 설정
                    if(!str.contains(" ")) {
                        strs[0] = "23";
                        strs[1] = "60";
                    } else{
                        strs = str.split(" ");
                    }
                    inFs.close();
                } catch (IOException e){
                    Toast.makeText(getApplicationContext(),"파일 없음",
                            Toast.LENGTH_LONG).show();
                }
                NPDialog npDialog = new NPDialog();
                npDialog.strs = strs;
                npDialog.show(getSupportFragmentManager(),"사용자 정보 수정");
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
            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "sample.jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.
                    DIRECTORY_PICTURES);

            String[] what = new String[]{ MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.MIME_TYPE,
                    MediaStore.Images.ImageColumns.DATA };
            Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            // 샘플 이미지 존재하는지 확인
            Cursor cursor = resolver.query(queryUri, what, null,null,
                    MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
            if(cursor.getCount() > 0){
                Toast.makeText(this,"샘플 이미지가 이미 있습니다." +
                        "\n/Pictures/sample.jpg",Toast.LENGTH_SHORT).show();
            } else{
                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues);
                fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();

                Toast.makeText(this, "샘플 이미지를 불러왔습니다." +
                        "\n/Pictures/sample.jpg", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this,"Fail to load sample.jpg",Toast.LENGTH_LONG).show();
        }
    }
}