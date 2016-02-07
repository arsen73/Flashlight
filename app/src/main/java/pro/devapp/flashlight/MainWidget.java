package pro.devapp.flashlight;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class MainWidget extends AppWidgetProvider {

    private static final String SYNC_CLICKED    = "automaticWidgetSyncButtonClick";

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main_widget);

        views.setOnClickPendingIntent(R.id.button, getPendingSelfIntent(context, SYNC_CLICKED));

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
        // TODO Auto-generated method stub
        super.onReceive(context, intent);

        RemoteViews remoteViews;
        ComponentName watchWidget;

        if (SYNC_CLICKED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            final boolean isCameraFlash = context.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.main_widget);
            watchWidget = new ComponentName(context, MainWidget.class);

            if(!CameraHolder.isProcess()){
                if(!CameraHolder.isOn()){
                    if(!isCameraFlash || !CameraHolder.hasFlash() || PreferenceHelper.get(context, "type", "led").equals("screen")){
                        Intent intent_new = new Intent(context, FullscreenActivity.class);
                        intent_new.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent_new);
                    } else {
                        remoteViews.setInt(R.id.button, "setImageResource", R.drawable.off);
                        OnTask task = new OnTask();
                        task.execute();
                    }
                } else {
                    remoteViews.setInt(R.id.button, "setImageResource", R.drawable.on);
                    CameraHolder.stop();
                    CameraHolder.release();
                }
            }
            appWidgetManager.updateAppWidget(watchWidget, remoteViews);
        }
    }


    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}

