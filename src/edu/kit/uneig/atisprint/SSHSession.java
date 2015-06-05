package edu.kit.uneig.atisprint;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Kevin Kellner
 * @version 1.0
 */
public class SSHSession {

    private String username;
    private String password;
    private String hostname;
    private int port;


    public SSHSession(String username, String password, String hostname, int port) {
        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.port = port;
    }



    public SSHSession(String username, String password, String hostname) {
        this(username, password, hostname, 22);
    }




    public void copy(String filename, InputStream file) throws JSchException, SftpException {
        Session session = createSession();

        session.connect();

        ChannelSftp channel;
        channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();
        // If you want you can change the directory using the following line.
        // channel.cd(RemoteDirectoryPath)

        channel.put(file, filename);


        channel.disconnect();
        session.disconnect();

    }


    public String execute(String command) throws JSchException, IOException {
        Session session = createSession();

        session.connect();

        // SSH Channel
        ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream in = channelssh.getInputStream();
        channelssh.setOutputStream(baos);

        // Execute command
        channelssh.setCommand(command);
        channelssh.connect();
        channelssh.disconnect();

        return baos.toString();
    }

    private Session createSession() throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, hostname, port);
        session.setPassword(password);

        // Avoid asking for key confirmation
        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);
        return session;
    }
}
