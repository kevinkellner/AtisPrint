package edu.kit.uneig.atisprint.tests;

import com.jcraft.jsch.JSchException;
import edu.kit.uneig.atisprint.SSHSession;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Scanner;

import static junit.framework.Assert.assertEquals;

/**
 * Created by kelln_000 on 10.06.2015.
 */
public class SSHSessionTest {

    private SSHSession session;

    @BeforeClass
    public void setup() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter username");
        String username = in.next();
        System.out.println("Enter password");
        String password = in.next();
        session = new SSHSession(username, password, "i08fs1.ira.uka.de");
    }

    @Test
    public void testExecuteDir() throws IOException, JSchException {
        String response = session.execute("dir");
        assertEquals("Response of dir command should not be null", response.length(), 0);
    }



}