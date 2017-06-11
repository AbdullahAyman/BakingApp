package com.victorylink.bakingapp.modules;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.victorylink.bakingapp.Interfaces.Qualifier.ApplicationContext;
import com.victorylink.bakingapp.Interfaces.Scope.BakingApplicationScope;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by aayman on 5/25/2017.
 */
@Module(includes = {ContextModule.class, NetworkModule.class})
public class PicassoModule {

    @Provides
    @BakingApplicationScope
    public Picasso picasso(@ApplicationContext Context context, OkHttp3Downloader okHttp3Downloader) {
        return new Picasso.Builder(context)
                .downloader(okHttp3Downloader)
                .build();
    }

    @Provides
    @BakingApplicationScope
    public OkHttp3Downloader okHttp3Downloader(OkHttpClient okHttpClient) {
        return new OkHttp3Downloader(okHttpClient);
    }
}
