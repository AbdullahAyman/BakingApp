package com.victorylink.bakingapp.modules;

import android.content.Context;

import com.victorylink.bakingapp.Interfaces.Qualifier.ApplicationContext;
import com.victorylink.bakingapp.Interfaces.Scope.BakingApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aayman on 5/25/2017.
 */
@Module
public class ContextModule {
    private Context context;
    private String baseURL;

    public ContextModule(Context contxt, String baseurl) {
        this.context = contxt.getApplicationContext();
        this.baseURL = baseurl;
    }

    @Provides
    @BakingApplicationScope
    @ApplicationContext
    public Context context() {
        return context;
    }

    @Provides
    @BakingApplicationScope
    public String baseurl() {
        return baseURL;
    }
}
