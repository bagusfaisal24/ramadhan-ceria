package com.example.widgetimsakiyah;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class Imsakiyah extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.imsakiyah);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
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

    @Override
    public void onReceive(Context context, Intent intent) {
        String s = intent.getStringExtra("location");
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.imsakiyah);
        if (s != null) {
            views.setTextViewText(R.id.wilayah, s);
            Log.e("location", s);
        } else {
            views.setTextViewText(R.id.wilayah, "");
        }
        AppWidgetManager appWidgetManager = AppWidgetManager
                .getInstance(context);
        appWidgetManager.updateAppWidget(new ComponentName(context,
                Imsakiyah.class), views);

        super.onReceive(context, intent);
    }
}

