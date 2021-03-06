package com.firstapp.manunya.instagramclient;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends ActionBarActivity {
    public static final String CLIENT_ID = "a216283905df43c2a3b6ad46adc0be62";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotoAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPopularPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // send out API request to popular photo
        // using async http library make this much easier
        photos = new ArrayList<>();
        // create the adapter and attach data source to it
        aPhotos = new InstagramPhotoAdapter(this, photos);
        // find the ListView
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        //set the adapter binding to the ListView
        lvPhotos.setAdapter(aPhotos);
        //get the popular photos
        fetchPopularPhotos();
    }

        public void  fetchPopularPhotos() {
        /*
        Client ID: a216283905df43c2a3b6ad46adc0be62
        Popular API: https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
        API map
            Type: {"data" => [x] => "type" } ("image" or "video")
            URL: {"data" => [x] => "images" => "standard_resolution" => "url"}
            Caption: {"data" => [x] => "caption" => "text"}
            Author name: {"data" => [x] => "user" => "username"}
         */
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        // create the network client
        AsyncHttpClient client = new AsyncHttpClient();
        // trigger the get request
        client.get(url, null, new JsonHttpResponseHandler() {
            // onSuccess (worked, 200)
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //expect JSON object (refer to API map above)
                //Log.i("DEBUG", response.toString());
                //clear out old items in case of refresh
                aPhotos.clear();
                //Iterate each of the photo items and decode into an object
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data"); //arrays of post
                    for(int i = 0; i < photosJSON.length(); i++) {
                        //get JSON obj at ith position
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.setData(photoJSON);
                        //add to arraylist
                        photos.add(photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //callback
                aPhotos.notifyDataSetChanged();
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
            }

            // onFailure (fail)
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
