package com.victorylink.bakingapp.Networking.Parser;

import com.google.gson.JsonObject;
import com.victorylink.bakingapp.Networking.Interfaces.CallBackJSONObject;
import com.victorylink.bakingapp.Networking.Interfaces.RetrofitInterface;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//=================================================================
// this class contains all functions with return type JsonObject
//=================================================================
public class JSONObjectParser implements Callback<JsonObject> {

    private final String baseURL;
    CallBackJSONObject callBackJSONObject;

    //=================================================================
    // constructor for creating calling object for network instance
    //=================================================================
    public JSONObjectParser(String baseUrl, CallBackJSONObject call) {
        this.callBackJSONObject = call;
        this.baseURL = baseUrl;
    }

    //=================================================================
    // below is all web api methods with return type jsonObject
    //=================================================================

    public void getBakingData(RetrofitInterface inter, String Url) {
        String url = baseURL + "/"+Url;
        Call<JsonObject> resString = inter.getData(url);
        resString.enqueue(this);
    }

    public void sendRecordedVoice(RetrofitInterface inter, String textMessage,/* RequestBody description,*/ MultipartBody.Part Video) {
        String url = baseURL;
        Call<JsonObject> objectRequest = inter.upload(url, /*description,*/ Video);
        objectRequest.enqueue(this);
    }

    public void sendPassportImage(RetrofitInterface inter, MultipartBody.Part Video, String url) {
        Call<JsonObject> objectRequest = inter.upload(url, /*description,*/ Video);
        objectRequest.enqueue(this);
    }

    //=================================================================
    // Response CallBack for success response
    //=================================================================
    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        callBackJSONObject.onSuccess(response);
    }

    //=================================================================
    // Response CallBack for error response
    //=================================================================
    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        callBackJSONObject.OnFail(t);
    }
}
