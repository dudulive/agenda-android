package br.com.treinaweb.candidatostreinaweb;

import java.util.ArrayList;
import java.util.List;

import br.com.treinaweb.candidatostreinaweb.dao.User;
import br.com.treinaweb.candidatostreinaweb.dao.UserDao;

import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class Tela1 extends BaseActivity {
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    DrawerItemAdapter mDrawerAdapter;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela1);

        AuthenticationPreferences authPreferences = new AuthenticationPreferences(this);
        if(authPreferences.getUser() == 0){
            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivity(intent);
        }

        mDrawerList = (ListView) findViewById(R.id.list_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        List<DrawerItem> lista = new ArrayList<DrawerItem>();

        lista.add(new DrawerItem("", 0));
        lista.add(new DrawerItem("Listagem", R.mipmap.ic_list_white));
        lista.add(new DrawerItem("Conta", R.mipmap.ic_account_box_white));
        lista.add(new DrawerItem("Logout", R.mipmap.ic_exit_to_app_white));

        mDrawerAdapter = new DrawerItemAdapter(getApplicationContext(), R.layout.listitem_navigation_drawer, lista);

        mDrawerList.setAdapter(mDrawerAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // TODO Auto-generated method stub
                invalidateOptionsMenu();
            }

        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(mOnItemClickListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(getFragmentManager().findFragmentByTag("Add") == null && getFragmentManager().findFragmentByTag("Profile") == null){
            if(!getIntent().hasExtra("id")){
                Fragment fragment = new CandidateListFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
            else if(getIntent().hasExtra("id")){
                CandidateAddFragment fragment = new CandidateAddFragment();
                long id = getIntent().getLongExtra("id", 0);
                fragment.setId(id);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "Add").commit();
            }
        }
    }

    AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            mDrawerLayout.closeDrawers();
            FragmentManager fragmentManager = getFragmentManager();
            switch((int) id){
                case 1:
                    Fragment lista = new CandidateListFragment();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, lista).commit();
                    break;
                case 2:
                    Fragment profile = new ProfileFragment();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, profile, "Profile").commit();
                    break;
                case 3:
                    ConfirmDialogFragment confirm = new ConfirmDialogFragment();
                    confirm.show(fragmentManager, "Confirm");
                    break;
            }
        }
    };

    private class ConfirmDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Logout");
            builder.setMessage("Tem certeza que gostaria de sair?")
                    .setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            AuthenticationPreferences authPreferences = new AuthenticationPreferences(getActivity());
                            long idUser = authPreferences.getUser();
                            UserDao dao = new UserDao(getActivity());
                            User user = dao.getById(idUser);

                            //invalidando token
                            AccountManager accountManager = AccountManager.get(getActivity());
                            accountManager.invalidateAuthToken("com.google", user.getToken());

                            finish();
                        }
                    })
                    .setNegativeButton("Continuar", null);

            return builder.create();
        }
    }
}