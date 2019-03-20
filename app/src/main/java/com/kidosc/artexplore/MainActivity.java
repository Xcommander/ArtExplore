package com.kidosc.artexplore;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mSendNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSendNotification=(Button)findViewById(R.id.send_notification);
        mSendNotification.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_notification:
                Log.d("xulinchao", "onClick: ");
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(this, null)
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("Hello")
                                .setContentText("Hi,come on");
                Intent resultIntent=new Intent(this,ResultActivity.class);
                TaskStackBuilder taskStackBuilder=TaskStackBuilder.create(this);
                taskStackBuilder.addParentStack(ResultActivity.class);
                taskStackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent=taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(resultPendingIntent);
                NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(1,builder.build());

                break;
            default:
                // TODO: 2019/3/20
                break;

        }
    }
}
