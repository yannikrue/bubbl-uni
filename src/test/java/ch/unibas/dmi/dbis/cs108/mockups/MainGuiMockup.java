package ch.unibas.dmi.dbis.cs108.mockups;

import ch.unibas.dmi.dbis.cs108.gui.LobbyGuiFX;
import ch.unibas.dmi.dbis.cs108.gui.LobbyGuiInterface;
import ch.unibas.dmi.dbis.cs108.gui.MainGuiInterface;
import ch.unibas.dmi.dbis.cs108.gui.RankGuiFX;

public class MainGuiMockup implements MainGuiInterface {

    private LobbyGuiInterface lobbyGui;

    @Override
    public void startGame() {
        // do nothing
    }

    @Override
    public void startTimer() {
        // do nothing
    }

    @Override
    public void endGame() {
        // do nothing
    }

    @Override
    public void setTimer() {
        // do nothing
    }

    @Override
    public RankGuiFX getRankGui() {
        return null;
    }

    @Override
    public LobbyGuiInterface getLobbyGui() {
        return lobbyGui;
    }

    public void setLobbyGui(LobbyGuiInterface lobbyGui) {
        this.lobbyGui = lobbyGui;
    }

}
