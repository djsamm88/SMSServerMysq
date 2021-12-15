package pakpak.kominfo.smsservermysql;

/**
 * Created by dinaskominfokab.pakpakbharat on 20/03/18.
 */


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.ContentValues.TAG;

/**
 * Created by dinaskominfokab.pakpakbharat on 07/03/18.
 */

public class PushPesan extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage message)
    {
        setNotif(message.getNotification().getTitle(), message.getNotification().getBody(), this);

        Log.d(TAG, "From: " + message.getFrom());
        Log.d(TAG, "Data: " + message.getData());
        Log.d(TAG, "Notif: " + message.getNotification().getTitle());
        Log.d(TAG, "ISI: " + message.getNotification().getBody());
        Log.d(TAG,"SEMUA:"+message.getData().get("image"));


    }





    public void setNotif(String judul, String isi, Context context)
    {

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        /****** sound *******/
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(judul)
                .setContentText(isi)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setSound(uri)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, mBuilder.build());
    }
}
