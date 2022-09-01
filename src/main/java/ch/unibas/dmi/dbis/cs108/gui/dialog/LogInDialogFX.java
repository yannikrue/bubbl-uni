package ch.unibas.dmi.dbis.cs108.gui.dialog;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import static ch.unibas.dmi.dbis.cs108.gui.dialog.UtilDialogsFX.noPortNumberAlert;
import static ch.unibas.dmi.dbis.cs108.gui.dialog.UtilDialogsFX.noIpAddressAlert;
import static ch.unibas.dmi.dbis.cs108.gui.dialog.UtilDialogsFX.noPlayerNameAlert;
import static ch.unibas.dmi.dbis.cs108.gui.dialog.UtilDialogsFX.displayErrorDialog;

public class LogInDialogFX extends Dialog {

    private static final String FILE_NAME = "LogInParameters.txt";

    private boolean cancelled = false;
    private TextField portNumberField = new TextField();
    private TextField ipAddressField = new TextField();
    private TextField playerNameField = new TextField();

    public LogInDialogFX() {
        initDialog();
    }

    /**
     * Initialises the log in dialog.
     */
    private void initDialog() {
        setTitle("Log In Dialog");
        setHeaderText(null);

        //Icon
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        Image img = new Image("/icons/icon.png");
        stage.getIcons().add(img);

        // root
        GridPane root = new GridPane();
        root.setPadding(new Insets(5, 5, 5, 5));
        root.setHgap(10);
        root.setVgap(10);

        // port number field
        Label portNumberLabel = new Label("Port number:");
        root.add(portNumberLabel, 0, 0);
        portNumberField.setPromptText("Port number");
        root.add(portNumberField, 1, 0);

        // IP address field
        Label ipAddressLabel = new Label("IP address:");
        root.add(ipAddressLabel, 0, 1);
        ipAddressField.setPromptText("IP address");
        root.add(ipAddressField, 1, 1);

        // player name field
        Label playerNameLabel = new Label("Player name:");
        root.add(playerNameLabel, 0, 2);
        playerNameField.setPromptText("Player name");
        root.add(playerNameField, 1, 2);

        // load previously used parameters
        try {
            BufferedReader in = new BufferedReader(new FileReader(FILE_NAME));
            portNumberField.setText(in.readLine());
            ipAddressField.setText(in.readLine());
            playerNameField.setText(in.readLine());
            in.close();
        } catch (FileNotFoundException fnfe) {
            // take the system user as player name
            playerNameField.setText(System.getProperty("user.name"));
        } catch (IOException ioe) {
            displayErrorDialog("An error was thrown when trying to read from file " + FILE_NAME);
        }

        // buttons
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        getDialogPane().setContent(root);
    }

    /**
     * Called by pressing the ok-button of the LogInDialog: reads the port number, IP address, and player name provided
     * by the user and ensures that none of them is empty. Then persistently stores these values in a local text file which is
     * read the next time the Client tries to log in.
     */
    public void ok() {
        cancelled = false;
        String portNumber = portNumberField.getText().trim();
        String ipAddress = ipAddressField.getText().trim();
        String playerName = playerNameField.getText().trim();
        if (portNumber.length() == 0) {
            noPortNumberAlert();
            showDialog();   // otherwise the alert-dialog can be closed without providing an input
        } else if (ipAddress.length() == 0) {
            noIpAddressAlert();
            showDialog();
        } else if (playerName.length() == 0) {
            noPlayerNameAlert();
            showDialog();
        } else {
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(FILE_NAME));
                out.write(portNumber);
                out.write("\n");
                out.write(ipAddress);
                out.write("\n");
                out.write(playerName);
                out.close();
            } catch (IOException ioe) {
                displayErrorDialog("An error was encountered when trying to write to " + FILE_NAME);
            }
        }
    }

    /**
     * This method is called externally to show the dialog. It calls the corresponding method for the button that the
     * user pressed.
     * https://stackoverflow.com/questions/43031602/how-to-set-a-method-to-a-javafx-alert-button
     */
    public void showDialog() {
        Optional<ButtonType> result = showAndWait();
        if (!result.isPresent()) {
            cancel();
        } else if (result.get().equals(ButtonType.OK)) {
            ok();
        } else if (result.get().equals(ButtonType.CANCEL)) {
            cancel();
        }
    }

    /**
     * Called by pressing the cancel-button of the LogInDialog: cancels the LogInDialog.
     */
    public void cancel() {
        cancelled = true;
    }

    /**
     * @return port number as String.
     */
    public String getPortNumber() {
        return portNumberField.getText().trim();
    }

    /**
     * Sets the portNumberField to param portNumber.
     */
    public void setPortNumber(String portNumber) {this.portNumberField.setText(portNumber);}

    /**
     * @return IP address as String.
     */
    public String getIpAddress() {
        return ipAddressField.getText().trim();
    }

    /**
     * Sets the ipAddressField to param ip.
     */
    public void setIpAddress(String ip) {
        this.ipAddressField.setText(ip);
    }

    /**
     * @return player name as String.
     */
    public String getPlayerName() {
        return playerNameField.getText().trim();
    }

    /**
     * Sets the player name.
     */
    public void setPlayerName(String playerName) {
        playerNameField.setText(playerName);
    }

    /**
     * @return true if the LogInDialog has been cancelled and false otherwise.
     */
    public boolean isCancelled() {
        return cancelled;
    }

}
