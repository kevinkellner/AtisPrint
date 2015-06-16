package edu.kit.uneig.atisprint;

import java.io.InputStream;

/**
 * This interface encapsulates all methods needed for ssh operations on a remote server.
 */
public interface SSHInterface {
    /**
     * Execute the command on the remote server and return the console output as string
     * @param command an ssh command
     * @return the response of the console on the server
     */
    String execute(String command);

    /**
     * Creates a new file with the specified filename and directory on the remote server.
     * @param dir the directory to save the file to
     * @param filename the name of the file including the extension
     * @param file the InputStream that is to be written to the file.
     */
    void copy(String dir, String filename, InputStream file);
}
