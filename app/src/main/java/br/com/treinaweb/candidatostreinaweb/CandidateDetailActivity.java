package br.com.treinaweb.candidatostreinaweb;

/**
 * Created by Edu on 13/02/2016.
 */
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.treinaweb.candidatostreinaweb.dao.Candidate;
import br.com.treinaweb.candidatostreinaweb.dao.CandidateDao;

public class CandidateDetailActivity extends BaseActivity {
    ImageView mImageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mImageView = (ImageView) findViewById(R.id.imageViewFoto);

        if(getIntent().hasExtra("image")){
            byte[] byteArray = getIntent().getByteArrayExtra("image");
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            mImageView.setImageBitmap(bitmap);
        }

        ImageButton imgBtn = (ImageButton) findViewById(R.id.imagebutton_edit);
        imgBtn.setOnClickListener(mOnClickListener);

        long id = getIntent().getLongExtra("id", 0);
        loadCandidate(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        else if(item.getItemId() == R.id.action_delete)
        {
            getSupportActionBar().startActionMode(mCallback);
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadCandidate(long id) {
        String nome = "";
        String sobrenome = "";
        String email = "";
        String telefone = "";
        String cep = "";
        String cargo = "";
        String descricao = "";

        CandidateDao dao = new CandidateDao(getApplicationContext());

        Candidate candidato = dao.getById(id);

        if(!getIntent().hasExtra("image")){
            byte[] byteArray = candidato.getFoto();
            if(byteArray != null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                mImageView.setImageBitmap(bitmap);
            }
        }

        nome = candidato.getNome();
        sobrenome = candidato.getSobrenome();
        email = candidato.getEmail();
        telefone = candidato.getTelefone();
        cep = candidato.getCep();
        cargo = candidato.getCargo();
        descricao = candidato.getDescricao();

        TextView txtViewNome = (TextView) findViewById(R.id.textViewNome);
        TextView txtViewSobrenome = (TextView) findViewById(R.id.textViewSobrenome);
        TextView txtViewEmail = (TextView) findViewById(R.id.textViewEmail);
        TextView txtViewTelefone = (TextView) findViewById(R.id.textViewTelefone);
        TextView txtViewCep = (TextView) findViewById(R.id.textViewCep);
        TextView txtViewCargo = (TextView) findViewById(R.id.textViewCargo);
        TextView txtViewDescricao = (TextView) findViewById(R.id.textViewDescricao);

        txtViewNome.setText(nome);
        txtViewSobrenome.setText(sobrenome);
        txtViewEmail.setText(email);
        txtViewTelefone.setText(telefone);
        txtViewCep.setText(cep);
        txtViewCargo.setText(cargo);
        txtViewDescricao.setText(descricao);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(CandidateDetailActivity.this, Tela1.class);

            long id = getIntent().getLongExtra("id", 0);
            intent.putExtra("id", id);

            startActivity(intent);
        }
    };

    private ActionMode.Callback mCallback = new ActionMode.Callback() {

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }

        @Override
        public void onDestroyActionMode(ActionMode mode) { }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.context_menu_delete, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if(item.getItemId() == R.id.action_cancel){
                mode.finish();
            }
            else if(item.getItemId() == R.id.action_confirm){
                mode.finish();
                CandidateDao dao = new CandidateDao(getApplicationContext());
                long id = getIntent().getLongExtra("id", 0);
                Candidate candidate = new Candidate();
                candidate.setId(id);
                int rows = dao.remove(candidate);
                if(rows > 0){
                    Toast.makeText(CandidateDetailActivity.this, "Candidato excluído com sucesso", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(CandidateDetailActivity.this, Tela1.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
            return false;
        }
    };

}
