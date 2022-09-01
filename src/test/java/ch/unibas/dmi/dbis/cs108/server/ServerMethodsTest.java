package ch.unibas.dmi.dbis.cs108.server;

import ch.unibas.dmi.dbis.cs108.Lobby.Lobby;
import ch.unibas.dmi.dbis.cs108.client.Client;
import ch.unibas.dmi.dbis.cs108.mockups.ChatGuiMockup;
import ch.unibas.dmi.dbis.cs108.mockups.ClientReceiverMockup;
import ch.unibas.dmi.dbis.cs108.mockups.GameGuiMockup;
import ch.unibas.dmi.dbis.cs108.mockups.HighScoreDialogMockup;
import ch.unibas.dmi.dbis.cs108.mockups.LobbyGuiMockup;
import ch.unibas.dmi.dbis.cs108.mockups.MainGuiMockup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Ignore integration tests since they take longer than regular unit-tests.
 * This can be accomplished using the annotation @Ignore (which will show the tests as ignored and thus yellow instead
 * of green) or making the class abstract (in which case the tests do not show up at all).
 * https://stackoverflow.com/questions/461644/how-to-get-junit-4-to-ignore-a-base-test-class
 */
abstract public class ServerMethodsTest {

    private static final Logger logger = LogManager.getLogger(ServerMethodsTest.class);

    private static final String LOCALHOST = "Localhost";
    private static final int PORT = 57532;

    private String lobbyName;
    private String playerName1;
    private String playerName2;
    private String playerName3;
    private String playerName4;
    private String newPlayerName1;
    private String message;
    private String message1;
    private String message2;

    private Server server;
    private Client client1;
    private Client client2;
    private Client client3;
    private Client client4;

    @Before
    /**
     * Runs before every test case
     */
    public void before() {
        lobbyName = "MyLobby";
        playerName1 = "Andrew";
        playerName2 = "James";
        playerName3 = "Rémi";
        playerName4 = "Kilian";
        newPlayerName1 = "Ben";
        message = "Hello";
        message1 = "Hello James";
        message2 = "Hello Rémi";

        server = new Server("57532");
        server.startServer();

        client1 = new Client(new ClientReceiverMockup(), null);
        client1.setPlayerName(playerName1);
        client1.setPortNumber(PORT);
        client1.setHost(LOCALHOST);
        client1.startClient();
        client1.setChatGui(new ChatGuiMockup());
        client1.setGameGui(new GameGuiMockup());
        LobbyGuiMockup lobbyGui1 = new LobbyGuiMockup();
        lobbyGui1.setHighScoreDialog(new HighScoreDialogMockup());
        client1.setLobbyGui(lobbyGui1);
        MainGuiMockup mainGui1 = new MainGuiMockup();
        mainGui1.setLobbyGui(lobbyGui1);
        client1.setMainGui(mainGui1);

        client2 = new Client(new ClientReceiverMockup(), null);
        client2.setPlayerName(playerName2);
        client2.setPortNumber(PORT);
        client2.setHost(LOCALHOST);
        client2.startClient();
        client2.setChatGui(new ChatGuiMockup());
        client2.setGameGui(new GameGuiMockup());
        client2.setLobbyGui(new LobbyGuiMockup());
        LobbyGuiMockup lobbyGui2 = new LobbyGuiMockup();
        lobbyGui2.setHighScoreDialog(new HighScoreDialogMockup());
        client2.setLobbyGui(lobbyGui2);
        MainGuiMockup mainGui2 = new MainGuiMockup();
        mainGui2.setLobbyGui(lobbyGui2);
        client2.setMainGui(mainGui2);

        client3 = new Client(new ClientReceiverMockup(), null);
        client3.setPlayerName(playerName3);
        client3.setPortNumber(PORT);
        client3.setHost(LOCALHOST);
        client3.startClient();
        client3.setChatGui(new ChatGuiMockup());
        client3.setGameGui(new GameGuiMockup());
        client3.setLobbyGui(new LobbyGuiMockup());
        LobbyGuiMockup lobbyGui3 = new LobbyGuiMockup();
        lobbyGui3.setHighScoreDialog(new HighScoreDialogMockup());
        client3.setLobbyGui(lobbyGui3);
        MainGuiMockup mainGui3 = new MainGuiMockup();
        mainGui3.setLobbyGui(lobbyGui3);
        client3.setMainGui(mainGui3);

        client4 = new Client(new ClientReceiverMockup(), null);
        client4.setPlayerName(playerName4);
        client4.setPortNumber(PORT);
        client4.setHost(LOCALHOST);
        client4.startClient();
        client4.setChatGui(new ChatGuiMockup());
        client4.setGameGui(new GameGuiMockup());
        client4.setLobbyGui(new LobbyGuiMockup());
        LobbyGuiMockup lobbyGui4 = new LobbyGuiMockup();
        lobbyGui4.setHighScoreDialog(new HighScoreDialogMockup());
        client4.setLobbyGui(lobbyGui4);
        MainGuiMockup mainGui4 = new MainGuiMockup();
        mainGui4.setLobbyGui(lobbyGui4);
        client4.setMainGui(mainGui4);

        // wait for the clients to connect to the server
        try {
            TimeUnit.SECONDS.sleep(6);
        } catch (InterruptedException ie) {
            // do nothing
        }
    }

