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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import edu.kit.uneig.atisprint.login.LoginPromptActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsActivity extends Activity {

    private final int changeUser = 0;
    private final int selectPrinter = 1;
    private final int setDir = 2;

    final CharSequence printers[] = new CharSequence[] {"pool-sw1", "pool-sw2", "pool-sw3", "pool-farb1"};

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        prefs = getApplicationContext().getSharedPreferences("AtisPrint", Context.MODE_PRIVATE);
        initializeListView();
    }

    /**
     * Initializes the ListView of this Activity. The ListView consists of an item and a subitem for each entry.
     */
    private void initializeListView() {
        //titles contains all the items in the ListView, subtitles are their subitems
        final String[] titles = new String[] {"Change user", "Select printer", "Set directory"};
        final String[] subtitles = new String[] {getUsername(), getPrinter(), getDirectory()};

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

    /**
     * Returns the username that is saved in the Preferences of this application or
     * "No user saved" if no user is saved
     * @return the username that is saved in the Preferences of this application
     */
    private String getUsername() {
        return prefs.getString("username", "No User saved");
    }

    /**
     * Returns the printer that is saved in the Preferences of this application or
     * the first printer if no printer is saved
     * @return the printer that is saved in the Preferences of this application.
     */
    private String getPrinter() {
        return prefs.getString("printer", printers[0].toString());
    }


    /**
     * Sets the printer to the specified printer and updates the listView afterwards.
     * The id corresponds to the index in the printers array.
     * @param id the id of the printer
     */
    private void setPrinter(int id) {
        if (id < printers.length) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("printer", printers[id].toString()).apply();
            initializeListView();
        }
    }

    private String getDirectory() {
        return prefs.getString("dir", "AtisPrint");
    }

    private void setDirectory(String dir) {
        String regEx = "([a-zA-Z]/?)+([a-zA-Z]+)?/?";
        if (dir.matches(regEx)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("dir", dir).apply();
            initializeListView();
        } else {
            Toast.makeText(this, "The directory is invalid", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == changeUser) {
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

    /**
     * This Listener is used for the ListItems inside the ListView.
     */
    private class ListItemListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case changeUser:
                    Intent signIn = new Intent(getBaseContext(), LoginPromptActivity.class);
                    startActivityForResult(signIn, changeUser);
                    break;
                case selectPrinter:
                    showPrinterSelection();
                    break;
                case setDir:
                    showDirectoryInput();
                    break;
            }
        }

        private void showDirectoryInput() {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
            builder.setTitle("Set the directory");
            final EditText input = new EditText(SettingsActivity.this);
            input.setText(getDirectory());
            builder.setView(input);
            builder.setNegativeButton("Cancel", null);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setDirectory(input.getText().toString());
                }
            });

            builder.setNeutralButton("Reset to default", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setDirectory("AtisPrint");
                }
            });
            builder.show();
        }

        /**
         * Shows a pop up window in which the user can select one printer which will then be saved to the Preference
         * file of this application.
         */
        private void showPrinterSelection() {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
            builder.setTitle("Select a printer");
            builder.setItems(printers, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setPrinter(which);
                }
            });
            builder.show();
        }
    }
}
