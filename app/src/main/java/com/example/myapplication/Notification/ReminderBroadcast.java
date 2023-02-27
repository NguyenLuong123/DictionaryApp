package com.example.myapplication.Notification;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.database.DatabaseOpenHelper;
import com.example.myapplication.model.Word;
import com.example.myapplication.view.FavouriteFragment;
import com.example.myapplication.view.Game2Fragment;
import com.example.myapplication.view.HomeFragment;
import com.example.myapplication.view.NotifyFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class ReminderBroadcast extends BroadcastReceiver {
//    private DatabaseOpenHelper databaseOpenHelper;
//    public static ArrayList<Word> ListWord = new ArrayList<>();
//
//
//    public ReminderBroadcast(DatabaseOpenHelper databaseOpenHelper){
//        this.databaseOpenHelper = databaseOpenHelper;
//    }



    @Override
    public void onReceive(Context context, Intent intent) {
        //ListWord = databaseOpenHelper.getListWord();
        int r = new Random().nextInt(FavouriteFragment.mWordList.size());

        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, r, resultIntent, PendingIntent.FLAG_MUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyLemubit")
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(FavouriteFragment.mWordList.get(r).getWord())
                .setContentText(FavouriteFragment.mWordList.get(r).getMean())
                .setContentIntent(resultPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(getNotificationId(), builder.build());
    }

    private int getNotificationId() {
        return (int) new Date().getTime();
    }
}
