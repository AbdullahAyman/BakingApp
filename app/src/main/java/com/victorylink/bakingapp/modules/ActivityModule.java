package com.victorylink.bakingapp.modules;

import android.app.Activity;

import com.victorylink.bakingapp.Interfaces.Scope.BakingApplicationScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aayman on 5/28/2017.
 */
@Module
public class ActivityModule {
    private Activity mContext;

    public ActivityModule(Activity context) {
        this.mContext = context;

    }

    @Provides
    @BakingApplicationScope
    @Named("activity_context")
    public Activity context() {
        return mContext;
    }

}
