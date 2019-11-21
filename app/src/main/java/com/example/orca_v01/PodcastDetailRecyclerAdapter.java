package com.example.orca_v01;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.icosillion.podengine.exceptions.MalformedFeedException;
import com.icosillion.podengine.models.Episode;


import java.util.ArrayList;
import java.util.List;

public class PodcastDetailRecyclerAdapter extends RecyclerView.Adapter<PodcastDetailRecyclerAdapter.PodViewHolder> {
    private List<Episode> arrayList;


    public PodcastDetailRecyclerAdapter(List<Episode> episodes) {
        this.arrayList = episodes;

    }

    @NonNull
    @Override
    public PodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v("CreateViewHolder", "in onCreateViewHolder");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.podcast_episode_view,parent,false);

        return new PodViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PodViewHolder holder, int position) {
        Log.v("BindViewHolder", "in onBindViewHolder");
        Episode episode = arrayList.get(position);
        try {
            holder.name.setText(episode.getTitle());
        } catch (MalformedFeedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class PodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name, desc, artist;
        ImageView art;
        View mView;
        public PodViewHolder(View itemView) {
            super(itemView);
            Log.v("ViewHolder","in View Holder");
            name = itemView.findViewById(R.id.episodeName);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
