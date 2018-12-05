package com.example.esther.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<Rugby>> {
    private ListView mListView;

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String BASE_URL="http://content.guardianapis.com/search";
    private static final int RUGBY_LOADER_ID = 1;
    private RugbyAdapter mAdapter;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView=findViewById(R.id.news_list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        mListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of rugby news as input
        mAdapter = new RugbyAdapter(this, new ArrayList<Rugby>());

        // Set the adapter
        // so the list can be populated in the user interface
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rugby currentRugby = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri rugbyUri = Uri.parse(currentRugby.getNewsUrl());

                // Create a new intent to view the rugby stories URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, rugbyUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);

            }
        });


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
       if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = android.support.v4.app.LoaderManager.getInstance(this);

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(RUGBY_LOADER_ID, null, this);

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }




    }
//Creates and returns a new loader instances if the loader id did not exist
    @NonNull
    @Override
    public android.support.v4.content.Loader<List<Rugby>> onCreateLoader(int i, @Nullable Bundle bundle) {
        Uri baseUri = Uri.parse(BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q","rugby");
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("api-key", "test");


        return new RugbyLoader(this,uriBuilder.toString());
    }
//called when the background task is completed
    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<List<Rugby>> loader, List<Rugby> rugbies) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No rugby news at the time!."
        mEmptyStateTextView.setText(R.string.no_rugby_news);

        // Clear the adapter of previous rugby news data
        mAdapter.clear();

        // If there is a valid list of the rugby news then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (rugbies != null && !rugbies.isEmpty()) {
            mAdapter.addAll(rugbies);

        }

    }
//Resets the loader to load a different data
    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<List<Rugby>> loader) {
        mAdapter.clear();

    }



}



