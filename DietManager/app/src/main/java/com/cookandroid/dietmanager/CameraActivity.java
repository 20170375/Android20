package com.cookandroid.dietmanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;


public class CameraActivity extends AppCompatActivity {
    ImageButton btnCamera2main, btnSelectPicture, btnInstagram;
    TextView tvSaveTime, tvFoodName;
    ImageView ivFood;
    LinearLayout dataLayout, kcalLayout;
    FrameView dataFrameView, kcalFrameView;
    String saveTime, strFoodName="No data", strFoodData="No Data", strTotal="No Data";
    float kcal=935f, tans=56.21f, danb=34.48f, jiba=27.91f, natt=1250.30f, cKcal=515f, tKcal=2105f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btnCamera2main = (ImageButton) findViewById(R.id.btnCamera2main);
        btnSelectPicture = (ImageButton) findViewById(R.id.btnSelectPicture);
        btnInstagram = (ImageButton) findViewById(R.id.btnInstagram);
        tvSaveTime = (TextView) findViewById(R.id.tvSaveTime);
        tvFoodName = (TextView) findViewById(R.id.tvFoodName);
        ivFood = (ImageView) findViewById(R.id.ivFood);
        dataLayout = (LinearLayout) findViewById(R.id.dataLayout);
        kcalLayout = (LinearLayout) findViewById(R.id.kcalLayout);

        // 오늘 칼로리 섭취량 계산
        calKcal();

        // 날짜받아서 화면에 띄우기
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        saveTime = year + "년 " + month + "월 " + day + "일 ";
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if(hour>=5 && hour<12){
            saveTime = saveTime + "아침식사";
        } else if(hour>=12 && hour<18){
            saveTime = saveTime + "점심식사";
        } else{
            saveTime = saveTime + "저녁식사";
        }
        tvSaveTime.setText(saveTime);

        // 뒤로가기 버튼 활성화
        btnCamera2main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 오늘 칼로리 섭취량 화면에 나타내기
        kcalFrameView = (FrameView) new FrameView(this,100,strTotal);
        kcalLayout.addView(kcalFrameView);

        // 카메라 버튼 활성화 (갤러리, 카메라 선택)
        btnSelectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.openGallery){
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, 0);
                        } else{
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivity(intent);
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    // 갤러리에 접근하여 사진 가져오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    ivFood.setMaxHeight(700);
                    ivFood.setImageBitmap(bmp);

                    // 가져온 Bitmap 파일로 음식 분석 알고리즘 실행
                    analyzeFood(bmp);

                    // 가져온 음식 이름 화면에 나타내기
                    tvFoodName.setVisibility(View.VISIBLE);

                    // 가져온 음식 정보 화면에 나타내기
                    loadFoodInfo();
                    tvFoodName.setText(strFoodName);
                    dataFrameView = (FrameView) new FrameView(this,360,strFoodData);
                    dataLayout.addView(dataFrameView);

                    // DB 최신화
                    dbUpdate();

                    // 오늘 칼로리 섭취량 update
                    kcalLayout.removeAllViews();
                    calKcal();
                    kcalFrameView = (FrameView) new FrameView(this,100,strTotal);
                    kcalLayout.addView(kcalFrameView);

                    // Instagram 버튼 활성화
                    btnSelectPicture.setVisibility(View.INVISIBLE);
                    btnSelectPicture.setClickable(false);
                    btnInstagram.setVisibility(View.VISIBLE);
                    btnInstagram.setClickable(true);

                } catch (Exception e) {
                }
            } else if (resultCode == 1) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    // 오늘 섭취한 칼로리 계산
    private void calKcal(){
        // DB load
        try {
            FileInputStream inFs = openFileInput("db.txt");
            byte[] txt = new byte[1000];
            int txtNo;
            String str = "";
            while((txtNo=inFs.read()) != -1){
                str += (char)txtNo;
            }
            inFs.close();

            // db.txt가 없거나 공백이면, 0으로 설정
            if(!str.contains(" ")) {
                cKcal = 0;
            } else{
                String[] line = str.split("\n");

                // make String today
                String today = "";
                Calendar cal = Calendar.getInstance();
                today += cal.get(Calendar.YEAR) + ":";
                today += cal.get(Calendar.MONTH) + 1 + ":";
                today += cal.get(Calendar.DAY_OF_MONTH) + ":";
                today += cal.get(Calendar.HOUR_OF_DAY) + ":";
                today += cal.get(Calendar.MINUTE);

                // find today data
                int i=0;
                while(i<line.length){
                    if(line[i].contains(today))
                        break;
                    i++;
                }

                cKcal = 0;
                while(i<line.length) {
                    String[] word = line[i].split(" ");
                    cKcal += Float.parseFloat(word[2]);
                    i++;
                }
            }
        } catch (IOException e){
            Toast.makeText(getApplicationContext(),"파일 없음",
                    Toast.LENGTH_LONG).show();
        }

        // 불러온 정보로 String 만들기
        strTotal = "오늘 총 섭취량" + "               " + (int)cKcal +
                " / " + (int)tKcal + "  kcal";
    }

    // 음식 영양소 정보 불러오는 메소드
    protected void loadFoodInfo(){

        ///////////////////////
        //  음식 영양소 정보  //
        //  load 및 대입연산  //
        ///////////////////////

        strFoodName = "스파게티";
        strFoodData = "칼로리"     + "               " + kcal + "  kcal"  + "\n" +
                      "탄수화물"   + "           " +  tans + "  g"   + "\n" +
                      "단백질"   + "               " +  danb + "  g"   + "\n" +
                      "지방"   + "                   " +  jiba + "  g"   + "\n" +
                      "나트륨"   + "               " +  natt + "  mg";
    }

    // 사진으로 음식 분석 알고리즘
    protected void analyzeFood(Bitmap bmp){

        //////////////////////
        //    Processing    //
        //////////////////////

    }

    // DB 최신화 메소드
    protected void dbUpdate(){
        String str = "";

        // "YY:MM:DD:HH:MM"
        Calendar cal = Calendar.getInstance();
        str += cal.get(Calendar.YEAR) + ":";
        str += cal.get(Calendar.MONTH) + 1 + ":";
        str += cal.get(Calendar.DAY_OF_MONTH) + ":";
        str += cal.get(Calendar.HOUR_OF_DAY) + ":";
        str += cal.get(Calendar.MINUTE) + " ";

        // "음식이름" + "칼로리" + "탄수화물" + "단백질" + "지방" + "나트륨"
        str += strFoodName + " ";
        str += kcal + " " + tans + " " + danb + " " + jiba + " " + natt + "\n";

        try {
            FileOutputStream outFs = openFileOutput("db.txt", Context.MODE_PRIVATE);
            outFs.write(str.getBytes());
            outFs.close();
        } catch (Exception e){
            Toast.makeText(getApplicationContext(),str,
                    Toast.LENGTH_LONG).show();
        }
    }
}
