package ch.unibas.dmi.dbis.cs108.gui;

import ch.unibas.dmi.dbis.cs108.rank.RankEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.util.List;

public class RankGuiFX extends BorderPane {

    private TableView<RankEntry> table = new TableView<>();

    public RankGuiFX() {
        initGui();
    }

    /**
     * Initialises the rank gui
     */
    private void initGui() {
        setCenter(table);
    }

    /**
     * Populates the table with the entries from @param rankList.
     */
    public void setRanking(List<RankEntry> rankingList) {
        ObservableList<RankEntry> ranking = FXCollections.observableArrayList(rankingList);
        TableColumn playerNameCol = new TableColumn("Player Name");
        playerNameCol.setCellValueFactory(new PropertyValueFactory<RankEntry, String>("playerName"));

        TableColumn sizeCol = new TableColumn("Size");
        sizeCol.setCellValueFactory(new PropertyValueFactory<RankEntry, String>("size"));

        TableColumn timeCol = new TableColumn("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<RankEntry, String>("time"));

        table.setItems(ranking);
        table.getColumns().setAll(playerNameCol, sizeCol, timeCol);
    }

}
