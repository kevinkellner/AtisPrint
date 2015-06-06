package edu.kit.uneig.atisprint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import edu.kit.uneig.atisprint.login.SaveLoginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsActivity extends Activity {

    protected static int SIGN_IN_REQUEST = 0xFF;

    private final int changeUser = 0;
    private final int selectPrinter = 1;

    final CharSequence printers[] = new CharSequence[] {"pool-sw1-raw", "pool-sw2-raw", "pool-sw3-raw", "pool-farb1-raw"};

    private String printer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initializeListView();
    }

    private String getUsername() {
        SharedPreferences prefs = getSharedPreferences("AtisPrint", Context.MODE_PRIVATE);
        return prefs.getString("username", "No User saved");
    }

    private String getPrinter() {
        SharedPreferences prefs = getSharedPreferences("AtisPrint", Context.MODE_PRIVATE);
        return prefs.getString("printer", printers[0].toString());
    }

    private void initializeListView() {
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

    private void setPrinter(String name) {
        SharedPreferences prefs = getSharedPreferences("AtisPrint", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("printer", name);
        editor.apply();
        initializeListView();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == changeUser) {
            initializeListView();
        } else if (requestCode == selectPrinter) {
            initializeListView();
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
                    startActivityForResult(signIn, changeUser);
                    break;
                case selectPrinter:
                    showPrinterSelection();
            }
        }

        private void showPrinterSelection() {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
            builder.setTitle("Select a printer");
            builder.setItems(printers, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setPrinter(printers[which].toString());
                }
            });
            builder.show();
        }
    }
}
