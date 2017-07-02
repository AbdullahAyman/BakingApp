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

import com.squareup.picasso.Picasso;
import com.victorylink.bakingapp.Views.RecipeActivityView;
import com.victorylink.bakingapp.utilities.BakingConstants;

import java.io.IOException;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        String bakingName = "", description = "", imageUrl = "";
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cooking);
        if (BakingConstants.index < BakingConstants.mBackingResponse.size()) {
            BakingConstants.index++;
        } else {
            BakingConstants.index = 0;
        }
        if (BakingConstants.mBackingResponse.size() > 0) {
            bakingName = BakingConstants.mBackingResponse.get(BakingConstants.index).getName();
            description = BakingConstants.mBackingResponse.get(BakingConstants.index).getServings() + "";
            imageUrl = BakingConstants.mBackingResponse.get(BakingConstants.index).getImage();
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

        views.setTextViewText(R.id.appwidget_text, bakingName);
        /*views.setTextViewText(R.id.card_bake_name, bakingName);
        views.setTextViewText(R.id.card_bake_description, description);


        views.setImageViewBitmap(R.id.img_card, bitmap);*/
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

        Intent intent = new Intent(context, RecipeActivityView.class);
        // intent.putExtra(BakingConstants.BAKING_ITEM_LIST, BakingConstants.mBackingResponse.get(BakingConstants.index));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.card_view, pendingIntent);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            if (BakingConstants.mBackingResponse.size() > 0)
                updateAppWidget(context, appWidgetManager, appWidgetId);
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
}

