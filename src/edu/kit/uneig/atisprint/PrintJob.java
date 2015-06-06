package edu.kit.uneig.atisprint;

import java.io.InputStream;

public class PrintJob {

    private int port;
    private InputStream file;
    private String username;
    private String password;
    private String hostname;
    private String printer;
    private String filename;


    private String directory;

    public PrintJob() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @return the file
     */
    public InputStream getFile() {
        return file;
    }

    /**
     * @param is the file to set
     */
    public void setFile(InputStream is) {
        this.file = is;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @param hostname the hostname to set
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the printer
     */
    public String getPrinter() {
        return printer;
    }

    /**
     * @param printer the printer to set
     */
    public void setPrinter(String printer) {
        this.printer = printer;
    }
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}
