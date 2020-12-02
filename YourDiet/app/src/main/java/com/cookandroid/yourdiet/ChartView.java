package com.cookandroid.yourdiet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class ChartView extends View {
    int height = 300;
    float tPercent=100f, dPercent=80f, jPercent=120f, nPercent=105f;

    public ChartView(Context context, int height, float tPercent, float dPercent,
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
        textPaint.setFakeBoldText(true);

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

        Paint barTextPaint = new Paint();
        barTextPaint.setTextSize(25);
        barTextPaint.setFakeBoldText(true);
        barTextPaint.setTextAlign(Paint.Align.CENTER);

        // 차트 축 그리기
        canvas.drawLine(170,height,getWidth()-150,height,axisPaint);
        canvas.drawLine(167,0,170,height,axisPaint);
        canvas.drawLine(155,height/3,165,height/3,axisPaint);

        // 차트 텍스트 그리기
        canvas.drawText("100%",92,height/3-10,textPaint);
        canvas.drawText("비율",102,height-10,textPaint);
        canvas.drawText("탄수화물",230,height+35,textPaint);
        canvas.drawText("단백질",420,height+35,textPaint);
        canvas.drawText("지방",600,height+35,textPaint);
        canvas.drawText("나트륨",730,height+35,textPaint);

        // bar 그리기 (탄수화물, 단백질, 지방, 나트륨 순)
        float tHeight = height - (height/3*2) * tPercent/100;
        float dHeight = height - (height/3*2) * dPercent/100;
        float jHeight = height - (height/3*2) * jPercent/100;
        float nHeight = height - (height/3*2) * nPercent/100;
        canvas.drawLine(285,tHeight,285,height-3,tanPaint);
        canvas.drawLine(460,dHeight,460,height-3,danPaint);
        canvas.drawLine(625,jHeight,625,height-3,jiPaint);
        canvas.drawLine(770,nHeight,770,height-3,naPaint);

        // bar 텍스트 그리기 (영양소 비율 소수점 첫째 자리까지)
        String tText = String.format("%.1f",tPercent) + "%";
        String dText = String.format("%.1f",dPercent) + "%";
        String jText = String.format("%.1f",jPercent) + "%";
        String nText = String.format("%.1f",nPercent) + "%";
        canvas.drawText(tText,285,tHeight+25,barTextPaint);
        canvas.drawText(dText,460,dHeight+25,barTextPaint);
        canvas.drawText(jText,625,jHeight+25,barTextPaint);
        canvas.drawText(nText,770,nHeight+25,barTextPaint);
    }
}
