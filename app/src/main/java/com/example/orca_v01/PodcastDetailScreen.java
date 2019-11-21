package com.example.orca_v01;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.icosillion.podengine.exceptions.InvalidFeedException;
import com.icosillion.podengine.exceptions.MalformedFeedException;
import com.icosillion.podengine.models.Episode;
import com.icosillion.podengine.models.Podcast;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PodcastDetailScreen extends AppCompatActivity {

    private String podName;
    private String podDesc;
    private String podUrl;
    private List<PodcastMetadata> PodcastData;
    private TextView podcastNameView;
    private TextView podcastArtistView;
    private TextView podcastDescription;
    private TextView debugText;
    private ImageView podcastArtView;
    private String podcastArtworkUrl;
    public String podcastFeedUrl;
    public List<PodcastEpisodeMetadata> podcastEpisodes = new ArrayList<>();
    private URL url;
    private String debugString;
    private Podcast podcast;
    private List<Episode> episodes;
    private RecyclerView episodeList;
    private PodcastDetailRecyclerAdapter episodeAdapter;

    public PodcastDetailScreen() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        debugString = "";
        super.onCreate(savedInstanceState);
        episodes = new ArrayList<>();
        setContentView(R.layout.activity_podcast_detail_screen);
        podcastNameView = findViewById(R.id.episodeName);
        podcastArtistView = findViewById(R.id.podcastArtist);
        podcastArtView = findViewById(R.id.podcastArt);
        podcastDescription = findViewById(R.id.podcastDescription);
        //debugText = findViewById(R.id.debugText);
        String podcastName = getIntent().getStringExtra("PODCAST_NAME");
        String podcastArtist = getIntent().getStringExtra("PODCAST_ARTIST");
        String podcastArtworkUrl = getIntent().getStringExtra("PODCAST_ART");
        String podcastFeedUrl = getIntent().getStringExtra("PODCAST_FEEDURL");
        new FetchFeedTask().execute((podcastFeedUrl));

        episodeList = findViewById(R.id.episodeListView);
        episodeAdapter = new PodcastDetailRecyclerAdapter(episodes);
        episodeList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        episodeList.setItemAnimator( new DefaultItemAnimator());
        //episodeList.setLayoutAnimation(animation);
        episodeList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        episodeList.setAdapter(episodeAdapter);




        podcastNameView.setText(podcastName);
        podcastArtistView.setText(podcastArtist);
        Picasso.get().load(podcastArtworkUrl).into(podcastArtView);


    }



    private class FetchFeedTask extends AsyncTask<String, Void, Boolean> {

        private String urlLink;

        @Override
        protected void onPreExecute() {

            //  name.setText("Feed Title");
            //  description.setText("Feed Description: ");
            urlLink = podcastFeedUrl;

        }

        @Override
        protected Boolean doInBackground(String... strings) {
            String feedUrl  = strings[0];
            if (TextUtils.isEmpty(feedUrl))
                return false;

            try {
                if(!feedUrl.startsWith("http://") && !feedUrl.startsWith("https://"))
                    feedUrl = "http://" + feedUrl;
                if(feedUrl.startsWith("http://"))
                    feedUrl = feedUrl.replace("http","https");

              // URL url = new URL(feedUrl);
               // InputStream inputStream = url.openConnection().getInputStream();
                 Podcast podcast = new Podcast(new URL(feedUrl));
                podDesc = podcast.getDescription();
                episodes = podcast.getEpisodes();

                return true;
            } catch (IOException e) {


            } catch (MalformedFeedException e) {
                e.printStackTrace();
            } catch (InvalidFeedException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {


            if (success) {
                episodeAdapter.notifyDataSetChanged();
                if(podDesc!=null) {
                    podcastDescription.setText(podDesc);
                    for (Episode e: episodes){
                        try {
                            debugString = debugString + e.getTitle() + '\n';
                        } catch (MalformedFeedException ex) {
                            ex.printStackTrace();
                        }
                    }
                   // debugText.setText(debugString);
                }

            } else {

            }
        }
    }






}
