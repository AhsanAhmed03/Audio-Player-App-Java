package com.example.getvideos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioViewHolder> {

    List<AudioModel> audios = new ArrayList<>();
    Context context;

    public AudioAdapter (Context context,List<AudioModel> audios){
        this.context = context;
        this.audios = audios;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_layout,parent,false);
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        AudioModel audioModel = audios.get(position);
        holder.titleText.setText(audioModel.getTitle());
        holder.pathText.setText(audioModel.getPath());
        String path = audioModel.getPath();
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,AudioPlayer.class);
                intent.putExtra("Audio_path",path);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return audios.size();
    }

    static class AudioViewHolder extends RecyclerView.ViewHolder{

        TextView titleText,pathText;
        CardView cardView;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.title_tv);
            pathText = itemView.findViewById(R.id.path_tv);
            cardView = itemView.findViewById(R.id.card_view);

        }
    }
}
