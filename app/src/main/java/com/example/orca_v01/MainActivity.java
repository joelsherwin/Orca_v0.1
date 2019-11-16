package com.example.orca_v01;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
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

        name  = findViewById(R.id.resultView);
        searchField = findViewById(R.id.searchField);
        requestQueue = Volley.newRequestQueue(this);
        searchString = null;


    }


    public void getRepoList(View view) {
        // First, we insert the username into the repo url.
        // The repo url is defined in GitHubs API docs (https://developer.github.com/v3/repos/).
        RequestQueue queue = Volley.newRequestQueue(this);
        this.url = "https://itunes.apple.com/search?media=podcast&term=";
        searchString = searchField.getText().toString();
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
                                String a = singleResult.getString("trackName");
                                podcastNames.add(a);
                                i++;

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        i = 0;
                        podName = "";
                        while (i<podcastNames.size()){
                            podName = podName + '\n' + podcastNames.get(i);
                            i++;
                        }
                        name.setText("Number of results : "+ numberOfResults.toString() + '\n' + podName);
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

}
