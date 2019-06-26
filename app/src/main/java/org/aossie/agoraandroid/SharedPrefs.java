package org.aossie.agoraandroid;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    private static final String myPrefs = "myprefs";
    private static final String UserNameKey = "userName";
    private static final String EmailKey = "emaiId";
    private static final String FullNameKey = "fullName";
    private final Context context;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;


    public SharedPrefs(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(myPrefs, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    //Save USerName in sharedPref and get Username
    public void saveUserName(String key) {
        editor.putString(UserNameKey, key);
        editor.commit();
    }

    public String getUserName() {
        return sharedPreferences.getString(UserNameKey, null);
    }


    //Save Email in sharedPref and get Email
    public void saveEmail(String email) {
        editor.putString(EmailKey, email);
        editor.commit();
    }

    public String getEmail() {
        return sharedPreferences.getString(EmailKey, null);
    }


    //Save firstName and lastName as fullName and get fullName
    public void saveFullName(String firstName, String lastName) {
        editor.putString(FullNameKey, firstName + " " + lastName);
        editor.commit();
    }

    public String getfullName() {
        return sharedPreferences.getString(FullNameKey, null);
    }


    public void clearLogin() {
        editor.putString(UserNameKey, null);
        editor.putString(EmailKey, null);
        editor.putString(FullNameKey, null);
        editor.commit();
    }
}
