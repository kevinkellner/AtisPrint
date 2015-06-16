package edu.kit.uneig.atisprint.tests;

import com.jcraft.jsch.JSchException;
import edu.kit.uneig.atisprint.SSHInterface;
import edu.kit.uneig.atisprint.SSHSession;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

/**
 * Contains tests for the ssh session class.
 */
public class SSHSessionTest {

    private static SSHInterface session;

    @BeforeClass
    public static void setup() throws IOException {
        Properties prop = new Properties();
        prop.load(SSHSessionTest.class.getResourceAsStream("loginData"));
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        String hostname = prop.getProperty("hostname");

        session = new SSHSession(username, password, hostname);
    }

    @Test
    public void testExecuteDir() throws Exception {
        String response = session.execute("dir");
        Assert.assertEquals("Response of dir command should not be null", response.length(), 0);
    }



}