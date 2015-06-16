package edu.kit.uneig.atisprint;

import com.jcraft.jsch.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Kevin Kellner
 * @version 1.0
 */
public class SSHSession extends SSHInterface {

    private String username;
    private String password;
    private String hostname;
    private int port;

    public SSHSession(String username, String password, String hostname, int port) {
        super(username, password, hostname, port);
    }

    public SSHSession(String username, String password, String hostname) {
        super(username, password, hostname);
    }

    @Override
    public void copy(String dir, String filename, InputStream file) throws JSchException, SftpException {
        Session session = createSession();

        session.connect();

        //create all missing directories via ssh
        ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
        channelssh.setCommand("mkdir -p " + dir);
        channelssh.connect();
        channelssh.disconnect();

        //create a sftp channel
        ChannelSftp channel;
        channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();

        channel.cd(dir); //change the directory
        channel.put(file, filename); //copy the file

        channel.disconnect();
        session.disconnect();

    }


    @Override
    public String execute(String command) {
        StringBuilder builder = new StringBuilder();
        try {
            Session session = createSession();
            session.connect();

            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();
            channel.connect();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    builder.append(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }
                Thread.sleep(1000); //we wait because the command needs time to be executed
            }
            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
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
