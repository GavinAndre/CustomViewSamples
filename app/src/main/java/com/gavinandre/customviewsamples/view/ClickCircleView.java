package com.gavinandre.customviewsamples.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by gavinandre on 17-2-17.
 */

public class ClickCircleView extends View {
    private Bitmap bitmap;
    private Paint paint;
    private Canvas canvas;
    private boolean isSpreadFlag = false;//标记是否发射完成

    public boolean isSpreadFlag() {
        return isSpreadFlag;
    }

    public void setIsSpreadFlag(boolean isSpreadFlag) {
        this.isSpreadFlag = isSpreadFlag;
    }

    public ClickCircleView(Context context, int width, int height, int screenWidth, int screenHeight) {
        super(context);
        bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888); // 设置位图的宽高
        canvas = new Canvas();
        canvas.setBitmap(bitmap);
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(50);
        canvas.drawCircle(screenWidth / 2, screenHeight / 2, width / 2 + 10, paint);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, null);
    }
}
