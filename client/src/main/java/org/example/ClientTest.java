package org.example;

import org.h2.engine.User;
import org.junit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;


public class ClientTest {


    @Test
    public void givenClient1_whenServerResponds_thenCorrect() throws IOException {
        UserConnect client1 = new UserConnect();
        client1.startConnection("127.0.0.1", 3000);
        String msg1 = client1.sendMessage("hello");
        String msg2 = client1.sendMessage("world");
        String terminate = client1.sendMessage("exit");

        assertEquals(msg1, "hello");
        assertEquals(msg2, "world");
        assertEquals(terminate, "good bye");
    }

    @Test
    public void givenClient2_whenServerResponds_thenCorrect() throws IOException {
        UserConnect client2 = new UserConnect();
        client2.startConnection("127.0.0.1", 3000);
        String msg1 = client2.sendMessage("hello");
        String msg2 = client2.sendMessage("world");
        String terminate = client2.sendMessage("exit");

        assertEquals(msg1, "hello");
        assertEquals(msg2, "world");
        assertEquals(terminate, "good bye");
    }
}
