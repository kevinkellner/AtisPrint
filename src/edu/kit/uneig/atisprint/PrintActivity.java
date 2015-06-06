package edu.kit.uneig.atisprint;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import edu.kit.uneig.atisprint.login.PreferencesWrapper;
import edu.kit.uneig.atisprint.login.LoginPromptActivity;

import java.io.FileNotFoundException;

public class PrintActivity extends Activity implements AsyncResponse {


    protected static int SIGN_IN_PROMPT = 0x02;

    private Uri receivedUri;
    private PreferencesWrapper pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = new PreferencesWrapper(this);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();


        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.equals("application/pdf")) {
                handleSendPdf(intent);
            }
        } else {
            setContentView(R.layout.activity_print);
        }
    }

    /**
     * This method receives an intent from the onCreate() method. The intent contains the PDF file that will be printed later on.
     * This method then creates a new intent and launches the SignInActivity which will provide us with the user's credentials.
     */
    private void handleSendPdf(Intent intent) {
        receivedUri = intent.getParcelableExtra(Intent.EXTRA_STREAM); //save the uri of the file
        String username = pref.getString("username", "--");
        String password = pref.getStringSecure("password", "--");

        if (username.equals("--") || password.equals("--")) {
            Intent prompt = new Intent(this, LoginPromptActivity.class);
            startActivityForResult(prompt, SIGN_IN_PROMPT);
        } else {
            Intent data = new Intent("JustPassingData");
            data.putExtra("username", username);
            data.putExtra("password", password);
            startPrintJob(data);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (requestCode == SIGN_IN_PROMPT) {
            if (resultCode == RESULT_OK) {
                startPrintJob(data);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Please Log In to print document", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startPrintJob(Intent data) {
        PrintJob printJob = new PrintJob();
        try {
            String uri = receivedUri.toString();
            printJob.setFile(getContentResolver().openInputStream(receivedUri));
            printJob.setFilename(uri.substring(uri.lastIndexOf("/") + 1));
            printJob.setUsername(data.getStringExtra("username"));
            printJob.setPassword(data.getStringExtra("password"));
            printJob.setPrinter(pref.getString("printer", "pool-sw1"));
            printJob.setHostname("i08fs1.ira.uka.de");
            printJob.setPort(22);
            printJob.setDirectory(pref.getString("dir", "AtisPrint"));

            Toast.makeText(this, "Printing " + printJob.getFilename() + " on " + printJob.getPrinter(), Toast.LENGTH_LONG).show();

            AsyncSshConnect ssh = new AsyncSshConnect();
            ssh.delegate = this; // add reference for callback
            ssh.execute(printJob);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void processFinish(String output) {
        Toast.makeText(this, output, Toast.LENGTH_LONG).show();
        System.out.println(output);
        finish();


    }

}
