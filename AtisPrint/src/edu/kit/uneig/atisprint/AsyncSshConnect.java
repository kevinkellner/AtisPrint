package edu.kit.uneig.atisprint;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

import android.os.AsyncTask;
import android.widget.Toast;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class AsyncSshConnect extends AsyncTask<Object, Void, String> {
    public AsyncResponse delegate = null;
    private final String filename = "AtisPrintCache.pdf";
    private final String createTestFile = "touch testFile.txt" + new Random().nextInt();
    private final String CMD_GET_PRINTERS = "lpstat -a";
    private final String CMD_PRINT_FILE = "lp -d pool-sw1 "+filename;

    @Override
    protected String doInBackground(Object... params) {
        // Don't try this at home. Seriously. It is THAT bad. Open for
        // suggestions tho.
        String user = (String) params[0];
        String password = (String) params[1];
        String hostname = (String) params[2];
        int port = (int) params[3];
        InputStream fis = (InputStream) params[4];
        String ret = "";
        try {
            try {
                copyFileOverSCP(user, password, hostname, port, fis);
                ret = executeRemoteSSHCommand(user, password, hostname, port, CMD_PRINT_FILE);
                fis.close();
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ret;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result); // callback to activity that started
                                        // this async task.
    }

    public void copyFileOverSCP(String username, String password,
            String hostname, int port, InputStream file) throws JSchException,
            FileNotFoundException, SftpException, InterruptedException {
        JSch jsch = new JSch();
        Session session = null;
        session = jsch.getSession(username, hostname, 22);
        session.setPassword(password);
        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);
        session.connect();

        ChannelSftp channel = null;
        channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();
        // If you want you can change the directory using the following line.
        // channel.cd(RemoteDirectoryPath)
        channel.put(file, filename);

        
        channel.disconnect();
        session.disconnect();

    }
    

    public String executeRemoteSSHCommand(String user, String password,
            String hostname, int port, String command) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, hostname, port);
        session.setPassword(password);

        // Avoid asking for key confirmation
        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);

        session.connect();

        // SSH Channel
        ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        channelssh.setOutputStream(baos);

        // Execute command
        channelssh.setCommand(command); //only here for testin'. real command above
        channelssh.connect();
        channelssh.disconnect();

        return baos.toString();
    }

}
