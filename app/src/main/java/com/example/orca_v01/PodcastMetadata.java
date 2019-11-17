package com.example.orca_v01;

public class PodcastMetadata {
    public String podcastName;
    public String podcastDesc;
    public String podcastGenre;
    public String podoastArtUrl;
    public String lastEpisode;
    public String podcastCount;

    public PodcastMetadata(String name, String desc, String artUrl){
        podcastName = name;
        podcastDesc = desc;
        podoastArtUrl = artUrl;
    }

    public String getPodcastName(){
        return podcastName;
    }

    public String getPodcastDesc(){
        return podcastDesc;
    }

    public String getPodoastArtUrl(){
        return podoastArtUrl;
    }

}
