package pakpak.kominfo.smsservermysql;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

import android.util.Log;
import android.view.View;
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

import pakpak.kominfo.smsservermysql.database.DbToken;
import pakpak.kominfo.smsservermysql.database.DbUser;
import pakpak.kominfo.smsservermysql.model.ModelKirim;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by dinaskominfokab.pakpakbharat on 16/06/18.
 */

public class OutboxService extends Service {



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
                handler.postDelayed(this, 50000);
            }
        };
        handler.postDelayed(refresh, 50000);

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
