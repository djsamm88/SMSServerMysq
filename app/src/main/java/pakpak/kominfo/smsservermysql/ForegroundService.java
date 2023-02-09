package pakpak.kominfo.smsservermysql;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Random;

import pakpak.kominfo.smsservermysql.database.DbInbox;
import pakpak.kominfo.smsservermysql.database.DbUser;
import pakpak.kominfo.smsservermysql.model.ModelKirim;


public class ForegroundService extends Service {


    private Handler h;
    private Runnable r;

    int counter = 0;

    String email;
    Context context;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification updateNotification(String isi) {

        Context context = getApplicationContext();

        PendingIntent action = PendingIntent.getActivity(context,
                0, new Intent(context, MainActivity.class),
                PendingIntent.FLAG_CANCEL_CURRENT); // Flag indicating that if the described PendingIntent already exists, the current one should be canceled before generating a new one.

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            String CHANNEL_ID = "alex_channel";

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "AlexChannel",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Alex channel description");
            manager.createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        return builder.setContentIntent(action)
                .setContentTitle("SMS Gateway running")
                .setTicker("Standby menerima SMS")
                .setContentText("Posisi ON | "+ isi)
                .setSmallIcon(R.drawable.medantechno)
                .setContentIntent(action)
                .setOngoing(true).build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().contains("start")) {

            new SmsBroadcastReceiver(); //inbox
            bacaOutbox();   //outbox

            startForeground(101, updateNotification(""));
            /*
            h = new Handler();

            r = new Runnable() {
                @Override
                public void run() {
                    startForeground(101, updateNotification());
                    h.postDelayed(this, 1000);
                }
            };

            h.post(r);

             */
        } else {
            //h.removeCallbacks(r);
            stopForeground(true);
            stopSelf();
        }

        return Service.START_STICKY;
    }


    private class SmsBroadcastReceiver extends BroadcastReceiver {

        private static final String TAG = "SmsBroadcastReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {


            /*
            Intent intentx = new Intent();
            intentx.setClassName("pakpak.kominfo.smsservermysql", "pakpak.kominfo.smsservermysql.MainActivity");
            intentx.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentx);
            */


                Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                SmsMessage[] msgs = null;
                String msg_from;


                System.out.println("pdunya" + bundle);

                if (bundle != null) {
                    //---retrieve the SMS message received---
                    try {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];

                        System.out.println("pdunya" + pdus);

                        for (int i = 0; i < msgs.length; i++) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            msg_from = msgs[i].getOriginatingAddress();
                            String msgBody = msgs[i].getMessageBody();

                            System.out.println("dari:" + msg_from + ".pesan:" + msgBody);


                            Random random = new Random();
                            int x = random.nextInt(90000) + 10000;

                            String waktu = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date());


                            DbUser dbUser = new DbUser(context);
                            String email;
                            try {
                                int id = dbUser.select_terbesar().getId_member();
                                String id_member = String.valueOf(id);
                                email = dbUser.select_terbesar().getEmail();
                                Log.d("member:", id_member + email);

                            } catch (Exception e) {
                                email = "";
                            }


                            try {

                                //newPostRef.setValue(new ModelKirim(String.valueOf(x),msg_from, msgBody, email, "new",waktu,newPostRef.getKey()));


                                inbox_simpan(msg_from, msgBody, msgs.toString(), email, context);

                                /***** notif ******/
                                //startForeground(88,setNotif("Pushed from:" + msg_from, msgBody, context));
                                //startForeground(101, updateNotification(msgBody));
                                /***** notif ******/

                            } catch (Exception y) {
                                /**** disini ke db sementara ****/

                                DbInbox dbInbox = new DbInbox(context);
                                try {
                                    dbInbox.insert(new ModelKirim(String.valueOf(x), msg_from, msgBody, email, "offline", waktu, msgs.toString()));
                                } catch (Exception xxxx) {

                                }

                            }

                        }
                    } catch (Exception e) {
//                            Log.d("Exception caught",e.getMessage());
                    }
                }
            }
        }



        private void inbox_simpan(String nomor, String pesan, String generated_id, String email, Context context) {


            try {

                RequestQueue queue = Volley.newRequestQueue(context);
                String url = new ConfigServer(context).get_url_local_inbox_simpan() + "&nomor=" + nomor + "&pesan=" + pesan + "&generated_id=" + generated_id + "&email=" + email;
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

            } catch (Exception e) {
                Log.d("simpan_inbox_err2", e.toString());
            }

        }


    }






    /* DISINI UNTUK SERVICE OUTBOXNYA */
    private void bacaOutbox()
    {
        /*
        Intent get_email = new Intent();
        email = get_email.getStringExtra("email");
        */
        context = this;

        DbUser dbUser = new DbUser(this);
        try{
            int id = dbUser.select_terbesar().getId_member();
            String id_member = String.valueOf(id);
            email = dbUser.select_terbesar().getEmail();
            Log.d("member:",id_member+email);

        }catch (Exception e)
        {
            try {

                int id = dbUser.select_terbesar().getId_member();
                String id_member = String.valueOf(id);
                email = dbUser.select_terbesar().getEmail();
                Log.d("member:",id_member+email);

            }catch (Exception o)
            {
                email = "admin@gmail.com";
            }


        }


        /******* auto refresh **********/

        final Handler handler = new Handler();
        Runnable refresh = new Runnable() {
            @Override
            public void run() {
                System.out.println("delay");
                panggilJson();
                handler.postDelayed(this, 20000);
            }
        };
        handler.postDelayed(refresh, 20000);

        /******* auto refresh **********/


    }

    private void panggilJson()
    {


        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                new ConfigServer(this).get_url_local_outbox_baru(), null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                // display response
                System.out.println("jumlah:"+response.length());

                if(response.length() > 0)
                {
                    for (int i = 0; i < response.length(); i++)
                    {
                        try {

                            JSONObject obj = response.getJSONObject(i);
                            ModelKirim modelKirim = new ModelKirim();

                            modelKirim.setId(obj.getString("id"));
                            modelKirim.setEmail(obj.getString("email"));
                            modelKirim.setNomor(obj.getString("nomor"));
                            modelKirim.setPesan(obj.getString("pesan"));
                            modelKirim.setStatus(obj.getString("status"));
                            modelKirim.setWaktu(obj.getString("waktu"));


                            if(modelKirim.getStatus().equals("new"))
                            {


                                SmsHelper.sendDebugSms(modelKirim.getNomor(), modelKirim.getPesan());




                                /*** jika sudah berhasil***/
                                update_sent(modelKirim.getId());
                                /*** jika sudah berhasil***/

                            }


                        }catch (Exception e)
                        {
                            Log.d("error_baca_json","aaa-"+e.toString());
                        }
                    }

                }else {


                }


            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response",error.toString());



                    }
                }
        );

        // add it to the RequestQueue
        queue.add(jsonObjReq);


    }





    private void update_sent(String id)
    {



        try {

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = new ConfigServer(this).get_url_local_outbox_update()+"&id=" + id;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("respon_update", response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(getApplicationContext(), token,Toast.LENGTH_LONG).show();
                    Log.d("respon_update", error.toString());
                }
            });
            queue.add(stringRequest);

        }catch (Exception e)
        {
            Log.d("respon_update",e.toString());
        }

    }

}