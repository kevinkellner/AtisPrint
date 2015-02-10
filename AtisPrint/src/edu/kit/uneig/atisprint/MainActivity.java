package edu.kit.uneig.atisprint;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        
        Toast.makeText(this, "Action:"+action+"\ntype:"+type, Toast.LENGTH_LONG).show();
        
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("application/pdf".equals(type)) {
                handleAsyncSendPdf(intent); // Handle pdf being sent
            }else if (type.startsWith("image/")) { 
//                Toast.makeText(this, intent.toString(), Toast.LENGTH_LONG).show();
                handleSendPdf(intent);
            }
        } else {
            
            setContentView(R.layout.activity_main);
        }
    }
    
    public void handleAsyncSendPdf(Intent intent) {
        InputStream is = null;
        try {
          is =  getAssets().open("blatt-13-aufgaben.pdf");
//          is =  getAssets().open("TestFileCopy.txt");
      } catch (IOException e2) {
          // TODO Auto-generated catch block
          e2.printStackTrace();
      }
      
      final InputStream caInput = new BufferedInputStream(is);
      
      AsyncSshConnect ssh = new AsyncSshConnect();
      ssh.delegate = this; //add reference for callback
      ssh.execute("s_kkelln", "335BA8637F", "i08fs1.ira.uka.de", 22, caInput);
    }
    
    public void handleSendPdf(Intent intent)  {
//        Uri receivedUri = (Uri)intent.getParcelableExtra(Intent.EXTRA_STREAM);
        
//        String filePath = getRealPathFromURI(this, receivedUri);
//        Toast.makeText(this, filePath, Toast.LENGTH_LONG).show();
//        File pdf = new File(filePath);
//        Toast.makeText(this, String.valueOf(pdf.exists()), Toast.LENGTH_SHORT).show();
        
        intent.putExtra("user", "s_kkelln");
        intent.putExtra("password", "335BA8637F");
        
        intent.setClass(this, SSHConnect.class);
        
        /*Intent sshIntent = new Intent(this, SSHConnect.class);
        sshIntent.putExtra("user", "s_kkelln");
        sshIntent.putExtra("password", "335BA8637F");
        sshIntent.putExtra("uri", receivedUri);
        sshIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); */
        Bundle userData = new Bundle();
        userData.putString("user", "s_kkelln");
        userData.putString("password", "335BA8637F");
        
        startActivity(intent, userData);
    }
    
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try { 
          String[] proj = { MediaStore.Images.Media.DATA };
          cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
          int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
          cursor.moveToFirst();
          return cursor.getString(column_index);
        } finally {
          if (cursor != null) {
            cursor.close();
          }
        }
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
        handleAsyncSendPdf(new Intent());
      
    }

    @Override
    public void processFinish(String output) {
        System.out.println(output);
        
    }
}
