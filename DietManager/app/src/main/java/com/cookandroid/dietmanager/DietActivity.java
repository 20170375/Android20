package com.cookandroid.dietmanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DietActivity extends AppCompatActivity {
    Button btnDiet2main;
    TextView tvFoodMap, tv1, tv2;
    ListView lvDiet;
    LinearLayout chartLayout, resultLayout;
    CharView charView;
    FrameView frameView;
    float tPer=100, dPer=80, jPer=120, nPer=105;
    String strResult="No Data";
    String[] strFoodList ={"No Data",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        btnDiet2main = (Button) findViewById(R.id.btnDiet2main);
        tvFoodMap = (TextView) findViewById(R.id.tvFoodMap);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        lvDiet = (ListView) findViewById(R.id.lvDiet);
        chartLayout = (LinearLayout) findViewById(R.id.chartLayout);
        resultLayout = (LinearLayout) findViewById(R.id.resultLayout);

        // 차트 화면에 나타내기
        charView = (CharView) new CharView(this,300,tPer,dPer,jPer,nPer);
        chartLayout.addView(charView);

        // 데이터 처리과정 //
        dataProcessing();

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
                startActivityForResult(intent,0);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    private static class CharView extends View{
        int height = 300;
        float tPercent=100, dPercent=80, jPercent=120, nPercent=105;

        public CharView(Context context, int height, float tPercent, float dPercent,
                        float jPercent, float nPercent) {
            super(context);
            this.height = height;
            this.tPercent = tPercent;
            this.dPercent = dPercent;
            this.jPercent = jPercent;
            this.nPercent = nPercent;
        }
        @Override
        protected void onDraw(Canvas canvas){
            super.onDraw(canvas);

            Paint axisPaint = new Paint();
            axisPaint.setStrokeWidth(5);

            Paint textPaint = new Paint();
            textPaint.setTextSize(30);

            Paint tanPaint = new Paint();
            tanPaint.setColor(Color.rgb(111,188,255));
            tanPaint.setStrokeWidth(80);

            Paint danPaint = new Paint();
            danPaint.setColor(Color.rgb(255,222,111));
            danPaint.setStrokeWidth(80);

            Paint jiPaint = new Paint();
            jiPaint.setColor(Color.rgb(180,255,111));
            jiPaint.setStrokeWidth(80);

            Paint naPaint = new Paint();
            naPaint.setColor(Color.rgb(255,170,255));
            naPaint.setStrokeWidth(80);

            // 차트 축 그리기
            canvas.drawLine(170,height,getWidth()-150,height,axisPaint);
            canvas.drawLine(167,0,170,height,axisPaint);
            canvas.drawLine(155,height/3,165,height/3,axisPaint);

            // 텍스트 그리기
            canvas.drawText("100%",92,height/3-10,textPaint);
            canvas.drawText("비율",102,height-10,textPaint);
            canvas.drawText("탄수화물",230,height+35,textPaint);
            canvas.drawText("단백질",420,height+35,textPaint);
            canvas.drawText("지방",600,height+35,textPaint);
            canvas.drawText("나트륨",730,height+35,textPaint);

            // bar 그리기 (탄수화물, 단백질, 지방, 나트륨 순)
            float tanHeight = height - (height/3*2) * tPercent/100;
            float danHeight = height - (height/3*2) * dPercent/100;
            float jiHeight = height - (height/3*2) * jPercent/100;
            float naHeight = height - (height/3*2) * nPercent/100;

            canvas.drawLine(285,tanHeight,285,height-3,tanPaint);
            canvas.drawLine(460,danHeight,460,height-3,danPaint);
            canvas.drawLine(625,jiHeight,625,height-3,jiPaint);
            canvas.drawLine(770,naHeight,770,height-3,naPaint);
        }
    }

    // 누적된 데이터 처리 메소드
    protected void dataProcessing(){

    }

    // 누적 데이터 기반 영양소 섭취 현황 생성 메소드
    protected void showChart(){

        ////////////////////////
        // 누적 데이터 불러오기 //
        ////////////////////////

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

        ////////////////////////////////
        // 음식 목록에서 권장 식단 선정 //
        ////////////////////////////////

        strFoodList = new String[]{"된장찌개", "카레라이스", "김치볶음밥", "순대국밥", "스파게티",
                "돈까스", "삼겹살"};
    }
}
