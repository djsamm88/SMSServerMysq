package pakpak.kominfo.smsservermysql;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
        {


            /*
            Intent intentx = new Intent();
            intentx.setClassName("pakpak.kominfo.smsservermysql", "pakpak.kominfo.smsservermysql.MainActivity");
            intentx.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentx);
            */




            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;


            System.out.println("pdunya"+bundle);

            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];

                    System.out.println("pdunya"+pdus);

                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();

                        System.out.println("dari:"+msg_from+".pesan:"+msgBody);



                        Random random = new Random();
                        int x = random.nextInt(90000) + 10000;

                        String waktu = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date());


                        DbUser dbUser = new DbUser(context);
                        String email;
                        try{
                            int id = dbUser.select_terbesar().getId_member();
                            String id_member = String.valueOf(id);
                            email = dbUser.select_terbesar().getEmail();
                            Log.d("member:",id_member+email);

                        }catch (Exception e)
                        {
                            email="";
                        }


                        try {

                            //newPostRef.setValue(new ModelKirim(String.valueOf(x),msg_from, msgBody, email, "new",waktu,newPostRef.getKey()));


                            inbox_simpan(msg_from,msgBody,msgs.toString(),email,context);

                            /***** notif ******/
                            setNotif("Pushed from:"+msg_from, msgBody, context);
                            /***** notif ******/

                        }catch (Exception y)
                        {
                            /**** disini ke db sementara ****/

                            DbInbox dbInbox = new DbInbox(context);
                            try{
                                dbInbox.insert(new ModelKirim(String.valueOf(x),msg_from,msgBody,email,"offline",waktu,msgs.toString()));
                            }catch (Exception xxxx)
                            {

                            }

                        }

                    }
                }catch(Exception e){
//                            Log.d("Exception caught",e.getMessage());
                }
            }
        }
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