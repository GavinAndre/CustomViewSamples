package com.gavinandre.customviewsamples.ui.activity;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gavinandre.customviewsamples.R;
import com.gavinandre.customviewsamples.view.CircleProgressImageView;

public class CustomViewActivity2 extends AppCompatActivity {
    private static final String TAG = CustomViewActivity2.class.getSimpleName();
    private CircleProgressImageView circleProgressView;
    private boolean isPlay;
    private MediaPlayer mediaPlayer;
    private boolean isComplete = true;

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
                if (!isPlay) {
                    playAudio();
                } else {
                    pauseAudio();
                }
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
            if (isComplete) {
                mediaPlayer.reset();
                //从asset文件夹下读取MP3文件
                AssetFileDescriptor fileDescriptor = getAssets().openFd("lalalademaxiya.mp3");
                mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                        fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                mediaPlayer.prepare();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopAudio();
                    }
                });
                circleProgressView.clearDuration();
                isComplete = false;
            }
            mediaPlayer.start();
            circleProgressView.setDuration(mediaPlayer.getDuration());
            circleProgressView.play();
            isPlay = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pauseAudio() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            circleProgressView.pause();
            isPlay = false;
        }
    }

    private void stopAudio() {
        circleProgressView.stop();
        isPlay = false;
        isComplete = true;
    }

}
