package com.firstapp.manunya.instagramclient;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

// resource: http://scriptedpapers.com/2013/11/26/android-pass-arraylist-of-custom-class-via-intent/
public class CommentsActivity extends ActionBarActivity {
    private ArrayList<Comment> comments;
    private CommentAdapter aComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        // Get the Bundle Object
        Bundle bundleObject = getIntent().getExtras();
        comments = (ArrayList<Comment>) bundleObject.getSerializable("comments");
        aComments = new CommentAdapter(this, comments);
        ListView lvComments = (ListView) findViewById(R.id.lvComments);
        lvComments.setAdapter(aComments);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comments, menu);
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

    public void onGoBack(View view) {
        finish();
    }
}
