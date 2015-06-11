package edu.kit.uneig.atisprint;

import android.os.AsyncTask;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AsyncSshConnect extends AsyncTask<PrintJob, Void, String> {
    public AsyncResponse delegate = null;

    private final String createTestFile = "touch testFile" + System.currentTimeMillis() + ".txt";
//    private final String CMD_GET_PRINTERS = "lpstat -a";
//    private final String CMD_PRINT_FILE = "lp -d pool-sw1 " + getFilename();

    @Override
    protected String doInBackground(PrintJob... params) {
        String user = params[0].getUsername();
        String password = params[0].getPassword();
        String hostname = params[0].getHostname();
        int port = params[0].getPort();
        InputStream fis = params[0].getFile();
        String printer = params[0].getPrinter(); //TODO make it possible to print more than one file(?)
        String filename = params[0].getFilename();
        String dir = params[0].getDirectory();

        SSHSession ssh = new SSHSession(user, password, hostname, port);

        String ret = "";
        try {
            ssh.copy(dir, filename, fis);
            if (! BuildConfig.DEBUG) {
                ret = ssh.execute("lp -d " + printer + " " + dir + filename);
            }
            fis.close();
        } catch (FileNotFoundException e) {
            onPostExecute("Error: The file was not found. Please try again.");
        } catch (SftpException e) {
            onPostExecute("Error: An Sftp Exception occurred. Please try again.");
        } catch (IOException e) {
            onPostExecute("Error: An I/O Exception occurred. Please try again.");
        } catch (JSchException e) {
            onPostExecute("Error: Authentication with the SSH Server failed.");
        }

        return ret;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result); // callback to activity that started this async task.
    }


}
