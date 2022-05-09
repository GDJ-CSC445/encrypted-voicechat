package edu.oswego.cs;

import edu.oswego.cs.network.opcodes.ParticipantOpcode;
import edu.oswego.cs.network.packets.Packet;
import edu.oswego.cs.network.packets.ParticipantACK;
import edu.oswego.cs.network.packets.ParticipantData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainMenu2.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        if (EncryptedVoiceChat.connectedToRoom) {
            ParticipantData participantData1 = new ParticipantData(ParticipantOpcode.LEAVE, EncryptedVoiceChat.port);
            EncryptedVoiceChat.socket.getOutputStream().write(participantData1.getBytes());
            EncryptedVoiceChat.socket.getOutputStream().flush();
        }

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
        root.getChildrenUnmodifiable().stream().map(x -> x.getId()).forEach(System.out::println);
        ListView lv = (ListView) root.getChildrenUnmodifiable().get(0);

        ParticipantData participantData = new ParticipantData(ParticipantOpcode.LIST_SERVERS, EncryptedVoiceChat.port);
        EncryptedVoiceChat.socket.getOutputStream().write(participantData.getBytes());
        byte[] buffer = new byte[1024];
        EncryptedVoiceChat.socket.getInputStream().read(buffer);
        Packet packet = Packet.parse(buffer);
        ParticipantACK participantACK = (ParticipantACK) packet;
        for (String param : participantACK.getParams())
            lv.getItems().add(param);

        HBox hBox = new HBox(root);
        scene = new Scene(hBox);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToActiveChat(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ListServer.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        root.getChildrenUnmodifiable().stream().map(x -> x.getId()).forEach(System.out::println);

        //root.getChildrenUnmodifiable().get(0).addEventHandler();
        // figure out how to see what is selected in HBox soo that you can pick that item to be sent in packet.

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ActiveChat.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void createServer(ActionEvent event) throws IOException {
        String serverName = ServerNameTextField.getText();
        ParticipantData participantData = new ParticipantData(ParticipantOpcode.CREATE_SERVER, EncryptedVoiceChat.port, new String[]{serverName});
        EncryptedVoiceChat.socket.getOutputStream().write(participantData.getBytes());
        ParticipantData participantData1 = new ParticipantData(ParticipantOpcode.JOIN, EncryptedVoiceChat.port, new String[]{serverName});
        EncryptedVoiceChat.socket.getOutputStream().write(participantData1.getBytes());
        EncryptedVoiceChat.socket.getOutputStream().flush();
        EncryptedVoiceChat.connectedToRoom = true;

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ActiveChat.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //update stage when we get new person to join room


    }
}