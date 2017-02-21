package com.gavinandre.customviewsamples.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * Created by gavinandre on 17-2-17.
 */
public class CircleRecordSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final String TAG = CircleRecordSurfaceView.class.getSimpleName();
    //选择按钮图标
    private boolean isChangeCenterBitmap = true;
    //持续画图
    private boolean isSustainedDraw = false;
    private boolean isStart = true;
    //是否画小圆点，默认为true
    private boolean isDrawSmallCircle = true;
    //小圆点颜色
    private int smallCircleColor;
    //是否画圆弧，默认为true
    private boolean isDrawArc = true;
    //圆弧颜色
    private int arcColor;

    private CompleteTimeCallBack completeTimeCallBack;
    private SurfaceHolder holder = null;
    //绘图属性---------
    private Canvas canvas;

    //录音按钮
    private Paint pPaint;
    private int px;//坐标x位置
    private int py;//坐标y位置
    //radius = defaultRadius * dp
    private int radius;//半径
    //defaultRadius 默认值为40
    private int defaultRadius = 40;
    //起始角度
    private float startAngle = 270;
    //进度
    private float sweepAngle;
    //小球起始角度默认等于进度条起始角度
    private float angle, duration = 20;
    private int startBitmap;
    private int stopBitmap;
    //中心图片的范围，默认为10，值越大图片越小
    private int centerBitmap_margin = 10;
    private int dp;
    private Bitmap bitmap;
    private boolean isGetBitmap = false;
    long a, b, calculateTime, sleepTime = 60, correctSleepTime;


    public CircleRecordSurfaceView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    public void init() {

        //获取mSurfaceHolder
        holder = getHolder();
        holder.addCallback(this);
        //背景设为透明
        if (!isInEditMode()) {
            setZOrderOnTop(true);
        }
        holder.setFormat(PixelFormat.TRANSLUCENT);

        //设置进度条
        pPaint = new Paint();
        pPaint.setAntiAlias(true);
        pPaint.setStrokeWidth(4);
        pPaint.setStyle(Paint.Style.STROKE);
        angle = startAngle;
        sweepAngle = 0;

        dp = Resources.getSystem().getDisplayMetrics().densityDpi / 160;

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!isStart) {
            reset();
        }
        radius = defaultRadius * dp;
        isChangeCenterBitmap = true;
        px = this.getWidth() / 2;
        py = this.getHeight() / 2;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isStart = false;
    }

    @Override
    public void run() {
        while (isStart) {
            if (isSustainedDraw) {
                canvas = holder.lockCanvas(); // 获得画布对象，开始对画布画画
                if (canvas == null) {
                    continue;
                }
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                //                canvas.drawColor(canvasColor); // 把画布填充指定颜色
                drawCenterBitmap();
                drawCircle();
                holder.unlockCanvasAndPost(canvas); // 完成画画，把画布显示在屏幕上

                calculateTime = calculateTime + sleepTime;
                try {
                    b = a;
                    a = System.currentTimeMillis();

                    if (b == 0) {
                        correctSleepTime = sleepTime;

                    } else {
                        if ((a - b) >= sleepTime && (a - b) < 2 * sleepTime) {
                            correctSleepTime = sleepTime - (a - b - correctSleepTime);
                        } else if ((a - b) > 2 * sleepTime) {
                            correctSleepTime = 0;
                            //不睡眠
                        } else if ((a - b) < sleepTime) {
                            correctSleepTime = sleepTime - (a - b - correctSleepTime);
                        }
                    }
                    if (correctSleepTime > 0) {
                        Thread.sleep(correctSleepTime);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (isChangeCenterBitmap) {
                canvas = holder.lockCanvas(); // 获得画布对象，开始对画布画画
                if (canvas == null) {
                    continue;
                }
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                //                canvas.drawColor(canvasColor); // 把画布填充指定颜色
                drawCenterBitmap();
                drawCircle();
                holder.unlockCanvasAndPost(canvas); // 完成画画，把画布显示在屏幕上
            }
        }
    }

    public void drawCircle() {

        //画大圆
        pPaint.setColor(Color.LTGRAY);
        pPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(px, py, radius, pPaint);

        //画点
        if (isDrawSmallCircle) {
            pPaint.setColor(smallCircleColor);
            pPaint.setStyle(Paint.Style.FILL);

            //radians=angle * Math.PI / 180 角度转弧度公式
            //Math.cos(radians) * radius cos计算x轴偏移量,sin计算y轴偏移量
            float ballX = (float) (px + radius * Math.cos(angle * Math.PI / 180));
            float ballY = (float) (py + radius * Math.sin(angle * Math.PI / 180));
            canvas.drawCircle(ballX, ballY, 4 * dp, pPaint);
        }

        //画圆弧
        if (isDrawArc) {
            pPaint.setStyle(Paint.Style.STROKE);
            //            pPaint.setColor(Color.parseColor(arcColor));
            pPaint.setColor(arcColor);
            RectF rect = new RectF(px - radius, py - radius, px + radius, py + radius);
            canvas.drawArc(rect, startAngle, sweepAngle, false, pPaint);//画弧形
        }

        float speed = 360 / (duration * (1000 / sleepTime));
        //        Log.i(TAG, "drawCircle: speed:"+speed);
        angle = angle + speed;
        if (angle > 360) {
            angle = 0;
        }
        sweepAngle = sweepAngle + speed;
        if (sweepAngle > 360) {
            reset();
            if (completeTimeCallBack != null) {
                completeTimeCallBack.stop();
            }
            isSustainedDraw = false;
            isChangeCenterBitmap = true;
        }
    }

    //画中心按钮图形
    private void drawCenterBitmap() {
        int area = radius - centerBitmap_margin * dp;
        RectF imageRect = new RectF(px - area, py - area, px + area, py + area);

        if (isChangeCenterBitmap) {
            if (!isGetBitmap) {
                if (isSustainedDraw) {
                    bitmap = BitmapFactory.decodeResource(getResources(), stopBitmap);
                } else {
                    bitmap = BitmapFactory.decodeResource(getResources(), startBitmap);
                }
            }
            isChangeCenterBitmap = false;
        }
        canvas.drawBitmap(bitmap, null, imageRect, pPaint);
    }

    public interface CompleteTimeCallBack {
        void stop();
    }

    public void completeTime(CompleteTimeCallBack completeTimeCallBack) {
        this.completeTimeCallBack = completeTimeCallBack;
    }

    public void startDraw() {
        isChangeCenterBitmap = true;
        isSustainedDraw = true;
    }

    public void stopDraw() {
        isChangeCenterBitmap = true;
        isSustainedDraw = false;
    }

    public void reset() {
        isStart = true;
        angle = startAngle;
        sweepAngle = 0;
    }

    //设置圆弧颜色，用#RRGGBB 或者 #AARRGGBB
    public void setArcColor(int arcColor) {
        this.arcColor = arcColor;
    }

    //设置小圆点颜色，用#RRGGBB 或者 #AARRGGBB
    public void setSmallCircleColor(int smallCircleColor) {
        this.smallCircleColor = smallCircleColor;
    }

    public void setDefaultRadius(int defaultRadius) {
        this.defaultRadius = defaultRadius;
    }

    public void setStartBitmap(int startBitmap) {
        this.startBitmap = startBitmap;
    }

    public void setStopBitmap(int stopBitmap) {
        this.stopBitmap = stopBitmap;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }
}

