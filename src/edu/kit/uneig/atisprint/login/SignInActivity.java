package edu.kit.uneig.atisprint.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import edu.kit.uneig.atisprint.R;

public class SignInActivity extends Activity {

    /**
     * This is the value that will be returned by the SharedPreferences if a username or password is not saved yet.
     */
    protected static final String NO_VALUE = "-";
    public static final int SET_USERDATA = 0x00;
    public static final int GET_USERDATA = 0x01;
    /**
     * This instance of SharedPreferences is used to access the preference storage. The ObscuredSharedPreferences wrapper class
     * will be used to encrypt everything that will be saved there with the ANDROID_ID
     */
    protected SharedPreferences settings;

    /**
     * The username and password will be saved in this intent.
     */
    protected Intent returnIntent;
    private EditText tfUsername;
    private EditText tfPassword;

    private CheckBox chkSavePw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        returnIntent = new Intent();
        //this will automatically encrypt the username and password with the ANDROID_ID when saving it
        settings = new ObscuredSharedPreferences(getApplicationContext(), this.getPreferences(MODE_PRIVATE));
        int mode = getIntent().getIntExtra("mode", SET_USERDATA);

        if (mode == SET_USERDATA) setUserData();
        if (mode == GET_USERDATA) returnUserData();
    }

    /**
     * Tries to return the user data if it is stored. If the password or the username are not saved
     * the result code {@code RESULT_CANCELED} will be returned.
     */
    private void returnUserData() {
        String username = getUsername();
        String password = getPassword();
        //both username and pw is saved, so return them
        if (!username.equals(NO_VALUE) && !password.equals(NO_VALUE)) {
            returnIntent.putExtra("username", username);
            returnIntent.putExtra("password", password);
            setResult(RESULT_OK, returnIntent);
            finish();
        } else {
            setUserData();
        }

    }

    private void setUserData() {
        setContentView(R.layout.activity_sign_in);
        tfUsername = (EditText) findViewById(R.id.tfUsername);
        tfPassword = (EditText) findViewById(R.id.tfPassword);
        chkSavePw = (CheckBox) findViewById(R.id.chkSavePw);

        String username = getUsername();
        String password = getPassword();

        //check if there is already a saved username and/or password and set them into the text fields.
        if (!username.equals(NO_VALUE)) tfUsername.setText(username);
        if (!password.equals(NO_VALUE)) tfPassword.setText(password);
    }

    /**
     * This method is called when the user clicks the 'Ok' button within the password and user name prompt that appears
     * when trying to set the credentials.
     * @param w the view where the user enters his credentials.
     */
    public void onClickOk(View w) {
        String password = tfPassword.getText().toString();
        String username = tfUsername.getText().toString();
        boolean savePw = chkSavePw.isChecked();

        SharedPreferences.Editor editor = settings.edit();

        //save username and password in preferences
        editor.putString("username", username);

        if (savePw) {
            editor.putString("password", password);
        } else {
            editor.remove("password");
        }

        editor.apply();

        //return username and pw as result of the activity
        returnIntent.putExtra("username", username);
        returnIntent.putExtra("password", password);

        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void onClickCancel(View view) {
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }

    /**
     * Checks whether a certain user name is valid by matching it with the criteria of the ATIS account creation
     * @param username the username to check
     * @return true if the username matches the following regex {@code s_[a-zA-Z]*}
     */
    private boolean isValidUsername(String username) {
        String regex = "s_[a-zA-Z]*";
        return username.matches(regex);
    }

    /**
     * Retrieves the user name saved in the preferences, returns NO_VALUE if the user name is not stored.
     * @return the user name saved in the preferences, return NO_VALUE if the user name is not stored.
     */
    public String getUsername() {
        return settings.getString("username", NO_VALUE);
    }

    /**
     * Retrieves the password saved in the preferences, returns NO_VALUE if the password is not stored.
     * @return the password saved in the preferences, return NO_VALUE if the password is not stored.
     */
    public String getPassword() {
        return settings.getString("password", NO_VALUE);
    }


}
