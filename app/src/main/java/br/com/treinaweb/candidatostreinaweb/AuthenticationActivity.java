package br.com.treinaweb.candidatostreinaweb;

/**
 * Created by Edu on 02/02/2016.
 */
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.common.AccountPicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import br.com.treinaweb.candidatostreinaweb.common.NetworkUtils;
import br.com.treinaweb.candidatostreinaweb.dao.User;
import br.com.treinaweb.candidatostreinaweb.dao.UserDao;

public class AuthenticationActivity extends Activity {

    private static final int AUTHORIZATION_CODE = 1001;
    private static final int ACCOUNT_CODE = 1002;
    private AccountManager accountManager;
    private final String SCOPE = "https://www.googleapis.com/auth/userinfo.profile";
    private User user;
    private ProgressBar barProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autentication);

        barProgress = (ProgressBar) findViewById(R.id.progressBar1);
        barProgress.setVisibility(View.GONE);

        accountManager = AccountManager.get(this);
    }

    @Override
    protected void onStart(){
        super.onStart();

        barProgress.setVisibility(View.VISIBLE);

        AuthenticationPreferences authPreferences = new AuthenticationPreferences(this);
        if(authPreferences.getUser() > 0){
            UserDao dao = new UserDao(this);
            user = dao.getById(authPreferences.getUser());
            authenticated();
        }
        else{
            user = new User();
            chooseAccount();
        }
    }

    private void chooseAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, ACCOUNT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == AUTHORIZATION_CODE) {
                requestToken();
            } else if (requestCode == ACCOUNT_CODE) {
                String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

                UserDao dao = new UserDao(this);
                User existUser = dao.getByEmail(accountName);

                if(existUser != null)
                    user = existUser;
                else
                    user.setEmail(accountName);

                invalidateToken();
                requestToken();
            }
        }
    }

    private void invalidateToken() {
        AccountManager accountManager = AccountManager.get(this);
        accountManager.invalidateAuthToken("com.google", user.getToken());

        user.setToken(null);
    }

    private void requestToken() {
        Account userAccount = null;
        String ususario = user.getEmail();
        for (Account account : accountManager.getAccountsByType("com.google")) {
            if (account.name.equals(ususario)) {
                userAccount = account;
                break;
            }
        }
        accountManager.getAuthToken(userAccount, "oauth2:" + SCOPE, null, this,
                mOnTokenAcquired, null);
    }

    private AccountManagerCallback<Bundle> mOnTokenAcquired = new AccountManagerCallback<Bundle>() {

        @Override
        public void run(AccountManagerFuture<Bundle> result) {
            try {
                Bundle bundle = result.getResult();

                Intent launch = (Intent) bundle.get(AccountManager.KEY_INTENT);
                if (launch != null) {
                    startActivityForResult(launch, AUTHORIZATION_CODE);
                } else {
                    String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                    user.setToken(token);

                    authenticated();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    };

    private void authenticated() {
        if(user.getName() != null){
            Intent intent = new Intent(this, Tela1.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else
            new GetUserTask().execute(String.format("https://www.googleapis.com/oauth2/v1/userinfo?access_token=%s", user.getToken()));
    }

    private class GetUserTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            readJson(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            barProgress.setVisibility(View.GONE);
            Intent intent = new Intent(getApplicationContext(), Tela1.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void readJson(String url) {

        InputStream is = null;
        try {
            is = NetworkUtils.OpenHttpConnection(url, this);

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder jsonStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                jsonStrBuilder.append(inputStr);

            JSONObject jObj = new JSONObject(jsonStrBuilder.toString());
            user.setName(jObj.getString("name"));

            String avatar = jObj.getString("picture");
            byte[] img = getImage(avatar);
            user.setAvatar(img);

            byte[] imgPerfil = getAssetFile("profile.jpg");
            user.setImgPerfil(imgPerfil);

            UserDao dao = new UserDao(this);

            if(user.getId() == 0)
                user = dao.save(user);
            else
                dao.update(user);

            AuthenticationPreferences authPreferences = new AuthenticationPreferences(this);
            authPreferences.setUser(user.getId());

        }catch(IOException ie){
            Log.i("readJson", ie.getLocalizedMessage());
        } catch (JSONException e) {
            Log.i("readJson", e.getLocalizedMessage());
        }
    }

    private byte[] getImage(String avatar){
        InputStream is;
        byte[] img = null;
        try{
            is = NetworkUtils.OpenHttpConnection(avatar, this);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            img = buffer.toByteArray();
        }
        catch(IOException ie){
            Log.i("readJson", ie.getLocalizedMessage());
            ie.printStackTrace();
        }
        return img;
    }

    private byte[] getAssetFile(String filePath){
        InputStream is;
        byte[] file = null;
        try{
            is = getAssets().open(filePath);
            int size = is.available();
            file = new byte[size];
            is.read(file);
            is.close();
        }catch(IOException ie){
            Log.i("readFile", ie.getLocalizedMessage());
            ie.printStackTrace();
        }
        return file;
    }
}
