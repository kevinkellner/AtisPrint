package edu.kit.uneig.atisprint;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.IOException;
import java.io.InputStream;

/**
 * This interface encapsulates all methods needed for ssh operations on a remote server.
 */
public abstract class SSHInterface {

    protected String username;
    protected String password;
    protected String hostname;
    protected int port;

    /**
     * Creates a new SSHInterface with the given parameters.
     * @param username username for login to the server
     * @param password password for the login
     * @param hostname the hostname to connect to
     * @param port the port
     */
    public SSHInterface(String username, String password, String hostname, int port) {
        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * Creates a new SSHInterface with the given parameters. The default port 22 will be used.
     * @param username username for login to the server
     * @param password password for the login
     * @param hostname the hostname to connect to
     */
    public SSHInterface(String username, String password, String hostname) {
        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.port = 22;
    }

    protected SSHInterface() {
    }

    /**
     * Execute the command on the remote server and return the console output as string
     * @param command an ssh command
     *                @throws Exception if an exception occurs.
     * @return the response of the console on the server
     * */
    public abstract String execute(String command) throws JSchException, IOException;

    /**
     * Creates a new file with the specified filename and directory on the remote server.
     * @param dir the directory to save the file to
     * @param filename the name of the file including the extension
     * @param file the InputStream that is to be written to the file.
     *             @throws Exception if an exception occurs.
     */
    public abstract void copy(String dir, String filename, InputStream file) throws JSchException, SftpException;
}
