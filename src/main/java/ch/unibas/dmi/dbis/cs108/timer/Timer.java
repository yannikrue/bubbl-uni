package ch.unibas.dmi.dbis.cs108.timer;

import ch.unibas.dmi.dbis.cs108.game.server.ServerGameLoop;
import ch.unibas.dmi.dbis.cs108.gui.TimerGuiFX;

import java.util.concurrent.TimeUnit;

public class Timer implements Runnable {

    private int time;  // count down in seconds.
    private TimerGuiFX timerGui;
    private ServerGameLoop gameLoop;
    private boolean running = true;

    public Timer(int startTime) {
        this.time = startTime;
    }

    /**
     * Starts the timer in a new thread.
     */
    public void startTimer() {
        new Thread(this).start();
    }

    /**
     * Counts down from start time to zero.
     */
    public void run() {
        while (time > 0 && running) {
            try {
                TimeUnit.SECONDS.sleep(1);
                time--;
                if (timerGui != null) {
                    timerGui.updateTime(time);
                } else if (gameLoop != null) {
                    gameLoop.updateTime(time);
                }
            } catch (InterruptedException ie) {
                // do nothing for now
            }
        }   // startTime == 0
    }

    /**
     * Set timer gui.
     */
    public void setTimerGui(TimerGuiFX timerGui) {
        this.timerGui = timerGui;
    }

    /**
     * Set game loop.
     */
    public void setGameLoop(ServerGameLoop gameLoop) {this.gameLoop = gameLoop;}

    /**
     * Set running.
     */
    public void setRunning(boolean running) {this.running = running;}

    /**
     * @return time
     */
    public int getTime() {
        return time;
    }

    /**
     * Sets time.
     */
    public void setTime(int time) {this.time = time;}

    /**
     * @return the time as string.
     */
    public String getTimeString() {
        long minutes = TimeUnit.SECONDS.toMinutes(this.time);
        long remainingSeconds = this.time - TimeUnit.MINUTES.toSeconds(minutes);
        String timeString = String.format("%02d:%02d", minutes, remainingSeconds);
        return timeString;
    }

}
