package edu.kit.uneig.atisprint.login;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class is used to read and write to the application preference storage.
 * There are two types of methods; the Secure methods use the ObscuredSharedPreferences to decrypt/encrypt the data
 * while saving/reading. The normal getters/setters don't.
 *
 * @author Kevin Kellner
 * @version 1.0
 */
public class PreferencesWrapper {
    private SharedPreferences prefs;
    private SharedPreferences securePrefs;

    /**
     * Constructor
     *
     * @param context the context
     */
    public PreferencesWrapper(Context context) {
        prefs = context.getSharedPreferences("AtisPrint", Context.MODE_PRIVATE);
        securePrefs = ObscuredSharedPreferences.getPrefs(context.getApplicationContext(),
                "AtisPrint", Context.MODE_PRIVATE);

    }

    /**
     * Returns the String value that is saved with the specified key or defValue if the key is not found and
     * decrypts it with the ANDROID_ID
     *
     * @param key      the key
     * @param defValue the default value
     * @return the string value that is saved with the key, or default value.
     */
    public String getStringSecure(String key, String defValue) {
        return securePrefs.getString(key, defValue);
    }

    /**
     * Saves the String with the key and encrypts it with the ANDROID_ID before saving it to the preferences
     *
     * @param key   the key
     * @param value the value
     */
    public void setStringSecure(String key, String value) {
        SharedPreferences.Editor editor = securePrefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Returns the String value that is saved with the specified key or defValue if the key is not found.
     *
     * @param key      the key
     * @param defValue the default value
     * @return the string value that is saved with the key, or default value.
     */
    public String getString(String key, String defValue) {
        return prefs.getString(key, defValue);
    }

    /**
     * Sets the key to the corresponding String value
     *
     * @param key   the key
     * @param value the value
     */
    public void setString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Returns the boolean value that is saved with the specified key or defValue if the key is not found.
     *
     * @param key      the key
     * @param defValue the default value
     * @return the boolean value that is saved with the key, or default value.
     */
    public boolean getBoolean(String key, boolean defValue) {
        return prefs.getBoolean(key, defValue);
    }

    /**
     * Sets the key to the corresponding boolean value.
     *
     * @param key   the key
     * @param value the value
     */
    public void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * Removes the specified key from the database
     *
     * @param key the key that is to be removed
     */
    public void remove(String key) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.apply();
    }
}
