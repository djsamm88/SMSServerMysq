package pakpak.kominfo.smsservermysql.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pakpak.kominfo.smsservermysql.model.ModelToken;

/**
 * Created by dinaskominfokab.pakpakbharat on 17/04/18.
 */

public class DbToken extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;


    private static final String DATABASE_NAME = "db_sms_token.db";


    private static final String TBL_COMMAND = "tbl_token";


    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "token";


    public DbToken(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TBL_COMMAND + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TBL_COMMAND);


        onCreate(db);
    }


    public  void insert(ModelToken modelToken) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, modelToken.getToken());

        db.insertOrThrow(TBL_COMMAND, null, values);
        //db.insert(TBL_COMMAND, null, values);
        db.close();
    }



    public ModelToken select_terbesar() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+TBL_COMMAND+" ORDER BY "+KEY_ID+" DESC LIMIT 1",null);

        if (cursor != null)
            cursor.moveToFirst();

        ModelToken modelToken = new ModelToken(
                cursor.getInt(0),
                cursor.getString(1)
        );

        return modelToken;
    }


    public void update(ModelToken modelToken) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, modelToken.getToken());


        db.update(TBL_COMMAND, values, KEY_ID + " = ?",
                new String[] { String.valueOf(modelToken.getId()) });
        db.close();
    }

}
