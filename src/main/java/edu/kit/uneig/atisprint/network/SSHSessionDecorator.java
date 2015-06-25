package edu.kit.uneig.atisprint.network;

/**
 *
 * @author Kevin Kellner
 * @version 1.0
 */
public abstract class SSHSessionDecorator extends SSHInterface {
    protected SSHInterface ssh;

    public SSHSessionDecorator(SSHInterface ssh) {
        this.ssh = ssh;
    }

}
