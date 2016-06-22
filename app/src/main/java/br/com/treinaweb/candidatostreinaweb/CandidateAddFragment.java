package br.com.treinaweb.candidatostreinaweb;

/**
 * Created by Edu on 11/02/2016.
 */
import java.io.ByteArrayOutputStream;

import br.com.treinaweb.candidatostreinaweb.dao.Candidate;
import br.com.treinaweb.candidatostreinaweb.dao.CandidateDao;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CandidateAddFragment extends Fragment {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    private Bitmap bitmapFoto = null;
    private long id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            Tela1 tela1 = (Tela1)activity;
            tela1.mDrawerToggle.setDrawerIndicatorEnabled(false);
        }catch(Exception e){

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_candidate_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_candidate_add, container, false);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.actionSave){
            save();
            return true;
        }
        else if (item.getItemId() == android.R.id.home){
            this.id = 0;
            Fragment fragment = new CandidateListFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        ImageView imgFoto = (ImageView) getView().findViewById(R.id.imageViewFoto);
        imgFoto.setClickable(true);
        imgFoto.setOnTouchListener(mOnTouchListener);

        if(id != 0 && bitmapFoto == null)
            loadCandidate();

        getActivity().getIntent().removeExtra("id");
    }

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
            }

            return true;
        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                bitmapFoto = (Bitmap) data.getExtras().get("data");

                ImageView imgFoto = (ImageView) getView().findViewById(R.id.imageViewFoto);
                imgFoto.setImageBitmap(bitmapFoto);
            }
        }
    }

    private void save(){
        EditText edNome = (EditText) getView().findViewById(R.id.editTextNome);
        EditText edSobrenome = (EditText) getView().findViewById(R.id.editTextSobrenome);
        EditText edEmail = (EditText) getView().findViewById(R.id.editTextEmail);
        EditText edTelefone = (EditText) getView().findViewById(R.id.editTextTelefone);
        EditText edCep = (EditText) getView().findViewById(R.id.editTextCep);
        EditText edCargo = (EditText) getView().findViewById(R.id.editTextCargo);
        EditText edDescricao = (EditText) getView().findViewById(R.id.editTextDescricao);

        String nome = edNome.getText().toString();
        String sobrenome = edSobrenome.getText().toString();
        String email = edEmail.getText().toString();
        String telefone = edTelefone.getText().toString();
        String cep = edCep.getText().toString();
        String cargo = edCargo.getText().toString();
        String descricao = edDescricao.getText().toString();

        Candidate candidato = new Candidate();

        if(bitmapFoto == null){
            ImageView imgFoto = (ImageView) getView().findViewById(R.id.imageViewFoto);
            try{
                bitmapFoto = ((BitmapDrawable)imgFoto.getDrawable()).getBitmap();
            }catch(ClassCastException ex){
                ex.printStackTrace();
                NinePatchDrawable np_drawable = ((NinePatchDrawable)imgFoto.getDrawable());
                bitmapFoto = Bitmap.createBitmap(np_drawable.getMinimumWidth(), np_drawable.getMinimumHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmapFoto);
                np_drawable.draw(canvas);
            }
        }

        if(bitmapFoto != null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapFoto.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArrayFoto = stream.toByteArray();
            candidato.setFoto(byteArrayFoto);
        }
        else{
            candidato.setFoto(null);
        }

        AuthenticationPreferences authPreferences = new AuthenticationPreferences(getActivity());
        candidato.setIdUser(authPreferences.getUser());

        candidato.setNome(nome);
        candidato.setSobrenome(sobrenome);
        candidato.setEmail(email);
        candidato.setTelefone(telefone);
        candidato.setCep(cep);
        candidato.setCargo(cargo);
        candidato.setDescricao(descricao);

        try{
            CandidateDao dao = new CandidateDao(getActivity().getApplicationContext());
            if(id != 0)
            {
                candidato.setId(id);
                dao.update(candidato);

                Toast.makeText(getActivity(), "Candidado alterado com sucesso!", Toast.LENGTH_LONG).show();

                Fragment fragment = new CandidateListFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment).commit();
            }
            else{
                dao.save(candidato);
                Toast.makeText(getActivity(), "Candidado salvo com sucesso!", Toast.LENGTH_LONG).show();
            }

            clean();
        }catch(Exception e){
            Log.e("error sqlite", e.getMessage());
            e.printStackTrace();
        }
    }

    public void clean(){
        ImageView imgFoto = (ImageView) getView().findViewById(R.id.imageViewFoto);
        EditText edNome = (EditText) getView().findViewById(R.id.editTextNome);
        EditText edSobrenome = (EditText) getView().findViewById(R.id.editTextSobrenome);
        EditText edEmail = (EditText) getView().findViewById(R.id.editTextEmail);
        EditText edTelefone = (EditText) getView().findViewById(R.id.editTextTelefone);
        EditText edCep = (EditText) getView().findViewById(R.id.editTextCep);
        EditText edCargo = (EditText) getView().findViewById(R.id.editTextCargo);
        EditText edDescricao = (EditText) getView().findViewById(R.id.editTextDescricao);

        //limpando campos
        imgFoto.setImageResource(android.R.drawable.alert_dark_frame);
        edNome.setText("");
        edSobrenome.setText("");
        edEmail.setText("");
        edTelefone.setText("");
        edCep.setText("");
        edCargo.setText("");
        edDescricao.setText("");
        this.id = 0;
    }

    public void setId(long id){
        this.id = id;
    }

    public void loadCandidate()
    {
        CandidateDao dao = new CandidateDao(getActivity().getApplicationContext());
        Candidate candidato = dao.getById(id);

        if(candidato.getFoto() != null){
            byte[] byteArray = candidato.getFoto();
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            ImageView imgFoto = (ImageView) getView().findViewById(R.id.imageViewFoto);
            imgFoto.setImageBitmap(bitmap);
        }

        String nome = candidato.getNome();
        String sobrenome = candidato.getSobrenome();
        String email = candidato.getEmail();
        String telefone = candidato.getTelefone();
        String cep = candidato.getCep();
        String cargo = candidato.getCargo();
        String descricao = candidato.getDescricao();

        EditText edTextNome = (EditText) getView().findViewById(R.id.editTextNome);
        EditText edTextSobrenome = (EditText) getView().findViewById(R.id.editTextSobrenome);
        EditText edTextEmail = (EditText) getView().findViewById(R.id.editTextEmail);
        EditText edTextTelefone = (EditText) getView().findViewById(R.id.editTextTelefone);
        EditText edTextCep = (EditText) getView().findViewById(R.id.editTextCep);
        EditText edTextCargo = (EditText) getView().findViewById(R.id.editTextCargo);
        EditText edTextDescricao = (EditText) getView().findViewById(R.id.editTextDescricao);

        edTextNome.setText(nome);
        edTextSobrenome.setText(sobrenome);
        edTextEmail.setText(email);
        edTextTelefone.setText(telefone);
        edTextCep.setText(cep);
        edTextCargo.setText(cargo);
        edTextDescricao.setText(descricao);
    }
}