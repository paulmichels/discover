package com.cnam.vuzix;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class Widget extends AppWidgetProvider {
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        boolean isLightMode = isLightMode(context);

        if(appWidgetManager == null)
        {
            appWidgetManager = (AppWidgetManager)context.getSystemService(Context.APPWIDGET_SERVICE);
            appWidgetId = appWidgetManager.getAppWidgetIds(new ComponentName(context, Widget.class))[0];

        }

        RemoteViews views = new RemoteViews(context.getPackageName(),
                isLightMode ? R.layout.widget_light: R.layout.widget_dark);

        appWidgetManager.updateAppWidget(appWidgetId,views);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        updateAppWidget(context,null,0);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateAppWidget(context, appWidgetManager, appWidgetIds[0]);

    }

    private static boolean isLightMode(Context context)
    {
        return ((DynamicTheme)context.getApplicationContext()).isLightMode();
    }
}

