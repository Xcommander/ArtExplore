package com.kidosc.artexplore;

import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity {
    private Button mSendNotification;
    private final static String CHANNEL_ID = "ArtExplore";
    private int mId = 0x212;
    private Button mAnimation;
    private ImageView mImageView;
    private Button mStartAnim;
    private Button mAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSendNotification = findViewById(R.id.send_notification);
        mSendNotification.setOnClickListener(this::onClick);
        mSendNotification.setBackground(getDrawable(R.drawable.levellisttest));
        mSendNotification.getBackground().setLevel(1);
        mSendNotification.setBackground(getDrawable(R.drawable.transitiontest));
        TransitionDrawable transitionDrawable = (TransitionDrawable) mSendNotification.getBackground();
        transitionDrawable.startTransition(1000);

        mAnimation = findViewById(R.id.relative_widget);
        mAnimation.setOnClickListener(this::onClick);
        mImageView = findViewById(R.id.clip_image);
        mImageView.getDrawable().setLevel(1000);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation_test);
        mAnimation.startAnimation(animation);
        mImageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.alpha_test));

        mSendNotification.setBackgroundResource(R.drawable.animation_list);
        ((AnimationDrawable) mSendNotification.getBackground()).start();
        mStartAnim = findViewById(R.id.start_anim);
        mStartAnim.setOnClickListener(this::onClick);

        //属性动画
        mAnimator = findViewById(R.id.animator);
        mAnimator.setOnClickListener(this::onClick);

    }

    /**
     * 属性动画---objectAnimator
     * 封装一个扩展类，动画部分在setXX()方法做处理,xx是指propertyName
     */
    private void performAnimate() {
        ViewWrapper viewWarpper = new ViewWrapper(mAnimator);
        ObjectAnimator.ofInt(viewWarpper, "width", 500).setDuration(5000).start();

    }

    private class ViewWrapper {
        private View mTarget;

        public ViewWrapper(View mTarget) {
            this.mTarget = mTarget;
        }

        private int getWidth() {
            return mTarget.getLayoutParams().width;
        }

        private void setWidth(int width) {
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }
    }

    /**
     * valueAnimator
     *
     * @param target
     * @param start
     * @param end
     */
    private void performAnimate(View target, int start, int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);
        valueAnimator.addUpdateListener((animator) -> {
            float fraction = animator.getAnimatedFraction();
            //估值器
            IntEvaluator evaluator = new IntEvaluator();
            target.getLayoutParams().width = evaluator.evaluate(fraction, start, end);
            target.requestLayout();

        });
        valueAnimator.setDuration(5000).start();

    }


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
            case R.id.start_anim:
                startActivity(new Intent(this, ResultActivity.class));
                overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                break;
            case R.id.animator:
                performAnimate(mAnimator, mAnimator.getWidth(), 500);
                break;

            default:
                // TODO: 2019/3/20
                break;

        }
    }
}
