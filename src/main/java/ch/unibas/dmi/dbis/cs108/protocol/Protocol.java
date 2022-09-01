package ch.unibas.dmi.dbis.cs108.protocol;

public class Protocol {

    // --- Communication from server to client ---

    // When a new Connection between the Server and a Client is established, the Client is asked to SUBMIT a valid
    // player name. Only then the user is allowed to proceed.
    public static final String SUBMIT = "SBMT";
    // The Server confirms to the Client that the player name provided is valid and has been ACCEPTED.
    public static final String ACCEPTED = "ACCE";
    // The Server tells the Client that the player name provided is invalid (already in use) and has been REJECTED.
    public static final String REJECTED = "REJE";
    // The Server tells the Clients to display the parameter representing a message following the command CHAT in the
    // the text area of their corresponding ClientGuis.
    public static final String CHAT = "CHAT";
    // The Server sends regularly a ping to the client to check for connection losses
    public static final String PING = "PING";
    // The Servers reply to the pong from the client
    public static final String PONG_ANSWER = "POAW";
    // The Server grants player names to Client
    public static final String GRANT_PLAYER_NAMES = "GPLN";
    // The Server confirms that the client joined a new lobby
    public static final String NEW_LOBBY = "NWLB";
    // The Server grants all existing lobbies and the current game state
    public static final String GRANT_LOBBY_LIST = "GRLB";
    // The Server grants all the names in the current lobby
    public static final String GRANT_NAMES_IN_LOBBY = "GNLB";
    // The server confirms that the client is in a new game
    public static final String NEW_GAME = "NWGM";
    // The Server ends the game.
    public static final String END_GAME = "ENGM";
    // The Server sends the current position of the players
    public static final String SEND_PLAYER_POSITIONS = "SPPS";
    // The Server sends the current sizes of the players
    public static final String SEND_PLAYER_SIZES = "SPSS";
    // The Server sends the player rotation angle
    public static final String SEND_PLAYER_ANGLES = "SPAN";
    // The Server sends the current position of the bubbles
    public static final String SEND_BUBBLE_POSITION = "SBPS";
    // The Server sends the id of the bubbles
    public static final String SEND_BUBBLE_ID = "SBID";
    // The Server sends the current position of the energizers
    public static final String SEND_ENERGIZER_POSITIONS = "SEPS";
    // The Server grants the player id
    public static final String SEND_PLAYER_ID = "SPID";
    // The Server grants the ranking of the game
    public static final String SEND_RANKING = "SRAN";
    // The Server sends the high score as a concatenated string to the Client.
    public static final String SEND_HIGH_SCORE = "SHSC";
    // The Server confirms the Client that it can QUIT
    public static final String QUIT_CONFIRMED = "QTCD";

    // --- Communication from client to server ---


    // The command NAME + playerName is used by the Client to send a player name to the Server to be evaluated and
    // either accepted or rejected.
    public static final String NAME = "NAME";
    // The command CHANGE_NAME is used by the Client to ask the Server to change the existing player name of a player
    public static final String CHANGE_NAME = "CHNM";
    // By sending BROADCAST + message, the Client asks the Server to broadcast the message provided to all connected
    // clients.
    public static final String BROADCAST = "BROD";
    // The Client informs the Server that the player QUIT.
    public static final String QUIT = "QUIT";
    // The client send regularly a pong to the server to check for connection losses
    public static final String PONG = "PONG";
    // The client reply to the ping from the server
    public static final String PING_ANSWER = "PIAW";
    // The Client requests all player names from the Server
    public static final String REQUEST_PLAYER_NAMES = "RPLN";
    // By sending WHISPER +  "|" + toPlayerName + "|" + message, the Client asks the Server to send the message only to the
    // specified player toPlayerName.
    public static final String WHISPER = "WHSP";
    // The client asks the Server with SET_UP_LOBBY + lobby name to set up new lobby with this name
    public static final String SET_UP_LOBBY = "SULB";
    // The client asks the Server with JOIN_LOBBY + lobby name to join this lobby
    public static final String JOIN_LOBBY = "JNLB";
    // The client asks the Server with LEAVE_LOBBY + lobby name to leave this lobby
    public static final String LEAVE_LOBBY = "LVLB";
    // The client asks the Server with BROADCAST_LOBBY + lobby name + message to send the message to all player
    // in the lobby with this lobby name
    public static final String BROADCAST_LOBBY = "BRLB";
    // The client asks the Server to create a new game for a provided lobby name
    public static final String REQUEST_NEW_GAME = "RNGM";
    // The client asks the Server to move in the direction specified by the parameter (e.g. MOVE + down to move
    // downwards).
    public static final String MOVE = "MOVE";
    //The client asks the Server to spawn new bubble in the direction specified by the parameter
    // (e.g. CREATE_BUBBLE + direction)
    public static final String CREATE_BUBBLE = "CBBL";
    //The client asks the server to change the angle of the player
    public static final String ANGLE = "ANGL";
    // The clients asks the Server to end the game.
    public static final String REQUEST_END_GAME = "REGM";

}
