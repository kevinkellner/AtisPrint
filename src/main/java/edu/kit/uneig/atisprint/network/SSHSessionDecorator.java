package edu.kit.uneig.atisprint.network;

/**
 * This class decorates the SSHSession to provide some methods useful for interaction over ssh.
 * @author Kevin Kellner
 * @version 1.0
 */
public abstract class SSHSessionDecorator extends SSHInterface {
    protected SSHInterface ssh;

    public SSHSessionDecorator(SSHInterface ssh) {
        this.ssh = ssh;
    }

}
