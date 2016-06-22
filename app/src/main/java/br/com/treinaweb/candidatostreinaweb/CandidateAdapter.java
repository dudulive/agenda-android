package br.com.treinaweb.candidatostreinaweb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.treinaweb.candidatostreinaweb.dao.Candidate;

/**
 * Created by Edu on 11/02/2016.
 */
public class CandidateAdapter extends ArrayAdapter<Candidate> {

    List<Candidate> data;
    Context context;
    int layoutResourceId;

    public CandidateAdapter(Context context, int resource, List<Candidate> data) {
        super(context, resource, data);
        this.context = context;
        this.layoutResourceId = resource;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            view = vi.inflate(layoutResourceId, parent, false);
        }

        TextView txtId = (TextView) view.findViewById(R.id.textViewId);
        TextView txtNome = (TextView) view.findViewById(R.id.textViewNome);
        TextView txtCargo = (TextView) view.findViewById(R.id.textViewCargo);
        ImageView imgFoto = (ImageView) view.findViewById(R.id.imageViewFoto);

        Candidate candidato = data.get(position);

        String id = String.valueOf(candidato.getId());
        txtId.setText(id);

        String nome = String.format("%s %s", candidato.getNome(), candidato.getSobrenome());
        txtNome.setText(nome);

        String cargo = candidato.getCargo();
        txtCargo.setText(cargo);

        byte[] img = candidato.getFoto();
        if(img != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            imgFoto.setImageBitmap(bitmap);
        }

        return view;
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    public void setData(List<Candidate> data){
        this.clear();
        this.addAll(data);
        this.data = data;
        this.notifyDataSetChanged();
    }
}
