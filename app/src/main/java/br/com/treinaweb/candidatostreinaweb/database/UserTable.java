package br.com.treinaweb.candidatostreinaweb.database;

/**
 * Created by Edu on 01/02/2016.
 */
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserTable {
    public static final String TABLE_USER = "usuarios";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "nome";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_USERNAME = COLUMN_EMAIL;
    public static final String COLUMN_TOKEN = "token";
    public static final String COLUMN_AVATAR = "avatar";
    public static final String COLUMN_IMG_PERFIL = "img_perfil";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_USER
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_EMAIL + " text not null, "
            + COLUMN_TOKEN + " text not null, "
            + COLUMN_AVATAR +  " blob not null, "
            + COLUMN_IMG_PERFIL +  " blob null "
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(UserTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(database);
    }
}