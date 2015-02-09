package edu.kit.uneig.atisprint;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

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
//        

        Toast.makeText(this, user+""+password, Toast.LENGTH_SHORT).show();
        InputStream is = null;
        try {
            is = getAssets().open("TestFileCopy.txt");
            File tempFile = new File(getCacheDir().getPath()+"tempFileAtis.txt");
            FileWriter write = new FileWriter(tempFile);
            byte b = (byte) is.read();
            while(is.read() != -1) {
                write.write(b);
            }
            write.close();
            is.close();
            
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSchException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        new AsyncTask<Integer, Void, Void>(){
            @Override
            protected Void doInBackground(Integer... params) {
                    try {
                        copyFileOverSCP(user, password, atisHostname, port, tempFile);
                        executeRemoteSSHCommand(user, password, atisHostname, port);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                return null;
            }
        }.execute(1);
    }
    
    public static String copyFileOverSCP(String username, String password, String hostname, int port, File f) throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, hostname, port);
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
        channelssh.setCommand("scp "+f.getPath()+" "+username+"@"+hostname+":"+"tempfile.txt"); //create empty testfile if all goes well
        channelssh.connect();
        channelssh.disconnect();

        return baos.toString();
        
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
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sshconnect, menu);
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
*/
}
