package com.example.youtubeaudio;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.WatchVideoActivity;
import com.example.YoutubeVideos;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import info.isuru.sheriff.enums.SheriffPermission;
import info.isuru.sheriff.helper.Sheriff;
import info.isuru.sheriff.interfaces.PermissionListener;

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.myviewholder> {
    Context context;
    Sheriff sheriffPermission;
    List<YoutubeVideos> list = new ArrayList<>();
    Application application;
    AppCompatActivity appCompatActivity;
    public YoutubeAdapter(Context context, List<YoutubeVideos> list, Application application, AppCompatActivity activity) {
        this.context = context;
        this.list = list;
        this.application = application;
        this.appCompatActivity=activity;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtuberow, parent, false);
        myviewholder vh = new myviewholder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, final int position) {
        try {
            Glide.with(context)
                    .load(list.get(position).image)
                    .centerCrop()
                    .into(holder.yt_thumbnail)

            ;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WatchVideoActivity.class);
                intent.putExtra("videoid",list.get(position).id);
                context.startActivity(intent);
            }
        });
        holder.info.setText(list.get(position).info);
        holder.views.setText(list.get(position).views);
        holder.video_title.setText(list.get(position).title);
        holder.swipereveallayout.setSwipeListener(new SwipeRevealLayout.SwipeListener() {
            @Override
            public void onClosed(SwipeRevealLayout view) {
            }

            @Override
            public void onOpened(SwipeRevealLayout view) {
                Intent intent = new Intent(context, WatchVideoActivity.class);
                intent.putExtra("videoid",list.get(position).id);
                intent.putExtra("videoname",list.get(position).title);
                context.startActivity(intent);
                view.close(true);
            }

            @Override
            public void onSlide(SwipeRevealLayout view, float slideOffset) {

            }
        });
        holder.save.setOnClickListener(v -> {
            SearchTube.savedvideos.add(list.get(position));
            Gson gson = new Gson();
            String json=gson.toJson(SearchTube.savedvideos);
            SearchTube.editor.putString("savedvideos",json);
            SearchTube.editor.apply();
        });

    }
    public void updatelist(List<YoutubeVideos> list){
        this.list= list;

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder{

        MaterialButton watch,save;
        com.chauthai.swipereveallayout.SwipeRevealLayout swipereveallayout;
        ImageView yt_thumbnail;
        TextView video_title,views,info;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            yt_thumbnail= itemView.findViewById(R.id.yt_thumbnail);
            watch=itemView.findViewById(R.id.watch);
            swipereveallayout= itemView.findViewById(R.id.swipereveallayout);
            save= itemView.findViewById(R.id.save);
            video_title= itemView.findViewById(R.id.video_title);
            views=itemView.findViewById(R.id.views);
            info= itemView.findViewById(R.id.info);
        }
    }
}
