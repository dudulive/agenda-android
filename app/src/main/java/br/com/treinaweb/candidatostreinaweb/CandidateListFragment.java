package br.com.treinaweb.candidatostreinaweb;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import br.com.treinaweb.candidatostreinaweb.dao.Candidate;
import br.com.treinaweb.candidatostreinaweb.dao.CandidateDao;

public class CandidateListFragment extends Fragment {
    private CandidateAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_candidate, container, false);

        ImageButton btnAdd = (ImageButton) view.findViewById(R.id.imagebutton_add);
        btnAdd.setOnClickListener(mOnClickListener);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            Tela1 tela1 = (Tela1)activity;
            tela1.mDrawerToggle.setDrawerIndicatorEnabled(true);
        }catch(Exception e){

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        try{
            AuthenticationPreferences authPreferences = new AuthenticationPreferences(getActivity().getApplicationContext());
            long idUser = authPreferences.getUser();

            CandidateDao dao = new CandidateDao(getActivity().getApplicationContext());
            List<Candidate> data = dao.getAll(idUser);

            mAdapter = new CandidateAdapter(getActivity().getApplicationContext(), R.layout.listitem_candidate, data);

            ListView list = (ListView) getView().findViewById(R.id.listViewCaditatos);
            list.setAdapter(mAdapter);
            list.setOnItemClickListener(mOnItemClickListener);

        } catch (Exception e) {
            Log.e("Error List", e.getMessage());
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Fragment fragment = new CandidateAddFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment, "Add");
            fragmentTransaction.commit();

            getActivity().getIntent().removeExtra("id");
        }
    };

    AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener(){
        ImageView mImageView;

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            mImageView = (ImageView) view.findViewById(R.id.imageViewFoto);

            Intent intent = new Intent(getActivity(), CandidateDetailActivity.class);
            intent.putExtra("id", id);

            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT)
                showDetail(intent);
            else
                showDetailApi21(intent);
        }

        private void showDetail(Intent intent) {
            startActivity(intent);
        }

        @TargetApi(21)
        private void showDetailApi21(Intent intent) {
            ((ViewGroup) mImageView.getParent()).setTransitionGroup(false);
            mImageView.setTransitionName("imageViewFoto");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ( (BitmapDrawable) mImageView.getDrawable() ).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
            intent.putExtra("image", stream.toByteArray());

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), mImageView, "imageViewFoto" );

            startActivity(intent, options.toBundle());
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_list_candidate, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        v.setImageResource(R.mipmap.ic_search_white);

        int crossId = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);            // Getting the 'search_plate' LinearLayout.
        ImageView image = (ImageView) searchView.findViewById(crossId);
        image.setImageResource(R.mipmap.ic_clear_white);

        searchView.setOnQueryTextListener(mQueryTextListener);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private SearchView.OnQueryTextListener mQueryTextListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String query) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            AuthenticationPreferences authPreferences = new AuthenticationPreferences(getActivity().getApplicationContext());
            long idUser = authPreferences.getUser();

            CandidateDao dao = new CandidateDao(getActivity().getApplicationContext());
            List<Candidate> data = dao.getAll(idUser);
            List<Candidate> filtered = filter(newText, data);
            mAdapter.setData(filtered);
            return false;
        }
    };

    public List<Candidate> filter(String text, List<Candidate> target) {
        List<Candidate> result = new ArrayList<Candidate>();
        for (Candidate element: target) {
            if (element.getNome().toLowerCase().contains(text.toLowerCase())
                    || element.getSobrenome().toLowerCase().contains(text.toLowerCase())) {
                result.add(element);
            }
        }
        return result;
    }
}