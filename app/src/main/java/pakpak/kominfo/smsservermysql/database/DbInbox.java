package pakpak.kominfo.smsservermysql.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pakpak.kominfo.smsservermysql.model.ModelKirim;

/**
 * Created by dinaskominfokab.pakpakbharat on 14/06/18.
 */

public class DbInbox extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "db_sms_inbox.db";

    private static final String table = "tbl_inbox";

    private static final String id = "id";
    private static final String email = "email";
    private static final String nomor = "nomor";
    private static final String pesan = "pesan";
    private static final String status = "status";
    private static final String waktu = "waktu";
    private static final String generated_id = "generated_id";



    public DbInbox(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE  tbl_inbox (id TEXT PRIMARY KEY,email TEXT, nomor TEXT, pesan TEXT, status TEXT,waktu TEXT,generated_id TEXT)";
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



    public void insert(ModelKirim modelKirim) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(id,modelKirim.getId());
        values.put(email,modelKirim.getEmail());
        values.put(nomor,modelKirim.getNomor());
        values.put(pesan,modelKirim.getPesan());
        values.put(status,modelKirim.getStatus());
        values.put(waktu,modelKirim.getWaktu());
        values.put(generated_id,modelKirim.getGenerated_id());

        db.insertOrThrow(table, null, values);
        //db.insert(table, null, values);
        db.close();

    }

    public void hapus(ModelKirim modelKirim) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, id + " = ?",
                new String[]{modelKirim.getId()});
        db.close();
    }



}
