package edu.kit.uneig.atisprint;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;

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
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();


        final String user = intent.getStringExtra("user");
        final String password = intent.getStringExtra("password");
        
        final Uri receivedUri = (Uri)intent.getParcelableExtra(Intent.EXTRA_STREAM);
        InputStream is = null;
        try {
//            is =  getAssets().open("blatt-13-aufgaben.pdf");
            is =  getAssets().open("TestFileCopy.txt");
        } catch (IOException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        
        final InputStream caInput = new BufferedInputStream(is);
        
        ConnectSSH ssh = new ConnectSSH();
//        ssh.execute(user, password, atisHostname, port, caInput);

            try {
                new AsyncTask<Integer, Void, String>(){
                    @Override
                    protected String doInBackground(Integer... params) {
                                try {
                                        
//                            final FileInputStream in = (FileInputStream) getContentResolver().openInputStream(receivedUri);
                                    copyFileOverSCP(user, password, atisHostname, port, caInput);
//                            executeRemoteSSHCommand(user, password, atisHostname, port);
                                } catch (Exception e1) {
                                    // TODO Auto-generated catch block
                                    System.out.println("HILFE");
                                    e1.printStackTrace();
                                }
                        return "Done";
                    }
                }.execute(1).get();
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println(e.toString());
                System.out.println("HILF2222");
            }
    }
    
    public static void copyFileOverSCP(String username, String password, String hostname, int port, InputStream file) throws JSchException, FileNotFoundException, SftpException, InterruptedException {
        JSch jsch = new JSch();
        Session session = null;
        session = jsch.getSession(username,hostname,22);
        session.setPassword(password);
        
        //Avoid asking for key confirmation
        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);
        
        session.connect();

        ChannelSftp channel = null;
        channel = (ChannelSftp) session.openChannel("sftp");
        
        //If you want you can change the directory using the following line.
//            channel.cd(RemoteDirectoryPath)
        channel.connect();
        channel.put(file,"testCopySFTP.txt");
        
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
        ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        channelssh.setOutputStream(baos);

        // Execute command
        channelssh.setCommand("touch testFile.txt"+new Random().nextInt()); //create empty testfile if all goes well
        channelssh.connect();
        channelssh.disconnect();

        return baos.toString();
    }

}
