package ch.unibas.dmi.dbis.cs108.gui.dialog;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.Optional;

public class ChangeNameDialogFX extends Dialog {

    private static final String FILE_NAME = "LogInParameters.txt";

    private boolean cancelled = false;
    private TextField playerNameField = new TextField();

    public ChangeNameDialogFX() {
        initDialog();
    }

    /**
     * Initialises the change name dialog.
     */
    private void initDialog() {
        setTitle("Change Name Dialog");
        setHeaderText("Please enter the new player name");

        //Icon
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        Image img = new Image("/icons/icon.png");
        stage.getIcons().add(img);

        // root
        GridPane root = new GridPane();
        root.setPadding(new Insets(5, 5, 5, 5));
        root.setHgap(10);
        root.setVgap(10);

        // player name field
        Label playerNameLabel = new Label("Player name:");
        root.add(playerNameLabel, 0, 0);
        playerNameField.setPromptText("Player name");
        root.add(playerNameField, 1, 0);

        // buttons
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        getDialogPane().setContent(root);
    }

    /**
     * When the user presses the OK-button, it updates the player name if it is not empty, otherwise the user is
     * notified that the player name is required.
     */
    private void ok() {
        cancelled = false;
        String playerName = playerNameField.getText().trim();
        if (playerName.length() == 0) {
            UtilDialogsFX.noPlayerNameAlert();
            showDialog();
        } else {
            try {
                BufferedReader in = new BufferedReader(new FileReader(FILE_NAME));
                String portNumber = in.readLine();
                String ipAddress = in.readLine();
                in.close();
                BufferedWriter out = new BufferedWriter(new FileWriter(FILE_NAME));
                out.write(portNumber);
                out.write("\n");
                out.write(ipAddress);
                out.write("\n");
                out.write(playerName);
                out.close();
            } catch (IOException ioe) {
                UtilDialogsFX.displayErrorDialog("An error was encountered when trying to write to " + FILE_NAME);
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
            // dialog is exited without pressing a button
        } else if (result.get().equals(ButtonType.OK)) {
            ok();
        } else if (result.get().equals(ButtonType.CANCEL)) {
            cancel();
        }
    }

    private void cancel() {
        cancelled = true;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public String getPlayerName() {
        return playerNameField.getText().trim();
    }

}
