package br.com.treinaweb.candidatostreinaweb;

/**
 * Created by Edu on 02/02/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AuthenticationPreferences {
    private static final String KEY_USER = "Key_User";
    private SharedPreferences preferences;

    public AuthenticationPreferences(Context context) {
        preferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
    }

    public void setUser(long userId) {
        Editor editor = preferences.edit();
        editor.putLong(KEY_USER, userId);
        editor.commit();
    }

    public long getUser() {
        return preferences.getLong(KEY_USER, 0);
    }
}
