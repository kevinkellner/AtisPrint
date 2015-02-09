package edu.kit.uneig.atisprint;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
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
        Intent i = getIntent();


        final String user = i.getStringExtra("user");
        final String password = i.getStringExtra("password");
        final String filePath = i.getStringExtra("file");
        final File pdf = new File(filePath);


        Toast.makeText(this, String.valueOf(pdf.exists()), Toast.LENGTH_LONG).show();
        new AsyncTask<Integer, Void, Void>(){
            @Override
            protected Void doInBackground(Integer... params) {
                    try {
                        copyFileOverSCP(user, password, atisHostname, port, pdf);
//                        executeRemoteSSHCommand(user, password, atisHostname, port);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                return null;
            }
        }.execute(1);
    }
    
    public static void copyFileOverSCP(String username, String password, String hostname, int port, File file) throws JSchException, FileNotFoundException, SftpException {
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
        channel.put(new FileInputStream(file),file.getName());
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
