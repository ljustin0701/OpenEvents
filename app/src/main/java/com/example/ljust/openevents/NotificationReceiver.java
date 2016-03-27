package com.example.ljust.openevents;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ljust on 03/26/2016.
 */

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int requestCode = intent.getIntExtra(MeetupFragment.NOTIFICATION_ID, 0);
        Notification notif = intent.getParcelableExtra(MeetupFragment.NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(requestCode, notif);
    }
}

