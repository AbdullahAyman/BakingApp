package com.victorylink.bakingapp.Views.BaseViews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(getLayoutResourceId(), container, false);
    }

    /**
     * Get the Layout Resource Id from each fragment extends the BaseFragment
     */
    protected abstract int getLayoutResourceId();

    /**
     * Get the Fragment Name for each fragment extends the BaseFragment
     */
    protected abstract String getFragmentName();

    /**
     * Update The actionBar Title for each fragment
     */
}
