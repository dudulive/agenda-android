package br.com.treinaweb.candidatostreinaweb.database;

/**
 * Created by Edu on 01/02/2016.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDbHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "app_database.db";
    static final int DB_VERSION = 1;

    public AppDbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        // TODO Auto-generated method stub
        UserTable.onCreate(database);
        CandidateTable.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        UserTable.onUpgrade(database, oldVersion, newVersion);
        CandidateTable.onUpgrade(database, oldVersion, newVersion);
    }
}
