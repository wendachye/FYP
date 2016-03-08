package com.example.wenda.tarucnfc;


import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;

import com.example.wenda.tarucnfc.Activitys.BaseActivity;
import com.example.wenda.tarucnfc.Activitys.MainActivity;
import com.example.wenda.tarucnfc.Activitys.NotificationActivity;
import com.example.wenda.tarucnfc.Fragments.BusRouteFragment;

import java.util.Calendar;
import java.util.Random;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    static final String NOTIFICATION_TYPE = "type";
    AlarmManager mAlarmManager;
    PendingIntent mPendingIntent;
    Calendar mCalendar;
    AudioManager myAudioManager;
    String mDeparture, mDestination, mRouteTime, mRouteDay;

    @Override
    public void onReceive(Context context, Intent intent) {
        int mReceivedID = Integer.parseInt(intent.getStringExtra(BaseActivity.EXTRA_REMINDER_ID));
        String type = intent.getStringExtra(NOTIFICATION_TYPE);
        mCalendar = Calendar.getInstance();

        mDeparture = BusRouteFragment.getBusDeparture();
        mDestination = BusRouteFragment.getBusDestination();
        mRouteDay = BusRouteFragment.getBusRouteDay();
        mRouteTime = BusRouteFragment.getBusRouteTime();

        // Create intent to open ReminderEditActivity on notification click
        Intent editIntent = new Intent(context, MainActivity.class);
        editIntent.putExtra(BaseActivity.KEY_SCHEDULE_ID, Integer.toString(1));
        PendingIntent mClick = PendingIntent.getActivity(context, 1, editIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int notificationId = new Random().nextInt(); // just use a counter in some util class...
        PendingIntent dismissIntent = NotificationActivity.getDismissIntent(notificationId, context);

        NotificationCompat.Builder mBuilder;
        // Create Notification
        mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.ic_bus)
                .setContentTitle("Upcoming Bus at " + mRouteTime)
                .setTicker(mDestination)
                    .setContentText("Bus to " + mDestination + " will arrive in 5 minutes")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(mClick)
                    .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                    .addAction(R.drawable.ic_close, "Dismiss", dismissIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mReceivedID, mBuilder.build());

    }

    public void setAlarm(Context context, Calendar calendar, int ID) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Put Reminder ID in Intent Extra
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(BaseActivity.EXTRA_REMINDER_ID, Integer.toString(ID));
        mPendingIntent = PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Calculate notification time
        Calendar c = Calendar.getInstance();
        long currentTime = c.getTimeInMillis();
        long diffTime = calendar.getTimeInMillis() - currentTime;

        // Start alarm using notification time
        mAlarmManager.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + diffTime,
                mPendingIntent);
    }

    public void cancelAlarm(Context context, int ID) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Cancel Alarm using Reminder ID
        mPendingIntent = PendingIntent.getBroadcast(context, ID, new Intent(context, AlarmReceiver.class), 0);
        mAlarmManager.cancel(mPendingIntent);
    }

}
