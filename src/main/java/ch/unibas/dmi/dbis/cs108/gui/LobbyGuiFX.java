package ch.unibas.dmi.dbis.cs108.gui;

import static ch.unibas.dmi.dbis.cs108.util.Util.splitParameterAtFirstN;

import ch.unibas.dmi.dbis.cs108.client.Client;
import ch.unibas.dmi.dbis.cs108.gui.dialog.HighScoreDialogFX;
import ch.unibas.dmi.dbis.cs108.gui.dialog.HighScoreDialogInterface;
import ch.unibas.dmi.dbis.cs108.gui.dialog.JoinLobbyDialogFX;
import ch.unibas.dmi.dbis.cs108.gui.dialog.NewLobbyDialogFX;
import ch.unibas.dmi.dbis.cs108.util.Util;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class LobbyGuiFX extends BorderPane implements LobbyGuiInterface {

    private static final int MINIMAL_NUMBER_OF_PLAYERS = 2;

    private Client client;
    private Button joinButton = new Button("Join Lobby");
    private Button newLobbyButton = new Button("New Lobby");
    private Button leaveButton = new Button("Leave Lobby");
    private Button startGameButton = new Button("Start Game");
    //private Button endGameButton = new Button("End Game");
    private Button highScoreButton = new Button("High Score");
    private NewLobbyDialogFX newLobbyDialog = new NewLobbyDialogFX();
    private JoinLobbyDialogFX joinLobbyDialog = new JoinLobbyDialogFX();
    private HighScoreDialogInterface highScoreDialog = new HighScoreDialogFX();
    private TextField lobbyNameField = new TextField();
    private TextArea textList = new TextArea();
    private TextArea playerList = new TextArea();
    private String lobbyName = "DEFAULT";

    public LobbyGuiFX(Client client) {
        this.client = client;
        client.setLobbyGui(this);
        initLobbyGui();
    }

    /**
     * Initialises the GUI with:
     * - a text field where the name of the current lobbies is
     * - a text area which lists all open lobbies when the player is not in a lobby or lists all players in it
     * - a button to create a new lobby only enable when the player is not in a lobby
     * - a button to join an existing lobby only enable when the player is not in a lobby
     * - a button to leave the current lobby only enable when the player is in a lobby
     * - a button to start a new game only available when the player is in a lobby with the minimum players
     * - a button to end the game only when the game is started
     */
    private void initLobbyGui() {

        VBox lobbyBox = new VBox();
        lobbyBox.setSpacing(5);

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(5);

        HBox gameButtons = new HBox();
        gameButtons.setSpacing(5);

        //text field
        lobbyNameField.setText("Start menu: join a lobby");
        lobbyNameField.setEditable(false);
        lobbyNameField.setMaxWidth(200);

        lobbyBox.getChildren().add(lobbyNameField);

        //lobby or player in lobby list
        textList.setText("Joining Server \n\n please wait");
        textList.setEditable(false);
        textList.setMaxWidth(200);

        lobbyBox.getChildren().add(textList);

        //player online list
        playerList.setEditable(false);
        playerList.setMaxWidth(200);

        lobbyBox.getChildren().add(playerList);


        //buttons
        newLobbyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
                newLobby();
            }
        });
        buttonBox.getChildren().add(newLobbyButton);

        joinButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
                joinLobby();
            }
        });
        buttonBox.getChildren().add(joinButton);
        
        leaveButton.setDisable(true);
        leaveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
                leaveLobby();
            }
        });
        buttonBox.getChildren().add(leaveButton);

        startGameButton.setDisable(true);
        startGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
                client.askServerToCreateNewGame(lobbyName);
                startGameButton.setDisable(true);
            }
        });
        gameButtons.getChildren().add(startGameButton);
        /*
        endGameButton.setDisable(true);
        endGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
                client.askServerToEndGame(lobbyName);
                endGameButton.setDisable(true);
            }
        });
        gameButtons.getChildren().add(endGameButton);
        */
        highScoreButton.setDisable(false);
        highScoreButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
                highScoreDialog.showDialog();
            }
        });
        gameButtons.getChildren().add(highScoreButton);

        lobbyBox.getChildren().add(buttonBox);
        lobbyBox.getChildren().add(gameButtons);
        setLeft(lobbyBox);
    }

    /**
     * Opens a newLobbyDialog where the player can enter the lobby name. The client sends then the
     * command to set up new lobby. After joining the new created lobby the new lobby and join button
     * are disabled and the leave button is available.
     */
    private void newLobby() {
        newLobbyDialog.showDialog();
        if (!newLobbyDialog.isCancelled()) {
            lobbyName = newLobbyDialog.getLobbyName().trim();
            client.askServerToSetUpLobby(lobbyName);
            newLobbyButton.setDisable(true);
            joinButton.setDisable(true);
            leaveButton.setDisable(false);
        }
    }

    /**
     * Opens a joinLobbyDialog where the player can select an existing lobby to join. The client sends
     * then the command to join this lobby. After joining the selected lobby the new lobby and join button
     * are disabled and the leave button is available.
     */
    private void joinLobby() {
        joinLobbyDialog.showDialog();
        if (!joinLobbyDialog.isCancelled()) {
            lobbyName = joinLobbyDialog.getLobbyName().trim();
            client.askServerToJoinLobby(lobbyName);
            newLobbyButton.setDisable(true);
            joinButton.setDisable(true);
            leaveButton.setDisable(false);
        }
    }

    /**
     * The client sends the command to leave the current lobby. After leaving the lobby the new lobby and
     * join button are available, the leave button is disabled and the lobby name sets on DEFAULT.
     */
    public void leaveLobby() {
        client.leaveLobby(lobbyName);
        joinButton.setDisable(false);
        newLobbyButton.setDisable(false);
        //endGameButton.setDisable(true);
        startGameButton.setDisable(true);
        leaveButton.setDisable(true);
        lobbyName = "DEFAULT";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayLobby(String lobbyName) {
        if (lobbyName.equals("DEFAULT")) {
            lobbyNameField.setText("Start menu: join a lobby");
        } else {
            lobbyNameField.setText("Lobby: " + lobbyName + "\n");
        }
        this.lobbyName = lobbyName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayLobbyList(String lobbyNames) {
        List<String> list = Util.splitParameter(lobbyNames);
        List<String> lobbyList = new ArrayList<>();
        String lobbyName;
        String state;
        if (this.lobbyName.equals("DEFAULT")) {
            textList.setText("Open Lobbies:\n\n");
            for (String s : list) {
                lobbyName = Util.splitParameterAtFirstN(s, "/", 2)[0];
                state = Util.splitParameterAtFirstN(s, "/", 2)[1];
                textList.appendText(lobbyName + " / " + state + "\n");
                if (state.equals("OPEN")) {
                    lobbyList.add(lobbyName);
                }
            }
        }
        joinLobbyDialog.setLobbyChoice(lobbyList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayPlayerList(String playerNames) {
        if (!lobbyName.equals("DEFAULT")) {
            List<String> playerList = Util.splitParameter(playerNames);
            textList.setText("Players in lobby:\n");
            for (String s : playerList) {
                textList.appendText("- " + s + "\n");
            }
            if (playerList.size() >= MINIMAL_NUMBER_OF_PLAYERS) {
                startGameButton.setDisable(false);
            } else {
                startGameButton.setDisable(true);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayPlayerNames(String playerNames) {
        List<String> list = Util.splitParameter(playerNames);
        Platform.runLater(new Runnable() {
            @Override
            public void run () {
                playerList.setText("Players online: \n");
                for (String s : list) {
                    playerList.appendText(s + "\n");
                }
            }
        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLobbyName() {
        return lobbyName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLobbyName(String lobbyName) {this.lobbyName = lobbyName;}

    /**
     * {@inheritDoc}
     */
    @Override
    public HighScoreDialogInterface getHighScoreDialog() {
        return highScoreDialog;
    }

}
