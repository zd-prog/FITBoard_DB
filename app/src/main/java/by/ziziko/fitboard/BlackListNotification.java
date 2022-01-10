package by.ziziko.fitboard;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import by.ziziko.fitboard.Activities.MainActivity;

public class BlackListNotification {

    private static final String CHANNEL_ID = "1";
    private static final int NOTIFY_ID = 100;

    @RequiresApi(api = Build.VERSION_CODES.O)
    static public void Notification(String login, Activity activity, NotificationManager notificationManager){
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(activity,CHANNEL_ID)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.message)
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(pendingIntent)
                        .setContentTitle("Пользователь " + login + " был добавлен в чёрный список")
                        .setContentText("Теперь он не может добавлять новости")
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setChannelId(CHANNEL_ID);
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Channel title",
                NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(NOTIFY_ID,notificationBuilder.build());

    }
}
