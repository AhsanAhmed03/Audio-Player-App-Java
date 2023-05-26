package com.example.getvideos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AudioPlayer extends AppCompatActivity {

    String audio_path;

    TextView playerpos, playerdur;

    SeekBar seekBar;

    ImageView rewind, play, pause, forward;

    MediaPlayer mediaPlayer;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        Intent intent = getIntent();
        audio_path = intent.getStringExtra("Audio_path");

        playerpos = findViewById(R.id.playerPosition);
        playerdur = findViewById(R.id.playerDuration);
        seekBar = findViewById(R.id.seek_bar);
        rewind = findViewById(R.id.btn_rew);
        pause = findViewById(R.id.btn_pause);
        play = findViewById(R.id.btn_ply);
        forward = findViewById(R.id.btn_frwd);

        mediaPlayer = new MediaPlayer();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this,500);
            }
        };

        int duration = mediaPlayer.getDuration();
        String sDuration = convertFormat(duration);
        playerdur.setText(sDuration);

        try {
            mediaPlayer.setDataSource(audio_path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                handler.postDelayed(runnable,0);
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                mediaPlayer.pause();
                handler.removeCallbacks(runnable);
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPos = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();
                if (mediaPlayer.isPlaying() && duration != currentPos);
                currentPos = currentPos +5000;
                playerpos.setText(convertFormat(currentPos));
                mediaPlayer.seekTo(currentPos);

            }
        });

        rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPos = mediaPlayer.getCurrentPosition();

                if (mediaPlayer.isPlaying() && currentPos > 5000){
                    currentPos = currentPos -5000;
                    playerpos.setText(convertFormat(currentPos));
                    mediaPlayer.seekTo(currentPos);
                }

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    mediaPlayer.seekTo(i);
                }
                playerpos.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                mediaPlayer.seekTo(0);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
        finish();
    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration){
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toMinutes(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));

    }
}