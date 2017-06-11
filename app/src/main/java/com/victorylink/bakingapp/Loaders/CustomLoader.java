package com.victorylink.bakingapp.Loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;


class CustomLoader extends AsyncTaskLoader<String> {

    public CustomLoader(Context context) {
        super(context);
    }

    /**
     * here you will do anything you need in the work thread
     * */
    public String loadInBackground() {
        String result = null;
        // Load result string
        return result;
    }
}
