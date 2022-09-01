package ch.unibas.dmi.dbis.cs108.gui.dialog;

import ch.unibas.dmi.dbis.cs108.rank.RankEntry;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class HighScoreDialogFX extends Dialog implements HighScoreDialogInterface {

    private TableView<RankEntry> table = new TableView<>();
    private List<RankEntry> highScore = new ArrayList<>();

    public HighScoreDialogFX() {
        initGui();
    }

    private void initGui() {
        BorderPane root = new BorderPane();
        root.setCenter(table);
        setHighScore();
        table.setPrefSize(300, 300);
        getDialogPane().setContent(root);

        //Icon
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        Image img = new Image("/icons/icon.png");
        stage.getIcons().add(img);

        // buttons
        // A CANCEL button has to be added for the X-button to close the dialog.
        // Source: https://stackoverflow.com/questions/52472046/alerts-in-javafx-do-not-close-when-x-button-is-pressed/52472182
        getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
    }

    /**
     * Populates the table with the entries from @param rankList.
     */
    public void setHighScore() {
        ObservableList<RankEntry> ranking = FXCollections.observableArrayList(highScore);
        TableColumn playerNameCol = new TableColumn("Player Name");
        playerNameCol.setCellValueFactory(new PropertyValueFactory<RankEntry, String>("playerName"));

        TableColumn sizeCol = new TableColumn("Size");
        sizeCol.setCellValueFactory(new PropertyValueFactory<RankEntry, String>("size"));

        TableColumn timeCol = new TableColumn("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<RankEntry, String>("time"));

        table.setItems(ranking);
        table.getColumns().setAll(playerNameCol, sizeCol, timeCol);
    }

    /**
     * Receives the @param highScoreList from the client to update the high score table in the high score dialog.
     */
    public void updateHighScoreList(List<RankEntry> highScoreList) {
        highScore = highScoreList;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setHighScore();
            }
        });
    }

    public void showDialog() {
        showAndWait();
    }

}
