package com.victorylink.bakingapp.utilities;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import com.squareup.picasso.Picasso;
import com.victorylink.bakingapp.Interfaces.Component.BakingAppComponent;
import com.victorylink.bakingapp.Interfaces.Component.DaggerBakingAppComponent;
import com.victorylink.bakingapp.Networking.Interfaces.RetrofitInterface;
import com.victorylink.bakingapp.R;
import com.victorylink.bakingapp.modules.ContextModule;

import timber.log.Timber;


public class BakingApp extends Application {
    public static int index = -1;//set to -1 to load first item when incremented
    private static String BASE_URL = "";
    private static RetrofitInterface retrofitInterface;
    BakingAppComponent daggerBakingAppComponent;
    private SharedPreferences sharedPreferences;
    private Picasso picasso;

    public static BakingApp get(Activity context) {
        return (BakingApp) context.getApplication();
    }
    public static BakingApp newInstance() {
        return new BakingApp();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BASE_URL = getResources().getString(R.string.baseURL);
        Timber.plant(new Timber.DebugTree());
        daggerBakingAppComponent = DaggerBakingAppComponent.builder()
                .contextModule(new ContextModule(getApplicationContext(), BASE_URL))//we can include module with constructors only
                /*.networkModule(new NetworkModule())
                .picassoModule(new PicassoModule())   // we cam delete all non Construction modules
                .retrofitModule(new RetrofitModule())*/
                .build();
        retrofitInterface = daggerBakingAppComponent.getRetrofitInterface();
        picasso = daggerBakingAppComponent.getPicasso();
        sharedPreferences = daggerBakingAppComponent.getSharedPreferences();

    }

    public BakingAppComponent getBakingAppComponent() {
        return daggerBakingAppComponent;
    }

    /**
     * will create instance of network connection
     */
    public RetrofitInterface newRetrofitRequest() {
        /*Retrofit retrofit = null;
        RetrofitInterface retrofitInterface = null;

        retrofit = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);*/
        return retrofitInterface;
    }

    public Picasso getPicasso() {
        return picasso;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
