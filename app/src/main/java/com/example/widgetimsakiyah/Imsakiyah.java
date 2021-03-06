package com.example.widgetimsakiyah;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.imsakiyah);
        Gson gson = new Gson();
        Type type = new TypeToken<List<ModelData>>() {
        }.getType();
        String wilayah = intent.getStringExtra("location");
        String listWaktu = intent.getStringExtra("waktu");
        List<ModelData> dataList = gson.fromJson(listWaktu, type);
        if (dataList != null) {
            views.setTextViewText(R.id.waktu, String.format("%s/ %s", dataList.get(0).getDateMasehi(), dataList.get(0).getDateHijriah()));
            for (ModelDetailData data : dataList.get(0).getWaktu()) {
                views.setTextViewText(R.id.txtTimeSubuh, data.getSubuh());
                views.setTextViewText(R.id.txtTimeImsak, data.getImsak());
                views.setTextViewText(R.id.txtTimeDhuhur, data.getDzuhur());
                views.setTextViewText(R.id.txtTimeAshar, data.getAshar());
                views.setTextViewText(R.id.txtTimeMagrib, data.getMaghrib());
                views.setTextViewText(R.id.txtTimeIsya, data.getIsya());
            }
        }

        if (wilayah != null) {
            views.setTextViewText(R.id.wilayah, wilayah);
            Log.e("location", wilayah);
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

