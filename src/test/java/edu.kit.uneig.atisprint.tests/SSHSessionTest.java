package edu.kit.uneig.atisprint.tests;

import edu.kit.uneig.atisprint.SSHInterface;
import edu.kit.uneig.atisprint.SSHSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.Properties;

/**
 * Contains tests for the ssh session class.
 */
public class SSHSessionTest {

    private static SSHInterface session;

    public SSHSessionTest() {
        super();
    }

    @Before
    public void setup() throws Exception {
        Properties prop = new Properties();
        InputStream instream = getClass().getResourceAsStream("loginData");
        prop.load(instream);
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        String hostname = prop.getProperty("hostname");

        session = new SSHSession(username, password, hostname);
    }

    @Test
    public void testExecuteDir() throws Exception {
        String response = session.execute("dir");
        Assert.assertNotEquals("Response of dir command should not be null", response.length(), 0);
    }

    @Test
    public void testCopyFile() throws Exception {
        InputStream instream = getClass().getClassLoader().getResourceAsStream("test.txt");
        session.copy("AtisPrintTest/", "test.txt", instream);
    }


}