package com.example.orca_v01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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

    public PodcastDetailScreen() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        debugString = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_detail_screen);
        podcastNameView = findViewById(R.id.podcastName);
        podcastArtistView = findViewById(R.id.podcastArtist);
        podcastArtView = findViewById(R.id.podcastArt);
        podcastDescription = findViewById(R.id.podcastDescription);
        debugText = findViewById(R.id.debugText);
        String podcastName = getIntent().getStringExtra("PODCAST_NAME");
        String podcastArtist = getIntent().getStringExtra("PODCAST_ARTIST");
        String podcastArtworkUrl = getIntent().getStringExtra("PODCAST_ART");
        String podcastFeedUrl = getIntent().getStringExtra("PODCAST_FEEDURL");
        new FetchFeedTask().execute((podcastFeedUrl));




        podcastNameView.setText(podcastName);
        podcastArtistView.setText(podcastArtist);
        Picasso.get().load(podcastArtworkUrl).into(podcastArtView);


    }


    public String parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        PodcastEpisodeMetadata podcastEpisode = new PodcastEpisodeMetadata();
        String description = null;
        boolean isItem = false;
        Integer count = 0;

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if(name == null)
                    continue;

                if(eventType == XmlPullParser.END_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MainActivity", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (isItem && count < 10 && name.equalsIgnoreCase("title"))  {
                     podcastEpisode.episodeName = result;
                     podcastEpisodes.add(podcastEpisode);
                    debugString = debugString + '\n' + result;
                    count++;
                }

                if ((name.equalsIgnoreCase("description") || name.contains("summary")))  {
                    if(!isItem) description = result;
                }
                
           }

            return description;
        } finally {
            inputStream.close();
        }
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
                feedUrl.replace(".xml","");
                URL url = new URL(feedUrl);
                InputStream inputStream = url.openConnection().getInputStream();
                podDesc = parseFeed(inputStream);
                return true;
            } catch (IOException e) {


            } catch (XmlPullParserException e) {


            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {


            if (success) {
                if(podDesc!=null) {
                    podcastDescription.setText(podDesc);
                    debugText.setText(debugString);
                }

            } else {

            }
        }
    }






}
