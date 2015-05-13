package edu.kit.uneig.atisprint;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class SigninActivity extends Activity {
    private static final String NO_VALUE = "-";
    public static final int SET_USERDATA = 0x00;
    public static final int GET_USERDATA = 0x01;
    
    private EditText tfUsername;
    private EditText tfPassword;
    private CheckBox chkSavePw;
    private SharedPreferences settings;
    private String username;
    private String password;
    
    /**
     * The user name and password will be saved in this intent.
     */
    private Intent returnIntent;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int mode = getIntent().getIntExtra("mode", SET_USERDATA);
        returnIntent = new Intent();
        returnIntent.putExtra(Intent.EXTRA_STREAM, getIntent().getParcelableExtra(Intent.EXTRA_STREAM));
        
        //this will automatically encrypt the username and password with the ANDROID_ID when saving it
        settings = new ObscuredSharedPreferences(this, this.getPreferences(MODE_PRIVATE));
        
        username = getUsername();
        password = getPassword();
        
        if (mode == SET_USERDATA) setUserData();
        if (mode == GET_USERDATA) returnUserData();

    }
    /**
     * Tries to return the user data if it is stored. If the password or the username are not saved, the setUserData method will be called, which
     * will in turn ask the user to enter his credentials.
     */
    private void returnUserData() {
        //both username and pw is saved, so return them
        if (username != NO_VALUE && password != NO_VALUE) {
            returnIntent.putExtra("username", username);
            returnIntent.putExtra("password", password);
            setResult(RESULT_OK, returnIntent);
            finish();
        } else {
            //either username or pw is not saved, so retrieve them by asking the user for them
            setUserData();
        }
    }
    
    private void setUserData() {
        setContentView(R.layout.activity_signin);
        tfUsername = (EditText) findViewById(R.id.tfUsername);
        tfPassword = (EditText) findViewById(R.id.tfPassword);
        chkSavePw = (CheckBox) findViewById(R.id.chkSavePw);
        
        if (username != NO_VALUE) tfUsername.setText(username);
        if (password != NO_VALUE) tfPassword.setText(password);
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
        }
        editor.commit();
        
        //return username and pw as result of the activity
        returnIntent.putExtra("username", username);
        returnIntent.putExtra("password", password);

        setResult(RESULT_OK, returnIntent);
        finish();
    }
    /**
     * Checks whether a certain user name is valid by matching it with the criteria of the ATIS account creation 
     * @param username
     * @return
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
    
    
    /**
     * This method is called if the user clicks the 'Cancel' button in the password prompt window.
     * @param w
     */
    public void onClickCancel(View w) {
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }
}
