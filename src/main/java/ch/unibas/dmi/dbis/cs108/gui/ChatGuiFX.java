package ch.unibas.dmi.dbis.cs108.gui;

import ch.unibas.dmi.dbis.cs108.gui.dialog.ChangeNameDialogFX;
import ch.unibas.dmi.dbis.cs108.gui.dialog.LogInDialogFX;
import ch.unibas.dmi.dbis.cs108.client.Client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * The ChatGuiFX is the gui representation of the Chat with the text area for the messages, the input area to enter
 * messages, as well as the feature to send messages to all player, the players in the lobby, or only one specified
 * player (whisper).
 */
public class ChatGuiFX extends BorderPane implements ChatGuiInterface {

    private static final Logger logger = LogManager.getLogger(ChatGuiFX.class);

    private static final String ALL = "All";
    private static final String LOBBY = "Lobby";
    private TextArea inputArea = new TextArea();
    private TextArea textArea = new TextArea();
    private Client client;
    private LogInDialogFX logInDialog = new LogInDialogFX();
    private Button changeNameButton;
    private ChangeNameDialogFX changeNameDialog = new ChangeNameDialogFX();
    private ChoiceBox<String> toChoiceBox = new ChoiceBox<>();

    public ChatGuiFX(Client client) {
        this.client = client;
        logIn();
        initGui();
    }

    public ChatGuiFX(Client client, String port, String ip, String name) {
        this.client = client;
        logInDialog.setPortNumber(port);
        logInDialog.setIpAddress(ip);
        if (name.length() > 0) {
            logInDialog.setPlayerName(name);
        } else {
            logInDialog.setPlayerName(System.getProperty("user.name"));
        }
        logIn();
        initGui();
    }

    /**
     * Initialises the GUI
     */
    private void initGui() {

        // text area
        textArea.setPrefSize(200, 400);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPadding(new Insets(2, 2, 2, 2));
        ScrollPane textScrollPane = new ScrollPane(textArea);
        textScrollPane.setFitToHeight(true);
        textScrollPane.setFitToWidth(true);
        textScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        textScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        setCenter(textScrollPane);

        // input HBox
        VBox inputHBox = new VBox();
        inputHBox.setSpacing(5);

        Label inputLabel = new Label("To chat, enter text below: ");
        inputHBox.getChildren().add(inputLabel);

        inputArea.setPrefSize(200, 50);
        inputArea.setWrapText(true);
        inputArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                KeyCode pressedKey = ke.getCode();
                if (pressedKey.equals(KeyCode.ENTER)) {
                    sendMessageToClient();
                }
            }
        });
        inputHBox.getChildren().add(inputArea);

        // button VBox
        HBox buttonVBox = new HBox();

        Button sendButton = new Button("Send");
        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                sendMessageToClient();
            }
        });
        buttonVBox.getChildren().add(sendButton);

        Label toLabel = new Label("to: ");
        buttonVBox.getChildren().add(toLabel);

        //client.requestPlayerNames();
        buttonVBox.getChildren().add(toChoiceBox);

        changeNameButton = new Button("Change Name");
        changeNameButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                changePlayerName();
            }
        });
        buttonVBox.getChildren().add(changeNameButton);

        inputHBox.getChildren().add(buttonVBox);
        setBottom(inputHBox);
    }

    /**
     * Sends the text entered by the user from the input area and sends it to the corresponding Client to be further
     * processed. Then sets the input area to an empty String.
     */
    private void sendMessageToClient() {
        String message = inputArea.getText().trim();
        if (message.length() > 0) {
            inputArea.setText("");
            String value = toChoiceBox.getValue();
            logger.info("Value: " + value);
            if (value == null || value.equals(ALL)) {
                client.sendMessageToAll(message);
            } else if (value.equals(LOBBY)) {
                client.sendMessageToLobby(client.getLobbyGui().getLobbyName(), message);
            } else {
                client.sendMessage(client.getPlayerName(), value, message);
            }
        }
    }

    /**
     * {@inheritDoc}
     * Displays the @param message at the bottom of the text area and scrolls down.
     */
    @Override
    public void displayMessage(String message) {
        textArea.appendText(message);

        // scroll text area
    }

    /**
     * {@inheritDoc}
     * Opens the LogInDialog where the player can enter the port number, IP address and player name.
     * Sets the ClientGui of the corresponding Client to this if it is null and starts it.
     */
    @Override
    public void logIn() {
        logInDialog.showDialog();
        if (!logInDialog.isCancelled()) {
            client.setPortNumber(Integer.parseInt(logInDialog.getPortNumber()));
            client.setHost(logInDialog.getIpAddress());
            client.setPlayerName(logInDialog.getPlayerName());
            if (client.getChatGui() == null) {
                client.setChatGui(this);
                client.startClient();
            }
        } else {
            client.closeClient();
        }
    }

    /**
     * {@inheritDoc}
     * The player can change the player name after having logged in successfully by calling the ChangeNameDialog.
     * The new player name is sent via Client to the Server to be validated.
     */
    @Override
    public void changePlayerName() {
        changeNameDialog.showDialog();
        if (!changeNameDialog.isCancelled()) {
            // call Client to ask for a change of the existing player name
            client.askServerToChangePlayerName(changeNameDialog.getPlayerName(), client.getLobbyGui().getLobbyName());
        }
    }

    /**
     * {@inheritDoc}
     * Updates the observable list with all player names (@param playerNames) except the player name of this client
     * as well as an option All. If the player is in a lobby he can also select the option lobby.
     * This is used to switch between the whisper chat which sends a message to a specific player, the lobby chat and
     * the global chat.
     */
    @Override
    public void setPlayerNames(List<String> playerNames) {
        ObservableList<String> names = FXCollections.observableArrayList(playerNames);
        names.add(ALL);
        if (!client.getLobbyGui().getLobbyName().equals("DEFAULT")) {
            names.add(LOBBY);
        }
        names.remove(client.getPlayerName());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                toChoiceBox.setValue(ALL);
                toChoiceBox.setItems(names);
            }
        });
    }

    /**
     * If @param bool is true, then the change name button is disabled, otherwise enabled.
     */
    public void setChangeNameButtonToDisable(boolean bool) {
        changeNameButton.setDisable(bool);
    }

}
