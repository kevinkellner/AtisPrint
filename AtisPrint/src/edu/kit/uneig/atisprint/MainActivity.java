package edu.kit.uneig.atisprint;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        
        
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("application/pdf".equals(type)) {
                handleSendPdf(intent); // Handle pdf being sent
            }else if (type.startsWith("image/")) { 
//                Toast.makeText(this, intent.toString(), Toast.LENGTH_LONG).show();
                handleSendPdf(intent);
            }
        } else {
            setContentView(R.layout.activity_main);
        }
    }
    
    public void handleSendPdf(Intent intent)  {
        Uri receivedUri = (Uri)intent.getParcelableExtra(Intent.EXTRA_STREAM);
        String filePath = receivedUri.getPath();
        Toast.makeText(this, filePath, Toast.LENGTH_LONG).show();
        File pdf = new File(filePath);
        Toast.makeText(this, String.valueOf(pdf.exists()), Toast.LENGTH_SHORT).show();
        
        Intent sshIntent = new Intent(this, SSHConnect.class);
        sshIntent.putExtra("user", "s_kkelln");
        sshIntent.putExtra("password", "335BA8637F");
        sshIntent.putExtra("file", pdf.getPath());
        sshIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
        Bundle userData = new Bundle();
        userData.putString("user", "s_kkelln");
        userData.putString("password", "335BA8637F");
        
        startActivity(sshIntent, userData);
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
        
      

      InputStream is = null;
      File tempFile = null;
      try {
          //InputStreamReader
          is = getAssets().open("TestFileCopy.txt");
          tempFile = new File(getCacheDir().getPath()+"tempFileAtis.txt");
          FileWriter write = new FileWriter(tempFile);
          byte b = (byte) is.read();
          while(is.read() != -1) {
              write.write(b);
          }
          write.close();
          is.close();
          
          
      } catch (IOException e1) {
          e1.printStackTrace();
      } 
      
      Intent sendIntent = new Intent();
      
    }
}
