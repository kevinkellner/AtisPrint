package edu.kit.uneig.atisprint;

import android.os.AsyncTask;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AsyncSshConnect extends AsyncTask<Object, Void, String> {
    public AsyncResponse delegate = null;

    private final String filename = "AtisPrintCache.pdf";
    private final String createTestFile = "touch testFile" + System.currentTimeMillis() + ".txt";
    private final String CMD_GET_PRINTERS = "lpstat -a";
    private final String CMD_PRINT_FILE = "lp -d pool-sw1 "+filename;

    @Override
    protected String doInBackground(Object... params) {
        // Don't try this at home. Seriously. Bad code ahead. Open for suggestions though.
        String user = (String) params[0];
        String password = (String) params[1];
        String hostname = (String) params[2];
        int port = (Integer) params[3];
        InputStream fis = (InputStream) params[4];
        String printer = (String) params[5];
        //Bad code end. You made it.

        SSHSession ssh = new SSHSession(user, password, hostname, port);
        
        String ret = "";
        try {
                ssh.copy(filename, fis);
//                ret = ssh.execute("lp -d " + printer + " " + filename);
                ret = ssh.execute(createTestFile);
                fis.close();
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SftpException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSchException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ret;
    }

    public String getFilename() {
        return filename;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result); // callback to activity that started this async task.
    }



}
