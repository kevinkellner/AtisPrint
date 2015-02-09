package edu.kit.uneig.atisprint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        System.out.println("HELLO");
        Intent sshIntent = new Intent(this, SSHConnect.class);
        sshIntent.putExtra("user", "s_kkelln");
        sshIntent.putExtra("password", "335BA8637F");
        sshIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
        Bundle userData = new Bundle();
//        userData.putString("user", "s_kkelln");
//        userData.putString("password", "335BA8637F");
//        
        startActivity(sshIntent, userData);
    }
}
