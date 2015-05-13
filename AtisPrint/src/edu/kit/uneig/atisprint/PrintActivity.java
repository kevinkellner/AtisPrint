package edu.kit.uneig.atisprint;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class PrintActivity extends Activity implements AsyncResponse {
    
    protected static int SIGN_IN_REQUEST = 0xFF;

    private String username;
    private String password;
    private String printer;

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGN_IN_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri receivedUri = (Uri) data.getParcelableExtra(Intent.EXTRA_STREAM);
                    InputStream in =  getContentResolver().openInputStream(receivedUri); 
                    String username = data.getStringExtra("username");
                    String password = data.getStringExtra("password");
                    String printer = "pool-sw1"; //TODO Read out printer from preferences or html.
                    
                    Toast.makeText(this, "Printing on "+printer, Toast.LENGTH_LONG).show();
                    
                    AsyncSshConnect ssh = new AsyncSshConnect();
                    ssh.delegate = this; // add reference for callback
                    ssh.execute(username, password, "i08fs1.ira.uka.de", 22, in, printer);
                    
                } catch(FileNotFoundException e) {
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
            Toast.makeText(this, output, Toast.LENGTH_LONG).show();
            System.out.println(output);
            finish();   
        

    }

}
