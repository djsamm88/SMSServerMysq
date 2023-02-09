package pakpak.kominfo.smsservermysql;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;



import org.json.JSONArray;
import org.json.JSONObject;

import pakpak.kominfo.smsservermysql.database.DbConfig;
import pakpak.kominfo.smsservermysql.database.DbToken;
import pakpak.kominfo.smsservermysql.database.DbUser;
import pakpak.kominfo.smsservermysql.model.ModelConfig;
import pakpak.kominfo.smsservermysql.model.ModelKirim;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


//import android.app.AlertDialog;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String PREF_USER_MOBILE_PHONE = "pref_user_mobile_phone";
    private static final int SMS_PERMISSION_CODE = 0;
    private static final int SMS_PERMISSION_CODE_SEND = 00;


    private String mUserMobilePhone;
    private SharedPreferences mSharedPreferences;

    public String email;

    public String url_outbox,url_inbox;




    private ListView listView,listView2;


    private ListAdapterSms bAdapter;
    private ListAdapterSmsInbox bAdapter2;

    private List<ModelKirim> adminList = new ArrayList<ModelKirim>();

    private List<ModelKirim> adminList2 = new ArrayList<ModelKirim>();

    Button btn_penggunaan,btn_tentang,btn_keluar,btn_start;


    AlertDialog.Builder dialog;
    LayoutInflater inflater_dialog;
    View dialogView;
    Context context;

    ProgressBar progressBar,progressBar2;
    TextView no_data,no_data2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!hasReadSmsPermission()) {
            showRequestPermissionsInfoAlertDialog();
        }

        if(!hasSendSmsPermission())
        {
            showRequestKirimPermissionsInfoAlertDialog();
        }

        //showRequestPermissionsInfoAlertDialog();

        //iklan google
        //MobileAds.initialize(this, "ca-app-pub-2993509046689702/3710191985");
        //untuk testing MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111");



        //initViews();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserMobilePhone = mSharedPreferences.getString(PREF_USER_MOBILE_PHONE, "");

        btn_penggunaan = (Button) findViewById(R.id.btn_usage);
        btn_tentang = (Button) findViewById(R.id.btn_tentang);
        btn_keluar =(Button) findViewById(R.id.btn_keluar);
        btn_start =(Button) findViewById(R.id.btn_start);


        progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar_cyclic2);
        progressBar.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);

        no_data2 = (TextView)findViewById(R.id.no_data2);
        no_data = (TextView)findViewById(R.id.no_data);

        DbUser dbUser = new DbUser(this);
        try{
            int id = dbUser.select_terbesar().getId_member();
            String id_member = String.valueOf(id);
            email = dbUser.select_terbesar().getEmail();
            Log.d("member:",id_member+email);

        }catch (Exception e)
        {
            /********** ambil dari login **********/

            try {
                Intent i = getIntent();
                email = i.getStringExtra("email");
                Log.d("member_intent:", email);
            }catch (Exception z)
            {
                email="djsamm88.web.id@mail.com";
            }


        }

        /********************** menjalankan MysService**********************/
        /*
        Intent go_service_outbox = new Intent(this, OutboxService.class);
        go_service_outbox.putExtra("email", email);
        startService(go_service_outbox);


        Calendar cal = Calendar.getInstance();
        final Intent intent = new Intent(MainActivity.this, OutboxService.class);
        intent.putExtra("email", email);

        PendingIntent pintent = PendingIntent.getService(MainActivity.this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);

         */


        if (isMyServiceRunning(ForegroundService.class)) return;
        Intent startIntent = new Intent(getApplicationContext(), ForegroundService.class);
        startIntent.setAction("start");
        startService(startIntent);
        /********************** menjalankan MysService**********************/





        btn_penggunaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm();
            }
        });


        btn_tentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogWebview(md5(email));

            }
        });



        btn_keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //signOut();
                if (!isMyServiceRunning(ForegroundService.class)) return;
                Intent stopIntent = new Intent(getApplicationContext(), ForegroundService.class);
                stopIntent.setAction("stop");
                startService(stopIntent);

                //finish();

            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //signOut();

                if (isMyServiceRunning(ForegroundService.class)) return;
                Intent startIntent = new Intent(getApplicationContext(), ForegroundService.class);
                startIntent.setAction("start");
                startService(startIntent);
                finish();

            }
        });




        update_token();


        /*********sms outbox***********************/
        listView = (ListView) findViewById(R.id.list);
        bAdapter = new ListAdapterSms(this, adminList,getApplicationContext());
        listView.setAdapter(bAdapter);
        panggilJson();
        /*********sms outbox***********************/


        /******************** inbox ****************************/
        listView2 = (ListView) findViewById(R.id.list2);
        bAdapter2 = new ListAdapterSmsInbox(this, adminList2,getApplicationContext());
        listView2.setAdapter(bAdapter2);
        panggilJsonInbox();
        /******************** inbox ****************************/



        /******* auto refresh **********/
        final Handler handler = new Handler();
        Runnable refresh = new Runnable() {
            @Override
            public void run() {

                //System.out.println("delay");
                adminList.clear();
                adminList2.clear();
                panggilJson();
                panggilJsonInbox();
                handler.postDelayed(this, 20000);
            }
        };
        handler.postDelayed(refresh, 20000);
        /******* auto refresh **********/




        /************ cek service outbox is running ***************/
        if(isMyServiceRunning(OutboxService.class))
        {
            System.out.println("OutboxService Is Running");
        }
        /************ cek service outbox is running ***************/




        System.out.println(new ConfigServer(MainActivity.this).get_url_local_outbox_delete());


    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        //startService(new Intent(getBaseContext(),OutboxService.class));
        /*
        Intent go_service_outbox = new Intent(this, OutboxService.class);
        go_service_outbox.putExtra("email", email);
        startService(go_service_outbox);

         */

        if (isMyServiceRunning(ForegroundService.class)) return;
        Intent startIntent = new Intent(getApplicationContext(), ForegroundService.class);
        startIntent.setAction("start");
        startService(startIntent);

    }




    public List<String> getSMS(){
        List<String> sms = new ArrayList<String>();
        Uri uriSMSURI = Uri.parse("content://sms/inbox");
        Cursor cur = getContentResolver().query(uriSMSURI, null, "read = 0", null, null);

        while (cur != null && cur.moveToNext()) {
            String address = cur.getString(cur.getColumnIndex("address"));
            String body = cur.getString(cur.getColumnIndexOrThrow("body"));
            sms.add("Number: " + address + " .Message: " + body);


            /************** update menjadi dibaca *******************/
            String SmsMessageId = cur.getString(cur.getColumnIndex("_id"));
            /************** update menjadi dibaca *******************/

            System.out.println(SmsMessageId);

            /*
            String waktu = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date());
            DbInbox dbInbox = new DbInbox(this);
            try{
                dbInbox.insert(new ModelKirim(SmsMessageId,address,body,email,"read",waktu));
            }catch (Exception xxxx)
            {
                Log.d("inbox","Udah ada");
                Log.d("err",xxxx.toString());
            }
            */
        }

        if (cur != null) {
            cur.close();
        }
        return sms;
    }




    /**
     * Checks if stored SharedPreferences value needs updating and updates \o/
     */



    /**
     * Validates if the app has readSmsPermissions and the mobile phone is valid
     *
     * @return boolean validation value
     */
    private boolean hasValidPreConditions() {
        if (!hasReadSmsPermission()) {
            requestReadSmsPermission();
            return false;
        }


        return true;
    }

    /**
     * Optional informative alert dialog to explain the user why the app needs the Read/Send SMS permission
     */
    private void showRequestPermissionsInfoAlertDialog()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_alert_dialog_title);
        builder.setMessage(R.string.permission_dialog_message);
        builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                requestReadSmsPermission();

            }
        });
        builder.show();
    }

    private void showRequestKirimPermissionsInfoAlertDialog()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_alert_dialog_title);
        builder.setMessage(R.string.permission_dialog_message);
        builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                requestSendSmsPermission();
            }
        });
        builder.show();
    }



    /**
     * Runtime permission shenanigans
     */

    private boolean hasSendSmsPermission() {
        return ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;

    }

    private boolean hasReadSmsPermission() {
        return ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;

    }

    private void requestReadSmsPermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_SMS))
        {
            Log.d(TAG, "shouldShowRequestPermissionRationale(), no permission requested");
            return;
        }
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                SMS_PERMISSION_CODE);
    }



    private void requestSendSmsPermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.SEND_SMS))
        {
            Log.d(TAG, "shouldShowRequestPermissionRationale(), no permission requested");
            return;
        }
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.SEND_SMS},
                SMS_PERMISSION_CODE_SEND);
    }



    private void update_token()
    {


        DbToken dbToken = new DbToken(this);

        try {
            String token = dbToken.select_terbesar().getToken();
            /************ update tbl_member -> notif_token di server **************/
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = StringUrl.url_token+"?token=" + token + "&email="+email;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Toast.makeText(getApplicationContext(), "Response is: "+ response,Toast.LENGTH_LONG).show();
                            Log.d("TOKEN", "telah di update");
                            Log.d("TOKEN", response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(getApplicationContext(), token,Toast.LENGTH_LONG).show();
                    Log.d("TOKEN", error.toString());
                }
            });
            queue.add(stringRequest);
            /************ update tbl_member -> notif_token di server **************/
        }catch (Exception e)
        {
            Log.d("TOKEN",e.toString());
        }

    }

    private void scrollMyListViewToBottom()
    {
        listView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listView.setSelection(bAdapter.getCount() - 1);
            }
        });
    }


    private void scrollMyListViewToBottom2()
    {
        listView2.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listView2.setSelection(bAdapter2.getCount() - 1);
            }
        });
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



    private void DialogForm()
    {
        dialog = new AlertDialog.Builder(this);

        inflater_dialog = LayoutInflater.from(this);
        dialogView = inflater_dialog.inflate(R.layout.dialog_penggunaan, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle("API Information");


        /**
         * database cek
         */

        DbConfig dbConfig = new DbConfig(this);

        try{
            ModelConfig modelConfig = dbConfig.select_terbesar();

             url_outbox   = modelConfig.getUrl_outbox();
             url_inbox   = modelConfig.getUrl_inbox();

        } catch(Exception e)
        {

             url_outbox   = "http://";
             url_inbox   = "http://";
            Toast.makeText(getApplicationContext(),"Url belum diatur...", Toast.LENGTH_LONG).show();
        }

        EditText v_text_outbox_url = dialogView.findViewById(R.id.text_outbox_url);
        v_text_outbox_url.setText(url_outbox);

        EditText v_text_inbox_url = dialogView.findViewById(R.id.text_inbox_url);
        v_text_inbox_url.setText(url_inbox);


        Button btn_update_config = dialogView.findViewById(R.id.btn_update_config);


        btn_update_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText get_url_inbox = dialogView.findViewById(R.id.text_inbox_url);
                EditText get_url_outbox = dialogView.findViewById(R.id.text_outbox_url);

                System.out.println(get_url_inbox.getText().toString()+" --- "+get_url_outbox.getText().toString());

                DbConfig dbC = new DbConfig(getApplicationContext());
                try {
                    dbC.insert(new ModelConfig("1", get_url_inbox.getText().toString(), get_url_outbox.getText().toString()));
                }catch (Exception e)
                {
                    System.out.println(" Try Updating because:"+e.toString());

                    try {
                        dbC.update("1", get_url_inbox.getText().toString(), get_url_outbox.getText().toString());
                    }catch (Exception x)
                    {
                        System.out.println(" Updating failed :"+e.toString());
                    }
                }

                Toast.makeText(getApplicationContext(),"Telah diupdate",Toast.LENGTH_LONG).show();


            }
        });


        dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }




    @SuppressLint("SetJavaScriptEnabled")
    private void DialogWebview(final String md5nya)
    {

        dialog = new AlertDialog.Builder(MainActivity.this);
        inflater_dialog = LayoutInflater.from(this);
        dialogView = inflater_dialog.inflate(R.layout.dialog_webview, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle("API Information");



        WebView myWebView = (WebView) dialogView.findViewById(R.id.myWebView);
        WebSettings webSettings = myWebView.getSettings();

        myWebView.loadUrl("file:///android_asset/usage.html");
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {

                /***************** ini agar bisa donlod ****************/
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                /***************** ini agar bisa donlod ****************/

                try {
                  Intent i = new Intent(Intent.ACTION_VIEW);
                  i.setData(Uri.parse(url));
                  startActivity(i);
              }catch (Exception e)
              {
                  System.out.println(e.toString());
              }

            }
        });
        

        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);


        dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // Use When the user clicks a link from a web page in your WebView
    private class MyWebViewClient extends WebViewClient {

        @Override

        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            //final Uri uri = request.getUrl();


            return false;
        }



        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub

            super.onPageStarted(view, url, favicon);

            super.onPageFinished(view, url);
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub

            super.onPageFinished(view, url);
        }


        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            Toast.makeText(MainActivity.this,
                    "The Requested Page Does Not Exist", Toast.LENGTH_LONG).show();
            super.onReceivedError(view, errorCode, description, failingUrl);
        }



    }





    private void panggilJson()
    {


        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                new ConfigServer(MainActivity.this).get_url_local_outbox(), null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                // display response
                //Log.d("inicek_email",url_local_outbox);
                //System.out.println("jumlah:"+response.length());

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
                            adminList.add(modelKirim);
                            bAdapter.notifyDataSetChanged();
                            scrollMyListViewToBottom();
                            progressBar.setVisibility(View.GONE);
                            if(adminList.size()>0)
                            {
                                no_data.setVisibility(View.GONE);
                            }





                        }catch (Exception e)
                        {
                            Log.d("errornya","aaa-"+e.toString());
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
                        Log.d("Error.Response_Outbox",error.toString());
                        Toast.makeText(getApplicationContext(),"Url Outbox Not Found,404",Toast.LENGTH_SHORT).show();

                    }
                }
        );

        // add it to the RequestQueue
        queue.add(jsonObjReq);


    }




    private void panggilJsonInbox()
    {


        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                new ConfigServer(this).get_url_local_inbox(), null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                // display response
                //Log.d("inicek_email",url_local_outbox);
                //System.out.println("jumlah:"+response.length());

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
                            adminList2.add(modelKirim);
                            bAdapter2.notifyDataSetChanged();
                            scrollMyListViewToBottom2();
                            progressBar2.setVisibility(View.GONE);
                            if(adminList2.size()>0)
                            {
                                no_data2.setVisibility(View.GONE);
                            }





                        }catch (Exception e)
                        {
                            Log.d("errornya","aaa-"+e.toString());
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
                        Log.d("Error.Response_inbox",error.toString());

                        Toast.makeText(getApplicationContext(),"Url Inbox Not Found,404",Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // add it to the RequestQueue
        queue.add(jsonObjReq);


    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}