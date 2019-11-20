package com.example.orca_v01;

public class PodcastEpisodeMetadata {
    public String episodeName;
    public String episodeDescription;
    public String episodeNumber;
    public String podoastArtUrl;
    public String podcastArtist;
    public String lastEpisode;
    public String podcastCount;
    public String podcastFeedUrl;

    public PodcastEpisodeMetadata(String name, String desc, String artUrl, String artist, String feedUrl){
        episodeName = name;
        episodeDescription = desc;
        episodeNumber = artUrl;
        podcastArtist = artist;
        podcastFeedUrl = feedUrl;
    }

    public PodcastEpisodeMetadata() {
        episodeName = "EpisodeName";
    }

    public String getEpisodeName(){
        return episodeName;
    }

    public String getEpisodeDescription(){
        return episodeDescription;
    }

    public String getPodoastArtUrl(){
        return podoastArtUrl;
    }

    public String getPodcastArtist(){return podcastArtist;}

    public String getEpisodeNumber() {
        return episodeNumber;
    }

    public String getPodcastFeedUrl() {
        return podcastFeedUrl;


    }
}