    @After
    /**
     * Runs after every test case.
     */
    public void after() {

        // wait for the clients to connect to the server
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException ie) {
            // do nothing
        }

        server.stopServer();
    }

    @Test
    public void testMethodChangePlayerNameOfConnection() {

        client1.askServerToSetUpLobby(lobbyName);

        // wait for the lobbies
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ie) {
            // do nothing
        }

        assertTrue(server.containsPlayerName(playerName1));
        assertFalse(server.containsPlayerName(newPlayerName1));

        client1.askServerToChangePlayerName(newPlayerName1, lobbyName);

        // wait for the name to be updated
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ie) {
            // do nothing
        }

        assertFalse(server.containsPlayerName(playerName1));
        assertTrue(server.containsPlayerName(newPlayerName1));
    }

    /**
     * Tests the method addLobby if the server class.
     */
    @Test
    public void testMethodAddLobby() {

        client1.askServerToSetUpLobby(lobbyName);

        // wait for the lobbies
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ie) {
            // do nothing
        }

        ArrayList<Lobby> lobbies = server.getLobbies();
        assertTrue(lobbies.size() == 1);
        assertTrue(lobbies.get(0).getLobbyName().equals(lobbyName));
        assertTrue(lobbies.get(0).getNumberOfPlayers() == 1);
    }

    /**
     * Tests the method joinLobby if the server class.
     * Note: this test seems to fail when running it as a batch but works when running on its own.
     */
    @Test
    public void testMethodJoinLobby() {

        client1.askServerToSetUpLobby(lobbyName);

        // wait for the lobbies
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ie) {
            // do nothing
        }

        client2.askServerToJoinLobby(lobbyName);

        // wait for the lobbies
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ie) {
            // do nothing
        }

        ArrayList<Lobby> lobbies = server.getLobbies();
        assertTrue(lobbies.size() == 1);
        assertTrue(lobbies.get(0).getLobbyName().equals(lobbyName));
        System.out.println("Number of players in lobby: " + lobbies.get(0).getNumberOfPlayers());
        assertTrue(lobbies.get(0).getNumberOfPlayers() == 2);
    }

    /**
     * Tests the method removePlayerFromLobby if the server class.
     */
    @Test
    public void testMethodRemovePlayerFromLobby() {

        client1.askServerToSetUpLobby(lobbyName);

        // wait for the lobbies
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ie) {
            // do nothing
        }

        ArrayList<Lobby> lobbies = server.getLobbies();
        assertTrue(lobbies.size() == 1);
        assertTrue(lobbies.get(0).getNumberOfPlayers() == 1);

        client1.leaveLobby(lobbyName);

        // wait for the lobbies
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ie) {
            // do nothing
        }

        assertTrue(lobbies.get(0).getNumberOfPlayers() == 0);
    }

    /**
     * Tests the method broadcastLobbyMessage if the server class.
     */
    @Test
    public void testMethodBroadcastLobbyMessage() {

        client1.askServerToSetUpLobby(lobbyName);
        client2.askServerToJoinLobby(lobbyName);

        // wait for the lobbies
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ie) {
            // do nothing
        }

        ArrayList<Lobby> lobbies = server.getLobbies();
        assertTrue(lobbies.size() == 1);
        assertTrue(lobbies.get(0).getLobbyName().equals(lobbyName));
        assertTrue(lobbies.get(0).getNumberOfPlayers() == 2);

        client1.sendMessageToLobby(lobbyName, message);

        // wait for the message
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ie) {
            // do nothing
        }

        // client 1 and 2 are in te same lobby
        ChatGuiMockup chatGui1 = (ChatGuiMockup) client1.getChatGui();
        ArrayList<String> messages1 = chatGui1.getMessages();
        assertTrue(messages1.get(messages1.size() - 1).equals("Andrew[lobby]: Hello\n\n"));

        ChatGuiMockup chatGui2 = (ChatGuiMockup) client2.getChatGui();
        ArrayList<String> messages2 = chatGui2.getMessages();
        assertTrue(messages2.get(messages2.size() - 1).equals("Andrew[lobby]: Hello\n\n"));

        // client 3 is not in the same lobby (should not receive message)
        ChatGuiMockup chatGui3 = (ChatGuiMockup) client3.getChatGui();
        ArrayList<String> messages3 = chatGui3.getMessages();
        assertFalse(messages3.get(messages3.size() - 1).equals("Andrew[lobby]: Hello\n\n"));
    }

    /**
     * Tests the method broadcastLobbyMessage if the server class.
     */
    @Test
    public void testWhisperMessage() {

        client1.sendMessage(client1.getPlayerName(), client2.getPlayerName(), message1);
        client1.sendMessage(client1.getPlayerName(), client3.getPlayerName(), message2);

        // wait for the message
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ie) {
            // do nothing
        }

        // client 1 sends different whisper messages to client2 and client3
        ChatGuiMockup chatGui1 = (ChatGuiMockup) client1.getChatGui();
        ArrayList<String> messages1 = chatGui1.getMessages();
        System.out.println(messages1.get(messages1.size() - 2));
        assertTrue(messages1.get(messages1.size() - 2).equals("Andrew[whisper]: Hello James\n\n"));
        assertTrue(messages1.get(messages1.size() - 1).equals("Andrew[whisper]: Hello Rémi\n\n"));

        //client 2 receives whisper message from client 1 (should only get message 1)
        ChatGuiMockup chatGui2 = (ChatGuiMockup) client2.getChatGui();
        ArrayList<String> messages2 = chatGui2.getMessages();
        assertTrue(messages2.get(messages2.size() - 1).equals("Andrew[whisper]: Hello James\n\n"));
        assertFalse(messages2.get(messages2.size() - 1).equals("Andrew[whisper]: Hello Rémi"));

        // client 3 receives whisper message from client 1 (should only get message 2)
        ChatGuiMockup chatGui3 = (ChatGuiMockup) client3.getChatGui();
        ArrayList<String> messages3 = chatGui3.getMessages();
        assertFalse(messages3.get(messages3.size() - 1).equals("Andrew[whisper]: Hello James\n\n"));
        assertTrue(messages3.get(messages3.size() - 1).equals("Andrew[whisper]: Hello Rémi\n\n"));

        ChatGuiMockup chatGui4 = (ChatGuiMockup) client4.getChatGui();
        ArrayList<String> messages4 = chatGui4.getMessages();
        assertFalse(messages4.get(messages4.size() - 1).equals("Andrew[whisper]: Hello James\n\n"));
        assertFalse(messages4.get(messages4.size() - 1).equals("Andrew[whisper]: Hello Rémi\n\n"));
    }

}
