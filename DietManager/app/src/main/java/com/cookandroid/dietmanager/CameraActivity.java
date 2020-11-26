package com.cookandroid.dietmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.Calendar;


public class CameraActivity extends AppCompatActivity {
    Button btnCamera2main, btnSelectPicture;
    TextView tvSaveTime, tvFoodName;
    ImageView ivFood;
    RadioButton rbFromGallery, rbFromCamera;
    String saveTime;
    View dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btnCamera2main = (Button) findViewById(R.id.btnCamera2main);
        btnSelectPicture = (Button) findViewById(R.id.btnSelectPicture);
        rbFromGallery = (RadioButton) findViewById(R.id.rbFromGallery);
        rbFromCamera = (RadioButton) findViewById(R.id.rbFromCamera);
        tvSaveTime = (TextView) findViewById(R.id.tvSaveTime);
        tvFoodName = (TextView) findViewById(R.id.tvFoodName);
        ivFood = (ImageView) findViewById(R.id.ivFood);

        Calendar cal = Calendar.getInstance();
        int Year = cal.get(Calendar.YEAR);
        int Month = cal.get(Calendar.MONTH) + 1;
        int Day = cal.get(Calendar.DAY_OF_MONTH);
        saveTime = Year + "년 " + Month + "월 " + Day + "일 ";
        tvSaveTime.setText(saveTime);

        btnCamera2main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSelectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogView = (View) View.inflate(CameraActivity.this,
                        R.layout.dialog1, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(CameraActivity.this);
                dlg.setView(dialogView);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(rbFromGallery.isChecked()){
//                            Intent intent = new Intent();
//                            intent.setType("image/*");
//                            intent.setAction(Intent.ACTION_GET_CONTENT);
//                            startActivityForResult(intent, 0);
                        } else{
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivity(intent);
                        }
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 0) {
//            if (resultCode == RESULT_OK) {
//                try {
//                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
//                    Bitmap bmp = BitmapFactory.decodeStream(inputStream);
//                    inputStream.close();
//                    ivFood.setImageBitmap(bmp);
//                } catch (Exception e) {
//                }
//            } else if (resultCode == 1) {
//                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
}
