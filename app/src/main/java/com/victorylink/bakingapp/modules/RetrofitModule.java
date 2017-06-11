package com.victorylink.bakingapp.modules;

import com.victorylink.bakingapp.Interfaces.Scope.BakingApplicationScope;
import com.victorylink.bakingapp.Networking.Interfaces.RetrofitInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by aayman on 5/25/2017.
 */
@Module(includes = NetworkModule.class)
public class RetrofitModule {

    @Provides
    @BakingApplicationScope
    public Retrofit getRetrofit(OkHttpClient okHttpClient, String baseurl) {
        return new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @BakingApplicationScope
    public RetrofitInterface getRetrofitInterface(Retrofit getRetrofit) {

        return getRetrofit.create(RetrofitInterface.class);
    }

}
