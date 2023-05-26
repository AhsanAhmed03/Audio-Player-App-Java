package com.example.getvideos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<AudioModel> audioList = new ArrayList<>();
    RecyclerView recyclerView;
    AudioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AudioAdapter(this,audioList);
        recyclerView.setAdapter(adapter);

        //To Get All Audio Files from device

        ContentResolver contentResolver = getContentResolver();
        Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.DATA,MediaStore.Audio.Media._ID};
        Cursor cursor = contentResolver.query(audioUri,projection,null,null,null);

        if (cursor != null){
            while (cursor.moveToNext()){
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

                AudioModel audioModel = new AudioModel(title,path);
                audioList.add(audioModel);
            }
        }

    }
}