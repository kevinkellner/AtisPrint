package edu.kit.uneig.atisprint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class SigninActivity extends Activity {
    
    private EditText tfUsername;
    private EditText tfPassword;
    private CheckBox chkSavePw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        tfUsername = (EditText) findViewById(R.id.tfUsername);
        tfPassword = (EditText) findViewById(R.id.tfPassword);
        chkSavePw = (CheckBox) findViewById(R.id.chkSavePw);
    }
    
    public void onClickOk(View w) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("username", tfUsername.getText().toString());
        returnIntent.putExtra("password", tfPassword.getText().toString());
        returnIntent.putExtra("savePw", chkSavePw.isChecked());
        setResult(RESULT_OK, returnIntent);
        finish();
    }
    
    public void onClickCancel(View w) {
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }
}
