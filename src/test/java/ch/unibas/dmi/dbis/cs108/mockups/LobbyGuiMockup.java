package ch.unibas.dmi.dbis.cs108.mockups;

import ch.unibas.dmi.dbis.cs108.gui.LobbyGuiInterface;
import ch.unibas.dmi.dbis.cs108.gui.dialog.HighScoreDialogInterface;

/**
 * Used for testing instead of the real LobbyGui.
 */
public class LobbyGuiMockup implements LobbyGuiInterface {

    private HighScoreDialogInterface highScoreDialog;

    @Override
    public void displayLobby(String lobbyName) {
        // do nothing
    }

    @Override
    public void displayLobbyList(String lobbyNames) {
        // do nothing
    }

    @Override
    public void displayPlayerList(String playerNames) {
        // do nothing
    }

    @Override
    public void displayPlayerNames(String playerNames) {
        // do nothing
    }

    @Override
    public String getLobbyName() {
        return null;
    }

    @Override
    public void leaveLobby() {
        // do nothing
    }

    //@Override
    //public void setEndGameButton(boolean disabled) {
        // do nothing
    //}

    @Override
    public void setLobbyName(String lobbyName) {
        // do nothing
    }

    @Override
    public HighScoreDialogInterface getHighScoreDialog() {
        return highScoreDialog;
    }

    public void setHighScoreDialog(HighScoreDialogInterface highScoreDialog) {
        this.highScoreDialog = highScoreDialog;
    }
}
