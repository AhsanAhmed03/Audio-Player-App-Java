package com.example.getvideos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AudioPlayer extends AppCompatActivity {

    String audio_path;

    TextView playerpos, playerdur;

    SeekBar seekBar;

    private List<String> audioPaths;
    private int currentPosition;

    ImageView rewind, play, pause, forward,play_next, play_previous;

    MediaPlayer mediaPlayer;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        // Retrieve the list of audio paths and selected position from the intent
        audioPaths = getIntent().getStringArrayListExtra("AudioPaths_list");
        currentPosition = getIntent().getIntExtra("SelectedPosition", 0);


        playerpos = findViewById(R.id.playerPosition);
        playerdur = findViewById(R.id.playerDuration);
        seekBar = findViewById(R.id.seek_bar);
        rewind = findViewById(R.id.btn_rew);
        pause = findViewById(R.id.btn_pause);
        play = findViewById(R.id.btn_ply);
        forward = findViewById(R.id.btn_frwd);
        play_next = findViewById(R.id.btn_next);
        play_previous = findViewById(R.id.btn_previous);

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

        // Initialize MediaPlayer
        mediaPlayer = new MediaPlayer();

        // Initialize and start playback using the first audio path
        initializeMediaPlayer(audioPaths.get(currentPosition));
        playAudio();

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

        play_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNextAudio();
            }
        });

        play_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPreviousAudio();
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

    // Method to play the next audio
    private void playNextAudio() {
        if (currentPosition < audioPaths.size() - 1) {
            currentPosition++;
            stopAudio();
            initializeMediaPlayer(audioPaths.get(currentPosition));
            playAudio();
        } else {
            // You can handle the case when there are no more songs (wrap around, display a message, etc.)
        }
    }

    // Method to play the previous audio
    private void playPreviousAudio() {
        if (currentPosition > 0) {
            currentPosition--;
            stopAudio();
            initializeMediaPlayer(audioPaths.get(currentPosition));
            playAudio();
        } else {
            // You can handle the case when you are at the beginning of the list
        }
    }

    // Method to stop the currently playing audio
    private void stopAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    // Method to initialize MediaPlayer with the given audio path
    private void initializeMediaPlayer(String path) {
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            Log.e("AudioPlayerException", "Error initializing MediaPlayer: " + e.getMessage());
        }
    }

    // Method to start playing audio
    private void playAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            play.setVisibility(View.GONE);
            pause.setVisibility(View.VISIBLE);
        }
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