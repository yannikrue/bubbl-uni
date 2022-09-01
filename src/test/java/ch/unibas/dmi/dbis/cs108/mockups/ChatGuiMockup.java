package ch.unibas.dmi.dbis.cs108.mockups;

import ch.unibas.dmi.dbis.cs108.gui.ChatGuiInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Used for testing instead of the real ChatGui.
 */
public class ChatGuiMockup implements ChatGuiInterface {

    private ArrayList<String> messages = new ArrayList<>();

    @Override
    public void changePlayerName() {
        // do nothing
    }

    @Override
    public void displayMessage(String message) {
        messages.add(message);
    }


    @Override
    public void logIn() {
        // do nothing
    }

    @Override
    public void setPlayerNames(List<String> playerNames) {
        // do nothing
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

}
