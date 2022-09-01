package ch.unibas.dmi.dbis.cs108.gui;

/**
 * Used for testing purposes.
 */
public interface MainGuiInterface {

    /**
     * Starts the game.
     */
    public void startGame();

    /**
     * Starts the timer.
     */
    public void startTimer();

    /**
     * Ends the game.
     */
    public void endGame();

    /**
     * Sets the a timer.
     */
    public void setTimer();

    /**
     * @return s rank gui of this main gui.
     */
    public RankGuiFX getRankGui();

    /**
     * @return s the lobby gui of this main gui.
     */
    public LobbyGuiInterface getLobbyGui();

}
