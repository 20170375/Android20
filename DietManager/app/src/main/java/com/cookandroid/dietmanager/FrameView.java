package com.cookandroid.dietmanager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class FrameView extends View {
    int height = 0;
    String strResult = "";

    public FrameView(Context context, int height, String strResult) {
        super(context);
        this.height = height;
        this.strResult = strResult;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint backPaint = new Paint();
        backPaint.setColor(Color.WHITE);

        Paint framePaint = new Paint();

        Paint textPaint = new Paint();
        textPaint.setTextSize(40);
        textPaint.setFakeBoldText(true);

        // frame 그리기
        canvas.drawRoundRect(new RectF(0, 0,getWidth(),height),
                27,27,framePaint);
        canvas.drawRoundRect(new RectF(3, 3,getWidth()-3,height-3),
                27,27,backPaint);

        // result 출력
        int textHeight = 65;
        for (String line: strResult.split("\n")) {
            canvas.drawText(line, 35, textHeight, textPaint);
            textHeight += textPaint.descent() - textPaint.ascent() + 10;
        }
    }
}
