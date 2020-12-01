package com.cookandroid.dietmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DietActivity extends AppCompatActivity {
    ImageButton btnDiet2main;
    TextView tvFoodMap, tv1, tv2;
    ListView lvDiet;
    LinearLayout chartLayout, resultLayout;
    ChartView chartView;
    FrameView frameView;
    float tPer=109.876f, dPer=76.543f, jPer=123.456f, nPer=105.543f;
    String strResult="No Data";
    String[] strFoodList ={"No Data",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        btnDiet2main = (ImageButton) findViewById(R.id.btnDiet2main);
        tvFoodMap = (TextView) findViewById(R.id.tvFoodMap);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        lvDiet = (ListView) findViewById(R.id.lvDiet);
        chartLayout = (LinearLayout) findViewById(R.id.chartLayout);
        resultLayout = (LinearLayout) findViewById(R.id.resultLayout);

        // 차트 화면에 나타내기
        chartView = (ChartView) new ChartView(this,300,tPer,dPer,jPer,nPer);
        chartLayout.addView(chartView);

        // 영양소 섭취 현황 분석 및 String 생성
        showChart();

        // 권장 식단 List 생성
        recommendDiet();

        // 영양 섭취 분석 결과 화면에 나타내기
        frameView = (FrameView) new FrameView(this,260,strResult);
        resultLayout.addView(frameView);

        // 권장 식단 리스트 화면에 나타내기
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,strFoodList);
        lvDiet.setAdapter(adapter);
        lvDiet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("FoodName",strFoodList[position]);
                startActivity(intent);
            }
        });

        // 뒤로가기 버튼 활성화
        btnDiet2main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // 누적 데이터 기반 영양소 섭취 현황 생성 메소드
    protected void showChart(){

        ///////////////////
        //    DB load    //
        ///////////////////

        ////////////////////
        //   Percentage   //
        //   Processing   //
        ////////////////////


        strResult = "";

        if(tPer >= 120){
            strResult = strResult + "탄수화물" + "의 섭취량이 " + "너무 많습니다." + "\n";
        } else if(tPer <= 80){
            strResult = strResult + "탄수화물" + "의 섭취량이 " + "부족합니다." + "\n";
        }

        if(dPer >= 120){
            strResult += "단백질" + "의 섭취량이 " + "너무 많습니다." + "\n";
        } else if(dPer <= 80){
            strResult += "단백질" + "의 섭취량이 " + "부족합니다." + "\n";
        }

        if(jPer >= 120){
            strResult += "지방" + "의 섭취량이 " + "너무 많습니다." + "\n";
        } else if(jPer <= 80){
            strResult += "지방" + "의 섭취량이 " + "부족합니다." + "\n";
        }

        if(nPer >= 120){
            strResult += "나트륨" + "의 섭취량이 " + "너무 많습니다." + "\n";
        } else if(nPer <= 80){
            strResult += "나트륨" + "의 섭취량이 " + "부족합니다." + "\n";
        }
    }

    // 누적 데이터 기반 권장 식단 추천 메소드
    protected void recommendDiet(){

        ///////////////////////
        //     음식 영양소    //
        //     정보 load     //
        ///////////////////////

        ////////////////////////
        //   전체 음식중에서   //
        //   Percentage 기반  //
        //   권장 식단 선정    //
        ////////////////////////

        strFoodList = new String[]{
                "치킨", "카레라이스", "초밥", "순대국밥", "스파게티", "돈까스",
                "삼겹살", "간장게장", "김치볶음밥", "된장찌개", "와플", "설렁탕"};
    }
}
