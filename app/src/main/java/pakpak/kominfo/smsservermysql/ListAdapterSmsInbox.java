package pakpak.kominfo.smsservermysql;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import pakpak.kominfo.smsservermysql.database.DbUser;
import pakpak.kominfo.smsservermysql.model.ModelKirim;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by dinaskominfokab.pakpakbharat on 14/06/18.
 */

public class ListAdapterSmsInbox extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ModelKirim> adminList;



    AlertDialog.Builder dialog;
    LayoutInflater inflater_dialog;
    View dialogView;

    Context context;


    private DatabaseReference xmFirebaseDatabase;
    private FirebaseDatabase xmFirebaseInstance;



    public ListAdapterSmsInbox(Activity activity, List<ModelKirim> adminList,Context context) {
        this.activity = activity;
        this.adminList = adminList;
        this.context = context;
    }



    @Override
    public int getCount() {
        return adminList.size();
    }

    @Override
    public Object getItem(int location) {
        return adminList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        if (inflater == null) inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) convertView = inflater.inflate(R.layout.list_sms, null);


        TextView v_nomor = (TextView) convertView.findViewById(R.id.nomor);
        TextView v_email = (TextView) convertView.findViewById(R.id.email);
        TextView v_status = (TextView) convertView.findViewById(R.id.status);
        TextView v_pesan = (TextView) convertView.findViewById(R.id.pesan);
        TextView v_id = (TextView) convertView.findViewById(R.id.id);
        TextView v_waktu = (TextView) convertView.findViewById(R.id.waktu);

        LinearLayout bgnya = (LinearLayout) convertView.findViewById(R.id.bgnya);

        if(position % 2==0)
        {
            convertView.setBackgroundResource(R.color.abuabu);
        }else{
            convertView.setBackgroundColor(Color.WHITE);
        }

        //setBackgroundResource(R.color.white);



        // getting movie data for the row

        final ModelKirim k = adminList.get(position);

        v_nomor.setText(k.getNomor());
        v_email.setText(k.getEmail());
        v_status.setText(k.getStatus());
        v_pesan.setText(k.getPesan());
        v_id.setText(k.getId());
        v_waktu.setText(k.getWaktu());


        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                dialog_hapus(k);

            }
        });
        return convertView;
    }


    private void delete(String id)
    {

        System.out.println("hapus:"+id);

        try {

            RequestQueue queue = Volley.newRequestQueue(context);
            String url = new ConfigServer(context).get_url_local_inbox_delete()+"&id=" + id;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("respon_delete_1", response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(getApplicationContext(), token,Toast.LENGTH_LONG).show();
                    Log.d("respon_delete_2", error.toString());
                }
            });
            queue.add(stringRequest);

        }catch (Exception e)
        {
            Log.d("respon_delete_3",e.toString());

        }

    }


    public void dialog_hapus(final ModelKirim modelKirim)
    {
        dialog = new AlertDialog.Builder(activity);

        inflater_dialog = LayoutInflater.from(activity);
        dialogView = inflater_dialog.inflate(R.layout.list_sms, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle(modelKirim.getGenerated_id());


        dialogView.setPadding(10,10,10,10);
        dialogView.setBackgroundResource(R.color.abuabu);
        TextView v_nomor = (TextView) dialogView.findViewById(R.id.nomor);
        TextView v_email = (TextView) dialogView.findViewById(R.id.email);
        TextView v_status = (TextView) dialogView.findViewById(R.id.status);
        TextView v_pesan = (TextView) dialogView.findViewById(R.id.pesan);
        TextView v_id = (TextView) dialogView.findViewById(R.id.id);
        TextView v_waktu = (TextView) dialogView.findViewById(R.id.waktu);

        v_nomor.setText(modelKirim.getNomor());
        v_email.setText(modelKirim.getEmail());
        v_status.setText(modelKirim.getStatus());
        v_pesan.setText(modelKirim.getPesan());
        v_id.setText(modelKirim.getId());
        v_waktu.setText(modelKirim.getWaktu());



        dialog.setNegativeButton("Delete          ", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                confirm_delete(modelKirim);
                dialog.dismiss();
            }
        });

        dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void confirm_delete(final ModelKirim modelKirim)
    {
        new AlertDialog.Builder(activity)
                .setTitle("Delete")
                .setMessage("Do you really want to delete?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(activity, "Deleting...", Toast.LENGTH_SHORT).show();
                        delete(modelKirim.getId());
                    }}).show();

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


}
