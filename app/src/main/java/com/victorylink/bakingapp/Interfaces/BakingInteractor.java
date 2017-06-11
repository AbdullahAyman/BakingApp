
package com.victorylink.bakingapp.Interfaces;


import com.victorylink.bakingapp.Networking.Interfaces.CallBackJSONArray;

public interface BakingInteractor {

    void getBakingData(String url, CallBackJSONArray listener, String baseUrl);
}
