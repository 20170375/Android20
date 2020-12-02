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
    TextView tvSaveTime, tvFoodName, tvWarning;
    ImageView ivFood;
    LinearLayout dataLayout, kcalLayout;
    FrameView dataFrameView, kcalFrameView;
    String saveTime, strFoodKor="No data", strFoodEng="No data",
            strFoodData="No Data", strTotalKcal="No Data";
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
        tvWarning = (TextView) findViewById(R.id.tvWarning);
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
        kcalFrameView = (FrameView) new FrameView(this,100,strTotalKcal);
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
        if (requestCode == 0){
            if (resultCode == RESULT_OK){
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    ivFood.setMaxHeight(700);
                    ivFood.setImageBitmap(bmp);

                    // 가져온 Bitmap 파일로 음식 분석 알고리즘 실행
                    analyzeFood(bmp);

                    // 가져온 음식 정보 화면에 나타내기
                    loadFoodInfo();
                    tvFoodName.setText(strFoodKor);
                    tvFoodName.setVisibility(View.VISIBLE);
                    dataFrameView = (FrameView) new FrameView(this,360,strFoodData);
                    dataLayout.addView(dataFrameView);

                    // DB 최신화
                    dbUpdate();

                    // 오늘 칼로리 섭취량 update
                    kcalLayout.removeAllViews();
                    calKcal();
                    kcalFrameView = (FrameView) new FrameView(this,100,strTotalKcal);
                    kcalLayout.addView(kcalFrameView);

                    // Instagram 버튼 활성화
                    btnSelectPicture.setVisibility(View.INVISIBLE);
                    btnSelectPicture.setClickable(false);
                    btnInstagram.setVisibility(View.VISIBLE);
                    btnInstagram.setClickable(true);

                } catch (Exception e) {
                }
            } else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    // 오늘 섭취한 칼로리 계산
    private void calKcal(){
        // DB load
        try {
            FileInputStream inFs = openFileInput("db.txt");
            byte[] txt = new byte[2000];
            int txtNo;
            String str = "";
            while((txtNo=inFs.read()) != -1)
                str += (char)txtNo;
            inFs.close();

            // line 단위로 분할
            String[] line = str.split("\n");

            // make String today
            String today = "";
            Calendar cal = Calendar.getInstance();
            today += cal.get(Calendar.YEAR) + ":";
            today += cal.get(Calendar.MONTH) + 1 + ":";
            today += cal.get(Calendar.DAY_OF_MONTH) + ":";
            today += cal.get(Calendar.HOUR_OF_DAY) + ":";

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
        } catch (IOException e){
            cKcal = 0;
        }

        // 불러온 정보로 String 만들기
        strTotalKcal = "오늘 총 섭취량" + "               " + (int)cKcal +
                " / " + (int)tKcal + "  kcal";

        // 오늘 섭취한 칼로리가 권장 섭취량을 초과할시 경고문 나타내기
        if(cKcal > tKcal){
            tvWarning.setVisibility(View.VISIBLE);
        } else{
            tvWarning.setVisibility(View.INVISIBLE);
        }
    }

    // 음식 영양소 정보 불러오는 메소드
    protected void loadFoodInfo(){

        ////////////////////
        //   음식 영양소   //
        //   정보 load    //
        ////////////////////

        // 음식이름 영>한 전환
        switch (strFoodEng){
            case "spagetti":
                strFoodKor = "토마토 스파게티";
                break;
            case "sushi":
                strFoodKor = "초밥";
                break;

            //      ~       //
            // 앞으로 추가.. //
            //      ~       //

            default:
                break;
        }

        // 불러온 음식 정보-임의로 설정 (토마토 스파게티)
        kcal = 935f;
        tans = 56.21f;
        danb = 34.48f;
        jiba = 27.91f;
        natt = 1250.30f;

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


        // 분석된 음식 이름-음식 임의로 설정 (토마토 스파게티)
        strFoodEng = "spagetti";
    }

    // DB 최신화 메소드
    protected void dbUpdate(){
        String str = "";

        // DB load
        try {
            FileInputStream inFs = openFileInput("db.txt");
            byte[] txt = new byte[1000];
            int txtNo;
            while((txtNo=inFs.read()) != -1)
                str += (char)txtNo;
            inFs.close();
        } catch (IOException e){
//            Toast.makeText(this, "fail to load db.txt", Toast.LENGTH_LONG).show();
        }

        // "YY:MM:DD:HH:MM"
        Calendar cal = Calendar.getInstance();
        str += cal.get(Calendar.YEAR) + ":";
        str += cal.get(Calendar.MONTH) + 1 + ":";
        str += cal.get(Calendar.DAY_OF_MONTH) + ":";
        str += cal.get(Calendar.HOUR_OF_DAY) + ":";
        str += cal.get(Calendar.MINUTE) + " ";

        // "음식이름" + "칼로리" + "탄수화물" + "단백질" + "지방" + "나트륨"
        str += strFoodEng + " ";
        str += kcal + " " + tans + " " + danb + " " + jiba + " " + natt + "\n";

        try {
            FileOutputStream outFs = openFileOutput("db.txt", Context.MODE_PRIVATE);
            outFs.write(str.getBytes());
            outFs.close();
        } catch (Exception e){
        }
    }
}
