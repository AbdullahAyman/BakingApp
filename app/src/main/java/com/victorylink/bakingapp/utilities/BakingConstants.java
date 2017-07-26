package com.victorylink.bakingapp.utilities;


import com.victorylink.bakingapp.DataModel.BackingResponse;

import java.util.ArrayList;

public class BakingConstants {

    /**
     * constant values for SharedPreference keys
     */
    public static String USER_NAME = "userName";

    /**
     * constant values for SavedInstance keys
     */
    public static String BAKING_ARRAY_LIST = "BakingArrayList";
    public static String BAKING_ITEM_LIST = "BakingItemList";
    public static String BAKING_RECYCLER_INDEX = "recyclerIndex";
    public static String CURRENT_FRAGMENT = "currentFragment";
    public static String CURRENT_SELECTED_ITEM = "selectedItem";
    public static String CURRENT_PLAYER_PERIOD = "periodIndex";
    public static String LAST_VISITED_ID = "id";
    public static String VIDEO_ID = "video_Id";

    public static ArrayList<BackingResponse> mBackingResponse = new ArrayList<BackingResponse>();

}
