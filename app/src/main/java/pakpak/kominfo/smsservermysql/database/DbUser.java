package pakpak.kominfo.smsservermysql.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pakpak.kominfo.smsservermysql.model.ModelUser;

/**
 * Created by dinaskominfokab.pakpakbharat on 31/03/18.
 */

public class DbUser extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "db_sms_user.db";

    private static final String table = "tbl_user";

    private static final String id_member = "id_member";
    private static final String nama_member = "nama_member";
    private static final String telepon = "telepon";
    private static final String email = "email";
    private static final String photoURL = "photoURL";
    private static final String info_lengkap = "info_lengkap";

    private static final String alamat = "alamat";
    private static final String id_kecamatan = "id_kecamatan";
    private static final String id_desa = "id_desa";
    private static final String status = "status";
    private static final String password = "password";
    private static final String firebase_uid = "firebase_uid";
    private static final String tgl_daftar = "tgl_daftar";



    public DbUser(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE  tbl_user (id_member INTEGER PRIMARY KEY,nama_member TEXT, telepon TEXT, email TEXT, photoURL TEXT,info_lengkap)";
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



    public void insert(ModelUser modelUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(id_member,modelUser.getId_member());
        values.put(nama_member,modelUser.getNama_member());
        values.put(telepon,modelUser.getTelepon());
        values.put(email,modelUser.getEmail());
        values.put(photoURL,modelUser.getPhotoURL());

        db.insertOrThrow(table, null, values);
        //db.insert(table, null, values);
        db.close();

    }


    // Getting single
    public ModelUser select_by_id(int idnya) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(table, new String[]{
                        id_member, nama_member, telepon, email, photoURL},
                id_member + "=?",
                new String[]{String.valueOf(idnya)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        ModelUser modelUser = new ModelUser(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4)
        );

        return modelUser;
    }



    // Getting single terbesar
    public ModelUser select_terbesar() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+table+" ORDER BY "+id_member+" DESC LIMIT 1",null);

        if (cursor != null)
            cursor.moveToFirst();

        ModelUser modelUser = new ModelUser(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4)
        );

        return modelUser;
    }


    public void update_profil_user_aja(int vid_member,String vnama_member,String vtelepon,String vemail,String vphotoURL,String vinfo_lengkap)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(nama_member,vnama_member);
        values.put(telepon,vtelepon);
        values.put(email,vemail);
        values.put(photoURL,vphotoURL);
        values.put(info_lengkap,vinfo_lengkap);

        db.update(
                table,
                values,
                id_member + " = ?",
                new String[]
                        {
                                String.valueOf(vid_member)
                        }
        );
        db.close();

    }


}
