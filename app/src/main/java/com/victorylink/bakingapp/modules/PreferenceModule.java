package com.victorylink.bakingapp.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.victorylink.bakingapp.Interfaces.Qualifier.ApplicationContext;
import com.victorylink.bakingapp.Interfaces.Scope.BakingApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aayman on 5/25/2017.
 */
@Module(includes = ContextModule.class)
public class PreferenceModule {

    @Provides
    @BakingApplicationScope
    public SharedPreferences getBakingSharedPreference(@ApplicationContext Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
