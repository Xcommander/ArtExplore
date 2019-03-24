package com.kidosc.artexplore.UI;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.kidosc.artexplore.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    private static final String ACTION_CLICK = "com.kidosc.arrexplore.click";
    private static final String TAG = "NewAppWidget";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.d(TAG, "updateAppWidget:id =  " + appWidgetId);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        Intent intent = new Intent();
        intent.setAction(ACTION_CLICK);
        intent.setComponent(new ComponentName(context,NewAppWidget.class));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 2, intent,  PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.imageview1, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int counter = appWidgetIds.length;
        Log.i(TAG, "counter = " + counter);
        for (int i = 0; i < counter; i++) {
            int appWidgetId = appWidgetIds[i];
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
        super.onReceive(context, intent);
        Log.d(TAG, "onReceive: action = " + intent.getAction());
        if (intent.getAction().equals(ACTION_CLICK)) {
            Toast.makeText(context, "clicked it", Toast.LENGTH_SHORT).show();
            ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue(1024), Thread::new, new ThreadPoolExecutor.AbortPolicy());

            singleThreadPool.execute(() -> {
                Bitmap srcBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon1);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                for (int i = 1; i < 37; i++) {
                    float degree = (i * 10) % 360;
                    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
                    remoteViews.setImageViewBitmap(R.id.imageview1, rotateBitmap(context, srcBitmap, degree));
                    Intent clickIntent = new Intent();
                    clickIntent.setAction(ACTION_CLICK);
                    clickIntent.setComponent(new ComponentName(context,NewAppWidget.class));
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 2, intent,  PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteViews.setOnClickPendingIntent(R.id.imageview1, pendingIntent);
                    appWidgetManager.updateAppWidget(new ComponentName(context,NewAppWidget.class), remoteViews);
                    SystemClock.sleep(30);

                }


            });
            singleThreadPool.shutdown();

        }
    }

    private Bitmap rotateBitmap(Context context, Bitmap srcBitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
        return bitmap;

    }
}

