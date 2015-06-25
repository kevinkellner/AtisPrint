package edu.kit.uneig.atisprint.network;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Kevin Kellner
 * @version 1.0
 */
public class SSHUtils extends SSHSessionDecorator {

    public SSHUtils(SSHInterface ssh) {
        super(ssh);
    }

    public int countFiles(String dir) throws IOException, JSchException {
        return getFileList(dir).length;
    }

    public String[] getFileList(String dir) throws IOException, JSchException {
        //TODO: Need to change directory.
        String fileList = execute("ls");
        String files[] = fileList.split("\n");
        return files;
    }

    public void deleteFile(String dir, String file) throws IOException, JSchException {
        execute("rm" + dir + file);
    }

    public void deleteFiles(String dir) throws IOException, JSchException {
        //TODO dir may not be root dir
        String[] files = getFileList(dir);
        for (String file : files) {
            deleteFile(dir, file); //Delete all files from directory
        }
    }

    @Override
    public String execute(String command) throws JSchException, IOException {
        return ssh.execute(command);
    }

    @Override
    public void copy(String dir, String filename, InputStream file) throws JSchException, SftpException {
        ssh.copy(dir, filename, file);
    }
}
