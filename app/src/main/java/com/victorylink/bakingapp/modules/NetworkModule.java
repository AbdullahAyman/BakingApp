package com.victorylink.bakingapp.modules;

import android.content.Context;

import com.victorylink.bakingapp.Interfaces.Qualifier.ApplicationContext;
import com.victorylink.bakingapp.Interfaces.Scope.BakingApplicationScope;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

/**
 * Created by aayman on 5/25/2017.
 */
@Module(includes = ContextModule.class)
public class NetworkModule {
    @Provides
    @BakingApplicationScope

    public Cache cache(File cachFile) {
        return new Cache(cachFile, 10 * 1000 * 1000);
    }

    @Provides
    @BakingApplicationScope

    public File cachFile(@ApplicationContext Context context) {
        return new File(context.getCacheDir(), "okhttp-cache");
    }

    @Provides
    @BakingApplicationScope

    public HttpLoggingInterceptor loggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Timber.i(message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return loggingInterceptor;
    }

    @Provides
    @BakingApplicationScope

    public OkHttpClient okHttpClient(HttpLoggingInterceptor loggingInterceptor, Cache cache) {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .cache(cache)
                .build();
    }
}
