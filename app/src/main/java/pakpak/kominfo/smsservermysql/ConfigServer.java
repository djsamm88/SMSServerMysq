package pakpak.kominfo.smsservermysql;

import android.content.Context;
import android.widget.Toast;

import pakpak.kominfo.smsservermysql.database.DbConfig;
import pakpak.kominfo.smsservermysql.model.ModelConfig;

/**
 * Created by dinaskominfokab.pakpakbharat on 11/08/18.
 */

public class ConfigServer {
    private Context context;

    String url_inbox,url_outbox;

    public ConfigServer(Context context)
    {
        this.context=context;
    }






    public String get_url_local_outbox()
    {

        DbConfig dbConfig = new DbConfig(context);
        try{
            ModelConfig modelConfig = dbConfig.select_terbesar();

            url_outbox   = modelConfig.getUrl_outbox();

        } catch(Exception e)
        {

            /****************** JIKA MAU DICOMPILE ALAMAT SERVER MANUAL *********************/

            try {

                //dbConfig.insert(new ModelConfig("1", "http://ujicoba.pakpakbharatkab.go.id/sms/data_inbox.php", "http://ujicoba.pakpakbharatkab.go.id/sms/data_outbox.php"));
                dbConfig.insert(new ModelConfig("1", "http://ujicoba.pakpakbharatkab.go.id/sms/data_inbox.php", "http://esurat.pakpakbharatkab.go.id/sms_data_out/index"));
                //dbConfig.insert(new ModelConfig("1", "https://sipelaknpb.pakpakbharatkab.go.id/sms_gateway/sms_gateway_inbox.php", "https://sipelaknpb.pakpakbharatkab.go.id/sms_gateway/sms_gateway_outbox.php"));

            }catch (Exception z)
            {

            }

            /****************** JIKA MAU DICOMPILE ALAMAT SERVER MANUAL *********************/

            /*
            url_outbox   = "";
            Toast.makeText(context,"Url belum diatur...", Toast.LENGTH_LONG).show();
            */

        }

        return url_outbox+"?action=list";

    }



    public String get_url_local_outbox_baru()
    {

        DbConfig dbConfig = new DbConfig(context);
        try{
            ModelConfig modelConfig = dbConfig.select_terbesar();

            url_outbox   = modelConfig.getUrl_outbox();

        } catch(Exception e)
        {

            url_outbox   = "";
            Toast.makeText(context,"Url belum diatur...", Toast.LENGTH_LONG).show();
        }

        return url_outbox+"?action=list_terbaru";

    }





    public String get_url_local_outbox_update()
    {

        DbConfig dbConfig = new DbConfig(context);
        try{
            ModelConfig modelConfig = dbConfig.select_terbesar();
            url_outbox   = modelConfig.getUrl_outbox();

        } catch(Exception e)
        {
            url_outbox   = "";
            Toast.makeText(context,"Url belum diatur...", Toast.LENGTH_LONG).show();
        }

        return url_outbox+"?action=update";

    }


    public String get_url_local_outbox_delete()
    {

        DbConfig dbConfig = new DbConfig(context);
        try{
            ModelConfig modelConfig = dbConfig.select_terbesar();
            url_outbox   = modelConfig.getUrl_outbox();

        } catch(Exception e)
        {
            url_outbox   = "";
            Toast.makeText(context,"Url belum diatur...", Toast.LENGTH_LONG).show();
        }

        return url_outbox+"?action=delete";

    }



    public String get_url_local_inbox()
    {
        DbConfig dbConfig = new DbConfig(context);
        try{
            ModelConfig modelConfig = dbConfig.select_terbesar();
            url_inbox   = modelConfig.getUrl_inbox();

        } catch(Exception e)
        {
            url_inbox   = "";
            Toast.makeText(context,"Url belum diatur...", Toast.LENGTH_LONG).show();
        }

        return url_inbox+"?action=list";
    }



    public String get_url_local_inbox_simpan()
    {
        DbConfig dbConfig = new DbConfig(context);
        try{
            ModelConfig modelConfig = dbConfig.select_terbesar();
            url_inbox   = modelConfig.getUrl_inbox();

        } catch(Exception e)
        {
            url_inbox   = "";
            Toast.makeText(context,"Url belum diatur...", Toast.LENGTH_LONG).show();
        }

        return url_inbox+"?action=simpan";
    }





    public String get_url_local_inbox_delete()
    {
        DbConfig dbConfig = new DbConfig(context);
        try{
            ModelConfig modelConfig = dbConfig.select_terbesar();
            url_inbox   = modelConfig.getUrl_inbox();

        } catch(Exception e)
        {
            url_inbox   = "";
            Toast.makeText(context,"Url belum diatur...", Toast.LENGTH_LONG).show();
        }

        return url_inbox+"?action=delete";
    }









}
