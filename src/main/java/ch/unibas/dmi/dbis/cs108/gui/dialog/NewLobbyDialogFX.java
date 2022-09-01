package ch.unibas.dmi.dbis.cs108.gui.dialog;

import ch.unibas.dmi.dbis.cs108.util.Util;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class NewLobbyDialogFX extends Dialog{
    private boolean cancelled = false;
    private TextField lobbyNameField = new TextField();

    public NewLobbyDialogFX() {
        initDialog();
    }

    /**
     * Initialise the join lobby dialog
     */
    private void initDialog() {
        setTitle("New Lobby Dialog");
        setHeaderText("Please enter the lobby name");

        //Icon
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        Image img = new Image("/icons/icon.png");
        stage.getIcons().add(img);

        //root
        GridPane root = new GridPane();
        root.setPadding(new Insets(5, 5, 5, 5));
        root.setHgap(10);
        root.setVgap(10);

        //lobby name field
        Label lobbyNameLabel = new Label("Lobby name:");
        root.add(lobbyNameLabel, 0, 0);
        lobbyNameField.setPromptText("Lobby name");
        root.add(lobbyNameField, 1, 0);

        //buttons
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        getDialogPane().setContent(root);
    }

    /**
     * When the user presses the OK-button, it updates the lobby name if it is not empty, otherwise the user is
     * notified that the lobby name is required.
     */
    private void ok() {
        cancelled = false;
        String lobbyName = lobbyNameField.getText().trim();
        lobbyName = Util.removeIllegalCharacters(lobbyName, new ArrayList<Character>(Arrays.asList('/', '|')));
        if (lobbyName.length() == 0 || lobbyName.equals("DEFAULT")) {
            UtilDialogsFX.noLobbyNameAlert();
            showDialog();
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

    /**
     * cancel the dialog
     */
    private void cancel() {
        cancelled = true;
    }

    /**
     * @return s cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * @return s the entered lobby name
     */
    public String getLobbyName() {
        return lobbyNameField.getText().trim();
    }
}
