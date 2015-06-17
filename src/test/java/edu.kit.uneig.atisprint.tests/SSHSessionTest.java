package edu.kit.uneig.atisprint.tests;

import android.content.res.Resources;
import android.test.InstrumentationTestCase;
import android.util.Log;
import com.jcraft.jsch.JSchException;
import edu.kit.uneig.atisprint.SSHInterface;
import edu.kit.uneig.atisprint.SSHSession;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

/**
 * Contains tests for the ssh session class.
 */
public class SSHSessionTest extends InstrumentationTestCase{

    private static SSHInterface session;

    @Before
    public void setup() throws Exception {
        super.setUp();
        Properties prop = new Properties();
        InputStream instream = getInstrumentation().getTargetContext().getResources().getAssets().open("loginData");
        prop.load(instream);
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        String hostname = prop.getProperty("hostname");

        session = new SSHSession(username, password, hostname);
    }

    @Test
    public void testExecuteDir() throws Exception {
        String response = session.execute("touch test.txt.iwashere");
        Assert.assertEquals("Response of dir command should not be null", response.length(), 0);
        Log.d("Response", response);
    }

    @Test
    public void testCopy() throws Exception {
        session.copy("test/", "test.txt",  getInstrumentation().getTargetContext().getResources().getAssets().open("test.txt"));
    }


}