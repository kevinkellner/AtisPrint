package edu.kit.uneig.atisprint.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import edu.kit.uneig.atisprint.R;

/**
 * @author Kevin Kellner
 * @version 1.0
 */
public class LoginPromptActivity extends Activity {

    /**
     * The username and password will be saved in this intent.
     */
    protected Intent returnIntent;

    /**
     * This is the value that will be returned by the SharedPreferences if a username or password is not saved yet.
     */
    protected static final String NO_VALUE = "-";
    /**
     * This class will be used to access the database of this application.
     */
    private SharedPreferences prefs;
    private ObscuredSharedPreferences secPrefs;
    private EditText tfUsername;
    private EditText tfPassword;
    private CheckBox chkSavePw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        returnIntent = new Intent();
        prefs = getApplicationContext().getSharedPreferences("AtisPrint", MODE_PRIVATE);
        secPrefs = ObscuredSharedPreferences.getPrefs(getApplicationContext(), "AtisPrint", MODE_PRIVATE);
        setUserData();
    }


    private void setUserData() {
        setContentView(R.layout.activity_sign_in);
        tfUsername = (EditText) findViewById(R.id.tfUsername);
        tfPassword = (EditText) findViewById(R.id.tfPassword);
        chkSavePw = (CheckBox) findViewById(R.id.chkSavePw);

        String username = prefs.getString("username", NO_VALUE);
        String password = secPrefs.getString("password", NO_VALUE);

        //check if there is already a saved username and/or password and set them into the text fields.
        if (!username.equals(NO_VALUE)) tfUsername.setText(username);
        if (!password.equals(NO_VALUE)) tfPassword.setText(password);
        chkSavePw.setChecked(prefs.getBoolean("savePw", false));
    }

    /**
     * This method is called when the user clicks the 'Ok' button within the password and user name prompt that appears
     * when trying to set the credentials.
     *
     * @param w the view where the user enters his credentials.
     */
    public void onClickOk(View w) {
        String password = tfPassword.getText().toString();
        String username = tfUsername.getText().toString();
        boolean savePw = chkSavePw.isChecked();

        SharedPreferences.Editor editor = prefs.edit();

        //save username in plaintext
        editor.putString("username", username);
        editor.putBoolean("savePw", savePw);
        editor.apply(); //save now because we may need to change the editor to store the password

        //use ObscuredSharedPreferences to encrypt password
        if (savePw) {
            editor = secPrefs.edit();
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
     *
     * @param username the username to check
     * @return true if the username matches the following regex {@code s_[a-zA-Z]*}
     */
    private boolean isValidUsername(String username) {
        String regex = "s_[a-zA-Z]*";
        return username.matches(regex);
    }
}
