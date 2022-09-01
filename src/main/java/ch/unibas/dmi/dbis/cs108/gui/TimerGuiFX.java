package ch.unibas.dmi.dbis.cs108.gui;

import ch.unibas.dmi.dbis.cs108.timer.Timer;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

import java.util.concurrent.TimeUnit;

public class TimerGuiFX extends BorderPane {

    private Timer timer;
    private Label timeField = new Label();

    public TimerGuiFX(Timer timer) {
        this.timer = timer;
        timer.setTimerGui(this);
        updateTime(timer.getTime());
        initGui();
    }

    /**
     * Initializes the timer gui.
     */
    private void initGui() {
        timeField.setFont(new Font(20));
        setTop(timeField);
    }

    /**
     * Update the time.
     * @param time: time in seconds.
     */
    public void updateTime(int time) {
        // https://stackoverflow.com/questions/8916472/convert-integer-minutes-into-string-hhmm
        long minutes = TimeUnit.SECONDS.toMinutes(time);
        long remainingSeconds = time - TimeUnit.MINUTES.toSeconds(minutes);
        String timeString = String.format("%02d:%02d", minutes, remainingSeconds);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                timeField.setText("Countdown: " + timeString);
            }
        });
    }

    /**
     * Show / hide timer gui
     */
    private void showTimer(boolean bool) {
        this.setVisible(bool);
    }

}
