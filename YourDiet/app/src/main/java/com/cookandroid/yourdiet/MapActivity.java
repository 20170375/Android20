package com.cookandroid.yourdiet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {
    ImageButton btnMap2main;
    TextView tvFoodMap;
    ListView lvRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        btnMap2main = (ImageButton) findViewById(R.id.btnMap2main);
        tvFoodMap = (TextView) findViewById(R.id.tvFoodMap);
        lvRestaurant = (ListView) findViewById(R.id.lvRestaurant);

        Intent intent = getIntent();
        tvFoodMap.setText("근처에 위치한 " + intent.getStringExtra("FoodName") + " 음식점");

        // 뒤로가기 버튼 활성화
        btnMap2main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
