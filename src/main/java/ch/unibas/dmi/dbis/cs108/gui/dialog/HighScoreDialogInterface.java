package ch.unibas.dmi.dbis.cs108.gui.dialog;

import ch.unibas.dmi.dbis.cs108.rank.RankEntry;

import java.util.List;

public interface HighScoreDialogInterface {

    public void showDialog();

    void updateHighScoreList(List<RankEntry> highScore);
}
