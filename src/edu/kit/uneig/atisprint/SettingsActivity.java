package edu.kit.uneig.atisprint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;
import edu.kit.uneig.atisprint.login.ObscuredSharedPreferences;
import edu.kit.uneig.atisprint.login.SaveLoginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsActivity extends Activity {

    protected static int SIGN_IN_REQUEST = 0xFF;

    private final int changeUser = 0;
    private final int selectPrinter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //titles contains all the items in the ListView, subtitles are their subitems
        final String[] titles = new String[] {"Change user", "Select printer"};
        final String[] subtitles = new String[] {getUsername(), getPrinter()};

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        for (int i = 0; i < titles.length; i++) {
            Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("title", titles[i]);
            datum.put("subtitle", subtitles[i]);
            data.add(datum);
        }

        SimpleAdapter mAdapter = new SimpleAdapter(this, data,
                                                    android.R.layout.simple_list_item_2,
                                                    new String[]{"title", "subtitle"},
                                                    new int[]{android.R.id.text1, android.R.id.text2});

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new ListItemListener());
        listView.setAdapter(mAdapter);
    }

    private String getUsername() {
        SharedPreferences prefs = getSharedPreferences("AtisPrint", Context.MODE_PRIVATE);
        return prefs.getString("username", "No User saved");
    }

    private String getPrinter() {
        return "PrinterTODO";
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

    private class ListItemListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case changeUser:
                    Intent signIn = new Intent(getBaseContext(), SaveLoginActivity.class);
                    startActivity(signIn);
                    break;
                case selectPrinter:
                    //TODO add printer selection activity.
            }
        }
    }
}
