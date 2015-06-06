package edu.kit.uneig.atisprint;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import edu.kit.uneig.atisprint.login.RetrieveLoginActivity;
import edu.kit.uneig.atisprint.login.SaveLoginActivity;

import java.io.FileNotFoundException;

public class PrintActivity extends Activity implements AsyncResponse {


    protected static int LOGIN_DATA_REQUEST = 0x01;
    protected static int SIGN_IN_REQUEST = 0x02;

    private Uri receivedUri;

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
            if (type.equals("application/pdf")) {
                try {
                    receivedUri = intent.getParcelableExtra(Intent.EXTRA_STREAM); //save the uri of the file
                    handleAsyncSendPdf(); // Handle pdf being sent
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else {
            setContentView(R.layout.activity_print);
        }
    }

    /**
     * This method receives an intent from the onCreate() method. The intent contains the PDF file that will be printed later on.
     * This method then creates a new intent and launches the SignInActivity which will provide us with the user's credentials.
     *
     * @throws FileNotFoundException if the file is not found
     */
    private void handleAsyncSendPdf() throws FileNotFoundException {

        //create new intent 
        Intent signIn = new Intent(this, RetrieveLoginActivity.class);
        //TODO: Make two different classes? One for getting, one for setting user data?

        startActivityForResult(signIn, LOGIN_DATA_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_DATA_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    startPrintJob(data);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Intent signInPrompt = new Intent(this, SaveLoginActivity.class);
                startActivityForResult(signInPrompt, SIGN_IN_REQUEST);
            }
        } else if (requestCode == SIGN_IN_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    startPrintJob(data);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Please Log In to print document", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startPrintJob(Intent data) throws FileNotFoundException {
        PrintJob printJob = new PrintJob();

        printJob.setFile(getContentResolver().openInputStream(receivedUri));
        printJob.setUsername(data.getStringExtra("username"));
        printJob.setPassword(data.getStringExtra("password"));
        printJob.setPrinter("pool-sw1"); //TODO Read out printer from preferences or html.
        printJob.setHostname("i08fs1.ira.uka.de");
        printJob.setPort(22);

        Toast.makeText(this, "Printing on " + printJob.getPrinter(), Toast.LENGTH_LONG).show();

        AsyncSshConnect ssh = new AsyncSshConnect();
        ssh.delegate = this; // add reference for callback
        ssh.execute(printJob);
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
