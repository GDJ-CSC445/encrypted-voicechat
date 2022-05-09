package edu.oswego.cs;

import edu.oswego.cs.network.opcodes.ParticipantOpcode;
import edu.oswego.cs.network.packets.ErrorPacket;
import edu.oswego.cs.network.packets.Packet;
import edu.oswego.cs.network.packets.ParticipantACK;
import edu.oswego.cs.network.packets.ParticipantData;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneController1 {

    public Button listServer;
    public Button backButton;
    public Button RefreshButton;
    public Button JoinServerButton;
    private Parent root;
    private Stage stage;
    private Scene scene;
    public Label connectedToLabel = null;

    public TextField ServerNameTextField;
    public TextField PasswordTextField;
    public Spinner<Integer> NumberOfParticipants;

    @FXML
    public void switchToMainMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainMenu2.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void LeaveRoom(ActionEvent event) throws IOException {
        if (EncryptedVoiceChat.connectedToRoom) {
            ParticipantData participantData1 = new ParticipantData(ParticipantOpcode.LEAVE, EncryptedVoiceChat.port);
            EncryptedVoiceChat.socket.getOutputStream().write(participantData1.getBytes());
            EncryptedVoiceChat.socket.getOutputStream().flush();
        }

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainMenu2.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchCreateServer(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CreateServer.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToServerList(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ListServer.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ListView lv = (ListView) root.getChildrenUnmodifiable().get(0);

        ParticipantData participantData = new ParticipantData(ParticipantOpcode.LIST_SERVERS, EncryptedVoiceChat.port);
        EncryptedVoiceChat.socket.getOutputStream().write(participantData.getBytes());

        byte[] buffer = new byte[1024];

        EncryptedVoiceChat.socket.getInputStream().read(buffer);
        Packet packet = Packet.parse(buffer);
        if (packet instanceof ParticipantACK) {
            ParticipantACK participantACK = (ParticipantACK) packet;
            for (String param : participantACK.getParams()) {
                lv.getItems().add(param);
                EncryptedVoiceChat.chatrooms.add(param);
            }
        } else if (packet instanceof ErrorPacket) {
            ErrorPacket errorPacket = (ErrorPacket) packet;
            ServerConnection.displayError(errorPacket.getErrorOpcode() + "; " + errorPacket.getErrorMsg());
        }


        HBox hBox = new HBox(root);
        scene = new Scene(hBox);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void listViewOnChange(Event event) {
       ListView<String> lv = (ListView<String>) event.getSource();
       EncryptedVoiceChat.selectedRoom = lv.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void switchToActiveChat(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ListServer.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        //root.getChildrenUnmodifiable().get(0).addEventHandler();
        // figure out how to see what is selected in HBox soo that you can pick that item to be sent in packet.

        ListView lv = (ListView) root.getChildrenUnmodifiable().get(0);

        System.out.println(lv.getItems().size());

        String selectedItem = (String) lv.getSelectionModel().getSelectedItem();
        System.out.println("GREG: " + selectedItem);
        ParticipantData participantData = new ParticipantData(ParticipantOpcode.JOIN, EncryptedVoiceChat.port, new String[]{selectedItem});
        EncryptedVoiceChat.socket.getOutputStream().write(participantData.getBytes());

        byte[] buffer = new byte[1024];

        EncryptedVoiceChat.socket.getInputStream().read(buffer);
        Packet packet = Packet.parse(buffer);
        if (packet instanceof ErrorPacket) {
            ErrorPacket errorPacket = (ErrorPacket) packet;
            ServerConnection.displayError(errorPacket.getErrorOpcode() + "; " + errorPacket.getErrorMsg());
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Text("This is a Dialog"));
            Scene dialogScene = new Scene(dialogVbox, 300, 200);

            stage.setScene(dialogScene);
            stage.show();
            return;
        }


        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ActiveChat.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void createServer(ActionEvent event) throws IOException {
        String serverName = ServerNameTextField.getText();
        String numberOfParticipants = String.valueOf(NumberOfParticipants.getValue());
        ParticipantData participantData = new ParticipantData(ParticipantOpcode.CREATE_SERVER, EncryptedVoiceChat.port, new String[]{serverName, numberOfParticipants});
        EncryptedVoiceChat.socket.getOutputStream().write(participantData.getBytes());

        participantData = new ParticipantData(ParticipantOpcode.JOIN, EncryptedVoiceChat.port, new String[]{serverName});
        EncryptedVoiceChat.socket.getOutputStream().write(participantData.getBytes());
        EncryptedVoiceChat.connectedToRoom = true;

        // error check

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ActiveChat.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //update stage when we get new person to join room


    }
}