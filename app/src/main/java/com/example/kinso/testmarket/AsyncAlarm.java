package com.example.kinso.testmarket;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by kinso on 2019-02-18.
 */

public class AsyncAlarm extends AsyncTask<Void,Void,Void> {
    private Context activity;
    public AsyncAlarm(Context activity){
        this.activity = activity;
    }
    @Override
    protected Void doInBackground(Void... voids){
        Intent intent = new Intent(activity,ActivitySendMessage.class);
        Bitmap mLargeIconForNoti = BitmapFactory.decodeResource(activity.getResources(), R.drawable.market_app_icon);
        PendingIntent mPendingIntent =PendingIntent.getActivity(activity,0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(activity)
                        .setSmallIcon(R.drawable.market_app_icon)
                        .setContentTitle("쪽지메시지")
                        .setContentText("알림 내용.")
                        .setLargeIcon(mLargeIconForNoti)
                        .setDefaults(Notification.DEFAULT_VIBRATE)//진동 알림
                        .setAutoCancel(true)//푸시 알림창을 사용자가 터치하면 자동사라짐(true)
                        .setContentIntent(mPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0,mBuilder.build());
        return null;
    }
}
