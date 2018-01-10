package com.example.apamr.musicplayerfinal;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public Button btnPlay;
    public Button btnNext;
    public Button btnPrev;
    public TextView txtstart;
    public TextView txtfinal;
    public SeekBar songProgressBar;
    public MediaPlayer mp;
    private Handler mHandler = new Handler();
    private double startTime = 0;
    private double finalTime = 0;
    public int seekForwardTime = 5000;
    public int seekBackwardTime = 5000;

    public static int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPrev = (Button) findViewById(R.id.button);
        btnPlay = (Button) findViewById(R.id.button2);
        btnNext = (Button) findViewById(R.id.button3);
        txtstart = (TextView) findViewById(R.id.starttime);
        txtfinal = (TextView) findViewById(R.id.finaltime);

        mp = MediaPlayer.create(MainActivity.this, R.raw.starboy);
        songProgressBar = (SeekBar) findViewById(R.id.seekBar);
        songProgressBar.setClickable(false);
//        songProgressBar.setOnSeekBarChangeListener(this);

        //mp.setOnCompletionListener(this);
        //utils = new Utilities();
        finalTime = mp.getDuration();
        startTime = mp.getCurrentPosition();
        txtfinal.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime)))
        );

        txtstart.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime)))
        );

        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                if (mp.isPlaying()) {
                    if (mp != null) {
                        mp.pause();
                        // Changing button image to play button
                        btnPlay.setBackgroundResource(R.drawable.play);
                    }
                } else {
                    // Resume song
                    if (mp != null) {
                        mp.start();
                        finalTime = mp.getDuration();
                        startTime = mp.getCurrentPosition();

                        if(oneTimeOnly == 0) {
                            songProgressBar.setMax((int)finalTime);
                            oneTimeOnly = 1;
                        }

                        txtfinal.setText(String.format("%d:%d",
                                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                finalTime)))
                        );

                        txtstart.setText(String.format("%d:%d",
                                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                startTime)))
                        );

                        songProgressBar.setProgress((int)startTime);
                        mHandler.postDelayed(UpdateSongTime,100);

                        // Changing button image to pause button
                        btnPlay.setBackgroundResource(R.drawable.pause);
                    }
                }

            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int temp = (int) startTime;

                if((temp-seekBackwardTime)>0)
                {
                    startTime = startTime - seekBackwardTime;
                    mp.seekTo((int)startTime);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int temp = (int) startTime;

                if((temp+seekForwardTime)<=finalTime)
                {
                    startTime = startTime + seekForwardTime;
                    mp.seekTo((int)startTime);
                }
            }
        });
    }
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mp.getCurrentPosition();
            txtstart.setText(String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            songProgressBar.setProgress((int)startTime);
            mHandler.postDelayed(this, 100);
        }
    };


}



