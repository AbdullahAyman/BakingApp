package com.victorylink.bakingapp.modules;

import android.content.Context;

import com.victorylink.bakingapp.Interfaces.Scope.RecipeRecyclerFragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aayman on 5/30/2017.
 */
@Module
public class RecipeRecyclerFragmentModule {
    private Context context;

    public RecipeRecyclerFragmentModule(Context activityView) {
        this.context = activityView;

    }

    @Provides
    @RecipeRecyclerFragmentScope
    public Context recipeContext() {
        return context;
    }

}
