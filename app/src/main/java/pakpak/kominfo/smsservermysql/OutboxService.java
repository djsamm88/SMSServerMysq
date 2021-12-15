package pakpak.kominfo.smsservermysql;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import pakpak.kominfo.smsservermysql.database.DbToken;
import pakpak.kominfo.smsservermysql.database.DbUser;
import pakpak.kominfo.smsservermysql.model.ModelKirim;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by dinaskominfokab.pakpakbharat on 16/06/18.
 */

public class OutboxService extends Service {


    DatabaseReference mFirebaseDatabase;
    FirebaseDatabase mFirebaseInstance;

    String email;
    Context context;
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub


        return null;

    }



    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {

        super.onStartCommand(intent, flags, startId);
        Log.d("OutboxService","onStartCommand");
        return START_STICKY;

    }

    @Override
    public void onCreate()
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
                handler.postDelayed(this, 50000);
            }
        };
        handler.postDelayed(refresh, 50000);

        /******* auto refresh **********/




    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }



    public static final String md5(final String s)
    {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public void setNotif(String judul, String isi, Context context)
    {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

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

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, mBuilder.build());
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
