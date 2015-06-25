package edu.kit.uneig.atisprint;

/**
 * This class uses the SSHInterface to provide some methods useful for interaction over ssh.
 * @author Kevin Kellner
 * @version 1.0
 */
public class SSHJobs extends SSHSession {
    private SSHInterface ssh;
    public SSHJobs(SSHInterface ssh) {
        this.ssh = ssh;
    }

    public int countFiles(String dir) {
        return getFileList(dir).length;
    }

    public String[] getFileList(String dir) {
        //TODO: Need to change directory.
        String fileList = ssh.execute("ls");
        String files[] = fileList.split("\n");
    }

    public void deleteFile(String dir, String file) {
        ssh.execute("rm" + dir + file);
    }

    public boolean deleteFiles(String dir){
        //TODO dir may not be root dir
        String[] files = getFileList(dir);
        for (String file : files) {
            deleteFile(dir, file); //Delete all files from directory
        }
    }
}
