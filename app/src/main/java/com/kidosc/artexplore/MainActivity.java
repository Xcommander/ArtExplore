package com.kidosc.artexplore;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mSendNotification;
    private final static String CHANNEL_ID = "ArtExplore";
    private int mId = 0x212;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSendNotification = findViewById(R.id.send_notification);
        mSendNotification.setOnClickListener(this);
        findViewById(R.id.relative_widget).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        /**
         * 初始化
         */
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Hello")
                        .setContentText("Hi,come on")
                        .setAutoCancel(true);
        Intent resultIntent = new Intent(this, ResultActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(ResultActivity.class);
        taskStackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        switch (v.getId()) {
            case R.id.send_notification:
                builder.setContentIntent(resultPendingIntent);
                notificationManager.notify(mId, builder.build());
                break;
            case R.id.relative_widget:
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
                remoteViews.setTextViewText(R.id.msg, getString(R.string.remote_view));
                remoteViews.setImageViewResource(R.id.icon, R.drawable.voice_changer);
                remoteViews.setOnClickPendingIntent(R.id.open_activity2, resultPendingIntent);
                builder.setCustomContentView(remoteViews);
                notificationManager.notify(mId, builder.build());

            default:
                // TODO: 2019/3/20
                break;

        }
    }
}
