package com.example.orca_v01;

public class PodcastMetadata {
    public String podcastName;
    public String podcastDesc;
    public String podcastGenre;
    public String podoastArtUrl;
    public String podcastArtist;
    public String lastEpisode;
    public String podcastCount;
    public String podcastFeedUrl;

    public PodcastMetadata(String name, String desc, String artUrl, String artist, String feedUrl){
        podcastName = name;
        podcastDesc = desc;
        podoastArtUrl = artUrl;
        podcastArtist = artist;
        podcastFeedUrl = feedUrl;
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

    public String getPodcastArtist(){return podcastArtist;}

    public String getPodcastFeedUrl() {
        return podcastFeedUrl;
    }
}
