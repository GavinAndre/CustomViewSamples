package com.gavinandre.customviewsamples.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.gavinandre.customviewsamples.R;
import com.gavinandre.customviewsamples.view.CircleRecordSurfaceView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gavinandre on 17-2-20.
 */

public class CustomViewActivity3 extends AppCompatActivity {
    private String TAG = CustomViewActivity3.class.getSimpleName();
    @BindView(R.id.circle_record_view)
    CircleRecordSurfaceView mCircleRecordView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_view_3);
        ButterKnife.bind(this);

        mCircleRecordView.setDuration(6);
        mCircleRecordView.setStartBitmap(R.mipmap.audio_record_mic_btn);
        mCircleRecordView.setStopBitmap(R.mipmap.audio_record_mic_btn_press);
        mCircleRecordView.setArcColor(ContextCompat.getColor(this, R.color.record_green));
        mCircleRecordView.setSmallCircleColor(ContextCompat.getColor(this, R.color.record_green));
        mCircleRecordView.setDefaultRadius(50);
        mCircleRecordView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mCircleRecordView.startDraw();
                        break;
                    case MotionEvent.ACTION_UP:
                        mCircleRecordView.reset();
                        mCircleRecordView.stopDraw();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
}
