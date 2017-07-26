package com.victorylink.bakingapp.Interfaces;


import com.victorylink.bakingapp.DataModel.BackingResponse;

import java.util.ArrayList;

public interface BakingView {
    void showProgress();

    void hideProgress();

    void assignAdapterData(ArrayList<BackingResponse> mResponse);

    void loadSelectedItem(BackingResponse mBackingResponse);

    void failerLoadingData();
}
