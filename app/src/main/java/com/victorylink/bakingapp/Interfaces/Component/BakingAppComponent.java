package com.victorylink.bakingapp.Interfaces.Component;

/**
 * Created by aayman on 5/25/2017.
 */

import android.content.SharedPreferences;

import com.squareup.picasso.Picasso;
import com.victorylink.bakingapp.Interfaces.Scope.BakingApplicationScope;
import com.victorylink.bakingapp.Networking.Interfaces.RetrofitInterface;
import com.victorylink.bakingapp.modules.ActivityModule;
import com.victorylink.bakingapp.modules.PicassoModule;
import com.victorylink.bakingapp.modules.PreferenceModule;
import com.victorylink.bakingapp.modules.RetrofitModule;

import dagger.Component;

@Component(modules = {RetrofitModule.class, PicassoModule.class, PreferenceModule.class, ActivityModule.class})
@BakingApplicationScope
public interface BakingAppComponent {
    Picasso getPicasso();

    RetrofitInterface getRetrofitInterface();

    SharedPreferences getSharedPreferences();
}
