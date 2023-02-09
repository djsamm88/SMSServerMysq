package pakpak.kominfo.smsservermysql;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import android.telephony.SmsMessage;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import pakpak.kominfo.smsservermysql.database.DbInbox;
import pakpak.kominfo.smsservermysql.database.DbUser;
import pakpak.kominfo.smsservermysql.model.ModelKirim;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * Created by dinaskominfokab.pakpakbharat on 14/06/18.
 */
public class SmsListener extends BroadcastReceiver {

    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub



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




    private void inbox_simpan(String nomor,String pesan,String generated_id,String email,Context context)
    {



        try {

            RequestQueue queue = Volley.newRequestQueue(context);
            String url = new ConfigServer(context).get_url_local_inbox_simpan()+"&nomor=" + nomor+"&pesan="+pesan+"&generated_id="+generated_id+"&email="+email;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("respon_simpan_inbox_ok", response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(getApplicationContext(), token,Toast.LENGTH_LONG).show();
                    Log.d("simpan_inbox_err1", error.toString());
                }
            });
            queue.add(stringRequest);

        }catch (Exception e)
        {
            Log.d("simpan_inbox_err2",e.toString());
        }

    }
}