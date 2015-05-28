package edu.kit.uneig.atisprint.login;

import android.os.Bundle;

/**
 * Created by kelln_000 on 28.05.2015.
 */
public class RetrieveLoginActivity extends LoginActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        returnUserData();
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

        } else {
            //either username or pw is not saved, so we can't give them back.
            setResult(RESULT_CANCELED, returnIntent);
        }
        finish();
    }


}
