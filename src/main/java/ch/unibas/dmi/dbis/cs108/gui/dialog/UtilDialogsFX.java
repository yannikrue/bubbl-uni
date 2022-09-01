package ch.unibas.dmi.dbis.cs108.gui.dialog;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class UtilDialogsFX {

    /**
     * Dialog that is displayed if the player does not enter a port number in the log in dialog.
     */
    public static void noPortNumberAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Port number required");
        alert.setContentText("Please enter a port number");

        alert.showAndWait();
    }

    /**
     * Dialog that is displayed if the player does not enter an IP address in the log in dialog.
     */
    public static void noIpAddressAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("IP address required");
        alert.setContentText("Please enter an IP address");

        alert.showAndWait();
    }

    /**
     * Dialog that is displayed if the player does not enter a name in the log in dialog or change name dialog.
     */
    public static void noPlayerNameAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Player name required");
        alert.setContentText("Please enter a player name");

        alert.showAndWait();
    }

    /**
     * Dialog that is displayed if the player does not enter a lobby name.
     */
    public static void noLobbyNameAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Lobby name invalid");
        alert.setContentText("Please enter a valid Lobby name");

        alert.showAndWait();
    }

    /**
     * Displays an alert with the @param text.
     */
    public static void displayErrorDialog(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }

}
