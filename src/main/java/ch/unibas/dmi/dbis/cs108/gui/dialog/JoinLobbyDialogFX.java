package ch.unibas.dmi.dbis.cs108.gui.dialog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class JoinLobbyDialogFX extends Dialog {

    private boolean cancelled = false;
    private ChoiceBox<String> lobbyChoice = new ChoiceBox<>();

    public JoinLobbyDialogFX() {
        initDialog();
    }

    /**
     * Initialise the join lobby dialog
     */
    private void initDialog() {
        setTitle("Join Lobby Dialog");
        setHeaderText("Please choose the lobby");

        //Icon
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        Image img = new Image("/icons/icon.png");
        stage.getIcons().add(img);

        //root
        GridPane root = new GridPane();
        root.setPadding(new Insets(5, 5, 5, 5));
        root.setHgap(10);
        root.setVgap(10);

        //choice box
        root.getChildren().add(lobbyChoice);

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        getDialogPane().setContent(root);
    }

    /**
     * When the user presses the OK-button, it updates the lobby name
     */
    private void ok() {
        cancelled = false;
        String lobbyName = lobbyChoice.getValue();
        if (lobbyName.length() == 0) {
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
     * @return s value of the choice box
     */
    public String getLobbyName() {
        return lobbyChoice.getValue();
    }

    /**
     * Sets choice box lobbyChoice with @param list
     */
    public void setLobbyChoice(List<String> list) {
        ObservableList<String> names = FXCollections.observableArrayList(list);
        lobbyChoice.setItems(names);
    }

}
