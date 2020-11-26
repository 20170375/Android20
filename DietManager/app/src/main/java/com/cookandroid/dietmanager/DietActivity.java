package com.cookandroid.dietmanager;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DietActivity extends AppCompatActivity {
    Button btnDiet2main;
    TextView tvFoodMap, tv1, tv2;
    ListView lvDiet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        btnDiet2main = (Button) findViewById(R.id.btnDiet2main);
        tvFoodMap = (TextView) findViewById(R.id.tvFoodMap);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        lvDiet = (ListView) findViewById(R.id.lvDiet);

        btnDiet2main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
