package br.com.treinaweb.candidatostreinaweb.dao;

/**
 * Created by Edu on 01/02/2016.
 */
import java.util.ArrayList;
import java.util.List;

import br.com.treinaweb.candidatostreinaweb.database.CandidateTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CandidateDao extends Dao<Candidate> {

    public CandidateDao(Context context) {
        super(context);
    }

    @Override
    public Candidate save(Candidate entity) {
        Candidate candidato = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try{
            ContentValues contentValues = new ContentValues();

            contentValues.put(CandidateTable.COLUMN_ID_USER, entity.getIdUser());
            contentValues.put(CandidateTable.COLUMN_FOTO, entity.getFoto());
            contentValues.put(CandidateTable.COLUMN_NOME, entity.getNome());
            contentValues.put(CandidateTable.COLUMN_SOBRENOME, entity.getSobrenome());
            contentValues.put(CandidateTable.COLUMN_EMAIL, entity.getEmail());
            contentValues.put(CandidateTable.COLUMN_TELEFONE, entity.getTelefone());
            contentValues.put(CandidateTable.COLUMN_CEP, entity.getCep());
            contentValues.put(CandidateTable.COLUMN_CARGO, entity.getCargo());
            contentValues.put(CandidateTable.COLUMN_DESCRICAO, entity.getDescricao());

            long id = db.insertOrThrow(CandidateTable.CANDIDATE_TABLE,null, contentValues);

            entity.setId(id);

            candidato = entity;
        }catch (Exception e){
            Log.e("Error CandidateDAO", e.getMessage());
        }finally {
            db.close();
        }
        return candidato;
    }

    @Override
    public int update(Candidate entity) {
        int countrows = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try{
            ContentValues contentValues = new ContentValues();

            contentValues.put(CandidateTable.COLUMN_ID_USER, entity.getIdUser());
            contentValues.put(CandidateTable.COLUMN_FOTO, entity.getFoto());
            contentValues.put(CandidateTable.COLUMN_NOME, entity.getNome());
            contentValues.put(CandidateTable.COLUMN_SOBRENOME, entity.getSobrenome());
            contentValues.put(CandidateTable.COLUMN_EMAIL, entity.getEmail());
            contentValues.put(CandidateTable.COLUMN_TELEFONE, entity.getTelefone());
            contentValues.put(CandidateTable.COLUMN_CEP, entity.getCep());
            contentValues.put(CandidateTable.COLUMN_CARGO, entity.getCargo());
            contentValues.put(CandidateTable.COLUMN_DESCRICAO, entity.getDescricao());

            countrows = db.update(CandidateTable.CANDIDATE_TABLE, contentValues, "_id = " + entity.getId(), null);
        }catch (Exception e){
            Log.e("Error CandidateDAO", e.getMessage());
        }finally {
            db.close();
        }
        return countrows;
    }

    @Override
    public int remove(Candidate entity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int countRows = 0;

        try{

            countRows = db.delete(CandidateTable.CANDIDATE_TABLE, "_id = " + entity.getId(), null);

        }catch (Exception e){
            Log.e("Error CandidateDAO", e.getMessage());
        }finally {
            db.close();
        }
        return countRows;
    }

    @Override
    public Candidate getById(long pk) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Candidate candidate = null;
        try{

            Cursor cursor = db.query(CandidateTable.CANDIDATE_TABLE, null,"_id = " + pk,null,null,null, CandidateTable.COLUMN_NOME + " asc");

            if (cursor.moveToNext()){
                candidate = new Candidate();

                candidate.setId(cursor.getLong(0));
                candidate.setIdUser(cursor.getLong(1));
                candidate.setFoto(cursor.getBlob(2));
                candidate.setNome(cursor.getString(3));
                candidate.setSobrenome(cursor.getString(4));
                candidate.setEmail(cursor.getString(5));
                candidate.setTelefone(cursor.getString(6));
                candidate.setCep(cursor.getString(7));
                candidate.setCargo(cursor.getString(8));
                candidate.setDescricao(cursor.getString(9));
            }

        }catch (Exception e){
            Log.e("Error CandidateDAO", e.getMessage());
        }finally {
            db.close();
        }

        return candidate;
    }

    @Override
    public List<Candidate> getAll() {
        List<Candidate> lista = new ArrayList<Candidate>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try{

            Cursor cursor = db.query(CandidateTable.CANDIDATE_TABLE, null, null,null,null,null, CandidateTable.COLUMN_NOME + " asc");

            while (cursor.moveToNext()){
                Candidate candidate = new Candidate();

                candidate.setId(cursor.getLong(0));
                candidate.setIdUser(cursor.getLong(1));
                candidate.setFoto(cursor.getBlob(2));
                candidate.setNome(cursor.getString(3));
                candidate.setSobrenome(cursor.getString(4));
                candidate.setEmail(cursor.getString(5));
                candidate.setTelefone(cursor.getString(6));
                candidate.setCep(cursor.getString(7));
                candidate.setCargo(cursor.getString(8));
                candidate.setDescricao(cursor.getString(9));

                lista.add(candidate);
            }

        }catch (Exception e){
            Log.e("Error CandidateDAO", e.getMessage());
        }finally {
            db.close();
        }

        return lista;
    }

    public List<Candidate> getAll(long idUser) {
        List<Candidate> lista = new ArrayList<Candidate>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try{

            Cursor cursor = db.query(CandidateTable.CANDIDATE_TABLE, null, CandidateTable.COLUMN_ID_USER + " = " + idUser,null,null,null, CandidateTable.COLUMN_NOME + " asc");

            while (cursor.moveToNext()){
                Candidate candidate = new Candidate();

                candidate.setId(cursor.getLong(0));
                candidate.setIdUser(cursor.getLong(1));
                candidate.setFoto(cursor.getBlob(2));
                candidate.setNome(cursor.getString(3));
                candidate.setSobrenome(cursor.getString(4));
                candidate.setEmail(cursor.getString(5));
                candidate.setTelefone(cursor.getString(6));
                candidate.setCep(cursor.getString(7));
                candidate.setCargo(cursor.getString(8));
                candidate.setDescricao(cursor.getString(9));

                lista.add(candidate);
            }

        }catch (Exception e){
            Log.e("Error CandidateDAO", e.getMessage());
        }finally {
            db.close();
        }

        return lista;
    }
}
