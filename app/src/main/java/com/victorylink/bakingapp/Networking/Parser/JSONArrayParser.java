package com.victorylink.bakingapp.Networking.Parser;

import com.google.gson.JsonArray;
import com.victorylink.bakingapp.Networking.Interfaces.CallBackJSONArray;
import com.victorylink.bakingapp.Networking.Interfaces.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aayman on 5/23/2017.
 */

public class JSONArrayParser implements Callback<JsonArray> {

    private final String baseURL;
    CallBackJSONArray callBackJSONObject;

    //=================================================================
    // constructor for creating calling object for network instance
    //=================================================================
    public JSONArrayParser(String baseUrl, CallBackJSONArray call) {
        this.callBackJSONObject = call;
        this.baseURL = baseUrl;
    }

    //=================================================================
    // below is all web api methods with return type jsonObject
    //=================================================================

    public void getBakingData(RetrofitInterface inter, String Url) {
        String url = baseURL + "/" + Url;
        Call<JsonArray> resString = inter.getDataArray(url);
        resString.enqueue(this);
    }

    //=================================================================
    // Response CallBack for success response
    //=================================================================
    @Override
    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
        callBackJSONObject.onSuccess(response);
    }

    //=================================================================
    // Response CallBack for error response
    //=================================================================
    @Override
    public void onFailure(Call<JsonArray> call, Throwable t) {
        callBackJSONObject.OnFail(t);
    }
}