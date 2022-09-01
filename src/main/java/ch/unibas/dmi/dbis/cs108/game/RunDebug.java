package ch.unibas.dmi.dbis.cs108.game;

import javafx.application.Application;

public class RunDebug {

    /**
     * This is simply a wrapper to launch the {@link DebugGUI} class.
     * The reason this class exists is documented in {@link DebugGUI#main(String[])}
     */
    public static void main(String[] args) {
        //Application.launch(GUI.class, args);
        Application.launch(DebugGUI.class, args);
    }
}
