package br.com.treinaweb.candidatostreinaweb.dao;

/**
 * Created by Edu on 01/02/2016.
 */
import java.util.ArrayList;
import java.util.List;

import br.com.treinaweb.candidatostreinaweb.database.UserTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserDao extends Dao<User> {

    public UserDao(Context context) {
        super(context);
    }

    @Override
    public User save(User entity) {
        User user = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try{
            ContentValues contentValues = new ContentValues();

            contentValues.put(UserTable.COLUMN_NAME, entity.getName());
            contentValues.put(UserTable.COLUMN_EMAIL, entity.getEmail());
            contentValues.put(UserTable.COLUMN_TOKEN, entity.getToken());
            contentValues.put(UserTable.COLUMN_AVATAR, entity.getAvatar());
            contentValues.put(UserTable.COLUMN_IMG_PERFIL, entity.getImgPerfil());

            long id = db.insertOrThrow(UserTable.TABLE_USER, null, contentValues);

            entity.setId(id);
            user = entity;
        }catch (Exception e){
            Log.e("Error UserDao", e.getMessage());
        }finally {
            db.close();
        }

        return user;
    }

    @Override
    public int update(User entity) {
        int countrows = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try{
            ContentValues contentValues = new ContentValues();

            contentValues.put(UserTable.COLUMN_NAME, entity.getName());
            contentValues.put(UserTable.COLUMN_EMAIL, entity.getEmail());
            contentValues.put(UserTable.COLUMN_TOKEN, entity.getToken());
            contentValues.put(UserTable.COLUMN_AVATAR, entity.getAvatar());
            contentValues.put(UserTable.COLUMN_IMG_PERFIL, entity.getImgPerfil());

            countrows = db.update(UserTable.TABLE_USER, contentValues, "_id = " + entity.getId(), null);
        }catch (Exception e){
            Log.e("Error UserDao", e.getMessage());
        }finally {
            db.close();
        }
        return countrows;
    }

    @Override
    public int remove(User entity) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int countRows = 0;

        try{

            countRows = db.delete(UserTable.TABLE_USER, "_id = " + entity.getId(), null);

        }catch (Exception e){
            Log.e("Error DAO", e.getMessage());
        }finally {
            db.close();
        }
        return countRows;
    }

    @Override
    public User getById(long pk) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;
        try{

            Cursor cursor = db.query(UserTable.TABLE_USER, null,"_id = " + pk,null,null,null, UserTable.COLUMN_NAME + " asc");

            if (cursor.moveToNext()){
                user = new User();

                user.setId(cursor.getLong(0));
                user.setName(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setToken(cursor.getString(3));
                user.setAvatar(cursor.getBlob(4));
                user.setImgPerfil(cursor.getBlob(5));

            }

        }catch (Exception e){
            Log.e("Error DAO", e.getMessage());
        }finally {
            db.close();
        }

        return user;
    }

    public User getByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;
        try{
            String[] selectionArgs = { email };
            Cursor cursor = db.query(UserTable.TABLE_USER, null,UserTable.COLUMN_EMAIL + " = ?", selectionArgs, null,null, UserTable.COLUMN_NAME + " asc");

            if (cursor.moveToNext()){
                user = new User();

                user.setId(cursor.getLong(0));
                user.setName(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setToken(cursor.getString(3));
                user.setAvatar(cursor.getBlob(4));
                user.setImgPerfil(cursor.getBlob(5));

            }

        }catch (Exception e){
            Log.e("Error DAO", e.getMessage());
        }finally {
            db.close();
        }

        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> lista = new ArrayList<User>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try{

            Cursor cursor = db.query(UserTable.TABLE_USER, null,null,null,null,null, UserTable.COLUMN_NAME + " asc");

            while (cursor.moveToNext()){
                User user = new User();

                user.setId(cursor.getLong(0));
                user.setName(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setToken(cursor.getString(3));
                user.setAvatar(cursor.getBlob(4));
                user.setImgPerfil(cursor.getBlob(5));

                lista.add(user);
            }

        }catch (Exception e){
            Log.e("Error DAO", e.getMessage());
        }finally {
            db.close();
        }

        return lista;
    }
}
