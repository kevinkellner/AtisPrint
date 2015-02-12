package edu.kit.uneig.atisprint;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class PrintActivity extends Activity implements AsyncResponse {
    
    protected static int SIGN_IN_REQUEST = 0xFF;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("application/pdf".equals(type)) {
                try {
                 // Handle pdf being sent
                    handleAsyncSendPdf(intent);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } 
            } 
        } else {
            setContentView(R.layout.activity_print);
        }
    }

    public void handleAsyncSendPdf(Intent intent) throws FileNotFoundException {
        final Uri receivedUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        
        Intent signIn = new Intent(this, SigninActivity.class);
        signIn.putExtra(Intent.EXTRA_STREAM, receivedUri);
                
        signIn.putExtra("mode", SigninActivity.GET_USERDATA);
        startActivityForResult(signIn, SIGN_IN_REQUEST);
        System.out.println("HandleASYNC");
        
        
        

    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGN_IN_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri receivedUri = (Uri) data.getParcelableExtra(Intent.EXTRA_STREAM);
                    InputStream in =  getContentResolver().openInputStream(receivedUri); 
                    String username = data.getStringExtra("username");
                    String password = data.getStringExtra("password");
                    
                    AsyncSshConnect ssh = new AsyncSshConnect();
                    ssh.delegate = this; // add reference for callback
                    ssh.execute(username, password, "i08fs1.ira.uka.de", 22, in);
                    
                }catch(FileNotFoundException e) {
                    e.printStackTrace();
                }
                
                
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Please Log In to print document", Toast.LENGTH_LONG).show();
                
            }
        }
    }

    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }

    @Override
    public void processFinish(String output) {
        System.out.println(output);
        finish();

    }
}
