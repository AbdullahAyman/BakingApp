package com.victorylink.bakingapp.modules;

import android.content.Context;

import com.victorylink.bakingapp.Interfaces.Scope.BakingRecyclerFragmentViewScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aayman on 5/29/2017.
 */
@Module
public class BakingRecyclerFragmentViewModule {
    private Context bakingActivityView;

    public BakingRecyclerFragmentViewModule(Context activityView) {
        this.bakingActivityView = activityView;

    }

    @Provides
    @BakingRecyclerFragmentViewScope
    public Context bakingContext() {
        return bakingActivityView;
    }
    /**
     * We must provide each item as a dependency to be injected to recycler
     * */
}
