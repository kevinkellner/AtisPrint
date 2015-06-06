package edu.kit.uneig.atisprint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import edu.kit.uneig.atisprint.login.SaveLoginActivity;

import java.util.ArrayList;

public class SettingsActivity extends Activity {

    protected static int SIGN_IN_REQUEST = 0xFF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
//        ArrayList<String> list = new ArrayList<String>();
//        list.add("Change log in data");
//        list.add("Select printer");
//
//        ListAdapter listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
//        ListView listView = (ListView) findViewById(R.id.listView);
//        listView.setAdapter(listAdapter);
    }

    public void onClickPrint(View v) {
        Intent intent = new Intent(this, SaveLoginActivity.class);
        startActivityForResult(intent, SIGN_IN_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGN_IN_REQUEST) {
            if (resultCode == RESULT_OK) {
                String username = data.getStringExtra("username");
                String password = data.getStringExtra("password");
                boolean savePw = data.getBooleanExtra("savePw", false);
                System.out.println(username);
                System.out.println(password);
                //TODO: Save user credentials if need be
            } else if (resultCode == RESULT_CANCELED) {
                System.out.println("Not Okay");
                //TODO: yeah..
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
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
}
