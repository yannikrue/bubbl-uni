package ch.unibas.dmi.dbis.cs108.gui;

import ch.unibas.dmi.dbis.cs108.gui.dialog.HighScoreDialogInterface;

public interface LobbyGuiInterface {

    /**
     * Updates the text area with the current lobby name.
     */
    public void displayLobby(String lobbyName);

    /**
     * When the player is not in a lobby the text area lists all open lobbies given
     * by @param lobbyNames.
     */
    public void displayLobbyList(String lobbyNames);

    /**
     * When the player is in a lobby the text area lists all the player in it given
     * by @param playerNames. If the number of player currently in the Lobby are equal to
     * or greater than MINIMAL_NUMBER_OF_PLAYERS, then the startGameButton becomes enabled.
     */
    public void displayPlayerList(String playerNames);

    /**
     * displays all clients @param playerNames connected to the server under the list of open lobbies or
     * player in the lobby.
     */
    public void displayPlayerNames(String playerNames);

    /**
     * @return s the lobby name.
     */
    public String getLobbyName();

    public void leaveLobby();

    /**
     * sets the game button on @param disabled.
     */
    //public void setEndGameButton(boolean disabled);

    /**
     * Sets the libby name to @param lobbyName.
     */
    public void setLobbyName(String lobbyName);

    /**
     * @return s the high score dialog of this lobby gui. This is used for testing purposes.
     */
    public HighScoreDialogInterface getHighScoreDialog();
}
