package edu.kit.uneig.atisprint.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * @author Kevin Kellner
 * @version 1.0
 */
public abstract class LoginActivity extends Activity {
    /**
     * This is the value that will be returned by the SharedPreferences if a username or password is not saved yet.
     */
    protected static final String NO_VALUE = "-";
    /**
     * This instance of SharedPreferences is used to access the preference storage. The ObscuredSharedPreferences wrapper class
     * will be used to encrypt everything that will be saved there with the ANDROID_ID
     */
    protected SharedPreferences settings;
    /**
     * The username and password will be saved in this intent.
     */
    protected Intent returnIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        returnIntent = new Intent();

        //this will automatically encrypt the username and password with the ANDROID_ID when saving it
        settings = new ObscuredSharedPreferences(this, this.getPreferences(MODE_PRIVATE));
    }

    /**
     * Retrieves the user name saved in the preferences, returns NO_VALUE if the user name is not stored.
     * @return the user name saved in the preferences, return NO_VALUE if the user name is not stored.
     */
    public String getUsername() {
        String username = settings.getString("username", NO_VALUE);
        return username;
    }

    /**
     * Retrieves the password saved in the preferences, returns NO_VALUE if the password is not stored.
     * @return the password saved in the preferences, return NO_VALUE if the password is not stored.
     */
    public String getPassword() {
        String password = settings.getString("password", NO_VALUE);
        return password;
    }
}
