package com.victorylink.bakingapp.Presenters.ActivityPresenter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.victorylink.bakingapp.DataModel.BackingResponse;
import com.victorylink.bakingapp.Interactors.ActivityInteractor.BakingInteractorImpl;
import com.victorylink.bakingapp.Interfaces.BakingInteractor;
import com.victorylink.bakingapp.Interfaces.BakingPresenter;
import com.victorylink.bakingapp.Interfaces.BakingView;
import com.victorylink.bakingapp.Networking.Interfaces.CallBackJSONArray;
import com.victorylink.bakingapp.utilities.BakingConstants;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Response;

/**
 * here you will find managing connection between Model and view
 */
public class BakingPresenterImpl implements BakingPresenter, CallBackJSONArray {

    private BakingView bakingView;
    private BakingInteractor bakingInteractor;

    public BakingPresenterImpl(BakingView bakingView) {
        this.bakingView = bakingView;
        //instance for handling main functionality like connecting server
        this.bakingInteractor = new BakingInteractorImpl();

    }

    /**
     * will called when user click getBakingData button to validate username and password
     */
    @Override
    public void loadDataList(String url, String baseUrl) {
        if (bakingView != null) {
            bakingView.showProgress();
        }

        bakingInteractor.getBakingData(url, this, baseUrl);
    }

    /**
     * when destroy view
     */
    @Override
    public void onDestroy() {
        bakingView = null;
    }


    /**
     * will called when user successfully logged in
     */
    @Override
    public void onSuccess(Response<JsonArray> o) {
        if (bakingView != null) {
            bakingView.hideProgress();
        }

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<BackingResponse>>() {
        }.getType();
        try {
            ArrayList<BackingResponse> mResponse = gson.fromJson(o.body(), listType);
            bakingView.assignAdapterData(mResponse);
            BakingConstants.mBackingResponse = mResponse;
        } catch (Exception e) {
        }
    }

    /**
     * will called when user failed getBakingData
     */
    @Override
    public void OnFail(Throwable o) {
        if (bakingView != null) {
            bakingView.hideProgress();
        }
    }

}
