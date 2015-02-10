package edu.kit.uneig.atisprint;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SSHConnect extends Activity {
    
    private static final String atisHostname = "i08fs1.ira.uka.de";
    private static final int port = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        Intent intent = getIntent();


        final String user = intent.getStringExtra("user");
        final String password = intent.getStringExtra("password");
        
        final Uri receivedUri = (Uri)intent.getParcelableExtra(Intent.EXTRA_STREAM);
        
        


        Toast.makeText(this, receivedUri.toString(), Toast.LENGTH_LONG).show();
        new AsyncTask<Integer, Void, Void>(){
            @Override
            protected Void doInBackground(Integer... params) {
                    try {
                        try {
                            final FileInputStream in = (FileInputStream) getContentResolver().openInputStream(receivedUri);
                            copyFileOverSCP(user, password, atisHostname, port, in);
                            executeRemoteSSHCommand(user, password, atisHostname, port);
                        } catch (FileNotFoundException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                return null;
            }
        }.execute(1);
    }
    
    public static void copyFileOverSCP(String username, String password, String hostname, int port, FileInputStream file) throws JSchException, FileNotFoundException, SftpException {
        JSch jsch = new JSch();
        Session session = null;
        session = jsch.getSession(username,hostname,22);
        session.setPassword(password);
        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);
        session.connect();
        ChannelSftp channel = null;
        channel = (ChannelSftp)session.openChannel("sftp");
        channel.connect();
//            File localFile = new File("localfilepath");
            //If you want you can change the directory using the following line.
//            channel.cd(RemoteDirectoryPath)
        channel.put(file,"testCopySFTP");
            channel.disconnect();
        session.disconnect();
        
    }
    
    
    
    public static String executeRemoteSSHCommand(String user, String password, String hostname, int port)
            throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, hostname, port);
        session.setPassword(password);

        // Avoid asking for key confirmation
        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);

        session.connect();

        // SSH Channel
        ChannelExec channelssh = (ChannelExec)
                session.openChannel("exec");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        channelssh.setOutputStream(baos);

        // Execute command
        channelssh.setCommand("touch testFile.txt"+new Random().nextInt()); //create empty testfile if all goes well
        channelssh.connect();
        channelssh.disconnect();

        return baos.toString();
    }

}
