package com.victorylink.bakingapp.Networking.Interfaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface RetrofitInterface {

    //=================================================================
    // getting data from web api method with GET Request Type
    // it's optional to add headers to Requested Method
    //=================================================================
    @Headers({
            "cache-control: no-cache",
            "content-type: application/json"})
    @GET
    Call<JsonObject> getData(@Url String url);

    //=================================================================
    // getting data from web api method with GET Request Type
    // it's optional to add headers to Requested Method
    //=================================================================
    @Headers({
            "cache-control: no-cache",
            "content-type: application/json"})
    @GET
    Call<JsonArray> getDataArray(@Url String url);

    //=================================================================
    // getting data from web api method with POST Request Type and send Data via url
    // it's optional to add headers to Requested Method
    //=================================================================
    @Headers({
            "cache-control: no-cache",
            "content-type: application/json"})
    @POST
    Call<JsonObject> postData(@Url String url);

    //=================================================================
    // getting data from web api method with POST Request Type and send Data via Request Body
    // it's optional to add headers to Requested Method
    //=================================================================
    @Headers({
            "cache-control: no-cache",
            "content-type: application/json"})
    @POST
    Call<JsonObject> postData(@Url String url, @Body JsonObject jsonObject);

    //=================================================================
    // uploading images/sound/videos/documents with multipart retrofit
    // it's optional to add headers to Requested Method
    //=================================================================
    @Multipart
    @POST
    Call<JsonObject> upload(@Url String url,
                              /*@Part("description") RequestBody description,*/
                            @Part MultipartBody.Part Video
    );
}