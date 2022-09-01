package ch.unibas.dmi.dbis.cs108.gui;

import java.util.List;

/**
 * The ChatGuiInterface is the basis for the ChatGuiFX as well as the ChatGuiMockup.
 */
public interface ChatGuiInterface {

    /**
     * Change the player name of the player.
     */
    public void changePlayerName();

    /**
     * Displays the @param message in the text area of the chat.
     */
    public void displayMessage(String message);

    /**
     * Log in the chat.
     */
    public void logIn();

    /**
     * Sets the @param playerNames, a list of all currently available players. for the whisper and lobby chat.
     */
    public void setPlayerNames(List<String> playerNames);

}
