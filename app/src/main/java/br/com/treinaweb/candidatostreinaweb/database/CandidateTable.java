package br.com.treinaweb.candidatostreinaweb.database;

/**
 * Created by Edu on 01/02/2016.
 */
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CandidateTable {
    public static final String CANDIDATE_TABLE = "candidatos";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ID_USER = "idusuario";
    public static final String COLUMN_FOTO = "foto";
    public static final String COLUMN_NOME = "nome";
    public static final String COLUMN_SOBRENOME = "sobrenome";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_TELEFONE = "telefone";
    public static final String COLUMN_CEP = "cep";
    public static final String COLUMN_CARGO = "cargo";
    public static final String COLUMN_DESCRICAO = "descricao";

    private static final String DATABASE_CREATE = "create table "
            + CANDIDATE_TABLE
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_ID_USER + " integer not null, "
            + COLUMN_FOTO + " blob, "
            + COLUMN_NOME + " text, "
            + COLUMN_SOBRENOME + " text, "
            + COLUMN_EMAIL + " text, "
            + COLUMN_TELEFONE + " text, "
            + COLUMN_CEP + " text, "
            + COLUMN_CARGO + " text, "
            + COLUMN_DESCRICAO + " text, "
            + " FOREIGN KEY(" + COLUMN_ID + ") "
            + " REFERENCES " + UserTable.TABLE_USER + "(" + UserTable.COLUMN_ID + ")"
            + ");";


    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(CandidateTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + CANDIDATE_TABLE);
        onCreate(database);
    }

}
