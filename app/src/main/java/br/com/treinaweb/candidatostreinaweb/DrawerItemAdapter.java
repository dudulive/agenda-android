package br.com.treinaweb.candidatostreinaweb;

/**
 * Created by Edu on 01/02/2016.
 */
import java.util.List;

import br.com.treinaweb.candidatostreinaweb.dao.User;
import br.com.treinaweb.candidatostreinaweb.dao.UserDao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerItemAdapter extends ArrayAdapter<DrawerItem> {
    List<DrawerItem> data;
    Context context;
    int layoutResourceId;

    public DrawerItemAdapter(Context context, int layoutResourceId, List<DrawerItem> data){
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(position == 0){
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            view = vi.inflate(R.layout.fragment_listitem_profile, parent, false);

            AuthenticationPreferences authPreferences = new AuthenticationPreferences(context);
            UserDao dao = new UserDao(context);
            User user = dao.getById(authPreferences.getUser());

            if(user != null){
                ImageView imgAvatar = (ImageView) view.findViewById(R.id.imageViewAvatar);
                ImageView imgPerfil = (ImageView) view.findViewById(R.id.imageViewProfile);
                TextView textUser = (TextView) view.findViewById(R.id.textViewUser);

                Bitmap bitmap;
                byte[] bitmapdata;
                if(user.getAvatar() != null){
                    bitmapdata = user.getAvatar();

                    bitmap = BitmapFactory.decodeByteArray(bitmapdata , 0, bitmapdata.length);
                    imgAvatar.setImageBitmap(bitmap);
                }

                if(user.getImgPerfil() != null){
                    bitmapdata = user.getImgPerfil();
                    bitmap = BitmapFactory.decodeByteArray(bitmapdata , 0, bitmapdata.length);
                    imgPerfil.setImageBitmap(bitmap);
                }

                textUser.setText(user.getName());
            }
        }
        else if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            view = vi.inflate(R.layout.listitem_navigation_drawer, parent, false);

            TextView titulo = (TextView) view.findViewById(R.id.TextViewTitle);
            ImageView icone = (ImageView) view.findViewById(R.id.ImageViewIcon);

            DrawerItem item = data.get(position);

            titulo.setText(item.getTitulo());
            icone.setImageResource(item.getIcone());
        }

        return view;
    }

    public void setData(List<DrawerItem> data){
        this.clear();
        this.addAll(data);
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public DrawerItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}