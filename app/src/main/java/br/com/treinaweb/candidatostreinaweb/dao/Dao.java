package br.com.treinaweb.candidatostreinaweb.dao;

/**
 * Created by Edu on 01/02/2016.
 */

import java.util.List;

import br.com.treinaweb.candidatostreinaweb.database.AppDbHelper;

import android.content.Context;

public abstract class Dao<T> {
    protected AppDbHelper dbHelper;

    public Dao(Context context){
        this.dbHelper = new AppDbHelper(context);
    }

    public abstract T save(T entity);

    public abstract int update(T entity);

    public abstract int remove(T entity);

    public abstract T getById(long pk);

    public abstract List<T> getAll();
}
