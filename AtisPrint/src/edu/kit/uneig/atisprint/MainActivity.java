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

public class MainActivity extends Activity implements AsyncResponse {

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
            setContentView(R.layout.activity_main);
        }
    }

    public void handleAsyncSendPdf(Intent intent) throws FileNotFoundException {
        final Uri receivedUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        BufferedInputStream in = (BufferedInputStream) getContentResolver().openInputStream(receivedUri); 
        
        AsyncSshConnect ssh = new AsyncSshConnect();
        ssh.delegate = this; // add reference for callback
        ssh.execute(getUsername(), getPassword(), "i08fs1.ira.uka.de", 22, in);
        

    }
    
    public String getUsername() {
        return "s_kkelln";
    }
    
    public String getPassword() {
        return "335BA8637F";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickPrint(View v) {

    }

    @Override
    public void processFinish(String output) {
        System.out.println(output);

    }
}
