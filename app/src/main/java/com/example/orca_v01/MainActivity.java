package com.example.orca_v01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public ArrayList<PodcastMetadata> searchResults = new ArrayList<>();
    private RecyclerView searchResultsView;
    private PodcastSearchRecyclerAdapter searchRecyclerAdapter;
    private EditText searchField;
    private Button fetchButtonVar;
    private TextView name;
    private TextView description;
    private ImageView podImage;
    private String podName;
    private String podDesc;
    private String imgUrl;
    private List<RssFeedModel> mFeedModelList;
    RequestQueue requestQueue;
    private String url;
    private String searchString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchResultsView = findViewById(R.id.podcastSearchResults);
        searchRecyclerAdapter = new PodcastSearchRecyclerAdapter(searchResults);

        int resId = R.anim.layout_animation_fall_down;
        final Context context = searchResultsView.getContext();
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, resId);

        searchResultsView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchResultsView.setItemAnimator( new DefaultItemAnimator());
        searchResultsView.setLayoutAnimation(animation);
        searchResultsView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        searchResultsView.setAdapter(searchRecyclerAdapter);


        searchField = findViewById(R.id.searchField);
        requestQueue = Volley.newRequestQueue(this);
        searchString = null;

        searchResultsView.addOnItemTouchListener(new RecyclerTouchListener(this,
                searchResultsView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                Toast.makeText(MainActivity.this, "Single Click on position        :"+position,
                        Toast.LENGTH_SHORT).show();
                PodcastMetadata podCast = searchResults.get(position);
                Intent intent = new Intent(getBaseContext(), PodcastDetailScreen.class);
                intent.putExtra("PODCAST_NAME", podCast.getPodcastName());
                intent.putExtra("PODCAST_ARTIST", podCast.getPodcastArtist());
                intent.putExtra("PODCAST_ART", podCast.getPodoastArtUrl());
                intent.putExtra("PODCAST_FEEDURL", podCast.getPodcastFeedUrl());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, "Long press on position :"+position,
                        Toast.LENGTH_LONG).show();
            }
        }));


    }


    public void getRepoList(View view) {
        // First, we insert the username into the repo url.
        // The repo url is defined in GitHubs API docs (https://developer.github.com/v3/repos/).
        RequestQueue queue = Volley.newRequestQueue(this);
        this.url = "https://itunes.apple.com/search?media=podcast&term=";
        searchString = searchField.getText().toString();
        searchResults.clear();
        this.url = url + searchString;
        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        Integer numberOfResults = null;
                        JSONArray results = null;
                        JSONObject singleResult = null;
                        Integer i = 0;
                        ArrayList<String> podcastNames = new ArrayList<>();
                        JSONObject JSONresponse = response;
                        try {
                            numberOfResults = JSONresponse.getInt("resultCount");
                            results = JSONresponse.getJSONArray("results");
                            while (i<numberOfResults){
                                singleResult = results.getJSONObject(i);
                                String p_name = singleResult.getString("trackName");
                                String p_desc = singleResult.getString("primaryGenreName");
                                String p_art = singleResult.getString("artworkUrl100");
                                String p_artist = singleResult.getString("artistName");
                                String p_feedUrl = singleResult.getString("feedUrl");
                                podcastNames.add(p_name);
                                PodcastMetadata pod = null;
                                pod = new PodcastMetadata(p_name,p_desc,p_art,p_artist,p_feedUrl);
                                searchResults.add(pod);
                                i++;
                            }
                            searchRecyclerAdapter.notifyDataSetChanged();
                            searchResultsView.scheduleLayoutAnimation();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        i = 0;
                        podName = "";
                        while (i<podcastNames.size()){
                            podName = podName + '\n' + podcastNames.get(i);
                            i++;
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                name.setText("That didn't work!");
            }
        });


// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }


    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}
