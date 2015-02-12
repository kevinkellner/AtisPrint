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
    
    private Intent returnIntent;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int mode = getIntent().getIntExtra("mode", SET_USERDATA);
        returnIntent = new Intent();
        returnIntent.putExtra(Intent.EXTRA_STREAM, getIntent().getParcelableExtra(Intent.EXTRA_STREAM));
        //this will automatically encrypt the username and password with the ANDROID_ID when saving it
        settings = new ObscuredSharedPreferences(
                this, this.getPreferences(MODE_PRIVATE));
        
        username = getUsername();
        password = getPassword();
        
        if (mode == SET_USERDATA) setUserData();
        if (mode == GET_USERDATA) returnUserData();

    }
    
    private void returnUserData() {
        
        //both username and pw is saved, so return them
        if (username != NO_VALUE && password != NO_VALUE) {
            returnIntent.putExtra("username", username);
            returnIntent.putExtra("password", password);
            setResult(RESULT_OK, returnIntent);
            finish();
        } else {
            //either username or pw is not saved, so retrieve them
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
    
    public void onClickOk(View w) {        
        String password = tfPassword.getText().toString();
        String username = tfUsername.getText().toString();
        boolean savePw = chkSavePw.isChecked();
        
        SharedPreferences.Editor editor = settings.edit();
        
        //save username and password in preferences
        editor.putString("username", username);
        if (savePw) editor.putString("password", password);
        editor.commit();
        
        //return username and pw as result
        returnIntent.putExtra("username", username);
        returnIntent.putExtra("password", password);
        
        
        
        setResult(RESULT_OK, returnIntent);
        finish();
    }
    
    
    public String getUsername() {
        String username = settings.getString("username", NO_VALUE);
        return username;
    }
    
    public String getPassword() {
        String password = settings.getString("password", NO_VALUE);
        return password;
    }
    
    
    
    public void onClickCancel(View w) {
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }
}
