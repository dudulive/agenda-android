package br.com.treinaweb.candidatostreinaweb;

/**
 * Created by Edu on 16/02/2016.
 */
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.treinaweb.candidatostreinaweb.dao.User;
import br.com.treinaweb.candidatostreinaweb.dao.UserDao;

public class ProfileFragment extends Fragment {
    static final int PICK_IMAGE_REQUEST = 100;
    User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageButton btnAdd = (ImageButton) view.findViewById(R.id.imagebutton_edit);
        btnAdd.setOnClickListener(mOnClickListener);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        AuthenticationPreferences authPreferences = new AuthenticationPreferences(getActivity().getApplicationContext());
        UserDao dao = new UserDao(getActivity().getApplicationContext());
        user = dao.getById(authPreferences.getUser());

        ImageView imgAvatar = (ImageView) getView().findViewById(R.id.imageViewAvatar);
        ImageView imgPerfil = (ImageView) getView().findViewById(R.id.imageViewProfile);
        TextView textUser = (TextView) getView().findViewById(R.id.textViewUser);
        TextView textEmail = (TextView) getView().findViewById(R.id.textViewEmail);

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
        textEmail.setText(user.getEmail());
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                Uri imageUri = data.getData();

                ImageView imgPerfil = (ImageView) getView().findViewById(R.id.imageViewProfile);
                imgPerfil.setImageURI(imageUri);

                InputStream imageStream = null;
                try {
                    imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Bitmap bmp = BitmapFactory.decodeStream(imageStream);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                try {
                    stream.close();
                    stream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(user != null){
                    UserDao dao = new UserDao(getActivity().getApplicationContext());

                    user.setImgPerfil(byteArray);
                    dao.update(user);
                }

                ((Tela1) getActivity()).mDrawerAdapter.notifyDataSetChanged();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}