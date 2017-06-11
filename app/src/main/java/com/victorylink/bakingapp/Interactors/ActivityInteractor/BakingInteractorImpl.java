package com.victorylink.bakingapp.Interactors.ActivityInteractor;

import com.google.gson.JsonArray;
import com.victorylink.bakingapp.Interfaces.BakingInteractor;
import com.victorylink.bakingapp.Networking.Interfaces.CallBackJSONArray;
import com.victorylink.bakingapp.Networking.Parser.JSONArrayParser;
import com.victorylink.bakingapp.utilities.BakingApp;

import retrofit2.Response;

/**
 * here you will find implementation for connections, loading data, query DB and parsing it
 */
public class BakingInteractorImpl implements BakingInteractor, CallBackJSONArray {
    CallBackJSONArray mCallBackJSONArray;


    /**
     * will called from presenter impl. to validate getBakingData via server connection
     */
    @Override
    public void getBakingData(String url, CallBackJSONArray listener, String baseURl) {
        mCallBackJSONArray = listener;
        JSONArrayParser jsonObjectParser = new JSONArrayParser(baseURl, this);
        jsonObjectParser.getBakingData(BakingApp.newInstance().newRetrofitRequest(), url);
    }

    /**
     * passing response back to presenter when success response
     */
    @Override
    public void onSuccess(Response<JsonArray> o) {
        mCallBackJSONArray.onSuccess(o);
    }

    /**
     * passing response back to presenter when fail response
     */
    @Override
    public void OnFail(Throwable o) {
        mCallBackJSONArray.OnFail(o);
    }
}
