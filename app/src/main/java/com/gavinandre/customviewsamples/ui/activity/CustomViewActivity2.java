package com.gavinandre.customviewsamples.ui.activity;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gavinandre.customviewsamples.R;
import com.gavinandre.customviewsamples.view.CircleProgressImageView;

import java.io.IOException;

public class CustomViewActivity2 extends AppCompatActivity {
    private static final String TAG = CustomViewActivity2.class.getSimpleName();
    private static final int SET_PROGRESS_VALUESE = 0x123;
    private int processValue = 0;
    private CircleProgressImageView circleProgressView;
    private boolean flag = true;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_view_2);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        circleProgressView = (CircleProgressImageView) findViewById(R.id.iv);
        circleProgressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAudio();
            }
        });
    }

    @Override
    protected void onDestroy() {
        //释放资源
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
        super.onDestroy();
    }

    private void playAudio() {
        try {
            mediaPlayer.reset();
            AssetFileDescriptor fileDescriptor = getAssets().openFd("lalalademaxiya.mp3");
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());

            mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    circleProgressView.setDuration(mediaPlayer.getDuration());
                    circleProgressView.play();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    circleProgressView.stop();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
