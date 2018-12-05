package com.example.esther.newsapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;


class RugbyLoader extends AsyncTaskLoader {
    private String mUrl;
    public RugbyLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public Object loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of news about rugby.
        List<Rugby> rugbies = NetworkConnect.getRugbyNews(mUrl);
        return rugbies;
    }


}

