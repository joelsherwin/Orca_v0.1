package com.example.orca_v01;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PodcastSearchRecyclerAdapter extends RecyclerView.Adapter<PodcastSearchRecyclerAdapter.PodViewHolder> {
    private ArrayList<PodcastMetadata> arrayList = new ArrayList<>();


    public PodcastSearchRecyclerAdapter(ArrayList<PodcastMetadata> searchResults) {
        this.arrayList = searchResults;

    }

    @NonNull
    @Override
    public PodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v("CreateViewHolder", "in onCreateViewHolder");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.podcast_recycler_view,parent,false);

        return new PodViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PodViewHolder holder, int position) {
        Log.v("BindViewHolder", "in onBindViewHolder");
        PodcastMetadata podcast = arrayList.get(position);
        holder.name.setText(podcast.getPodcastName());
        holder.desc.setText(podcast.getPodcastDesc());
        holder.artist.setText(podcast.getPodcastArtist());
        Picasso.get().load(podcast.getPodoastArtUrl()).into(holder.art);


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
            name = itemView.findViewById(R.id.podcastName);
            desc = itemView.findViewById(R.id.podcastDescription);
            art = itemView.findViewById(R.id.podcastArt);
            artist = itemView.findViewById(R.id.podcastArtist);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
