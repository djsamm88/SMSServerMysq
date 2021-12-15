package pakpak.kominfo.smsservermysql.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pakpak.kominfo.smsservermysql.model.ModelConfig;


/**
 * Created by dinaskominfokab.pakpakbharat on 11/08/18.
 */


public class DbConfig extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "db_config_url.db";

    private static final String table = "tbl_config_url";

    private static final String id = "id";
    private static final String url_inbox = "url_inbox";
    private static final String url_outbox = "url_outbox";



    public DbConfig(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE  "+table+" (id INTEGER PRIMARY KEY,url_inbox TEXT, url_outbox TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS "+table);

        // Create tables again
        onCreate(db);
    }



    public void insert(ModelConfig modelConfig) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(id,modelConfig.getId());
        values.put(url_inbox,modelConfig.getUrl_inbox());
        values.put(url_outbox,modelConfig.getUrl_outbox());
        db.insertOrThrow(table, null, values);
        //db.insert(table, null, values);
        db.close();

    }


    // Getting single
    public ModelConfig select_by_id(int idnya) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(table, new String[]{
                        id, url_inbox, url_outbox},
                id + "=?",
                new String[]{String.valueOf(idnya)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        ModelConfig modelConfig = new ModelConfig(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2)
        );

        return modelConfig;
    }



    // Getting single terbesar
    public ModelConfig select_terbesar() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+table+" ORDER BY "+id+" DESC LIMIT 1",null);

        if (cursor != null)
            cursor.moveToFirst();

        ModelConfig modelConfig = new ModelConfig(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2)
        );

        return modelConfig;
    }


    public void update(String vid,String vurl_inbox,String vurl_outbox)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(url_inbox,vurl_inbox);
        values.put(url_outbox,vurl_outbox);
        db.update(
                table,
                values,
                id + " = ?",
                new String[]
                        {
                                String.valueOf(vid)
                        }
        );
        db.close();

    }


}

