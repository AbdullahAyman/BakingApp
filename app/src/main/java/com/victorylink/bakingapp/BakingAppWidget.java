package com.victorylink.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.victorylink.bakingapp.DataModel.BackingResponse;
import com.victorylink.bakingapp.Networking.Interfaces.CallBackJSONArray;
import com.victorylink.bakingapp.Networking.Parser.JSONArrayParser;
import com.victorylink.bakingapp.Views.RecipeActivityView;
import com.victorylink.bakingapp.utilities.BakingApp;
import com.victorylink.bakingapp.utilities.BakingConstants;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Response;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        String bakingName = "", Serving = "", imageUrl = "";
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cooking);
        if (BakingApp.index < BakingConstants.mBackingResponse.size()) {
            BakingApp.index++;
        } else {
            BakingApp.index = 0;
        }
        BackingResponse backingResponse = BakingConstants.mBackingResponse.get(BakingApp.index);
        if (BakingConstants.mBackingResponse.size() > 0) {
            bakingName = backingResponse.getName();
            Serving = backingResponse.getServings() + " People";
            imageUrl = backingResponse.getImage();
            if (imageUrl.length() > 0)
                try {
                    bitmap = Picasso.with(context)
                            .load(imageUrl)
                            .placeholder(R.drawable.cooking)
                            .get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
        Log.d("bakingName", "updateAppWidget: " + bakingName, null);
        views.setTextViewText(R.id.card_bake_name, bakingName);
        views.setTextViewText(R.id.card_bake_description, "Serving: " + Serving);

        views.setImageViewBitmap(R.id.img_card, bitmap);
        Intent intent = new Intent(context, RecipeActivityView.class);
        intent.putExtra(BakingConstants.BAKING_ITEM_LIST, backingResponse);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.card_view, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            if (BakingConstants.mBackingResponse.size() > 0)
                updateAppWidget(context, appWidgetManager, appWidgetId);
            else
                getBakingData(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private void getBakingData(final Context context, final AppWidgetManager appWidgetManager,
                               final int appWidgetId) {
        String baseURL = context.getResources().getString(R.string.baseURL);
        String functionBaking = context.getResources().getString(R.string.functionBaking);

        JSONArrayParser jsonObjectParser = new JSONArrayParser(baseURL, new CallBackJSONArray() {
            @Override
            public void onSuccess(Response<JsonArray> o) {
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<BackingResponse>>() {
                }.getType();
                try {
                    ArrayList<BackingResponse> mResponse = gson.fromJson(o.body(), listType);
                    BakingConstants.mBackingResponse = mResponse;
                    updateAppWidget(context, appWidgetManager, appWidgetId);
                } catch (Exception e) {
                }
            }

            @Override
            public void OnFail(Throwable o) {

            }
        });
        jsonObjectParser.getBakingData(BakingApp.newInstance().newRetrofitRequest(), functionBaking);
    }
}

