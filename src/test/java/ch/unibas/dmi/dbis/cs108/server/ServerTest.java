package ch.unibas.dmi.dbis.cs108.server;

import ch.unibas.dmi.dbis.cs108.connection.Connection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.net.Socket;

public class ServerTest {

    private static final String LOCALHOST = "Localhost";
    private static final int PORT = 57532;

    /**
     * The default player name "[New Player]" should not be considered a valid player name.
     */
    @Test
    public void testAddingDefaultPlayerName() {
        Server server = new Server("57532");
        Socket socket = new Socket();
        Connection connection = new Connection(server, socket);
        boolean added = server.addConnection(connection, Connection.getDefaultName());
        assertFalse(added);
    }

    /**
     * Adding a valid player name.
     */
    @Test
    public void testAddingValidPlayerName() {
        String validPlayerName = "Andrew";
        Server server = new Server("57532");
        Socket socket = new Socket();
        Connection connection = new Connection(server, socket);
        boolean added = server.addConnection(connection, validPlayerName);
        if (added) {
            connection.setPlayerName(validPlayerName);
        }
        assertTrue(added);
    }

    /**
     * Adding two player with the same player name should not work as player names have to be unique.
     */
    @Test
    public void testAddingTwiceTheSamePlayerName() {
        Server server = new Server("57532");
        Socket socket = new Socket();
        String playerName = "Andrew";
        Connection connection = new Connection(server, socket);
        boolean added = server.addConnection(connection, playerName);
        if (added) {
            connection.setPlayerName(playerName);
        }
        assertTrue(added);

        String samePlayerName = "Andrew";
        Connection anotherConnection = new Connection(server, socket);
        added = server.addConnection(connection, samePlayerName);
        if (added) {
            connection.setPlayerName(playerName);
        }
        assertFalse(added);
    }

    /**
     * Trying to change a valid player name "[New Player]" to the default player name should not work.
     */
    @Test
    public void testChangingToDefaultPlayerName() {
        String validPlayerName = "Andrew";
        Server server = new Server("57532");
        Socket socket = new Socket();
        Connection connection = new Connection(server, socket);
        boolean added = server.addConnection(connection, validPlayerName);
        if (added) {
            connection.setPlayerName(validPlayerName);
        }
        boolean changed = server.changePlayerNameOfConnection(validPlayerName,Connection.getDefaultName());
        if (changed) {
            connection.setPlayerName(Connection.getDefaultName());
        }
        assertTrue(connection.getPlayerName().equals(validPlayerName));
    }

    /**
     * The player must be able to change the name during the game to another valid player name.
     */
    @Test
    public void testChangePlayerNameToValidName() {
        String validPlayerName = "Andrew";
        String anotherValidPlayerName = "James";
        Server server = new Server("57532");
        Socket socket = new Socket();
        Connection connection = new Connection(server, socket);
        boolean added = server.addConnection(connection, validPlayerName);
        if (added) {
            connection.setPlayerName(validPlayerName);
        }
        boolean changed = server.changePlayerNameOfConnection(validPlayerName,anotherValidPlayerName);
        if (changed) {
            connection.setPlayerName(anotherValidPlayerName);
        }
        assertTrue(connection.getPlayerName().equals(anotherValidPlayerName));
    }

    /**
     * Try to change a player name to an already existing name should not work.
     */
    @Test
    public void testChangePlayerNameToExistingName(){
        String playerName = "Andrew";
        String playerName2 = "Bob";
        Server server = new Server("57532");
        Socket socket = new Socket();
        Connection connection = new Connection(server, socket);
        boolean added = server.addConnection(connection, playerName);
        if (added) {
            connection.setPlayerName(playerName);
        }
        assertTrue(added);

        Connection connection2 = new Connection(server, socket);
        added = server.addConnection(connection, playerName2);
        if (added) {
            connection.setPlayerName(playerName2);
        }
        assertTrue(added);

        boolean change = server.changePlayerNameOfConnection(playerName, playerName2);
        assertFalse(change);

    }

}
