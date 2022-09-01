package ch.unibas.dmi.dbis.cs108.connection;

import static org.junit.Assert.assertTrue;

import java.net.Socket;
import ch.unibas.dmi.dbis.cs108.server.Server;
import org.junit.Test;

public class ConnectionTest {

    @Test
    public void testIsDefaultName() {
        Server server = new Server("57532");
        Socket socket = new Socket();
        Connection connection = new Connection(server, socket);
        assertTrue(connection.isDefaultName("[New Player]"));
    }

}
