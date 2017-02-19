package com.gavinandre.customviewsamples.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.gavinandre.customviewsamples.R;

/**
 * Created by gavinandre on 17-2-17.
 */
public class CircleProgressImageView extends View {

    private static final String TAG = CircleProgressImageView.class.getSimpleName();

    //表示坐标系中的一块矩形区域
    private RectF mRectF;

    //画笔
    private Paint mPaint;

    //画笔宽度
    private int mCircleStoreWidth = 3;

    //最大进度值
    private int mMaxProcessValue = 100;

    //进度值
    private int mProcessValue;

    private int width;

    private int height;

    //播放器按钮id值
    private int bitmapPlay;
    private int bitmapStop;

    //播放器按钮Bitmap对象
    private Bitmap drawBitmapPlay;
    private Bitmap drawBitmapStop;

    private Context context;

    //标记是否正在播放中
    private boolean isPlay;

    public CircleProgressImageView(Context context) {
        this(context, null);
    }

    public CircleProgressImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.circle_progress_image_attrs);
        bitmapPlay = a.getResourceId(R.styleable.circle_progress_image_attrs_play_image, R.mipmap.play_button);
        bitmapStop = a.getResourceId(R.styleable.circle_progress_image_attrs_stop_image, R.mipmap.stop_button);
        a.recycle();
        drawBitmapPlay = BitmapFactory.decodeResource(context.getResources(), bitmapPlay);
        drawBitmapStop = BitmapFactory.decodeResource(context.getResources(), bitmapStop);
        mRectF = new RectF();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = measureWidth(widthMeasureSpec);
        height = measureWidth(heightMeasureSpec);
        mRectF.left = width / 2 - drawBitmapPlay.getWidth() / 2;
        mRectF.top = height / 2 - drawBitmapPlay.getHeight() / 2;
        mRectF.right = width / 2 + drawBitmapPlay.getWidth() / 2;
        mRectF.bottom = height / 2 + drawBitmapPlay.getHeight() / 2;
    }

    public int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 200;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(specSize, result);
            }
        }
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        //画圆
        mPaint.setColor(ContextCompat.getColor(context, R.color.orange));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mCircleStoreWidth);
        //        canvas.drawArc(mRectF, -90, 360, false, mPaint);
        mPaint.setColor(ContextCompat.getColor(context, R.color.gray));
        canvas.drawArc(mRectF, -90, ((float) mProcessValue / mMaxProcessValue) * 360, false, mPaint);
        Log.d(TAG, ((float) mProcessValue / mMaxProcessValue) * 360 + "");
        float imageLeft = width / 2 - drawBitmapPlay.getWidth() / 2;
        float imageTop = height / 2 - drawBitmapPlay.getHeight() / 2;
        if (isPlay) {
            canvas.drawBitmap(drawBitmapStop, imageLeft, imageTop, mPaint);
        } else {
            canvas.drawBitmap(drawBitmapPlay, imageLeft, imageTop, mPaint);
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //定时更新界面
                    if (isPlay) {
                        mProcessValue += 150;
                        if (mProcessValue == mMaxProcessValue) {
                            isPlay = false;
                        }
                        invalidate();
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 150);
                    }
            }
            super.handleMessage(msg);
        }
    };

    public void play() {
        isPlay = true;
        Message message = handler.obtainMessage(1);
        handler.sendMessageDelayed(message, 150);
    }

    public void setDuration(int duration) {
        this.mMaxProcessValue = duration;
    }

    public void clearDuration() {
        this.mMaxProcessValue = 0;
        this.mProcessValue = 0;
    }

    public void pause() {
        isPlay = false;
        invalidate();
    }

    public void stop() {
        isPlay = false;
        this.mMaxProcessValue = 0;
        this.mProcessValue = 0;
        invalidate();
    }
}
